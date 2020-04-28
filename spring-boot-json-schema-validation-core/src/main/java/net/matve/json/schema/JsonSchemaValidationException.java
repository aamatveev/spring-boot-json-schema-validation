package net.matve.json.schema;

import org.springframework.validation.Errors;

public class JsonSchemaValidationException extends JsonSchemaException {
    private Errors errors;

    public JsonSchemaValidationException(Errors errors) {
        this.errors = errors;
    }

    public Errors getBindingResult() {
        return this.errors;
    }
}
