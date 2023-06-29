package cn.rjtech.admin.spotcheckformm;

import cn.hutool.core.util.StrUtil;
import cn.jbolt._admin.permission.PermissionKey;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.CheckPermission;
import cn.jbolt.core.permission.JBoltAdminAuthInterceptor;
import cn.jbolt.core.permission.UnCheckIfSystemAdmin;
import cn.rjtech.admin.inventoryspotcheckform.InventorySpotCheckFormService;
import cn.rjtech.admin.spotcheckform.SpotCheckFormService;
import cn.rjtech.admin.spotcheckformitem.SpotCheckFormItemService;
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
@UnCheckIfSystemAdmin
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

   /**
	* 始业点检-制造工单入口
	*/
	public void index2(@Para(value = "cinvcode") String cinvcode,
					   @Para(value = "cmodocno") String cmodocno,
					   @Para(value = "croutingname") String croutingname) {
		set("cinvcode",cinvcode);
		set("cmodocno",cmodocno);
		set("croutingname",croutingname);
		render("spotcheckformm.html");
	}
	/**
	 * 首页
	 */
	public void index() {
		render("index.html");
	}
	/**
	 * 数据源
	 */
	public void datas2() {
		renderJsonData(service.getAdminDatas2(getPageNumber(), getPageSize(),getKv()));
	}
   /**
	* 数据源
	*/
	public void datas() {
		renderJsonData(service.getAdminDatas(getPageNumber(), getPageSize(), getKeywords(), getInt("iType"), getBoolean("IsDeleted")));
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
	public void edit(@Para(value = "inventoryspotcheckformid") Long inventoryspotcheckformid,
					 @Para(value = "cinvcode") String cinvcode,
					 @Para(value = "cmodocno") String cmodocno,
					 @Para(value = "croutingname") String croutingname) {
		Record record1 = inventorySpotCheckFormService.pageList(Kv.create().set("iautoid", inventoryspotcheckformid)
				.set("page",1).set("pageSize",5)).getList().get(0);
		SpotCheckForm spotCheckForm = spotCheckFormService.findById(record1.getStr("ispotcheckformid"));

		if(record1 == null){
			renderFail(JBoltMsg.DATA_NOT_EXIST);
			return;
		}
		record1.set("coperationname",croutingname);
		record1.set("crcvdocno",cmodocno);
		set("spotCheckFormM",record1);
		set("spotcheckformid",spotCheckForm.getIAutoId());
		render("edit.html");
	}
	/**
	 * 获取表格中的数据
	 */
	public void table3(@Para( value = "spotcheckformid") String spotcheckformid){
		//根据生产表格id获取项目名称
		if (StrUtil.isNotBlank(spotcheckformid)) {
			//生产表单项目标题
			List<Record> formItemLists = spotCheckFormItemService.formItemLists(Kv.by("iqcformid", spotcheckformid));
			//行转列
			List<Map<String, Object>> columns = service.lineRoll(formItemLists,spotcheckformid);
			set("columns",columns);
			List<Record> byIdGetDetail = spotCheckFormService.findByIdGetDetail(spotcheckformid);
			List<Map<String, Object>> maps = service.lineRoll2(byIdGetDetail);
			// 查询表头数据及参数数据
			set("dataList", maps);
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
	public void deleteByIds() {
		renderJson(service.deleteByIds(get("ids")));
	}

   /**
	* 删除
	*/
	@Before(Tx.class)
    @TxConfig(DataSourceConstants.MOMDATA)
	public void delete() {
		renderJson(service.deleteById(getLong(0)));
	}


}
