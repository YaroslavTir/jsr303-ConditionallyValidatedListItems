package jsr303;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ymolodkov on 20.07.16.
 */
public class MainForm {

    @NotNull
    private String name;

    @ConditionallyValidatedListItems
    private List<ConditionallyValidated> subforms = new ArrayList<>();

    public MainForm(String name, List<ConditionallyValidated> subforms) {
        this.name = name;
        this.subforms = subforms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ConditionallyValidated> getSubforms() {
        return subforms;
    }

    public void setSubforms(List<ConditionallyValidated> subforms) {
        this.subforms = subforms;
    }

    public static class SubForm implements ConditionallyValidated {

        private String someName;
        @NotNull
        private Integer someInteger;
        @NotNull
        private String  someString;

        private boolean isValidate = false;

        public SubForm(String someName, Integer someInteger, String someString, boolean isValidate) {
            this.someName = someName;
            this.someInteger = someInteger;
            this.someString = someString;
            this.isValidate = isValidate;
        }

        @Override
        public boolean isValidationRequired() {
            return isValidate ;
        }

        public String getSomeName() {
            return someName;
        }

        public void setSomeName(String someName) {
            this.someName = someName;
        }

        public Integer getSomeInteger() {
            return someInteger;
        }

        public void setSomeInteger(Integer someInteger) {
            this.someInteger = someInteger;
        }

        public String getSomeString() {
            return someString;
        }

        public void setSomeString(String someString) {
            this.someString = someString;
        }

        public boolean isValidate() {
            return isValidate;
        }

        public void setIsValidate(boolean isValidate) {
            this.isValidate = isValidate;
        }
    }
}
