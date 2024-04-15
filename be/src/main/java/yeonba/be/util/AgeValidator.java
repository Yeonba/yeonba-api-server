package yeonba.be.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AgeValidator {

    private static final int ADULT_AGE = 18;

    public static boolean isNotAdult(LocalDate birth, LocalDate now) {

        long age = ChronoUnit.YEARS.between(birth, now);

        return age < ADULT_AGE;
    }
}
