package cn.rjtech.admin.sysassem;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONObject;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.Person;
import cn.rjtech.model.momdata.SysAssem;
import cn.rjtech.model.momdata.SysAssemdetail;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.HttpApiUtils;
import cn.smallbun.screw.core.util.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.json.JSONArray;

import java.math.BigDecimal;
import java.util.*;

/**
 * 组装拆卸及形态转换单
 * @ClassName: SysAssemService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 09:45
 */
public class SysAssemService extends BaseService<SysAssem> {
	private final SysAssem dao=new SysAssem().dao();
	@Override
	protected SysAssem dao() {
		return dao;
	}
	@Inject
	private PersonService personservice;

	@Inject
	private SysAssemdetailService sysassemdetailservice;
	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @param keywords   关键词
     * @param BillType 单据类型;采购PO  委外OM
     * @param state 状态 1已保存 2待审批 3已审批 4审批不通过
     * @param IsDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<SysAssem> getAdminDatas(int pageNumber, int pageSize, String keywords, String BillType, String state, Boolean IsDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("BillType",BillType);
        sql.eq("state",state);
        sql.eqBooleanToChar("IsDeleted",IsDeleted);
        //关键词模糊查询
        sql.like("deptName",keywords);
        //排序
        sql.desc("AutoID");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param sysAssem
	 * @return
	 */
	public Ret save(SysAssem sysAssem) {
		if(sysAssem==null || isOk(sysAssem.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(sysAssem.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysAssem.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(sysAssem.getAutoID(), JBoltUserKit.getUserId(), sysAssem.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param sysAssem
	 * @return
	 */
	public Ret update(SysAssem sysAssem) {
		if(sysAssem==null || notOk(sysAssem.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SysAssem dbSysAssem=findById(sysAssem.getAutoID());
		if(dbSysAssem==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(sysAssem.getName(), sysAssem.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysAssem.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(sysAssem.getAutoID(), JBoltUserKit.getUserId(), sysAssem.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param sysAssem 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SysAssem sysAssem, Kv kv) {
		//addDeleteSystemLog(sysAssem.getAutoID(), JBoltUserKit.getUserId(),sysAssem.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param sysAssem model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SysAssem sysAssem, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SysAssem sysAssem, String column, Kv kv) {
		//addUpdateSystemLog(sysAssem.getAutoID(), JBoltUserKit.getUserId(), sysAssem.getName(),"的字段["+column+"]值:"+sysAssem.get(column));
		/**
		switch(column){
		    case "IsDeleted":
		        break;
		}
		*/
		return null;
	}
	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
		Page<Record> paginate = dbTemplate("sysassem.recpor", kv).paginate(pageNumber, pageSize);
		return paginate;
	}
	/**
	 * 批量删除主从表
	 */
	public Ret deleteRmRdByIds(String ids) {
		tx(() -> {
			deleteByIds(ids);
			String[] split = ids.split(",");
			for(String s : split){
				delete("DELETE T_Sys_AssemDetail   where  MasID = ?",s);
			}
			return true;
		});
		return ret(true);
	}
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		tx(() -> {
			deleteById(id);
			delete("DELETE T_Sys_AssemDetail   where  MasID = ?",id);
			return true;
		});
		return ret(true);
	}

	/**
	 * 执行JBoltTable表格整体提交
	 *
	 * @param jBoltTable
	 * @return
	 */
	public Ret submitByJBoltTable(JBoltTable jBoltTable) {
		SysAssem sysotherin = jBoltTable.getFormModel(SysAssem.class,"sysAssem");
		//获取当前用户信息？
		User user = JBoltUserKit.getUser();
		Date now = new Date();
		tx(()->{
			//通过 id 判断是新增还是修改
			if(sysotherin.getAutoID() == null){
				sysotherin.setOrganizeCode(getOrgCode());
				sysotherin.setCreatePerson(user.getName());
				sysotherin.setCreateDate(now);
				sysotherin.setState("1");
				//主表新增
				ValidationUtils.isTrue(sysotherin.save(), ErrorMsg.SAVE_FAILED);
			}else{
				//主表修改
				ValidationUtils.isTrue(sysotherin.update(), ErrorMsg.UPDATE_FAILED);
			}
			//从表的操作
			// 获取保存数据（执行保存，通过 getSaveRecordList）
			saveTableSubmitDatas(jBoltTable,sysotherin);
			//获取修改数据（执行修改，通过 getUpdateRecordList）
			updateTableSubmitDatas(jBoltTable,sysotherin);
			//获取删除数据（执行删除，通过 getDelete）
			deleteTableSubmitDatas(jBoltTable);
			return true;
		});
		return SUCCESS;
	}

	//可编辑表格提交-新增数据
	private void saveTableSubmitDatas(JBoltTable jBoltTable,SysAssem sysotherin){
		List<Record> list = jBoltTable.getSaveRecordList();
		if(CollUtil.isEmpty(list)) return;
		User user = JBoltUserKit.getUser();
		Date now = new Date();
		ArrayList<SysAssemdetail> sysAssemdetails = new ArrayList<>();
		for (int i=0;i<list.size();i++) {
			Record row = list.get(i);
			SysAssemdetail sysAssemdetail = new SysAssemdetail();
			sysAssemdetail.setMasID(sysotherin.getAutoID());
			sysAssemdetail.setAutoID(JBoltSnowflakeKit.me.nextIdStr());
			sysAssemdetail.setCreateDate(now);
			sysAssemdetail.setModifyDate(now);
			sysAssemdetail.setCreatePerson(user.getName());
			sysAssemdetail.setModifyPerson(user.getName());
			sysAssemdetail.setBarcode(row.get("barcode"));
			sysAssemdetail.setSourceBillNoRow(row.get("sourcebillnorow"));
			sysAssemdetail.setQty(new BigDecimal(row.get("qty").toString()));
			sysAssemdetail.setSourceType(row.getStr("sourcebilltype"));
			sysAssemdetail.setSourceBillNo(row.getStr("sourcebillno"));
			sysAssemdetail.setSourceBillDid(row.getStr("sourcebilldid"));
			sysAssemdetail.setSourceBillID(row.getStr("sourcebilldid"));
			sysAssemdetail.setAssemType(row.getStr("assemtype"));
			sysAssemdetail.setWhCode(row.getStr("whcode"));
			sysAssemdetail.setPosCode(row.getStr("poscode"));
			sysAssemdetail.setCombinationNo(Integer.valueOf(row.getStr("combinationno")==null ? "0" : row.getStr("combinationno")));
			sysAssemdetail.setRowNo(Integer.valueOf(row.getStr("rowno")==null ? "0" : row.getStr("rowno")));
			sysAssemdetail.setTrackType(row.getStr("tracktype"));
			sysAssemdetail.setMemo(row.getStr("memo"));

			sysAssemdetails.add(sysAssemdetail);
		}
		sysassemdetailservice.batchSave(sysAssemdetails);
	}
	//可编辑表格提交-修改数据
	private void updateTableSubmitDatas(JBoltTable jBoltTable,SysAssem sysotherin){
		List<Record> list = jBoltTable.getUpdateRecordList();
		if(CollUtil.isEmpty(list)) return;
		User user = JBoltUserKit.getUser();
		Date now = new Date();
		ArrayList<SysAssemdetail> sysAssemdetails = new ArrayList<>();
		for(int i = 0;i < list.size(); i++){
			Record row = list.get(i);
			SysAssemdetail sysAssemdetail = new SysAssemdetail();
			sysAssemdetail.setMasID(sysotherin.getAutoID());
			sysAssemdetail.setModifyDate(now);
			sysAssemdetail.setModifyPerson(user.getName());
			sysAssemdetail.setBarcode(row.get("barcode"));
			sysAssemdetail.setSourceBillNoRow(row.get("sourcebillnorow"));
			sysAssemdetail.setQty(new BigDecimal(row.get("qty").toString()));
			sysAssemdetail.setSourceType(row.getStr("sourcebilltype"));
			sysAssemdetail.setSourceBillNo(row.getStr("sourcebillno"));
			sysAssemdetail.setSourceBillDid(row.getStr("sourcebilldid"));
			sysAssemdetail.setSourceBillID(row.getStr("sourcebilldid"));
			sysAssemdetail.setAssemType(row.getStr("assemtype"));
			sysAssemdetail.setWhCode(row.getStr("whcode"));
			sysAssemdetail.setPosCode(row.getStr("poscode"));
			sysAssemdetail.setCombinationNo(Integer.valueOf(row.getStr("combinationno")));
			sysAssemdetail.setRowNo(Integer.valueOf(row.getStr("rowno")));
			sysAssemdetail.setTrackType(row.getStr("tracktype"));
			sysAssemdetail.setMemo(row.getStr("memo"));

			sysAssemdetails.add(sysAssemdetail);

		}
		sysassemdetailservice.batchUpdate(sysAssemdetails);

		// 测试调用接口
		System.out.println("开始上传u8开始上传u8开始上传u8开始上传u8开始上传u8开始上传u8"+ new Date());
		Ret ret = pushU8(sysotherin, sysAssemdetails);
		System.out.println(new Date()+"u8上传结束，u8上传结束，u8上传结束，u8上传结束，u8上传结束"+ret);
	}
	//可编辑表格提交-删除数据
	private void deleteTableSubmitDatas(JBoltTable jBoltTable){
		Object[] ids = jBoltTable.getDelete();
		if(ArrayUtil.isEmpty(ids)) return;
		sysassemdetailservice.deleteByIds(ids);
	}

	public List<Record> getdictionary(Kv kv) {
		return dbTemplate("sysassem.dictionary", kv).find();
	}

	public List<Record> style(Kv kv) {
		return dbTemplate("sysassem.style", kv).find();
	}

	public List<Record> getBarcodeDatas(String q, Integer limit, String orgCode) {
		return dbTemplate("sysassem.getBarcodeDatas",Kv.by("q", q).set("limit",limit).set("orgCode",orgCode)).find();
	}


	//推送u8数据接口
	public Ret pushU8(SysAssem sysassem, List<SysAssemdetail> sysassemdetail) {
		if(!CollectionUtils.isNotEmpty(sysassemdetail)){
			return Ret.ok().msg("数据不能为空");
		}

		User user = JBoltUserKit.getUser();
		Map<String, Object> data = new HashMap<>();

		data.put("userCode",user.getUsername());
		data.put("organizeCode",this.getdeptid());
		data.put("token","");

		JSONObject preallocate = new JSONObject();


		preallocate.set("userCode",user.getUsername());
		preallocate.set("password","123456");
		preallocate.set("organizeCode",this.getdeptid());
		preallocate.set("CreatePerson",user.getId());
		preallocate.set("CreatePersonName",user.getName());
		preallocate.set("loginDate", DateUtil.format(new Date(), "yyyy-MM-dd"));
		preallocate.set("tag","AssemVouch");
		preallocate.set("type","AssemVouch");

		data.put("PreAllocate",preallocate);

		JSONArray maindata = new JSONArray();
		sysassemdetail.stream().forEach(s -> {
			JSONObject jsonObject = new JSONObject();
			jsonObject.set("IWhCode",s.getWhCode());
			jsonObject.set("iwhname","");
			jsonObject.set("invcode","");
			jsonObject.set("userCode",user.getUsername());
			jsonObject.set("organizeCode",this.getdeptid());
			jsonObject.set("OWhCode",s.getPosCode());
			jsonObject.set("owhname","");
			jsonObject.set("barcode",s.getBarcode());
			jsonObject.set("invstd","");
			jsonObject.set("invname","");
			jsonObject.set("CreatePerson",s.getCreatePerson());
			jsonObject.set("BillType",s.getAssemType());
			jsonObject.set("qty",s.getQty());
			jsonObject.set("CreatePersonName",user.getName());
			jsonObject.set("IRdName","");
			jsonObject.set("IRdType",sysassem.getIRdCode());
			jsonObject.set("ORdName","");
			jsonObject.set("ORdType",sysassem.getORdCode());
			jsonObject.set("Tag","AssemVouch");
			jsonObject.set("IDeptCode",sysassem.getDeptCode());
			jsonObject.set("ODeptCode",sysassem.getDeptCode());
			jsonObject.set("VouchTemplate","");
			jsonObject.set("RowNo",s.getRowNo());

			maindata.put(jsonObject);
		});
		data.put("MainData",maindata);

		//            请求头
		Map<String, String> header = new HashMap<>(5);
		header.put("Content-Type", "application/json");
		String url = "http://localhost:8081/web/erp/common/vouchProcessDynamicSubmit";

		try {
			String post = HttpApiUtils.httpHutoolPost(url, data, header);
			com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(post);
			if (isOk(post)) {
				if ("201".equals(jsonObject.getString("code"))) {
					return Ret.ok().setOk().data(jsonObject);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return fail("上传u8失败");
	}


	//通过当前登录人名称获取部门id
	public String getdeptid(){
		String dept = "001";
		User user = JBoltUserKit.getUser();
		Person person = personservice.findFirstByUserId(user.getId());
		if(null != person && "".equals(person)){
			dept = person.getCOrgCode();
		}
		return dept;
	}

}