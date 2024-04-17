package yeonba.be.util;

import java.time.LocalDate;
import java.time.Period;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AgeValidator {

    private static final int ADULT_AGE = 18;

    public static boolean isNotAdult(LocalDate birth, LocalDate currentDate) {

        int age = Period.between(birth, currentDate).getYears();

        return age < ADULT_AGE;
    }
}
