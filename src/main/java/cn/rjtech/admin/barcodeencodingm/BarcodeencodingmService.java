package cn.rjtech.admin.barcodeencodingm;

import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.jbolt.core.util.JBoltStringUtil;
import cn.rjtech.admin.barcodeencodingd.BarcodeencodingdService;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.expensebudget.ExpenseBudgetService;
import cn.rjtech.admin.investmentplan.InvestmentPlanService;
import cn.rjtech.admin.proposalm.ProposalmService;
import cn.rjtech.admin.purchasem.PurchasemService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.BarCodeEnum;
import cn.rjtech.enums.ItemEnum;
import cn.rjtech.model.momdata.Barcodeencodingd;
import cn.rjtech.model.momdata.Barcodeencodingm;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 销售报价单明细扩展自定义项管理 Service
 *
 * @ClassName: BarcodeencodingmService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-04-15 15:46
 */
public class BarcodeencodingmService extends BaseService<Barcodeencodingm> {

    private final Barcodeencodingm dao = new Barcodeencodingm().dao();
    @Inject
    private ExpenseBudgetService expenseBudgetService;
    @Inject
    private InvestmentPlanService investmentPlanService;
    @Inject
    private ProposalmService proposalmService;
    @Inject
    private PurchasemService purchasemService;
    @Inject
    private DepartmentService departmentService;
    @Override
    protected Barcodeencodingm dao() {
        return dao;
    }

    @Inject
    private BarcodeencodingdService barcodeencodingdService;


    /**
     * 后台管理分页查询true
     */
    public Page<Barcodeencodingm> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
        return paginateByKeywords("iautoid", "desc", pageNumber, pageSize, keywords, "ruleCode");
    }

    /**
     * 后台管理分页查询true
     */
    public Page<Record> paginateAdminList(int pageNumber, int pageSize, Kv para) {
        Page<Record> paginate = dbTemplate("barcodeencodingd.indexdata", para.set("corgcode", getOrgCode())).paginate(pageNumber, pageSize);
        List<Record> list = paginate.getList();
        for (Record record : list) {
            record.set("citemname", ItemEnum.toEnum(record.getStr("citem")).getText());
            record.set("createname", JBoltUserCache.me.getUserName(record.getLong("icreateby")));
            record.set("updatename", JBoltUserCache.me.getUserName(record.getLong("iupdateby")));
        }
        return paginate;
    }

    /**
     * 保存
     */
    public Ret save(Barcodeencodingm barcodeencodingm) {
        ValidationUtils.assertNull(barcodeencodingm.getIautoid(), JBoltMsg.PARAM_ERROR);

        tx(() -> {
            // ValidationUtils.isTrue(notExists(columnName, value), Msg.DATA_SAME_NAME_EXIST);
            // 判段部门是否非末级
            // String cdepcode = outsourcingsendapplym.getcDepCode();

            boolean save = barcodeencodingm.save();
            ValidationUtils.isTrue(save, ErrorMsg.SAVE_FAILED);

            // TODO 其他业务代码实现
            // 配置返回的属性

            return true;

        });

        // 添加日志
        // addSystemLog(outsourcingsendapplym.getIautoid(), JBoltUserKit.getUserId(), SystemLog.TYPE_SAVE, SystemLog.TARGETTYPE_xxx, outsourcingsendapplym.getName())
        return successWithData(Kv.by("barcodeencodingm", barcodeencodingm));
    }

    /**
     * 更新
     */
    public Ret update(Barcodeencodingm barcodeencodingm) {
        ValidationUtils.isTrue(isOk(barcodeencodingm.getIautoid()), JBoltMsg.PARAM_ERROR);

        tx(() -> {
            // 更新时需要判断数据存在
            Barcodeencodingm dbBarcodeencodingm = findById(barcodeencodingm.getIautoid());
            ValidationUtils.notNull(dbBarcodeencodingm, JBoltMsg.DATA_NOT_EXIST);

            // TODO 其他业务代码实现
            // ValidationUtils.isTrue(notExists(columnName, value), Msg.DATA_SAME_NAME_EXIST);

            ValidationUtils.isTrue(barcodeencodingm.update(), ErrorMsg.UPDATE_FAILED);

            return true;
        });

        // 添加日志
        // addSystemLog(barcodeencodingm.getIautoid(), JBoltUserKit.getUserId(), SystemLog.TYPE_UPDATE, SystemLog.TARGETTYPE_xxx, barcodeencodingm.getName())
        return SUCCESS;
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        tx(() -> {
            for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
                long iautoid = Long.parseLong(idStr);
                Barcodeencodingm dbBarcodeencodingm = findById(iautoid);
                ValidationUtils.notNull(dbBarcodeencodingm, JBoltMsg.DATA_NOT_EXIST);

                //处理细表数据
                List<Record> getlist = barcodeencodingdService.getList(iautoid);
                for (Record record : getlist) {
                    Long did = record.getLong("iautoid");
                    Barcodeencodingd barcodeencodingd = barcodeencodingdService.findById(did);
                    ValidationUtils.isTrue(barcodeencodingd.delete(), "删除细表数据失败!");
                }

                // TODO 可能需要补充校验组织账套权限
                // TODO 存在关联使用时，校验是否仍在使用

                ValidationUtils.isTrue(dbBarcodeencodingm.delete(), JBoltMsg.FAIL);
            }

            return true;
        });

        // 添加日志
        // Barcodeencodingm barcodeencodingm = ret.getAs("data");
        // addDeleteSystemLog(iautoid, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, barcodeencodingm.getName());
        return SUCCESS;
    }

    @Override
    protected int systemLogTargetType() {
        return 0;
    }

    public Ret saveTableSumit(JBoltTable jBoltTable) {
        tx(() -> {
            Barcodeencodingm barcodeencodingm = jBoltTable.getFormBean(Barcodeencodingm.class, "barcodeencodingm");

            barcodeencodingm.setDupdatetime(new Date());
            barcodeencodingm.setIupdateby(JBoltUserKit.getUserId());
            barcodeencodingm.setCorgcode(getOrgCode());
            barcodeencodingm.setIorgid(getOrgId());

            if (barcodeencodingm.getIautoid() == null) {
                //保存
                barcodeencodingm.setDcreatetime(new Date());
                barcodeencodingm.setIcreateby(JBoltUserKit.getUserId());
                ValidationUtils.isTrue(barcodeencodingm.save(), "编码规则保存失败!");
            } else {
                ValidationUtils.isTrue(barcodeencodingm.update(), "编码规则主表更新失败!");
            }

            Long mid = barcodeencodingm.getIautoid();

            // 细表的逻辑处理
            barcodeencodingdService.finishJBoltTable(jBoltTable, mid);

            return true;
        });

        return SUCCESS;
    }

    /**
     * 通过规则编码来查找
     */
    public Barcodeencodingm findByCrulecode(String crulecode) {
        return daoTemplate("barcodeencodingm.findByCrulecode", Kv.by("crulecode", crulecode).set("corgcode", getOrgCode())).findFirst();
    }
    /**
     * 获取应用模块的单据的内容：编码规则中可配置，英文名称，单据年份
     * */
    public String getItemOrderContent(String citem,String cprojectcode,Long iautoid){
    	String rsStr = "";
    	switch (ItemEnum.toEnum(citem)){
    		//费用预算
	        case EXPENSE_BUDGET:
	        	rsStr = expenseBudgetService.getItemOrderContent(iautoid,cprojectcode);
	            break;
	        //费用预算-追加项目
	        case EXPENSE_BUDGET_APPENDITEM:
	        	rsStr = expenseBudgetService.getItemOrderContent(iautoid,cprojectcode);
	            break;
    		//投资计划
	        case INVESTMENT_PLAN:
	        	rsStr = investmentPlanService.getItemOrderContent(iautoid,cprojectcode);
	            break;
	        //投资计划-追加项目
	        case INVESTMENT_PLAN_APPENDITEM:
	        	rsStr = investmentPlanService.getItemOrderContent(iautoid,cprojectcode);
	            break;	         
	        //禀议书
	        case PROPOSAL:
	        	rsStr = proposalmService.getItemOrderContent(iautoid,cprojectcode);
	            break;
	        //申购单
	        case PURCHASE:
	        	rsStr = purchasemService.getItemOrderContent(iautoid,cprojectcode);
	            break;
	        //项目档案
	        case PROJECT:
	        	rsStr = proposalmService.getItemOrderContent(iautoid,cprojectcode);
	            break;		            
	        default:
                break;
    	}
    	return rsStr;
    }
    /**
     * 获取成品条码
     * Kv中可设置的参数:  单据主键：iautoid
     * 					投资类型：iinvestmenttype
     * cdepcode  U8部门
     * type     投资类型
     *
     * @param citem 应用模块
     */
    public String genCode(Kv para, String citem) {
        Barcodeencodingm byCintem = findByCitem(citem);
        List<Record> list = barcodeencodingdService.getList(byCintem.getIautoid());
        StringBuilder code = new StringBuilder();
        for (Record record : list) {
        	String csuffix = record.getStr("csuffix");
            switch (BarCodeEnum.toEnum(record.getStr("cprojectcode"))) {
                case FIXED:
                    // 固定值
                    code.append(record.getStr("cprojectvalue"));
                    if(JBoltStringUtil.isNotBlank(csuffix)) code.append(csuffix);
                    break;
                case DEPT:
                    // U8部门英文名称
                	String cdepnameen = getItemOrderContent(citem,record.getStr("cprojectcode"),para.getLong("iautoid"));
                    ValidationUtils.notBlank(cdepnameen, ""+BarCodeEnum.DEPT.getText()+"为空");
                    code.append(cdepnameen);
                    if(JBoltStringUtil.isNotBlank(csuffix)) code.append(csuffix);
                    break;        
                case TYPE:
                    // 投资类型
                	if(citem.equals(ItemEnum.INVESTMENT_PLAN.getValue()) || citem.equals(ItemEnum.INVESTMENT_PLAN_APPENDITEM.getValue())){
                		String iinvestmenttype = para.getStr("iinvestmenttype");
                		ValidationUtils.notNull(iinvestmenttype, "投资类型为空");
                        code.append(iinvestmenttype);
                	}else{
                		ValidationUtils.error( "投资类型编码项目只能用于投资计划No.生成，其它应用模块不需要配置!");
                	}
                	if(JBoltStringUtil.isNotBlank(csuffix)) code.append(csuffix);
                    break;
                case ORDERYEAR:
                	//单据年份
                	String iyear = getItemOrderContent(citem,record.getStr("cprojectcode"),para.getLong("iautoid"));
                    code.append(iyear);
                    if(JBoltStringUtil.isNotBlank(csuffix)) code.append(csuffix);
                    break;                    
                case SYSTEMDATE:
                	//系统日期
                	String cdateformat = record.getStr("cdateformat");
                	ValidationUtils.notBlank(cdateformat, BarCodeEnum.SYSTEMDATE.getText()+"格式为空");
                    code.append(JBoltDateUtil.format(new Date(), cdateformat));
                    if(JBoltStringUtil.isNotBlank(csuffix)) code.append(csuffix);
                    break;
                default:
                    break;
            }

        }

        // 最后处理流水号
        for (Record record : list) {
            String cprojectcode = record.getStr("cprojectcode");
            if (BarCodeEnum.NUM.getValue().equals(cprojectcode)) {
            	Integer ibillnolen = record.getInt("ibillnolen");
                // 流水号
                int billNolength =  ibillnolen == null ? 4 : ibillnolen;
                code = new StringBuilder(BillNoUtils.genProposalSystemNo(getOrgId(), code.toString(), false, billNolength));
            }
        }
        String barcodeStr = code.toString().trim();
        ValidationUtils.notBlank(barcodeStr, "编码为空,保存失败!");
        return barcodeStr;
    }

    /**
     * 获取成品条码
     *
     * @param iautoid     	单据主键
     * @param type         投资类型
     * @param citem        应用模块
     */
    public String genCode(String cdepcode, String type, Integer billNolength, String str, String citem) {
        Kv para = Kv.by("cdepcode", cdepcode)
                .set("type", type)
                .set("billNolength", billNolength)
                .set("str", str);

        return genCode(para, citem);
    }
    
    /**
     * 通过应用模块查找有效的编码规则
     */
    public Barcodeencodingm findByCitem(String citem) {
        Kv kv = Kv.by("citem", citem)
                .set("corgcode", getOrgCode());

        Barcodeencodingm barcodeencodingm = daoTemplate("barcodeencodingm.findByCitem", kv).findFirst();
        ValidationUtils.notNull(barcodeencodingm, "没有定义该模块的编码规则!");
        return barcodeencodingm;
    }

}