package cn.rjtech.admin.cusfieldmappingform;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.CusfieldmappingForm;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

/**
 * 系统配置-导入字段映射表单
 *
 * @ClassName: CusfieldmappingFormService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-02 13:52
 */
public class CusfieldmappingFormService extends BaseService<CusfieldmappingForm> {
    
    private final CusfieldmappingForm dao = new CusfieldmappingForm().dao();

    @Override
    protected CusfieldmappingForm dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     */
    public Page<CusfieldmappingForm> getAdminDatas(int pageNumber, int pageSize) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //排序
        sql.desc("iAutoId");
        return paginate(sql);
    }

    /**
     * 保存
     */
    public Ret save(CusfieldmappingForm cusfieldmappingForm) {
        if (cusfieldmappingForm == null || isOk(cusfieldmappingForm.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(cusfieldmappingForm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = cusfieldmappingForm.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(cusfieldmappingForm.getIAutoId(), JBoltUserKit.getUserId(), cusfieldmappingForm.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(CusfieldmappingForm cusfieldmappingForm) {
        if (cusfieldmappingForm == null || notOk(cusfieldmappingForm.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        CusfieldmappingForm dbCusfieldmappingForm = findById(cusfieldmappingForm.getIAutoId());
        if (dbCusfieldmappingForm == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(cusfieldmappingForm.getName(), cusfieldmappingForm.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = cusfieldmappingForm.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(cusfieldmappingForm.getIAutoId(), JBoltUserKit.getUserId(), cusfieldmappingForm.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param cusfieldmappingForm 要删除的model
     * @param kv                  携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(CusfieldmappingForm cusfieldmappingForm, Kv kv) {
        //addDeleteSystemLog(cusfieldmappingForm.getIAutoId(), JBoltUserKit.getUserId(),cusfieldmappingForm.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param cusfieldmappingForm model
     * @param kv                  携带额外参数一般用不上
     */
    @Override
    public String checkInUse(CusfieldmappingForm cusfieldmappingForm, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

}