package net.matve;

import net.matve.json.schema.JsonSchemaValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // error handle for @JsonRequestBody
    @ExceptionHandler(value = {JsonSchemaValidationException.class})
    protected ResponseEntity<Object> handleJsonSchemaValidationException(JsonSchemaValidationException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST.value());

        //Get all errors
        Map<String, Object> errorsData = new LinkedHashMap<>();
        List<FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors();
        fieldErrors.stream().forEach((fieldError) -> {
            errorsData.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        body.put("errors", errorsData);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // error handle for @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        //Get all errors
        Map<String, Object> errorsData = new LinkedHashMap<>();
        List<FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors();
        fieldErrors.stream().forEach((fieldError) -> {
            errorsData.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        body.put("errors", errorsData);

        return new ResponseEntity<>(body, headers, status);

    }

}
