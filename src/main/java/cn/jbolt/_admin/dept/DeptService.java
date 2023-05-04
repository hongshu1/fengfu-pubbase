package cn.jbolt._admin.dept;

import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.model.Dept;
import cn.jbolt.core.service.JBoltDeptService;

import java.util.List;

/**
 *
 * @ClassName: DeptService
 * @author: JFinal学院-小木
 * @date: 2021-02-07 20:34
 */
public class DeptService extends JBoltDeptService{
    /**
     * 得到JSTree数据源
     * @param checkedId 默认选中节点
     * @param openLevel -1全部 0 不动 >0指定层级
     * @return
     */
    public List<JsTreeBean> getAllCrudJsTreeDatas(Long checkedId, int openLevel) {
        List<Dept> depts=getAllList();
        return convertJsTree(depts,checkedId,openLevel,null,"sn,name",SORT_RANK,null,false);
    }

    public String getDeptNameByDeptids(String deptids){
        return queryColumn("SELECT name+',' FROM (SELECT * FROM jb_dept WHERE '"+deptids+"' LIKE CONCAT('%', id, '%') )t2  FOR XML PATH('')");
    }

    /**
     * 获取树形结构数据
     */
    public List<Dept> getOrgTreeDatas(long orgId, boolean onlyEnable,boolean asOptions){
        Sql sql=selectSql().asc("sort_rank");
        if(asOptions) {
            sql.select("id","name","pid","sn");
        }
        if(onlyEnable) {
            sql.eq("enable", TRUE);
        }
        sql.bracketLeft()
                .eq("id", orgId)
                .or().startWith("dept_path", orgId + ",")
                .bracketRight();

        // 查询所有部门
        List<Dept> datas=find(sql);
        return convertToModelTree(datas, "id", "pid", (p)-> ObjUtil.equal(p.getPid(), orgId));
    }
    /**
     * 根据部门编码返回名称
     */
	public String findNameBySn(String str) {
		Dept dept = findFirst(selectSql().eq("sn", str));
		if(dept == null) return null;
		return dept.getName();
	}
}