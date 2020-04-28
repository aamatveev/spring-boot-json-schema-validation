package net.matve.json.schema;

import org.everit.json.schema.ValidationException;
import org.springframework.validation.Errors;

public class DetailsValidationExceptionMiddleware implements ValidationExceptionMiddleware {
    /**
     * Does the best effort to convert the validation exception
     * to errors that will be contributed to the {@link Errors} instance
     * @param validationException Everit JSON schema validation errors
     * @param errors binding errors that will be used in Spring MVC mechanism
     */
    public void convert(ValidationException validationException, Errors errors) {
        if("exclusiveMinimum".equals(validationException.getKeyword())) {
            String schemaLocation = validationException.getSchemaLocation();
            String field = schemaLocation.substring(schemaLocation.lastIndexOf("/") + 1);
            errors.rejectValue(field, "exclusive-minimum", validationException.getErrorMessage());
        }
        if("required".equals(validationException.getKeyword())) {
            String schemaLocation = validationException.getSchemaLocation();
            parseRequired(validationException, errors);
        }
        for (ValidationException nestedException : validationException.getCausingExceptions()) {
            if(isRequired(nestedException)) {
                parseRequired(nestedException, errors);
                continue;
            }
            errors.reject("reject", validationException.getErrorMessage());
        }
    }

    private boolean isRequired(ValidationException exception) {
        return exception.getErrorMessage().startsWith("required key");
    }

    private void parseRequired(ValidationException exception, Errors errors) {
        String message = exception.getMessage();
        int leftBracket = message.indexOf("[");
        int rightBracket = message.indexOf("]");
        String field = message.substring(leftBracket + 1, rightBracket);
        String schemaLocation = exception.getSchemaLocation();
        if (schemaLocation.contains("/")) {
            String path = schemaLocation.substring(schemaLocation.lastIndexOf("/") + 1);
            errors.setNestedPath(path);
        }
        errors.rejectValue(field, "required-field", "Field is required");
    }
}
