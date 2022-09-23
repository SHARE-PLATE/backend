package louie.hanse.shareplate.validator.share;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import louie.hanse.shareplate.exception.type.ShareExceptionType;
import org.springframework.util.ObjectUtils;

public class ShareLatitudeValidator implements ConstraintValidator<ValidShareLatitude, Double> {

    @Override
    public boolean isValid(Double latitude, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if (ObjectUtils.isEmpty(latitude)) {
            context.buildConstraintViolationWithTemplate(
                    ShareExceptionType.EMPTY_SHARE_INFO.getMessage())
                .addConstraintViolation();
            return false;
        }

        if (!(33.1 <= latitude && latitude <= 38.45)) {
            context.buildConstraintViolationWithTemplate(
                    ShareExceptionType.OUT_OF_SCOPE_FOR_KOREA.getMessage())
                .addConstraintViolation();
            return false;
        }
        return true;
    }
}
