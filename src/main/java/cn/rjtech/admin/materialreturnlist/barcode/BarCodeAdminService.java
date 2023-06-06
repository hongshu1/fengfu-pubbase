//package cn.rjtech.admin.materialreturnlist.barcode;
//import cn.jbolt.core.service.JBoltDictionaryService;
//import cn.rjtech.base.service.view.BaseU9ViewService;
//import com.jfinal.aop.Inject;
//import com.jfinal.kit.Kv;
//import com.jfinal.plugin.activerecord.Page;
//
//import com.jfinal.plugin.activerecord.Record;
//import java.util.*;
//
///**
// * 条码汇总
// * @author Kephon
// */
//public class BarCodeAdminService extends BaseU9ViewService {
//
//    @Inject
//    JBoltDictionaryService jBoltDictionaryService;
//
//
//
//    /**
//     * 数据源
//     */
//    public Page<Record> datas(int pageSize, int pageNumber, Kv para){
//        Page<Record> paginate = dbTemplate("report.barCodeSelectDatas",para).paginate(pageNumber, pageSize);
//
//
//        List<Record> list = paginate.getList();
//
//        Set<String> set=new HashSet<>();
//        for (Record record : list) {
//            String ccategoryname = record.getStr("ccategoryname");
//            set.add(ccategoryname);
//            if(ccategoryname.equals("钢卷")||ccategoryname.equals("钢板")){
//                record.set("type","卷料");
//            }else if(ccategoryname.equals("片料")){
//                record.set("type","片料");
//            }
//        }
//        return paginate;
//    }
//
//    public List<Record> datasList(Kv kv) {
//        List<Record> records = dbTemplate("report.barCodeSelectDatas", kv).find();
//
//        return records;
//    }
//
//
//    /**
//     * 详情数据源
//     */
//    public Page<Record> detailsDatas(Integer pageNumber, Integer pageSize, Kv para) {
//        return  dbTemplate("report.barCodeSelectData", para).paginate(pageNumber, pageSize);
//
//    }
//
//
//
//
//
//
//
//
//
//}
