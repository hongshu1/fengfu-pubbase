package cn.rjtech.admin.subjectd;

import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.Subjectd;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

import static cn.hutool.core.util.StrUtil.COMMA;
/**
 * 科目对照细表 Service
 * @ClassName: SubjectdService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-01 13:52
 */
public class SubjectdService extends BaseService<Subjectd> {

	private final Subjectd dao = new Subjectd().dao();

	@Override
	protected Subjectd dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<Subjectd> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iautoid","desc", pageNumber, pageSize, keywords, "name");
	}

	/**
	 * 保存
	 * @param subjectd
	 * @return
	 */
	public Ret save(Subjectd subjectd) {
		if(subjectd==null || isOk(subjectd.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			ValidationUtils.isTrue(subjectd.save(), ErrorMsg.SAVE_FAILED);


			// TODO 其他业务代码实现

			return true;
		});

		// 添加日志
		// addSaveSystemLog(subjectd.getIautoid(), JBoltUserKit.getUserId(), subjectd.getName());
		return SUCCESS;
	}

	/**
	 * 更新
	 */
	public Ret update(Subjectd subjectd) {
		if(subjectd==null || notOk(subjectd.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// 更新时需要判断数据存在
			Subjectd dbSubjectd = findById(subjectd.getIautoid());
			ValidationUtils.notNull(dbSubjectd, JBoltMsg.DATA_NOT_EXIST);

			// TODO 其他业务代码实现
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);

			ValidationUtils.isTrue(subjectd.update(), ErrorMsg.UPDATE_FAILED);

			return true;
		});

		//添加日志
		//addUpdateSystemLog(subjectd.getIautoid(), JBoltUserKit.getUserId(), subjectd.getName());
		return SUCCESS;
	}

	/**
	 * 删除 指定多个ID
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
				long iAutoId = Long.parseLong(idStr);
				Subjectd dbSubjectd = findById(iAutoId);
				ValidationUtils.notNull(dbSubjectd, JBoltMsg.DATA_NOT_EXIST);

				// TODO 可能需要补充校验组织账套权限
				// TODO 存在关联使用时，校验是否仍在使用

				ValidationUtils.isTrue(dbSubjectd.delete(), JBoltMsg.FAIL);
			}

			return true;
		});

		// 添加日志
		// Subjectd subjectd = ret.getAs("data");
		// addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, subjectd.getName());
		return SUCCESS;
	}

	/**
	 * 删除数据后执行的回调
	 * @param subjectd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(Subjectd subjectd, Kv kv) {
		//addDeleteSystemLog(subjectd.getIautoid(), JBoltUserKit.getUserId(),subjectd.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param subjectd 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanDelete(Subjectd subjectd, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(subjectd, kv);
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
	 * 根据主表id获取数据
	 */
	public List<Subjectd> findBySubjectMId (Long isubjectmid){
		return  find("select * from Bas_SubjectD where iSubjectMId =?",isubjectmid);
	}

	/**
	 * 拼接科目名称
	 */
	public String jointSubjectName(Long isubjectmid){
		List<Subjectd> bySubjectMId = findBySubjectMId(isubjectmid);
		//拼接字符串
		StringBuilder cdepname = new StringBuilder();
		for (Subjectd subjectd : bySubjectMId) {
			cdepname.append(subjectd.getCsubjectname()).append(",");
		}
		if (cdepname.length() > 0) {
			cdepname = new StringBuilder(cdepname.substring(0, cdepname.length() - 1));
		}
		return cdepname.toString();
	}
	/**
	 * 拼接科目编码
	 */
	public String jointSubjectCode(Long isubjectmid) {
		List<Subjectd> bySubjectMId = findBySubjectMId(isubjectmid);
		//拼接字符串
		StringBuilder code = new StringBuilder();
		for (Subjectd subjectd : bySubjectMId) {
			code.append(subjectd.getCsubjectcode()).append(",");
		}
		if (code.length() > 0) {
			code = new StringBuilder(code.substring(0, code.length() - 1));
		}
		return code.toString();
	}
}