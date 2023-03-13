package cn.rjtech.service.u9.organization;

import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.base.service.BaseU9RecordService;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * U9组织机构
 *
 * @author Kephon
 */
public class OrganizationRecordService extends BaseU9RecordService {

    @Override
    protected String getTableName() {
        return "base_organization";
    }

    @Override
    protected String getPrimaryKey() {
        return "ID";
    }

    public List<Record> findAllWithTrl() {
        Sql sql = selectSql().select("o.id, o.shortname, o.location, o.code, o.createdon, o.modifiedon, ot.name")
                .from(table(), "o")
                .innerJoin("Base_Organization_Trl", "ot", "o.id = ot.id");

        return find(sql);
    }

    /**
     * 组织
     */
    public Record getWithTrlById(long id) {
        Sql sql = selectSql().select("o.id, o.orgcontacts, o.shortname, o.location, o.code, o.createdon, o.modifiedon, ot.name")
                .from(table(), "o")
               .innerJoin("Base_Organization_Trl", "ot", "o.id = ot.id")
                .eq("o.id", id);

        return findFirst(sql);
    }


}
