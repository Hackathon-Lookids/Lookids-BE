package hanghackathon.lookids.global;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
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
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.endpoint}")
    private String endPoint;

    public List<String> uploadFile(List<MultipartFile> multipartFileList) {

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
        } catch(SdkClientException e) {
            e.printStackTrace();
        }

        List<String> imageUrlList = new ArrayList<>();
        for (MultipartFile image : multipartFileList) {
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            log.info(fileName);

            try {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(image.getSize());
                s3.putObject(new PutObjectRequest(bucketName, fileName, image.getInputStream(), metadata));
            } catch (IOException e) {
                throw new RuntimeException("failed to upload image : " + fileName, e);
            }
            imageUrlList.add(s3.getUrl(bucketName, fileName).toString());
        }
        return imageUrlList;
    }
}
