package cn.rjtech.admin.scheduproductplan;


public interface ApsScheduling {

    /**
     * 库存计划审查。
     */
    void inventoryPlanReview();

    /**
     * 计算后两天的最低出库数量要求，统计最低库存要求。
     */
    void inventoryLowestCalculation();

    /**
     * 排程处理
     *
     * @return 0: 排程成功，1：需要加班，重新排程
     */
    int apsArrangement();

    /**
     * 对某一天进行生产计划检查
     *
     * @param dateNumber 具体哪一天
     * @return 0：正常工作，1：需要加班
     */
    int inventoryCheck(int dateNumber);

    /**
     * 计算库存数据
     *
     * @param dateNumber 当前排班天数
     */
    void apsCalculation(int dateNumber);

    /**
     * 当前排班不能满足时，用于调整班次
     *
     * @param dateNumber 当前排班天数
     */
    void apsModify(int dateNumber);

    /**
     * 排班结果的优化
     */
    void schedulingOptimization();

    /**
     * 排班结果检查
     */
    void schedulingCheck();

    /**
     * 排程结束、结果输出
     *
     * @param productInformationByShift 指定班次所有日期的产品信息
     * @param productNumberByShift      指定班次所有日期的产品数量
     * @param shiftIndex                指定哪个班次的信息和数量,0:早班，1：中班,2：加班，3:晚班
     */
    void getProductInfo(String[] productInformationByShift, int[] productNumberByShift, int shiftIndex);

}
