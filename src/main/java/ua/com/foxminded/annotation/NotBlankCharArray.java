package ua.com.foxminded.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankCharArray.Validator.class)
public @interface NotBlankCharArray {
    String message() default "must not be blank";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<NotBlankCharArray, char[]> {
        @Override
        public boolean isValid(char[] value, ConstraintValidatorContext context) {
            if (value == null || value.length == 0)
                return false;
            for (char c : value) {
                if (!Character.isWhitespace(c))
                    return true;
            }
            return false;
        }
    }
}
