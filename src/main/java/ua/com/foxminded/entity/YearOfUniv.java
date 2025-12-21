package ua.com.foxminded.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class YearOfUniv {

    @Column(name = "year_of_univ")
    private int curentYearInUniv;
    
    private int totalYearsOfStudy;

    public YearOfUniv(int curentYearInUniv) {
        if (curentYearInUniv < 1 || curentYearInUniv > 6) {
            throw new IllegalArgumentException();
        }
        this.curentYearInUniv = curentYearInUniv;
    }

    public int calculateYearOfUniversity() {
        int yearsLeft;
        yearsLeft = totalYearsOfStudy - curentYearInUniv;
        int currentYear;
        currentYear = totalYearsOfStudy - yearsLeft;
        return currentYear;

    }
}
