package cn.rjtech.service.u9.department;

import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.base.service.BaseU9RecordService;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * U9部门
 *
 * @author Kephon
 */
public class DepartmentService extends BaseU9RecordService {

    @Override
    protected String getTableName() {
        return "CBO_Department";
    }

    @Override
    protected String getPrimaryKey() {
        return "ID";
    }

    public List<Record> findByOrgLevel0WithTrl(long org) {
        Sql sql = selectSql().select("d.*, dt.name")
                .from(table(), "d")
                .innerJoin("CBO_Department_Trl", "dt", "d.id = dt.id")
                .eq("d.org", org)
                .eq("d.level", 0);

        return find(sql);
    }

    public List<Record> findByParentNode(long parentnode) {
        Sql sql = selectSql().select("d.*, dt.name")
                .from(table(), "d")
                .innerJoin("CBO_Department_Trl", "dt", "d.id = dt.id")
                .eq("d.parentnode", parentnode);

        return find(sql);
    }

}
