package cn.rjtech.admin.codingrulem;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.admin.codingruled.CodingRuleDService;
import cn.rjtech.admin.form.FormService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.Form;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.CodingRuleM;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 系统配置-编码规则
 * @ClassName: CodingRuleMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-04 14:00
 */
public class CodingRuleMService extends BaseService<CodingRuleM> {
	private final CodingRuleM dao=new CodingRuleM().dao();

	@Inject
	private FormService formService;
    @Inject
    private CodingRuleDService codingRuleDService;

	@Override
	protected CodingRuleM dao() {
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
     * @param cFormTypeSn 业务对象类型：1. 单据类型 2. 条码号
     * @param iCodingType 编码方式: 1. 自动生成编码，允许手工修改 2. 完全手工编码
     * @param IsDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, String keywords, String cFormTypeSn, Integer iCodingType, Boolean IsDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("cFormTypeSn",cFormTypeSn);
        sql.eq("iCodingType",iCodingType);
        sql.eqBooleanToChar("IsDeleted",IsDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
		Page<Record> paginates = paginateRecord(sql);
		List<Record> list = paginates.getList();
		list.forEach(row -> {
			row.set("cformname",formService.getNameByFormId(row.getStr("iformid")));
		});
		return paginates;
	}

	/**
	 * 保存
	 * @param codingRuleM
	 * @return
	 */
	public Ret save(CodingRuleM codingRuleM,String iFormId) {
		if(codingRuleM==null || isOk(codingRuleM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		ValidationUtils.notBlank(iFormId, JBoltMsg.PARAM_ERROR);
		Form form = formService.findById(iFormId);
		ValidationUtils.notNull(form, "表单参数不合法，请确认选项是否正确");

		String userName = JBoltUserKit.getUserName();
		Long userId = JBoltUserKit.getUserId();
		Date now = new Date();

		codingRuleM.setICreateBy(userId);
		codingRuleM.setDCreateTime(now);
		codingRuleM.setCCreateName(userName);
		codingRuleM.setIUpdateBy(userId);
		codingRuleM.setDUpdateTime(now);
		codingRuleM.setCUpdateName(userName);
		codingRuleM.setIsDeleted(false);
		codingRuleM.setIFormId(form.getIAutoId());

		// 组织信息
		codingRuleM.setCOrgCode(getOrgCode());
		codingRuleM.setCOrgName(getOrgName());
		codingRuleM.setIOrgId(getOrgId());

		boolean success = codingRuleM.save();
		return ret(success);
	}

	/**
	 * 更新
	 * @param codingRuleM
	 * @return
	 */
	public Ret update(CodingRuleM codingRuleM) {
		if(codingRuleM==null || notOk(codingRuleM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		CodingRuleM dbCodingRuleM=findById(codingRuleM.getIAutoId());
		if(dbCodingRuleM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(codingRuleM.getName(), codingRuleM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=codingRuleM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(codingRuleM.getIAutoId(), JBoltUserKit.getUserId(), codingRuleM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param codingRuleM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(CodingRuleM codingRuleM, Kv kv) {
		//addDeleteSystemLog(codingRuleM.getIAutoId(), JBoltUserKit.getUserId(),codingRuleM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param codingRuleM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(CodingRuleM codingRuleM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

    public Ret saveTableSubmit(JBoltTable jBoltTable) {
        CodingRuleM m = jBoltTable.getFormModel(CodingRuleM.class,"codingRuleM");
        ValidationUtils.notNull(m,JBoltMsg.PARAM_ERROR);


        tx(() -> {

            // 新增
            if (ObjUtil.isNull(m.getIAutoId())) {
                doSaveTable(m, jBoltTable);
            } 
            // 修改
            else {
                doUpdateTable(m, jBoltTable);
            }

            return true;
        });

        return successWithData(m.keep("iautoid"));
    }

    /**
     * 表格新增
     */
    private void doSaveTable(CodingRuleM m, JBoltTable jBoltTable) {
        List<Record> save = jBoltTable.getSaveRecordList();
        ValidationUtils.notEmpty(save, JBoltMsg.PARAM_ERROR);

		ValidationUtils.notNull(m.getIFormId(), JBoltMsg.PARAM_ERROR);
		Form form = formService.findById(m.getIFormId());
		ValidationUtils.notNull(form, "表单参数不合法，请确认选项是否正确");

		String userName = JBoltUserKit.getUserName();
		Long userId = JBoltUserKit.getUserId();
		Date now = new Date();

		m.setDCreateTime(now);
		m.setICreateBy(userId);
		m.setCCreateName(userName);
		m.setDUpdateTime(now);
		m.setIUpdateBy(userId);
		m.setCUpdateName(userName);
		m.setIsDeleted(false);
		m.setIFormId(form.getIAutoId());

		// 组织信息
		m.setCOrgCode(getOrgCode());
		m.setCOrgName(getOrgName());
		m.setIOrgId(getOrgId());
        
        ValidationUtils.isTrue(m.save(), ErrorMsg.SAVE_FAILED);

        for (Record d : save) {
            d.set("iautoid", JBoltSnowflakeKit.me.nextId())
                    .set("icodingrulemid", m.getIAutoId());
        }
        codingRuleDService.batchSaveRecords(save);
    }

    /**
     * 修改表格
     */
    private void doUpdateTable(CodingRuleM m, JBoltTable jBoltTable) {
        ValidationUtils.validateId(m.getIAutoId(), JBoltMsg.PARAM_ERROR);
        
        CodingRuleM dbCodingRuleM = findById(m.getIAutoId());
        ValidationUtils.notNull(dbCodingRuleM, JBoltMsg.DATA_NOT_EXIST);
        ValidationUtils.isTrue(!dbCodingRuleM.getIsDeleted(), "编码规则已被删除");
        
        // 新增
        List<Record> save = jBoltTable.getSaveRecordList();
        if (CollUtil.isNotEmpty(save)) {
            
        }
        
        // 修改
        List<Record> update = jBoltTable.getUpdateRecordList();
        if (CollUtil.isNotEmpty(update)) {

            codingRuleDService.batchUpdateRecords(update);
        }
        
        // 删除
        if (ArrayUtil.isNotEmpty(jBoltTable.getDelete())) {
            codingRuleDService.deleteByMultiIds(ArrayUtil.join(jBoltTable.getDelete(), COMMA));
        }
    }

}