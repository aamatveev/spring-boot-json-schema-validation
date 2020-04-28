package net.matve.json.schema.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
public @interface JsonRequestBody {
    boolean strict() default true;
    String schemaPath() default "";
}
