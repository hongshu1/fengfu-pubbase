package cn.rjtech.admin.cusorderresult;

import cn.rjtech.admin.cusordersum.CusOrderSumService;
import cn.rjtech.admin.scheduproductplan.ScheduProductPlanMonthService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
            List<Record> records = dbTemplate("cusorderresult.paginate", kv).find();

            //放入betweenDateList,根据顺序拼上qtyXX

        }
        return paginate;
    }
    //获得当前月份第一天和最后一天

    /**
     *
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


    public static void main(String[] args) {

        Kv kv = new Kv();
        if (kv.getDate("beginDate") == null||kv.getDate("endDate") == null){
            kv.set(getBeginEndDate());
        }
        Date beginDate = kv.getDate("startdate");
        Date endDate = kv.getDate("enddate");
        List<String> betweenDate = ScheduProductPlanMonthService.getBetweenDate(kv.getStr("beginDate"), kv.getStr("endDate"));
        System.out.println(betweenDate.toString());
    }
}
