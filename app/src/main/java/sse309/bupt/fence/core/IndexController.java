package sse309.bupt.fence.core;

import android.util.Log;

import java.util.Date;

import sse309.bupt.fence.Config;
import sse309.bupt.fence.bean.BreakInfo;
import sse309.bupt.fence.bean.User;
import sse309.bupt.fence.util.Util;
import sse309.bupt.fence.util.trapzoidal_integral;

public class IndexController {
    private static final String TAG = "FENCELOG";

    /**
     * 计算风险系数
     *
     * @param si  统计系数 statisticIndex
     * @param bdi 边界距离系数 boundaryDistanceIndex
     * @param bti 边界时间系数 houndaryTimeIndex
     * @return ri 风险系数
     */
    public static double caculateRiskIndex(double si, double bdi,
                                           double bti) {
        Log.i(TAG, "------------------RiskIndex-----------------");
        Log.i(TAG, "-------------------config-----------------");
        Log.i(TAG, "gravity_bdi = " + Config.gravity_bdi + "  gravity_bti = " + Config.gravity_bti);

        double riskIndex = (Config.gravity_bdi * bdi + Config.gravity_bti * bti) /
                (Config.gravity_bti + Config.gravity_bdi) * (1 + si);
        if (riskIndex > 1)
            riskIndex =1;
        Log.i(TAG, "riskIndex = " + riskIndex);

        return riskIndex;
    }

    /**
     * 计算历史统计系数si
     *
     * @return
     */
    public static double cacalateStatisticIndex() {
        double statisticIndex = 0;
        double t = 0;
        Log.i(TAG, "------------------StatisticIndex-----------------");
        Log.i(TAG, "------------------config-----------------");
        Log.i(TAG, "miu:" + Config.miu + "   sigma:" + Config.sigma);
        for (BreakInfo breakInfo : User.getInstance().getBreakInfos()) {
            t = Util.caculateTime(new Date(System.currentTimeMillis()),
                    new Date(breakInfo.getBreakTime()));
            Log.i(TAG, "t:" + t);
            // 计算统计系数
            double statisticNum = 0;
            if (t < 10 * Config.sigma) {
                statisticNum = trapzoidal_integral.trap_method(-10 * Config.sigma, -t, 0.01);
            }
            Log.i(TAG, "statisticNum:" + statisticNum);
            statisticIndex = statisticIndex + statisticNum * (1 - statisticIndex);
            Log.i(TAG, "statisticIndex:" + statisticIndex);

        }
        return statisticIndex;
    }

    /**
     * 计算边界距离系数
     *
     * @param boundaryDistance 边界距离
     * @return bdi边界距离系数
     */
    public static double caculateBoundaryDistanceIndex(double boundaryDistance, double threshouldSpace) {
        Log.i(TAG, "-----------------BsoundaryDistanceIndex-----------------");
        Log.i(TAG, "----------------------config-------------------");
        Log.i(TAG, "boundaryDistance = " + boundaryDistance);
        Log.i(TAG, "threshouldSpace:" + threshouldSpace + "   n_boundary:" + Config.n_boundary);

        if (boundaryDistance >= Config.base_threshouldspace + threshouldSpace) {
            Log.i(TAG, "boundaryIndex = 0");
            return 0;
        } else if (boundaryDistance <= 0) {
            Log.i(TAG, "boundaryIndex = 1");
            return 1;
        } else {
            //这里需要考虑用什么函数
            //假设为幂函数
            double tempDistanceIndex = Math.pow((boundaryDistance - Config.base_threshouldspace - threshouldSpace), Config.n_boundary);
            Log.i(TAG, "tempDistanceIndex = " + tempDistanceIndex);

            //归一化之后的边界系数
            tempDistanceIndex = tempDistanceIndex / Math.pow((Config.base_threshouldspace + threshouldSpace), Config.n_boundary);
            Log.i(TAG, "DistanceIndex = " + tempDistanceIndex);

            return tempDistanceIndex;
        }
    }

    /**
     * 计算边界时间系数
     *
     * @param boundaryTime 边界时间
     * @return bti 边界时间系数
     */
    public static double caculateBoundaryTimeIndex(double boundaryTime) {
        Log.i(TAG, "-----------------BoundaryTimeIndex-----------------");
        Log.i(TAG, "----------------------config-------------------");
        Log.i(TAG, "n_time = " + Config.n_time);
        Log.i(TAG, "t_max = " + Config.t_max);

        double t_max_num = Config.t_max * ((double) 1 / 24 / 60);
        Log.i(TAG, "t_max_num = " + Config.t_max * ((double) 1 / 24 / 60));

        Log.i(TAG, "boundaryTime = " + boundaryTime);
        if (boundaryTime >= t_max_num)
            return 1;
        double boundaryTimeIndex = Math.pow(boundaryTime, Config.n_time) /
                (Math.pow(t_max_num, Config.n_time));
        Log.i(TAG, "boundaryTimeIndex = " + boundaryTimeIndex);

        return boundaryTimeIndex;
    }

    /**
     * 计算空间阈值
     *
     * @param riskIndex 风险系数
     * @return threshouldSpace 空间阈值
     */
    public static double caculateThreshouldSpace(double riskIndex) {
        Log.i(TAG, "-----------------ThreshouldSpace-----------------");
        Log.i(TAG, "----------------------config-------------------");
        Log.i(TAG, "base_threshouldspace = " + Config.base_threshouldspace +
                "  n_threshouldspace = " + Config.n_threshouldspace);

        double threshouldSpace = Config.base_threshouldspace *
                (-Math.pow(riskIndex, Config.n_threshouldspace) + 1);
        Log.i(TAG, "threshouldSpace = " + threshouldSpace);

        return threshouldSpace;
    }

    /**
     * 计算速度阈值
     *
     * @param riskIndex 风险系数
     * @return threshouldSpeed 速度阈值
     */
    public static double caculateThreshouldSpeed(double riskIndex) {
        Log.i(TAG, "-----------------ThreshouldSpeed-----------------");
        Log.i(TAG, "----------------------config-------------------");
        Log.i(TAG, "base_threshouldspeed = " + Config.base_threshouldspeed +
                "  n_threshouldspeed = " + Config.n_threshouldspeed);

        double threshouldSpeed = Config.base_threshouldspeed *
                (-Math.pow(riskIndex, Config.n_threshouldspeed) + 1);
        Log.i(TAG, "threshouldSpeed = " + threshouldSpeed);

        return threshouldSpeed;
    }

}
