package app.spring_webapp_thymeleaf.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.Properties;

/**
 * Configure simple global exception handler serve default /error page
 *
 * In case of an internal error should redirect to default error page "pages/error"
 */
@Configuration
@EnableWebMvc
public class SimpleGlobalExceptionHandling {

    @Bean(name = "simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();

        Properties mappings = new Properties();
        //mappings.setProperty(CannotGetJdbcConnectionException.class.getName(), "pages/error");

        r.setExceptionMappings(mappings);
        r.setDefaultErrorView("pages/error");
        r.setExceptionAttribute("exception");
        return r;
    }
}