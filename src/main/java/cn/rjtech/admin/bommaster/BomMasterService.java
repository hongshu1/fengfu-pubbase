package cn.rjtech.admin.bommaster;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.util.JBoltListMap;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.bomcompare.BomCompareService;
import cn.rjtech.admin.bommasterinv.BomMasterInvService;
import cn.rjtech.admin.customer.CustomerService;
import cn.rjtech.admin.equipmentmodel.EquipmentModelService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.enums.AuditStatusEnum;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import org.apache.poi.ss.usermodel.CellRange;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.SSCellRange;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 物料建模-Bom母项
 * @ClassName: BomMasterService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-28 16:39
 */
public class BomMasterService extends BaseService<BomMaster> {
	private final BomMaster dao=new BomMaster().dao();
	
	@Inject
	private InventoryService inventoryService;
	@Inject
	private BomCompareService bomCompareService;
	@Inject
	private EquipmentModelService equipmentModelService;
	@Inject
	private VendorService vendorService;
	@Inject
	private BomMasterInvService bomMasterInvService;
	@Inject
	private CustomerService customerService;
	
	@Override
	protected BomMaster dao() {
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
     * @param isEnabled 是否启用
     * @param isDeleted 是否删除 1已删除
	 * @return
	 */
	public Page<BomMaster> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isEnabled, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isEnabled",isEnabled);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cDocName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param bomMaster
	 * @param userId
	 * @param userName
	 * @param now
	 * @return
	 */
	public Ret save(BomMaster bomMaster, Long userId, String userName, Date now, int auditState) {
		if(bomMaster==null) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		bomMaster.setICreateBy(userId);
		bomMaster.setCCreateName(userName);
		bomMaster.setDCreateTime(now);
		
		bomMaster.setISource(SourceEnum.MES.getValue());
		bomMaster.setIUpdateBy(userId);
		bomMaster.setCUpdateName(userName);
		bomMaster.setDUpdateTime(now);
		bomMaster.setIsDeleted(false);
		bomMaster.setIsEnabled(true);
		bomMaster.setIOrgId(getOrgId());
		bomMaster.setCOrgCode(getOrgCode());
		bomMaster.setCOrgName(getOrgName());
		bomMaster.setIsEffective(false);
		// 设置默认审批流
		bomMaster.setIAuditStatus(auditState);
		//if(existsName(bomMaster.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=bomMaster.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(bomMaster.getIAutoId(), JBoltUserKit.getUserId(), bomMaster.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param bomMaster
	 * @param userId
	 * @param userName
	 * @param now
	 * @return
	 */
	public Ret update(BomMaster bomMaster, Long userId, String userName, Date now) {
		if(bomMaster==null || notOk(bomMaster.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		bomMaster.setCUpdateName(userName);
		bomMaster.setIUpdateBy(userId);
		bomMaster.setDUpdateTime(now);
		//更新时需要判断数据存在
		BomMaster dbBomMaster=findById(bomMaster.getIAutoId());
		if(dbBomMaster==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(bomMaster.getName(), bomMaster.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=bomMaster.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(bomMaster.getIAutoId(), JBoltUserKit.getUserId(), bomMaster.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param bomMaster 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(BomMaster bomMaster, Kv kv) {
		//addDeleteSystemLog(bomMaster.getIAutoId(), JBoltUserKit.getUserId(),bomMaster.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param bomMaster model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(BomMaster bomMaster, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(BomMaster bomMaster, String column, Kv kv) {
		//addUpdateSystemLog(bomMaster.getIAutoId(), JBoltUserKit.getUserId(), bomMaster.getName(),"的字段["+column+"]值:"+bomMaster.get(column));
		/**
		switch(column){
		    case "isEnabled":
		        break;
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}
	
	public Ret submitForm(String formJsonData, String tableJsonData, String commonInvData, Boolean flag) {
		ValidationUtils.notBlank(formJsonData, JBoltMsg.PARAM_ERROR);
		ValidationUtils.notBlank(tableJsonData, JBoltMsg.PARAM_ERROR);
		JSONObject formData = JSONObject.parseObject(formJsonData);
		JSONArray tableData = JSONObject.parseArray(tableJsonData);
		JSONObject invMap = JSONObject.parseObject(commonInvData);
		// 校验数据
		verification(formData, tableData);
        BomMaster bomMaster = JSONObject.parseObject(formJsonData, BomMaster.class);
        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        DateTime now = DateUtil.date();
        // 主键id为空为 新增或者为修改
        if (ObjUtil.isNull(bomMaster.getIAutoId())){
			return saveForm(bomMaster, tableData, userId, userName, now, invMap, flag);
        }
        return updateForm(bomMaster, tableData, userId, userName, now, invMap, flag);
	}
	
	public Ret saveForm(BomMaster bomMaster, JSONArray tableData, Long userId, String userName, DateTime now, JSONObject invMap, Boolean flag){
		// 设置主键id
		long bomMasterId = JBoltSnowflakeKit.me.nextId();
		bomMaster.setIAutoId(bomMasterId);
		List<BomCompare> bomCompareList =getBomCompareList(bomMasterId, tableData, userId, userName, now);
		tx(() -> {
			saveCommonCompare(invMap, flag);
			save(bomMaster, userId, userName, now, AuditStatusEnum.NOT_AUDIT.getValue());
			bomCompareService.batchSave(bomCompareList);
			return true;
		});
		return SUCCESS;
	}
	
	private void copyBomMaster(BomMaster bomMaster, BomMaster oldBomMaster){
		// 机型id
		oldBomMaster.setIEquipmentModelId(bomMaster.getIEquipmentModelId());
		// 文件编码
		oldBomMaster.setCDocCode(bomMaster.getCDocCode());
		// 文件名称
		oldBomMaster.setCDocName(bomMaster.getCDocName());
		// 版本
		oldBomMaster.setCBomVersion(bomMaster.getCBomVersion());
		// 启用日期
		oldBomMaster.setDEnableDate(bomMaster.getDEnableDate());
		// 停用日期
		oldBomMaster.setDDisableDate(bomMaster.getDDisableDate());
		// 序号1
		oldBomMaster.setCNo1(bomMaster.getCNo1());
		// 序号2
		oldBomMaster.setCNo2(bomMaster.getCNo2());
		// 设变号1
		oldBomMaster.setCDcNo1(bomMaster.getCDcNo1());
		// 设变号2
		oldBomMaster.setCDcNo2(bomMaster.getCDcNo2());
		// 设备日期1
		oldBomMaster.setDDcDate1(bomMaster.getDDcDate1());
		// 设备日期2
		oldBomMaster.setDDcDate2(bomMaster.getDDcDate2());
		// 存货id
		oldBomMaster.setIInventoryId(bomMaster.getIInventoryId());
		// 客户ID
		oldBomMaster.setICustomerId(bomMaster.getICustomerId());
		// 共用件备注
		oldBomMaster.setCCommonPartMemo(bomMaster.getCCommonPartMemo());
	}
	
	public Ret updateForm(BomMaster bomMaster, JSONArray tableData, Long userId, String userName, DateTime now, JSONObject invMap, Boolean flag){
		BomMaster oldBomMaster = findById(bomMaster.getIAutoId());
		Integer iAuditStatus = oldBomMaster.getIAuditStatus();
		AuditStatusEnum auditStatusEnum = AuditStatusEnum.toEnum(iAuditStatus);
		ValidationUtils.isTrue((AuditStatusEnum.NOT_AUDIT.getValue()==iAuditStatus || AuditStatusEnum.REJECTED.getValue()==iAuditStatus), "该物料清单状态为【"+auditStatusEnum.getText()+"】不能进行修改");
		// 复制
		copyBomMaster(bomMaster, oldBomMaster);
		List<BomCompare> bomCompareList = getBomCompareList(oldBomMaster.getIAutoId(), tableData, userId, userName, now);
		tx(() -> {
			// 将原来的删除掉子件数据 只有审核中才会有这个删除
//			bomMasterInvService.deleteByBomMasterId(oldBomMaster.getIAutoId());
//			bomMasterInvService.saveBomMasterInv(oldBomMaster.getIAutoId(), oldBomMaster.getIInventoryId(), bomCompareList);
			saveCommonCompare(invMap, flag);
			// 删除子件数据
			bomCompareService.deleteByBomMasterId(oldBomMaster.getIAutoId());
			update(oldBomMaster, userId, userName, now);
			bomCompareService.batchSave(bomCompareList);
			
			return true;
		});
		return SUCCESS;
	}
	
	private List<BomCompare> getBomCompareList(long bomMasterId, JSONArray tableData, Long userId, String userName, DateTime now){
		List<BomCompare> bomCompareList = new ArrayList<>();
		for (int i=0; i<tableData.size(); i++){
			JSONObject row = tableData.getJSONObject(i);
			if (isAdd(row)){
				continue;
			}
			// 获取一行的子件
			List<BomCompare> bomCompares = objectConversionOfToBomCompara(row, bomMasterId, userId, userName, now);
			ValidationUtils.notEmpty(bomCompares, "第"+(i+1)+"行存货编码未解析出来");
			bomCompareList.addAll(bomCompares);
		}
		ValidationUtils.notEmpty(bomCompareList, "未解析到子件数据");
		// 记录每行最小层次的id
		Map<String, Long> bomCompareMap = new HashMap<>();
		for (BomCompare bomCompare : bomCompareList){
			Integer level = bomCompare.getILevel();
			Long iPid = bomCompare.getIPid();
			// level 等于1 说明是产品（半成品）下的半成品或部品
			if (1 == level && ObjUtil.isNull(iPid)){
				bomCompare.setIPid(bomMasterId);
			}
			String code = bomCompare.getCInvLev();
			// map有记录当前编码，说明已经记录了，不需要重新记录
			if (bomCompareMap.containsKey(code)){
				continue;
			}
			// 记录相同的编码数据
			List<BomCompare> bomCompares = new ArrayList<>();
			// 找出相同的
			for (BomCompare fristBomCompare : bomCompareList){
				if (code.equals(fristBomCompare.getCInvLev())){
					bomCompares.add(fristBomCompare);
				}
			}
			bomCompares.sort(Comparator.comparing(obj -> obj.getIRawType()));
			bomCompareMap.put(code, bomCompares.get(0).getIAutoId());
		}
		
		// 找到上一级最小层级的id,除第一层级无需找
		for (BomCompare bomCompare : bomCompareList){
			Integer level = bomCompare.getILevel();
			if (1 == level){
				continue;
			}
			// 不等于null，说明在前面已经赋值了
			if (ObjUtil.isNotNull(bomCompare.getIPid())){
				continue;
			}
			String cInvLev = bomCompare.getCInvLev();
			String perCode = getPerCode(cInvLev);
			bomCompare.setIPid(bomCompareMap.get(perCode));
		}
		return bomCompareList;
	}
	
	public void verification(JSONObject formData, JSONArray tableData){
		verificationOfForm(formData);
		verificationOfTable(formData.getString(BomMaster.IINVENTORYID), tableData);
	}
	
	public void verificationOfForm(JSONObject formData){
		String inventoryId = formData.getString(BomMaster.IINVENTORYID);
		String equipmentModelId = formData.getString(BomMaster.IEQUIPMENTMODELID);
		// 普通校验
		ValidationUtils.notBlank(inventoryId, "产品存货编码为空");
		ValidationUtils.notBlank(formData.getString(BomMaster.CBOMVERSION), "版本/版次为空");
//		ValidationUtils.notBlank(equipmentModelId, "机型为空");
		ValidationUtils.notBlank(formData.getString(BomMaster.CDOCNAME), "文件名称为空");
		ValidationUtils.notBlank(formData.getString(BomMaster.CDOCCODE), "文件编码为空");
		ValidationUtils.notBlank(formData.getString(BomMaster.DENABLEDATE), "启用日期为空");
		ValidationUtils.notBlank(formData.getString(BomMaster.DDISABLEDATE), "停用日期为空");
		// 校验当前存货是否为当前选择机型下的
		Inventory inventory = inventoryService.findById(inventoryId);
		ValidationUtils.notNull(inventory, JBoltMsg.DATA_NOT_EXIST);
//		ValidationUtils.isTrue(equipmentModelId.equals(String.valueOf(inventory.getIEquipmentModelId())), "机型跟产品编码不匹配");
	}
	
	/**
	 *
	 * @param finishedProductId 产品id
	 * @param tableData 半成品，部品，原材料
	 */
	public void verificationOfTable(String finishedProductId, JSONArray tableData){
	    List<String> codes = new ArrayList<>();
		for (int i=0; i<tableData.size(); i++){
			JSONObject row = tableData.getJSONObject(i);
            if (isAdd(row)){
                return;
            }
			verificationOfTableRow(finishedProductId, row);
            String code = getCode(row);
            codes.add(code);
            // 记录
            row.put("code", code);
        }
        Set<String> codeSet = new HashSet<>(codes);
		ValidationUtils.isTrue(codes.size() == codeSet.size(), "编码栏不能出现重复");
	}
	
	public void verificationOfTableRow(String finishedProductId, JSONObject row){
        // 部品
        String invItemId = row.getString(BomCompare.INVITEMID.toLowerCase());
        // 片料
        String blankingItemId = row.getString(BomCompare.BLANKINGITEMID.toLowerCase());
        // 卷料
        String originalItemId = row.getString(BomCompare.ORIGINALITEMID.toLowerCase());
        // 分条料
        String slicingInvItemId = row.getString(BomCompare.SLICINGINVITEMID.toLowerCase());
		
		// 校验母件成品编码不能跟子件编码一致
		ValidationUtils.isTrue(!finishedProductId.equals(invItemId), "部品存货不能跟成品存货选择一致");
		ValidationUtils.isTrue(!finishedProductId.equals(blankingItemId), "片料（落料）存货不能跟成品存货选择一致");
		ValidationUtils.isTrue(!finishedProductId.equals(originalItemId), "卷料（原材料）存货不能跟成品存货选择一致");
		ValidationUtils.isTrue(!finishedProductId.equals(slicingInvItemId), "分条料存货不能跟成品存货选择一致");
		
		// 校验部品存货是否存在一致
		ValidationUtils.isTrue(!(StrUtil.isNotBlank(invItemId) && invItemId.equals(blankingItemId)), "部品存货不能跟片料（落料）存货选择一致");
		ValidationUtils.isTrue(!(StrUtil.isNotBlank(invItemId) && invItemId.equals(originalItemId)), "部品存货不能跟卷料（原材料）存货选择一致");
		ValidationUtils.isTrue(!(StrUtil.isNotBlank(invItemId) && invItemId.equals(slicingInvItemId)), "部品存货不能跟分条料存货选择一致");
		
		// 校验（原材料）存货是否存在一致
		ValidationUtils.isTrue(!(StrUtil.isNotBlank(originalItemId) && originalItemId.equals(slicingInvItemId)), "卷料（原材料）存货不能跟分条料存货选择一致");
		ValidationUtils.isTrue(!(StrUtil.isNotBlank(originalItemId) && originalItemId.equals(blankingItemId)), "卷料（原材料）存货不能跟片料（落料）存货选择一致");
		// 分条料 校验
		ValidationUtils.isTrue(!(StrUtil.isNotBlank(slicingInvItemId) && slicingInvItemId.equals(blankingItemId)), "分条料存货不能跟片料（落料）存货选择一致");
		
		String code1 = row.getString(BomCompare.CODE1);
		String code2 = row.getString(BomCompare.CODE2);
		String code3 = row.getString(BomCompare.CODE3);
		String code4 = row.getString(BomCompare.CODE4);
		String code5 = row.getString(BomCompare.CODE5);
		String code6 = row.getString(BomCompare.CODE6);
		
		// 校验编码栏是否都为空
		boolean codeFlag = StrUtil.isBlank(code1)
				&& StrUtil.isBlank(code2)
				&& StrUtil.isBlank(code3)
				&& StrUtil.isBlank(code4)
				&& StrUtil.isBlank(code5)
				&& StrUtil.isBlank(code6);
		ValidationUtils.isTrue(!codeFlag, "选择存货后，编码栏不能为空");
		int count = 0;
		if (StrUtil.isNotBlank(code1)){
		    count+=1;
        }
        
        if (StrUtil.isNotBlank(code2)){
            count+=1;
        }
        if (StrUtil.isNotBlank(code3)){
            count+=1;
        }
        if (StrUtil.isNotBlank(code4)){
            count+=1;
        }
        if (StrUtil.isNotBlank(code5)){
            count+=1;
        }
        if (StrUtil.isNotBlank(code6)){
            count+=1;
        }
        ValidationUtils.isTrue(count==1, "编号栏只能填写一列");
        
        if (StrUtil.isNotBlank(invItemId)){
			ValidationUtils.notNull(row.getBigDecimal(BomCompare.INVQTY.toLowerCase()), "部品QTY不能为空");
		}

	}
	
	
	/**
	 * 拼接编码栏确保唯一
	 * @param row
	 * @return
	 */
	public String getCode(JSONObject row){
		return getCode(row.getString(BomCompare.CODE1), row.getString(BomCompare.CODE2), row.getString(BomCompare.CODE3), row.getString(BomCompare.CODE4)
				,row.getString(BomCompare.CODE5), row.getString(BomCompare.CODE6));
	}
	
	/**
	 * 是否添加
	 * @param row
	 * @return
	 */
	private boolean isAdd(JSONObject row){
		// 部品
		String invItemId = row.getString(BomCompare.INVITEMID.toLowerCase());
		// 片料
		String blankingItemId = row.getString(BomCompare.BLANKINGITEMID.toLowerCase());
		// 卷料
		String originalItemId = row.getString(BomCompare.ORIGINALITEMID.toLowerCase());
		// 分条料
		String slicingInvItemId = row.getString(BomCompare.SLICINGINVITEMID.toLowerCase());
		// 判断物料是否都为空，为空全部跳过
		return StrUtil.isBlank(blankingItemId) && StrUtil.isBlank(invItemId) && StrUtil.isBlank(originalItemId) &&StrUtil.isBlank(slicingInvItemId);
	}
	
	public List<BomCompare> objectConversionOfToBomCompara(JSONObject row, Long bomMasterId, Long userId, String userName, Date now){
        List<BomCompare> bomCompares = new ArrayList<>();
		String code = getCode(row.getString(BomCompare.CODE1),
                row.getString(BomCompare.CODE2),
                row.getString(BomCompare.CODE3),
                row.getString(BomCompare.CODE4),
                row.getString(BomCompare.CODE5),
                row.getString(BomCompare.CODE6));
        // 获取层次及上一级id
        int invLev;
        if (code.equals(row.getString(BomCompare.CODE6))){
            invLev = 6;
        }else if (code.equals(row.getString(BomCompare.CODE5))){
            invLev = 5;
        }else if (code.equals(row.getString(BomCompare.CODE4))){
            invLev = 4;
        }else if (code.equals(row.getString(BomCompare.CODE3))){
            invLev = 3;
        }else if (code.equals(row.getString(BomCompare.CODE2))){
            invLev = 2;
        }else{
            invLev = 1;
        }
        
        // 原材料类型：0.部品 1. 片料 2. 分条料 3. 卷料
		
		/**
		 * 二种情况：
		 * 	第一种（一行填写二个及以上的存货）：
		 *
		 *
		 * 	第二种（一行填写一个存货，以上一个部品作为父级）
		 *
		 */
		BomCompare bomCompare = null;
		// 部品 先赋值成品的存货id
        if (StrUtil.isNotBlank(row.getString(BomCompare.INVITEMID.toLowerCase()))){
           bomCompare = bomCompareService.createBomCompare(JBoltSnowflakeKit.me.nextId(), userId, userName, now, bomMasterId, null,
				   row.getLong(BomCompare.INVITEMID.toLowerCase()), invLev, invLev, code, 0,
                    row.getBigDecimal(BomCompare.INVQTY.toLowerCase()), row.getBigDecimal(BomCompare.INVWEIGHT.toLowerCase()),
				   row.getLong(BomCompare.IVENDORID.toLowerCase()), row.getString(BomCompare.CMEMO.toLowerCase()),
					Boolean.valueOf(row.getString(BomCompare.ISOUTSOURCED.toLowerCase())), false);
            bomCompares.add(bomCompare);
        }
		BomCompare blankBomCompare = null;
        // 片料
        if (StrUtil.isNotBlank(row.getString(BomCompare.BLANKINGITEMID.toLowerCase()))){
        	Long pid = null;
        	if (ObjUtil.isNotNull(bomCompare)){
        		pid = bomCompare.getIAutoId();
			}
        	blankBomCompare = bomCompareService.createBomCompare(JBoltSnowflakeKit.me.nextId(), userId, userName, now, bomMasterId, pid,
					row.getLong(BomCompare.BLANKINGITEMID.toLowerCase()), invLev, invLev, code, 1,
                    row.getBigDecimal(BomCompare.BLANKINGQTY.toLowerCase()), row.getBigDecimal(BomCompare.BLANKINGWEIGHT.toLowerCase()),
					null, null, false, false);
            bomCompares.add(blankBomCompare);
        }
        // 分条料
		BomCompare slicingBomCompare = null;
        if (StrUtil.isNotBlank(row.getString(BomCompare.SLICINGINVITEMID.toLowerCase()))){
			
			/**
			 * 1.不存在片料:
			 * 			1-1: 存在部品，直接去部品id
			 * 			1-2: 不存在部品，去上一行最小层级的id
			 * 2.存在片料： 直接去片料的id
			 */
			Long pid = null;
			if (ObjUtil.isNotNull(bomCompare)){
				pid = bomCompare.getIAutoId();
			}
			if (ObjUtil.isNotNull(blankBomCompare)){
				pid = blankBomCompare.getIAutoId();
			}
			slicingBomCompare = bomCompareService.createBomCompare(JBoltSnowflakeKit.me.nextId(), userId, userName, now,bomMasterId, pid,
					row.getLong(BomCompare.SLICINGINVITEMID.toLowerCase()), invLev, invLev, code, 2,
                    row.getBigDecimal(BomCompare.SLICINGQTY.toLowerCase()), row.getBigDecimal(BomCompare.SLICINGWEIGHT.toLowerCase()), null,
					null, false, false);
            bomCompares.add(slicingBomCompare);
        }
        
        // 卷料
        if (StrUtil.isNotBlank(row.getString(BomCompare.ORIGINALITEMID.toLowerCase()))){
			Long pid = null;
			if (ObjUtil.isNotNull(bomCompare)){
				pid = bomCompare.getIAutoId();
			}
			if (ObjUtil.isNotNull(blankBomCompare)){
				pid = blankBomCompare.getIAutoId();
			}
			if (ObjUtil.isNotNull(slicingBomCompare)){
				pid = slicingBomCompare.getIAutoId();
			}
            BomCompare originalBomCompare = bomCompareService.createBomCompare(JBoltSnowflakeKit.me.nextId(), userId, userName, now,bomMasterId, pid,
					row.getLong(BomCompare.ORIGINALITEMID.toLowerCase()), invLev, invLev, code, 3,
                    row.getBigDecimal(BomCompare.ORIGINALQTY.toLowerCase()), row.getBigDecimal(BomCompare.ORIGINALWEIGHT.toLowerCase()),
					null, null, false, false);
            bomCompares.add(originalBomCompare);
        }
        return bomCompares;
    }
    
    public String getPerCode(String code){
		String split = "-";
		if (!code.contains(split)) return null;
		return code.substring(0, code.lastIndexOf(split));
	}
    
    /**
     * 获取当前部品属于第几层级
     * @return
     */
    public String getCode(String code1, String code2, String code3, String code4, String code5, String code6){
	    if (StrUtil.isNotBlank(code6)){
	       return code6;
        }
        if (StrUtil.isNotBlank(code5)){
            return code5;
        }
        if (StrUtil.isNotBlank(code4)){
            return code4;
        }
        if (StrUtil.isNotBlank(code3)){
            return code3;
        }
        if (StrUtil.isNotBlank(code2)){
            return code2;
        }
	    return code1;
    }
    
    public List<JsTreeBean> getDatas(Kv kv){
    	kv.set("orgId", getOrgId());
		List<Record> recordList = dbTemplate("bommaster.datas", kv).find();
        if (StrUtil.isBlank(kv.getStr("keyWords"))){
            return createJsTreeBean(kv.getStr("enableIcon"), recordList);
        }
        
        Map<String, Record> bomCompareMap = new HashMap<>();
        
  		for (Record record : recordList){
			String pid = record.getStr("pid");
			String id = record.getStr("id");
			// pid等于空说明 当前为产品/半成品
			if (StrUtil.isBlank(pid)){
				bomCompareMap.put(id, record);
				continue;
			}
			// 已近包含了父级
			if (bomCompareMap.containsKey(pid)){
				continue;
			}
			bomCompareMap.put(id, record);
		}
  
		List<Record> allRecordList = new ArrayList<>();
    	return createJsTreeBean(kv.getStr("enableIcon"), allRecordList);
	}
	
	public List<JsTreeBean> createJsTreeBean(String enableIconStr, List<Record> recordList){
        List<JsTreeBean> trees = new ArrayList<>();
        for (Record record : recordList){
            Long id = record.getLong("id");
            Object pid = record.get("pid");
            StringBuilder text = new StringBuilder(record.getStr("cinvname"));
            if (pid == null) {
                pid = "#";
                if (StrUtil.isNotBlank(enableIconStr)){
                    String enableIcon = enableIconStr;
                    enableIcon = enableIcon.replace("?", record.getStr("id"));
                    text.append(enableIcon);
                }
            }
            JsTreeBean jsTreeBean = new JsTreeBean(id, pid, text.toString(), true);
            trees.add(jsTreeBean);
        }
       return trees;
    }
	
	public Page<Record> getPageData(int pageNumber, int pageSize, Kv kv){
		int end = pageNumber * pageSize;
		if (end <= 0) {
			end = pageSize;
		}
		
		int begin = (pageNumber - 1) * pageSize;
		if (begin < 0) {
			begin = 0;
		}
		
		Integer size = queryInt("EXEC BD_BOMTREE_COUNT @orgId=?, @pId= ?,@itemCode = ?,@itemName = ?", getOrgId(), kv.getLong("pid"), kv.getStr("invCode"), kv.getStr("invName"));
		
		if (size == null || size < 0){
			return new Page(new ArrayList(0), pageNumber, pageSize, 0, 0);
		}
		
		long totalRow = Long.valueOf(size);
		int totalPage = (int)(totalRow / (long)pageSize);
		if (totalRow % (long)pageSize != 0L) {
			++totalPage;
		}
		
		if (pageNumber > totalPage) {
			return new Page(new ArrayList(0), pageNumber, pageSize, totalPage, (int)totalRow);
		} else {
			List<Record> list = findRecord("EXEC BD_BOMTree_Page @orgId=?, @pId= ?,@itemCode = ?,@itemName = ?,@pageNumber=?,@pageSize = ?", false,
					getOrgId(),
					kv.getLong("pid"),
					kv.getStr("invCode"),
					kv.getStr("invName"),
				    begin,
					end);
			return new Page(list, pageNumber, pageSize, totalPage, (int)totalRow);
		}
		
	}
	
	public Map<String, Object>  importExcelFile(UploadFile file) throws IOException {
//		StringBuilder errorMsg = new StringBuilder();
		return readInfoFromXls(new FileInputStream(file.getFile()));
	}
	
	/**
	 * 导入excel数据
	 */
	private Map<String, Object> readInfoFromXls(InputStream in) throws IOException {
		Map<String, Object> repMap = new HashMap();
		XSSFWorkbook workbook = new XSSFWorkbook(in);
		// 循环工作表Sheet workbook.getNumberOfSheets()
		/*for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {

		}*/
		XSSFSheet sheet = workbook.getSheetAt(0);
		ValidationUtils.notNull(sheet, "未解析到sheet数据");
		// 获取表单数据
		Record headForm = buildForm(sheet);
		// 获取表格子件数据
		List<Record> bodyTable = buildTable(sheet);
		repMap.put("head", headForm);
		repMap.put("body", bodyTable);
		return repMap;
	}
	
	/**
	 * 获取表头数据
	 * @param sheet
	 * @return
	 */
	private Record buildForm(XSSFSheet sheet){
		Record record = new Record();
		/**
		 * 合并单元格取数
 		 */
		// 机型
		Inventory inventory= null;
		CellRange<XSSFCell> equipmentModelCellRange = createCellRange(sheet, new CellRangeAddress(2, 2, 1, 6));
		XSSFCell[] flattenedCells = equipmentModelCellRange.getFlattenedCells();
		String equipmentModelName = getCellRangeValue(flattenedCells);
		// 机型名称不为空，则找到对应的机型id
		EquipmentModel equipmentModel = StrUtil.isNotBlank(equipmentModelName) ? equipmentModelService.findByName(equipmentModelName) : null;
		if (ObjUtil.isNotNull(equipmentModel)){
			record.set(BomMaster.IEQUIPMENTMODELID, equipmentModel.getIAutoId());
		}
		record.set(BomMaster.EQUIPMENTMODELNAME, equipmentModelName);
		// 文 件 名 称
		CellRange<XSSFCell> docNameCellRange = createCellRange(sheet, new CellRangeAddress(2, 2, 8, 13));
		record.set(BomMaster.CDOCNAME, getCellRangeValue(docNameCellRange.getFlattenedCells()));
		// 文 件 编 号
		CellRange<XSSFCell> docCodeCellRange = createCellRange(sheet, new CellRangeAddress(2, 2, 14, 15));
		record.set(BomMaster.CDOCCODE, getCellRangeValue(docCodeCellRange.getFlattenedCells()));
		// 启用日期
		CellRange<XSSFCell> enableDateCellRange = createCellRange(sheet, new CellRangeAddress(2, 2, 18, 20));
		String enableDateStr = getCellRangeValue(enableDateCellRange.getFlattenedCells());
		Date enableDate = null;
		if (StrUtil.isNotBlank(enableDateStr)){
			enableDate = DateUtil.parseDate(enableDateStr);
		}
		record.set(BomMaster.DENABLEDATE, getDateConventStr(enableDate));
		// NO.1
		CellRange<XSSFCell> no1CellRange = createCellRange(sheet, new CellRangeAddress(5, 5, 12, 13));
		record.set(BomMaster.CNO1, getCellRangeValue(no1CellRange.getFlattenedCells()));
		// 客户部番
		CellRange<XSSFCell> invCode1CellRange = createCellRange(sheet, new CellRangeAddress(5, 5, 14, 15));
		record.set(BomMaster.INVCODE1, getCellRangeValue(invCode1CellRange.getFlattenedCells()));
		/**
		 * 通过下标找到对应的值
		 */
		// 版本号
		XSSFCell bomVersionCell = sheet.getRow(2).getCell(16);
		record.set(BomMaster.CBOMVERSION, getStringCellValue(bomVersionCell));
		// 停用日期
		XSSFCell disableCell = sheet.getRow(2).getCell(21);
		String disableDateStr = disableCell.getStringCellValue();
		Date disableDate = null;
		if (StrUtil.isNotBlank(disableDateStr)){
			disableDate = DateUtil.parseDate(disableDateStr);
		}
		record.set(BomMaster.DDISABLEDATE, getDateConventStr(disableDate));
		// 设变号1
		XSSFCell dcNo1Cell = sheet.getRow(5).getCell(16);
		record.set(BomMaster.CDCNO1, getStringCellValue(dcNo1Cell));
		// 设变日期1
		XSSFCell docDate1 = sheet.getRow(5).getCell(18);
		record.set(BomMaster.DDCDATE1, getDateConventStr(docDate1.getDateCellValue()));
		// NO.2
		XSSFCell no2Cell = sheet.getRow(5).getCell(20);
		record.set(BomMaster.CNO2, getStringCellValue(no2Cell));
		// UG部番
		XSSFCell cInvAddCode1Cell = sheet.getRow(5).getCell(21);
		record.set(BomMaster.INVADDCODE1, getStringCellValue(cInvAddCode1Cell));
		// 产品编码
		XSSFCell cInvCodeCell = sheet.getRow(5).getCell(19);
		String cInvCode = getStringCellValue(cInvCodeCell);
		record.set(Inventory.CINVCODE, cInvCode);
		
		// 设变号2
		XSSFCell dcNo2Cell = sheet.getRow(5).getCell(23);
		record.set(BomMaster.CDCNO2, getStringCellValue(dcNo2Cell));
		
		// 重量
//		XSSFCell dcNo2Cell = sheet.getRow(5).getCell(24);
//		record.set(BomMaster.CDCNO2, getStringCellValue(dcNo2Cell));
		
		// 设变日期2
		XSSFCell dcDate2 = sheet.getRow(5).getCell(25);
		record.set(BomMaster.DDCDATE2, getDateConventStr(dcDate2.getDateCellValue()));
		
		// 客户：GHAC
		XSSFCell customerNameCell = sheet.getRow(7).getCell(1);
		String customerName = getStringCellValue(customerNameCell);
		// 赋值客户id
		if (StrUtil.isNotBlank(customerName)){
			String customerStr = customerName.contains("客户：") ? customerName.substring(customerName.lastIndexOf("客户：")) : customerName;
			Record customerRecord = customerService.findByVendorName(customerStr);
			if (ObjUtil.isNotNull(customerRecord)){
				record.set(BomMaster.ICUSTOMERID, customerRecord.getLong("cvenid"));
			}
			record.set(BomMaster.USTOMERNAME, customerStr);
		}
		// 共用件: 2WD/4WD 共用
		XSSFCell commonPartMemoCell = sheet.getRow(7).getCell(8);
		String commonPartMemo = getStringCellValue(commonPartMemoCell);
		record.set(BomMaster.CCOMMONPARTMEMO, commonPartMemo);
		
		if (StrUtil.isNotBlank(cInvCode)){
			inventory = inventoryService.findBycInvCode(cInvCode);
		}
		// 赋值存货id
		if (inventory != null){
			record.set(BomMaster.IINVENTORYID, inventory.getIAutoId());
			record.set(Inventory.IWEIGHT, inventory.getIWeight());
			record.set(BomMaster.INVCODE1, inventory.getCInvCode1());
			record.set(BomMaster.INVADDCODE1, inventory.getCInvAddCode1());
		}
		return record;
	}
	
	private String getDateConventStr(Date date){
		if (ObjUtil.isNull(date)){
			return null;
		}
		return DateUtil.formatDate(date);
	}
	
	private String getStringCellValue(XSSFCell cell){
		if (cell == null){
			return null;
		}
		cell.setCellType(CellType.STRING);
		return cell.getStringCellValue();
	}
	
	private String getCellRangeValue(XSSFCell[] flattenedCells){
		if (flattenedCells==null || flattenedCells.length<1){
			return null;
		}
		String value = null;
		for (XSSFCell cell : flattenedCells){
			cell.setCellType(CellType.STRING);
			if (StrUtil.isBlank(cell.getStringCellValue())){
				continue;
			}
			value = cell.getStringCellValue();
		}
		return value;
	}
	
	
	private String getBigDecimalCell(String value){
		if (StrUtil.isBlank(value)){
			return null;
		}
		DecimalFormat df = new DecimalFormat("#0.000");
		BigDecimal num = new BigDecimal(value);
		return df.format(num);
	}
	
	/**
	 * 解析excel
	 */
	private List<Record> buildTable(XSSFSheet sheet) {
		List<Record> list = new ArrayList<>();
		// 记录上一行有编码栏的值
		Record perCacheRecord = null;
		// 子件数据从第十一行开始读，下标10
		for (int i=10; i<=sheet.getLastRowNum(); i++){
			XSSFRow row = sheet.getRow(i);
			if (ObjUtil.isNull(row) || ObjUtil.isNull(row.getCell(1))){
				perCacheRecord = null;
				continue;
			}
			Record rowRecord = new Record();
			// 编码栏1-6
			String codeKey = null;
			// 编码值
			String codeValue = null;
			// 先获取编码
			for (int iCode=1; iCode<7; iCode++){
				XSSFCell cell = row.getCell(iCode);
				if (ObjUtil.isNull(cell)){
					continue;
				}
				cell.setCellType(CellType.STRING);
				String stringCellValue = cell.getStringCellValue();
				if (StrUtil.isBlank(stringCellValue)){
					continue;
				}
				// 通过下标确定具体是那一栏
				switch (iCode){
					case 1:
						codeKey = BomCompare.CODE1;
						break;
					case 2:
						codeKey = BomCompare.CODE2;
						break;
					case 3:
						codeKey = BomCompare.CODE3;
						break;
					case 4:
						codeKey = BomCompare.CODE4;
						break;
					case 5:
						codeKey = BomCompare.CODE5;
						break;
					default:
						codeKey = BomCompare.CODE6;
						break;
				}
				codeValue = stringCellValue;
				break;
			}
			// 编码全部为空，则跳过（第二层开始查找存货）
			if (StrUtil.isBlank(codeKey)){
				// 上一层对象没有记录则直接跳过
				if (ObjUtil.isNull(perCacheRecord)){
					continue;
				}
				buildLastRow(sheet, row,rowRecord);
				int perIndexOf = list.lastIndexOf(perCacheRecord);
				if (perIndexOf > -1){
					Record record = list.get(perIndexOf);
					record.setColumns(rowRecord);
				}
				perCacheRecord = null;
				continue;
			}else {
				rowRecord.set(codeKey, codeValue);
			}
			// 补充第一行所有数据行
			buildFristRow(sheet, row, rowRecord);
			// 记录当前行
			perCacheRecord = rowRecord;
			list.add(rowRecord);
		}
		return list;
	}
	
	private void buildFristRow(XSSFSheet sheet, XSSFRow row, Record record){
		// 部品编码
		XSSFCell itemCell = row.getCell(7);
		record.set(BomCompare.INVITEMCODE, getStringCellValue(itemCell));
		// 客户部番
		CellRange<XSSFCell> cInvCode1CellRange = createCellRange(sheet, new CellRangeAddress(row.getRowNum(), row.getRowNum(), 8, 9));
		String cInvCode1 = getCellRangeValue(cInvCode1CellRange.getFlattenedCells());
		record.set(BomCompare.CINVCODE1, cInvCode1);
		// UG部番
		CellRange<XSSFCell> cInvAddCode1CellRange = createCellRange(sheet, new CellRangeAddress(row.getRowNum(), row.getRowNum(), 10, 11));
		String cInvAddCode1 = getCellRangeValue(cInvAddCode1CellRange.getFlattenedCells());
		record.set(BomCompare.CINVADDCODE1, cInvAddCode1);
		// QTY
		XSSFCell qtyCell = row.getCell(14);
		record.set(BomCompare.INVQTY, getBigDecimalCell(getStringCellValue(qtyCell)));
		// 材料类别
		XSSFCell materialTypeCell = row.getCell(15);
		record.set(BomCompare.MATERIALTYPE, getStringCellValue(materialTypeCell));
		// 原材料编码
		XSSFCell originalItemCodeCell = row.getCell(16);
		record.set(BomCompare.ORIGINALITEMCODE, getStringCellValue(originalItemCodeCell));
		
		// 原材料供应商
		XSSFCell originalvendorNameCell = row.getCell(17);
		record.set(BomCompare.ORIGINALQTY, getStringCellValue(originalvendorNameCell));
		
		// 原材料可制件数
		XSSFCell originalQtyCell = row.getCell(18);
		record.set(BomCompare.ORIGINALQTY, getBigDecimalCell(getStringCellValue(originalQtyCell)));
		
		// 分条料编码
		XSSFCell slicingItemCodeCell = row.getCell(19);
		record.set(BomCompare.SLICINGINVITEMCODE, getStringCellValue(slicingItemCodeCell));
		
		// 分条料可制件数
		XSSFCell slicingQtyCell = row.getCell(21);
		record.set(BomCompare.SLICINGQTY, getBigDecimalCell(getStringCellValue(slicingQtyCell)));
		
		// 落料编码
		XSSFCell blankingItemCodeCell = row.getCell(22);
		record.set(BomCompare.BLANKINGITEMCODE, getStringCellValue(blankingItemCodeCell));
		
		// 落料可制件数
		XSSFCell blankingQtyCell = row.getCell(24);
		record.set(BomCompare.BLANKINGQTY, getBigDecimalCell(getStringCellValue(blankingQtyCell)));
		// 部品加工商
		XSSFCell vendorNameCell = row.getCell(25);
		String stringCellValue = getStringCellValue(vendorNameCell);
		String vendorName = null;
		if (StrUtil.isNotBlank(stringCellValue)){
			Vendor vendor = vendorService.findByName(stringCellValue);
			if (vendor != null){
				vendorName = vendor.getCVenName();
				record.set(BomCompare.IVENDORID, vendor.getIAutoId());
			}
		}
		record.set(BomCompare.VENDORNAME, vendorName);
		// 是否外作
		XSSFCell isOutSourcedCell = row.getCell(26);
		int isOutSourced = 0;
		if ("是".equals(getStringCellValue(isOutSourcedCell))){
			isOutSourced = 1;
		}
		record.set(BomCompare.ISOUTSOURCED, isOutSourced);
		// 备注
		XSSFCell cMeCell = row.getCell(27);
		record.set(BomCompare.CMEMO, getStringCellValue(cMeCell));
		// 匹配存货编码
		setInventory(record);
	}
	
	private void buildLastRow(XSSFSheet sheet, XSSFRow row, Record record){
		
		// 部品名称
		CellRange<XSSFCell> cInvName1CellRange = createCellRange(sheet, new CellRangeAddress(row.getRowNum(), row.getRowNum(), 7, 8));
		record.set(BomCompare.CINVNAME1, getCellRangeValue(cInvName1CellRange.getFlattenedCells()));
		// UG部品名称
		CellRange<XSSFCell> cInvName2CellRange = createCellRange(sheet, new CellRangeAddress(row.getRowNum(), row.getRowNum(), 9, 10));
		record.set(BomCompare.CINVNAME2, getCellRangeValue(cInvName2CellRange.getFlattenedCells()));
		
		// 重量(KG)
		XSSFCell weightCell = row.getCell(14);
		record.set(BomCompare.INVWEIGHT, getBigDecimalCell(getStringCellValue(weightCell)));
		/*// 厚度
		XSSFCell thicknessCell = row.getCell(16);
		record.set(BomCompare.THICKNESS, getStringCellValue(thicknessCell));*/
		// 材质
		XSSFCell materialCell = row.getCell(16);
		record.set(BomCompare.MATERIAL, getStringCellValue(materialCell));
		// 原材料规格(mm)
		XSSFCell originalStdCell = row.getCell(17);
		record.set(BomCompare.ORIGINALSTD, getStringCellValue(originalStdCell));
		// 原材料重量(KG)
		XSSFCell originalWeightCell = row.getCell(18);
		record.set(BomCompare.ORIGINALWEIGHT, getBigDecimalCell(getStringCellValue(originalWeightCell)));
		
		// 分条规格(mm)
		XSSFCell slicingStdCell = row.getCell(20);
		record.set(BomCompare.SLICINGSTD, getStringCellValue(slicingStdCell));
		
		// 分条料重量(KG)
		XSSFCell slicingWeightCell = row.getCell(21);
		record.set(BomCompare.SLICINGWEIGHT, getBigDecimalCell(getStringCellValue(slicingWeightCell)));
		// 落料重量(KG)
		XSSFCell blankingWeightCell = row.getCell(24);
		record.set(BomCompare.BLANKINGWEIGHT, getBigDecimalCell(getStringCellValue(blankingWeightCell)));
	}
	
	private CellRange<XSSFCell> createCellRange(XSSFSheet hssfSheet, CellRangeAddress range){
		int firstRow = range.getFirstRow();
		int firstColumn = range.getFirstColumn();
		int lastRow = range.getLastRow();
		int lastColumn = range.getLastColumn();
		int height = lastRow - firstRow + 1;
		int width = lastColumn - firstColumn + 1;
		List<XSSFCell> temp = new ArrayList(height * width);
		
		for(int rowIn = firstRow; rowIn <= lastRow; ++rowIn) {
			for(int colIn = firstColumn; colIn <= lastColumn; ++colIn) {
				XSSFRow row = hssfSheet.getRow(rowIn);
				if (row == null) {
					row = hssfSheet.createRow(rowIn);
				}
				
				XSSFCell cell = row.getCell(colIn);
				if (cell == null) {
					cell = row.createCell(colIn);
				}
				
				temp.add(cell);
			}
		}
		
		return SSCellRange.create(firstRow, firstColumn, height, width, temp, XSSFCell.class);
	}
	
	public Page<Record> getVersionRecord(Integer pageNumber, Integer pageSize, Kv kv) {
		Page<Record> page = dbTemplate("bommaster.getVersionRecord", kv.set("orgId", getOrgId())).paginate(pageNumber, pageSize);
		if (CollUtil.isEmpty(page.getList())){
			return page;
		}
		for (Record record : page.getList()){
			AuditStatusEnum auditStatusEnum = AuditStatusEnum.toEnum(record.getInt(BomMaster.IAUDITSTATUS));
			record.set(BomMaster.AUDITSTATUSSTR, auditStatusEnum.getText());
		}
		return page;
	}
	
	public void setInventory(Record record){
		
		String invItemCode = record.getStr(BomCompare.INVITEMCODE);
		
		String originalItemCode = record.getStr(BomCompare.ORIGINALITEMCODE);
		
		String slicingItemCode = record.getStr(BomCompare.SLICINGINVITEMCODE);
		
		String blankingItemCode = record.getStr(BomCompare.BLANKINGITEMCODE);
		
		Inventory inventory = null;
		// 部品 通过客户部番跟UG部番查找
		if (StrUtil.isNotBlank(invItemCode)){
			inventory = inventoryService.findBycInvCode(invItemCode);
		}
		
		// 部品id
		if (inventory != null){
			record.set(BomCompare.INVITEMID, inventory.getIAutoId());
			record.set(BomCompare.INVITEMCODE, inventory.getCInvCode());
			record.set(BomCompare.CINVNAME, inventory.getCInvName());
		}
		
		if (StrUtil.isNotBlank(originalItemCode)){
			Inventory originalInventory = inventoryService.findBycInvCode(originalItemCode);
			setItemRecord(record, originalInventory, 0);
		}
		if (StrUtil.isNotBlank(slicingItemCode)){
			Inventory slicingInventory = inventoryService.findBycInvCode(slicingItemCode);
			setItemRecord(record, slicingInventory, 1);
		}
		
		if (StrUtil.isNotBlank(blankingItemCode)){
			Inventory blankingInventory = inventoryService.findBycInvCode(blankingItemCode);
			setItemRecord(record, blankingInventory, 2);
		}
	}
	
	/**
	 * 赋值（存货id，存货编码，存货名称）
	 * @param record
	 * @param inventory
	 * @param type 0:原材料 1： 分条料 2：漏料
	 */
	private void setItemRecord(Record record, Inventory inventory, int type){
		if (ObjUtil.isNull(inventory)){
			return;
		}
		switch (type){
			case 0:
				record.set(BomCompare.ORIGINALITEMID, inventory.getIAutoId());
				record.set(BomCompare.ORIGINALITEMCODE, inventory.getCInvCode());
				record.set(BomCompare.ORIGINALSTD, inventory.getCInvStd());
				record.set(BomCompare.ORIGINALINVNAME, inventory.getCInvName());
				break;
			case 1:
				record.set(BomCompare.SLICINGINVITEMID, inventory.getIAutoId());
				record.set(BomCompare.SLICINGINVITEMCODE, inventory.getCInvCode());
				record.set(BomCompare.SLICINGSTD, inventory.getCInvStd());
				record.set(BomCompare.SLICINGINVNAME, inventory.getCInvName());
				break;
			case 2:
				record.set(BomCompare.BLANKINGITEMID, inventory.getIAutoId());
				record.set(BomCompare.BLANKINGITEMCODE, inventory.getCInvCode());
				record.set(BomCompare.BLANKINGSTD, inventory.getCInvStd());
				record.set(BomCompare.BLANKINGINVNAME, inventory.getCInvName());
				break;
		}
	}
	
	public List<Record> queryBomMasterId(Kv kv){
		return dbTemplate("bommaster.getVersionRecord", kv.set("orgId", getOrgId())).find();
	}
	
	public Ret del(Long bomMasterId) {
		ValidationUtils.notNull(bomMasterId, JBoltMsg.PARAM_ERROR);
		BomMaster bomMaster = findById(bomMasterId);
		ValidationUtils.notNull(bomMaster, JBoltMsg.DATA_NOT_EXIST);
		tx(() -> {
		    // 校验审批中或已审批的数据不能进行删除
            Integer iAuditStatus = bomMaster.getIAuditStatus();
            AuditStatusEnum auditStatusEnum = AuditStatusEnum.toEnum(iAuditStatus);
//            ValidationUtils.isTrue((AuditStatusEnum.NOT_AUDIT.getValue()==iAuditStatus || AuditStatusEnum.REJECTED.getValue()==iAuditStatus), "该物料清单状态为【"+auditStatusEnum.getText()+"】不能进行删除");
			// 删除母件
			bomMaster.setIsDeleted(true);
			bomMaster.setIUpdateBy(JBoltUserKit.getUserId());
			bomMaster.setCUpdateName(JBoltUserKit.getUserName());
			bomMaster.setDUpdateTime(DateUtil.date());
			// 设置为失效
			bomMaster.setIsEffective(false);
			bomMaster.update();
			// 删除母件下所有子件数据
			bomMasterInvService.deleteByBomMasterId(bomMasterId);
			return true;
		});
		return SUCCESS;
	}
	
	public Ret saveCopy(Long bomMasterId, String cVersion) {
		ValidationUtils.notBlank(cVersion, JBoltMsg.JBOLTTABLE_IS_BLANK);
		ValidationUtils.notNull(bomMasterId, JBoltMsg.PARAM_ERROR);
		BomMaster bomMaster = findById(bomMasterId);
		ValidationUtils.notNull(bomMaster, JBoltMsg.DATA_NOT_EXIST);
		ValidationUtils.isTrue(!cVersion.equals(bomMaster.getCBomVersion()), "版本号不能一致");
		
		// 获取母件所有子件集合
		tx(() -> {
			saveCopyBomMaster(bomMasterId, cVersion, bomMaster);
			return true;
		});
		return SUCCESS;
	}
	
	
	private void saveCopyBomMaster(Long bomMasterId, String cVersion, BomMaster bomMaster){
		// 设置新的id
		long newBomMasterId = JBoltSnowflakeKit.me.nextId();
		List<BomCompare> bomCompareList = getNewBomCompareList(bomMasterId, newBomMasterId, bomCompareService.findByBomMasterIdList(bomMasterId));
		operation(bomMasterId, newBomMasterId, cVersion, bomMaster, bomCompareList);
	}
    
    /**
     * 操作
     * @param bomMasterId
     * @param newBomMasterId
     * @param cVersion
     * @param bomMaster
     * @param bomCompareList
     */
	private void operation(Long bomMasterId, Long newBomMasterId, String cVersion, BomMaster bomMaster, List<BomCompare> bomCompareList){
		bomMaster.setIAutoId(newBomMasterId);
		bomMaster.setCBomVersion(cVersion);
		bomMaster.setDAuditTime(null);
		// 设置copy前的ID
		bomMaster.setICopyFromId(bomMasterId);
		DateTime now = DateUtil.date();
		bomMaster.setDAuditTime(now);
		bomCompareService.batchSave(bomCompareList);
		save(bomMaster,JBoltUserKit.getUserId(),JBoltUserKit.getUserName(), now, AuditStatusEnum.AWAIT_AUDIT.getValue());
		
	}
	
	/**
	 * 将查询出来的子件更换ip
 	 * @param bomMasterId
	 * @param newBomMasterId
	 * @return
	 */
	private List<BomCompare> getNewBomCompareList(Long bomMasterId, Long newBomMasterId, List<BomCompare> bomCompareList){
		
		Map<Long, Long> pidMap = new HashMap<>();
		// 先复制产品的id替换成新的
		pidMap.put(bomMasterId, newBomMasterId);
		for (BomCompare bomCompare :  bomCompareList){
			Long id = bomCompare.getIAutoId();
			Long newId = JBoltSnowflakeKit.me.nextId();
			bomCompare.setIAutoId(newId);
			// 设置新的bomMasterId
			bomCompare.setIBOMMasterId(newBomMasterId);
			// key为旧id， value为新id
			pidMap.put(id, newId);
		}
		for (BomCompare bomCompare : bomCompareList){
			Long pid = bomCompare.getIPid();
			// 替换成新的id
			bomCompare.setIPid(pidMap.get(pid));
		}
		return bomCompareList;
	}
	
	/**
	 * 更新共用件 物料清单
	 * @param invMap
	 * @param flag 校验是否存在共用件在审批中
	 */
	private void saveCommonCompare(JSONObject invMap, Boolean flag){
		if (ObjUtil.isNull(invMap)){
			return;
		}
		ValidationUtils.isTrue(flag, "此物料已关联其它成品/半成品物料BOM且再审批中，不能进行升级版本");
		// 去重母件id
		Set<Long> setBomMasterId = new HashSet<>();
		// 记录相同存货id的重量跟数量
		Map<Long, JSONObject> itemMap = new HashMap<>();
		for (String invId :invMap.keySet()){
			List<JSONObject> list  = (List<JSONObject>) invMap.get(invId);
			for (JSONObject jsonObject : list){
				setBomMasterId.add(jsonObject.getLong(BomCompare.IBOMMASTERID));
				if (itemMap.containsKey(invId)){
					continue;
				}
				itemMap.put(Long.valueOf(invId), jsonObject);
			}
		}
		for (Long bomMasterId :setBomMasterId){
			BomMaster bomMaster = findById(bomMasterId);
			String newBomVersion = upgradeVersion(bomMaster.getCBomVersion());
			// 生成新的id
			long newBomMasterId = JBoltSnowflakeKit.me.nextId();
			List<BomCompare> bomCompareList = getNewBomCompareList(bomMasterId, newBomMasterId, bomCompareService.findByBomMasterIdList(bomMasterId));
			for (BomCompare bomCompare : bomCompareList){
				Long inventoryId = bomCompare.getIInventoryId();
				if (itemMap.containsKey(inventoryId)){
					JSONObject jsonObject = itemMap.get(inventoryId);
					bomCompare.setIQty(jsonObject.getBigDecimal("qty"));
					bomCompare.setIWeight(jsonObject.getBigDecimal("weight"));
				}
			}
			operation(bomMasterId, newBomMasterId, newBomVersion, bomMaster, bomCompareList);
		}
	}
	
	/**
	 * 升级版本号
	 * @param bomVersion
	 * @return
	 */
	public String upgradeVersion(String bomVersion){
		return bomVersion.concat("1");
	}
	
	public Ret audit(Long bomMasterId, Integer status) {
		ValidationUtils.notNull(bomMasterId, JBoltMsg.JBOLTTABLE_IS_BLANK);
		ValidationUtils.notNull(status, JBoltMsg.JBOLTTABLE_IS_BLANK);
		BomMaster bomMaster = findById(bomMasterId);
		ValidationUtils.notNull(bomMaster, JBoltMsg.DATA_NOT_EXIST);
		DateTime date = DateUtil.date();
		bomMaster.setIAuditStatus(status);
		bomMaster.setDAuditTime(date);
		update(bomMaster, JBoltUserKit.getUserId(), JBoltUserKit.getUserName(), date);
		tx(() -> {
			bomMaster.setIsEffective(true);
			// 将生效的bom改为失效
			updateNotEffective(bomMaster.getIInventoryId());
			BomMaster first = findFirst("select * from Bd_BomMaster where isEffective = 1 and iInventoryId = ?", bomMaster.getIInventoryId());
			if (ObjUtil.isNotNull(first)){
				// 将失效的bom母件记录删除
				bomMasterInvService.deleteByBomMasterId(first.getIAutoId());
			}
			// 保存最新的一份
			bomMasterInvService.saveBomMasterInv(bomMasterId, bomMaster.getIInventoryId());
			update(bomMaster, JBoltUserKit.getUserId(), JBoltUserKit.getUserName(), date);
			
//			-----------推送U8-----------------
			
			
			
			
			// 删除母件下所有子件数据
			return true;
		});
		return SUCCESS;
	}
	
	/**
	 * 将其余生效的改为失效
	 * @param inventoryId
	 */
	public void updateNotEffective(long	inventoryId){
		update("UPDATE Bd_BomMaster SET isEffective = 0 WHERE iInventoryId = ?", inventoryId);
	}
	
	/**
	 * 校验共用件问题。
	 * @param bomMasterId
	 * @param tableJsonData
	 * @return
	 */
	public Ret checkCommonInv(Long bomMasterId, String tableJsonData) {
		ValidationUtils.notBlank(tableJsonData, JBoltMsg.JBOLTTABLE_IS_BLANK);
		JSONArray jsonArray = JSONObject.parseArray(tableJsonData);
		Map<String, List<JSONObject>> invMap = new HashMap<>();
		boolean flag = true;
		for (Object o : jsonArray){
			JSONObject jsonObject = (JSONObject) o;
			String invId = jsonObject.getString("id");
			List<JSONObject> list = invMap.containsKey(invId) ? invMap.get(invId) : new ArrayList<>();
			list.add(jsonObject);
			invMap.put(invId, list);
		}
		// 记录共同件的重量或数量不一致的数据
		Map<String, List<JSONObject>> commonInvMap = new HashMap<>();
		for (String invId : invMap.keySet()){
			List<JSONObject> invList = invMap.get(invId);
			List<JSONObject> masterList = new ArrayList<>();
			// 用于判断 同一种存货编码是否出现多个子件下的bomMaster相同
			Map<Long, String> masterIdMap = new HashMap<>();
			for (JSONObject item : invList){
				// 校验物料是否有存在审批中
				List<Record> awaitCommonInv = bomCompareService.getCommonInv(bomMasterId, invId, item.getString("qty"), item.getString("weight"), 1, false);
				// 为true才会校验， false
				if (flag){
					// 判断是否为空
					flag = CollUtil.isEmpty(awaitCommonInv);
				}
				List<Record> commonInv = bomCompareService.getCommonInv(bomMasterId, invId, item.getString("qty"), item.getString("weight"), 2, true);
				// 校验是否存在不一样存货重量及数量的生效物料清单
				if (CollUtil.isNotEmpty(commonInv)){
						for (Record record : commonInv){
							Long masterId = record.getLong(BomCompare.IBOMMASTERID);
							if (masterIdMap.containsKey(masterId)){
								continue;
							}
							masterIdMap.put(masterId, null);
							// 记录bomMasterId
							item.put(BomCompare.IBOMMASTERID, masterId);
							// 设置bomId
							item.put(BomCompare.IAUTOID, record.getLong(BomCompare.IAUTOID));
							masterList.add(item);
						}
				}
			}
			if (CollUtil.isNotEmpty(masterList)){
				commonInvMap.put(invId, masterList);
			}
		}
		
		if (CollUtil.isNotEmpty(commonInvMap)){
			return successWithData(Okv.by("data", commonInvMap).set("flag", flag));
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @param id
	 * @return
	 */
	public String test(Long id){
		BomMaster bomMaster = findById(id);
		// 获取所有数据
		List<Record> compareList = findRecords("EXEC BD_BOMTree @orgId = ?, @pId =?", getOrgId(), id);
		JBoltListMap<Long, Record> listMap = new JBoltListMap<>();
		for (Record record : compareList){
			listMap.addItem(record.getLong("pid"), record);
		}
		
		if (CollUtil.isEmpty(listMap)){
			return null;
		}
		DateTime date = DateUtil.date();
		List<Record> repList = new ArrayList<>();
		
		for (Record record : compareList){
			Long bomMasterId = record.getLong("id");
			if (listMap.containsKey(bomMasterId)){
				Record masterJson = createMasterJson(bomMaster, date, record, listMap.get(bomMasterId));
				repList.add(masterJson);
			}
		}
		
		for (Long masterId : listMap.keySet()){
			List<Record> list = listMap.get(masterId);
			// 等于空，说明是成品或半成品
			if (masterId == null){
				continue;
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("data", repList);
		return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
	}
	
	public Record createMasterJson(BomMaster bomMaster, Date date, Record masterRecord, List<Record> compareList){
		Record record = new Record();
		// bom唯一标识
		record.set("bomid", masterRecord.getStr("id"));
		// 母件编码
		record.put("pinvcode", masterRecord.getStr("cInvCode"));
		// 母件名称
		record.put("pinvname", masterRecord.getStr("cInvName"));
		// 规格型号
		record.put("pinvstd", masterRecord.getStr("cInvStd"));
		// BOM类别
		record.put("bomtype", null);
		// 版本代号
		record.set("version", bomMaster.getCBomVersion());
		//
		record.set("versiondesc", "mes审核推单");
		// 状态：审核
		record.set("bomstatus", "审核");
		// 建档人
		record.set("CreateUser", bomMaster.getCCreateName());
		// 建档时间 "2021-02-10 12:29:30"
		record.set("dCreateDate", DateUtil.formatDateTime(bomMaster.getDCreateTime()));
		// 修改人
		record.set("ModifyUser", bomMaster.getCUpdateName());
		// 修改时间
		record.set("ModifyDate", DateUtil.formatDateTime(bomMaster.getDUpdateTime()));
		// 审核人
		record.set("RelsUser", JBoltUserKit.getUserName());
		// 审核时间
		record.set("RelsTime", DateUtil.formatDateTime(date));
		// 子件
		record.set("items", getCompareListJson(compareList));
		return record;
	}
	
	public Record createCompareJson(Record compareRecord){
		Record record = new Record();
		// bomId
		record.set("bomid", compareRecord.getStr("id"));
		// 子件编号
		record.set("cinvcode", compareRecord.getStr("cInvCode"));
		// 子件名称
		record.set("cinvname", compareRecord.getStr("cInvName"));
		// 子件规格
		record.set("cinvstd", compareRecord.getStr("cInvStd"));
		// 子件行号
		record.set("SortSeq", compareRecord.getStr("SortSeq"));
		// 单位
		record.set("cComUnitName", compareRecord.getStr("cUomName"));
		// 基本用量
		record.set("BaseQtyN", compareRecord.getStr("iQty"));
		// 基础数量
		record.set("BaseQtyD", "1");
		record.set("subitems", null);
		
		return record;
	}
	
	/**
	 *
	 * @param compareList 子件值
	 * @return
	 */
	public List<Record> getCompareListJson(List<Record> compareList){
		List<Record> jsonList = new ArrayList<>();
		int index = 1;
		for (Record record : compareList){
			// 设置次序
			record.set("SortSeq", index*10);
			jsonList.add(createCompareJson(record));
			index++;
		}
		return jsonList;
	}
	
	public Record getBomMasterByInvId(Long orgId, Long invId){
		return dbTemplate("bommaster.getBomMasterByInvId", Okv.by("orgId", orgId).set("invId", invId)).findFirst();
	}
	
	
	public Page<Record> findBomCompareByBomMasterInvId(Kv kv, Integer pageNumber, Integer pageSize){
		Okv okv = Okv.by("orgId", getOrgId()).set(kv);
		return dbTemplate("bommaster.findBomCompareByBomMasterInvId", okv).paginate(pageNumber, pageSize);
	}
	
}
