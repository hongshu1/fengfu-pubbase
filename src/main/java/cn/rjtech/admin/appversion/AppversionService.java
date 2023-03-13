package cn.rjtech.admin.appversion;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltUserCache;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelMerge;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.base.service.BaseService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.BoolCharEnum;
import cn.rjtech.enums.PutawayEnum;
import cn.rjtech.enums.SystemTypeEnum;
import cn.rjtech.model.main.Appversion;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: AppversionService
 * @author: JFinal学院-小木
 * @date: 2020-09-16 17:03
 */
public class AppversionService extends BaseService<Appversion> {

    private final Appversion dao = new Appversion().dao();

    @Override
    protected Appversion dao() {
        return dao;
    }

    /**
     * 后台管理分页查询true
     */
    public Page<Appversion> paginateAdminList(int pageNumber, int pageSize, String sortColumn, String sortType) {
        if (sortColumn == null || sortType == null) {
            sortColumn = "createTime";
            sortType = "desc";
        }

        Okv kv = Okv.by("isdeleted", ZERO_STR);
        return paginate("*", kv, sortColumn, sortType, pageNumber, pageSize, false, false);
    }

    /**
     * 删除
     */
    public Ret delete(Integer userId, Integer id) {
        Ret ret = deleteById(id, false);
        if (ret.isOk()) {
            //添加日志
            //Appversion appversion=ret.getAs("data");
            //addDeleteSystemLog(id, userId, SystemLog.TARGETTYPE_xxx, appversion.getName());
        }
        return ret;
    }

    /**
     * 生成Excel数据 byte[]
     */
    public JBoltExcel getExcelReport() {
        return JBoltExcel
                //创建
                .create()
                //设置工作表
                .setSheets(
                        //设置工作表 列映射 顺序 标题名称
                        JBoltExcelSheet
                                .create("versions")
                                //表头映射关系
                                .setHeaders(1,
                                        JBoltExcelHeader.create("systemtype", "系统版本", 16),
                                        JBoltExcelHeader.create("putaway", "是否发布", 15),
                                        JBoltExcelHeader.create("isforced", "强制更新", 15),
                                        JBoltExcelHeader.create("versionintro", "版本介绍", 12),
                                        JBoltExcelHeader.create("versiondate", "版本日期", 20),
                                        JBoltExcelHeader.create("versioncode", "版本编码", 10),
                                        JBoltExcelHeader.create("createtime", "创建时间", 20),
                                        JBoltExcelHeader.create("createby", "创建人", 10),
                                        JBoltExcelHeader.create("updatetime", "变更时间", 20),
                                        JBoltExcelHeader.create("updateby", "变更人", 10)
                                )
                                //设置单元格合并区域
                                .setMerges(JBoltExcelMerge.create(0, 0, 9, "APP版本信息"))
                                //特殊数据转换器
                                .setDataChangeHandler((data, index) -> {
                                    data.put("systemtype", SystemTypeEnum.toEnum(data.getInt("systemtype")).getText());
                                    data.put("isforced", BoolCharEnum.toEnum(data.getStr("isforced")).getText());
                                    data.put("putaway", PutawayEnum.toEnum(data.getInt("putaway")).getText());
                                    // 将user_id转为user_name
                                    data.changeWithKey("user_id", "user_username", JBoltUserCache.me.getUserName(data.getLong("user_id")));
                                    data.changeBooleanToStr("is_new", "是", "否");
                                })
                                //设置导出的数据源 来自于数据库查询出来的Model List
                                .setModelDatas(2, getAllAppVersionList())
                );
    }

    public List<Appversion> getAllAppVersionList() {
        return find("SELECT * FROM T_Sys_AppVersion WHERE IsDeleted = ? ", ZERO_STR);
    }

    /**
     * 更新数据
     */
    public Ret update(Appversion appversion) {
        Date now = new Date();

        tx(() -> {
            Appversion dbAppversion = findById(appversion.getAutoID());
            ValidationUtils.notNull(dbAppversion, JBoltMsg.DATA_NOT_EXIST);
            ValidationUtils.isTrue(!dbAppversion.getIsDeleted(), "应用版本已被删除");

            // 构造数据
            appversion.setUpdateBy(JBoltUserKit.getUserName());
            // 发布时间
            if (!appversion.getPutaway().equals(dbAppversion.getPutaway())) {
                appversion.setPutawayTime(PutawayEnum.YES.getValue() == appversion.getPutaway() ? now : null);
            }

            ValidationUtils.isTrue(appversion.update(), ErrorMsg.UPDATE_FAILED);

            addUpdateSystemLog(appversion.getAutoID(), JBoltUserKit.getUserId(), ProjectSystemLogTargetType.APP_VERSION.getValue(), appversion.getVersionCode());
            return true;
        });

        return SUCCESS;
    }

    /**
     * 保存数据
     */
    public Ret save(Appversion appversion) {
        Date now = new Date();

        // 构造数据
        String userName = JBoltUserKit.getUserName();
        appversion.setUpdateBy(userName);
        appversion.setCreateBy(userName);
        appversion.setUpdateTime(now);
        appversion.setCreateTime(now);
        appversion.setIsDeleted(false);
        if (appversion.getPutaway() == PutawayEnum.YES.getValue()) {
            appversion.setPutawayTime(now);
        }

        // 保存数据
        tx(() -> {
            ValidationUtils.isTrue(appversion.save(), ErrorMsg.SAVE_FAILED);

            addSaveSystemLog(appversion.getAutoID(), JBoltUserKit.getUserId(), ProjectSystemLogTargetType.APP_VERSION.getValue(), appversion.getVersionCode());

            return true;
        });
        return SUCCESS;
    }

    /**
     * 改变发布状态
     */
    public Ret putaway(long autoid) {
        Date now = new Date();

        tx(() -> {
            Appversion appversion = findById(autoid);
            ValidationUtils.notNull(appversion, "APP版本为空");

            appversion.setUpdateBy(JBoltUserKit.getUserName());
            appversion.setUpdateTime(now);
            appversion.setPutaway(appversion.getPutaway() == PutawayEnum.YES.getValue() ? PutawayEnum.NO.getValue() : PutawayEnum.YES.getValue());
            appversion.setPutawayTime(appversion.getPutaway() == PutawayEnum.YES.getValue() ? now : null);

            ValidationUtils.isTrue(appversion.update(), ErrorMsg.UPDATE_FAILED);
            return true;
        });

        return SUCCESS;
    }

    /**
     * 是否强制更新
     */
    public Ret updateisforced(long autoid) {
        Date now = new Date();

        tx(() -> {
            Appversion appversion = findById(autoid);
            ValidationUtils.notNull(appversion, "APP版本为空");

            // 构造数据
            appversion.setUpdateBy(JBoltUserKit.getUserName());
            appversion.setUpdateTime(now);
            appversion.setIsForced(!appversion.getIsForced());

            ValidationUtils.isTrue(appversion.update(), ErrorMsg.UPDATE_FAILED);
            return true;
        });

        return SUCCESS;
    }

    public Appversion getAppNewVersion(String appcode) {
        return dao.findFirst("SELECT TOP 1 * FROM base_appversion WHERE IsDeleted = '0' AND AppCode = ? ORDER BY CreateTime DESC", appcode);
    }

    @Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.APP_VERSION.getValue();
    }

}