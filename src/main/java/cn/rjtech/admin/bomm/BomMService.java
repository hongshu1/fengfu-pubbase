package cn.rjtech.admin.bomm;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.BomSourceTypeEnum;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.BomD;
import cn.rjtech.model.momdata.BomM;
import cn.rjtech.model.momdata.BomMaster;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 物料建模-BOM主表
 * @ClassName: BomMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-08 17:03
 */
public class BomMService extends BaseService<BomM> {
	private final BomM dao=new BomM().dao();
	@Override
	protected BomM dao() {
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
     * @param iType 新增来源;1.导入新增 2.手工新增
     * @param isEffective 是否生效：0. 否 1. 是
     * @param isDeleted 删除状态;0. 未删除 1. 已删除
	 * @return
	 */
	public Page<BomM> getAdminDatas(int pageNumber, int pageSize, String keywords, Integer iType, Boolean isEffective, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("iType",iType);
        sql.eqBooleanToChar("isEffective",isEffective);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cInvName", "cDocName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param bomM
	 * @return
	 */
	public Ret save(BomM bomM, Long userId, String userName, Date now, int auditState) {
		if(bomM==null) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		bomM.setICreateBy(userId);
		bomM.setCCreateName(userName);
		bomM.setDCreateTime(now);
		bomM.setISource(SourceEnum.MES.getValue());
		bomM.setIUpdateBy(userId);
		bomM.setCUpdateName(userName);
		bomM.setDUpdateTime(now);
		bomM.setIsDeleted(false);
		bomM.setIOrgId(getOrgId());
		bomM.setCOrgCode(getOrgCode());
		bomM.setCOrgName(getOrgName());
		bomM.setIsEffective(false);
		bomM.setIsView(false);
		// 设置默认审批流
		bomM.setIAuditStatus(auditState);
		//if(existsName(bomMaster.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		
		boolean success=bomM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(bomM.getIAutoId(), JBoltUserKit.getUserId(), bomM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param bomM
	 * @return
	 */
	public Ret update(BomM bomM) {
		if(bomM==null || notOk(bomM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		BomM dbBomM=findById(bomM.getIAutoId());
		if(dbBomM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=bomM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(bomM.getIAutoId(), JBoltUserKit.getUserId(), bomM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param bomM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(BomM bomM, Kv kv) {
		//addDeleteSystemLog(bomM.getIAutoId(), JBoltUserKit.getUserId(),bomM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param bomM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(BomM bomM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(BomM bomM, String column, Kv kv) {
		//addUpdateSystemLog(bomM.getIAutoId(), JBoltUserKit.getUserId(), bomM.getName(),"的字段["+column+"]值:"+bomM.get(column));
		/**
		switch(column){
		    case "isEffective":
		        break;
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}
	
	public List<JsTreeBean> getTreeDatas(Kv kv) {
		kv.set("orgId", getOrgId());
		List<Record> recordList = dbTemplate("bomm.datas", kv).find();
		if (CollectionUtil.isEmpty(recordList)){
			return null;
		}
		return createJsTreeBean(kv.getStr(BomM.ENABLEICON), recordList);
	}
	
	public List<JsTreeBean> createJsTreeBean(String enableIconStr, List<Record> recordList){
		List<JsTreeBean> trees = new ArrayList<>();
		for (Record record : recordList){
			Long id = record.getLong(BomM.IAUTOID);
			Object pid = record.get(BomD.IBOMMID);
			StringBuilder text = new StringBuilder(record.getStr(BomM.CINVNAME));
			if (pid == null) {
				pid = "#";
				if (StrUtil.isNotBlank(enableIconStr)){
					String enableIcon = enableIconStr;
					enableIcon = enableIcon.replace("?", record.getStr(BomM.IAUTOID));
					text.append(enableIcon);
				}
			}
			JsTreeBean jsTreeBean = new JsTreeBean(id, pid, text.toString(), true);
			trees.add(jsTreeBean);
		}
		return trees;
	}
	
	public Page<Record> getVersionRecord(Integer pageNumber, Integer pageSize, Kv kv) {
		Page<Record> page = dbTemplate("bomm.getVersionRecord", kv.set("orgId", getOrgId())).paginate(pageNumber, pageSize);
		if (CollectionUtil.isEmpty(page.getList())){
			return page;
		}
		for (Record record : page.getList()){
			AuditStatusEnum auditStatusEnum = AuditStatusEnum.toEnum(record.getInt(BomM.IAUDITSTATUS));
			record.set(BomM.AUDITSTATUSSTR, auditStatusEnum.getText());
			
			BomSourceTypeEnum bomSourceTypeEnum = BomSourceTypeEnum.toEnum(record.getInt(BomM.ITYPE));
			record.set(BomM.TYPENAME, bomSourceTypeEnum.getText());
		}
		return page;
	}
	
	/**
	 * 更改是否显示字段
	 * @param id
	 * @return
	 */
	public Ret updateIsView(Long id){
		ValidationUtils.notNull(id, JBoltMsg.PARAM_ERROR);
		return toggleBoolean(id, BomM.ISVIEW);
	}
	
}
