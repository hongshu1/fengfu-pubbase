package cn.rjtech.admin.sysmaterialspreparedetail;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.inventoryroutinginvc.InventoryRoutingInvcService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.momaterialsscansum.MoMaterialscanlogService;
import cn.rjtech.admin.person.PersonService;
import cn.rjtech.admin.stockbarcodeposition.StockBarcodePositionService;
import cn.rjtech.admin.sysmaterialsprepare.SysMaterialsprepareService;
import cn.rjtech.admin.sysmaterialspreparescan.SysMaterialspreparescanService;
import cn.rjtech.admin.syspureceive.SysPureceiveService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.HttpApiUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.*;

/**
 * 备料单明细
 *
 * @ClassName: SysMaterialspreparedetailService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-01 23:50
 */
public class SysMaterialspreparedetailService extends BaseService<SysMaterialspreparedetail> {
    private final SysMaterialspreparedetail dao = new SysMaterialspreparedetail().dao();

    @Inject
    private MoMaterialscanlogService moMaterialscanlogService;

    @Inject
    private SysMaterialsprepareService sysMaterialsprepareService;
    @Inject
    private SysPureceiveService syspureceiveservice;

    @Inject
    private PersonService personservice;

    @Inject
    private MoDocService moDocS;

    @Inject
    private SysMaterialspreparescanService sysmaterialspreparescanservice;

    @Inject
    private InventoryRoutingInvcService inventoryroutinginvcservice;

    @Inject
    private InventoryService invent;

    @Inject
    private StockBarcodePositionService stockbarcodepositionservice;

    @Override
    protected SysMaterialspreparedetail dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber     第几页
     * @param pageSize       每页几条数据
     * @param SourceBillType 来源单据类型
     * @param State          状态：1=已扫描，0=未扫描
     * @return
     */
    public Page<SysMaterialspreparedetail> getAdminDatas(int pageNumber, int pageSize, String SourceBillType, String State) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("SourceBillType", SourceBillType);
        sql.eq("State", State);
        //排序
        sql.desc("AutoID");
        return paginate(sql);
    }

    /**
     * 保存
     *
     * @param sysMaterialspreparedetail
     * @return
     */
    public Ret save(SysMaterialspreparedetail sysMaterialspreparedetail) {
        if (sysMaterialspreparedetail == null || isOk(sysMaterialspreparedetail.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        boolean success = sysMaterialspreparedetail.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(sysMaterialspreparedetail.getAutoID(), JBoltUserKit.getUserId(), sysMaterialspreparedetail.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     *
     * @param sysMaterialspreparedetail
     * @return
     */
    public Ret update(SysMaterialspreparedetail sysMaterialspreparedetail) {
        if (sysMaterialspreparedetail == null || notOk(sysMaterialspreparedetail.getAutoID())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        SysMaterialspreparedetail dbSysMaterialspreparedetail = findById(sysMaterialspreparedetail.getAutoID());
        if (dbSysMaterialspreparedetail == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        boolean success = sysMaterialspreparedetail.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(sysMaterialspreparedetail.getAutoID(), JBoltUserKit.getUserId(), sysMaterialspreparedetail.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param sysMaterialspreparedetail 要删除的model
     * @param kv                        携带额外参数一般用不上
     * @return
     */
    @Override
    protected String afterDelete(SysMaterialspreparedetail sysMaterialspreparedetail, Kv kv) {
        //addDeleteSystemLog(sysMaterialspreparedetail.getAutoID(), JBoltUserKit.getUserId(),sysMaterialspreparedetail.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param sysMaterialspreparedetail model
     * @param kv                        携带额外参数一般用不上
     * @return
     */
    @Override
    public String checkInUse(SysMaterialspreparedetail sysMaterialspreparedetail, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    public List<Record> cworkname() {
        return dbTemplate("materialsprepare.iWorkRegionMid", Kv.of("isenabled", "1")).find();
    }

    public List<Record> cworkname1() {
        return dbTemplate("materialsprepare.cworkname", Kv.of("isenabled", "1")).find();
    }

    public List<Record> cworkshiftcode() {
        return dbTemplate("materialsprepare.cworkshiftcode", Kv.of("isenabled", "1")).find();
    }

    public Page<Record> getMaterialsdetials(int pageNumber, int pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("materialsprepare.recpordetail", kv).paginate(pageNumber, pageSize);
        return paginate;
    }

    public Page<Record> getMaterialsdetials1(int pageNumber, int pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("materialsprepare.recpor1", kv).paginate(pageNumber, pageSize);
        return paginate;
    }

    public Page<Record> getBarcode(int pageNumber, int pageSize, Kv kv) {
        Page<Record> paginate = dbTemplate("materialsprepare.getBarcodedatas", kv).paginate(pageNumber, pageSize);
        return paginate;
    }

    public Record barcode(Kv kv) {
        Record first = dbTemplate("materialsprepare.barcodeDatas", kv).findFirst();
        ValidationUtils.notNull(first, "未查到条码为：" + kv.getStr("barcode") + "的数据,请核实再录入。");
        return first;
    }

    public Record getchooseM(Kv kv) {
        return dbTemplate("materialsprepare.getManualAdddatas", kv).findFirst();
    }

    public Ret submitByJBoltTable(String map1) {
        String[] split = map1.split(",");
        //工单号
        String[] split5 = split[0].split(":");
        String id = split5[1];
        SysMaterialsprepare sysMaterialsprepare = new SysMaterialsprepare();
        //获取当前用户信息
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        tx(() -> {
            MoDoc modoc = moDocS.findFirst("select * from Mo_MoDoc where cMoDocNo=?", id);
            sysMaterialsprepare.setSourceBillID(modoc.getIAutoId());
            sysMaterialsprepare.setSourceBillNo(modoc.getCMoDocNo());
            sysMaterialsprepare.setBillType("手工作成");
            sysMaterialsprepare.setOrganizeCode(getOrgCode());
            sysMaterialsprepare.setCcreatename(user.getUsername());
            sysMaterialsprepare.setDcreatetime(now);
            sysMaterialsprepare.setIcreateby(user.getId());
            sysMaterialsprepare.setBillDate(DateUtil.format(now, "yyyy-MM-dd HH:mm:ss"));
            sysMaterialsprepare.setBillNo("BL" + DateUtil.format(new Date(), "yyyyMMdd") + RandomUtil.randomNumbers(6));
            //主表新增
            ValidationUtils.isTrue(sysMaterialsprepare.save(), ErrorMsg.SAVE_FAILED);
            //从表的操作
            // 获取保存数据（执行保存，通过 getSaveRecordList）
            saveTableSubmitDatas(sysMaterialsprepare, id, map1);
            //修改工单状态
            //获取修改数据（执行修改，通过 getUpdateRecordList）
            //获取删除数据（执行删除，通过 getDelete）
            return true;
        });
        return SUCCESS;
    }

    private void saveTableSubmitDatas(SysMaterialsprepare sysMaterialsprepare, String id, String map1) {
        String[] split = map1.split(",");
        ArrayList<SysMaterialspreparedetail> sysMaterialspreparedetails = new ArrayList<>();
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        for (int i = 1; i < split.length; i++) {
            String[] split1 = split[i].split(":");
            //存货编码:输入的数量
            if (!split1[0].equals("") && !split1[1].equals("")) {
                SysMaterialspreparedetail sysMaterialspreparedetail = new SysMaterialspreparedetail();
                sysMaterialspreparedetail.setMasID(Long.valueOf(sysMaterialsprepare.getAutoID()));
                sysMaterialspreparedetail.setAutoID(String.valueOf(JBoltSnowflakeKit.me.nextId()));
//				sysMaterialspreparedetail.setPosCode();
//				sysMaterialspreparedetail.setBarcode();
                sysMaterialspreparedetail.setInvCode(split1[0]);
//				sysMaterialspreparedetail.setNum();
                sysMaterialspreparedetail.setQty(new BigDecimal(split1[1]));
//				sysMaterialspreparedetail.setPackRate();
//            sysMaterialspreparedetail.setSourceBillType();
//            sysMaterialspreparedetail.setSourceBillNo()
//            sysMaterialspreparedetail.setSourceBillNoRow()
//            sysMaterialspreparedetail.setSourceBillID()
//            sysMaterialspreparedetail.setSourceBillDid()
//            sysMaterialspreparedetail.setMemo()
                sysMaterialspreparedetail.setState(String.valueOf(0));
                sysMaterialspreparedetail.setIsDeleted(false);
                sysMaterialspreparedetail.setIcreateby(user.getId());
                sysMaterialspreparedetail.setCcreatename(user.getUsername());
                sysMaterialspreparedetail.setDcreatetime(now);
//            sysMaterialspreparedetail.setIupdateby()
//            sysMaterialspreparedetail.setCupdatename()
//            sysMaterialspreparedetail.setDupdatetime()
                sysMaterialspreparedetails.add(sysMaterialspreparedetail);
            }
        }
        this.batchSave(sysMaterialspreparedetails);
    }


    public Ret pushU8(SysMaterialsprepare sysMaterialsprepare, List<SysMaterialspreparedetail> sysMaterialspreparedetails) {
        if (CollUtil.isEmpty(sysMaterialspreparedetails)) {
            return fail("数据不能为空");
        }

        User user = JBoltUserKit.getUser();
        JSONObject data = new JSONObject();

        data.set("userCode", user.getUsername());
        data.set("organizeCode", this.getdeptid());
        data.set("token", "");

        JSONObject preallocate = new JSONObject();


        preallocate.set("userCode", user.getUsername());
        preallocate.set("password", "123456");
        preallocate.set("organizeCode", this.getdeptid());
        preallocate.set("CreatePerson", user.getId());
        preallocate.set("CreatePersonName", user.getUsername());
        preallocate.set("loginDate", DateUtil.format(new Date(), "yyyy-MM-dd"));
        preallocate.set("tag", "AssemVouch");
        preallocate.set("type", "AssemVouch");

        data.set("PreAllocate", preallocate);

        ArrayList<Object> maindata = new ArrayList<>();
        sysMaterialspreparedetails.stream().forEach(s -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("iwhname", "");
            jsonObject.set("invcode", "");
            jsonObject.set("userCode", user.getUsername());
            jsonObject.set("organizeCode", this.getdeptid());
            jsonObject.set("OWhCode", s.getPosCode());
            jsonObject.set("owhname", "");
            jsonObject.set("barcode", s.getBarcode());
            jsonObject.set("invstd", "");
            jsonObject.set("invname", "");
//            jsonObject.set("CreatePerson", s.getCreatePerson());
            jsonObject.set("qty", s.getQty());
            jsonObject.set("CreatePersonName", user.getUsername());
            jsonObject.set("IRdName", "");
            jsonObject.set("ORdName", "");
            jsonObject.set("Tag", "AssemVouch");
            jsonObject.set("VouchTemplate", "");
//			jsonObject.set("IWhCode",s.getWhCode());
//			jsonObject.set("BillType",s.getAssemType());
//			jsonObject.set("IRdType",sysassem.getIRdCode());
//			jsonObject.set("ORdType",sysassem.getORdCode());
//			jsonObject.set("IDeptCode",sysassem.getDeptCode());
//			jsonObject.set("ODeptCode",sysassem.getDeptCode());
//			jsonObject.set("RowNo",s.getRowNo());

            maindata.add(jsonObject);
        });
        data.set("MainData", maindata);

        //请求头
        Map<String, String> header = new HashMap<>(5);
        header.put("Content-Type", "application/json");
        String url = "http://localhost:8081/web/erp/common/vouchProcessDynamicSubmit";

        try {
            String post = HttpApiUtils.httpHutoolPost(url, data, header);
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(post);
            if (isOk(post)) {
                if ("201".equals(jsonObject.getString("code"))) {
                    return successWithData(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Ret.msg("上传u8失败");
    }

    //通过当前登录人名称获取部门id
    public String getdeptid() {
        String dept = "001";
        User user = JBoltUserKit.getUser();
        Person person = personservice.findFirstByUserId(user.getId());
        if (null != person && "".equals(person)) {
            dept = person.getCOrgCode();
        }
        return dept;
    }


    public Ret submitByJBoltTableGo(String map1) {
        String[] split = map1.split(",");
        String[] split5 = split[0].split(":");
        String id = split5[1];
        SysMaterialsprepare sysMaterialsprepare = new SysMaterialsprepare();
        //获取当前用户信息？
//		User user = JBoltUserKit.getUser();
//		Date now = new Date();
        tx(() -> {
            //通过 id 判断是新增还是修改
//			MoDoc modoc = moDocS.findFirst("select * from Mo_MoDoc where cMoDocNo=?", id);
//			sysMaterialsprepare.setSourceBillID(modoc.getIAutoId());
//			sysMaterialsprepare.setSourceBillNo(modoc.getCMoDocNo());
//			sysMaterialsprepare.setBillType("手工作成");
//			sysMaterialsprepare.setOrganizeCode(getOrgCode());
//			sysMaterialsprepare.setCcreatename(user.getName());
//			sysMaterialsprepare.setDcreatetime(now);
//			sysMaterialsprepare.setIcreateby(user.getId());
//			sysMaterialsprepare.setBillDate(DateUtil.format(now, "yyyy-MM-dd HH:mm:ss"));
//			sysMaterialsprepare.setBillNo("BL" + DateUtil.format(new Date(), "yyyyMMdd") + RandomUtil.randomNumbers(6));
            //主表新增
//			ValidationUtils.isTrue(sysMaterialsprepare.save(), ErrorMsg.SAVE_FAILED);
            //从表的操作
            // 获取保存数据（执行保存，通过 getSaveRecordList）
            saveTableSubmitDatasGo(sysMaterialsprepare, id, map1);
            //修改工单状态
            //获取修改数据（执行修改，通过 getUpdateRecordList）
            //获取删除数据（执行删除，通过 getDelete）
            return true;
        });
        return SUCCESS;
    }

    private void saveTableSubmitDatasGo(SysMaterialsprepare sysMaterialsprepare, String id, String map1) {
        String[] split = map1.split(",");
        ArrayList<SysMaterialspreparedetail> sysMaterialspreparedetails = new ArrayList<>();
        for (int i = 1; i < split.length; i++) {
            String[] split1 = split[i].split(":");
            if (!split1[0].equals("") && !split1[1].equals("") && !split1[1].equals("0") && !split1[2].equals("")) {
//				Kv kv = new Kv();
//				kv.set("cmodocno", String.valueOf(id));
//				kv.set("barcode", split1[0]);
//				Record record = dbTemplate("materialsprepare.test1", kv).findFirst();
                User user = JBoltUserKit.getUser();
                Date now = new Date();
//				SysMaterialspreparedetail sysMaterialspreparedetail = new SysMaterialspreparedetail();
                SysMaterialspreparedetail sysMaterialspreparedetail = this.findFirst("SELECT * FROM T_Sys_MaterialsPrepareDetail WHERE MasID=? AND Barcode=?", split1[0], split1[2]);
//				sysMaterialspreparedetail.setMasID(Long.valueOf(sysMaterialsprepare.getAutoID()));
//				sysMaterialspreparedetail.setAutoID(String.valueOf(JBoltSnowflakeKit.me.nextId()));
//				sysMaterialspreparedetail.setPosCode(record.getStr("PosCode")==null?"":record.getStr("PosCode"));
//				sysMaterialspreparedetail.setBarcode(record.getStr("Barcode")==null?"":record.getStr("Barcode"));
//				sysMaterialspreparedetail.setInvCode(record.getStr("cInvCode")==null?"":record.getStr("cInvCode"));
                sysMaterialspreparedetail.setNum(new BigDecimal(split1[1]).add(sysMaterialspreparedetail.getNum()));
//				sysMaterialspreparedetail.setQty(new BigDecimal(split1[1]));
//				sysMaterialspreparedetail.setPackRate(record.getBigDecimal("PackRate")==null?BigDecimal.ZERO:record.getBigDecimal("PackRate"));
//            sysMaterialspreparedetail.setSourceBillType();
//            sysMaterialspreparedetail.setSourceBillNo()
//            sysMaterialspreparedetail.setSourceBillNoRow()
//            sysMaterialspreparedetail.setSourceBillID()
//            sysMaterialspreparedetail.setSourceBillDid()
//            sysMaterialspreparedetail.setMemo()
//				sysMaterialspreparedetail.setState(String.valueOf(0));
//				sysMaterialspreparedetail.setIsDeleted(false);
//				sysMaterialspreparedetail.setIcreateby(user.getId());
//				sysMaterialspreparedetail.setCcreatename(user.getName());
//				sysMaterialspreparedetail.setDcreatetime(now);
                sysMaterialspreparedetail.setIupdateby(user.getId());
                sysMaterialspreparedetail.setCupdatename(user.getUsername());
                sysMaterialspreparedetail.setDupdatetime(now);
                sysMaterialspreparedetails.add(sysMaterialspreparedetail);
            }
        }
        this.batchUpdate(sysMaterialspreparedetails);
//		Kv kv = new Kv();
//		kv.set("imodocid", String.valueOf(id));
//		kv.set("barcode", String.valueOf(id));
//		List<Record> records = dbTemplate("materialsprepare.test1", kv).find();
//		if (CollUtil.isEmpty(records)) return;
//		User user = JBoltUserKit.getUser();
//		Date now = new Date();
//		ArrayList<SysMaterialspreparedetail> sysMaterialspreparedetails = new ArrayList<>();
//		for (int i = 0; i < records.size(); i++) {
//			SysMaterialspreparedetail sysMaterialspreparedetail = new SysMaterialspreparedetail();
//			sysMaterialspreparedetail.setMasID(Long.valueOf(sysMaterialsprepare.getAutoID()));
//			sysMaterialspreparedetail.setAutoID(String.valueOf(JBoltSnowflakeKit.me.nextId()));
//			sysMaterialspreparedetail.setPosCode(records.get(i).getStr("PosCode"));
//			sysMaterialspreparedetail.setBarcode(records.get(i).getStr("Barcode"));
//			sysMaterialspreparedetail.setInvCode(records.get(i).getStr("cInvCode"));
//			sysMaterialspreparedetail.setNum(BigDecimal.valueOf(0));
//			sysMaterialspreparedetail.setQty(records.get(i).getBigDecimal("Qty"));
//			sysMaterialspreparedetail.setPackRate(records.get(i).getBigDecimal("PackRate"));
////            sysMaterialspreparedetail.setSourceBillType();
////            sysMaterialspreparedetail.setSourceBillNo()
////            sysMaterialspreparedetail.setSourceBillNoRow()
////            sysMaterialspreparedetail.setSourceBillID()
////            sysMaterialspreparedetail.setSourceBillDid()
////            sysMaterialspreparedetail.setMemo()
//			sysMaterialspreparedetail.setState(String.valueOf(0));
//			sysMaterialspreparedetail.setIsDeleted(false);
//			sysMaterialspreparedetail.setIcreateby(user.getId());
//			sysMaterialspreparedetail.setCcreatename(user.getName());
//			sysMaterialspreparedetail.setDcreatetime(now);
////            sysMaterialspreparedetail.setIupdateby()
////            sysMaterialspreparedetail.setCupdatename()
////            sysMaterialspreparedetail.setDupdatetime()
//			sysMaterialspreparedetails.add(sysMaterialspreparedetail);
//		}
//		this.batchSave(sysMaterialspreparedetails);
    }

//	private void updateTableSubmitDatasGo(JBoltTable jBoltTable, SysMaterialsprepare sysMaterialsprepare) {
//		List<Record> list = jBoltTable.getUpdateRecordList();
//		if (CollUtil.isEmpty(list)) return;
//		User user = JBoltUserKit.getUser();
//		Date now = new Date();
//		ArrayList<SysMaterialspreparedetail> sysMaterialspreparedetails = new ArrayList<>();
//		for (int i = 0; i < list.size(); i++) {
//			Record row = list.get(i);
//			SysMaterialspreparedetail sysMaterialspreparedetail = new SysMaterialspreparedetail();
//			sysMaterialspreparedetail.setMasID(Long.valueOf(sysMaterialsprepare.getAutoID()));
////            sysMaterialspreparedetail.setModifyDate(now);
////            sysMaterialspreparedetail.setModifyPerson(user.getName());
//			sysMaterialspreparedetail.setBarcode(row.get("barcode"));
//			sysMaterialspreparedetail.setSourceBillNoRow(row.get("sourcebillnorow"));
//			sysMaterialspreparedetail.setQty(new BigDecimal(row.get("qty").toString()));
//			sysMaterialspreparedetail.setPosCode(row.getStr("poscode"));
//			sysMaterialspreparedetail.setSourceBillNo(row.getStr("sourcebillno"));
//			sysMaterialspreparedetail.setSourceBillDid(row.getStr("sourcebilldid"));
//			sysMaterialspreparedetail.setSourceBillID(row.getStr("sourcebilldid"));
//			sysMaterialspreparedetail.setMemo(row.getStr("memo"));
////			sysMaterialspreparedetail.setSourceType(row.getStr("sourcebilltype"));
////			sysMaterialspreparedetail.setAssemType(row.getStr("assemtype"));
////			sysMaterialspreparedetail.setWhCode(row.getStr("whcode"));
////			sysMaterialspreparedetail.setCombinationNo(Integer.valueOf(row.getStr("combinationno")));
////			sysMaterialspreparedetail.setRowNo(Integer.valueOf(row.getStr("rowno")));
////			sysMaterialspreparedetail.setTrackType(row.getStr("tracktype"));
//
//			sysMaterialspreparedetails.add(sysMaterialspreparedetail);
//
//		}
//		sysmaterialspreparedetailservice.batchUpdate(sysMaterialspreparedetails);
//
//		System.out.println("开始上传u8开始上传u8开始上传u8开始上传u8开始上传u8开始上传u8" + new Date());
//		Ret ret = pushU8(sysMaterialsprepare, sysMaterialspreparedetails);
//		System.out.println(new Date() + "u8上传结束，u8上传结束，u8上传结束，u8上传结束，u8上传结束" + ret);
//	}


    public Ret submitByJBoltTableGo1(String map1) {
        String[] no1 = map1.split(",");
        String billno = no1[0].split(":")[1];
        String cmodocno = no1[0].split(":")[2];

//		获取当前用户信息
        User user = JBoltUserKit.getUser();
        Date now = new Date();
        tx(() -> {
            ArrayList<SysMaterialspreparescan> sysMaterialspreparescans = new ArrayList<>();
            ArrayList<SysMaterialspreparedetail> sysMaterialspreparedetails = new ArrayList<>();
            ArrayList<StockBarcodePosition> stockBarcodePositions = new ArrayList<>();
            ArrayList<MoMaterialscanlog> moMaterialscanlogs = new ArrayList<>();

            for (int i = 1; i < no1.length; i++) {
                String[] data = no1[i].split(":");
                SysMaterialspreparedetail sysMaterialspreparedetail = new SysMaterialspreparedetail();
                SysMaterialspreparescan sysMaterialspreparescan = new SysMaterialspreparescan();
                MoMaterialscanlog moMaterialscanlog = new MoMaterialscanlog();
                //提交备料存入备料细表
                sysMaterialspreparedetail.setAutoID(JBoltSnowflakeKit.me.nextId() + "");
                sysMaterialspreparedetail.setMasID(Long.valueOf(sysMaterialsprepareService.findFirst("SELECT * FROM T_Sys_MaterialsPrepare WHERE BillNo=?", billno).getAutoID()));
                sysMaterialspreparedetail.setBarcode(data[0]);
                sysMaterialspreparedetail.setInvCode(data[1]);
                StockBarcodePosition stockBarcodePosition = stockbarcodepositionservice.findFirst("SELECT * FROM T_Sys_StockBarcodePosition WHERE Barcode=?", data[0]);
                sysMaterialspreparedetail.setPosCode(stockBarcodePosition.getPosCode());
                sysMaterialspreparedetail.setQty(stockBarcodePosition.getQty());
                sysMaterialspreparedetail.setPackRate(stockBarcodePosition.getPackRate());
//				sysMaterialspreparedetail.setSourceBillType();
//				sysMaterialspreparedetail.setSourceBillNo();
//				sysMaterialspreparedetail.setSourceBillNoRow();
//				sysMaterialspreparedetail.setSourceBillID();
//				sysMaterialspreparedetail.setSourceBillDid();
//				sysMaterialspreparedetail.setMemo();
                sysMaterialspreparedetail.setState("1");
                sysMaterialspreparedetail.setIsDeleted(false);
                sysMaterialspreparedetail.setIcreateby(user.getId());
                sysMaterialspreparedetail.setCcreatename(user.getUsername());
                sysMaterialspreparedetail.setDcreatetime(now);
//				sysMaterialspreparedetail.setIupdateby();
//				sysMaterialspreparedetail.setCupdatename();
//				sysMaterialspreparedetail.setDupdatetime();
                sysMaterialspreparedetails.add(sysMaterialspreparedetail);


                //提交备料存入扫描日志表
                sysMaterialspreparescan.setIAutoId(JBoltSnowflakeKit.me.nextId());
                sysMaterialspreparescan.setCBarcode(data[0]);
                sysMaterialspreparescan.setIInventoryId(invent.findFirst("SELECT * FROM Bd_Inventory WHERE cInvCode=?", data[1]).getIAutoId());
                sysMaterialspreparescan.setCBatch(data[2]);
                sysMaterialspreparescan.setState("1");
                sysMaterialspreparescan.setIScanDate(now);
//				sysMaterialspreparescan.setIQty();
                sysMaterialspreparescan.setIMaterialsPrepareId(sysMaterialspreparedetail.getMasID());
                sysMaterialspreparescan.setIMaterialsPrepareDetailId(Long.valueOf(sysMaterialspreparedetail.getAutoID()));
//				sysMaterialspreparescan.setIWarehouseAreaId();
//				sysMaterialspreparescan.setDProdDate();
//				sysMaterialspreparescan.setITotalQty();
//				sysMaterialspreparescan.setIScanQty();
                sysMaterialspreparescans.add(sysMaterialspreparescan);

                //提交备料存入下一环节扫码日志表
                Kv kv2 = new Kv();
                kv2.set("imodocid", moDocS.findFirst("SELECT * FROM Mo_MoDoc WHERE cMoDocNo=?", cmodocno).getIAutoId());
                kv2.set("cinvcode", data[1]);
                Record zijianwuliaoS = dbTemplate("materialsprepare.zijianwuliaoji2", kv2).findFirst();
                moMaterialscanlog.setIAutoId(JBoltSnowflakeKit.me.nextId());
                moMaterialscanlog.setIMoDocId(moDocS.findFirst("SELECT * FROM Mo_MoDoc WHERE cMoDocNo=?", cmodocno).getIAutoId());
                moMaterialscanlog.setIMaterialsPrepairDid(Long.valueOf(sysMaterialspreparedetail.getAutoID()));
                moMaterialscanlog.setIInventoryId(sysMaterialspreparescan.getIInventoryId());
                moMaterialscanlog.setCBarcode(data[0]);
                moMaterialscanlog.setIQty(sysMaterialspreparedetail.getQty());
                moMaterialscanlog.setICreateBy(user.getId());
                moMaterialscanlog.setCCreateName(user.getUsername());
                moMaterialscanlog.setDCreateTime(now);
                moMaterialscanlog.setIPlayQty(zijianwuliaoS.get("planIqty"));
                moMaterialscanlogs.add(moMaterialscanlog);
                //更改条码库存表状态
                stockBarcodePosition.setState(3);
                stockBarcodePositions.add(stockBarcodePosition);


            }
            this.batchSave(sysMaterialspreparedetails);
            sysmaterialspreparescanservice.batchSave(sysMaterialspreparescans);
            stockbarcodepositionservice.batchUpdate(stockBarcodePositions);
            moMaterialscanlogService.batchSave(moMaterialscanlogs);

            return true;
        });
        return SUCCESS;
    }


    public Ret submitQTY(String Oldbarcode, String Oldqty, String Newbarcode, String Newqty) {
        tx(() -> {
            StockBarcodePosition stockBarcodePosition = stockbarcodepositionservice.findFirst("SELECT * FROM T_Sys_StockBarcodePosition WHERE Barcode=?", Oldbarcode);
            //旧现品票的物料数量,数量相减
            stockBarcodePosition.setQty(new BigDecimal(Oldqty).subtract(new BigDecimal(Newqty)));

            StockBarcodePosition stockBarcodePosition1 = new StockBarcodePosition();
            stockBarcodePosition1._setAttrs(stockBarcodePosition);
            //修改新现品票不同的部分
            stockBarcodePosition1.setAutoID(JBoltSnowflakeKit.me.nextId());
            stockBarcodePosition1.setBarcode(Newbarcode);
            stockBarcodePosition1.setQty(new BigDecimal(Newqty));

            //旧现品票更新
            stockBarcodePosition.update();
            //新现品票更新
            stockBarcodePosition1.save();

            return true;
        });
        return SUCCESS;
    }

}