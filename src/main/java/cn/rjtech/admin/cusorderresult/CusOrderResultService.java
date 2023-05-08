package cn.rjtech.admin.cusorderresult;

import cn.rjtech.admin.cusordersum.CusOrderSumService;
import cn.rjtech.admin.scheduproductplan.ScheduProductPlanMonthService;
import cn.rjtech.model.momdata.Inventory;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class CusOrderResultService extends CusOrderSumService {
    @Inject
    private ScheduProductPlanMonthService productPlanMonthService;



    public Page findCusOrderResult(Integer pageNumber, Integer pageSize, Kv kv) {

        //默认查询当前月份的
        if (kv.getDate("startdate") == null||kv.getDate("enddate") == null){
            kv.set(getBeginEndDate());
        }

        Page<Record> paginate = dbTemplate("cusorderresult.paginate", kv).paginate(pageNumber, pageSize);

        List<String> betweenDateList = ScheduProductPlanMonthService.getBetweenDate(kv.getStr("startdate"), kv.getStr("enddate"));
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
     *获得当前月份第一天和最后一天
     * @return kv.startdate  kv.startdate
     */
    public static Kv  getBeginEndDate() {
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = now.with(TemporalAdjusters.lastDayOfMonth());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String beginDateStr = firstDayOfMonth.format(formatter);
        String endDateStr = lastDayOfMonth.format(formatter);
        Kv kv = new Kv();
        kv.set("startdate", beginDateStr);
        kv.set("enddate", endDateStr);
        return kv;
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

    public static void main(String[] args) {
        System.out.println(ScheduProductPlanMonthService.getBetweenDate("2023-1-4", "2023-3-8"));
        //List<String> list1 = Arrays.asList("A1", "A2", "A3");
        //List<String> list2 = Arrays.asList("B1", "B2", "B3");
        //List<String> list3 = Arrays.asList("C1", "C2", "C3");
        //List<List<String>> lists = new ArrayList<>();
        //lists.add(list1);
        //lists.add(list2);
        //lists.add(list3);
        //
        //Inventory inventory = new Inventory();
        //Map map = addInventory("code1", "customer1", lists);
        //
        //System.out.println(map.toString());
    }
}
