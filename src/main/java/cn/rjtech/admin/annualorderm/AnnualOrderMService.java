package cn.rjtech.admin.annualorderm;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.annualorderd.AnnualOrderDService;
import cn.rjtech.admin.annualorderdamount.AnnualorderdAmountService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.AnnualOrderD;
import cn.rjtech.model.momdata.AnnualOrderM;
import cn.rjtech.model.momdata.AnnualorderdAmount;
import cn.rjtech.util.ValidationUtils;

/**
 * 年度计划订单
 *
 * @ClassName: AnnualOrderMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-23 17:23
 */
public class AnnualOrderMService extends BaseService<AnnualOrderM> {
    private final AnnualOrderM dao = new AnnualOrderM().dao();
    @Inject
    private AnnualOrderDService annualOrderDService;
    @Inject
    private AnnualorderdAmountService annualorderdAmountService;
    @Override
    protected AnnualOrderM dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }
    /**
     * 后台管理数据查询
     */
    public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
    	para.set("iorgid",getOrgId());
    	Page<Record> pageList = dbTemplate("annualorderm.paginateAdminDatas",para).paginate(pageNumber, pageSize);
    	return pageList;
    }

    /**
     * 保存
     *
     * @param annualOrderM
     * @return
     */
    public Ret save(AnnualOrderM annualOrderM) {
        if (annualOrderM == null || isOk(annualOrderM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(annualOrderM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = annualOrderM.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(annualOrderM.getIAutoId(), JBoltUserKit.getUserId(), annualOrderM.getName());
        }
        return ret(success);
    }
	/**
	 * 删除
	 */
	public Ret delete(Long id) {
		return updateColumn(id, "isdeleted", true);
	}
    /**
     * 更新
     *
     * @param annualOrderM
     * @return
     */
    public Ret update(AnnualOrderM annualOrderM) {
        if (annualOrderM == null || notOk(annualOrderM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        AnnualOrderM dbAnnualOrderM = findById(annualOrderM.getIAutoId());
        if (dbAnnualOrderM == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(annualOrderM.getName(), annualOrderM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = annualOrderM.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(annualOrderM.getIAutoId(), JBoltUserKit.getUserId(), annualOrderM.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param annualOrderM 要删除的model
     * @param kv           携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(AnnualOrderM annualOrderM, Kv kv) {
        //addDeleteSystemLog(annualOrderM.getIAutoId(), JBoltUserKit.getUserId(),annualOrderM.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param annualOrderM model
     * @param kv           携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkInUse(AnnualOrderM annualOrderM, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * 执行JBoltTable表格整体提交
     *
     * @param jBoltTable
     * @return
     */
    public Ret submitByJBoltTable(JBoltTable jBoltTable) {
    	AnnualOrderM annualOrderM = jBoltTable.getFormModel(AnnualOrderM.class,"annualOrderM");
    	User user = JBoltUserKit.getUser();
    	Date now = new Date();
    	tx(()->{
	    	if(annualOrderM.getIAutoId() == null){
		    	annualOrderM.setIOrgId(getOrgId());
		    	annualOrderM.setCOrgCode(getOrgCode());
		    	annualOrderM.setCOrgName(getOrgName());
		    	annualOrderM.setICreateBy(user.getId());
		    	annualOrderM.setCCreateName(user.getName());
		    	annualOrderM.setDCreateTime(now);
		    	annualOrderM.setIUpdateBy(user.getId());
		    	annualOrderM.setCUpdateName(user.getName());
		    	annualOrderM.setDUpdateTime(now);
		    	ValidationUtils.isTrue(annualOrderM.save(), ErrorMsg.SAVE_FAILED);
	    	}else{
	    		annualOrderM.setIUpdateBy(user.getId());
		    	annualOrderM.setCUpdateName(user.getName());
		    	annualOrderM.setDUpdateTime(now);
		    	ValidationUtils.isTrue(annualOrderM.update(), ErrorMsg.UPDATE_FAILED);
	    	}
	    	saveTableSubmitDatas(jBoltTable,annualOrderM);
	    	updateTableSubmitDatas(jBoltTable,annualOrderM);
	    	deleteTableSubmitDatas(jBoltTable);
	    	return true;
    	});
        return SUCCESS;
    }
    //可编辑表格提交-新增数据
    private void saveTableSubmitDatas(JBoltTable jBoltTable,AnnualOrderM annualOrderM){
    	List<Record> list = jBoltTable.getSaveRecordList();
    	if(CollUtil.isEmpty(list)) return;
    	for (int i=0;i<list.size();i++) {
    		Record record = list.get(i);
			AnnualOrderD annualOrderD = new AnnualOrderD();
			annualOrderD.setIAnnualOrderMid(annualOrderM.getIAutoId());
			annualOrderD.setIInventoryId(record .getLong("iinventoryid"));
			annualOrderD.setIYear1(annualOrderM.getIYear());
			annualOrderD.setIYear1Sum(record.getBigDecimal("inowyearmonthamounttotal"));
			annualOrderD.setIYear2(annualOrderM.getIYear()+1);
			annualOrderD.setIYear2Sum(record.getBigDecimal("inextyearmonthamounttotal"));
			annualOrderD.setIsDeleted(false);
			ValidationUtils.isTrue(annualOrderD.save(), ErrorMsg.SAVE_FAILED);
			saveAnnualOrderDModel(record,annualOrderM,annualOrderD);
		}
    	return;
    }
    //可编辑表格提交-修改数据
    private void updateTableSubmitDatas(JBoltTable jBoltTable,AnnualOrderM annualOrderM){
    	List<Record> list = jBoltTable.getUpdateRecordList();
    	if(CollUtil.isEmpty(list)) return;
    	for(int i = 0;i < list.size(); i++){
    		Record record = list.get(i);
			Long iAnnualOrderDid = record.getLong("iautoid");
			AnnualOrderD annualOrderD = annualOrderDService.findById(iAnnualOrderDid);
			ValidationUtils.notNull(annualOrderD, JBoltMsg.DATA_NOT_EXIST);
			annualOrderD.setIInventoryId(record .getLong("iinventoryid"));
			annualOrderD.setIYear1(annualOrderM.getIYear());
			annualOrderD.setIYear1Sum(record.getBigDecimal("inowyearmonthamounttotal"));
			annualOrderD.setIYear2(annualOrderM.getIYear()+1);
			annualOrderD.setIYear2Sum(record.getBigDecimal("inextyearmonthamounttotal"));
			ValidationUtils.isTrue(annualOrderD.update(), ErrorMsg.UPDATE_FAILED);
			annualorderdAmountService.deleteBy(Okv.by("iannualorderdid", annualOrderD.getIAutoId()));
			saveAnnualOrderDModel(record,annualOrderM,annualOrderD);
    	}
    }
    //可编辑表格提交-删除数据
    private void deleteTableSubmitDatas(JBoltTable jBoltTable){
    	Object[] ids = jBoltTable.getDelete();
    	if(ArrayUtil.isEmpty(ids)) return;
    	for (Object id : ids) {
    		annualorderdAmountService.deleteBy(Okv.by("iannualorderdid", id));
			annualOrderDService.deleteById(id);
		}
    }
    private void saveAnnualOrderDModel(Record record,AnnualOrderM annualOrderM,AnnualOrderD annualOrderD){
    	AnnualorderdAmount annualorderdAmount;
		for (int j=1; j <= 12; j++) {
			annualorderdAmount = new AnnualorderdAmount();
			annualorderdAmount.setIAnnualOrderDid(annualOrderD.getIAutoId());
			annualorderdAmount.setIYear(annualOrderM.getIYear());
			annualorderdAmount.setIMonth(j);
			BigDecimal inowyearmonthamount= record.getBigDecimal("inowyearmonthamount"+j);
			annualorderdAmount.setIAmount(inowyearmonthamount == null ? BigDecimal.ZERO : inowyearmonthamount);
			annualorderdAmount.setIsDeleted(false);
			ValidationUtils.isTrue(annualorderdAmount.save(), ErrorMsg.SAVE_FAILED);
		}
		for (int m=1; m <= 3; m++) {
			annualorderdAmount = new AnnualorderdAmount();
			annualorderdAmount.setIAnnualOrderDid(annualOrderD.getIAutoId());
			annualorderdAmount.setIYear(annualOrderM.getIYear()+1);
			annualorderdAmount.setIMonth(m);
			BigDecimal inowyearmonthamount= record.getBigDecimal("inextyearmonthamount"+m);
			annualorderdAmount.setIAmount(inowyearmonthamount == null ? BigDecimal.ZERO : inowyearmonthamount);
			annualorderdAmount.setIsDeleted(false);
			ValidationUtils.isTrue(annualorderdAmount.save(), ErrorMsg.SAVE_FAILED);
		}
    }
    /**
     * 根据年份生成动态日期头
     */
    public void dateHeader(int iYear) {



    }

}