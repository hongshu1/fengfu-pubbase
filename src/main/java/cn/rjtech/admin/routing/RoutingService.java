package cn.rjtech.admin.routing;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
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
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
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
import java.util.stream.Collectors;

/**
 * 物料建模-工艺路线
 * @ClassName: BomMasterService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-28 16:39
 */
public class RoutingService extends BaseService<BomMaster> {
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
	
    public List<JsTreeBean> findRoutingAll(Kv kv){
    	kv.set("orgId", getOrgId());
		List<Record> recordList = dbTemplate("routing.findRoutingAll", kv).find();
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
            String id = record.getStr(InventoryRoutingConfig.IAUTOID);
			String pid = record.getStr(InventoryRoutingConfig.IPID);
			String invName = record.getStr(Inventory.CINVNAME);
			if (StrUtil.isBlank(invName)){
				String cOperationName = record.getStr(InventoryRoutingConfig.COPERATIONNAME);
				invName = "【虚拟件".concat(": ").concat(cOperationName).concat("】");
			}
			StringBuilder text = new StringBuilder(invName);
            if (pid == null) {
                pid = "#";
                if (StrUtil.isNotBlank(enableIconStr)){
                    String enableIcon = enableIconStr;
                    enableIcon = enableIcon.replace("?", id);
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
			if (ObjectUtil.isNotNull(first)){
				// 将失效的bom母件记录删除
				bomMasterInvService.deleteByBomMasterId(first.getIAutoId());
			}
			// 保存最新的一份
			bomMasterInvService.saveBomMasterInv(bomMasterId, bomMaster.getIInventoryId());
			update(bomMaster, JBoltUserKit.getUserId(), JBoltUserKit.getUserName(), date);
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
	
}
