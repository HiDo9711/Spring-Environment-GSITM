package project.environment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import groovy.util.logging.Slf4j;

@Configuration
@Slf4j
@PropertySource("classpath:/application.properties")
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.add}")
    private String potoAdd;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations(potoAdd);
    }
}