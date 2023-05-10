package cn.rjtech.admin.cusorderresult;

import cn.rjtech.admin.cusordersum.CusOrderSumService;
import cn.rjtech.admin.scheduproductplan.ScheduProductPlanMonthService;
import cn.rjtech.util.Util;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CusOrderResultService extends CusOrderSumService {
    @Inject
    private ScheduProductPlanMonthService productPlanMonthService;



    public Page findCusOrderResult(Integer pageNumber, Integer pageSize, Kv kv) {

        //默认查询当前月份的
        if (kv.getDate("startdate") == null||kv.getDate("enddate") == null){
            kv.set(Util.getBeginEndDate());
        }

        Page<Record> paginate = dbTemplate("cusorderresult.paginate", kv).paginate(pageNumber, pageSize);

        List<String> betweenDateList = Util.getBetweenDate(kv.getStr("startdate"), kv.getStr("enddate"));
        for (Record record : paginate.getList()) {
            String cInvCode = record.getStr("cInvCode");
            HashMap<String, List> dateList = new HashMap<>();
            //查询一个物料的每日汇总的数目
            Kv data = new Kv();
            data.set("startdate",kv.getStr("startdate")).set("enddate",kv.getStr("enddate")).set("cinvcode",cInvCode);
            List<Record> records = dbTemplate("cusorderresult.paginate", data).find();
            List<String> list1 = new ArrayList<>();
            //之后再补上其他三个list qtyXX

            //放入betweenDateList,根据顺序拼上

        }
        return paginate;
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
