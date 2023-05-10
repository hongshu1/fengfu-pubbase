package cn.rjtech.admin.cusorderresult;

import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.scheduproductplan.ScheduProductPlanMonthService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.util.DateUtils;
import cn.rjtech.util.Util;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 客户计划汇总及实绩管理
 */
//@CheckPermission(PermissionKey.CUSORDER_RESULT)
@UnCheckIfSystemAdmin
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/cusorderresult", viewPath = "/_view/admin/cusorderresult")
public class CusOrderResultController extends BaseAdminController {

    @Inject
    private CusOrderResultService orderResultService;
    @Inject
    private ScheduProductPlanMonthService service;


    /**
     * 首页
     */
    public void index() {

            //String startdate = get("startdate");
            String startdate = "";
            String enddate = "";
        //默认查询当前月份的
        if (getDate("startdate") == null||getDate("enddate") == null){
            Kv kv = Util.getBeginEndDate();
            startdate = kv.getStr("beginDate");
            enddate = kv.getStr("endDate");
        }

            String cworkname = get("cworkname");
            String cinvcode = get("cinvcode");
            String cinvcode1 = get("cinvcode1");
            String cinvname1 = get("cinvname1");
            set("startdate",startdate);
            set("enddate",enddate);
            set("cworkname",cworkname);
            set("cinvcode",cinvcode);
            set("cinvcode1",cinvcode1);
            set("cinvname1",cinvname1);

            List<String> collist = new ArrayList<>();
            List<String> namelist = new ArrayList<>();
            List<String> weeklist = new ArrayList<>();
            List<Record> name2list = new ArrayList<>();
            if (StringUtils.isNotBlank(startdate) && StringUtils.isNotBlank(enddate)){
                //排产开始日期到截止日期之间的日期集 包含开始到结束那天 有序
                List<String> scheduDateList = Util.getBetweenDate(startdate,enddate);
                //页面顶部colspan列  key:2023年1月  value:colspan="13"
                Map<String,Integer> yearMonthMap = new HashMap<>();
                for (int i = 0; i < scheduDateList.size(); i++) {
                    String year = scheduDateList.get(i).substring(0,4);
                    int month = Integer.parseInt(scheduDateList.get(i).substring(5,7));
                    String yearMonth = year + "年" + month + "月";
                    if (yearMonthMap.containsKey(yearMonth)){
                        int count = yearMonthMap.get(yearMonth);
                        yearMonthMap.put(yearMonth,count + 1);
                    }else {
                        yearMonthMap.put(yearMonth,2);
                    }

                    String weekDay = DateUtils.formatDate(DateUtils.parseDate(scheduDateList.get(i)),"E");
                    if (weekDay.equals("星期一")){weeklist.add("Mon");}
                    if (weekDay.equals("星期二")){weeklist.add("Tue");}
                    if (weekDay.equals("星期三")){weeklist.add("Wed");}
                    if (weekDay.equals("星期四")){weeklist.add("Thu");}
                    if (weekDay.equals("星期五")){weeklist.add("Fri");}
                    if (weekDay.equals("星期六")){weeklist.add("Sat");}
                    if (weekDay.equals("星期日")){weeklist.add("Sun");}
                }

                int monthCount = 1;
                List<String> name2listStr = new ArrayList<>();
                for (int i = 0; i < scheduDateList.size(); i++) {
                    String year = scheduDateList.get(i).substring(0,4);
                    int month = Integer.parseInt(scheduDateList.get(i).substring(5,7));
                    String yearMonth = year + "年" + month + "月";
                    if (!name2listStr.contains(yearMonth)){
                        name2listStr.add(yearMonth);
                        Record record = new Record();
                        record.set("colname",yearMonth);
                        record.set("colsum",yearMonthMap.get(yearMonth));
                        name2list.add(record);
                    }

                    int seq = i + 1;
                    int day = Integer.parseInt(scheduDateList.get(i).substring(8));
                    if (i != 0 && day == 1){
                        collist.add("qtysum"+monthCount);
                        collist.add("qty"+seq);

                        namelist.add("合计");
                        namelist.add(day+"日");
                        monthCount ++;
                        continue;
                    }
                    if (seq == scheduDateList.size()){
                        collist.add("qty"+seq);
                        collist.add("qtysum"+monthCount);

                        namelist.add(day+"日");
                        namelist.add("合计");
                        continue;
                    }
                    collist.add("qty"+seq);
                    namelist.add(day+"日");
                }
            }
            set("collist", collist);
            set("colnamelist", namelist);
            set("weeklist", weeklist);
            set("colname2list", name2list);


        render("index.html");
    }
    /**
     * 数据源
     */
    public void datas() {

      renderJsonData(orderResultService.findCusOrderResult(getPageNumber(), getPageSize(), getKv()));

    }

}
