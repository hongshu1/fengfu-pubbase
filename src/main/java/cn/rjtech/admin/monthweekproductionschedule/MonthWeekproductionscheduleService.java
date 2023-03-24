package cn.rjtech.admin.monthweekproductionschedule;

import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.service.base.JBoltBaseService;
import cn.rjtech.model.momdata.ForgeignCurrency;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.spire.ms.System.Collections.ArrayList;

import java.util.Date;
import java.util.List;

/**
 * @author ：chang
 * @description ：TODO
 * @date ：2023/3/23 13:30
 */
public class MonthWeekproductionscheduleService extends JBoltBaseService<ForgeignCurrency> {

    @Override
    protected ForgeignCurrency dao() {
        return null;
    }

    @Override
    protected int systemLogTargetType() {
        return 0;
    }


    /**
     * 获取排产信息
     */
    public List<Record> productionSchedulingInformation() {
        List<Record> records = new ArrayList();
        // 获取层级信息
        List<Dictionary> dictionaries = JBoltDictionaryCache.me.getListByTypeKey("pslevel_type", true);
        for (int i = 0; i < dictionaries.size(); i++) {
            Dictionary dictionary = dictionaries.get(i);
            // 每个层级的截止日期和锁定日期
            Record record = new Record();
            record.set("hierarchy", dictionary.getName());
            record.set("schedulingDeadline", ""); // todo 排程截止时间
            record.set("lockDeadline", ""); // todo 锁定截止时间
            records.add(record);
        }
        return records;
    }

    /**
     * APS排程逻辑处理
     */
    public void querySchedulingData(Kv kv) {
        Date layDate = kv.getDate("layDate");// 截至日期
        int hierarchy = kv.getInt("hierarchy");// 排产层级

        /*
            1、获取客户计划汇总进行月周计划排产
                1.1根据排产层级找到产线档案的产线，通过产线去存货档案找到默认产线对应的存货编码
                1.2通过存货编码去客户计划汇总获取对应的客户订单，此客户订单类型为内作销售
                1.3客户订单通过截止日期进行筛选
         */
        dbTemplate("monthweekproductionschedule.planSummaryList", kv).find(); // todo:该sql 待补充

        /*
            如果当前是第一层级，则直接使用
         */


    }
}

































