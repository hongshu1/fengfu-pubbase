package cn.rjtech.admin.sysmaterialspreparedetail;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SysMaterialspreparedetail;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 备料单明细
 * @ClassName: SysMaterialspreparedetailService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-01 23:50
 */
public class SysMaterialspreparedetailService extends BaseService<SysMaterialspreparedetail> {
	private final SysMaterialspreparedetail dao=new SysMaterialspreparedetail().dao();
	@Override
	protected SysMaterialspreparedetail dao() {
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
     * @param SourceBillType 来源单据类型
     * @param State 状态：1=已扫描，0=未扫描
	 * @return
	 */
	public Page<SysMaterialspreparedetail> getAdminDatas(int pageNumber, int pageSize, String SourceBillType, String State) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("SourceBillType",SourceBillType);
        sql.eq("State",State);
        //排序
        sql.desc("AutoID");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param sysMaterialspreparedetail
	 * @return
	 */
	public Ret save(SysMaterialspreparedetail sysMaterialspreparedetail) {
		if(sysMaterialspreparedetail==null || isOk(sysMaterialspreparedetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=sysMaterialspreparedetail.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(sysMaterialspreparedetail.getAutoID(), JBoltUserKit.getUserId(), sysMaterialspreparedetail.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param sysMaterialspreparedetail
	 * @return
	 */
	public Ret update(SysMaterialspreparedetail sysMaterialspreparedetail) {
		if(sysMaterialspreparedetail==null || notOk(sysMaterialspreparedetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SysMaterialspreparedetail dbSysMaterialspreparedetail=findById(sysMaterialspreparedetail.getAutoID());
		if(dbSysMaterialspreparedetail==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=sysMaterialspreparedetail.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(sysMaterialspreparedetail.getAutoID(), JBoltUserKit.getUserId(), sysMaterialspreparedetail.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param sysMaterialspreparedetail 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SysMaterialspreparedetail sysMaterialspreparedetail, Kv kv) {
		//addDeleteSystemLog(sysMaterialspreparedetail.getAutoID(), JBoltUserKit.getUserId(),sysMaterialspreparedetail.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param sysMaterialspreparedetail model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SysMaterialspreparedetail sysMaterialspreparedetail, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	public List<Record> cworkname() {
		return dbTemplate("materialsprepare.cworkname", Kv.of("isenabled", "1")).find();
	}

	public List<Record> cworkshiftcode() {
		return dbTemplate("materialsprepare.cworkshiftcode", Kv.of("isenabled", "1")).find();
	}

	public Page<Record> getMaterialsdetials(int pageNumber, int pageSize, Kv kv) {
		Page<Record> paginate = dbTemplate("materialsprepare.recpordetail", kv).paginate(pageNumber, pageSize);
		return paginate;
	}

	public Page<Record> getMaterialsdetials1(int pageNumber, int pageSize, Kv kv) {
		Page<Record> paginate = dbTemplate("materialsprepare.recpor1", kv).paginate(pageNumber, pageSize);
		return paginate;
	}

	public Page<Record> getBarcode(int pageNumber, int pageSize, Kv kv) {
		Page<Record> paginate = dbTemplate("materialsprepare.barcode", kv).paginate(pageNumber, pageSize);
		return paginate;
	}
}