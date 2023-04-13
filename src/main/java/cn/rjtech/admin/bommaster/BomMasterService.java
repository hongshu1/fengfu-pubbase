package cn.rjtech.admin.bommaster;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.rjtech.admin.bomcompare.BomCompareService;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.model.momdata.BomCompare;
import cn.rjtech.model.momdata.Inventory;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.BomMaster;
import com.jfinal.plugin.activerecord.Record;



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
	public Ret save(BomMaster bomMaster, Long userId, String userName, Date now) {
		if(bomMaster==null) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		bomMaster.setICreateBy(userId);
		bomMaster.setCCreateName(userName);
		bomMaster.setDCreateTime(now);
		// 设置默认审批流
		bomMaster.setIAuditStatus(0);
		bomMaster.setISource(1);
		bomMaster.setIUpdateBy(userId);
		bomMaster.setCUpdateName(userName);
		bomMaster.setDUpdateTime(now);
		bomMaster.setIsDeleted(false);
		bomMaster.setIsEnabled(true);
		bomMaster.setIOrgId(getOrgId());
		bomMaster.setCOrgCode(getOrgCode());
		bomMaster.setCOrgName(getOrgName());
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
	
	public Ret submitForm(String formJsonData, String tableJsonData) {
		ValidationUtils.notBlank(formJsonData, JBoltMsg.PARAM_ERROR);
		ValidationUtils.notBlank(tableJsonData, JBoltMsg.PARAM_ERROR);
		JSONObject formData = JSONObject.parseObject(formJsonData);
		JSONArray tableData = JSONObject.parseArray(tableJsonData);
		// 校验数据
		verification(formData, tableData);
        BomMaster bomMaster = JSONObject.parseObject(formJsonData, BomMaster.class);
        Long userId = JBoltUserKit.getUserId();
        String userName = JBoltUserKit.getUserName();
        DateTime now = DateUtil.date();
        
        
        
        // 主键id为空为 新增或者为修改
        if (ObjectUtil.isNull(bomMaster.getIAutoId())){
            // 设置主键id
            long bomMasterId = JBoltSnowflakeKit.me.nextId();
            bomMaster.setIAutoId(bomMasterId);
            List<BomCompare> bomCompareList = new ArrayList<>();
            for (int i=0; i<tableData.size(); i++){
                JSONObject row = tableData.getJSONObject(i);
                if (isAdd(row)){
                    continue;
                }
                // 获取一行的子件
                List<BomCompare> bomCompares = objectConversionOfToBomCompara(row, bomMasterId, userId, userName, now);
                bomCompareList.addAll(bomCompares);
            }
            ValidationUtils.notEmpty(bomCompareList, "未解析到子件数据");
            // 记录每行最小层次的id
			Map<String, Long> bomCompareMap = new HashMap<>();
			for (BomCompare bomCompare : bomCompareList){
				Integer level = bomCompare.getILevel();
				// level 等于1 说明是产品（半成品）下的半成品或部品
				if (1 == level){
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
				if (ObjectUtil.isNotNull(bomCompare.getIBOMMasterId())){
					continue;
				}
				String cInvLev = bomCompare.getCInvLev();
				String perCode = getPerCode(cInvLev);
				bomCompare.setIPid(bomCompareMap.get(perCode));
			}
			tx(() -> {
				save(bomMaster, userId, userName, now);
				bomCompareService.batchSave(bomCompareList);
				return true;
			});
        }
        
        return SUCCESS;
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
		ValidationUtils.notBlank(equipmentModelId, "机型为空");
		ValidationUtils.notBlank(formData.getString(BomMaster.CDOCNAME), "文件名称为空");
		ValidationUtils.notBlank(formData.getString(BomMaster.CDOCCODE), "文件编码为空");
		ValidationUtils.notBlank(formData.getString(BomMaster.DENABLEDATE), "启用日期为空");
		ValidationUtils.notBlank(formData.getString(BomMaster.DDISABLEDATE), "停用日期为空");
		// 校验当前存货是否为当前选择机型下的
		Inventory inventory = inventoryService.findById(inventoryId);
		ValidationUtils.notNull(inventory, JBoltMsg.DATA_NOT_EXIST);
		ValidationUtils.isTrue(equipmentModelId.equals(String.valueOf(inventory.getIEquipmentModelId())), "机型跟产品编码不匹配");
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
        String invItemId = row.getString(BomCompare.INVITEMID);
        // 片料
        String blankingItemId = row.getString(BomCompare.BLANKINGITEMID);
        // 卷料
        String originalItemId = row.getString(BomCompare.ORIGINALITEMID);
        // 分条料
        String slicingInvItemId = row.getString(BomCompare.SLICINGINVITEMID);
		
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
			ValidationUtils.notNull(row.getBigDecimal(BomCompare.INVQTY), "部品QTY不能为空");
		}
		if (StrUtil.isNotBlank(blankingItemId)){
			ValidationUtils.notNull(row.getBigDecimal(BomCompare.BLANKINGQTY), "片料可制件数不能为空");
			ValidationUtils.notNull(row.getBigDecimal(BomCompare.BLANKINGWEIGHT), "片料重量不能为空");
		}
		
		if (StrUtil.isNotBlank(originalItemId)){
			ValidationUtils.notNull(row.getBigDecimal(BomCompare.ORIGINALQTY), "原材料可制件数不能为空");
			ValidationUtils.notNull(row.getBigDecimal(BomCompare.ORIGINALWEIGHT), "原材料重量不能为空");
		}
		
		if (StrUtil.isNotBlank(slicingInvItemId)){
			ValidationUtils.notNull(row.getBigDecimal(BomCompare.SLICINGQTY), "分条料可制件数不能为空");
			ValidationUtils.notNull(row.getBigDecimal(BomCompare.SLICINGWEIGHT), "分条料重量不能为空");
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
		String invItemId = row.getString(BomCompare.INVITEMID);
		// 片料
		String blankingItemId = row.getString(BomCompare.BLANKINGITEMID);
		// 卷料
		String originalItemId = row.getString(BomCompare.ORIGINALITEMID);
		// 分条料
		String slicingInvItemId = row.getString(BomCompare.SLICINGINVITEMID);
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
		 *
		 */
		BomCompare bomCompare = null;
		// 部品 先赋值成品的存货id
        if (StrUtil.isNotBlank(row.getString(BomCompare.INVITEMID))){
           bomCompare = bomCompareService.createBomCompare(JBoltSnowflakeKit.me.nextId(), userId, userName, now, bomMasterId, null, row.getLong(BomCompare.INVITEMID), invLev, invLev, code, 0,
                    row.getBigDecimal(BomCompare.INVQTY), null, row.getLong(BomCompare.IVENDORID), row.getString(BomCompare.CMEMO),
					Boolean.valueOf(row.getString(BomCompare.ISOUTSOURCED)), false);
            bomCompares.add(bomCompare);
        }
		BomCompare blankBomCompare = null;
        // 片料
        if (StrUtil.isNotBlank(row.getString(BomCompare.BLANKINGITEMID))){
        	Long pid = null;
        	if (ObjectUtil.isNotNull(bomCompare)){
        		pid = bomCompare.getIAutoId();
			}
        	blankBomCompare = bomCompareService.createBomCompare(JBoltSnowflakeKit.me.nextId(), userId, userName, now, bomMasterId, pid, row.getLong(BomCompare.BLANKINGITEMID), invLev, invLev, code, 1,
                    row.getBigDecimal(BomCompare.BLANKINGQTY), row.getBigDecimal(BomCompare.BLANKINGWEIGHT), null, null, false, false);
            bomCompares.add(blankBomCompare);
        }
        // 分条料
		BomCompare slicingBomCompare = null;
        if (StrUtil.isNotBlank(row.getString(BomCompare.SLICINGINVITEMID))){
			
			/**
			 * 1.不存在片料:
			 * 			1-1: 存在部品，直接去部品id
			 * 			1-2: 不存在部品，去上一行最小层级的id
			 * 2.存在片料： 直接去片料的id
			 */
			Long pid = null;
			if (ObjectUtil.isNotNull(bomCompare)){
				pid = bomCompare.getIAutoId();
			}
			if (ObjectUtil.isNotNull(blankBomCompare)){
				pid = blankBomCompare.getIAutoId();
			}
			slicingBomCompare = bomCompareService.createBomCompare(JBoltSnowflakeKit.me.nextId(), userId, userName, now,bomMasterId, pid, row.getLong(BomCompare.SLICINGINVITEMID), invLev, invLev, code, 2,
                    row.getBigDecimal(BomCompare.SLICINGQTY), row.getBigDecimal(BomCompare.SLICINGWEIGHT), null, null, false, false);
            bomCompares.add(slicingBomCompare);
        }
        
        // 卷料
        if (StrUtil.isNotBlank(row.getString(BomCompare.ORIGINALITEMID))){
			Long pid = null;
			if (ObjectUtil.isNotNull(bomCompare)){
				pid = bomCompare.getIAutoId();
			}
			if (ObjectUtil.isNotNull(blankBomCompare)){
				pid = blankBomCompare.getIAutoId();
			}
			if (ObjectUtil.isNotNull(slicingBomCompare)){
				pid = slicingBomCompare.getIAutoId();
			}
            BomCompare originalBomCompare = bomCompareService.createBomCompare(JBoltSnowflakeKit.me.nextId(), userId, userName, now,bomMasterId, pid, row.getLong(BomCompare.ORIGINALITEMID), invLev, invLev, code, 3,
                    row.getBigDecimal(BomCompare.ORIGINALQTY), row.getBigDecimal(BomCompare.ORIGINALWEIGHT), null, null, false, false);
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
     * @param code1
     * @param code2
     * @param code3
     * @param code4
     * @param code5
     * @param code6
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
        if (!kv.containsKey("keyWords")){
            return createJsTreeBean(kv.getStr("enableIcon"), recordList);
        }
        List<Record> allRecordList = new ArrayList<>();
    	return createJsTreeBean(kv.getStr("enableIcon"), allRecordList);
	}
	
	public List<JsTreeBean> createJsTreeBean(String enableIconStr, List<Record> recordList){
        List<JsTreeBean> trees = new ArrayList<>();
        for (Record record : recordList){
            Long id = record.getLong("id");
            Object pid = record.get("pid");
            StringBuilder text = new StringBuilder(record.getStr("cinvcode"));
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
		Page<Record> test = test(pageNumber, pageSize, kv);
		kv.set("orgId", getOrgId());
		return dbTemplate("bommaster.datas", kv).paginate(pageNumber, pageSize);
	}
	
	public Page<Record> test(int pageNumber, int pageSize, Kv kv){
		StringBuilder sqlStr = new StringBuilder("WITH a ( iAutoId, pid ) AS ( " +
				" SELECT " +
				" b1.iAutoId ," +
				" b1.iPid AS pid " +
				" FROM " +
				" Bd_BomCompare AS b1 ");
		Long pid = kv.getLong("pid");
		if (StrUtil.isNotBlank(kv.getStr("pid"))){
			sqlStr.append(" where ").append(" b1.iPid = ").append(pid).append(" OR b1.iAutoId = ").append(pid);
		}
		sqlStr.append( " UNION ALL " +
				" SELECT\n" +
				" b2.iAutoId , " +
				" b2.iPid AS pid " +
				" FROM " +
				" Bd_BomCompare AS b2 " +
				" INNER JOIN a ON b2.iPid  = a.iAutoId  " +
				" ) \n");
		
		
		String a = new String(" SELECT DISTINCT inv.cInvCode,b.iautoId from a INNER JOIN Bd_BomCompare b ON a.iAutoId = b.iAutoId " +
				" INNER JOIN Bd_Inventory inv ON inv.iAutoId = b.iInventoryId");
		
		
		String b =new String(" SELECT COUNT(1) FROM (SELECT DISTINCT inv.cInvCode,b.iautoId from a INNER JOIN Bd_BomCompare b ON a.iAutoId = b.iAutoId " +
				" INNER JOIN Bd_Inventory inv ON inv.iAutoId = b.iInventoryId) a");
		
		
		String findSql =sqlStr.toString().concat(a);
		
		String totalRowSql =sqlStr.toString().concat(b);
		
       return Db.use(dataSourceConfigName()).paginateByFullSql(pageNumber, pageSize, totalRowSql, findSql);
    }
}
