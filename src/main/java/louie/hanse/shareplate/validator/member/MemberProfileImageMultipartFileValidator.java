package louie.hanse.shareplate.validator.member;

import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import louie.hanse.shareplate.exception.type.MemberExceptionType;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public class MemberProfileImageMultipartFileValidator implements ConstraintValidator<ValidMemberProfileImage, MultipartFile> {

    private static final List<String> enableContentTypes = createEnableContentTypes();

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
//        context.disableDefaultConstraintViolation();

        String contentType = multipartFile.getContentType();

        if (!enableContentTypes.contains(contentType)) {
            context.buildConstraintViolationWithTemplate(
                MemberExceptionType.NOT_SUPPORT_IMAGE_TYPE.getMessage())
                .addConstraintViolation();
            return false;
        }
        return true;
    }

    private static List<String> createEnableContentTypes() {
        return List.of(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE);
    }
}
