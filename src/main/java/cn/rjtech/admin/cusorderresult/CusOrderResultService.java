package cn.rjtech.admin.cusorderresult;

import cn.rjtech.admin.cusordersum.CusOrderSumService;
import cn.rjtech.admin.scheduproductplan.ScheduProductPlanMonthService;
import cn.rjtech.util.DateUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.*;

public class CusOrderResultService extends CusOrderSumService {
    @Inject
    private ScheduProductPlanMonthService productPlanMonthService;


    public Page<Record> getAdminDatas(Integer pageNumber, Integer pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("cusorderresult.paginate", kv).paginate(pageNumber, pageSize);
        paginate.setList(testDatas());
        return paginate;
    }

    public List<Record> testDatas(){
        List<Record> datas = new ArrayList<>();
        for (int i = 0; i <= 2; i++) {
            Record data = new Record();
            data.put("cinvcode", "存货编码" +i);
            data.put("cinvcode1", "客户部番"+i);
            data.put("cinvname1", "部品名称"+i);

            List<Record> customers = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                Record customer = new Record();

                // 客户订单
                List<Record> customerorderqtyDatas = new ArrayList<>();
                // 取货计划
                List<Record> pickupplanqtyDatas = new ArrayList<>();
                // 出货实际
                List<Record> actualshipmentqtyDatas = new ArrayList<>();
                // 订单与取货差异
                List<Record> differenceqtyDatas = new ArrayList<>();
                for (int k = 0; k < 3; k++) {
                    // 日期
                    String time = DateUtils.getDate("yyyy-MM-dd", k, Calendar.DATE);
                    Record customerorderqtyData = new Record();
                    customerorderqtyData.put("time", time);
                    customerorderqtyData.put("iqty", 1);
                    customerorderqtyDatas.add(customerorderqtyData);
                    Record pickupplanqtyData = new Record();
                    pickupplanqtyData.put("time", time);
                    pickupplanqtyData.put("iqty", 100);
                    pickupplanqtyDatas.add(pickupplanqtyData);
                    Record actualshipmentqtyData = new Record();
                    actualshipmentqtyData.put("time", time);
                    actualshipmentqtyData.put("iqty", 2);
                    actualshipmentqtyDatas.add(actualshipmentqtyData);
                    Record differenceqtyData = new Record();
                    differenceqtyData.put("time", time);
                    differenceqtyData.put("iqty", 3000);
                    differenceqtyDatas.add(differenceqtyData);
                }

                // 客户名称
                customer.put("ccusname", "客户名称" + j);
                customer.put("customerorderqtydatas", customerorderqtyDatas);
                customer.put("pickupplanqtydatas", pickupplanqtyDatas);
                customer.put("actualshipmentqtydatas", actualshipmentqtyDatas);
                customer.put("differenceqtydatas", differenceqtyDatas);

                customers.add(customer);
            }
            data.put("customers", customers);
            datas.add(data);
        }
        return datas;
    }

    /**
     * 组装返回的多层map
     * @param inventoryCode
     * @param customerName
     * @param lists
     */
    public static Map addInventory(String inventoryCode, String customerName, List<List<String>> lists) {
        Map<String, Map<String, List<List<String>>>> inventoryMap= new HashMap<>();
        Map<String, List<List<String>>> customerMap = inventoryMap.computeIfAbsent(inventoryCode, k -> new HashMap<>());
        customerMap.put(customerName, lists);
        return inventoryMap;
    }

    public Map setLists(String inventoryCode, String customerName, List<List<String>> lists) {
        Map<String, Map<String, List<List<String>>>> inventoryMap= new HashMap<>();
        Map<String, List<List<String>>> customerMap = inventoryMap.computeIfAbsent(inventoryCode, k -> new HashMap<>());
        customerMap.put(customerName, lists);
        return inventoryMap;
    }

}
