package cn.rjtech.admin.permissionbtn;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.PermissionBtn;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.service.JBoltPermissionBtnService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 菜单按钮
 *
 * @ClassName: PermissionBtnService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-11-10 13:47
 */
public class PermissionBtnService extends JBoltPermissionBtnService {

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.PERMISSION_BTN.getValue();
    }

    /**
     * 后台管理数据查询
     */
    public List<PermissionBtn> getAdminDatas(Long permissionId) {
        if (null == permissionId) {
            return CollUtil.empty(List.class);
        }

        // 创建sql对象
        Sql sql = selectSql()
                .eq("permission_id", permissionId)
                .orderBySortRank();

        return find(sql);
    }

    /**
     * 保存
     */
    public Ret save(PermissionBtn permissionBtn) {
        if (permissionBtn == null || isOk(permissionBtn.getId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //if(existsName(permissionBtn.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        permissionBtn.setSortRank(getNextSortRank());
        boolean success = permissionBtn.save();
        if (success) {
            //添加日志
            addSaveSystemLog(permissionBtn.getId(), JBoltUserKit.getUserId(), permissionBtn.getBtnName());
        }
        return ret(success);
    }

    /**
     * 更新
     */
    public Ret update(PermissionBtn permissionBtn) {
        if (permissionBtn == null || notOk(permissionBtn.getId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        //更新时需要判断数据存在
        PermissionBtn dbPermissionBtn = findById(permissionBtn.getId());
        if (dbPermissionBtn == null) {
            return fail(JBoltMsg.DATA_NOT_EXIST);
        }
        //if(existsName(permissionBtn.getName(), permissionBtn.getId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
        boolean success = permissionBtn.update();
        if (success) {
            //添加日志
            addUpdateSystemLog(permissionBtn.getId(), JBoltUserKit.getUserId(), permissionBtn.getBtnName());
        }
        return ret(success);
    }

    /**
     * 删除数据后执行的回调
     *
     * @param permissionBtn 要删除的model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    protected String afterDelete(PermissionBtn permissionBtn, Kv kv) {
        addDeleteSystemLog(permissionBtn.getId(), JBoltUserKit.getUserId(), permissionBtn.getBtnName());
        return null;
    }

    /**
     * 检测是否可以删除
     *
     * @param permissionBtn model
     * @param kv            携带额外参数一般用不上
     */
    @Override
    public String checkInUse(PermissionBtn permissionBtn, Kv kv) {
        //这里用来覆盖 检测是否被其它表引用
        return null;
    }

    /**
     * 上移
     */
    public Ret up(Long id) {
        PermissionBtn permissionBtn = findById(id);
        if (permissionBtn == null) {
            return fail("数据不存在或已被删除");
        }
        Integer rank = permissionBtn.getSortRank();
        if (rank == null || rank <= 0) {
            return fail("顺序需要初始化");
        }
        if (rank == 1) {
            return fail("已经是第一个");
        }
        PermissionBtn upPermissionBtn = findFirst(Okv.by("sort_rank", rank - 1));
        if (upPermissionBtn == null) {
            return fail("顺序需要初始化");
        }
        upPermissionBtn.setSortRank(rank);
        permissionBtn.setSortRank(rank - 1);

        upPermissionBtn.update();
        permissionBtn.update();
        return SUCCESS;
    }

    /**
     * 下移
     */
    public Ret down(Long id) {
        PermissionBtn permissionBtn = findById(id);
        if (permissionBtn == null) {
            return fail("数据不存在或已被删除");
        }
        Integer rank = permissionBtn.getSortRank();
        if (rank == null || rank <= 0) {
            return fail("顺序需要初始化");
        }
        int max = getCount();
        if (rank == max) {
            return fail("已经是最后已一个");
        }
        PermissionBtn upPermissionBtn = findFirst(Okv.by("sort_rank", rank + 1));
        if (upPermissionBtn == null) {
            return fail("顺序需要初始化");
        }
        upPermissionBtn.setSortRank(rank);
        permissionBtn.setSortRank(rank + 1);

        upPermissionBtn.update();
        permissionBtn.update();
        return SUCCESS;
    }

    /**
     * 初始化排序
     */
    public Ret initSortRank() {
        List<PermissionBtn> allList = findAll();
        if (allList.size() > 0) {
            for (int i = 0; i < allList.size(); i++) {
                allList.get(i).setSortRank(i + 1);
            }
            batchUpdate(allList);
        }
        //添加日志
        addUpdateSystemLog(null, JBoltUserKit.getUserId(), "所有数据", "的顺序:初始化所有");
        return SUCCESS;
    }

    /**
     * 生成excel导入使用的模板
     */
    public JBoltExcel getImportExcelTpl() {
        return JBoltExcel
                //创建
                .create()
                .setSheets(
                        JBoltExcelSheet.create()
                                //设置列映射 顺序 标题名称 不处理别名
                                .setHeaders(1, false,
                                        JBoltExcelHeader.create("权限资源ID", 15),
                                        JBoltExcelHeader.create("视图类型：1. 列表 2. 表单", 15),
                                        JBoltExcelHeader.create("按钮编码", 15),
                                        JBoltExcelHeader.create("按钮名称", 15),
                                        JBoltExcelHeader.create("显示位置：1. 工具栏 2. 列表内", 15),
                                        JBoltExcelHeader.create("执行方法，用于按钮调用Js方法", 15),
                                        JBoltExcelHeader.create("按钮说明", 15),
                                        JBoltExcelHeader.create("顺序号", 15)
                                )
                );
    }

    /**
     * 读取excel文件
     */
    public Ret importExcel(File file) {
        StringBuilder errorMsg = new StringBuilder();
        JBoltExcel jBoltExcel = JBoltExcel
                //从excel文件创建JBoltExcel实例
                .from(file)
                //设置工作表信息
                .setSheets(
                        JBoltExcelSheet.create()
                                //设置列映射 顺序 标题名称
                                .setHeaders(1,
                                        JBoltExcelHeader.create("permission_id", "权限资源ID"),
                                        JBoltExcelHeader.create("view_type", "视图类型：1. 列表 2. 表单"),
                                        JBoltExcelHeader.create("btn_code", "按钮编码"),
                                        JBoltExcelHeader.create("btn_name", "按钮名称"),
                                        JBoltExcelHeader.create("position", "显示位置：1. 工具栏 2. 列表内"),
                                        JBoltExcelHeader.create("func", "执行方法，用于按钮调用Js方法"),
                                        JBoltExcelHeader.create("tooltip", "按钮说明"),
                                        JBoltExcelHeader.create("sort_rank", "顺序号")
                                )
                                //从第三行开始读取
                                .setDataStartRow(2)
                );
        //从指定的sheet工作表里读取数据
        List<PermissionBtn> permissionBtns = JBoltExcelUtil.readModels(jBoltExcel, 1, PermissionBtn.class, errorMsg);
        if (notOk(permissionBtns)) {
            if (errorMsg.length() > 0) {
                return fail(errorMsg.toString());
            } else {
                return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
            }
        }
        
        //执行批量操作
        boolean success = tx(() -> {
            batchSave(permissionBtns);
            return true;
        });

        if (!success) {
            return fail(JBoltMsg.DATA_IMPORT_FAIL);
        }
        return SUCCESS;
    }

    /**
     * 生成要导出的Excel
     */
    public JBoltExcel exportExcel(List<PermissionBtn> datas) {
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
                                        JBoltExcelHeader.create("permission_id", "权限资源ID", 15),
                                        JBoltExcelHeader.create("view_type", "视图类型：1. 列表 2. 表单", 15),
                                        JBoltExcelHeader.create("btn_code", "按钮编码", 15),
                                        JBoltExcelHeader.create("btn_name", "按钮名称", 15),
                                        JBoltExcelHeader.create("position", "显示位置：1. 工具栏 2. 列表内", 15),
                                        JBoltExcelHeader.create("func", "执行方法，用于按钮调用Js方法", 15),
                                        JBoltExcelHeader.create("tooltip", "按钮说明", 15),
                                        JBoltExcelHeader.create("sort_rank", "顺序号", 15)
                                )
                                //设置导出的数据源 来自于数据库查询出来的Model List
                                .setModelDatas(2, datas)
                );
    }

    public void deleteByMultiIds(Object[] delete) {
        delete("DELETE FROM base_permission_btn WHERE id IN (" + ArrayUtil.join(delete, COMMA, "'", "'") + ")");
    }

    public List<Record> findRecordByPermissionId(long permissionId) {
        return findRecord("SELECT * FROM base_permission_btn WHERE permission_id = ? ORDER BY sort_rank ", true, permissionId);
    }

    public List<Record> findMenuBtnWithAlias(Long applicationId, String checkedIds) {
        Okv para = Okv.by("applicationId", applicationId)
                .set("checkedIds", checkedIds);

        return dbTemplate("permissionbtn.findMenuBtnWithAlias", para).find();
    }

    public boolean existsPermissionId(Long permissionId) {
        return null != queryColumn(selectSql().select("1").eq("permission_id", permissionId).first());
    }

}