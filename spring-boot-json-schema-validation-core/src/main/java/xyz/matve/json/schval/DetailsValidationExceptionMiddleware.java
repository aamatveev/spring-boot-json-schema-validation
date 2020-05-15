package xyz.matve.json.schval;

import org.everit.json.schema.ValidationException;
import org.springframework.validation.Errors;

import java.util.stream.Stream;

public class DetailsValidationExceptionMiddleware implements ValidationExceptionMiddleware {
    /**
     * Does the best effort to convert the validation exception
     * to errors that will be contributed to the {@link Errors} instance
     *
     * @param validationException Everit JSON schema validation errors
     * @param errors              binding errors that will be used in Spring MVC mechanism
     */
    public void convert(ValidationException validationException, Errors errors) {
        processException(validationException, errors);
    }

    private void processException(ValidationException validationException, Errors errors){
        String schemaLocation = validationException.getSchemaLocation();
        if(validationException.getKeyword() != null) {
            // TODO need define all of errorCodes
            Stream.of(
                    "exclusiveMinimum",
                    "minimum",
                    "exclusiveMaximum",
                    "maximum"
            )
                    .forEach(errorCode -> {
                        if(errorCode.equals(validationException.getKeyword())){
                            // TODO need test this var
                            String field = validationException
                                    .getPointerToViolation()
                                    .replace("#/", "")
                                    .replace("/", ".");
                            errors.rejectValue(field, errorCode, validationException.getErrorMessage());
                        }
                    });

            if ("required".equals(validationException.getKeyword())) {
                parseRequired(validationException, errors);
            }
        }
        for (ValidationException nestedException : validationException.getCausingExceptions()) {
            // recursion
            processException(nestedException, errors);
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
        String pointerToViolation = exception.getPointerToViolation();
        if (pointerToViolation.contains("/")) {
            String path = pointerToViolation.substring(pointerToViolation.lastIndexOf("/") + 1);
            errors.setNestedPath(path);
        }
        errors.rejectValue(field, "required-field", "Field is required");
    }
}
