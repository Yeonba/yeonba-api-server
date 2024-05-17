package yeonba.be.util;

import java.util.Objects;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UserException;

public class BoundsValidator {

    public static void validateBounds(Integer lowerBound, Integer upperBound) {

        if (Objects.isNull(lowerBound) || Objects.isNull(upperBound)) {

            return;
        }

        if (lowerBound > upperBound) {
            throw new GeneralException(UserException.LOWER_BOUND_LESS_THAN_OR_EQUAL_UPPER_BOUND);
        }
    }
}
