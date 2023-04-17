package cn.rjtech.admin.scheduproductplan;

import cn.hutool.core.util.ArrayUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 本APS排程的整体思路如下：
 * 1、参与排程的生产线可能涉及多种不同的产品型号，生产涉及完成品、半成品和部品。
 * 2、进行排程时，参照工作者的常规排程思路进行排程。
 * 3、排程具体算法规则：
 * 3.1、从excel文件导入相关的生产数据，主要包含各型号的客户需求量和初始计划在库数量。
 * 3.2、如果某一天所有产品型号的客户需求量均为0，则该天不参与最低库存统计计算。
 * 3.3、针对某一特定型号，计算最低库存数量。
 * 3.3.1、最低库存数量为后续2至n天的客户需求量的累加，n通常为不大4的整数。
 * 3.4、评估当前一班应优先生产哪种型号的产品：比对所有生产型号的当前库存量与最低库存量的差值，最小者优先进行生产。
 * 3.5、如果发现当前的排班不能满足最低库存量的需求，则按以下顺序和限制进行加班的安排。
 * 3.5.1、将当前工作日排为白班加班（如本周其它天数均已排白班加班，则周一不排白班加班）
 * 3.5.2、白班加班不能满足生产需求，则排正常工作日的3班。当本周某一天排了3班之后，本周所有普通工作日（不含周六和周日）均为3班。
 * 3.5.3、如正常工作日的3班不能满足要求，则排周六的白班。
 * 3.5.4、如周六的白班不能满足要求，则排周六3班。
 * 3.5.5、如周六的3班不能满足要求，则排周日白班。
 * 3.5.6、如周日的3班不能满足要求，则排周日3班。
 * 3.5.7、如周日3班全部安排之后，仍不能满足生产需求，则给出出错提示。
 * 3.6、在排班过程中，如果出现因为产能不足而导致班次变更的情况，则更新班次之后，对所有生产计划进行重排。
 * 3.7、当本次排程的时间跨度已全部进行排程时，本次排程工作结束。
 * 4、完成品的排程工作结束之后，依据该排程结果，进行半成品的排程。
 * 4.1、进行半成品的排程时，最低库存量为下一天的对应完成品所需生产的数量。
 * 5、根据半成品的生产要求，进行部品的排程。
 *
 * @description ：APS 流程算法实现
 * @date ：2023/3/13 11:39
 */
public class ApsCalculation extends ApsModel implements ApsScheduling {
    private String[] productInformation; // 每个班次生产的产品信息
    private int[] productNumber; // 每个班次生产的产品数量
    private String typeLowestrate; // 当前操作的产品
    private Map<String, Integer> productCurrentdate = new HashMap<>(); // 当天各班次产品的生产数量
    private double inventoryRate; // 库存率因子
    private double workFactor; // 加班因子

    public ApsCalculation(String[] productType, Map<String, Integer> inventoryOriginal, Map<String, int[]> plan, int inventoryLowestDay,
                          Map<String, Integer> planNextMonthAverage, Map<String, int[]> capability, Weekday[] workday,
                          double inventoryRate, double workFactor) {
        //初始化给Model赋值
        super(productType, inventoryOriginal, plan, inventoryLowestDay, planNextMonthAverage, capability, workday);
        //库存率因子
        this.inventoryRate = inventoryRate;
        //加班因子
        this.workFactor = workFactor;

        //每个班次生产的产品信息(工作日天数*4)
        productInformation = new String[getShiftLen()];
        //将所有排班信息预设为'z'，进行排班时，将使用对应的产品型号替代z，每一天预设4个排班信息，分别对应早班、中班、加班、晚班。
        for (int i = 0; i < getShiftLen(); i++) {
            productInformation[i] = "z";
        }
        //每个班次生产的产品数量(工作日天数*4)
        productNumber = new int[getShiftLen()]; // 每个班次产量
        typeLowestrate = getProductType()[0]; // 默认第一个产品

        // 产品当天的生产数量默认为0
        for (int i = 0; i < getProductTypeLen(); i++) {
            productCurrentdate.put(getProductType()[i], 0);
        }
    }

    /**
     * 库存计划审查。
     * <p>
     * 在排程日度跨期，检测哪一天不需参与库存计划的计算。
     * <p>
     * 当某一天所有产品类型需求量为0 时则不需参与库存计划的计算。
     * <p>
     */
    public void inventoryPlanReview() {
        for (int i = 0; i < getApsDate(); i++) {
            int index = 0; // 标识当天所有产品的需求量是不是为0
            for (int j = 0; j < getProductTypeLen(); j++) {
                // i 产品的类型需求
                int[] planArr = getPlan().get(getProductType()[j]);
                if (planArr[i] != 0){
                    break;
                }
                if (++index == getProductTypeLen()){
                    getPlanFlag()[i] = 1; //如果planFlag为1，则这一天不参与库存计划的计算。
                }
            }
        }
    }

    /**
     * 计算后 inventoryLowestDay 天的最低出库数量要求，统计最低库存要求。
     * <p>
     * 找到当天之后的 inventoryLowestDay 个有效天的客户需求量。
     * <p>
     * 当某一天的有效客户需求量为-1 时，取 下月平均量，否者取有效的客户需求量，进行求和计算出最低出库数量
     */
    public void inventoryLowestCalculation() {
        int inventoryLowestDay = getInventoryLowestDay();
        int apsDate = getApsDate();
        // 用于存储某一天的有效客户需求量，并初始化为-1
        int[] inventory_number = new int[inventoryLowestDay];

        for (int i = 0; i < apsDate; i++) {
            // 用于存储某一天的有效客户需求量，并初始化为-1
            for (int day = 0; day < inventoryLowestDay; day++) {
                inventory_number[day] = -1;
            }

            int index = i + 1; // 当天之后寻找有效天的客户需求量
            two:
            for (int day = 0; day < inventory_number.length; day++) {
                // 当前一天找不到有效天的客户需求量时，之后就不用找了，直接跳出
                if (day > 0 && inventory_number[day - 1] == -1){
                    break;
                }

                //考察后面两个有效天的客户需求量，从第二天开始
                for (int j = index; j < apsDate; j++) {
                    // 当找到有效天的客户需求量跳出循环
                    if (getPlanFlag()[j] == 0 && inventory_number[day] == -1) {
                        inventory_number[day] = j;
                        // 当日期跨度为倒数第二天时，不需要寻找下一个inventory_number，直接跳出循环
                        if (j == apsDate - 1) {
                            break two;
                        }
                        index = j + 1; // 从第一个有效天的客户需求量定位后查询
                        break;
                    }
                }
            }

            // 某一天的有效客户需求量为-1 时，取 下月平均量
            for (int k = 0; k < getProductTypeLen(); k++) {
                String productType = getProductType()[k];
                for (int num = 0; num < inventoryLowestDay; num++) {
                    if (inventory_number[num] == -1) {
                        getInventoryLowest().get(productType)[i] += getPlanNextMonthAverage().get(productType);
                    } else {
                        getInventoryLowest().get(productType)[i] += getPlan().get(productType)[inventory_number[num]];
                    }

                }
            }
        }
    }

    /**
     * 排程处理
     *
     * @return 0: 排程成功，1：需要加班，重新排程
     */
    public int apsArrangement() {
        for (int i = 1; i <= getApsDate(); i++) {
            //如果当前的生产计划检查不通过（即产能不能满足生产要求） 0：正常工作，1：需要加班
            if (inventoryCheck(i) == 1) {
                apsModify(i); //则安排加班或安排夜班，包括安排周六、周日的加班与夜班。
                return 1; //每次安排了加班或夜班之后，都根据排班情况，从头开始，再一次进行工作安排。
            } else {
                apsCalculation(i); //生产计划检查通过，则根据当前一天的生产任务，进行当前一天生产计划安排。
            }
        }
        return 0;
    }

    /**
     * 对某一天进行生产计划检查
     *
     * @param dateNumber 具体哪一天
     * @return 0：正常工作，1：需要加班
     */
    public int inventoryCheck(int dateNumber) {
        int _dateNumber = dateNumber - 1; // 数组下标减一
        // 临时库存率
        Map<String, Float> inventoryRateMap = new HashMap<>();

        for (int i = 0; i < getProductTypeLen(); i++) {
            String productType = getProductType()[i];
            // 临时库存率1
            float inventory_rate_temp;
            int _plan = getPlan().get(productType)[_dateNumber];
            int _inventoryLowest = getInventoryLowest().get(productType)[_dateNumber];
            // 下式用于计算库存生产率，如果是第一天的生产计划，使用初始库存进行计算
            if (dateNumber == 1) {
                // (初始库存数 + 当天生产数量 - 当天计划使用 - 存货比例 * 当天最低库存数量 ） /  当天最低库存数，进行除法判断
                inventory_rate_temp = _inventoryLowest == 0 ? 0 : (float) (getInventoryOriginal().get(productType) + productCurrentdate.get(productType) - _plan - inventoryRate * _inventoryLowest) / _inventoryLowest;
            } else {
                Integer _planNextMonthAverage = getPlanNextMonthAverage().get(productType);
                // (前一天计划在库数 + 当天生产数量 - 当天计划使用 - 存货比例 * 当天最低库存数量 ） /  当天最低库存数，进行除法判断
                inventory_rate_temp = _planNextMonthAverage == 0 ? 0 : (float) (getInventory().get(productType)[dateNumber - 2] + productCurrentdate.get(productType) - _plan - inventoryRate * _inventoryLowest) / _planNextMonthAverage;
            }
            inventoryRateMap.put(productType, inventory_rate_temp);
        }

        // 各型号中库存生产率最低者将被优先生产, 获取当天产品型号库存率最小的
        float inventory_rate_min = inventoryRateMap.values().stream().sorted().findFirst().get(); // // 最小库存率
        // 最小库存率的产品类型
        typeLowestrate = inventoryRateMap.entrySet()
                .stream()
                .filter(item -> Objects.equals(item.getValue(), inventory_rate_min))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No suitable logging system located"));

        if (inventory_rate_min < 0) return 1;
        return 0;
    }

    /**
     * 计算库存数据
     * <p>
     * 计算当前产量
     * <p>
     * 计算每一个班次的生产数据
     *
     * @param dateNumber 当前排班天数
     */
    public void apsCalculation(int dateNumber) {
        int i = dateNumber - 1; // 数组下标减一
        int[] dateFlag = getDateFlag();

        // 初始化当天生产数量为0
        for (int j = 0; j < getProductTypeLen(); j++) {
            productCurrentdate.put(getProductType()[j], 0);
        }

        // 周六或周日排班日
        if (dateFlag[i] == 4 || dateFlag[i] == 5) {
            inventoryRateCalculation(dateNumber);
            return;
        }

        // 白班之前的库存率检查
        inventoryCheck(dateNumber);
        schedulingResultInformation(dateNumber, 0, 0);
        inventoryRateCalculation(dateNumber);

        // 中班之前的库存率检查
        inventoryCheck(dateNumber);
        schedulingResultInformation(dateNumber, 1, 1);
        inventoryRateCalculation(dateNumber);

        // 中班和加班之间应该是连续生产同一种产品，故而不进行产品类型的切换,不需要进行库存率检查。
        if (getDateFlag()[i] == 2) {
            schedulingResultInformation(dateNumber, 2, 1);
            inventoryRateCalculation(dateNumber);
        } else if (getDateFlag()[i] == 3) {
            //夜班之前的库存率检查
            inventoryCheck(dateNumber);
            schedulingResultInformation(dateNumber, 3, 2);
            inventoryRateCalculation(dateNumber);
        }
    }

    /**
     * 当前计划库存数计算
     * <p>
     * 当前计划库存数 inventory
     *
     * @param dateNumber 当前排班天数
     */
    public void inventoryRateCalculation(int dateNumber) {
        int i = dateNumber - 1; // 数组下标减一
        for (int j = 0; j < getProductTypeLen(); j++) {
            String productType = getProductType()[j];
            if (i == 0) {
                // 第一天： 初始库存数 + 当天生产数量 - 当天计划使用
                getInventory().get(productType)[i] = getInventoryOriginal().get(productType) + productCurrentdate.get(productType) - getPlan().get(productType)[i];
            } else {
                // 非第一天： 前一天计划库存数 + 当天生产数量 - 当天计划使用
                getInventory().get(productType)[i] = getInventory().get(productType)[i - 1] + productCurrentdate.get(productType) - getPlan().get(productType)[i];
            }
        }
    }

    /**
     * 排程结果信息
     * 1、当前班次的产品信息 productInformation
     * <p>
     * 2、当前班次的产品产量 productNumber
     * <p>
     * 3、当天的产品产量 productCurrentdate
     *
     * @param date_number     当前排班天数
     * @param shiftIndex      班次的定位 （0：白班，1：中班，2：加班（加班工作量为中班的0.3），3：夜班）
     * @param capabilityIndex 班次的产量定位（0：白班，1：中班，2：夜班）
     */
    public void schedulingResultInformation(int date_number, int shiftIndex, int capabilityIndex) {
        int i = date_number - 1; // 数组下标减一

        for (int j = 0; j < getProductTypeLen(); j++) {
            String productType = getProductType()[j];
            if (typeLowestrate.equals(productType)) {
                productInformation[4 * i + shiftIndex] = typeLowestrate;

                int capability;
                if (shiftIndex == 2) {
                    // 加班工作量为中班的0.3
                    capability = (int) (workFactor * getCapability().get(productType)[capabilityIndex]);
                } else {
                    capability = getCapability().get(productType)[capabilityIndex];
                }
                productNumber[4 * i + shiftIndex] = capability;

                // 当天生产数量 =  当天生产数量 + 当前生产数量
                productCurrentdate.put(productType, productCurrentdate.get(productType) + capability);
                break;
            }
        }
    }

    /**
     * 当前排班不能满足时，用于调整班次
     *
     * @param dateNumber 当前排班天数
     */
    public void apsModify(int dateNumber) {
        // 数组从0 开始
        int _dateNumber = dateNumber - 1;
        int[] dateFlag = getDateFlag();
        Weekday[] workday = getWorkday();
        int modify_flag = 0;

        //如果在整个排班时间段内，有某天未排加班，则在这一天安排加班。
        for (int i = _dateNumber; i >= 0; i = i - 1) {
            if (getDateFlag()[i] == 1) {
                getDateFlag()[i] = 2;
                modify_flag = 1;
                break;
            }
        }
        if (modify_flag == 1) return;

        //如果在整个排班时间段内，均已安排加班仍不能满足生产要求，则在最近的一个星期里安排夜班，安排了夜班之后，将原有的加班信息全部清除。
        //程序如运行至此，则说明所有工作日均已排加班，仍不能满足生产要求。则需要将当前星期改为3班。
        for (int j = _dateNumber; j >= 0; j = j - 1) {
            if (dateFlag[j] == 2 && workday[j] != Weekday.sat && workday[j] != Weekday.sun) {
                //由于即将排夜班，故而需要清除所有的白班加班标记
                for (int j1 = j; j1 >= 0; j1--) {
                    if (dateFlag[j1] == 2) {
                        dateFlag[j1] = 1;
                        productInformation[4 * j1 + 2] = "z"; //清除白班加班的班次标记
                        productNumber[4 * j1 + 2] = 0; //清除白班加班的产量信息
                    }
                }
                //将排程有效期内的当前整周都调整为三班,整周内原设为白班加班的班次和产量信息也清除
                switch (workday[j]) {
                    case fri:
                        dateFlag[j] = 3;
                        if (j - 1 >= 0) dateFlag[j - 1] = 3;
                        if (j - 2 >= 0) dateFlag[j - 2] = 3;
                        if (j - 3 >= 0) dateFlag[j - 3] = 3;
                        if (j - 4 >= 0) dateFlag[j - 4] = 3;
                        modify_flag = 1;
                        break;
                    case thu:
                        dateFlag[j] = 3;
                        if ((j + 1) < getApsDate()) dateFlag[j + 1] = 3;
                        productInformation[4 * (j + 1) + 2] = "z";
                        productNumber[4 * (j + 1) + 2] = 0;
                        if (j - 1 >= 0) dateFlag[j - 1] = 3;
                        if (j - 2 >= 0) dateFlag[j - 2] = 3;
                        if (j - 3 >= 0) dateFlag[j - 3] = 3;
                        modify_flag = 1;
                        break;
                    case wed:
                        dateFlag[j] = 3;
                        if ((j + 1) < getApsDate()) dateFlag[j + 1] = 3;
                        productInformation[4 * (j + 1) + 2] = "z";
                        productNumber[4 * (j + 1) + 2] = 0;
                        if ((j + 2) < getApsDate()) dateFlag[j + 2] = 3;
                        productInformation[4 * (j + 2) + 2] = "z";
                        productNumber[4 * (j + 2) + 2] = 0;
                        if (j - 1 >= 0) dateFlag[j - 1] = 3;
                        if (j - 2 >= 0) dateFlag[j - 2] = 3;
                        modify_flag = 1;
                        break;
                    case tue:
                        dateFlag[j] = 3;
                        if ((j + 1) < getApsDate()) dateFlag[j + 1] = 3;
                        productInformation[4 * (j + 1) + 2] = "z";
                        productNumber[4 * (j + 1) + 2] = 0;
                        if ((j + 2) < getApsDate()) dateFlag[j + 2] = 3;
                        productInformation[4 * (j + 2) + 2] = "z";
                        productNumber[4 * (j + 2) + 2] = 0;
                        if ((j + 3) < getApsDate()) dateFlag[j + 3] = 3;
                        productInformation[4 * (j + 3) + 2] = "z";
                        productNumber[4 * (j + 3) + 2] = 0;
                        if (j - 1 >= 0) dateFlag[j - 1] = 3;
                        modify_flag = 1;
                        break;
                    case mon:
                        dateFlag[j] = 3;
                        if ((j + 1) < getApsDate()) dateFlag[j + 1] = 3;
                        productInformation[4 * (j + 1) + 2] = "z";
                        productNumber[4 * (j + 1) + 2] = 0;
                        if ((j + 2) < getApsDate()) dateFlag[j + 2] = 3;
                        productInformation[4 * (j + 2) + 2] = "z";
                        productNumber[4 * (j + 2) + 2] = 0;
                        if ((j + 3) < getApsDate()) dateFlag[j + 3] = 3;
                        productInformation[4 * (j + 3) + 2] = "z";
                        productNumber[4 * (j + 3) + 2] = 0;
                        if ((j + 4) < getApsDate()) dateFlag[j + 4] = 3;
                        productInformation[4 * (j + 4) + 2] = "z";
                        productNumber[4 * (j + 4) + 2] = 0;
                        modify_flag = 1;
                        break;
                }
            }

            // 从周六加班调整为周六三班
            if (dateFlag[j] == 2 && workday[j] == Weekday.sat) {
                // System.out.println("将第" + j + "天从周六加班调整为周六三班\n");
                dateFlag[j] = 3;
                productInformation[4 * j + 2] = "z"; //清除白班加班的班次标记
                productNumber[4 * j + 2] = 0;          //清除白班加班的产量信息
                modify_flag = 1;
                break;
            }

            // 从周日加班调整为周日三班
            if (dateFlag[j] == 2 && workday[j] == Weekday.sun) {
                // System.out.println("将第" + j + "天从周日加班调整为周日三班\n");
                dateFlag[j] = 3;
                productInformation[4 * j + 2] = "z"; //清除白班加班的班次标记
                productNumber[4 * j + 2] = 0;          //清除白班加班的产量信息
                modify_flag = 1;
                break;
            }
        }
        if (modify_flag == 1) return;

        //程序运行至此，代表所有已处理的工作日全部排三班仍不能满足生产要求，将最先遇到的周六date_flag设为1。
        for (int k = _dateNumber; k >= 0; k = k - 1) {
            if (dateFlag[k] == 4) {
                // System.out.println("将第" + k + "天调整为周六加班\n");
                dateFlag[k] = 1;
                modify_flag = 1;
                break;
            }
        }
        if (modify_flag == 1) return;

        //程序运行至此，代表所有周六全部排三班仍不能满足生产要求，将最先遇到的周日date_flag设为1。
        for (int m = _dateNumber; m >= 0; m = m - 1) {
            //如果搜索至最后一天，且最后一天不是周日，则系统无法完成排程。
            if (m == 0 && dateFlag[0] != 5) {
                System.out.println("ERROR:No arrangement available, please choose another INVENTORY_RATE");
            }
            //如果搜索至最后一天，且最后一天是周日，则更改标记后，立即进行检查，确定是否可以完成排程。
            if (m == 0 && dateFlag[0] == 5) {
                dateFlag[m] = 3;  //直接设为3班状态
                if (inventoryCheck(dateNumber) == 1)  //直接测试
                    System.out.println("ERROR:No arrangement available, please choose another INVENTORY_RATE");
            }
            if (dateFlag[m] == 5) {
                // System.out.println("将第" + m + "天调整为周日加班\n");
                dateFlag[m] = 1;
                break;
            }
        }
    }

    /**
     * 排班结果的优化。优化的原则如下：
     * <p>
     * 1、如果date_flag为1或2，则考察本日与下一日是否有同一类型产品，并判断其是否相邻。如不相邻，则进行调整。
     * <p>
     * 2、如果date_flag为3,则先考察早班与中班是否同一类型产品，并判断其是否相邻。如不相邻，则进行调整。
     * <p>
     * 3、如果date_flag为3，则判断当天晚班与下一日的早班和中班是否有同一类型产品，并判断其是否相邻。如不相邻，则进行调整。
     */
    public void schedulingOptimization() {
        int[] dateFlag = getDateFlag();
        for (int optimize = 0; optimize < getApsDate() - 1; optimize++) {
            if (dateFlag[optimize] == 1) {
                for (int search1 = optimize + 1; search1 < getApsDate(); search1++) {
                    if (dateFlag[search1] == 4 || dateFlag[search1] == 5) continue;

                    int _current = 4 * optimize; // 前一天早班定位
                    int _current2 = _current + 1; // 前一天中班定位
                    int _next = 4 * search1; // 后一天早班定位
                    int _next2 = _next + 1; // 后一天中班定位

                    if (dateFlag[search1] == 1 || dateFlag[search1] == 3) {
                        // 前一生产日早班与后一生产日早班相同，前一生产日的早班与中班信息互换。
                        if (productInformation[_current].equals(productInformation[_next])) {
                            ArrayUtil.swap(productInformation, _current, _current2);
                            ArrayUtil.swap(productNumber, _current, _current2);
                        }

                        // 前一生产日早班与后一生产日中班相同：前一生产日的早班与中班信息互换，后一生产日的早班与中班信息互换。
                        if (productInformation[_current].equals(productInformation[_next2])) {
                            ArrayUtil.swap(productInformation, _current, _current2);
                            ArrayUtil.swap(productNumber, _current, _current2);

                            ArrayUtil.swap(productInformation, _next, _next2);
                            ArrayUtil.swap(productNumber, _next, _next2);
                        }

                        //前一生产日中班与后一生产日中班相同，后一生产日的早班与中班信息互换。
                        if (productInformation[_current2].equals(productInformation[_next2])) {
                            ArrayUtil.swap(productInformation, _next, _next2);
                            ArrayUtil.swap(productNumber, _next, _next2);
                        }

                    }

                    if (dateFlag[search1] == 2) {
                        //前一生产日早班与后一生产日早班相同，前一生产日的早班与中班信息互换。
                        if (productInformation[_current].equals(productInformation[_next])) {
                            ArrayUtil.swap(productInformation, _current, _current2);
                            ArrayUtil.swap(productNumber, _current, _current2);
                        }
                    }

                }
            }

            if (dateFlag[optimize] == 2) {
                for (int search2 = optimize + 1; search2 < getApsDate(); search2++) {
                    if (dateFlag[search2] == 4 || dateFlag[search2] == 5) {
                        continue;
                    }
                    if (dateFlag[search2] == 1 || dateFlag[search2] == 3) {
                        int _current = 4 * optimize; // 前一天早班定位
                        int _current2 = _current + 1; // 前一天中班定位
                        int _next = 4 * search2; // 后一天早班定位
                        int _next2 = _next + 1; // 后一天中班定位

                        //前一生产日中班与后一生产日中班相同，后一生产日的早班与中班信息互换。
                        if (productInformation[_current2].equals(productInformation[_next2])) {
                            ArrayUtil.swap(productInformation, _next, _next2);
                            ArrayUtil.swap(productNumber, _next, _next2);
                        }
                    }
                    //后一生产日为白班加班
                    if (dateFlag[search2] == 2) {
                        //这种情况无法合并
                    }
                }
            }

            if (dateFlag[optimize] == 3) {
                int _current = 4 * optimize; // 前一天早班定位
                int _current2 = _current + 1; // 前一天中班定位
                int _current3 = _current + 3; // 前一天晚班定位

                //前一生产日早班与前一生产日晚班相同，前一生产日的早班与中班信息互换。
                if (productInformation[_current].equals(productInformation[_current3])) {
                    ArrayUtil.swap(productInformation, _current, _current2);
                    ArrayUtil.swap(productNumber, _current, _current2);
                }

                for (int search3 = optimize + 1; search3 < getApsDate(); search3++) {
                    if (dateFlag[search3] == 4 || dateFlag[search3] == 5) {
                        continue;
                    }
                    if (dateFlag[search3] == 1 || dateFlag[search3] == 3) {
                        int _next = 4 * search3; // 后一天早班定位
                        int _next2 = _next + 1; // 后一天中班定位

                        //前一生产日晚班与后一生产日中班相同，后一生产日的早班与中班信息互换。
                        if (productInformation[_current3].equals(productInformation[_current2])) {
                            ArrayUtil.swap(productInformation, _next, _next2);
                            ArrayUtil.swap(productNumber, _next, _next2);
                        }
                    }
                    //后一生产日为白班加班
                    if (dateFlag[search3] == 2) {
                        //没有调整的必要
                    }
                }
            }
        }
    }

    /**
     * 排班结果检查
     */
    public void schedulingCheck() {
        for (int numberCheck = 0; numberCheck < getApsDate(); numberCheck++) {
            for (int i = 0; i < getProductTypeLen(); i++) {
                String productType = getProductType()[i];
                int inventory = getInventory().get(productType)[numberCheck];
                int inventoryLowest = getInventoryLowest().get(productType)[numberCheck];

                if (inventory < inventoryLowest)
                    System.out.println("ERROR:Inventory is too low\n");
            }
        }

        int[] dateFlag = getDateFlag();
        System.out.println("上班类型：" + Arrays.toString(dateFlag));
        System.out.println("本次排程初始排班信息，1代表正常两班，2代表白班加班（加班工作量为中班的0.3），3代表三班，4代表周六排班日，5代表周日排班日");
        System.out.println();

        Map<String, int[]> inventory = getInventory();
        for (Map.Entry<String, int[]> stringEntry : inventory.entrySet()) {
            System.out.println("产品：" + stringEntry.getKey() + "  ,产量：" + Arrays.toString(stringEntry.getValue()));
        }
        System.out.println();

        Map<String, int[]> inventoryLowest = getInventoryLowest();
        for (Map.Entry<String, int[]> stringEntry : inventoryLowest.entrySet()) {
            System.out.println("产品：" + stringEntry.getKey() + "  ,最低库存产量：" + Arrays.toString(stringEntry.getValue()));
        }
        System.out.println();

        System.out.println("班次产品信息：" + Arrays.toString(productInformation));
        System.out.println();

        System.out.println("班次产品数量：" + Arrays.toString(productNumber));
        System.out.println();

    }

    /**
     * 排程结束、结果输出
     *
     * @param productInformationByShift 指定班次所有日期的产品信息
     * @param productNumberByShift      指定班次所有日期的产品数量
     * @param shiftIndex                指定哪个班次的信息和数量,0:早班，1：中班,2：加班，3:晚班
     */
    public void getProductInfo(String[] productInformationByShift, int[] productNumberByShift, int shiftIndex) {
        int days = getApsDate();              // 获取天数
        for (int day = 0; day < days; day++) {  // 遍历所有日期
            // 计算指定班次在该日期的产品信息和数量的下标
            int index = day * 4 + shiftIndex;
            // 将指定班次在该日期的产品信息和数量放入对应数组中
            productInformationByShift[day] = productInformation[index];
            productNumberByShift[day] = productNumber[index];
        }
    }


}
