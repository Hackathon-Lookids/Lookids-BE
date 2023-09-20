package hanghackathon.lookids.global;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Util {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public List<String> uploadFile(List<MultipartFile> multipartFileList) throws IOException {

        List<String> imageUrlList = new ArrayList<>();
        for (MultipartFile image : multipartFileList) {
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            log.info(fileName);

            try {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(image.getSize());
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, image.getInputStream(), metadata));
            } catch (IOException e) {
                throw new RuntimeException("failed to upload image : " + fileName, e);
            }
            imageUrlList.add(amazonS3.getUrl(bucket, fileName).toString());

        }
        return imageUrlList;
    }
}
