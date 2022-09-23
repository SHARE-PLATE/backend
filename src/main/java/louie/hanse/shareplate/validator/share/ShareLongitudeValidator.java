package louie.hanse.shareplate.validator.share;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import louie.hanse.shareplate.exception.type.ShareExceptionType;
import org.springframework.util.ObjectUtils;

public class ShareLongitudeValidator implements ConstraintValidator<ValidShareLongitude, Double> {

    @Override
    public boolean isValid(Double longitude, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if (ObjectUtils.isEmpty(longitude)) {
            context.buildConstraintViolationWithTemplate(
                    ShareExceptionType.EMPTY_SHARE_INFO.getMessage())
                .addConstraintViolation();
            return false;
        }

        if (!(125.06666667 <= longitude && longitude <= 131.87222222)) {
            context.buildConstraintViolationWithTemplate(
                    ShareExceptionType.OUT_OF_SCOPE_FOR_KOREA.getMessage())
                .addConstraintViolation();
            return false;
        }
        return true;
    }
}
