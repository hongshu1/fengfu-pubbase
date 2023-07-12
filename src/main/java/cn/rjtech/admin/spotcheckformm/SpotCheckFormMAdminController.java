package cn.rjtech.admin.spotcheckformm;

import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.rjtech.admin.inventoryspotcheckform.InventorySpotCheckFormService;
import cn.rjtech.admin.spotcheckform.SpotCheckFormService;
import cn.rjtech.admin.spotcheckformitem.SpotCheckFormItemService;
import cn.rjtech.admin.spotcheckformtableparam.SpotCheckFormTableParamService;
import cn.rjtech.base.controller.BaseAdminController;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.model.momdata.SpotCheckForm;
import cn.rjtech.model.momdata.SpotCheckFormM;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;

import java.util.List;
import java.util.Map;

/**
 * 始业点检表管理
 * @ClassName: SpotCheckFormMAdminController
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-29 09:16
 */
@CheckPermission(PermissionKey.SPOTCHECKFORMM)
@Before(JBoltAdminAuthInterceptor.class)
@Path(value = "/admin/spotCheckFormM", viewPath = "/_view/admin/spotcheckformm")
public class SpotCheckFormMAdminController extends BaseAdminController {

	@Inject
	private SpotCheckFormMService service;
	@Inject
	private SpotCheckFormService spotCheckFormService;
	@Inject
	private InventorySpotCheckFormService inventorySpotCheckFormService;
	@Inject
	private SpotCheckFormItemService spotCheckFormItemService;
	@Inject
	private SpotCheckFormTableParamService SpotCheckFormTableParamService;


   /**
	* 始业点检-制造工单入口
	*/
	public void index2(@Para(value = "modocid") String modocid,@Para(value = "itype") String itype) {
		set("modocid",modocid);
		set("itype",itype);
		render("spotcheckformm.html");
	}
	/**
	 * 首页
	 */
	public void index() {
		render("index.html");
	}

	public void index3() {
		render("index2.html");
	}
	/**
	 * 数据源
	 */
	public void datas2(@Para(value = "itype") String itype) {
		renderJsonData(service.getAdminDatas2(getKv().set("itype",itype)));
	}
   /**
	* 数据源
	*/
	public void datas() {
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKv()));
	}

   /**
	* 新增
	*/
	public void add() {
		render("add.html");
	}

   /**
	* 保存
	*/
	@Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
	public void save(@Para("spotCheckFormM")SpotCheckFormM spotCheckFormM) {
		renderJson(service.save(spotCheckFormM));
	}

   /**
	* 编辑
	*/
   @CheckPermission(PermissionKey.SPOTCHECKFORMM_EDIT)
	public void edit(@Para(value = "coperationname") String coperationname,
					 @Para(value = "iinventoryid") String iinventoryid,
					 @Para(value = "modocid") String modocid,
					 @Para(value = "routingconfigid") String routingconfigid,
					 @Para(value = "cequipmentnames") String cequipmentnames,
					 @Para(value = "spotcheckformmid") Long spotcheckformmid,
					 @Para(value = "ispotcheckformid") Long ispotcheckformid,
					 @Para(value = "cspotcheckformname") String cspotcheckformname,
					 @Para(value = "controls") int controls,
					 @Para(value = "itype") int itype) {
		if(routingconfigid == null){
			renderFail("请在【点检建模】配置点检表后，再完成点检操作！");
			return;
		}
		List<Record> list = inventorySpotCheckFormService.pageList(Kv.create().set("iinventoryid",iinventoryid).set("ispotcheckformid",ispotcheckformid)
						.set("cspotcheckformname",cspotcheckformname)
				.set("page", 1).set("pageSize", 5)).getList();
		if (list.size()>0) {
			Record record = list.get(0);
			SpotCheckForm spotCheckForm = spotCheckFormService.findById(record.getStr("ispotcheckformid"));
			record.set("iautoid",null);
			if (StrUtil.isBlank(cequipmentnames)){
				Record equipment = service.getEquipment(Long.valueOf(routingconfigid));
				record.set("cequipmentnames",equipment.getStr("cequipmentnames"));
			}else {
				record.set("cequipmentnames",cequipmentnames);
			}
			if (spotcheckformmid!=null){
				record.set("iautoid",spotcheckformmid);
				SpotCheckFormM checkFormM = service.findById(spotcheckformmid);
				record.set("iauditstatus",checkFormM.getIAuditStatus());
				record.set("iauditway",checkFormM.getIAuditWay());
			}
			set("spotCheckFormM",record);
			set("coperationname",coperationname);
			set("modocid",modocid);
			set("iinventoryid",iinventoryid);
			set("routingconfigid",routingconfigid);
			set("spotcheckformid",spotCheckForm.getIAutoId());
			set("controls",controls);
			set("itype",itype);
		}
		keepPara();
		render("edit.html");
	}

	/**
	 * 编辑
	 */
	@CheckPermission(PermissionKey.SPOTCHECKFORMM_EDIT)
	public void edit2(@Para(value = "coperationname") String coperationname,
					 @Para(value = "iinventoryid") String iinventoryid,
					 @Para(value = "modocid") String modocid,
					 @Para(value = "routingconfigid") String routingconfigid,
					 @Para(value = "cequipmentnames") String cequipmentnames,
					 @Para(value = "spotcheckformmid") Long spotcheckformmid,
					 @Para(value = "controls") int controls) {
		Record data = service.getData(spotcheckformmid);
		List<Record> list = inventorySpotCheckFormService.pageList(Kv.create().set("iinventoryid",iinventoryid).set("itype",data.getStr("itype"))
				.set("page", 1).set("pageSize", 5)).getList();
		if (list.size()>0) {
			Record record = list.get(0);
			SpotCheckForm spotCheckForm = spotCheckFormService.findById(record.getStr("ispotcheckformid"));
			record.set("iautoid",null);
			if (StrUtil.isBlank(cequipmentnames)){
				Record equipment = service.getEquipment(Long.valueOf(routingconfigid));
				record.set("cequipmentnames",equipment.getStr("cequipmentnames"));
			}else {
				record.set("cequipmentnames",cequipmentnames);
			}
			if (spotcheckformmid!=null){
				record.set("iautoid",spotcheckformmid);
				SpotCheckFormM checkFormM = service.findById(spotcheckformmid);
				record.set("iauditstatus",checkFormM.getIAuditStatus());
				record.set("iauditway",checkFormM.getIAuditWay());
			}
			set("spotCheckFormM",record);
			set("coperationname",coperationname);
			set("modocid",modocid);
			set("iinventoryid",iinventoryid);
			set("routingconfigid",routingconfigid);
			set("spotcheckformid",spotCheckForm.getIAutoId());
			set("controls",controls);
			set("data",data);
		}
		keepPara();
		render("edit2.html");
	}



	/**
	 * 获取表格中的数据
	 */
	public void table3(@Para( value = "spotcheckformid") String spotcheckformid,
			@Para( value = "spotcheckformmid") String spotcheckformmid){
		//根据生产表格id获取项目名称
		if (StrUtil.isNotBlank(spotcheckformid)) {
			if (StrUtil.isNotBlank(spotcheckformmid)) {
				SpotCheckFormM spotCheckFormM = service.findById(spotcheckformmid);
				spotcheckformid=String.valueOf(spotCheckFormM.getISpotCheckFormId());
			}
			//生产表单项目标题
			//行转列
			List tableHeadData = service.getTableHeadData(Long.valueOf(spotcheckformid));

			set("columns",tableHeadData);
			List<Map<String, Object>> recordList = service.findByFormId(Long.valueOf(spotcheckformid),spotcheckformmid);
			// 查询表头数据及参数数据
			set("dataList", recordList);
		}
		render("_table3.html");
	}

   /**
	* 更新
	*/
	@Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
	public void update(@Para("spotCheckFormM")SpotCheckFormM spotCheckFormM) {
		renderJson(service.update(spotCheckFormM));
	}

   /**
	* 批量删除
	*/
    @Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
	@CheckPermission(PermissionKey.SPOTCHECKFORMM_DELETE)
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
	@CheckPermission(PermissionKey.SPOTCHECKFORMM_DELETE)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}

	@CheckPermission(PermissionKey.SPOTCHECKFORMM_ADD_SUBMIT)
	public void submitForm(@Para(value = "formJsonData") String formJsonDataStr,
						   @Para(value = "tableJsonData") String tableJsonDataStr){
		renderJsonData(service.submitForm(formJsonDataStr, tableJsonDataStr));
	}

}
