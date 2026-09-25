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
@Constraint(validatedBy = SizeCharArray.Validator.class)
public @interface SizeCharArray {

    String message() default "size not in range";

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<SizeCharArray, char[]> {
        private int min;
        private int max;

        @Override
        public void initialize(SizeCharArray annotation) {
            this.min = annotation.min();
            this.max = annotation.max();
        }

        @Override
        public boolean isValid(char[] value, ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }
            return value.length >= min && value.length <= max;
        }
    }
}
