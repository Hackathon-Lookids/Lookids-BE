package hanghackathon.lookids.global;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Util {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.endpoint}")
    private String endPoint;

    public List<String> uploadFile(List<MultipartFile> multipartFileList) throws IOException {

        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();

        String bucketName = "lookids";

        try {
            // create bucket if the bucket name does not exist
            if (!s3.doesBucketExistV2(bucketName)) {
                s3.createBucket(bucketName);
            }
        } catch (SdkClientException e) {
            e.printStackTrace();
        }

        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        int millis = now.get(ChronoField.MILLI_OF_SECOND);

        List<String> imageUrlList = new ArrayList<>();
        if (multipartFileList == null) {
            return null;
        } else {
            for (MultipartFile image : multipartFileList) {
                if (image == null || image.isEmpty()) {
                    imageUrlList.add(null);
                } else {
//            String imageName = "image" + hour + minute + second + millis;
                    String imageName = "image" + UUID.randomUUID();
                    String fileExtension = '.' + image.getOriginalFilename().replaceAll("^.*\\.(.*)$", "$1");
                    String fullImageName = "S3" + imageName + fileExtension;

                    ObjectMetadata objectMetadata = new ObjectMetadata();
                    objectMetadata.setContentType(image.getContentType());
                    objectMetadata.setContentLength(image.getSize());

                    InputStream inputStream = image.getInputStream();

                    s3.putObject(new PutObjectRequest(bucketName, fullImageName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

                    imageUrlList.add(s3.getUrl(bucketName, fullImageName).toString());
                }
            }
            return imageUrlList;
        }
    }
}
