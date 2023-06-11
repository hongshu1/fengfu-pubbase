package cn.rjtech.admin.subjectm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.util.JBoltListMap;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.subjectd.SubjectdService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.IsEnableEnum;
import cn.rjtech.model.momdata.Subjectd;
import cn.rjtech.model.momdata.Subjectm;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 科目对照主表 Service
 * @ClassName: SubjectmService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-01 13:53
 */
public class SubjectmService extends BaseService<Subjectm> {

	private final Subjectm dao = new Subjectm().dao();

	@Override
	protected Subjectm dao() {
		return dao;
	}

	@Inject
	private SubjectdService subjectdService;
	/**
	 * 后台管理分页查询
	 */
	public List<Record> paginateAdminDatas( Kv para) {
		if (para.getStr("senabled")!=null||para.getStr("keywords")!=null||para.getStr("cversion")!=null) {
			if (para.getStr("senabled")!=null) {
				para.set("isenabled",para.getStr("senabled"));
			}else {
				para.set("isenabled",null);
			}
		}
		List<Record> records = dbTemplate("subjectm.paginateAdminDatas", para).find();

		for (Record record : records) {
			//获取上级科目名称
			if (record.getLong("iparentid")!=null) {
				Subjectm parent = findById(record.getLong("iparentid"));
				if(parent!=null) record.set("parentname",parent.getCSubjectName());
			}
			//获取创建人名称
			String icreateby = JBoltUserCache.me.getUserName(record.getLong("icreateby"));
			record.set("icreateby",icreateby);
			//获取更新人名称
			if (record.getLong("iUpdateBy")!=null){
				String iupdateby = JBoltUserCache.me.getUserName(record.getLong("iUpdateBy"));
				record.set("iupdateby",iupdateby);

			}
			//获取u8科目名称
			record.set("ccodename", subjectdService.jointSubjectName(record.getLong("iautoid")));
			//获取u8科目编码
			record.set("ucode", subjectdService.jointSubjectCode(record.getLong("iautoid")));

		}
		return this.convertToRecordTree((List)records, "iautoid", "iparentid", (px) -> {
			return this.notOk(px.getStr("iparentid"));
		});
	}

	/**
	 * 保存
	 * @param subjectm
	 * @return
	 */
	public Ret save(Subjectm subjectm,Kv kv) {
		if(subjectm==null || isOk(subjectm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//如果新增数据版本和启用版本一直默认为启用状态
		List<Subjectm> subjectmList = find("select * from Bas_SubjectM where isenabled =1");
		if (CollUtil.isNotEmpty(subjectmList)) {
			if (subjectmList.get(0).getCVersion().equals(kv.getStr("cversion"))) {
				subjectm.setIsEnabled(true);
			}
		}

		//判断是否选择的是末级并且是否选择u8科目
		if (kv.getStr("ccodename")==null) {
			ValidationUtils.isTrue(!subjectm.getIsEnd(),"该科目已为末级科目请选择u8对应科目");
		}

		//判断是否存在上级
		if (subjectm.getStr("iparentid")!=null&&!" ".equals(kv.getStr("iparentid"))){

			Subjectm csubjectname = findById(subjectm.getStr("iparentid"));
			//根据上级id来获取最大等级
			Subjectm parentSubjectm = findById(subjectm.getIParentId());
			subjectm.setCLevel(parentSubjectm.getCLevel()+1);
			subjectm.setIParentId(csubjectname.getIAutoId());
			subjectm.setCVersion(csubjectname.getCVersion());
			subjectm.setIsEnabled(csubjectname.getIsEnabled());
		}
		//没有上级id的默认为1级
		if (subjectm.getIParentId()==null){
			subjectm.setCLevel(1);
		}
		//基础数据
		subjectm.setCOrgCode(getOrgCode());
		subjectm.setIOrgId(getOrgId());
		subjectm.setCreateTime(new Date());
		subjectm.setICreateBy(JBoltUserKit.getUserId());
		subjectm.setIsDelete(0);
		if (subjectm.getCVersion()==null) {
			subjectm.setCVersion(kv.getStr("cversion"));
		}
		tx(() -> {
			ValidationUtils.isTrue(subjectm.save(), ErrorMsg.SAVE_FAILED);
			//处理细表数据
			List<String> ccodes = StrSplitter.split(kv.getStr("ccode"), COMMA, true, true);
			List<String> ccodenames = StrSplitter.split(kv.getStr("ccodename"), COMMA, true, true);
			for (int i = 0; i < ccodenames.size(); i++) {
				Subjectd subjectd = new Subjectd();
				subjectd.setIsubjectmid(subjectm.getIAutoId());
				subjectd.setCsubjectname(ccodenames.get(i));
				subjectd.setCsubjectcode(ccodes.get(i));
				subjectd.setCreatetime(new Date());
				subjectd.setIcreateby(JBoltUserKit.getUserId());
				ValidationUtils.isTrue(subjectd.save(), ErrorMsg.SAVE_FAILED);
			}
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			// TODO 其他业务代码实现

			return true;
		});

		// 添加日志
		// addSaveSystemLog(subjectm.getIAutoId(), JBoltUserKit.getUserId(), subjectm.getName());
		return SUCCESS;
	}

	/**
	 * 更新
	 */
	public Ret update(Subjectm subjectm,Kv kv) {
		if(subjectm==null || notOk(subjectm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		if (subjectm.getIsEnd()) {
			List<Subjectm> byPid = findByPid(subjectm.getIAutoId());
			if (CollUtil.isNotEmpty(byPid)) {
				return fail("存在下级科目不可选择末级科目");
			}
		}
		//不是末级查询明细数据并删除
		//处理细表数据
		List<Subjectd> subjectdList = subjectdService.findBySubjectMId(subjectm.getIAutoId());
		for (Subjectd subjectd : subjectdList) {
			subjectd.delete();
		}

		//基础数据
		subjectm.setCOrgCode(getOrgCode());
		subjectm.setIOrgId(getOrgId());
		subjectm.setIUpdateBy(JBoltUserKit.getUserId());
		subjectm.setUpdateTime(new Date());
		if (kv.getStr("ccode")!=null){

			List<String> ccodes = StrSplitter.split(kv.getStr("ccode"), COMMA, true, true);
			List<String> ccodenames = StrSplitter.split(kv.getStr("ccodename"), COMMA, true, true);
			for (int i = 0; i < ccodenames.size(); i++) {
				Subjectd subjectd = new Subjectd();
				subjectd.setIsubjectmid(subjectm.getIAutoId());
				subjectd.setCsubjectname(ccodenames.get(i));
				subjectd.setCsubjectcode(ccodes.get(i));
				subjectd.setIcreateby(JBoltUserKit.getUserId());
				subjectd.setCreatetime(new Date());
				subjectd.setUpdatetime(new Date());
				subjectd.setIupdateby(JBoltUserKit.getUserId());
				ValidationUtils.isTrue(subjectd.save(), ErrorMsg.SAVE_FAILED);
			}
		}
		tx(() -> {
			// 更新时需要判断数据存在
			Subjectm dbSubjectm = findById(subjectm.getIAutoId());
			ValidationUtils.notNull(dbSubjectm, JBoltMsg.DATA_NOT_EXIST);
			// TODO 其他业务代码实现
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			ValidationUtils.isTrue(subjectm.update(), ErrorMsg.UPDATE_FAILED);

			return true;
		});

		//添加日志
		//addUpdateSystemLog(subjectm.getIAutoId(), JBoltUserKit.getUserId(), subjectm.getName());
		return SUCCESS;
	}

	/**
	 * 删除 指定多个ID
	 */
	public Ret deleteByBatchIds(String ids) {
		tx(() -> {
			for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
				long iAutoId = Long.parseLong(idStr);
				Subjectm dbSubjectm = findById(iAutoId);
				//ValidationUtils.isTrue(!dbSubjectm.getCVersion().equals(String.valueOf(DateUtil.year(new Date()))),"该数据版本还处于生效，不可删除！");
				ValidationUtils.notNull(dbSubjectm, JBoltMsg.DATA_NOT_EXIST);
				//删除对应下级科目数据
				List<Subjectm> list = recursion(dbSubjectm.getIAutoId(), new ArrayList<>());
				for (Subjectm subjectm : list) {
					if (subjectm!=null) {
						subjectm.setIsDelete(1).update();
					}
				}
				dbSubjectm.setIsDelete(1).update();
				// TODO 可能需要补充校验组织账套权限
				// TODO 存在关联使用时，校验是否仍在使用

			}
			return true;
		});

		// 添加日志
		// Subjectm subjectm = ret.getAs("data");
		// addDeleteSystemLog(iAutoId, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, subjectm.getName());
		return SUCCESS;
	}
	/**
	 * 递归数据
	 */
	public List<Subjectm>   recursion(Long pid,List<Subjectm> list){
		List<Subjectm> subjectms = findByPid(pid);
		if (CollUtil.isNotEmpty(subjectms)){
			for (Subjectm subjectm : subjectms) {
				list.add(subjectm);
				recursion(subjectm.getIAutoId(),list);
			}
		} return list;
	}


	/**
	 *根据id获取对应下级数据
	 */
	public List<Subjectm> findByPid(Long pid){
		return find("select * from Bas_SubjectM where iParentId=?", pid);
	}
	/**
	 * 删除数据后执行的回调
	 * @param subjectm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(Subjectm subjectm, Kv kv) {
		//addDeleteSystemLog(subjectm.getIAutoId(), JBoltUserKit.getUserId(),subjectm.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param subjectm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanDelete(Subjectm subjectm, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(subjectm, kv);
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
	 * 切换isend属性
	 */
	public Ret toggleIsend(Long id) {
		return toggleBoolean(id, "isEnd");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param subjectm 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(Subjectm subjectm,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Subjectm subjectm, String column, Kv kv) {
		//addUpdateSystemLog(subjectm.getIAutoId(), JBoltUserKit.getUserId(), subjectm.getName(),"的字段["+column+"]值:"+subjectm.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param subjectm model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkInUse(Subjectm subjectm, Kv kv) {
		//这里用来覆盖 检测Subjectm是否被其它表引用
		return null;
	}

	public List<Subjectm> getAllSubjectmsOptionsWithLevel() {
		List<Subjectm> subjectms = this.find(this.selectSql().select(new String[]{"iautoid,csubjectname,csubjectcode,iparentid,clevel"}).orderBySortRank("clevel"));
		List<Subjectm> parents = new ArrayList();
		this.processParentSubjectm(subjectms, parents);
		if (parents.size() > 0 && subjectms.size() > 0) {
			this.processSubjectmItems(subjectms, parents);
		}

		return parents;
	}

	private void processParentSubjectm(List<Subjectm> subjectms, List<Subjectm> parents) {
		for(int i = 0; i < subjectms.size(); ++i) {
			Subjectm subjectm = (Subjectm)subjectms.get(i);
			if ("1".equals(subjectm.getCLevel()) && this.notOk(subjectm.getIParentId())) {
				parents.add(subjectm);
				subjectms.remove(i);
				--i;
			}
		}
	}

	private void processSubjectmItems(List<Subjectm> subjectms, List<Subjectm> parents) {
		JBoltListMap<String, Subjectm> map = new JBoltListMap();
		Iterator var4 = subjectms.iterator();

		Subjectm p;
		while(var4.hasNext()) {
			p = (Subjectm)var4.next();
			map.addItem("p_" + p.getIParentId(), p);
		}

		var4 = parents.iterator();

		while(var4.hasNext()) {
			p = (Subjectm)var4.next();
			this.processSubItems(map, p);
		}
	}
	private void processSubItems(JBoltListMap<String, Subjectm> map, Subjectm subjectm) {
		List<Subjectm> items = (List)map.get("p_" + subjectm.getIAutoId());
		if (items != null && items.size() > 0) {
			Iterator var4 = items.iterator();

			while(var4.hasNext()) {
				Subjectm item = (Subjectm)var4.next();
				this.processSubItems(map, item);
			}
		}

		subjectm.putItems(items);
	}

	/**
	 * 根据科目编码获取数据
	 */
	public Subjectm findBySubjectCode(String subjectcode){
		return 	findFirst("select * from Bas_SubjectM where cSubjectCode=?",subjectcode);
	}

	/**
	 * 根据科目名称获取数据
	 */
	public Subjectm findBySubjectName(String subjectname){
		return 	findFirst("select * from Bas_SubjectM where cSubjectName=?",subjectname);
	}

	/**
	 * U8科目列表
	 * @return
	 */
	public Page<Record> u8MajorSubjectsList(int pageNumber, int pageSize,Kv para) {
		//获取U8科目编码规则
		Record first = dbTemplate(u8SourceConfigName(), "subjectm.u8getcoding", para).findFirst();
		//String codingrule = first.getStr("codingrule").substring(0, 1);
		//para.set("codingrule",codingrule);
		return dbTemplate(u8SourceConfigName(), "subjectm.u8MajorSubjectsList", para).paginate(pageNumber, pageSize);
	}

	public List<Record> getAutocompleteList(String keyword, Integer limit) {
		return dbTemplate("subjectm.getAutocompleteList",Kv.by("keyword", keyword).set("limit",limit)
				.set("iorgid",getOrgId()).set("isend",IsEnableEnum.YES.getValue())).find();
	}

	public Subjectm findByName(String name) {
		return findFirst(selectSql().eq("csubjectname", name).eq("isenabled", "1"));
	}

	public List<Record> highestsubjectAutocomplete(String keyword, Integer limit) {
		return dbTemplate("subjectm.highestsubjectAutocomplete",Kv.by("keyword", keyword).set("limit",limit)
				.set("iorgid",getOrgId())).find();
	}
	public List<Record> lowestsubjectAutocomplete(String keyword, Integer limit,Long ihighestsubjectid) {
		return dbTemplate("subjectm.lowestsubjectAutocomplete",Kv.by("keyword", keyword).set("limit",limit)
				.set("iorgid",getOrgId()).set("ihighestsubjectid",ihighestsubjectid)).find();
	}

	/**
	 *版本启用
	 */
	public Ret versionEnable(Kv kv) {
		if (kv.getStr("cversion")==null&&notOk(kv.getStr("cversion"))) {
			return  fail("版本查询框不可为空，请输入版本号！！");
		}
		List<Record> list = dbTemplate("subjectm.paginateAdminDatas", kv).find();
		if (kv.getInt("senabled")==1){
			List<Subjectm> subjectmList = find("select * from Bas_SubjectM where isenabled =1");
			if (subjectmList.size()>0){
				ValidationUtils.error("已启用："+subjectmList.get(0).getCVersion()+",请先停用在启用！");
			}
		}
		List<Subjectm> subjectms = new ArrayList<>();
		for (Record record : list) {
			Subjectm subjectm = findById(record.get("iautoid"));
			subjectm.setIsEnabled("1".equals(kv.getStr("senabled")));
			subjectms.add(subjectm);
		}
		tx(() -> {
			batchUpdate(subjectms);
			return true;
		});
		return SUCCESS;
	}

	/**
	 *复制对应版本数据
	 */
	public Ret copyDatas(Kv kv) {
		kv.set("copycondition","1");
		String str = dbTemplate("subjectm.maxCversion", kv).findFirst().getStr("Cversion");
		kv.set("cversion",str);
		//获取一级数据
		List<Record> list = dbTemplate("subjectm.paginateAdminDatas", kv).find();
		//获取历史数据
		List<Record> records = dbTemplate("subjectm.getMaxDatas", kv).find();
		ArrayList<Subjectm> subjectms1 = new ArrayList<>();
		for (Record record : records) {
			Subjectm subjectm = findById(record.getStr("iautoid"));
			subjectm.setIsEnabled(false);
			subjectms1.add(subjectm);
		}
		List<Subjectm> subjectms = new ArrayList<>();
		List<Subjectd> subjectds = new ArrayList<>();

		tx(() -> {
			//获取对应版本数据
			for (Record record : list) {
				//根据id获取数据
				Subjectm subjectm = findById(record.getStr("iautoid"));
				//获取所有下级数据
				List<Subjectm> recursion = recursion(subjectm.getIAutoId(), new ArrayList<>());
				//根据id获取细表数据
				List<Subjectd> subjectdList = subjectdService.findBySubjectMId(subjectm.getIAutoId());

				subjectm.setIAutoId(JBoltSnowflakeKit.me.nextId());
				//基础数据
				subjectm.setCOrgCode(getOrgCode());
				subjectm.setIOrgId(getOrgId());
				subjectm.setCreateTime(new Date());
				subjectm.setICreateBy(JBoltUserKit.getUserId());
				subjectm.setIsEnabled(true);
				subjectm.setCVersion(String.valueOf(Integer.parseInt(str)+1));

				//处理明细数据
				for (Subjectd subjectd : subjectdList) {
					subjectd.setIsubjectmid(subjectm.getIAutoId());
					subjectd.setIautoid(JBoltSnowflakeKit.me.nextId());
					//基础数据
					subjectd.setCreatetime(new Date());
					subjectd.setIcreateby(JBoltUserKit.getUserId());
					subjectds.add(subjectd);
				}

				//处理下级数据
				//存储旧id
				String id="";
				for (int i = 0; i < recursion.size(); i++) {
					Subjectm sm = recursion.get(i);

					if (sm.getIParentId().equals(record.getLong("iautoid"))) {
						//根据id获取细表数据
						List<Subjectd> dList = subjectdService.findBySubjectMId(sm.getIAutoId());

						id= String.valueOf(sm.getIAutoId());
						sm.setIAutoId(JBoltSnowflakeKit.me.nextId());
						sm.setIParentId(subjectm.getIAutoId());
						sm.setCVersion(subjectm.getCVersion());
						sm.setCOrgCode(getOrgCode());
						sm.setIOrgId(getOrgId());
						sm.setCreateTime(new Date());
						sm.setIsEnabled(true);
						sm.setICreateBy(JBoltUserKit.getUserId());
						//根据id获取细表数据
						for (Subjectd subjectd : dList) {
							subjectd.setIsubjectmid(sm.getIAutoId());
							subjectd.setIautoid(JBoltSnowflakeKit.me.nextId());
							//基础数据
							subjectd.setCreatetime(new Date());
							subjectd.setIcreateby(JBoltUserKit.getUserId());
							subjectds.add(subjectd);
						}
						subjectms.add(sm);
					}else if (sm.getIParentId().equals(Long.parseLong(id))){
						//根据id获取细表数据
						List<Subjectd> dList = subjectdService.findBySubjectMId(sm.getIAutoId());
							id= String.valueOf(sm.getIAutoId());
							sm.setIParentId(recursion.get(i-1).getIAutoId());
							sm.setIAutoId(JBoltSnowflakeKit.me.nextId());
							sm.setCVersion(recursion.get(i-1).getCVersion());
							sm.setCOrgCode(getOrgCode());
							sm.setIOrgId(getOrgId());
							sm.setCreateTime(new Date());
							sm.setIsEnabled(true);
							sm.setICreateBy(JBoltUserKit.getUserId());
						//根据id获取细表数据
						for (Subjectd subjectd : dList) {
							subjectd.setIsubjectmid(sm.getIAutoId());
							subjectd.setIautoid(JBoltSnowflakeKit.me.nextId());
							//基础数据
							subjectd.setCreatetime(new Date());
							subjectd.setIcreateby(JBoltUserKit.getUserId());
							subjectds.add(subjectd);
						}
							subjectms.add(sm);
					}
				}
				ValidationUtils.isTrue(subjectm.save(), ErrorMsg.SAVE_FAILED);
			}
			batchSave(subjectms,500);
			subjectdService.batchSave(subjectds,500);
			batchUpdate(subjectms1,500);
			return true;
		});
		return SUCCESS;
	}
	public String getName(Long iautoid) {
        if(iautoid == null) return null;
        return queryColumn("SELECT csubjectname FROM Bas_Subjectm WHERE iautoid = ? ", iautoid);
    }
	/**
	 * 根据明细科目名称查询科目大类
	 * */
	public Subjectm findHighestSubjectByLowestSubjectName(String cLowestSubjectName) {
		Subjectm lSubjectm = findByName(cLowestSubjectName);
		if(lSubjectm == null) return null;
		Record rc = dbTemplate("subjectm.getHighestSubject",Kv.by("ilowestsubjectid", lSubjectm.getIAutoId())).findFirst();
		Long ihighestsubjectid = rc.getLong("ihighestsubjectid");
		if(ihighestsubjectid!=null) return findById(ihighestsubjectid);
		return null;
	}

 
	public List<Record> u8SubjectAutocomplete(String keyword, Integer limit) {
		return dbTemplate(u8SourceConfigName(),"subjectm.u8SubjectAutocomplete",Kv.by("keywords", keyword).set("limit",limit)).find();
	}
}