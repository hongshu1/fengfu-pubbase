package cn.rjtech.admin.cusfieldsmappingform;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.CusfieldsmappingForm;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * 系统配置-导入字段映射表单
 *
 * @ClassName: CusfieldsmappingFormService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-02 13:52
 */
public class CusfieldsmappingFormService extends BaseService<CusfieldsmappingForm> {
    
    private final CusfieldsmappingForm dao = new CusfieldsmappingForm().dao();

    @Override
    protected CusfieldsmappingForm dao() {
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
    public Page<CusfieldsmappingForm> getAdminDatas(int pageNumber, int pageSize) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //排序
        sql.desc("iAutoId");
        return paginate(sql);
    }

    /**
     * 保存
     */
    public void save(long iiCusFieldMappingMid, long iformid) {
        CusfieldsmappingForm form = new CusfieldsmappingForm();

        form.setICusFieldMappingMid(iiCusFieldMappingMid);
        form.setIFormId(iformid);

        form.save();
    }

    /**
     * 更新
     */
    public Ret update(CusfieldsmappingForm cusfieldsmappingForm) {
        if (cusfieldsmappingForm == null || notOk(cusfieldsmappingForm.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        CusfieldsmappingForm dbCusfieldsmappingForm = findById(cusfieldsmappingForm.getIAutoId());
        if (dbCusfieldsmappingForm == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(cusfieldsmappingForm.getName(), cusfieldsmappingForm.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = cusfieldsmappingForm.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(cusfieldsmappingForm.getIAutoId(), JBoltUserKit.getUserId(), cusfieldsmappingForm.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param cusfieldsmappingForm 要删除的model
     * @param kv                  携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(CusfieldsmappingForm cusfieldsmappingForm, Kv kv) {
        //addDeleteSystemLog(cusfieldsmappingForm.getIAutoId(), JBoltUserKit.getUserId(),cusfieldsmappingForm.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param cusfieldsmappingForm model
     * @param kv                  携带额外参数一般用不上
     */
    @Override
    public String checkInUse(CusfieldsmappingForm cusfieldsmappingForm, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * 获取格式关联的表单ID
     */
    public List<Long> getIformIdsByMid(long mid) {
        return query(selectSql().select(CusfieldsmappingForm.IFORMID).eq(CusfieldsmappingForm.ICUSFIELDMAPPINGMID, mid));
    }
    
}