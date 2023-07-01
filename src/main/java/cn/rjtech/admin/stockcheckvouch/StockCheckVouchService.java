package cn.rjtech.admin.stockcheckvouch;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.StockCheckVouch;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 盘点单
 *
 * @ClassName: StockCheckVouchService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-21 10:47
 */
public class StockCheckVouchService extends BaseService<StockCheckVouch> {

    private final StockCheckVouch dao = new StockCheckVouch().dao();

    @Override
    protected StockCheckVouch dao() {
        return dao;
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    /**
     * 后台管理数据查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页几条数据
     * @param CheckType  盘点方式;0 明盘 1 暗盘
     */
    public Page<StockCheckVouch> getAdminDatas(int pageNumber, int pageSize, String CheckType) {
        //创建sql对象
        Sql sql = selectSql().page(pageNumber, pageSize);
        //sql条件处理
        sql.eq("CheckType", CheckType);
        //排序
        sql.desc("Autoid");
        return paginate(sql);
    }

    public Page<Record> pageList(Kv kv) {
        Page<Record> paginate = dbTemplate("stockcheckvouch.datas", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
        return paginate;
    }

    /**
     * 保存
     */
    public Ret save(StockCheckVouch stockCheckVouch) {
        if (stockCheckVouch == null || isOk(stockCheckVouch.getAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(stockCheckVouch.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = stockCheckVouch.save();
        if (success) {
            //添加日志
            //addSaveSystemLog(stockCheckVouch.getAutoid(), JBoltUserKit.getUserId(), stockCheckVouch.getName());
        }
        return ret(success);
    }

    /*
     * 保存整体表单
     * */
    public Ret saveSubmit(Kv kv) {
        return null;
    }

    /*
     * 新增
     * */
    public Ret addFormSave(Kv kv) {
        StockCheckVouch stockCheckVouch = new StockCheckVouch();
        saveStockCheckVouchModel(stockCheckVouch, kv);
        boolean save = stockCheckVouch.save();
        return ret(save);
    }

    /*
    * 主表传参
    * */
    public void saveStockCheckVouchModel(StockCheckVouch stockCheckVouch, Kv kv) {
        Date date = new Date();
        String userName = JBoltUserKit.getUserName();
        stockCheckVouch.setOrganizeCode(getOrgCode());
        stockCheckVouch.setCheckType(kv.getStr("checktype"));
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmss");
        stockCheckVouch.setBillNo(sdf.format(date)); //todo 等编码规则配置功能做好了，通过编码规则生成
        stockCheckVouch.setBillDate(date.toString());
        stockCheckVouch.setCheckPerson(kv.getStr("checkperson"));
        stockCheckVouch.setWhCode(kv.getStr("whcode"));
        //创建人
        stockCheckVouch.setIcreateby(JBoltUserKit.getUserId());
        stockCheckVouch.setCcreatename(userName);
        stockCheckVouch.setDcreatetime(date);
        //更新人
        stockCheckVouch.setIupdateby(JBoltUserKit.getUserId());
        stockCheckVouch.setCupdatename(userName);
        stockCheckVouch.setDupdatetime(date);
        stockCheckVouch.setCcreatename(userName);
        stockCheckVouch.setAutoId(JBoltSnowflakeKit.me.nextId());
    }

    /**
     * 更新
     */
    public Ret update(StockCheckVouch stockCheckVouch) {
        if (stockCheckVouch == null || notOk(stockCheckVouch.getAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        StockCheckVouch dbStockCheckVouch = findById(stockCheckVouch.getAutoId());
        if (dbStockCheckVouch == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(stockCheckVouch.getName(), stockCheckVouch.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = stockCheckVouch.update();
        if (success) {
            //添加日志
            //addUpdateSystemLog(stockCheckVouch.getAutoid(), JBoltUserKit.getUserId(), stockCheckVouch.getName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param stockCheckVouch 要删除的model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(StockCheckVouch stockCheckVouch, Kv kv) {
        //addDeleteSystemLog(stockCheckVouch.getAutoid(), JBoltUserKit.getUserId(),stockCheckVouch.getName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param stockCheckVouch model
     * @param kv              携带额外参数一般用不上
     */
    @Override
    public String checkInUse(StockCheckVouch stockCheckVouch, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /*
     * 加载继续盘点页面的数据
     * */
    public List<Record> keepCheckVouchDatas() {
        return null;
    }

    public List<Record> findAll(String sortColumn, String sortType, Kv kv){
        kv.set("orgId", getOrgId());
        return dbTemplate("stockcheckvouch.list", kv.set("sortColumn", sortColumn).set("sortType", sortType)).find();
    }

    /**
     * 生成要导出的Excel
     * @return
     */
    public JBoltExcel exportExcel(List<Record> datas) {
        return JBoltExcel
            //创建
            .create()
            //设置工作表
            .setSheets(
                //设置工作表 列映射 顺序 标题名称
                JBoltExcelSheet
                    .create()
                    //表头映射关系
                    .setHeaders(1,
                        JBoltExcelHeader.create("","", 20),
                        JBoltExcelHeader.create("","", 20),
                        JBoltExcelHeader.create("","", 20),
                        JBoltExcelHeader.create("","", 20),
                        JBoltExcelHeader.create("","", 20),
                        JBoltExcelHeader.create("","", 20),
                        JBoltExcelHeader.create("","", 20),
                        JBoltExcelHeader.create("","", 20),
                        JBoltExcelHeader.create("","", 20),
                        JBoltExcelHeader.create("","", 20),
                        JBoltExcelHeader.create("","", 20),
                        JBoltExcelHeader.create("","", 20)
                    )
                    //设置导出的数据源 来自于数据库查询出来的Model List
                    .setRecordDatas(2,datas)
            );
    }

}