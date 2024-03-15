package org.jk.eSked;

import com.vaadin.flow.server.InitParameters;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@WebServlet(urlPatterns = "/*", name = "slot", asyncSupported = true, loadOnStartup = 1,
        initParams = { @WebInitParam(name = InitParameters.I18N_PROVIDER, value = "com.vaadin.example.ui.TranslationProvider") })
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
