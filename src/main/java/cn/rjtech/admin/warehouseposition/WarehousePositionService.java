package cn.rjtech.admin.warehouseposition;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.admin.warehousearea.WarehouseAreaService;
import cn.rjtech.admin.warehouseshelves.WarehouseShelvesService;
import cn.rjtech.base.service.BaseService;
import cn.rjtech.model.momdata.Warehouse;
import cn.rjtech.model.momdata.WarehouseArea;
import cn.rjtech.model.momdata.WarehousePosition;
import cn.rjtech.model.momdata.WarehouseShelves;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.Date;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 库位档案 Service
 *
 * @ClassName: WarehousePositionService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-02 20:45
 */
public class WarehousePositionService extends BaseService<WarehousePosition> {
    private final WarehousePosition dao = new WarehousePosition().dao();


    @Override
    protected WarehousePosition dao() {
        return dao;
    }

    @Inject
    private WarehouseService warehouseService;
    @Inject
    private WarehouseAreaService warehouseAreaService;
    @Inject
    private WarehouseShelvesService warehouseShelvesService;

    /**
     * 后台管理分页查询
     */
    public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("warehouseposition.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
        //return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
    }

    public List<Record> list(Kv kv) {
        return dbTemplate("warehouseposition.paginateAdminDatas", kv).find();
    }

    public Record findByIautoId(Kv kv) {
        return dbTemplate("warehouseposition.paginateAdminDatas", kv).findFirst();
    }

    public WarehousePosition findByCpositionCode(String cshelvescode) {
        return findFirst(Okv.by("cPositionCode", cshelvescode).set("isDeleted", false), "iautoid", "asc");
    }

    /**
     * 保存
     */
    public Ret save(WarehousePosition warehousePosition) {
        if (warehousePosition == null || isOk(warehousePosition.getIautoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(warehousePosition.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        ValidationUtils.isTrue(findByCpositionCode(warehousePosition.getCpositioncode()) == null, warehousePosition.getCpositioncode() + " 编码重复");

        warehousePosition.setIcreateby(JBoltUserKit.getUserId());
        warehousePosition.setCcreatename(JBoltUserKit.getUserName());
        warehousePosition.setDcreatetime(new Date());

        warehousePosition.setCorgcode(getOrgCode());
        warehousePosition.setCorgname(getOrgName());
        warehousePosition.setIorgid(getOrgId());

        //更新信息
        warehousePosition.setIupdateby(JBoltUserKit.getUserId());
        warehousePosition.setCupdatename(JBoltUserKit.getUserName());
        warehousePosition.setDupdatetime(new Date());


        boolean success = warehousePosition.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(warehousePosition.getIautoid(), JBoltUserKit.getUserId(), warehousePosition.getName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(WarehousePosition warehousePosition) {
        if (warehousePosition == null || notOk(warehousePosition.getIautoid())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        WarehousePosition dbWarehousePosition = findById(warehousePosition.getIautoid());
        if (dbWarehousePosition == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }

        //查询编码是否存在
        WarehousePosition ware = findByCpositionCode(warehousePosition.getCpositioncode());

        //编码重复判断
        if (ware != null && !warehousePosition.getIautoid().equals(ware.getIautoid())) {
            ValidationUtils.assertNull(ware.getCpositioncode(), warehousePosition.getCpositioncode() + " 编码重复！");
        }

        dbWarehousePosition.setIupdateby(JBoltUserKit.getUserId());
        dbWarehousePosition.setCupdatename(JBoltUserKit.getUserName());
        dbWarehousePosition.setDupdatetime(new Date());
        //if(existsName(warehousePosition.getName(), warehousePosition.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = warehousePosition.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(warehousePosition.getIautoid(), JBoltUserKit.getUserId(), warehousePosition.getName());
        }
        return ret(success);
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        tx(() -> {
            String[] idarry = ids.split(",");


            //料品档案主表
            //deleteByIds(ids,true);
            update("UPDATE Bd_Warehouse_Position SET isDeleted = 1 WHERE iAutoId IN (" + ArrayUtil.join(idarry, COMMA) + ") ");
            return true;
        });
        return SUCCESS;
//    return deleteByIds(ids,true);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param warehousePosition 要删除的model
     * @param kv                携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(WarehousePosition warehousePosition, Kv kv) {
        //addDeleteSystemLog(warehousePosition.getIautoid(), JBoltUserKit.getUserId(),warehousePosition.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param warehousePosition 要删除的model
     * @param kv                携带额外参数一般用不上
     */
    @Override
    public String checkCanDelete(WarehousePosition warehousePosition, Kv kv) {
        //如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
        return checkInUse(warehousePosition, kv);
    }

    /**
     * 设置返回二开业务所属的关键systemLog的targetType
     */
    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 切换isdeleted属性
     */
    public Ret toggleIsdeleted(Long id) {
        return toggleBoolean(id, "isDeleted");
    }

    /**
     * 切换isenabled属性
     */
    public Ret toggleIsenabled(Long id) {
        return toggleBoolean(id, "isEnabled");
    }

    /**
     * 检测是否可以toggle操作指定列
     *
     * @param warehousePosition 要toggle的model
     * @param column            操作的哪一列
     * @param kv                携带额外参数一般用不上
     */
    @Override
    public String checkCanToggle(WarehousePosition warehousePosition, String column, Kv kv) {
        //没有问题就返回null  有问题就返回提示string 字符串
        return null;
    }

    /**
     * toggle操作执行后的回调处理
     */
    @Override
    protected String afterToggleBoolean(WarehousePosition warehousePosition, String column, Kv kv) {
        //addUpdateSystemLog(warehousePosition.getIautoid(), JBoltUserKit.getUserId(), warehousePosition.getName(),"的字段["+column+"]值:"+warehousePosition.get(column));
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param warehousePosition model
     * @param kv                携带额外参数一般用不上
     */
    @Override
    public String checkInUse(WarehousePosition warehousePosition, Kv kv) {
        //这里用来覆盖 检测WarehousePosition是否被其它表引用
        return null;
    }

    public List<WarehousePosition> dataList(Boolean isEnabled, Long iwarehouseid) {
        Okv kv = Okv.create();
        kv.set("isDeleted", false);
        if (isEnabled != null) {
            kv.set("isEnabled", isEnabled);
        }
        if (iwarehouseid == null) {
            return null;
        }
        kv.set("iWarehouseId", iwarehouseid);
        return getCommonList(kv, "iAutoId", "asc");
    }

    public Ret importExcelData(File file) {
        StringBuilder errorMsg = new StringBuilder();

        Date now = new Date();
        
        JBoltExcel jBoltExcel = JBoltExcel
                //从excel文件创建JBoltExcel实例
                .from(file)
                //设置工作表信息
                .setSheets(
                        JBoltExcelSheet.create("sheet1")
                                //设置列映射 顺序 标题名称
                                .setHeaders(
                                        JBoltExcelHeader.create("cpositioncode", "库位编码"),
                                        JBoltExcelHeader.create("cpositionname", "库位名称"),
                                        JBoltExcelHeader.create("cwhcode", "所属仓库编码"),
                                        JBoltExcelHeader.create("careacode", "所属库区编码"),
                                        JBoltExcelHeader.create("cshelvescode", "所属货架编码"),

                                        JBoltExcelHeader.create("ilength", "长"),
                                        JBoltExcelHeader.create("iwidth", "宽"),
                                        JBoltExcelHeader.create("iheight", "高"),
                                        JBoltExcelHeader.create("imaxweight", "最大重量"),
                                        JBoltExcelHeader.create("imaxbulk", "最大体积"),

                                        JBoltExcelHeader.create("cmemo", "备注")
                                )
                                //特殊数据转换器
                                .setDataChangeHandler((data, index) -> {
                                    ValidationUtils.notNull(data.get("cpositioncode"), "库位编码为空！");
                                    ValidationUtils.notNull(data.get("cpositionname"), "库位名称为空！");
                                    ValidationUtils.notNull(data.get("cwhcode"), "仓库编码为空！");

                                    ValidationUtils.isTrue(findByCpositionCode(data.getStr("cpositioncode")) == null, data.getStr("cpositioncode") + "编码重复");

                                    Warehouse warehouse = warehouseService.findByWhCode(data.getStr("cwhcode"));
                                    ValidationUtils.notNull(warehouse, data.getStr("cwhcode") + JBoltMsg.DATA_NOT_EXIST);
                                    data.change("iwarehouseid", warehouse.getIAutoId());

                                    if (isOk(data.getStr("careacode"))) {
                                        WarehouseArea warehouseArea = warehouseAreaService.findByWhAreaCode(data.getStr("careacode"));
                                        ValidationUtils.notNull(warehouse, data.getStr("careacode") + JBoltMsg.DATA_NOT_EXIST);
                                        data.change("iwarehouseareaid", warehouseArea.getIautoid());
                                    }

                                    if (isOk(data.getStr("cshelvescode"))) {
                                        WarehouseShelves warehouseShelves = warehouseShelvesService.findByCshelvesCode(data.getStr("cshelvescode"));
                                        ValidationUtils.notNull(warehouse, data.getStr("cshelvescode") + JBoltMsg.DATA_NOT_EXIST);
                                        data.change("iwarehouseshelvesid", warehouseShelves.getIautoid());
                                    }

                                    data.remove("cwhcode");
                                    data.remove("careacode");
                                    data.remove("cshelvescode");

                                    data.change("icreateby", JBoltUserKit.getUserId());
                                    data.change("ccreatename", JBoltUserKit.getUserName());
                                    data.change("dcreatetime", now);
                                    //更新
                                    data.change("iupdateby", JBoltUserKit.getUserId());
                                    data.change("cupdatename", JBoltUserKit.getUserName());
                                    data.change("dupdatetime", now);

                                    data.change("corgcode", getOrgCode());
                                    data.change("corgname", getOrgName());
                                    data.change("iorgid", getOrgId());

                                })
                                //从第三行开始读取
                                .setDataStartRow(3)
                );
        //从指定的sheet工作表里读取数据
        List<WarehousePosition> models = JBoltExcelUtil.readModels(jBoltExcel, "sheet1", WarehousePosition.class, errorMsg);
        if (notOk(models)) {
            if (errorMsg.length() > 0) {
                return fail(errorMsg.toString());
            } else {
                return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
            }
        }

        //读取数据没有问题后判断必填字段
        if (models.size() > 0) {
            tx(() -> {
                batchSave(models);
                return true;
            });

        }

        return SUCCESS;
    }

    public List<Record> options(Kv kv) {
        String iMouldsId = kv.getStr("iMouldsId");
        if (isOk(iMouldsId)) {
            return dbTemplate("warehouseposition.findByMouldsId", kv).find();
        } else {
            Sql sql = selectSql().eq("isDeleted", false).eq("isEnabled", true).orderBy("dCreateTime", false);
            Long iAutoid = kv.getLong("iWarehouseId");
            if (isOk(iAutoid)) {
                sql.eq("iWarehouseId", iAutoid);
            }
            return findRecord(sql);
        }
    }

    public List<WarehousePosition> getIdAndNameList() {
        return find("SELECT iAutoId,cPositionName FROM Bd_Warehouse_Position WHERE isDeleted = '0' ");
    }

    /**
     * 打印
     *
     * @param code
     * @return
     */
    public Kv selectPrint(String code) {
        Kv kv = Kv.by("ids", code);

        String str = "(";
        String[] split = code.split(",");
        for (String id : split) {
            str += "'" + id + "',";
        }
        str = str.substring(0, str.length() - 1);
        str += ")";
        kv.set("str", str);
        try {
            List<Record> list = dbTemplate("warehouseposition.printwarehouseposition", kv).find();
            Kv target = new Kv();
            target.set("table", list);
            return target;
        } finally {

        }

    }


}