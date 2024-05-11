package yeonba.be.util;

import java.time.LocalDate;
import java.time.Period;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UserException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AgeValidator {

    public static void validateAgeByBirth(LocalDate birth, LocalDate currentDate) {

        int ageLowerBound = 20;
        int ageUpperBound = 40;
        int age = Period.between(birth, currentDate).getYears();

        boolean isLessThanLowerBound = age < ageLowerBound;
        boolean isGreaterThanUpperBound = age > ageUpperBound;

        if (isLessThanLowerBound || isGreaterThanUpperBound) {
            throw new GeneralException(UserException.AGE_OUT_OF_RANGE);
        }
    }
}
