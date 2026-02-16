package ua.com.foxminded;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MainApp {
    
    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }

}
