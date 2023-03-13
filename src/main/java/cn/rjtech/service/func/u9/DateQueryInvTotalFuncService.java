package cn.rjtech.service.func.u9;

import cn.rjtech.base.service.func.BaseU9FuncService;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * P_Sys_DateQueryInvTotal 存储过程
 *
 * @author Kephon
 */
public class DateQueryInvTotalFuncService extends BaseU9FuncService {

    /**
     * 获取所有料品的月结期初台账数据
     */
    public List<Record> getInvEndAmount(long orgId, String startDate, String endDate) {

        return findRecords("EXEC P_Sys_InvOpeningQty '10',null,?,'1002302210000346',?,?,'2023-01-01','rj',null ",orgId,startDate,endDate);

    }
}
