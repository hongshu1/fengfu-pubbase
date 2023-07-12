package cn.rjtech.common.columsmapdetail;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.common.model.Columsmapdetail;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字段映射 Service
 * @ClassName: ColumsmapdetailService
 * @author: lidehui
 * @date: 2023-02-01 11:09
 */
public class ColumsmapdetailService extends BaseService<Columsmapdetail> {

	private final Columsmapdetail dao = new Columsmapdetail().dao();

	@Override
	protected Columsmapdetail dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<Columsmapdetail> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("AutoId","DESC", pageNumber, pageSize, keywords, "AutoId");
	}

	/**
	 * 保存
	 * @param columsmapdetail
	 * @return
	 */
	public Ret save(Columsmapdetail columsmapdetail) {
		if(columsmapdetail==null || isOk(columsmapdetail.getAutoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(columsmapdetail.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=columsmapdetail.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(columsmapdetail.getAutoid(), JBoltUserKit.getUserId(), columsmapdetail.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param columsmapdetail
	 * @return
	 */
	public Ret update(Columsmapdetail columsmapdetail) {
		if(columsmapdetail==null || notOk(columsmapdetail.getAutoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Columsmapdetail dbColumsmapdetail=findById(columsmapdetail.getAutoid());
		if(dbColumsmapdetail==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(columsmapdetail.getName(), columsmapdetail.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=columsmapdetail.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(columsmapdetail.getAutoid(), JBoltUserKit.getUserId(), columsmapdetail.getName());
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
	 * @param columsmapdetail 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Columsmapdetail columsmapdetail, Kv kv) {
		//addDeleteSystemLog(columsmapdetail.getAutoid(), JBoltUserKit.getUserId(),columsmapdetail.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param columsmapdetail 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(Columsmapdetail columsmapdetail, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(columsmapdetail, kv);
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
	 * 根据主项目ID查询要转换的字段
	 *
	 * @param masId 主项目ID
	 * @return 要转换的字段list
	 */
	public Map<List<String>, List<Record>> getColumsmapdetaiList(String masid) throws RuntimeException{
		Kv kv = Kv.by("masid", masid);
		List<Columsmapdetail> masIdList = dbTemplate("columsmapdetail.getMasIdList", kv).query();
		Map<List<String>,List<Record>> map = new HashMap<>();
		for (Columsmapdetail columsmapdetail : masIdList){
			List key = new ArrayList();
			key.add(columsmapdetail.getFlag());
			key.add(columsmapdetail.getFlagtype());
			map.put(key, getColumsmapdetailList(masid,columsmapdetail.getFlag()));
		}
		return map;
	}

	/**
	 * 根据主项目ID查询要转换的字段
	 *
	 * @param masId 主项目ID
	 * @param flag  标识
	 * @return 要转换的字段list
	 * @field
	 */
	public List<Record> getColumsmapdetailList(String masid, String flag) {
		Kv kv = Kv.by("masid", masid);
		kv.set("flag", flag);
		List<Record> masIdList = dbTemplate("columsmapdetail.getMasIdFlagList", kv).find();
		return masIdList;
	}

	public List<Record> theFirstLayerOfData(String masid) {
		Kv kv = Kv.by("masid", masid);
		List<Record> masIdList = dbTemplate("columsmapdetail.theFirstLayerOfData", kv).find();
		return masIdList;
	}

	public List<Record> theFirstLayerOfDataExtend(String masid){
		Kv kv = Kv.by("masid", masid);
		List<Record> masIdList = dbTemplate("columsmapdetail.theFirstLayerOfDataExtend", kv).find();
		return masIdList;
	}

	/**
	 * 根据父标识和主项目ID查询数据
	 *
	 * @param masId 主项目ID
	 * @param flag  父标识
	 * @return
	 */
	public List<Record> queryLowerLevelData(String masid, String flag){
		Kv kv = Kv.by("masid", masid).set("flag", flag).set("pflag", flag);
		List<Record> masIdList = dbTemplate("columsmapdetail.queryLowerLevelData", kv).find();
		return masIdList;
	}

	/**
	 * 根据日志vouchType查询数据配置信息
	 * @param vouchType
	 * @return
	 */
	public List<Columsmapdetail> getLogVouchTypeColumsMapDetailList(String vouchType){
		Kv kv = Kv.by("vouchType", vouchType);
		List<Columsmapdetail> masIdList = dbTemplate("columsmapdetail.getLogVouchTypeColumsMapDetailList", kv).query();
		return masIdList;
	}

	public String getVouchProcessRuslte(String sourceType){
		Kv kv = Kv.by("sourceType", sourceType);
		return dbTemplate("columsmapdetail.getVouchProcessRuslte", kv).queryStr();
	}


}