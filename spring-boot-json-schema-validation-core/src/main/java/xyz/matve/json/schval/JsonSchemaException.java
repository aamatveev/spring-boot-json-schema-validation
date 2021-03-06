package xyz.matve.json.schval;

public class JsonSchemaException extends RuntimeException {

    public JsonSchemaException() {
        super();
    }

    public JsonSchemaException(String msg) {
        super(msg);
    }

    public JsonSchemaException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonSchemaException(Throwable cause) {
        super(cause);
    }
}
