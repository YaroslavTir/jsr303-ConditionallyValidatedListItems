package jsr303;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class MainFormTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldSkipValidationOfSubForm() {
        List<ConditionallyValidated> subForms = Stream.of(
                new MainForm.SubForm("text", null, "text", false),
                new MainForm.SubForm("text", 1, "text", false),
                new MainForm.SubForm("text", null, null, false))
                .collect(Collectors.toList());
        MainForm mainForm = new MainForm("name", subForms);
        Set<ConstraintViolation<MainForm>> validate = validator.validate(mainForm);
        assertEquals( 0, validate.size() );
    }

    @Test
    public void shouldValidateSubForm() {
        List<ConditionallyValidated> subForms = Stream.of(
                new MainForm.SubForm("text", null, "text", true),
                new MainForm.SubForm("text", 1, "text", true),
                new MainForm.SubForm("text", 1, null, true))
            .collect(Collectors.toList());
        MainForm mainForm = new MainForm("name", subForms);
        Set<ConstraintViolation<MainForm>> validate = validator.validate(mainForm);
        assertEquals( 2, validate.size() );
    }

}