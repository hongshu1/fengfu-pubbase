package cn.rjtech.admin.locListCN;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;

import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.LocListCN;
/**
 * 地区 Service
 * @ClassName: LocListCNService
 * @author: RJ
 * @date: 2023-03-27 11:03
 */
public class LocListCNService extends BaseService<LocListCN> {

	private final LocListCN dao = new LocListCN().dao();

	@Override
	protected LocListCN dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<LocListCN> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param LocListCN
	 * @return
	 */
	public Ret save(LocListCN LocListCN) {
		if(LocListCN==null || isOk(LocListCN.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(LocListCN.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=LocListCN.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(LocListCN.getIautoid(), JBoltUserKit.getUserId(), LocListCN.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param LocListCN
	 * @return
	 */
	public Ret update(LocListCN LocListCN) {
		if(LocListCN==null || notOk(LocListCN.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		LocListCN dbLocListCN=findById(LocListCN.getIAutoId());
		if(dbLocListCN==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(LocListCN.getName(), LocListCN.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=LocListCN.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(LocListCN.getIautoid(), JBoltUserKit.getUserId(), LocListCN.getName());
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
	 * @param LocListCN 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(LocListCN LocListCN, Kv kv) {
		//addDeleteSystemLog(LocListCN.getIautoid(), JBoltUserKit.getUserId(),LocListCN.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param LocListCN 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(LocListCN LocListCN, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(LocListCN, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	public List<LocListCN> findByIPid(long ipid){
		return find("SELECT * FROM [dbo].[Bd_LocListCN] WHERE iPId = ?", ipid);
	}

	public List<LocListCN> findByNameChild(String name){
		return find("SELECT * FROM [dbo].[Bd_LocListCN] WHERE iPId IN( "+(isOk(name)?"(SELECT iAutoId FROM Bd_LocListCN WHERE Name ='"+name+"')":"0")+")");
	}

	//QQ位置文件解析，保存
	public Ret QQI18NLocListSave()
	{
		String json = "D:/";
		File jsonFile = new File(json);
		//通过上面那个方法获取json文件的内容
		String jsonData = getJsonStr(jsonFile);
		//转json对象
		JSONObject parse = (JSONObject) JSONObject.parse(jsonData);
		JSONObject location = parse.getJSONObject("Location");

		//获取主要数据
		JSONArray countryregion = location.getJSONArray("CountryRegion");

		for(Object object : countryregion){
			//第一级 国家
			JSONObject jsonObject = (JSONObject)object;

			LocListCN LocListCN = new LocListCN();
			LocListCN.setIAutoId(JBoltSnowflakeKit.me.nextId());
			LocListCN.setILevel(1);
			LocListCN.setIPid(0L);
			LocListCN.setName(jsonObject.getString("Name"));
			if(LocListCN.getName()!=null){
				LocListCN.save();
			}
			System.out.println(LocListCN.getName());

			//第二级 省/自治区
			JSONArray state = null;
			if(jsonObject.get("State") instanceof  JSONArray){
				state = jsonObject.getJSONArray("State");
			}else if(jsonObject.get("State") instanceof  JSONObject){
				state = new JSONArray();
				state.add(jsonObject.get("State"));
			}
			if(state!=null){
				for(Object object2 : state){
					JSONObject jsonObject2 = (JSONObject)object2;
					LocListCN LocListCN2 = new LocListCN();
					LocListCN2.setIAutoId(JBoltSnowflakeKit.me.nextId());
					LocListCN2.setIPid(LocListCN.getIAutoId());
					LocListCN2.setILevel(2);
					LocListCN2.setName(jsonObject2.getString("Name"));
					if(LocListCN2.getName()!=null){
						LocListCN2.save();
					}
					System.out.println(LocListCN2.getName());

					JSONArray city = null;
					//第三级 城市
					if(jsonObject2.get("City") instanceof  JSONArray){
						city = jsonObject2.getJSONArray("City");
					}else if(jsonObject2.get("City") instanceof JSONObject){
						city = new JSONArray();
						city.add(jsonObject2.get("City"));
					}

					if(city!=null){
						for(Object object3 : city){
							JSONObject jsonObject3 = (JSONObject)object3;
							LocListCN LocListCN3 = new LocListCN();
							LocListCN3.setIAutoId(JBoltSnowflakeKit.me.nextId());
							if(isOk(LocListCN2.getName())){
								LocListCN3.setIPid(LocListCN2.getIAutoId());
							}else{
								LocListCN3.setIPid(LocListCN.getIAutoId());
							}
							LocListCN3.setILevel(3);
							LocListCN3.setName(jsonObject3.getString("Name"));
							if(LocListCN3.getName()!=null){
								LocListCN3.save();
							}
							System.out.println(LocListCN3.getName());

							//第四级 区县
							JSONArray region = null;
							if(jsonObject3.get("Region") instanceof JSONArray){
								region = jsonObject3.getJSONArray("Region");
							}else if(jsonObject3.get("Region") instanceof JSONObject){
								region = new JSONArray();
								region.add(jsonObject3.get("Region"));
							}
							if(region!=null){
								for(Object object4 : region){
									JSONObject jsonObject4 = (JSONObject)object4;
									LocListCN LocListCN4 = new LocListCN();
									LocListCN4.setIAutoId(JBoltSnowflakeKit.me.nextId());
									if(isOk(LocListCN3.getName())){
										LocListCN4.setIPid(LocListCN3.getIAutoId());
									}else{
										LocListCN4.setIPid(LocListCN2.getIAutoId());
									}

									LocListCN4.setILevel(4);
									LocListCN4.setName(jsonObject4.getString("Name"));
									if(LocListCN4.getName()!=null){
										LocListCN4.save();
									}
									System.out.println(LocListCN4.getName());
								}
							}
						}
					}

				}
			}

		}
		return SUCCESS;
	}

	public static String getJsonStr(File jsonFile){
		String jsonStr = "";
		try {
			FileReader fileReader = new FileReader(jsonFile);
			Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
			int ch = 0;
			StringBuffer sb = new StringBuffer();
			while ((ch = reader.read()) != -1) {
				sb.append((char) ch);
			}
			fileReader.close();
			reader.close();
			jsonStr = sb.toString();
			return jsonStr;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}