package ua.com.foxminded.entity;

import javax.persistence.Embeddable;

@Embeddable
public class YearOfUniversity {

    private int yearsOfStudy;
    private int curentYearInUniv;

    public YearOfUniversity(int curentYearInUniv) {
        if (curentYearInUniv < 1 || curentYearInUniv > 6) {
            throw new IllegalArgumentException();
        }
        this.curentYearInUniv = curentYearInUniv;
    }

    public int calculateYearOfUniversity() {
        int yearsLeft;
        yearsLeft = yearsOfStudy - curentYearInUniv;
        int currentYear;
        currentYear = yearsOfStudy - yearsLeft;
        return currentYear;

    }
}
