package ua.com.foxminded.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "university")
public class UniversityProperties {

    private int totalYearsOfStudy;
    private int yearsOfBachelorStudy;
    private int yearsOfMaisterStudy;

    public int getTotalYearsOfStudy() {
        return totalYearsOfStudy;
    }

    public void setTotalYearsOfStudy(int totalYearsOfStudy) {
        this.totalYearsOfStudy = totalYearsOfStudy;
    }

    public int getYearsOfBachelorStudy() {
        return yearsOfBachelorStudy;
    }

    public void setYearsOfBachelorStudy(int yearsOfBachelorStudy) {
        this.yearsOfBachelorStudy = yearsOfBachelorStudy;
    }

    public int getYearsOfMaisterStudy() {
        return yearsOfMaisterStudy;
    }

    public void setYearsOfMaisterStudy(int yearsOfMaisterStudy) {
        this.yearsOfMaisterStudy = yearsOfMaisterStudy;
    }

}
