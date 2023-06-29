package cn.rjtech.wms.print.printmachine;

import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.common.model.Printmachine;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import static cn.hutool.core.util.StrUtil.COMMA;

/**
 * 打印机器 Service
 * @ClassName: PrintmachineService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-24 14:23
 */
public class PrintmachineService extends BaseService<Printmachine> {

	private final Printmachine dao = new Printmachine().dao();

	@Override
	protected Printmachine dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<Printmachine> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("autoid","desc", pageNumber, pageSize, keywords, "name");
	}

	/**
	 * 保存
	 * @param printmachine
	 * @return
	 */
	public Ret save(Printmachine printmachine) {
		if(printmachine==null || isOk(printmachine.getAutoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			ValidationUtils.isTrue(printmachine.save(), ErrorMsg.SAVE_FAILED);


			// TODO 其他业务代码实现

			return true;
		});

		// 添加日志
		// addSaveSystemLog(printmachine.getAutoid(), JBoltUserKit.getUserId(), printmachine.getName());
		return SUCCESS;
	}

	/**
	 * 更新
	 */
	public Ret update(Printmachine printmachine) {
		if(printmachine==null || notOk(printmachine.getAutoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// 更新时需要判断数据存在
			Printmachine dbPrintmachine = findById(printmachine.getAutoid());
			ValidationUtils.notNull(dbPrintmachine, JBoltMsg.DATA_NOT_EXIST);

			// TODO 其他业务代码实现
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);

			ValidationUtils.isTrue(printmachine.update(), ErrorMsg.UPDATE_FAILED);

			return true;
		});

		//添加日志
		//addUpdateSystemLog(printmachine.getAutoid(), JBoltUserKit.getUserId(), printmachine.getName());
		return SUCCESS;
	}

	/**
	 * 删除 指定多个ID
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
				long AutoID = Long.parseLong(idStr);
				Printmachine dbPrintmachine = findById(AutoID);
				ValidationUtils.notNull(dbPrintmachine, JBoltMsg.DATA_NOT_EXIST);

				// TODO 可能需要补充校验组织账套权限
				// TODO 存在关联使用时，校验是否仍在使用

				ValidationUtils.isTrue(dbPrintmachine.delete(), JBoltMsg.FAIL);
			}

			return true;
		});

		// 添加日志
		// Printmachine printmachine = ret.getAs("data");
		// addDeleteSystemLog(AutoID, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, printmachine.getName());
		return SUCCESS;
	}

	/**
	 * 删除数据后执行的回调
	 * @param printmachine 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(Printmachine printmachine, Kv kv) {
		//addDeleteSystemLog(printmachine.getAutoid(), JBoltUserKit.getUserId(),printmachine.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param printmachine 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanDelete(Printmachine printmachine, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(printmachine, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	//Map<String,String> getPringCodeNameList(@Param("organizeCode") String organizeCode, @Param("PrinterCode") String PrinterCode);
	public Record getPringCodeNameList(String organizeCode, String PrinterCode) {
		Kv paras = Kv.by("organizeCode", organizeCode).set("PrinterCode",PrinterCode);
		return dbTemplate("printmachine.getPringCodeNameList", paras).findFirst();
	}

}