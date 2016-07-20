package jsr303;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

@Target( { ElementType.METHOD, ElementType.FIELD })
@Constraint(validatedBy = ConditionallyValidatedListItemsValidator.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface ConditionallyValidatedListItems {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
