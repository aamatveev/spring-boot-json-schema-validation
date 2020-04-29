package net.matve;

import net.matve.json.schema.JsonRequestBodyPostProcessorBeanPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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