package cn.rjtech.admin.barcodereport.billnobarcodetracepage;

import cn.jbolt.core.util.JBoltRandomUtil;
import cn.rjtech.base.service.view.BaseU9ViewService;
import cn.rjtech.constants.DataSourceConstants;
import cn.rjtech.wms.utils.StringUtils;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * 条码汇总
 * @author Kephon
 */
public class BillNoBarcodeTracePageAdminService extends BaseU9ViewService {

//    @Inject
//    JBoltDictionaryService jBoltDictionaryService;


    public Page<Record> newdatas(int pageSize, int pageNumber, Kv para){

        Page<Record> page = barcodeTotalDatas(pageSize, pageNumber, para);
        return page;
    }

    /**
     * 条码汇总表新版数据源
     */
    public Page<Record> barcodeTotalDatas(int pageSize, int pageNumber, Kv kv){
        List<Record> list = getBarcodeTotalList(DataSourceConstants.U8,kv);

        //查看类型
        HashSet<String> set=new HashSet<>();

        for (Record record : list) {
            String stockclassname = record.getStr("stockclassname");
            set.add(stockclassname);
        }


        long totalRow;
        totalRow=list.size();
        int totalPage = (int) (totalRow / pageSize);
        if (totalRow % pageSize != 0) {
            totalPage++;
        }
        List<Record> recordArrayList = new ArrayList<>();
        //截取的开始位置
        int pageStart=pageNumber==1?0:(pageNumber-1)*pageSize;
        int pageEnd= Math.min((int) totalRow, pageNumber * pageSize);
        if(totalRow>pageStart){
            recordArrayList =list.subList(pageStart, pageEnd);
        }
        return  new Page<>(recordArrayList, pageNumber, pageSize, totalPage, (int) totalRow);
    }

        /**
         *条码汇总表构建临时表 ,并执行存储过程
         * */
        public List<Record> getBarcodeTotalList(String dataSourceConfigName,Kv para) {
            String tempTableName = buildTempdb(dataSourceConfigName,para);

            List<Record> list = excuteBarcodeTracePage(dataSourceConfigName, tempTableName);

            return list;
        }

    /**
     * 条码汇总表构建临时表
     * */
    public String buildTempdb(String dataSourceConfigName,Kv para){
        //条码汇总表将参数转成临时表参数
        List<Record> records = paraToListRecord(para);


        // 随机ID
        String randomId = JBoltRandomUtil.randomNumber(10);
        // 临时表名
        String tempTableName = "Tempdb.dbo.temp_params_"+randomId+"";

        // 创建临时表
        updateWithDataSource(dataSourceConfigName, String.format("CREATE TABLE %s(" +
                "    ParmsKey nvarchar(50) NOT NULL," +
                "    ParmsValue nvarchar(2000) NOT NULL" +
                ")", tempTableName));

        // 保存
        Db.use(dataSourceConfigName).batchSave(tempTableName, records, Math.min(50, records.size()));
        return tempTableName;
    }


    /**
     * 条码汇总表将参数转成临时表参数
     * */
    public List<Record> paraToListRecord(Kv para) {
        Map<String,Object> paramMap=new HashMap();
        paramMap.putAll(para);
        // 插入数据
        List<Record> records = new ArrayList<>();

        for (Map.Entry<String, Object> r : paramMap.entrySet()) {
            Record record = new Record();
            String parmsKey="";
            String parmsValue="";

            String key = r.getKey();
            Object rValue = r.getValue();

            if(rValue!=null){
                if(key.equals("sqlids")){
                    parmsKey="and a.barcode in ";
                    parmsValue="("+rValue+")";
                }
                //开始时间
                if(key.equals("starttime")){
                    parmsKey="and a.CreateDate > ";
                    parmsValue="'"+rValue+"'";
                }

                //结束时间
                if(key.equals("endtime")){
                    parmsKey="and a.CreateDate < ";
                    parmsValue="'"+rValue+"'";
                }
                //单据号
                if(key.equals("billno")){
                    parmsKey="and a.BillNo like ";
                    parmsValue="'%"+rValue+"%'";
                } //供应商
                if(key.equals("vencode")){
                    parmsKey="and c.vencode like ";
                    parmsValue="'%"+rValue+"%'";
                } //'客户名称'
                if(key.equals("cusname")){
                    parmsKey="and c.cusname like ";
                    parmsValue="'%"+rValue+"%'";
                }//存货编码
                if(key.equals("invcode")){
                    parmsKey="and i.invcode like ";
                    parmsValue="'%"+rValue+"%'";
                }//客户部番
                if(key.equals("otherinvcode")){
                    parmsKey="and i.otherinvcode like ";
                    parmsValue="'%"+rValue+"%'";
                }//部品名称
                if(key.equals("otherinvname")){
                    parmsKey="and i.otherinvname like ";
                    parmsValue="'%"+rValue+"%'";
                }//现品票
                if(key.equals("barcode")){
                    parmsKey="and a.barcode like ";
                    parmsValue="'%"+rValue+"%'";
                }//库区
                if(key.equals("posname")){
                    parmsKey="and p.posname like ";
                    parmsValue="'%"+rValue+"%'";
                }//仓库
                if(key.equals("whname")){
                    parmsKey="and w.whname like ";
                    parmsValue="'%"+rValue+"%'";
                }
            }



            if(StringUtils.isNotBlank(parmsKey)){
                record.set("ParmsKey",parmsKey ).set("ParmsValue", parmsValue);
                records.add(record);
            }

        }

        return records;
    }


    /**
     * 执行存储过程
     * */
    public List<Record> excuteBarcodeTracePage(String dataSourceConfigName,String tempTableName) {
        List<Map> listMap=(List<Map>) executeFunc(dataSourceConfigName, (conn) -> {
            List<Map<String, Object>> list = new ArrayList<>();
            CallableStatement proc = conn.prepareCall("{ call P_Sys_InventoryBarcodeTracePage(?,@Type ='BillNo') }");
            proc.setObject(1, tempTableName);
            //执行
            boolean isSuccess = true;
            ResultSet rs = null;
            try{
                rs = proc.executeQuery();
            }catch (SQLException e){
                isSuccess = false;
            }
            if(isSuccess){
                // 获取结果集列名
                ResultSetMetaData rsm = rs.getMetaData();
                List<String> allColumn = new ArrayList<>();
                // 获取结果集列名集合
                int cH = 1;
                while (cH <= rsm.getColumnCount()) {
                    String columnName = rsm.getColumnName(cH).toLowerCase();
                    allColumn.add(columnName);
                    cH++;
                }
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>(10);
                    int i = 1;
                    while (i <= allColumn.size()) {
                        String string = rs.getString(i);
                        map.put(allColumn.get(i - 1), string);
                        i++;
                    }
                    list.add(map);
                }
            }
            return list;
        });

        List<Record> listRecord=new ArrayList<>();
        for (Map map : listMap) {
            Record record=new Record();
            record.put(map);
            listRecord.add(record);
        }
        return listRecord;
    }

    /**
     * 数据源
     */
    public Page<Record> datas(int pageSize, int pageNumber, Kv para){
        Page<Record> paginate = dbTemplate("report.barCodeSelectDatas",para).paginate(pageNumber, pageSize);


        List<Record> list = paginate.getList();

        Set<String> set=new HashSet<>();
        for (Record record : list) {
            String ccategoryname = record.getStr("ccategoryname");
            set.add(ccategoryname);
            if(ccategoryname.equals("钢卷")||ccategoryname.equals("钢板")){
                record.set("type","卷料");
            }else if(ccategoryname.equals("片料")){
                record.set("type","片料");
            }
        }
        return paginate;
    }

    public List<Record> datasList(Kv kv) {
        List<Record> records = dbTemplate("report.barCodeSelectDatas", kv).find();

        return records;
    }


    /**
     * 详情数据源
     */
    public Page<Record> detailsDatas(Integer pageNumber, Integer pageSize, Kv para) {
        return  dbTemplate("report.barCodeSelectData", para).paginate(pageNumber, pageSize);

    }


    @Override
    protected String getTableName() {
        return null;
    }
}
