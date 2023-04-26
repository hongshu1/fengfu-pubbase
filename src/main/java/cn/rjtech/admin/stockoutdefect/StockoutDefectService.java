package cn.rjtech.admin.stockoutdefect;

import java.math.BigDecimal;
import java.util.Date;

import com.jfinal.plugin.activerecord.Page;

import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;

import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.StockoutDefect;
import cn.rjtech.model.momdata.StockoutQcFormM;

/**
 * 质量管理-出库异常品记录 Service
 *
 * @ClassName: StockoutDefectService
 * @author: RJ
 * @date: 2023-04-26 11:59
 */
public class StockoutDefectService extends BaseService<StockoutDefect> {

    private final StockoutDefect dao = new StockoutDefect().dao();

    @Override
    protected StockoutDefect dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<StockoutDefect> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    /**
     * 保存
     */
    public Ret save(StockoutDefect stockoutDefect) {
        if (stockoutDefect == null || isOk(stockoutDefect.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(stockoutDefect.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = stockoutDefect.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(stockoutDefect.getIAutoId(), JBoltUserKit.getUserId(), stockoutDefect.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(StockoutDefect stockoutDefect) {
        if (stockoutDefect == null || notOk(stockoutDefect.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        StockoutDefect dbStockoutDefect = findById(stockoutDefect.getIAutoId());
        if (dbStockoutDefect == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(stockoutDefect.getName(), stockoutDefect.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = stockoutDefect.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(stockoutDefect.getIAutoId(), JBoltUserKit.getUserId(), stockoutDefect.getName());
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
     * @param stockoutDefect 要删除的model
     * @param kv             携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(StockoutDefect stockoutDefect, Kv kv) {
        //addDeleteSystemLog(stockoutDefect.getIAutoId(), JBoltUserKit.getUserId(),stockoutDefect.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param stockoutDefect 要删除的model
     * @param kv             携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(StockoutDefect stockoutDefect, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(stockoutDefect, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换isfirsttime属性
     */
    public Ret toggleIsFirstTime(Long id) {
        return toggleBoolean(id, "isFirstTime");
    }

    /**
     * 切换isdeleted属性
     */
    public Ret toggleIsDeleted(Long id) {
        return toggleBoolean(id, "IsDeleted");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param stockoutDefect 要toggle的model
     * @param column         操作的哪一列
     * @param kv             携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(StockoutDefect stockoutDefect, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(StockoutDefect stockoutDefect, String column, Kv kv) {
        //addUpdateSystemLog(stockoutDefect.getIAutoId(), JBoltUserKit.getUserId(), stockoutDefect.getName(),"的字段["+column+"]值:"+stockoutDefect.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param stockoutDefect model
     * @param kv             携带额外参数一般用不上
     */
    @Override
    public String checkInUse(StockoutDefect stockoutDefect, Kv kv) {
        //这里用来覆盖 检测StockoutDefect是否被其它表引用
        return null;
    }

    public void savestockoutDefectmodel(StockoutDefect stockoutDefect, StockoutQcFormM stockoutQcFormM) {
        stockoutDefect.setIAutoId(JBoltSnowflakeKit.me.nextId());
        Date date = new Date();
        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        stockoutDefect.setCDocNo(stockoutQcFormM.getCStockoutQcFormNo());   //异常品单号
        stockoutDefect.setIStockoutQcFormMid(stockoutQcFormM.getIAutoId()); //出库检id
        stockoutDefect.setIInventoryId(stockoutQcFormM.getIInventoryId());  //存货ID
        stockoutDefect.setIStatus(1);                                       //状态：1. 待记录 2. 待判定 3. 已完成
        stockoutDefect.setIDqQty(new BigDecimal(0));                        //不合格数量
        stockoutDefect.setCDesc(stockoutQcFormM.getCMemo());                //不良内容描述
        stockoutDefect.setIQcUserId(stockoutQcFormM.getIQcUserId());        //检验用户ID
        stockoutDefect.setDQcTime(stockoutQcFormM.getDUpdateTime());        //检验时间

        stockoutDefect.setICreateBy(userId);
        stockoutDefect.setDCreateTime(date);
        stockoutDefect.setCCreateName(userName);
        stockoutDefect.setIOrgId(getOrgId());
        stockoutDefect.setCOrgCode(getOrgCode());
        stockoutDefect.setCOrgName(getOrgName());
        stockoutDefect.setIUpdateBy(userId);
        stockoutDefect.setCUpdateName(userName);
        stockoutDefect.setDUpdateTime(date);
    }

    /*
     * 根据出货检id查询异常品质单
     * */
    public StockoutDefect findStockoutDefectByiStockoutQcFormMid(Object iStockoutQcFormMid) {
        return findFirst("SELECT * FROM PL_StockoutDefect WHERE iStockoutQcFormMid = ?", iStockoutQcFormMid);
    }

}