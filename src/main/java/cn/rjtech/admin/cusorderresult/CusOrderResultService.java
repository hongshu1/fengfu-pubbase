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
import java.util.List;

public class CusOrderResultService extends CusOrderSumService {
    @Inject
    private ScheduProductPlanMonthService productPlanMonthService;



    public Page findCusOrderResult(Integer pageNumber, Integer pageSize, Kv kv) {

        //默认查询当前月份的
        if (kv.getDate("beginDate") == null||kv.getDate("endDate") == null){
            kv.set(setBeginEndDate());
        }
        Date beginDate = kv.getDate("beginDate");
        Date endDate = kv.getDate("endDate");

        Page<Record> paginate = dbTemplate("cusordersum.getCusOrderSumList", kv).paginate(pageNumber, pageSize);
        List<String> betweenDate = ScheduProductPlanMonthService.getBetweenDate(kv.getStr("beginDate"), kv.getStr("endDate"));

        return paginate;
    }
    //获得当前月份第一天和最后一天
    public static Kv  setBeginEndDate() {
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = now.with(TemporalAdjusters.lastDayOfMonth());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String beginDateStr = firstDayOfMonth.format(formatter);
        String endDateStr = lastDayOfMonth.format(formatter);
        Kv kv = new Kv();
        kv.set("beginDate", beginDateStr);
        kv.set("endDate", endDateStr);
        return kv;
    }
    public Kv getDateKv (String startDate,String endDate){
        Kv kv = new Kv();
        int[] startDateValues = this.parseDate(startDate);

        kv.set("startyear",startDateValues[0]);
        kv.set("startmonth",startDateValues[1]);
        kv.set("startday",startDateValues[2]);
        int[] ints1 = parseDate(endDate);
        return  kv;
    }
    public  int[] parseDate(String dateString) {
        String[] parts = dateString.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        return new int[] { year, month, day };
    }

    public static void main(String[] args) {

        Kv kv = new Kv();
        if (kv.getDate("beginDate") == null||kv.getDate("endDate") == null){
            kv.set(setBeginEndDate());
        }
        Date beginDate = kv.getDate("beginDate");
        Date endDate = kv.getDate("endDate");
        List<String> betweenDate = ScheduProductPlanMonthService.getBetweenDate(kv.getStr("beginDate"), kv.getStr("endDate"));
        System.out.println(betweenDate.toString());
    }
}
