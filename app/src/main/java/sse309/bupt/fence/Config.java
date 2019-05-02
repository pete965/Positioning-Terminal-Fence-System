package sse309.bupt.fence;

public class Config {
    //空间阈值
    public static double base_threshouldspace = 10; //基础空间阈值
    public static int n_threshouldspace = 2; // 幂函数指数大小 n为正偶数
    //速度阈值
    public static double base_threshouldspeed = 1; //基础速度阈值，是最大阈值的一半
    public static int n_threshouldspeed = 2; //幂函数指数大小 n为正偶数
    //统计系数相关参数
    public static double sigma = 30;
    public static double miu = 0;
    //临界区大小
    public static double delta = 3;
    //边界系数
    public static int n_boundary = 2; //幂函数指数大小 n为正偶数
    //时间系数
    public static int n_time = 2; //幂函数指数大小 n为正偶数
    public static double t_max = 0.5; //极限时间(分钟)
    //危险系数
    public static int gravity_bdi = 1; //bdi的权重
    public static int gravity_bti = 1; //bti的权重
}