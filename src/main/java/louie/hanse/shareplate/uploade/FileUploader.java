package louie.hanse.shareplate.uploade;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class FileUploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${file.upload.location.member}")
    private String fileUploadLocationMember;
    @Value("${file.upload.location.share}")
    private String fileUploadLocationShare;

    private final AmazonS3 amazonS3Client;

    public String uploadShareImage(MultipartFile image) throws IOException {
        return uploadImage(image, fileUploadLocationShare);
    }

    public String uploadMemberImage(MultipartFile image) throws IOException {
        return uploadImage(image, fileUploadLocationMember);
    }

    private String uploadImage(MultipartFile image, String fileUploadLocation) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(image.getContentType());
        objectMetadata.setContentLength(image.getSize());

        String originalImageName = image.getOriginalFilename();
        String ext = originalImageName.substring(originalImageName.lastIndexOf(".") + 1);

        String storeFileName = UUID.randomUUID() + "." + ext;
        String key = fileUploadLocation + "/" + storeFileName;

        try (InputStream inputStream = image.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        }

        return amazonS3Client.getUrl(bucket, key).toString();
    }

}
