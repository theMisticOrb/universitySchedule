package ua.com.foxminded.entity;

import java.time.LocalDate;
import java.time.Period;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class YearOfUniv {

    @Column(name = "year_of_univ")
    private int curentYearInUniv;


    protected YearOfUniv() {
    }

    public YearOfUniv(LocalDate startDate, int totalYears) {
        int calculatedYear = calculateCurrentYear(startDate);
        if (calculatedYear < 1 || calculatedYear > totalYears) {
            throw new IllegalArgumentException(
                "Computed year " + calculatedYear + " outside valid range 1.." + totalYears);
        }
        this.curentYearInUniv = calculatedYear;
    }
    
    public YearOfUniv(int manualYear, int totalYears) {
        if (manualYear < 1 || manualYear > totalYears) {
            throw new IllegalArgumentException("Year must be between 1 and " + totalYears);
        }
        this.curentYearInUniv = manualYear;
    }

    public int getCurentYearInUniv() {
        return curentYearInUniv;
    }

    private static int calculateCurrentYear(LocalDate startDate) {
        return Period.between(startDate, LocalDate.now()).getYears() + 1;
    }
}
