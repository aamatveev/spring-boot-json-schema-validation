package net.matve.json.schema;

import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.web.context.request.NativeWebRequest;


public interface JsonSchemaResolver {

    /**
     * Resolves json schemas according to method parameter and webrequest. Must always return a resource, if
     * it is unavailable you can send a not found Resource
     */
    Resource resolveJsonSchemaResource(MethodParameter methodParameter, NativeWebRequest webRequest);
}