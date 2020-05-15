package xyz.matve.json.schval;

import org.everit.json.schema.ValidationException;
import org.springframework.validation.Errors;


public interface ValidationExceptionMiddleware {

    /**
     * Does the best effort to convert the validation exception
     * to errors that will be contributed to the {@link Errors} instance
     *
     * @param validationException Everit JSON schema validation errors
     * @param errors              binding errors that will be used in Spring MVC mechanism
     */
    void convert(ValidationException validationException, Errors errors);
}
