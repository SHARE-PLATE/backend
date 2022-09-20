package louie.hanse.shareplate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import louie.hanse.shareplate.exception.type.ShareExceptionType;

public class LatitudeValidator implements ConstraintValidator<ValidLatitude, Double>  {

    @Override
    public boolean isValid(Double latitude, ConstraintValidatorContext context) {
        if (!(33.1 <= latitude && latitude <= 38.45)) {
            context.buildConstraintViolationWithTemplate(
                    ShareExceptionType.OUT_OF_SCOPE_FOR_KOREA.getMessage())
                .addConstraintViolation();
            return false;
        }
        return true;
    }
}
