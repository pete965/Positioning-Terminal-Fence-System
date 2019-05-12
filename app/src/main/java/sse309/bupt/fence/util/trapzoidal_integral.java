package sse309.bupt.fence.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

import sse309.bupt.fence.Config;

public class trapzoidal_integral {
    public static double trap_method(double lower, double upper, double space) {
        double temp = upper - lower;
        double seg_num = 0;
        if (temp % space == 0) {
            seg_num = temp / space;
        } else {
            seg_num = temp / space + 1;
        }
        double upper_temp = 0;
        double result = 0;
        for (int i = 1; i <= seg_num; i++) {
            if ((upper_temp = i * space + lower) <= upper) {
                result = result + ((2 / (Math.sqrt(2 * Math.PI) * Config.sigma) * Math.exp(-Math.pow((upper_temp - Config.miu), 2) / (2 * Math.pow(Config.sigma, 2)))) +
                        (2 / (Math.sqrt(2 * Math.PI) * Config.sigma) * Math.exp(-Math.pow((upper_temp - space - Config.miu), 2) / (2 * Math.pow(Config.sigma, 2)))))
                        / 2 * space;
            } else {
                result = result + ((2 / (Math.sqrt(2 * Math.PI) * Config.sigma) * Math.exp(-Math.pow((upper - Config.miu), 2) / (2 * Math.pow(Config.sigma, 2)))) +
                        (2 / (Math.sqrt(2 * Math.PI) * Config.sigma) * Math.exp(-Math.pow((upper_temp - space - Config.miu), 2) / (2 * Math.pow(Config.sigma, 2))))) / 2 * space;
            }
        }
        return result;
    }
}
