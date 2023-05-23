package cn.rjtech.admin.sysscandeliver;

import cn.rjtech.util.ValidationUtils;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SysScandeliverdetail;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 扫码发货明细
 * @ClassName: SysScandeliverdetailService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-22 09:48
 */
public class SysScandeliverdetailService extends BaseService<SysScandeliverdetail> {
	private final SysScandeliverdetail dao=new SysScandeliverdetail().dao();
	@Override
	protected SysScandeliverdetail dao() {
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
     * @param TrackType 跟单类型
     * @param SourceBillType 来源单据类型
     * @param isDeleted 删除状态
	 * @return
	 */
	public Page<SysScandeliverdetail> getAdminDatas(int pageNumber, int pageSize, String TrackType, String SourceBillType, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("TrackType",TrackType);
        sql.eq("SourceBillType",SourceBillType);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //排序
        sql.desc("AutoID");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param sysScandeliverdetail
	 * @return
	 */
	public Ret save(SysScandeliverdetail sysScandeliverdetail) {
		if(sysScandeliverdetail==null || isOk(sysScandeliverdetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(sysScandeliverdetail.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysScandeliverdetail.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(sysScandeliverdetail.getAutoID(), JBoltUserKit.getUserId(), sysScandeliverdetail.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param sysScandeliverdetail
	 * @return
	 */
	public Ret update(SysScandeliverdetail sysScandeliverdetail) {
		if(sysScandeliverdetail==null || notOk(sysScandeliverdetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SysScandeliverdetail dbSysScandeliverdetail=findById(sysScandeliverdetail.getAutoID());
		if(dbSysScandeliverdetail==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(sysScandeliverdetail.getName(), sysScandeliverdetail.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysScandeliverdetail.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(sysScandeliverdetail.getAutoID(), JBoltUserKit.getUserId(), sysScandeliverdetail.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param sysScandeliverdetail 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SysScandeliverdetail sysScandeliverdetail, Kv kv) {
		//addDeleteSystemLog(sysScandeliverdetail.getAutoID(), JBoltUserKit.getUserId(),sysScandeliverdetail.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param sysScandeliverdetail model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SysScandeliverdetail sysScandeliverdetail, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SysScandeliverdetail sysScandeliverdetail, String column, Kv kv) {
		//addUpdateSystemLog(sysScandeliverdetail.getAutoID(), JBoltUserKit.getUserId(), sysScandeliverdetail.getName(),"的字段["+column+"]值:"+sysScandeliverdetail.get(column));
		/**
		switch(column){
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}
	public List<Record> findEditTableDatas(String autoid) {
		System.out.println(autoid);
		ValidationUtils.notNull(autoid, JBoltMsg.PARAM_ERROR);
		Kv para = new Kv();
		para.set("masid",autoid);
		List<Record> records = dbTemplate("sysscandeliver.dList", para).find();
		return records;
	}

}