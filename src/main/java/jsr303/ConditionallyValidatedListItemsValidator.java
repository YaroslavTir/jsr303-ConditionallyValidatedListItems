package jsr303;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.util.ReflectionUtils;

import javax.validation.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

public class ConditionallyValidatedListItemsValidator implements ConstraintValidator<ConditionallyValidatedListItems, Object> {

    public static final String PROPERTY_PATH = "propertyPath";

    @Override
    public void initialize(ConditionallyValidatedListItems constraintAnnotation) {
        //do nothing
    }

    @Override
    public boolean isValid(Object form, ConstraintValidatorContext context) {
        boolean result = true;
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        if (form instanceof List) {
            List formList = (List) form;
            for (int index = 0; index < formList.size(); index++) {
                Object formListItem = formList.get(index);
                if (formListItem instanceof ConditionallyValidated) {
                    ConditionallyValidated conditionallyValidated = (ConditionallyValidated) formListItem;
                    if (conditionallyValidated.isValidationRequired()) {
                        Set<ConstraintViolation<ConditionallyValidated>> validate = validator.validate(conditionallyValidated);
                        this.addViolationsToContext(context, index, validate);
                        if (validate.size() > 0) result = false;
                    }
                }
            }
        }
        return result;
    }

    private void addViolationsToContext(ConstraintValidatorContext context, Integer propertyIndex, Set<ConstraintViolation<ConditionallyValidated>> violations) {
        context.disableDefaultConstraintViolation();
        violations.forEach(violation -> {
            ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder =
                    context.buildConstraintViolationWithTemplate(violation.getMessageTemplate());
            if (propertyIndex != null) {
                this.setIndexToPropertyPath(violationBuilder, propertyIndex);
            }
            violation.getPropertyPath().forEach(node -> violationBuilder.addNode(node.getName()));
            violationBuilder.addConstraintViolation();
        });
    }

    private void setIndexToPropertyPath(ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder, Integer index) {
        Field field = ReflectionUtils.findField(violationBuilder.getClass(), PROPERTY_PATH);
        ReflectionUtils.makeAccessible(field);
        Object path = ReflectionUtils.getField(field, violationBuilder);
        if (path instanceof PathImpl) {
            PathImpl pathImpl = (PathImpl) path;
            pathImpl.setLeafNodeIndex(index);
        }
    }

}
