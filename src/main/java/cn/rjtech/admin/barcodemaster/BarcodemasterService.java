package cn.rjtech.admin.barcodemaster;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.common.model.Barcodemaster;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;

/**
 * 条码表 Service
 *
 * @ClassName: BarcodemasterService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-30 13:21
 */
public class BarcodemasterService extends BaseService<Barcodemaster> {

    private final Barcodemaster dao = new Barcodemaster().dao();

    @Override
    protected Barcodemaster dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<Barcodemaster> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 保存
     */
    public Ret save(Barcodemaster barcodemaster) {
        if (barcodemaster == null || isOk(barcodemaster.getAutoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(barcodemaster.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = barcodemaster.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(barcodemaster.getAutoid(), JBoltUserKit.getUserId(), barcodemaster.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(Barcodemaster barcodemaster) {
        if (barcodemaster == null || notOk(barcodemaster.getAutoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        Barcodemaster dbBarcodemaster = findById(barcodemaster.getAutoid());
        if (dbBarcodemaster == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(barcodemaster.getName(), barcodemaster.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = barcodemaster.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(barcodemaster.getAutoid(), JBoltUserKit.getUserId(), barcodemaster.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        return deleteByIds(ids, true);
    }

    /**
     * 删除
     */
    public Ret delete(Long id) {
        return deleteById(id, true);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param barcodemaster 要删除的model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(Barcodemaster barcodemaster, Kv kv) {
        //addDeleteSystemLog(barcodemaster.getAutoid(), JBoltUserKit.getUserId(),barcodemaster.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param barcodemaster 要删除的model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(Barcodemaster barcodemaster, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(barcodemaster, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换ispack属性
     */
    public Ret toggleIspack(Long id) {
        return toggleBoolean(id, "IsPack");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param barcodemaster 要toggle的model
     * @param column        操作的哪一列
     * @param kv            携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(Barcodemaster barcodemaster, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(Barcodemaster barcodemaster, String column, Kv kv) {
        //addUpdateSystemLog(barcodemaster.getAutoid(), JBoltUserKit.getUserId(), barcodemaster.getName(),"的字段["+column+"]值:"+barcodemaster.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param barcodemaster model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    public String checkInUse(Barcodemaster barcodemaster, Kv kv) {
        //这里用来覆盖 检测Barcodemaster是否被其它表引用
        return null;
    }

    /*
     * 传参
     * */
    public void saveBarcodemasterModel(Barcodemaster barcodemaster, Date now) {
        barcodemaster.setAutoid(JBoltSnowflakeKit.me.nextId());
        barcodemaster.setOrganizecode(getOrgCode());
        barcodemaster.setCreateperson(JBoltUserKit.getUserName());
        barcodemaster.setCreatedate(now);
        barcodemaster.setModifyperson(JBoltUserKit.getUserName());
        barcodemaster.setModifydate(now);
        barcodemaster.setMemo("仓库期初");
    }

}