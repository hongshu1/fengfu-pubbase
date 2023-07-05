package cn.rjtech.admin.formuploadd;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.FormUploadD;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 制造工单-上传记录明细
 * @ClassName: FormUploadDService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-29 15:38
 */
public class FormUploadDService extends BaseService<FormUploadD> {
	private final FormUploadD dao=new FormUploadD().dao();
	@Override
	protected FormUploadD dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv para) {
		Page<Record> paginate = dbTemplate("formuploadd.getAdminDatas", para).paginate(pageNumber, pageSize);
		for (Record record : paginate.getList()) {
			record.set("cattachments2",record.getStr("cattachments"));
		}
		return paginate;
	}

	/**
	 * 保存
	 * @param formUploadD
	 * @return
	 */
	public Ret save(FormUploadD formUploadD) {
		if(formUploadD==null || isOk(formUploadD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(formUploadD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formUploadD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(formUploadD.getIAutoId(), JBoltUserKit.getUserId(), formUploadD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param formUploadD
	 * @return
	 */
	public Ret update(FormUploadD formUploadD) {
		if(formUploadD==null || notOk(formUploadD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		FormUploadD dbFormUploadD=findById(formUploadD.getIAutoId());
		if(dbFormUploadD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(formUploadD.getName(), formUploadD.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=formUploadD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(formUploadD.getIAutoId(), JBoltUserKit.getUserId(), formUploadD.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param formUploadD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(FormUploadD formUploadD, Kv kv) {
		//addDeleteSystemLog(formUploadD.getIAutoId(), JBoltUserKit.getUserId(),formUploadD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param formUploadD model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(FormUploadD formUploadD, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * 删除
	 */
	public Ret delete(Long id) {
		deleteById(id);
		return SUCCESS;
	}
	/**
	 * 根据主id
	 */
	public List<Record> findByPid(Long pid){
	return 	dbTemplate("formuploadd.getAdminDatas",Kv.by("pid",pid)).find();
	}

	/**
	 * 根据主id API接口使用
	 */
	public Page<Record> findByPid2(Integer pageNumber, Integer pageSize,Long pid){
		return 	dbTemplate("formuploadd.getAdminDatas",Kv.by("pid",pid)).paginate(pageNumber,pageSize);
	}

	public Ret delete2(Long iautoid) {
		deleteById(iautoid);
		return SUCCESS;
	}
}