package com.ghostcat.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GhostDecimalUtils {

    public BigDecimal add(BigDecimal bd1, BigDecimal bd2) {
        if (null == bd1) {
            bd1 = BigDecimal.ZERO;
        }

        if (null == bd2) {
            bd2 = BigDecimal.ZERO;
        }

        return bd1.add(bd2);
    }

    public static BigDecimal sub(BigDecimal bd1, BigDecimal bd2) {
        if (null == bd1) {
            bd1 = BigDecimal.ZERO;
        }

        if (null == bd2) {
            bd2 = BigDecimal.ZERO;
        }

        return bd1.subtract(bd2);
    }

    public BigDecimal divide(BigDecimal bd1, BigDecimal bd2, int scale, RoundingMode roundMode) {

        if (null == bd1 || null == bd2) {
            return BigDecimal.ZERO;
        }

        return bd1.divide(bd2, scale, roundMode);
    }

    public BigDecimal divide(BigDecimal bd1, BigDecimal bd2) {
        if (null == bd1 || null == bd2) {
            return BigDecimal.ZERO;
        }

        return bd1.divide(bd2);
    }

    public BigDecimal multiply(BigDecimal bd1, BigDecimal bd2) {

        if (null == bd1 || null == bd2) {
            return BigDecimal.ZERO;
        }

        return bd1.multiply(bd2);
    }

}
