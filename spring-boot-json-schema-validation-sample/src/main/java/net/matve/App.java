package net.matve;

import net.matve.model.Config;
import net.matve.model.PaymentMethod;
import net.matve.repo.ConfigRepository;
import net.matve.repo.PaymentMethodRepository;
import net.matve.json.schema.JsonRequestBodyPostProcessorBeanPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class App extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    /**
     * tomcat hack for war package
     * @param builder
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(App.class);
    }

    /**
     * initConfigs
     * @param repository
     * @return
     */
    @Bean
    protected CommandLineRunner initConfigs(ConfigRepository repository) {
        return args -> {
            repository.save(new Config("Config 1", new BigDecimal("15.41")));
            repository.save(new Config("Config 2", new BigDecimal("9.69")));
            repository.save(new Config("Config 3", new BigDecimal("47.99")));
        };
    }

    /**
     * initPaymentMethods
     * @param repo
     * @return
     */
    @Bean
    protected CommandLineRunner initPaymentMethods(PaymentMethodRepository repo){
        return args -> {
            repo.saveAll(
                    Stream.of("Visa", "MasterCard", "MIR", "PayPal")
                            .map(PaymentMethod::valueOf)
                            .collect(Collectors.toList())
            );
        };
    }

    /**
     * create bean BeanPostProcessor
     * @return
     */
    // TODO must move to starter
    @Bean
    static BeanPostProcessor jsonRequestBodyPostProcessorBeanPostProcessor() {
        return new JsonRequestBodyPostProcessorBeanPostProcessor();
    }
}
