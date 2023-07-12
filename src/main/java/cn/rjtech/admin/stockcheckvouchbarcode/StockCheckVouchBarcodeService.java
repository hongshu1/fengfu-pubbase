package cn.rjtech.admin.stockcheckvouchbarcode;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.StockCheckVouchBarcode;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * 库存盘点-条码明细 Service
 *
 * @ClassName: StockCheckVouchBarcodeService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-30 11:16
 */
public class StockCheckVouchBarcodeService extends BaseService<StockCheckVouchBarcode> {

    private final StockCheckVouchBarcode dao = new StockCheckVouchBarcode().dao();

    @Override
    protected StockCheckVouchBarcode dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<StockCheckVouchBarcode> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 保存
     */
    public Ret save(StockCheckVouchBarcode stockCheckVouchBarcode) {
        if (stockCheckVouchBarcode == null || isOk(stockCheckVouchBarcode.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(stockCheckVouchBarcode.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = stockCheckVouchBarcode.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(stockCheckVouchBarcode.getAutoID(), JBoltUserKit.getUserId(), stockCheckVouchBarcode.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(StockCheckVouchBarcode stockCheckVouchBarcode) {
        if (stockCheckVouchBarcode == null || notOk(stockCheckVouchBarcode.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        StockCheckVouchBarcode dbStockCheckVouchBarcode = findById(stockCheckVouchBarcode.getAutoID());
        if (dbStockCheckVouchBarcode == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(stockCheckVouchBarcode.getName(), stockCheckVouchBarcode.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = stockCheckVouchBarcode.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(stockCheckVouchBarcode.getAutoID(), JBoltUserKit.getUserId(), stockCheckVouchBarcode.getName());
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
     * @param stockCheckVouchBarcode 要删除的model
     * @param kv                     携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(StockCheckVouchBarcode stockCheckVouchBarcode, Kv kv) {
        //addDeleteSystemLog(stockCheckVouchBarcode.getAutoID(), JBoltUserKit.getUserId(),stockCheckVouchBarcode.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param stockCheckVouchBarcode 要删除的model
     * @param kv                     携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(StockCheckVouchBarcode stockCheckVouchBarcode, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(stockCheckVouchBarcode, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换isdeleted属性
     */
    public Ret toggleIsDeleted(Long id) {
        return toggleBoolean(id, "isDeleted");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param stockCheckVouchBarcode 要toggle的model
     * @param column                 操作的哪一列
     * @param kv                     携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(StockCheckVouchBarcode stockCheckVouchBarcode, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(StockCheckVouchBarcode stockCheckVouchBarcode, String column, Kv kv) {
        //addUpdateSystemLog(stockCheckVouchBarcode.getAutoID(), JBoltUserKit.getUserId(), stockCheckVouchBarcode.getName(),"的字段["+column+"]值:"+stockCheckVouchBarcode.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param stockCheckVouchBarcode model
     * @param kv                     携带额外参数一般用不上
     */
    @Override
    public String checkInUse(StockCheckVouchBarcode stockCheckVouchBarcode, Kv kv) {
        //这里用来覆盖 检测StockCheckVouchBarcode是否被其它表引用
        return null;
    }

    public List<StockCheckVouchBarcode> findListByMasId(Long masid) {
        return find("select * from T_Sys_StockCheckVouchBarcode where masid=?", masid);
    }

}