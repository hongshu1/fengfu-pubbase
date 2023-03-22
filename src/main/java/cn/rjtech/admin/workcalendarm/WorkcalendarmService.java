package cn.rjtech.admin.workcalendarm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt._admin.dept.DeptService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.Dept;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.service.JBoltDictionaryService;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.workcalendard.WorkcalendardService;
import cn.rjtech.model.momdata.Workcalendard;
import cn.rjtech.model.momdata.Workcalendarm;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.*;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 工作日历 Service
 * @ClassName: WorkcalendarmService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-10-27 09:31
 */
public class WorkcalendarmService extends BaseService<Workcalendarm> {

	private final Workcalendarm dao=new Workcalendarm().dao();

	@Override
	protected Workcalendarm dao() {
		return dao;
	}

	@Inject
	private WorkcalendardService workcalendardService;
	@Inject
	private JBoltDictionaryService jBoltDictionaryService;
	@Inject
	private DeptService deptService;

	/**
	 * 后台管理分页查询
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
		Page<Record> page = dbTemplate("workcalendarm.paginateAdminDatas", kv).paginate(pageNumber, pageSize);

		List<Dictionary> list = jBoltDictionaryService.getListByTypeKey("week_type",true);

		for(Record record : page.getList()){
			//处理多部门
			record.set("deptnames", deptService.getDeptNameByDeptids(record.getStr("idepids")));
			//处理周次
			List<Dictionary> dictionaries = new ArrayList<>();
			for(Dictionary dictionary : list){
				if(isOk(record.getStr("cweekday")) && record.getStr("cweekday").contains(dictionary.getSn())){
					dictionaries.add(dictionary);
				}
			}
			record.set("dics", dictionaries);
		}

		return page;
	}

	public List<Record> getDataExport(Kv kv){
		List<Record> list = dbTemplate("workcalendarm.getDataExport", kv).find();
		List<Dictionary> dictionaries = jBoltDictionaryService.getListByTypeKey("week_type",true);
		for(Record record : list){
			record.set("deptnames", deptService.getDeptNameByDeptids(record.getStr("idepids")));
			record.set("dstarttimed", DateUtil.formatDate(record.getDate("dstarttimed")));
			record.set("dendtimed", DateUtil.formatDate(record.getDate("dendtimed")));

			StringBuilder cWeekday = new StringBuilder();
			//处理工作日
			for(Dictionary dictionary : dictionaries){
				if(isOk(record.getStr("cweekday")) && record.getStr("cweekday").contains(dictionary.getSn())){
					  cWeekday.append(dictionary.getName()).append(",");

				}
			}
			record.set("cweekday", cWeekday.toString());
		}
		return list;
	}

	/**
	 * 保存
	 */
	public Ret save(Workcalendarm workcalendarm) {
		if(workcalendarm==null || isOk(workcalendarm.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(workcalendarm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=workcalendarm.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(workcalendarm.getIautoid(), JBoltUserKit.getUserId(), workcalendarm.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 */
	public Ret update(Workcalendarm workcalendarm) {
		if(workcalendarm==null || notOk(workcalendarm.getIautoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Workcalendarm dbWorkcalendarm=findById(workcalendarm.getIautoid());
		if(dbWorkcalendarm==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(workcalendarm.getName(), workcalendarm.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=workcalendarm.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(workcalendarm.getIautoid(), JBoltUserKit.getUserId(), workcalendarm.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String id : StrSplitter.split(ids, COMMA, true, true)) {
				Workcalendarm workcalendarm = findById(id);
				ValidationUtils.notNull(workcalendarm, JBoltMsg.DATA_NOT_EXIST);
				workcalendarm.setIsdeleted(true);
				workcalendarm.update();
			}
			return true;
		});
		return SUCCESS;
		//return deleteByIds(ids,true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param workcalendarm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(Workcalendarm workcalendarm, Kv kv) {
		//addDeleteSystemLog(workcalendarm.getIautoid(), JBoltUserKit.getUserId(),workcalendarm.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param workcalendarm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanDelete(Workcalendarm workcalendarm, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(workcalendarm, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	/**
	 * 切换isenabled属性
	 */
	public Ret toggleIsenabled(Long id) {
		return toggleBoolean(id, "isEnabled");
	}

	/**
	 * 切换isdeleted属性
	 */
	public Ret toggleIsdeleted(Long id) {
		return toggleBoolean(id, "isDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param workcalendarm 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanToggle(Workcalendarm workcalendarm,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Workcalendarm workcalendarm, String column, Kv kv) {
		//addUpdateSystemLog(workcalendarm.getIautoid(), JBoltUserKit.getUserId(), workcalendarm.getName(),"的字段["+column+"]值:"+workcalendarm.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param workcalendarm model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkInUse(Workcalendarm workcalendarm, Kv kv) {
		//这里用来覆盖 检测Workcalendarm是否被其它表引用
		return null;
	}

	public Workcalendarm findWorkcalendarNameInfo(String workShiftName){
		return dao().findFirst("SELECT * FROM Bd_WorkCalendarM WHERE cCalendarName = ?", workShiftName);
	}

	public Ret updateEditTable(JBoltTable jBoltTable, Long userId, Date now) {
		Workcalendarm workcalendarm = jBoltTable.getFormModel(Workcalendarm.class, "workcalendarm");
		ValidationUtils.notNull(workcalendarm, JBoltMsg.PARAM_ERROR);

		tx(() -> {

			//修改
			if(isOk(workcalendarm.getIautoid())){
				workcalendarm.setIupdateby(JBoltUserKit.getUserId());
				workcalendarm.setDupdatetime(now);
				workcalendarm.setCupdatename(JBoltUserKit.getUserName());
				ValidationUtils.isTrue(workcalendarm.update(), JBoltMsg.FAIL);
			}else{
				//新增
				//名称是否存在
				ValidationUtils.isTrue(findWorkcalendarNameInfo(workcalendarm.getCcalendarname())==null, "名称重复");
				workcalendarm.setIautoid(JBoltSnowflakeKit.me.nextId());
				workcalendarm.setIcreateby(JBoltUserKit.getUserId());
				workcalendarm.setCcreatename(JBoltUserKit.getUserName());
				workcalendarm.setDcreatetime(now);

				workcalendarm.setCorgcode("1");
				workcalendarm.setCorgname("1");
				workcalendarm.setIorgid(getOrgId());

				ValidationUtils.isTrue(workcalendarm.save(), JBoltMsg.FAIL);
			}
			//新增
			List<Workcalendard> saveRecords = jBoltTable.getSaveModelList(Workcalendard.class);
			if (CollUtil.isNotEmpty(saveRecords)) {
				for (Workcalendard row : saveRecords) {
					row.setIworkcalendarid(workcalendarm.getIautoid());
				}
				workcalendardService.batchSave(saveRecords, 500);
			}

			//修改
			List<Workcalendard> updateRecords = jBoltTable.getUpdateModelList(Workcalendard.class);
			if (CollUtil.isNotEmpty(updateRecords)) {
				workcalendardService.batchUpdate(updateRecords, 500);
			}

			// 删除
			Object[] deletes = jBoltTable.getDelete();
			if (ArrayUtil.isNotEmpty(deletes)) {
				workcalendardService.deleteMultiByIds(deletes);
			}

			return true;
		});

		return SUCCESS;
	}


	public Ret importExcelData(File file) {
		StringBuilder errorMsg=new StringBuilder();
		JBoltExcel jBoltExcel=JBoltExcel
				//从excel文件创建JBoltExcel实例
				.from(file)
				//设置工作表信息
				.setSheets(
						JBoltExcelSheet.create("sheet1")
								//设置列映射 顺序 标题名称
								.setHeaders(
										JBoltExcelHeader.create("idepids","适用部门"),
										JBoltExcelHeader.create("cyear","年度"),
										JBoltExcelHeader.create("ccalendarname","日历名称"),
										JBoltExcelHeader.create("cweekday","工作日"),
										JBoltExcelHeader.create("ccalendarsn","假期类型"),
										JBoltExcelHeader.create("dstarttime","开始时间"),
										JBoltExcelHeader.create("dendtime","结束时间")
								)
								//特殊数据转换器
								.setDataChangeHandler((data,index) ->{

								})
								//从第三行开始读取
								.setDataStartRow(3)
				);
		//从指定的sheet工作表里读取数据
		List<Record> models = JBoltExcelUtil.readRecords(jBoltExcel, "sheet1", true, errorMsg);
		if(notOk(models)) {
			if(errorMsg.length()>0) {
				return fail(errorMsg.toString());
			}else {
				return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
			}
		}

		//读取数据没有问题后判断必填字段
		if(models.size()>0){
			List<Workcalendarm> workcalendarmList = new ArrayList<>();
			List<Workcalendard> workcalendardList = new ArrayList<>();
			Date now = new Date();
			Set<Kv> workcalendarmSet = new HashSet<>();
			//处理主表数据
			for(Record p:models){
				ValidationUtils.notNull(p.getStr("idepids"), "适用部门编码！");
				ValidationUtils.notNull(p.getStr("cyear"), "年度为空！");
				ValidationUtils.notNull(p.getStr("ccalendarname"), "日历名称！");
				ValidationUtils.notNull(p.getStr("cweekday"), "工作日！");

				Dept dept = deptService.findFirst(Okv.by("sn", p.getStr("idepids")));
				ValidationUtils.notNull(dept, "部门编码不存在！");
				workcalendarmSet.add(Kv.by("idepids", dept.getId()).set("cyear",p.getStr("cyear")).set("ccalendarname", p.getStr("ccalendarname")).set("cweekday", p.getStr("cweekday")));
			}

			for(Kv kv : workcalendarmSet){
				Workcalendarm workcalendarm = new Workcalendarm();
				workcalendarm.setIautoid(JBoltSnowflakeKit.me.nextId())
						.setIdepids(kv.getStr("idepids"))
						.setCyear(kv.getStr("cyear"))
						.setCcalendarname(kv.getStr("ccalendarname"))
						.setCweekday(kv.getStr("cweekday"))
						.setIcreateby(JBoltUserKit.getUserId())
						.setCcreatename(JBoltUserKit.getUserName())
						.setDcreatetime(now)

						.setCorgcode(getOrgCode())
						.setCorgname(getOrgName())
						.setIorgid(getOrgId());
				workcalendarmList.add(workcalendarm);

				//处理细表
				for(Record p:models){
					if(isOk(p.getStr("cyear")) && p.getStr("ccalendarname").equals(workcalendarm.getCcalendarname())){
						Workcalendard workcalendard = new Workcalendard();
						workcalendard.setIworkcalendarid(workcalendarm.getIautoid())
								.setCcalendarsn(p.getStr("ccalendarsn"))
								.setDstarttime(new DateTime(p.getStr("dstarttime")))
								.setDendtime(new DateTime(p.getStr("dendtime")));
						workcalendardList.add(workcalendard);
					}
				}
			}
			tx(() -> {
				batchSave(workcalendarmList);
				workcalendardService.batchSave(workcalendardList);
				return true;
			});

		}

		return SUCCESS;
	}

}