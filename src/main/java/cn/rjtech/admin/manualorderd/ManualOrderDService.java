package cn.rjtech.admin.manualorderd;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.ManualOrderD;
import cn.rjtech.model.momdata.ManualOrderM;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 客户订单-手配订单明细
 * @ClassName: ManualOrderDService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 15:33
 */
public class ManualOrderDService extends BaseService<ManualOrderD> {
	private final ManualOrderD dao=new ManualOrderD().dao();
	@Override
	protected ManualOrderD dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 保存
	 * @param manualOrderD
	 * @return
	 */
	public Ret save(ManualOrderD manualOrderD) {
		if(manualOrderD==null || isOk(manualOrderD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(manualOrderD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=manualOrderD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(manualOrderD.getIAutoId(), JBoltUserKit.getUserId(), manualOrderD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param manualOrderD
	 * @return
	 */
	public Ret update(ManualOrderD manualOrderD) {
		if(manualOrderD==null || notOk(manualOrderD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ManualOrderD dbManualOrderD=findById(manualOrderD.getIAutoId());
		if(dbManualOrderD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(manualOrderD.getName(), manualOrderD.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=manualOrderD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(manualOrderD.getIAutoId(), JBoltUserKit.getUserId(), manualOrderD.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param manualOrderD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ManualOrderD manualOrderD, Kv kv) {
		//addDeleteSystemLog(manualOrderD.getIAutoId(), JBoltUserKit.getUserId(),manualOrderD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param manualOrderD model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(ManualOrderD manualOrderD, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(ManualOrderD manualOrderD, String column, Kv kv) {
		//addUpdateSystemLog(manualOrderD.getIAutoId(), JBoltUserKit.getUserId(), manualOrderD.getName(),"的字段["+column+"]值:"+manualOrderD.get(column));
		/**
		switch(column){
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}

	public List<Record> dataList(Kv kv) {
		if(kv.getLong("imanualordermid") == null)
			return null;
		return dbTemplate("manualorderm.manualorderds",kv).find();
	}

    public List<ManualOrderD> findByMid(ManualOrderM manualOrderM) {
        return find(selectSql().eq("iManualOrderMid", manualOrderM.getIAutoId()).eq("isDeleted", 0));
    }

	public List<ManualOrderD> findByMId(long iManualOrderMid) {
		return find(selectSql().eq("iManualOrderMid", iManualOrderMid).eq("isDeleted", 0));
	}
}