package cn.rjtech.admin.RcvDocDefect;

import cn.hutool.core.date.DateUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.RcvDocDefect;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;

/**
 * 来料异常品记录 Service
 * @ClassName: RcvDocDefectService
 * @author: RJ
 * @date: 2023-04-18 16:36
 */
public class RcvDocDefectService extends BaseService<RcvDocDefect> {

	private final RcvDocDefect dao = new RcvDocDefect().dao();

	@Override
	protected RcvDocDefect dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageSize, int pageNumber, Okv kv) {
		return dbTemplate(dao()._getDataSourceConfigName(), "RcvDocDefect.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
	}


	/**
	 * 保存
	 * @param rcvDocDefect
	 * @return
	 */
	public Ret save(RcvDocDefect rcvDocDefect) {
		if(rcvDocDefect==null || isOk(rcvDocDefect.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(rcvDocDefect.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=rcvDocDefect.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(rcvDocDefect.getIautoid(), JBoltUserKit.getUserId(), rcvDocDefect.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param rcvDocDefect
	 * @return
	 */
	public Ret update(RcvDocDefect rcvDocDefect) {
		if(rcvDocDefect==null || notOk(rcvDocDefect.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		RcvDocDefect dbRcvDocDefect=findById(rcvDocDefect.getIAutoId());
		if(dbRcvDocDefect==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(rcvDocDefect.getName(), rcvDocDefect.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=rcvDocDefect.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(rcvDocDefect.getIautoid(), JBoltUserKit.getUserId(), rcvDocDefect.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		return deleteByIds(ids,true);
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		return deleteById(id,true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param rcvDocDefect 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(RcvDocDefect rcvDocDefect, Kv kv) {
		//addDeleteSystemLog(rcvDocDefect.getIautoid(), JBoltUserKit.getUserId(),rcvDocDefect.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param rcvDocDefect 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(RcvDocDefect rcvDocDefect, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(rcvDocDefect, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
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
	 * @param rcvDocDefect 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(RcvDocDefect rcvDocDefect,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(RcvDocDefect rcvDocDefect, String column, Kv kv) {
		//addUpdateSystemLog(rcvDocDefect.getIautoid(), JBoltUserKit.getUserId(), rcvDocDefect.getName(),"的字段["+column+"]值:"+rcvDocDefect.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param rcvDocDefect model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(RcvDocDefect rcvDocDefect, Kv kv) {
		//这里用来覆盖 检测RcvDocDefect是否被其它表引用
		return null;
	}


	//更新状态并保存数据方法
	public Ret updateEditTable(JBoltTable jBoltTable, Kv formRecord) {
		Date now = new Date();

		tx(() -> {
				//判断是否有主键id
				if(isOk(formRecord.getStr("rcvDocDefect.iautoid"))){
					RcvDocDefect rcvDocDefect = findById(formRecord.getLong("rcvDocDefect.iautoid"));
					if (rcvDocDefect.getIStatus() == 2){
						//录入数据
						rcvDocDefect.setCApproach(formRecord.getStr("rcvDocDefect.capproach"));
						rcvDocDefect.setIStatus(3);
						//更新人和时间
						rcvDocDefect.setIUpdateBy(JBoltUserKit.getUserId());
						rcvDocDefect.setCUpdateName(JBoltUserKit.getUserName());
						rcvDocDefect.setDUpdateTime(now);

					}else {
						//录入数据
						rcvDocDefect.setIRespType(formRecord.getInt("rcvDocDefect.iresptype"));
						rcvDocDefect.setIsFirstTime(formRecord.getBoolean("rcvDocDefect.isfirsttime"));
						rcvDocDefect.setCBadnessSns(formRecord.getStr("rcvDocDefect.cbadnesssns"));
						rcvDocDefect.setCDesc(formRecord.getStr("rcvDocDefect.cdesc"));
						rcvDocDefect.setIStatus(2);
						//更新人和时间
						rcvDocDefect.setIUpdateBy(JBoltUserKit.getUserId());
						rcvDocDefect.setCUpdateName(JBoltUserKit.getUserName());
						rcvDocDefect.setDUpdateTime(now);
					}
					rcvDocDefect.update();
				}else{
					//保存未有主键的数据

				}
				return true;
			});


		return SUCCESS;
	}


	//未有主键的保存方法
	public void RcvDocfectLinesave(Kv formRecord, Date now){
		System.out.println("formRecord==="+formRecord);
		System.out.println("now==="+now);
		RcvDocDefect rcvDocDefect = new RcvDocDefect();
		rcvDocDefect.setIAutoId(formRecord.getLong("rcvDocDefect.iautoid"));
		rcvDocDefect.setIStatus(1);
		rcvDocDefect.setIRespType(0);
		rcvDocDefect.setIRespType(formRecord.getInt("rcvDocDefect.iresptype"));
		rcvDocDefect.setIsFirstTime(formRecord.getBoolean("rcvDocDefect.isfirsttime"));
		rcvDocDefect.setCBadnessSns(formRecord.getStr("rcvDocDefect.cbadnesssns"));
		rcvDocDefect.setCDesc(formRecord.getStr("rcvDocDefect.cdesc"));

		rcvDocDefect.setIAutoId(JBoltSnowflakeKit.me.nextId());
		String billNo = BillNoUtils.getcDocNo(getOrgId(), "YCP", 5);
		rcvDocDefect.setCDocNo(billNo);


		rcvDocDefect.setIOrgId(getOrgId());
		rcvDocDefect.setCOrgCode(getOrgCode());
		rcvDocDefect.setCOrgName(getOrgName());
		rcvDocDefect.setICreateBy(JBoltUserKit.getUserId());
		rcvDocDefect.setCCreateName(JBoltUserKit.getUserName());
		rcvDocDefect.setDCreateTime(now);
		rcvDocDefect.setIUpdateBy(JBoltUserKit.getUserId());
		rcvDocDefect.setCUpdateName(JBoltUserKit.getUserName());
		rcvDocDefect.setDUpdateTime(now);
		rcvDocDefect.save();
	}

}