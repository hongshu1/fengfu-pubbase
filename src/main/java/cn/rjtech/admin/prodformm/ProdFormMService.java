package cn.rjtech.admin.prodformm;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.rjtech.admin.prodformparam.ProdFormParamService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.ProdFormM;
import com.jfinal.plugin.activerecord.Record;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 制造管理-生产表单主表
 * @ClassName: ProdFormMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-28 10:04
 */
public class ProdFormMService extends BaseService<ProdFormM> {
	private final ProdFormM dao=new ProdFormM().dao();
	@Override
	protected ProdFormM dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }
	@Inject
	private ProdFormParamService prodFormParamService;
	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
     * @param iProdFormId 生产表格ID
	 * @return
	 */
	public Page<ProdFormM> getAdminDatas(int pageNumber, int pageSize, Long iProdFormId) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("iProdFormId",iProdFormId);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param prodFormM
	 * @return
	 */
	public Ret save(ProdFormM prodFormM) {
		if(prodFormM==null || isOk(prodFormM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=prodFormM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(prodFormM.getIAutoId(), JBoltUserKit.getUserId(), prodFormM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param prodFormM
	 * @return
	 */
	public Ret update(ProdFormM prodFormM) {
		if(prodFormM==null || notOk(prodFormM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ProdFormM dbProdFormM=findById(prodFormM.getIAutoId());
		if(dbProdFormM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=prodFormM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(prodFormM.getIAutoId(), JBoltUserKit.getUserId(), prodFormM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param prodFormM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ProdFormM prodFormM, Kv kv) {
		//addDeleteSystemLog(prodFormM.getIAutoId(), JBoltUserKit.getUserId(),prodFormM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param prodFormM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(ProdFormM prodFormM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
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
		if (ObjUtil.isNotNull(byIdGetDetail)){

			for (int i=0; i<byIdGetDetail.size(); i++){

				Record item = byIdGetDetail.get(i);
				String qcItemId = item.getStr("iseq");
				if (!qcItemId.equals(iseq)) {
					iseq=qcItemId;
					boolean flag = false;
					List<Map<String, Object>> itemParamList = new ArrayList<>();
					for (Record object :byIdGetDetail){
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