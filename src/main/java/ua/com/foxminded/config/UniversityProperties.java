package ua.com.foxminded.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "university")
public class UniversityProperties {

    private int totalYearsOfStudy;

    public int getTotalYearsOfStudy() {
        return totalYearsOfStudy;
    }

    public void setTotalYearsOfStudy(int totalYearsOfStudy) {
        this.totalYearsOfStudy = totalYearsOfStudy;
    }


}
