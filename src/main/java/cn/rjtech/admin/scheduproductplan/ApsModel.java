package cn.rjtech.admin.scheduproductplan;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：chang
 * @description ：APS 算法数据
 * @date ：2023/3/13 11:03
 */
public class ApsModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 当月日期跨度
     */
    private int apsDate;
    /**
     * 产品类型
     */
    private String[] productType;
    /**
     * 产品类型长度
     */
    private int productTypeLen;
    /**
     * 产品初始库存数
     */
    private Map<String, Integer> inventoryOriginal = new HashMap<>();
    /**
     * 产品每天的计划使用数
     */
    private Map<String, int[]> plan = new HashMap<>();
    /**
     * 产品每天的计划库存数据
     */
    private Map<String, int[]> inventory = new HashMap<>();
    /**
     * 产品最低库存运算天数
     */
    private int inventoryLowestDay;

    /**
     * 产品每天的最低库存数量
     */
    private Map<String, int[]> inventoryLowest = new HashMap<>();
    /**
     * 产品下月平均量
     */
    private Map<String, Integer> planNextMonthAverage = new HashMap<>();
    /**
     * 本次排程工作日信息
     */
    private Weekday[] workday;
    /**
     * 当前日期跨度班次个数
     */
    private int shiftLen;
    /**
     * 产品的班次的产量数据
     */
    private Map<String, int[]> capability = new HashMap<>();
    /**
     * 本次排程初始排班信息，1代表正常两班，2代表白班加班（加班工作量为中班的0.3），3代表三班，4代表周六排班日，5代表周日排班日
     */
    private int[] dateFlag;
    /**
     * 用于标记某一天是否有客户需求，如果所有产品在某一天均无客户需求，这定义其flag 为1，这天不参与库存计算
     */
    private int[] planFlag;

    public ApsModel(String[] productType, Map<String, Integer> inventoryOriginal, Map<String, int[]> plan, int inventoryLowestDay, Map<String, Integer> planNextMonthAverage, Map<String, int[]> capability, Weekday[] workday) {
        //月天数
        int _apsDate = workday.length;
        this.apsDate = _apsDate;
        //产品类型
        this.productType = productType;
        //产品类型长度
        this.productTypeLen = productType.length;
        //各产品期初库存
        this.inventoryOriginal = inventoryOriginal;
        //产品需求计划
        this.plan = plan;
        //最低库存天数
        this.inventoryLowestDay = inventoryLowestDay;

        for (String item : productType) {
            //产品每天的计划库存数据
            this.inventory.put(item, new int[_apsDate]);
            //产品每天的最低库存数量
            this.inventoryLowest.put(item, new int[_apsDate]);
        }
        //产品下月平均量
        this.planNextMonthAverage = planNextMonthAverage;
        //工作日信息
        this.workday = workday;
        // 班次：早班、中班、白班加班（加班工作量为中班的0.3）、晚班
        this.shiftLen = apsDate * 4;
        //产品的班次的产量
        this.capability = capability;

        //1代表星期一到五，4代表星期六，5代表星期日
        int[] _DateFlag = new int[_apsDate];
        for (int i = 0; i < _apsDate; i++) {
            _DateFlag[i] = Weekday.catchName(workday[i]);
        }
        //本次排程初始排班信息，1代表正常两班，2代表白班加班（加班工作量为中班的0.3），3代表三班，4代表周六排班日，5代表周日排班日
        this.dateFlag = _DateFlag;
        //用于标记某一天是否有客户需求，如果所有产品在某一天均无客户需求，这定义其flag 为1，这天不参与库存计算
        this.planFlag = new int[_apsDate];
    }

    public int getApsDate() {
        return apsDate;
    }

    public String[] getProductType() {
        return productType;
    }

    public int getProductTypeLen() {
        return productTypeLen;
    }

    public int getInventoryLowestDay() {
        return inventoryLowestDay;
    }

    public Map<String, int[]> getInventoryLowest() {
        return inventoryLowest;
    }

    public Map<String, int[]> getInventory() {
        return inventory;
    }

    public Map<String, Integer> getInventoryOriginal() {
        return inventoryOriginal;
    }

    public Map<String, int[]> getPlan() {
        return plan;
    }

    public Map<String, Integer> getPlanNextMonthAverage() {
        return planNextMonthAverage;
    }

    public Map<String, int[]> getCapability() {
        return capability;
    }

    public Weekday[] getWorkday() {
        return workday;
    }

    public int[] getDateFlag() {
        return dateFlag;
    }

    public int[] getPlanFlag() {
        return planFlag;
    }

    public int getShiftLen() {
        return shiftLen;
    }

}
