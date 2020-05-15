package xyz.matve.json;

import xyz.matve.json.schval.JsonRequestBodyPostProcessorBeanPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonSchemaValidationAutoconfiguration {

    /**
     * create bean BeanPostProcessor
     * @return
     */
    @Bean
    public BeanPostProcessor jsonRequestBodyPostProcessorBeanPostProcessor() {
        return new JsonRequestBodyPostProcessorBeanPostProcessor();
    }
}