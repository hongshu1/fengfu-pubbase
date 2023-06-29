package cn.rjtech.admin.spotcheckformm;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventoryspotcheckform.InventorySpotCheckFormService;
import cn.rjtech.admin.inventoryspotcheckformOperation.InventoryspotcheckformOperationService;
import cn.rjtech.admin.spotcheckformd.SpotCheckFormDService;
import cn.rjtech.admin.spotcheckformdline.SpotcheckformdLineService;
import cn.rjtech.model.momdata.*;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import com.jfinal.plugin.activerecord.Record;

import java.util.*;

/**
 * 始业点检表管理
 * @ClassName: SpotCheckFormMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-29 09:16
 */
public class SpotCheckFormMService extends BaseService<SpotCheckFormM> {
	private final SpotCheckFormM dao=new SpotCheckFormM().dao();
	@Override
	protected SpotCheckFormM dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }
	@Inject
	private InventorySpotCheckFormService inventorySpotCheckFormService;
	@Inject
	private InventoryService inventoryService;
	@Inject
	private InventoryspotcheckformOperationService inventoryspotcheckformOperationService;
	@Inject
	private SpotCheckFormDService spotCheckFormDService;
	@Inject
	private SpotcheckformdLineService spotcheckformdLineService;

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @param keywords   关键词
     * @param iType 类型;1.首末点检表 2.首中末点检表
     * @param IsDeleted 删除状态;0. 未删除 1. 已删除
	 * @return
	 */
	public Page<SpotCheckFormM> getAdminDatas(int pageNumber, int pageSize, String keywords, Integer iType, Boolean IsDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("iType",iType);
        sql.eqBooleanToChar("IsDeleted",IsDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cOperationName", "cPersonName", "cCreateName", "cUpdateName", "cAuditName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param spotCheckFormM
	 * @return
	 */
	public Ret save(SpotCheckFormM spotCheckFormM) {
		if(spotCheckFormM==null || isOk(spotCheckFormM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=spotCheckFormM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(spotCheckFormM.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param spotCheckFormM
	 * @return
	 */
	public Ret update(SpotCheckFormM spotCheckFormM) {
		if(spotCheckFormM==null || notOk(spotCheckFormM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SpotCheckFormM dbSpotCheckFormM=findById(spotCheckFormM.getIAutoId());
		if(dbSpotCheckFormM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=spotCheckFormM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(spotCheckFormM.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param spotCheckFormM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SpotCheckFormM spotCheckFormM, Kv kv) {
		//addDeleteSystemLog(spotCheckFormM.getIAutoId(), JBoltUserKit.getUserId(),spotCheckFormM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param spotCheckFormM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SpotCheckFormM spotCheckFormM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 *制造工单入口数据
	 */
	public Page<Record> getAdminDatas2(Integer pageNumber, Integer pageSize, Kv kv) {
		//获取制造工单的存货编码
		Inventory cinvcode = inventoryService.findFirst("SELECT * FROM Bd_Inventory WHERE  cInvCode=? and isDeleted=0", kv.getStr("cinvcode"));
		//获取对应点检建模数据
		List<InventorySpotCheckForm> list = inventorySpotCheckFormService.findByInventoryId(cinvcode.getIAutoId(), 1);
		Page<Record> page1 = null;
		for (InventorySpotCheckForm inventorySpotCheckForm : list) {
			Page<Record> page = inventorySpotCheckFormService.pageList(kv.set("iautoid", inventorySpotCheckForm.getIAutoId()).set("page",pageNumber).set("pageSize",pageSize));
			for (Record record : page.getList()) {
				record.set("croutingname",kv.getStr("croutingname"));
				record.set("cinvcode",kv.getStr("cinvcode"));
				record.set("cmodocno",kv.getStr("cmodocno"));
			}
			page1=page;
		}

		return page1;
	}

	/**
	 * 标题数据处理
	 */
	public List<Map<String, Object>> lineRoll(List<Record> formItemLists,String iprodformid) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		// 标题选择值
		if (ObjUtil.isNotNull(formItemLists)){

			for (int i=0; i<formItemLists.size(); i++){

				Record item = formItemLists.get(i);
				String qcItemId = item.getStr("iqcitemid");

				boolean flag = false;
				List<Map<String, Object>> itemParamList = new ArrayList<>();
				for (Record object :formItemLists){
					if (qcItemId.equals(object.getStr("iqcitemid"))){
						flag = true;
						itemParamList.add(object.getColumns());
					}
				}
				if (flag){
					item.put("compares", itemParamList);
				}
				mapList.add(item.getColumns());
			}
		}

		if (CollectionUtil.isNotEmpty(mapList)){

			Collections.sort(mapList, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					Integer map1 = Integer.valueOf(o1.get("iseq").toString());
					Integer map2 = Integer.valueOf(o2.get("iseq").toString());
					return map1.compareTo(map2);
				}
			});
			return mapList;
		}
		return null;
	}

	public List<Map<String, Object>> lineRoll2(List<Record> byIdGetDetail) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		String iseq = "";
		String id = "";
		if (ObjUtil.isNotNull(byIdGetDetail)){

			for (int i=0; i<byIdGetDetail.size(); i++){

				Record item = byIdGetDetail.get(i);
				String qcItemId = item.getStr("iseq");
				if (!qcItemId.equals(iseq)) {
					iseq=qcItemId;
					boolean flag = false;
					List<Map<String, Object>> itemParamList = new ArrayList<>();
					for (Record object :byIdGetDetail){
						if (!object.getStr("iAutoId").equals(id)) {
							id=object.getStr("iAutoId");
							SpotCheckFormD checkFormD = spotCheckFormDService.findFirst("select * from PL_SpotCheckFormD where iSpotCheckFormMid=?", object.getLong("iAutoId"));
							if (ObjUtil.isNotNull(checkFormD)) {
								List<SpotcheckformdLine> list = spotcheckformdLineService.findBySpotCheckFormDId(checkFormD.getIAutoId());
								object.set("cValue",list.get(0).getCValue());
								object.set("iStdVal",checkFormD.getIStdVal());
								object.set("iMaxVal",checkFormD.getIMaxVal());
								object.set("iMinVal",checkFormD.getIMinVal());
							}
						}
						if (qcItemId.equals(object.getStr("iseq"))){
							flag = true;
							itemParamList.add(object.getColumns());
						}
					}
					if (flag){
						item.put("compares", itemParamList);
					}
					mapList.add(item.getColumns());
				}

			}
		}
		if (CollectionUtil.isNotEmpty(mapList)){

			Collections.sort(mapList, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					Integer map1 = Integer.valueOf(o1.get("proditemiseq").toString());
					Integer map2 = Integer.valueOf(o2.get("proditemiseq").toString());
					return map1.compareTo(map2);
				}
			});
			return mapList;
		}
		return null;
	}
}