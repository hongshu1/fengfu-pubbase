package cn.rjtech.admin.scheduproductplan;

import java.util.Map;

/**
 * @author ：
 * @description ：APS 排程
 * @date ：2023/3/13 13:09
 */
public class ApsUtil {


    /**
     * APS 排程处理
     *
     * @param productType          参与排程的产品类型     物料
     * @param inventoryOriginal    产品的初始库存数      期初库存
     * @param plan                 产品每天的计划使用数   计划需求数
     * @param inventoryLowestDay   产品最低库存运算天数   最低在库天数
     * @param planNextMonthAverage 产品下月平均量        下月平均需求数量
     * @param capability           产品的班次的产量数据   各物料各班次产量数据
     * @param workday              本次排程工作日信息     月星期几
     * @param inventoryRate        库存率因子
     * @param workFactor           加班因子             加班小时数
     */
    public static ApsScheduling apsCalculation(String[] productType,
                                               Map<String, Integer> inventoryOriginal,
                                               Map<String, int[]> plan,
                                               int inventoryLowestDay,
                                               Map<String, Integer> planNextMonthAverage,
                                               Map<String, int[]> capability,
                                               Weekday[] workday,
                                               double inventoryRate,
                                               double workFactor) {
        // 1、初始化算法数据
        ApsScheduling apsCalculation = new ApsCalculation(productType, inventoryOriginal, plan, inventoryLowestDay, planNextMonthAverage, capability, workday, inventoryRate, workFactor);
        // 2、库存计划审查，是否参与库存运算检测 (标记某一天是否有客户需求，如果所有产品在某一天均无客户需求，则定义planFlag为1，这天不参与库存计算)
        apsCalculation.inventoryPlanReview();
        // 3、计算每天的最低库存量
        //计算后 inventoryLowestDay 天的最低出库数量要求，统计最低库存要求。
        //找到当天之后的 inventoryLowestDay 个有效天的客户需求量。
        //当某一天的有效客户需求量为-1 时，取 下月平均量，否者取有效的客户需求量，进行求和计算出最低出库数量
        apsCalculation.inventoryLowestCalculation();
        // 4、进行排程
        for (int process = 0, apsProcess = 0; process <= 1000; process++) {
            // 4.1、排班处理、生产计划检查、排产、调整班次
            apsProcess = apsCalculation.apsArrangement();
            // 4.2、为0时排程成功
            if (apsProcess == 0)
                break;
        }
        // 5、排程优化
        apsCalculation.schedulingOptimization();
        // 6、排程结果检查
        apsCalculation.schedulingCheck();
        return apsCalculation;
    }

}
