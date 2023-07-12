package cn.rjtech.admin.foreigncurrency;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.exch.ExchService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.IsEnableEnum;
import cn.rjtech.enums.IsOkEnum;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.Exch;
import cn.rjtech.model.momdata.ForeignCurrency;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 币种档案 Service
 *
 * @ClassName: ForeignCurrencyService
 * @author: WYX
 * @date: 2023-03-20 21:09
 */
public class ForeignCurrencyService extends BaseService<ForeignCurrency> {

	@Inject
	private ExchService exchService; 
	
    private final ForeignCurrency dao = new ForeignCurrency().dao();

    @Override
    protected ForeignCurrency dao() {
        return dao;
    }

    /**
     * 后台管理分页查询
     */
    public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
        para.set("iorgid", getOrgId());
        return dbTemplate("foreigncurrency.paginateAdminDatas", para).paginate(pageNumber, pageSize);
    }

    /**
     * 保存
     */
    public Ret save(ForeignCurrency foreignCurrency) {
        if (foreignCurrency == null || isOk(foreignCurrency.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        Date now = new Date();
        Long userid = JBoltUserKit.getUserId();
        String loginUserName = JBoltUserKit.getUserName();
        foreignCurrency.setIOrgId(getOrgId());
        foreignCurrency.setCOrgName(getOrgName());
        foreignCurrency.setCOrgCode(getOrgCode());
        foreignCurrency.setICreateBy(userid);
        foreignCurrency.setDCreateTime(now);
        foreignCurrency.setCCreateName(loginUserName);
        foreignCurrency.setIUpdateBy(userid);
        foreignCurrency.setDUpdateTime(now);
        foreignCurrency.setCUpdateName(loginUserName);
        foreignCurrency.setIsDeleted(false);
        foreignCurrency.setISource(SourceEnum.MES.getValue());
        boolean success = foreignCurrency.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(foreignCurrency.getIautoid(), JBoltUserKit.getUserId(), foreignCurrency.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(ForeignCurrency foreignCurrency) {
        if (foreignCurrency == null || notOk(foreignCurrency.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        ForeignCurrency dbForeignCurrency = findById(foreignCurrency.getIAutoId());
        if (dbForeignCurrency == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        foreignCurrency.setIUpdateBy(JBoltUserKit.getUserId());
        foreignCurrency.setCUpdateName(JBoltUserKit.getUserName());
        foreignCurrency.setDUpdateTime(new Date());
        boolean success = foreignCurrency.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(foreignCurrency.getIautoid(), JBoltUserKit.getUserId(), foreignCurrency.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        return deleteByIds(ids, true);
    }

    /**
     * 逻辑删除
     */
    public Ret delete(Long id) {
        return updateColumn(id, "isdeleted", true);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param foreignCurrency 要删除的model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(ForeignCurrency foreignCurrency, Kv kv) {
        //addDeleteSystemLog(foreignCurrency.getIautoid(), JBoltUserKit.getUserId(),foreignCurrency.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param foreignCurrency 要删除的model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(ForeignCurrency foreignCurrency, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(foreignCurrency, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换bcal属性
     */
    public Ret toggleBcal(Long id) {
        return toggleBoolean(id, "bcal");
    }

    /**
     * 切换iotherused属性
     */
    public Ret toggleIotherused(Long id) {
        return toggleBoolean(id, "iotherused");
    }

    /**
     * 切换isdeleted属性
     */
    public Ret toggleIsDeleted(Long id) {
        return toggleBoolean(id, "isDeleted");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param foreignCurrency 要toggle的model
     * @param column          操作的哪一列
     * @param kv              携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(ForeignCurrency foreignCurrency, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(ForeignCurrency foreignCurrency, String column, Kv kv) {
        //addUpdateSystemLog(foreignCurrency.getIautoid(), JBoltUserKit.getUserId(), foreignCurrency.getName(),"的字段["+column+"]值:"+foreignCurrency.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param foreignCurrency model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    public String checkInUse(ForeignCurrency foreignCurrency, Kv kv) {
        //这里用来覆盖 检测ForeignCurrency是否被其它表引用
        return null;
    }

    public List<Record> findAll(Kv kv){
        return dbTemplate("foreigncurrency.paginateAdminDatas", kv.set("iorgid", getOrgId())).find();
    }
    
    public List<Record> getAllCexchName(){
        return dbTemplate("foreigncurrency.getAllCexchName",Kv.by("iorgid", getOrgId())).find();
    }

	public List<JsTreeBean> findForeigncurrencyRootTressList() {
		List<JsTreeBean> jsTreeBeanList = new ArrayList<JsTreeBean>();
		List<ForeignCurrency> foreignCurrencyList = find(selectSql().eq("iorgid", getOrgId()).eq("isdeleted", IsEnableEnum.NO.getValue()));
		//List<Record> foreignCurrencyList = dbTemplate("foreigncurrency.getAllCexchName",Kv.by("iorgid", getOrgId())).find();
		for (int i=0;i< foreignCurrencyList.size();i++) {
			ForeignCurrency foreignCurrency = foreignCurrencyList.get(i);
            // 当前节点
			boolean isselected=false;
			jsTreeBeanList.add(new JsTreeBean(foreignCurrency.getIAutoId(), "#", foreignCurrency.getCexchName(),false,isselected,"root_opened", true));
		}
		return jsTreeBeanList;
	}

	public List<Record> findFlatTableDatas(Kv para) {
		String itype = para.getStr("itype");
		String cexchnamekv = para.getStr("cexchnamekv");
		ValidationUtils.notBlank(itype, "汇率类型为空");
		ValidationUtils.notBlank(cexchnamekv, "币种名称为空");
		List<Record> list = new ArrayList<Record>();
		Date now = new Date();
		if(itype.equals("1")){
			StringBuilder sql = new StringBuilder();
			sql.append("select cdate_rs.cdate,exch_rs.nflat nflat1, null nflat2,1 nflattype"
					+ ",(select top 1 iautoid from bd_foreigncurrency where cexch_name = '"+cexchnamekv+"' and isdeleted = 0) foreigncurrencyid"
					+ " from (");
			Date nowMonthFirstDayDate = JBoltDateUtil.thisMonthFirstDay(now);
			Date nowMonthLastDayDate = JBoltDateUtil.thisMonthLastDay(now);
			int dayint = JBoltDateUtil.daysBetween(nowMonthFirstDayDate, nowMonthLastDayDate)+1;
			for (int i=0;i < dayint;i++) {
				String afterDate = JBoltDateUtil.format(JBoltDateUtil.getDaysAfter(nowMonthFirstDayDate, i), "yyyy.MM.dd") ;
				if(i == 0) sql.append("select '" + afterDate + "' cdate ");
				else sql.append("union all select '" + afterDate + "' cdate ");
			}
			sql.append(") cdate_rs");
			para.set("cdatesql",sql);
			list = dbTemplate("foreigncurrency.findRegularFlatDatas",para).find();
		}else{
			list = dbTemplate("foreigncurrency.findFloatFlatDatas",para).find();
		}
		return list;
	}

	public ForeignCurrency findModelByExchName(Kv para) {
		ValidationUtils.notBlank(para.getStr("cexch_name"), "币种名称为空");
		return findFirst(selectSql().eq("iorgid",getOrgId()).eq("cexch_name", para.getStr("cexch_name")).eq("isdeleted", IsEnableEnum.NO.getValue()));
	}

	public Ret saveOrUpdateForeignCurrency(Kv para) {
		Long iautoid = para.getLong("iautoid");
		String cexchcode = para.getStr("cexchcode");
		String cexchname = para.getStr("cexchname");
		String idecStr = para.getStr("idec");
		Short idec = Short.parseShort(idecStr);
		Float nerror = para.getFloat("nerror");
		Boolean bcal = para.getBoolean("bcal");
		ForeignCurrency foreignCurrency = new ForeignCurrency();
		tx(()->{
			User user = JBoltUserKit.getUser();
			Date now = new Date();
			if(iautoid == null){
				foreignCurrency
					.setCexchCode(cexchcode)
					.setCexchName(cexchname)
					.setIdec(idec)
					.setNerror(nerror)
					.setBcal(bcal)
					.setIOrgId(getOrgId())
					.setCOrgCode(getOrgCode())
					.setCOrgName(getOrgName())
					.setICreateBy(user.getId())
					.setCCreateName(user.getName())
					.setDCreateTime(now)
					.setIUpdateBy(user.getId())
					.setCUpdateName(user.getName())
					.setDUpdateTime(now)
					.setIsDeleted(IsOkEnum.NO.getText())
					.setISource(SourceEnum.MES.getValue());
				ValidationUtils.isTrue(foreignCurrency.save(), ErrorMsg.SAVE_FAILED);
			}else{
				foreignCurrency.setIAutoId(iautoid)
				.setCexchCode(cexchcode)
				.setCexchName(cexchname)
				.setIdec(idec)
				.setNerror(nerror)
				.setBcal(bcal)
				.setIUpdateBy(user.getId())
				.setCUpdateName(user.getName())
				.setDUpdateTime(now);
				ValidationUtils.isTrue(foreignCurrency.update(), ErrorMsg.UPDATE_FAILED);
			}
			return true;
		});
		return successWithData(foreignCurrency);
	}
	/**
	 * 外币设置汇率单元格提交修改：
	 * 	1.单元格提交参数为id和单元格column： 其中id由币种主键，日期，汇率类型(固定汇率：2 ，浮动汇率：1)组成，以"-"拼接
	 * 	2.固定汇率，记账汇率：itype = 1；固定汇率，调整汇率：itype = 2；浮动汇率，记账汇率：itype = 3；浮动汇率，调整汇率：页面中表格列不可编辑
	 * 	3.nflat1为记账汇率，nflat2为调整汇率
	 * 	4.根据币种名称，类型，日期，年份查询汇率是否存在，不存在则新增，存在：记账汇率或者调整汇率从已有值修改为空时删除汇率数据，否则修改
	 * */
	public Ret saveCellTable(Kv para) {
		String id = para.getStr("id");
		String[] idArry = id.split("-");
		ValidationUtils.isTrue(idArry.length == 3, "参数解析出错!");
		String foreignCurrencyId = idArry[0];
		String cDate = idArry[1];
		String nFlatType = idArry[2];
		tx(()->{
			String nflat = "";
			if(para.containsKey("nflat1")) nflat = para.getStr("nflat1");
			if(para.containsKey("nflat2")) nflat = para.getStr("nflat2");
			Date aDate = JBoltDateUtil.toDate(cDate, "yyyy.MM");
			Integer iperiod = aDate.getMonth()+1;
			Integer itype = nFlatType.equals("1") ? 1 : (para.containsKey("nflat1") ? 2 : 3);
			String cdateForSave = nFlatType.equals("1") ? cDate : iperiod.toString();
			Double nflatForSave = null;
			ForeignCurrency foreignCurrency = findById(foreignCurrencyId);
			try{
				if(JBoltStringUtil.isNotBlank(nflat)) nflatForSave = Double.parseDouble(nflat); 
			}catch(Exception e){
				ValidationUtils.isTrue(false, "汇率格式不正确，请检查");
			}
			Exch exch = new Exch();
			User user = JBoltUserKit.getUser();
			Date now = new Date();
			Exch exchIsExists = exchService.findUniqueExch(getOrgId(), foreignCurrency.getCexchName(), itype, cdateForSave, JBoltDateUtil.getYear());
			if(exchIsExists == null){
				exch.setIOrgId(getOrgId())
					.setCOrgCode(getOrgCode())
					.setCOrgName(getOrgName())
					.setCexchName(foreignCurrency.getCexchName())
					.setIperiod(iperiod)
					.setItype(itype)
					.setCdate(cdateForSave)
					.setNflat(nflatForSave)
					.setIYear(JBoltDateUtil.getYear())
					.setISource(SourceEnum.MES.getValue())
					.setICreateBy(user.getId())
					.setCCreateName(user.getName())
					.setDCreateTime(now)
					.setIUpdateBy(user.getId())
					.setCUpdateName(user.getName())
					.setDUpdateTime(now)
					.setIsDeleted(IsOkEnum.NO.getText());
					ValidationUtils.isTrue(exch.save(), ErrorMsg.SAVE_FAILED);
			}else if(nflatForSave == null){
				ValidationUtils.isTrue(exchIsExists.delete(), ErrorMsg.DELETE_FAILED);
			}else{
				exchIsExists.setNflat(nflatForSave)
				.setIUpdateBy(user.getId())
				.setCUpdateName(user.getName())
				.setDUpdateTime(now);
				ValidationUtils.isTrue(exchIsExists.update(), ErrorMsg.SAVE_FAILED);
			}
			
			return true;
		});
		return SUCCESS;
	}
	/**
	 * 删除币种和币种汇率
	 * */
	public Ret deleteForeignCurrencyAndExch(Kv para) {
		Long iautoid = para.getLong("iautoid");
		ValidationUtils.notNull(iautoid, "币种ID为空，请检查");
		tx(()->{
			ForeignCurrency foreignCurrency = findById(iautoid);
			Integer isource = foreignCurrency.getISource();
			ValidationUtils.isTrue(isource == SourceEnum.MES.getValue(), "来源U8的数据不能删除!");
			exchService.deleteByCexchName(foreignCurrency.getCexchName());
			ValidationUtils.isTrue(foreignCurrency.delete(), ErrorMsg.DELETE_FAILED);
			return true;
		});
		return SUCCESS;
	}
}
