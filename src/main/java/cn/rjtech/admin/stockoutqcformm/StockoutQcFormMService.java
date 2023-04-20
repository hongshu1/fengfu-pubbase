package cn.rjtech.admin.stockoutqcformm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.jfinal.aop.Inject;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Page;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.core.para.JBoltPara;
import cn.jbolt.core.util.JBoltRealUrlUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.model.momdata.StockoutQcFormM;
/**
 * 质量管理-出库检
 * @ClassName: StockoutQcFormMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-12 19:18
 */
public class StockoutQcFormMService extends BaseService<StockoutQcFormM> {
	private final StockoutQcFormM dao=new StockoutQcFormM().dao();

	@Inject
	private RcvDocQcFormMService rcvDocQcFormMService;

	@Override
	protected StockoutQcFormM dao() {
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
	 * @return
	 */
	public Page<StockoutQcFormM> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("id");
		return paginate(sql);
	}

	public Page<Record> pageList(Kv kv) {
		return dbTemplate("stockoutqcformm.list", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
	}

	/**
	 * 保存
	 * @param stockoutQcFormM
	 * @return
	 */
	public Ret save(StockoutQcFormM stockoutQcFormM) {
		if(stockoutQcFormM==null || isOk(stockoutQcFormM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(stockoutQcFormM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockoutQcFormM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(stockoutQcFormM.getId(), JBoltUserKit.getUserId(), stockoutQcFormM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param stockoutQcFormM
	 * @return
	 */
	public Ret update(StockoutQcFormM stockoutQcFormM) {
		if(stockoutQcFormM==null || notOk(stockoutQcFormM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		StockoutQcFormM dbStockoutQcFormM=findById(stockoutQcFormM.getIAutoId());
		if(dbStockoutQcFormM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(stockoutQcFormM.getName(), stockoutQcFormM.getId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockoutQcFormM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(stockoutQcFormM.getId(), JBoltUserKit.getUserId(), stockoutQcFormM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param stockoutQcFormM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(StockoutQcFormM stockoutQcFormM, Kv kv) {
		//addDeleteSystemLog(stockoutQcFormM.getId(), JBoltUserKit.getUserId(),stockoutQcFormM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param stockoutQcFormM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(StockoutQcFormM stockoutQcFormM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * 上传图片
	 */
	public List<String> uploadImage(List<UploadFile> files) {
		List<String> imgList = new ArrayList<>();
		if (ObjectUtil.isEmpty(files)) {
			return imgList;
		}
		for (UploadFile uploadFile : files) {
			String localUrl = FileUtil.normalize(JBoltRealUrlUtil.get(JFinal.me().getConstants().getBaseUploadPath() + '/'
				+ uploadFile.getUploadPath() + '/' + uploadFile.getFileName()));
			imgList.add(localUrl);
		}
		return imgList;
	}

	/*
	 * 更新
	 * */
	public Ret updateEditTable(JBoltPara JboltPara) {
		if (JboltPara == null || JboltPara.isEmpty()) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		String cmeasurereason = JboltPara.getString("cmeasurereason"); //测定理由
		String cmeasurepurpose = JboltPara.getString("cmeasurepurpose"); //测定目的
		String cmeasureunit = JboltPara.getString("cmeasureunit"); //测定单位
		String cmemo = JboltPara.getString("cmemo"); //备注
		String isok = JboltPara.getString("isok"); //是否合格
		String cdcno = JboltPara.getString("cdcno"); //设变号
		JSONObject tableDataList = JboltPara.getJSONObject("tableDataList");
		//tableDataList转为map
		Map<String, Object> innerMap = tableDataList.getInnerMap();
		//对innerMap排序
		Map<String, Object> sortMap = rcvDocQcFormMService.sortMapByKey(innerMap);
		sortMap.forEach((key, value) -> {
			System.out.println(key);
			String s = value.toString();
			List<String> list = rcvDocQcFormMService.oobjectToList(value, String.class)
				.stream()
				.filter(e -> StringUtils.isNotBlank(e))
				.collect(Collectors.toList());
			System.out.println("list==>" + new Gson().toJson(list));
			//1、将table数据保存在检验表

		});
		//2、将输入框的数据保存在【来料检表（PL_RcvDocQcFormM）】
//        RcvDocQcFormM docQcFormM = findById(JboltPara.getString("iautoid"));
//        saveDocQcFormMModel(docQcFormM,JboltPara);

		return ret(true);
	}

}