package cn.rjtech.admin.calendar;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.JBoltBaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.Calendar;
import cn.rjtech.util.DateUtils;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日历配置 Service
 * @ClassName: CalendarService
 * @author: chentao
 * @date: 2022-11-23 15:04
 */
public class CalendarService extends JBoltBaseService<Calendar> {
	private final Calendar dao=new Calendar().dao();
	@Override
	protected Calendar dao() {
		return dao;
	}
	
	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<Calendar> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("dTakeDate","ASC", pageNumber, pageSize, keywords, "dTakeDate");
	}

	public List<String> takeDateList(Okv okv){
		return dbTemplate("calendar.getTakeDateList",okv).query();
	}
	public List<String> getTakeDateListByYear(Kv kv){
		String year = kv.getStr("year");
		return dbTemplate("calendar.getTakeDateListByYear",kv.set("startdate",year.concat("-01-01")).set("enddate",year.concat("-12-31"))).query();
	}
	public Ret saveCalendar(Kv kv) {
		String year = kv.getStr("year");
		String type = kv.getStr("type");
		String dates = kv.getStr("dates");
		List<Calendar> calendarList = new ArrayList<>();
		JSONArray jsonArray = JSONArray.parseArray(dates);
		for (int i = 0; i < jsonArray.size(); i++) {
			String dateStr = jsonArray.getString(i);
			Calendar calendar = new Calendar();
			calendar.setIAutoId(JBoltSnowflakeKit.me.nextId());
			calendar.setCSourceCode(type);
			calendar.setCSourceType(type);
			calendar.setDTakeDate(DateUtils.parseDate(dateStr));
			calendar.setICaluedarType(true);
			calendar.setICreateBy(JBoltUserKit.getUserId());
			calendar.setCCreateName(JBoltUserKit.getUserName());
			calendar.setDCreateTime(new Date());
			calendarList.add(calendar);
		}
		tx(() -> {
			delCalendar(Okv.by("startdate",year.concat("-01-01")).set("enddate",year.concat("-12-31")).set("type",type));

			batchSave(calendarList);
			return true;
		});
		return SUCCESS;
	}
	public Integer delCalendar(Okv okv) {
		return dbTemplate("calendar.delCalendar",okv).delete();
	}
	/**
	 * 保存
	 * @param calendar
	 * @return
	 */
	public Ret save(Calendar calendar) {
		if(calendar==null || isOk(calendar.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(calendar.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=calendar.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(calendar.getIautoid(), JBoltUserKit.getUserId(), calendar.getName());
		}
		return ret(success);
	}
	
	/**
	 * 更新
	 * @param calendar
	 * @return
	 */
	public Ret update(Calendar calendar) {
		if(calendar==null || notOk(calendar.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Calendar dbCalendar=findById(calendar.getIAutoId());
		if(dbCalendar==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(calendar.getName(), calendar.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=calendar.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(calendar.getIautoid(), JBoltUserKit.getUserId(), calendar.getName());
		}
		return ret(success);
	}
	
	/**
	 * 删除 指定多个ID
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		return deleteByIds(ids,true);
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		return deleteById(id,true);
	}
	
	/**
	 * 删除数据后执行的回调
	 * @param calendar 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Calendar calendar, Kv kv) {
		//addDeleteSystemLog(calendar.getIautoid(), JBoltUserKit.getUserId(),calendar.getName());
		return null;
	}
	
	/**
	 * 检测是否可以删除
	 * @param calendar 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(Calendar calendar, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(calendar, kv);
	}
	
	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}
	
	/**
	 * 切换icaluedartype属性
	 */
	public Ret toggleIcaluedartype(Long id) {
		return toggleBoolean(id, "iCaluedarType");
	}
	
	/**
	 * 检测是否可以toggle操作指定列
	 * @param calendar 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(Calendar calendar, String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}
	
	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Calendar calendar, String column, Kv kv) {
		//addUpdateSystemLog(calendar.getIautoid(), JBoltUserKit.getUserId(), calendar.getName(),"的字段["+column+"]值:"+calendar.get(column));
		return null;
	}
	
	/**
	 * 检测是否可以删除
	 * @param calendar model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(Calendar calendar, Kv kv) {
		//这里用来覆盖 检测Calendar是否被其它表引用
		return null;
	}
	
}
