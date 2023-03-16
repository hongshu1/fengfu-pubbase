package cn.rjtech.admin.hrhiperson;

import cn.jbolt.core.service.base.BaseU8RecordService;

/**
 * 人事档案
 *
 * @author Kephon
 */
public class HrHiPersonService extends BaseU8RecordService {

    @Override
    protected String getTableName() {
        return "hr_hi_person";
    }

    @Override
    protected String getPrimaryKey() {
        return "cPsn_Num";
    }

}
