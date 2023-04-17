package cn.rjtech.admin.bomcompare;

import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.BomCompare;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 物料建模-Bom清单
 * @ClassName: BomCompareService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-01 10:50
 */
public class BomCompareService extends BaseService<BomCompare> {
	private final BomCompare dao=new BomCompare().dao();
	@Override
	protected BomCompare dao() {
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
	 * @param keywords   关键词
     * @param iRawType 原材料类型：1. 卷料 2. 片料 3. 分条料
     * @param isOutSourced 是否外作：0. 否 1. 是
     * @param isDeleted 删除状态： 0. 未删除 1. 已删除
	 * @return
	 */
	public Page<BomCompare> getAdminDatas(int pageNumber, int pageSize, String keywords, Integer iRawType, Boolean isOutSourced, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("iRawType",iRawType);
        sql.eqBooleanToChar("isOutSourced",isOutSourced);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param bomCompare
	 * @return
	 */
	public Ret save(BomCompare bomCompare) {
		if(bomCompare==null || isOk(bomCompare.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(bomCompare.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=bomCompare.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(bomCompare.getIAutoId(), JBoltUserKit.getUserId(), bomCompare.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param bomCompare
	 * @return
	 */
	public Ret update(BomCompare bomCompare) {
		if(bomCompare==null || notOk(bomCompare.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		BomCompare dbBomCompare=findById(bomCompare.getIAutoId());
		if(dbBomCompare==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(bomCompare.getName(), bomCompare.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=bomCompare.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(bomCompare.getIAutoId(), JBoltUserKit.getUserId(), bomCompare.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param bomCompare 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(BomCompare bomCompare, Kv kv) {
		//addDeleteSystemLog(bomCompare.getIAutoId(), JBoltUserKit.getUserId(),bomCompare.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param bomCompare model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(BomCompare bomCompare, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(BomCompare bomCompare, String column, Kv kv) {
		//addUpdateSystemLog(bomCompare.getIAutoId(), JBoltUserKit.getUserId(), bomCompare.getName(),"的字段["+column+"]值:"+bomCompare.get(column));
		/**
		switch(column){
		    case "isOutSourced":
		        break;
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}
	
	public BomCompare createBomCompare(Long iautoId, Long userId, String userName, Date now, Long bOMMasterId, Long pid, Long itemId, int seq, int Level, String invLev,
									   int rawType, BigDecimal qty, BigDecimal weight, Long vendorId, String cMemo, boolean isOutSourced, boolean isDeleted){
		BomCompare bomCompare = new BomCompare();
		bomCompare.setIAutoId(iautoId);
		bomCompare.setICreateBy(userId);
		bomCompare.setCCreateName(userName);
		bomCompare.setDCreateTime(now);
		
		bomCompare.setIUpdateBy(userId);
		bomCompare.setCUpdateName(userName);
		bomCompare.setDUpdateTime(now);
		bomCompare.setIBOMMasterId(bOMMasterId);
		bomCompare.setIInventoryId(itemId);
		bomCompare.setISeq(seq);
		bomCompare.setILevel(Level);
		bomCompare.setCInvLev(invLev);
		bomCompare.setISource(1);
		
		bomCompare.setIPid(pid);
		
		bomCompare.setIRawType(rawType);
		bomCompare.setIQty(qty);
		bomCompare.setIWeight(weight);
		bomCompare.setIVendorId(vendorId);
		bomCompare.setCMemo(cMemo);
		bomCompare.setIsOutSourced(isOutSourced);
		bomCompare.setIsDeleted(isDeleted);
		bomCompare.setIOrgId(getOrgId());
		bomCompare.setCOrgCode(getOrgCode());
		bomCompare.setCOrgName(getOrgName());
		return bomCompare;
	}
}
