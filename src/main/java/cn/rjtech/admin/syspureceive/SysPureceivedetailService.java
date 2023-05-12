package cn.rjtech.admin.syspureceive;

import cn.rjtech.util.ValidationUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SysPureceivedetail;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 采购收料单明细
 * @ClassName: SysPureceivedetailService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-10 10:01
 */
public class SysPureceivedetailService extends BaseService<SysPureceivedetail> {
	private final SysPureceivedetail dao=new SysPureceivedetail().dao();
	@Override
	protected SysPureceivedetail dao() {
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
     * @param SourceBillType 来源类型;PO 采购 OM委外
     * @param TrackType 跟单类型
     * @param IsDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<SysPureceivedetail> getAdminDatas(int pageNumber, int pageSize, String SourceBillType, String TrackType, Boolean IsDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("SourceBillType",SourceBillType);
        sql.eq("TrackType",TrackType);
        sql.eqBooleanToChar("IsDeleted",IsDeleted);
        //排序
        sql.desc("AutoID");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param sysPureceivedetail
	 * @return
	 */
	public Ret save(SysPureceivedetail sysPureceivedetail) {
		if(sysPureceivedetail==null || isOk(sysPureceivedetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(sysPureceivedetail.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysPureceivedetail.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(sysPureceivedetail.getAutoID(), JBoltUserKit.getUserId(), sysPureceivedetail.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param sysPureceivedetail
	 * @return
	 */
	public Ret update(SysPureceivedetail sysPureceivedetail) {
		if(sysPureceivedetail==null || notOk(sysPureceivedetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SysPureceivedetail dbSysPureceivedetail=findById(sysPureceivedetail.getAutoID());
		if(dbSysPureceivedetail==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(sysPureceivedetail.getName(), sysPureceivedetail.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysPureceivedetail.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(sysPureceivedetail.getAutoID(), JBoltUserKit.getUserId(), sysPureceivedetail.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param sysPureceivedetail 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SysPureceivedetail sysPureceivedetail, Kv kv) {
		//addDeleteSystemLog(sysPureceivedetail.getAutoID(), JBoltUserKit.getUserId(),sysPureceivedetail.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param sysPureceivedetail model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SysPureceivedetail sysPureceivedetail, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SysPureceivedetail sysPureceivedetail, String column, Kv kv) {
		//addUpdateSystemLog(sysPureceivedetail.getAutoID(), JBoltUserKit.getUserId(), sysPureceivedetail.getName(),"的字段["+column+"]值:"+sysPureceivedetail.get(column));
		/**
		switch(column){
		    case "IsDeleted":
		        break;
		}
		*/
		return null;
	}
	public List<Record> findEditTableDatas(Kv para) {
		ValidationUtils.notNull(para.getLong("masid"), JBoltMsg.PARAM_ERROR);
		List<Record> records = dbTemplate("syspureceive.dList", para).find();

		return records;
	}

	/**
	 * 批量删除主从表
	 */
	public Ret deleteRmRdByIds(String ids) {
		tx(() -> {
			String[] split = ids.split(",");
			for(String s : split){
				updateColumn(s, "isdeleted", true);
			}
			return true;
		});
		return ret(true);
	}
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		updateColumn(id, "isdeleted", true);
		return ret(true);
	}

	public List<Record> getwhname(String id) {
		Record first = Db.use(u8SourceConfigName()).findFirst("select * from V_Sys_WareHouse where WhCode=?", id);
		return null;
	}

}