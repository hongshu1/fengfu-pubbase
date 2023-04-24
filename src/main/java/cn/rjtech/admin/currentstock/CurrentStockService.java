package cn.rjtech.admin.currentstock;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseU8RecordService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.StockCheckVouch;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

/**
 * 盘点单Service
 * @ClassName: CurrentStockService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2021-11-12 16:01
 */
public class CurrentStockService extends BaseU8RecordService {



	public Page<Record> datas(Integer pageNumber, Integer pageSize, Kv kv) {
		kv.set("organizecode",getOrgCode());
		Page<Record> paginate = dbTemplate("currentstock.datas", kv).paginate(pageNumber, pageSize);
		return paginate;
	}

	/**
	 * 盘点单物料清单列表
	 * */
	public Page<Record> invDatas(Integer pageNumber, Integer pageSize, Kv kv) {
		kv.set("organizecode",getOrgCode());
		String whcode = kv.getStr("whcode");

		Page<Record> paginate = dbTemplate("currentstock.invDatas", kv).paginate(pageNumber, pageSize);
		return paginate;
	}





	/**仓库*/
	public List<Record> autocompleteWareHouse(Kv kv) {
		return dbTemplate("currentstock.autocompleteWareHouse", kv.set("organizecode", getOrgCode())).find();
	}
	/**库位*/
	public List<Record> autocompletePosition(Kv kv) {
		return dbTemplate("currentstock.autocompletePosition", kv.set("organizecode", getOrgCode())).find();
	}
	/**盘点人*/
	public List<Record> autocompleteUser(Kv kv) {
		return dbTemplate("currentstock.autocompleteUser",kv).find();
	}


	public Ret SaveStockchekvouch(Kv kv){
		//主表
		StockCheckVouch stockcheckvouch = new StockCheckVouch();
		//构造数据
		Date date = new Date();
		//创建时间
		stockcheckvouch.setCreateDate(date);
		String userName = JBoltUserKit.getUserName();
		//创建人
		stockcheckvouch.setCreatePerson(userName);
		String orgCode = getOrgCode();
		//组织编码
		stockcheckvouch.setOrganizeCode(orgCode);
		String code = BillNoUtils.genCurrentNo();
		//单号
		stockcheckvouch.setBillNo(code);
		//获取数据
		String whcode = kv.getStr("whcode");
		//盘点仓库
		stockcheckvouch.setWhCode(whcode);
		//盘点人
		stockcheckvouch.setCheckPerson(kv.getStr("id"));
		stockcheckvouch.setCheckType(kv.getStr("isenabled"));
		kv.getStr("poscode");

		boolean save = stockcheckvouch.save();
		return ret(save);
	}

	/**
	 * 保存
	 */
	public Ret save(StockCheckVouch stockchekvouch) {
		if (stockchekvouch == null || isOk(stockchekvouch.getAutoid())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			stockchekvouch.setOrganizeCode(getOrgCode());
			stockchekvouch.setBillNo(BillNoUtils.genCurrentNo());
			stockchekvouch.setCreateDate(new Date());
			ValidationUtils.isTrue(stockchekvouch.save(), ErrorMsg.SAVE_FAILED);


			// TODO 其他业务代码实现

			return true;
		});
		return SUCCESS;
	}
	public Ret saveSubmit(Kv kv){
		String datas = kv.getStr("datas");
		String barcode = kv.getStr("barcode");
		List<Kv> datasList = JSON.parseArray(datas, Kv.class);
		List<Kv> barcodeList = JSON.parseArray(barcode, Kv.class);
		return SUCCESS;

	}
	@Override
	protected String getTableName() {
		return null;
	}

	@Override
	protected String getPrimaryKey() {
		return null;
	}
}