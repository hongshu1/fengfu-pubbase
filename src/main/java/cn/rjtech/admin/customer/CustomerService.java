package cn.rjtech.admin.customer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dept;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.admin.customeraddr.CustomerAddrService;
import cn.rjtech.admin.customerclass.CustomerClassService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.model.momdata.*;
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
import com.jfinal.plugin.activerecord.Record;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 往来单位-客户档案
 * @ClassName: CustomerService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-21 15:10
 */
public class CustomerService extends BaseService<Customer> {
	private final Customer dao=new Customer().dao();
	@Override
	protected Customer dao() {
		return dao;
	}

	@Inject
	private CustomerClassService customerclassService;
	@Inject
    private CustomerAddrService addrService;
	@Inject
	private VendorService vendorService;

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理分页查询
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("customer.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
		//return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 */
	public Ret save(Customer customerm) {
		if(customerm==null || isOk(customerm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(customerm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=customerm.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(customerm.getIautoid(), JBoltUserKit.getUserId(), customerm.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 */
	public Ret update(Customer customerm) {
		if(customerm==null || notOk(customerm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Customer dbCustomerm=findById(customerm.getIAutoId());
		if(dbCustomerm==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(customerm.getName(), customerm.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=customerm.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(customerm.getIautoid(), JBoltUserKit.getUserId(), customerm.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 */
	public Ret deleteByBatchIds(String ids) {
		return deleteByIds(ids,true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param customerm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(Customer customerm, Kv kv) {
		//addDeleteSystemLog(customerm.getIautoid(), JBoltUserKit.getUserId(),customerm.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param customerm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanDelete(Customer customerm, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(customerm, kv);
	}

	/**
	 * 切换isdeleted属性
	 */
	public Ret toggleIsdeleted(Long id) {
		return toggleBoolean(id, "isDeleted");
	}

	/**
	 * 切换isenabled属性
	 */
	public Ret toggleIsenabled(Long id) {
		return toggleBoolean(id, "isEnabled");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param customerm 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanToggle(Customer customerm,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Customer customerm, String column, Kv kv) {
		//addUpdateSystemLog(customerm.getIautoid(), JBoltUserKit.getUserId(), customerm.getName(),"的字段["+column+"]值:"+customerm.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param customerm model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(Customer customerm, Kv kv) {
		//这里用来覆盖 检测Customerm是否被其它表引用
		return null;
	}

	public Customer findByCustomermCode(String customercode){
		return findFirst(Okv.by("cCusCode", customercode).set("isDeleted", false), "iautoid", "asc");
	}

	public Ret updateEditTable(JBoltTable jBoltTable, Long userId, Date now) {
		Customer customerm = jBoltTable.getFormModel(Customer.class, "customerm");
		ValidationUtils.notNull(customerm, JBoltMsg.PARAM_ERROR);

		tx(() -> {
			//修改
			if(isOk(customerm.getIAutoId())){
				customerm.setIUpdateBy(JBoltUserKit.getUserId());
				customerm.setDUpdateTime(now);
				customerm.setCUpdateName(JBoltUserKit.getUserName());
				ValidationUtils.isTrue(customerm.update(), JBoltMsg.FAIL);
			}else{
				//新增
				//编码是否存在
				ValidationUtils.isTrue(findByCustomermCode(customerm.getCCCCode())==null, "编码重复");
				customerm.setIAutoId(JBoltSnowflakeKit.me.nextId());
				customerm.setICreateBy(JBoltUserKit.getUserId());
				customerm.setCCreateName(JBoltUserKit.getUserName());
				customerm.setDCreateTime(now);
				customerm.setIUpdateBy(JBoltUserKit.getUserId());
				customerm.setCUpdateName(JBoltUserKit.getUserName());
				customerm.setDUpdateTime(now);
				customerm.setCOrgCode(getOrgCode());
				customerm.setCOrgName(getOrgName());
				customerm.setIOrgId(getOrgId());

				ValidationUtils.isTrue(customerm.save(), JBoltMsg.FAIL);
			}


			//新增
			List<CustomerAddr> saveRecords = jBoltTable.getSaveModelList(CustomerAddr.class);
			if (CollUtil.isNotEmpty(saveRecords)) {
				for (CustomerAddr row : saveRecords) {
					row.setICustomerId(customerm.getIAutoId());

				}
				addrService.batchSave(saveRecords, 500);
			}

			//修改
			List<CustomerAddr> updateRecords = jBoltTable.getUpdateModelList(CustomerAddr.class);
			if (CollUtil.isNotEmpty(updateRecords)) {
				addrService.batchUpdate(updateRecords, 500);
			}

			// 删除
			Object[] deletes = jBoltTable.getDelete();
			if (ArrayUtil.isNotEmpty(deletes)) {
				addrService.deleteMultiByIds(deletes);
			}

			return true;
		});

		return SUCCESS;
	}

	public List<Record> getDataExport(Kv kv){
		return dbTemplate("customerm.paginateAdminDatas", kv).find();
	}

	public Ret importExcelData(File file) {
		List<Sheet> sheets = ExcelUtil.getReader(file).getSheets();
		StringBuilder errorMsg=new StringBuilder();
		if(sheets.size()>1){
			//第一个sheet为客户档案
			String sheetName1 = sheets.get(0).getSheetName();
			JBoltExcelSheet jBoltExcelSheet1 = JBoltExcelSheet.create(sheetName1);
			//设置sheet表格参数
			List<JBoltExcelHeader> headers = new ArrayList<>();
			headers.add(JBoltExcelHeader.create("ccustomerclasscode", "客户分类编码",50));
			headers.add(JBoltExcelHeader.create("ccustomercode", "客户编码",50));
			headers.add(JBoltExcelHeader.create("ccustomername", "客户名称",50));
			headers.add(JBoltExcelHeader.create("cshortname", "简称",50));
			headers.add(JBoltExcelHeader.create("cdeliveryadvance", "发货提前期",50));
			headers.add(JBoltExcelHeader.create("ccustomerlevelsn", "客户等级编码",50));
			headers.add(JBoltExcelHeader.create("cregion", "地区",50));
			headers.add(JBoltExcelHeader.create("idepid", "分管部门编码",50));
			headers.add(JBoltExcelHeader.create("ipersonid", "分管人员编码",50));

			jBoltExcelSheet1.setHeaders(2,headers);
			jBoltExcelSheet1.setDataStartRow(3);
			JBoltExcel jBoltExcel = JBoltExcel.from(file)
					.setSheets(jBoltExcelSheet1);
			List<Record> customerms = JBoltExcelUtil.readRecords(jBoltExcel,sheetName1, true, errorMsg);


			//第二个sheet为客户档案详情
			String sheetName2 = sheets.get(1).getSheetName();
			JBoltExcelSheet jBoltExcelSheet2 = JBoltExcelSheet.create(sheetName2);
			//设置sheet表格参数
			List<JBoltExcelHeader> headers2 = new ArrayList<>();
			headers2.add(JBoltExcelHeader.create("ccustomercode", "客户编码",50));
			headers2.add(JBoltExcelHeader.create("csitescode", "位置编码",50));
			headers2.add(JBoltExcelHeader.create("csitesname", "位置名称",50));
			headers2.add(JBoltExcelHeader.create("csearchcode", "搜索码",50));
			headers2.add(JBoltExcelHeader.create("ccontactcode", "联系人编码",50));
			headers2.add(JBoltExcelHeader.create("ccontactname", "联系人姓名",50));
			headers2.add(JBoltExcelHeader.create("ccountry", "国家/地区",50));
			headers2.add(JBoltExcelHeader.create("cprovince", "省／自治区",50));
			headers2.add(JBoltExcelHeader.create("ccity", "城市",50));
			headers2.add(JBoltExcelHeader.create("carea", "区县",50));
			headers2.add(JBoltExcelHeader.create("caddress", "地址",50));
			headers2.add(JBoltExcelHeader.create("cpostcode", "邮政编码",50));
			headers2.add(JBoltExcelHeader.create("cphone", "移动电话",50));
			headers2.add(JBoltExcelHeader.create("ctele", "固定电话",50));
			headers2.add(JBoltExcelHeader.create("cfax", "传真",50));
			headers2.add(JBoltExcelHeader.create("cemail", "电子邮件",50));


			jBoltExcelSheet2.setHeaders(2,headers2);
			jBoltExcelSheet2.setDataStartRow(3);
			JBoltExcel jBoltExcel2 = JBoltExcel.from(file)
					.setSheets(jBoltExcelSheet2);
			List<Record> customerds = JBoltExcelUtil.readRecords(jBoltExcel2, sheetName2, true, errorMsg);

			excelSave(customerms, customerds, errorMsg);
		}

		return SUCCESS;
	}

	/**
	 * 保存导入数据
	 */
	public void excelSave(List<Record> customerms, List<Record> customerds, StringBuilder errorMsg){
		int i=1;
		Kv customerKv = new Kv();
		Date now = new Date();
		//处理主表增加
		List<Customer> customermList = new ArrayList<>();
		for(Record recordm : customerms){
			ValidationUtils.notNull(recordm.getStr("ccustomerclasscode"), i+"分类编码为空！");
			ValidationUtils.notNull(recordm.getStr("ccustomercode"), i+"客户编码为空！");
			ValidationUtils.notNull(recordm.getStr("ccustomername"), i+"客户名称为空！");
			ValidationUtils.notNull(recordm.getStr("cdeliveryadvance"), i+"发货提前期为空！");
			ValidationUtils.notNull(recordm.getStr("ccustomerlevelsn"), i+"客户等级编码为空！");

			ValidationUtils.isTrue(findByCustomermCode(recordm.getStr("ccustomercode"))==null, recordm.getStr("ccustomercode")+"编码重复");
			CustomerClass customerclass = customerclassService.findFirst(Okv.by("cCode", recordm.getStr("ccustomerclasscode")),
					"iautoid", "desc");
			ValidationUtils.isTrue(customerclass!=null, recordm.getStr("ccustomerclasscode")+"客户编码不存在！");
			ValidationUtils.isTrue(JBoltDictionaryCache.me.getBySn("customer_level", recordm.getStr("ccustomerlevelsn"))!=null, recordm.getStr("ccustomerlevelsn")+"客户等级编码不存在！");

			Customer customerm = new Customer();
/*			customerm.setIAutoId(JBoltSnowflakeKit.me.nextId())
					.setcusc(customerclass.getIautoid())
					.setCcustomercode(recordm.getStr("ccustomercode"))
					.setCcustomername(recordm.getStr("ccustomername"))
					.setCshortname(recordm.getStr("cshortname"))
					.setCdeliveryadvance(recordm.getStr("cdeliveryadvance"))
					.setCcustomerlevelsn(recordm.getStr("ccustomerlevelsn"))
					.setCregion(recordm.getStr("cregion"))
					.setIcreateby(JBoltUserKit.getUserId())
					.setCcreatename(JBoltUserKit.getUserName())
					.setDcreatetime(now)
					.setCorgcode(getOrgCode())
					.setCorgname(getOrgName())
					.setIorgid(getOrgId());*/

			/*if(isOk(recordm.getStr("idepid"))){
				Dept dept = deptService.findFirst(Okv.by("sn", recordm.getStr("idepid")));
				if(dept!=null){
					customerm.setIdepid(dept.getId());
				}
			}
			if(isOk(recordm.getStr("ipersonid"))){
				Person person = personService.findFirst(Okv.by("cPersonCode", recordm.getStr("ipersonid")), "iautoid", "desc");
				if(person!=null){
					customerm.setIpersonid(person.getIautoid());
				}
			}*/

			customermList.add(customerm);
			customerKv.set(recordm.getStr("ccustomercode"), customerm.getIAutoId());
			i+=1;
		}

		//处理细表增加
		/*List<Customerd> customerdList = new ArrayList<>();
		for(Record recordd : customerds){
			ValidationUtils.notNull(recordd.getStr("ccustomercode"), "位置地址表格 客户编码为空！");
			ValidationUtils.notNull(recordd.getStr("csitescode"), "位置地址表格 位置编码为空！");

			//不是同表导入编码情况，找历史数据
			if(!isOk(customerKv.getStr(recordd.getStr("ccustomercode")))){
				Customerm customerm = findByCustomermCode(recordd.getStr("ccustomercode"));
				ValidationUtils.notNull(customerm, recordd.getStr("ccustomercode")+"位置地址表格 编码不存在！");
				customerKv.set(recordd.getStr("ccustomercode"), customerm.getIautoid());
			}

			Customerd customerd = new Customerd();
			customerd.setIcustomerid(Long.parseLong(customerKv.getStr(recordd.getStr("ccustomercode"))))
					.setCsitescode(recordd.getStr("csitescode"))
					.setCsitesname(recordd.getStr("csitesname"))
					.setCsearchcode(recordd.getStr("csearchcode"))
					.setCcontactcode(recordd.getStr("ccontactcode"))
					.setCcontactname(recordd.getStr("ccontactname"))
					.setCcountry(recordd.getStr("ccountry"))
					.setCprovince(recordd.getStr("cprovince"))
					.setCcity(recordd.getStr("ccity"))
					.setCarea(recordd.getStr("carea"))
					.setCaddress(recordd.getStr("caddress"))
					.setCpostcode(recordd.getStr("cpostcode"))
					.setCphone(recordd.getStr("cphone"))
					.setCtele(recordd.getStr("ctele"))
					.setCfax(recordd.getStr("cfax"))
					.setCemail(recordd.getStr("cemail"))
					.setIcreateby(JBoltUserKit.getUserId())
					.setCcreatename(JBoltUserKit.getUserName())
					.setDcreatetime(now);

			customerdList.add(customerd);
		}*/

		tx(() -> {
//			batchSave(customermList);
//			customerdService.batchSave(customerdList);
			return true;
		});
	}

	/**
	 * 供应商数据源
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> findVendorPage(int pageNumber, int pageSize, Kv kv){
		return vendorService.dbTemplate("customer.findVendor", kv).paginate(pageNumber, pageSize);
	}

	/**
	 *根据供应商编码查询供应商
	 * @return
	 */
	public Vendor findVendorByCode(String vendorCode){
		Kv kv = new Kv();
		kv.set("vendorCode",vendorCode);
		return vendorService.daoTemplate("customer.findVendorByCode",kv).findFirst();
	}
}
