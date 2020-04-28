package net.matve.json.schema;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

public class JsonRequestBodyPostProcessorBeanPostProcessor implements BeanPostProcessor {
    private JsonSchemaResolver jsonSchemaResolver;
    private ValidationExceptionMiddleware validationExceptionMiddleware;

    public JsonRequestBodyPostProcessorBeanPostProcessor() {
        this.jsonSchemaResolver = new ParamNameJsonSchemaResolver();
        this.validationExceptionMiddleware = new DetailsValidationExceptionMiddleware();
    }

    public JsonRequestBodyPostProcessorBeanPostProcessor(JsonSchemaResolver jsonSchemaResolver, ValidationExceptionMiddleware validationExceptionMiddleware) {
        this.jsonSchemaResolver = jsonSchemaResolver;
        this.validationExceptionMiddleware = validationExceptionMiddleware;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RequestMappingHandlerAdapter) {
            RequestMappingHandlerAdapter handlerAdapter = (RequestMappingHandlerAdapter) bean;
            List<HandlerMethodArgumentResolver> argumentResolvers = handlerAdapter.getArgumentResolvers();
            List<HandlerMethodArgumentResolver> extendedArgumentResolverList = new ArrayList<>(argumentResolvers);
            for (int i = 0; i < argumentResolvers.size(); i++) {
                HandlerMethodArgumentResolver argumentResolver = argumentResolvers.get(i);
                if (argumentResolver instanceof RequestResponseBodyMethodProcessor) {
                    JsonRequestBodyArgumentResolver jsonRequestBodyArgumentResolver
                            = new JsonRequestBodyArgumentResolver(
                            (RequestResponseBodyMethodProcessor) argumentResolver,
                            jsonSchemaResolver, validationExceptionMiddleware);
                    extendedArgumentResolverList.add(i + 1, jsonRequestBodyArgumentResolver);
                    break;
                }
            }
            handlerAdapter.setArgumentResolvers(extendedArgumentResolverList);
        }
        return bean;
    }
}
