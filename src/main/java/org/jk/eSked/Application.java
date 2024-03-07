package org.jk.eSked;

import com.vaadin.flow.server.InitParameters;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@WebServlet(urlPatterns = "/*", name = "slot", asyncSupported = true, loadOnStartup = 1,
        initParams = { @WebInitParam(name = InitParameters.I18N_PROVIDER, value = "com.vaadin.example.ui.TranslationProvider") })

public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
