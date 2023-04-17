package cn.jbolt._admin.permission;
/**
 * 权限定义KEY  用于在注解里使用
 * 举例 @CheckPermission(PermissionKey.USER)
 * 此文件由JBoltGeneratorGUI生成 请勿手动修改
 */
public class PermissionKey {
    /**
	 * 空权限
	 */
	public static final String NONE = "NONE";
	/**
	 * 操作台
	 */
	public static final String DASHBOARD = "dashboard";
	/**
	 * 平台管理
	 */
	public static final String PLATFORMMGR = "platformmgr";
	/**
	 * 用户管理
	 */
	public static final String USER = "user";
	/**
	 * 部门管理
	 */
	public static final String DEPT = "dept";
	/**
	 * 岗位管理
	 */
	public static final String POST = "post";
	/**
	 * 开发管理
	 */
	public static final String ROLE_PERMISSION_MENU = "role_permission_menu";
	/**
	 * 角色管理
	 */
	public static final String ROLE = "role";
	/**
	 * 顶部导航
	 */
	public static final String TOPNAV = "topnav";
	/**
	 * 菜单管理
	 */
	public static final String PERMISSION = "permission";
	/**
	 * 数据字典
	 */
	public static final String DICTIONARY = "dictionary";
	/**
	 * 全局参数
	 */
	public static final String GLOBALCONFIG = "globalconfig";
	/**
	 * 系统通知
	 */
	public static final String SYS_NOTICE = "sys_notice";
	/**
	 * 文件管理
	 */
	public static final String QINIU_CONFIG = "qiniu_config";
	/**
	 * 七牛账号
	 */
	public static final String QINIU = "qiniu";
	/**
	 * 七牛Bucket
	 */
	public static final String QINIU_BUCKET = "qiniu_bucket";
	/**
	 * 系统监控
	 */
	public static final String JBOLT_MONITOR = "jbolt_monitor";
	/**
	 * Druid数据库监控
	 */
	public static final String DRUID_MONITOR = "druid_monitor";
	/**
	 * 服务器监控
	 */
	public static final String JBOLT_SERVER_MONITOR = "jbolt_server_monitor";
	/**
	 * 日志监控
	 */
	public static final String JBOLT_LOG_MONITOR = "jbolt_log_monitor";
	/**
	 * 登录日志
	 */
	public static final String JBOLT_LOGIN_LOG = "jbolt_login_log";
	/**
	 * 关键操作日志
	 */
	public static final String SYSTEMLOG = "systemlog";
	/**
	 * 在线用户
	 */
	public static final String ONLINE_USER = "online_user";
	/**
	 * 开发平台
	 */
	public static final String DEV_PLATFORM = "dev_platform";
	/**
	 * 应用中心
	 */
	public static final String APPLICATION = "application";
	/**
	 * 微信公众平台
	 */
	public static final String WECHAT_MPINFO = "wechat_mpinfo";
	/**
	 * 基础配置
	 */
	public static final String WECHAT_CONFIG_BASEMGR = "wechat_config_basemgr";
	/**
	 * 菜单配置
	 */
	public static final String WECHAT_MENU = "wechat_menu";
	/**
	 * 支付配置
	 */
	public static final String WECHAT_CONFIG_PAYMGR = "wechat_config_paymgr";
	/**
	 * 关注回复
	 */
	public static final String WECHAT_AUTOREPLY_SUBSCRIBE = "wechat_autoreply_subscribe";
	/**
	 * 关键词回复
	 */
	public static final String WECHAT_AUTOREPLY_KEYWORDS = "wechat_autoreply_keywords";
	/**
	 * 默认回复
	 */
	public static final String WECHAT_AUTOREPLY_DEFAULT = "wechat_autoreply_default";
	/**
	 * 素材库
	 */
	public static final String WECHAT_MEDIA = "wechat_media";
	/**
	 * 用户管理
	 */
	public static final String WECHAT_USER = "wechat_user";
	/**
	 * 其它配置
	 */
	public static final String WECHAT_CONFIG_EXTRAMGR = "wechat_config_extramgr";
	/**
	 * 报表设计器
	 */
	public static final String UREPORT_DESIGNER = "ureport_designer";
	/**
	 * 打印设计器
	 */
	public static final String HIPRINT_DESIGN = "hiprint_design";
	/**
	 * 代码生成器
	 */
	public static final String JBOLT_CODE_GEN = "jbolt_code_gen";
	/**
	 * 独立逻辑权限
	 */
	public static final String LOGIC_PERMISSION = "logic_permission";
	/**
	 * Ureport报表查看权
	 */
	public static final String UREPORT_DETAIL = "ureport_detail";
	/**
	 * 邮件接收配置
	 */
	public static final String EMAIL_RECEIVER = "email_receiver";
	/**
	 * 系统管理
	 */
	public static final String  SYSTEMMGR = " systemmgr";
	/**
	 * 组织管理
	 */
	public static final String ORGMGR = "orgmgr";
	/**
	 * 消息管理
	 */
	public static final String MESSAGEMMR = "messagemmr";
	/**
	 * 组织管理
	 */
	public static final String ORGMMR1 = "orgmmr1";
	/**
	 * 权限管理
	 */
	public static final String ROLEMR = "rolemr";
	/**
	 * 组织建模
	 */
	public static final String FUNC1 = "func1";
	/**
	 * 排产建模
	 */
	public static final String SCHEDULE = "schedule";
	/**
	 * 生产班次
	 */
	public static final String WORKSHIFTM = "workshiftm";
	/**
	 * 排程工作日历
	 */
	public static final String CALENDAR = "calendar";
	/**
	 * 新增班次
	 */
	public static final String WORKSHIFTM_ADD = "workshiftm_add";
	/**
	 * 修改班次
	 */
	public static final String WORKSHIFTM_EDIT = "workshiftm_edit";
	/**
	 * 删除班次
	 */
	public static final String WORKSHIFTM_DELETE = "workshiftm_delete";
	/**
	 * 新增日历
	 */
	public static final String WORKCALENDARM_ADD = "workcalendarm_add";
	/**
	 * 修改日历
	 */
	public static final String WORKCALENDARM_EDIT = "workcalendarm_edit";
	/**
	 * 删除日历
	 */
	public static final String WORKCALENDARM_DELETE = "workcalendarm_delete";
	/**
	 * 导出
	 */
	public static final String WORKCALENDARM_EXPORT = "workcalendarm_export";
	/**
	 * 导入
	 */
	public static final String WORKCALENDARM_IMPORT = "workcalendarm_import";
	/**
	 * 导出
	 */
	public static final String WORKSHIFTM_EXPORT = "workshiftm_export";
	/**
	 * 导入
	 */
	public static final String WORKSHIFTM_IMPORT = "workshiftm_import";
	/**
	 * 应用管理
	 */
	public static final String RJ_APPLICATION = "rj_application";
	/**
	 * 产线档案
	 */
	public static final String WORKREGIONM = "workregionm";
	/**
	 * APS高级排程
	 */
	public static final String SCHEDULED_APS = "scheduled_aps";
	/**
	 * 往来单位
	 */
	public static final String SUPPLIER_CUSTOMER = "supplier_customer";
	/**
	 * 客户档案
	 */
	public static final String CUSTOMER = "customer";
	/**
	 * 供应商档案
	 */
	public static final String VENDORCLASS = "vendorclass";
	/**
	 * APS计划排程
	 */
	public static final String SCHEDUBASEPLAN = "schedubaseplan";
	/**
	 * 料品管理
	 */
	public static final String MATERIAL = "material";
	/**
	 * 计量单位
	 */
	public static final String UOMCLASS = "uomclass";
	/**
	 * U9生产计划
	 */
	public static final String SCHEDUSOURCEORDER = "schedusourceorder";
	/**
	 * 组合件关系
	 */
	public static final String COMBINE = "combine";
	/**
	 * 仓库建模
	 */
	public static final String WAREHOUSE_MODEL = "warehouse_model";
	/**
	 * 仓库档案
	 */
	public static final String WAREHOUSE = "warehouse";
	/**
	 * 库区档案
	 */
	public static final String WAREHOUSE_AREA = "warehouse_area";
	/**
	 * 货架档案
	 */
	public static final String WAREHOUSE_SHELVES = "warehouse_shelves";
	/**
	 * 库位档案
	 */
	public static final String WAREHOUSE_POSITION = "warehouse_position";
	/**
	 * 生产建模
	 */
	public static final String PRODUCTION_MODEL = "production_model";
	/**
	 * 工种档案
	 */
	public static final String WORKCLASS = "workclass";
	/**
	 * 工序档案
	 */
	public static final String OPERATION = "operation";
	/**
	 * 车间执行
	 */
	public static final String WORKSHOP_EXECUTE = "workshop_execute";
	/**
	 * 生产工单管理
	 */
	public static final String PRODUCTION_TICKETS = "production_tickets";
	/**
	 * 生产工位派工
	 */
	public static final String PRODUCTION_PRODUCTION = "production_production";
	/**
	 * 分条加工
	 */
	public static final String CUTSTRIPPROCESS = "cutstripprocess";
	/**
	 * 片料加工
	 */
	public static final String SLICEINVPROCESS = "sliceinvprocess";
	/**
	 * 板料加工
	 */
	public static final String PIECEINVPROCESS = "pieceinvprocess";
	/**
	 * 生产工序报工
	 */
	public static final String PRODUCTION_REPORT = "production_report";
	/**
	 * 生产完工
	 */
	public static final String PRODUCTION_COMPLETE = "production_complete";
	/**
	 * 返修工单管理
	 */
	public static final String REWORKORDER = "reworkorder";
	/**
	 * 委外工单管理
	 */
	public static final String PMISSUEM = "pmissuem";
	/**
	 * 员工计件工资
	 */
	public static final String PIECE_RATE = "piece_rate";
	/**
	 * 设备管理
	 */
	public static final String EQUIPMENT_MANAGE = "equipment_manage";
	/**
	 * 设备档案
	 */
	public static final String EQUIPMENT = "equipment";
	/**
	 * 质量管理
	 */
	public static final String QUALITY = "quality";
	/**
	 * 点检事项
	 */
	public static final String EQUIPMENTQC = "equipmentqc";
	/**
	 * 点检参数
	 */
	public static final String SPOTCHECKPARAM = "spotcheckparam";
	/**
	 * 点检适用标准
	 */
	public static final String INVENTORYSPOTCHECKFORM = "inventoryspotcheckform";
	/**
	 * 检验配置
	 */
	public static final String QC_SETTING = "qc_setting";
	/**
	 * 检验参数
	 */
	public static final String PARAMETER = "parameter";
	/**
	 * 检验标准
	 */
	public static final String BASIS = "basis";
	/**
	 * 专用检验标准
	 */
	public static final String RANGEBASIS = "rangebasis";
	/**
	 * 巡检检验
	 */
	public static final String PATROLRECORD = "patrolrecord";
	/**
	 * 专检检验
	 */
	public static final String SPECIAL_INSPECTION = "special_inspection";
	/**
	 * 强度检验
	 */
	public static final String STRENGTH_INSPECTION = "strength_inspection";
	/**
	 * 首中末检验
	 */
	public static final String FIRST_CENTRE_LAST = "first_centre_last";
	/**
	 * 模具首检任务
	 */
	public static final String MOULDSFIRSTDOC = "mouldsfirstdoc";
	/**
	 * 编辑
	 */
	public static final String WAREHOUSE_EDIT = "warehouse_edit";
	/**
	 * 删除
	 */
	public static final String WAREHOUSE_DELETE = "warehouse_delete";
	/**
	 * 新增
	 */
	public static final String WAREHOUSE_AREA_ADD = "warehouse_area_add";
	/**
	 * 编辑
	 */
	public static final String WAREHOUSE_AREA_EDIT = "warehouse_area_edit";
	/**
	 * 删除
	 */
	public static final String WAREHOUSE_AREA_DELETE = "warehouse_area_delete";
	/**
	 * 导出
	 */
	public static final String WAREHOUSE_EXPORT = "warehouse_export";
	/**
	 * 导入
	 */
	public static final String WAREHOUSE_IMPORT = "warehouse_import";
	/**
	 * 功能权限
	 */
	public static final String MENU_PERMISSION = "menu_permission";
	/**
	 * 数据权限
	 */
	public static final String DATA_PERMISSION = "data_permission";
	/**
	 * 业务对象
	 */
	public static final String BUS_OBJECT = "bus_object";
	/**
	 * 检验标准文件
	 */
	public static final String BASISDRAWING = "basisdrawing";
	/**
	 * 生产派工单明细
	 */
	public static final String ASSIGNORDERCHILDINV = "assignorderchildinv";
	/**
	 * 客户档案明细
	 */
	public static final String CUSTOMERD = "customerd";
	/**
	 * 生产返工单工序
	 */
	public static final String MOREWORKOPERATION = "moreworkoperation";
	/**
	 * 生产返工单工序物料集
	 */
	public static final String MOREWORKOPERATIONINVC = "moreworkoperationinvc";
	/**
	 * 分类
	 */
	public static final String PARAMETERCLASS = "parameterclass";
	/**
	 * 巡检记录参数
	 */
	public static final String PATROLRECORDPARAMETER = "patrolrecordparameter";
	/**
	 * 巡检记录不良细表
	 */
	public static final String PATROLRECORDSCRAPD = "patrolrecordscrapd";
	/**
	 * 巡检记录不良主表
	 */
	public static final String PATROLRECORDSCRAPM = "patrolrecordscrapm";
	/**
	 * 检验配置制程检验报工
	 */
	public static final String QCSETTINGREPORT = "qcsettingreport";
	/**
	 * 检验适用标准关联
	 */
	public static final String RANGEBASISLINE = "rangebasisline";
	/**
	 * 返修工单工艺
	 */
	public static final String REWORKORDERROUTING = "reworkorderrouting";
	/**
	 * 工序报工条码
	 */
	public static final String ROUTEREPORTBARCODE = "routereportbarcode";
	/**
	 * 工序报工不良明细
	 */
	public static final String ROUTEREPORTSCRAPD = "routereportscrapd";
	/**
	 * 工序报工不良
	 */
	public static final String ROUTEREPORTSCRAPM = "routereportscrapm";
	/**
	 * 报工记录隔离明细
	 */
	public static final String ROUTEREPORTSEPARATED = "routereportseparated";
	/**
	 * 报工记录隔离
	 */
	public static final String ROUTEREPORTSEPARATEM = "routereportseparatem";
	/**
	 * 排程资源表
	 */
	public static final String SCHEDUBASEPLANEQUIPCODE = "schedubaseplanequipcode";
	/**
	 * 排程物料临时表
	 */
	public static final String SCHEDUBASETEMP = "schedubasetemp";
	/**
	 * APS排程锁定
	 */
	public static final String SCHEDURELATIONLOCK = "schedurelationlock";
	/**
	 * APS排程来源
	 */
	public static final String SCHEDUSOURCE = "schedusource";
	/**
	 * 片料加工余料
	 */
	public static final String SLICEINVPROCESSSTUFF = "sliceinvprocessstuff";
	/**
	 * 新增
	 */
	public static final String WAREHOUSE_SHELVES_ADD = "warehouse_shelves_add";
	/**
	 * 编辑
	 */
	public static final String WAREHOUSE_SHELVES_EDIT = "warehouse_shelves_edit";
	/**
	 * 删除
	 */
	public static final String WAREHOUSE_SHELVES_DELETE = "warehouse_shelves_delete";
	/**
	 * 导出
	 */
	public static final String WAREHOUSE_SHELVES_EXPORT = "warehouse_shelves_export";
	/**
	 * 导入
	 */
	public static final String WAREHOUSE_SHELVES_IMPORT = "warehouse_shelves_import";
	/**
	 * 打印
	 */
	public static final String WAREHOUSE_SHELVES_PRINT = "warehouse_shelves_print";
	/**
	 * 新增
	 */
	public static final String WAREHOUSE_POSITION_ADD = "warehouse_position_add";
	/**
	 * 编辑
	 */
	public static final String WAREHOUSE_POSITION_EDIT = "warehouse_position_edit";
	/**
	 * 删除
	 */
	public static final String WAREHOUSE_POSITION_DELETE = "warehouse_position_delete";
	/**
	 * 导出
	 */
	public static final String WAREHOUSE_POSITION_EXPORT = "warehouse_position_export";
	/**
	 * 导入
	 */
	public static final String WAREHOUSE_POSITION_IMPORT = "warehouse_position_import";
	/**
	 * 打印
	 */
	public static final String WAREHOUSE_POSITION_PRINT = "warehouse_position_print";
	/**
	 * 新增
	 */
	public static final String WORKREGIONM_ADD = "workregionm_add";
	/**
	 * 新增
	 */
	public static final String CUSTOMER_ADD = "customer_add";
	/**
	 * 编辑
	 */
	public static final String CUSTOMER_EDIT = "customer_edit";
	/**
	 * 删除
	 */
	public static final String CUSTOMER_DELETE = "customer_delete";
	/**
	 * 导出
	 */
	public static final String CUSTOMER_EXPORT = "customer_export";
	/**
	 * 导入
	 */
	public static final String CUSTOMER_IMPORT = "customer_import";
	/**
	 * 新增
	 */
	public static final String VENDOR_ADD = "vendor_add";
	/**
	 * 编辑
	 */
	public static final String VENDOR_EDIT = "vendor_edit";
	/**
	 * 删除
	 */
	public static final String VENDOR_DELETE = "vendor_delete";
	/**
	 * 导出
	 */
	public static final String VENDOR_EXPORT = "vendor_export";
	/**
	 * 导入
	 */
	public static final String VENDOR_IMPORT = "vendor_import";
	/**
	 * 新增
	 */
	public static final String UOMCLASS_ADD = "uomclass_add";
	/**
	 * 编辑
	 */
	public static final String UOMCLASS_EDIT = "uomclass_edit";
	/**
	 * 删除
	 */
	public static final String UOMCLASS_DELETE = "uomclass_delete";
	/**
	 * 导出
	 */
	public static final String UOMCLASS_EXPORT = "uomclass_export";
	/**
	 * 导入
	 */
	public static final String UOMCLASS_IMPORT = "uomclass_import";
	/**
	 * 新增/编辑
	 */
	public static final String BOMCOMPARE_ADDEDIT = "bomcompare_addEdit";
	/**
	 * 删除
	 */
	public static final String BOMCOMPARE_DELETE = "bomcompare_delete";
	/**
	 * 导出
	 */
	public static final String BOMCOMPARE_EXPORT = "bomcompare_export";
	/**
	 * 导入
	 */
	public static final String BOMCOMPARE_IMPORT = "bomcompare_import";
	/**
	 * 新增
	 */
	public static final String COMBINE_ADD = "combine_add";
	/**
	 * 导出
	 */
	public static final String COMBINE_EXPORT = "combine_export";
	/**
	 * 导入
	 */
	public static final String COMBINE_IMPORT = "combine_import";
	/**
	 * 新增
	 */
	public static final String WORKCLASS_ADD = "workclass_add";
	/**
	 * 编辑
	 */
	public static final String WORKCLASS_EDIT = "workclass_edit";
	/**
	 * 导出
	 */
	public static final String WORKCLASS_EXPORT = "workclass_export";
	/**
	 * 导入
	 */
	public static final String WORKCLASS_IMPORT = "workclass_import";
	/**
	 * 删除
	 */
	public static final String WORKCLASS_DELETE = "workclass_delete";
	/**
	 * 新增
	 */
	public static final String OPERATION_ADD = "operation_add";
	/**
	 * 编辑
	 */
	public static final String OPERATION_EDIT = "operation_edit";
	/**
	 * 删除
	 */
	public static final String OPERATION_DELETE = "operation_delete";
	/**
	 * 导出
	 */
	public static final String OPERATION_EXPORT = "operation_export";
	/**
	 * 导入
	 */
	public static final String OPERATION_IMPORT = "operation_import";
	/**
	 * 新增
	 */
	public static final String EQUIPMENT_ADD = "equipment_add";
	/**
	 * 编辑
	 */
	public static final String EQUIPMENT_EDIT = "equipment_edit";
	/**
	 * 删除
	 */
	public static final String EQUIPMENT_DELETE = "equipment_delete";
	/**
	 * 新增
	 */
	public static final String SPOTCHECKPARAM_ADD = "spotcheckparam_add";
	/**
	 * 导出
	 */
	public static final String SPOTCHECKPARAM_EXPORT = "spotcheckparam_export";
	/**
	 * 导入
	 */
	public static final String SPOTCHECKPARAM_IMPORT = "spotcheckparam_import";
	/**
	 * 新增
	 */
	public static final String INVENTORYSPOTCHECKFORM_ADD = "inventoryspotcheckform_add";
	/**
	 * 导出
	 */
	public static final String INVENTORYSPOTCHECKFORM_EXPORT = "inventoryspotcheckform_export";
	/**
	 * 导入
	 */
	public static final String INVENTORYSPOTCHECKFORM_IMPORT = "inventoryspotcheckform_import";
	/**
	 * 采购管理
	 */
	public static final String PHE_PO_MANAGE = "phe_po_manage";
	/**
	 * 送货管理
	 */
	public static final String PHE_DELIVERY_MANAGE = "phe_delivery_manage";
	/**
	 * 采购订单
	 */
	public static final String PHE_PO_HEADER = "phe_po_header";
	/**
	 * 采购送货单
	 */
	public static final String SRM_PO_DELIVERY = "srm_po_delivery";
	/**
	 * 片料委外送货单
	 */
	public static final String SRM_FLAKE_OM_DELIVERY = "srm_flake_om_delivery";
	/**
	 * 委外送货单
	 */
	public static final String SRM_OM_DELIVERY = "srm_om_delivery";
	/**
	 * 首末检验记录文件
	 */
	public static final String FIRSTLASTRECORDDRAWING = "firstlastrecorddrawing";
	/**
	 * 收料管理
	 */
	public static final String SL = "sl";
	/**
	 * 编辑
	 */
	public static final String SCHEDUSOURCEORDER_EDIT = "schedusourceorder_edit";
	/**
	 * 发料管理
	 */
	public static final String FL = "fl";
	/**
	 * 导入
	 */
	public static final String SCHEDUSOURCEORDER_IMPORT = "schedusourceorder_import";
	/**
	 * u9生产计划同步
	 */
	public static final String SCHEDUSOURCEORDER_U9_SYNC = "schedusourceorder_u9_sync";
	/**
	 * 排程计划同步
	 */
	public static final String SCHEDUSOURCEORDER_SCHEDULE_SYNC = "schedusourceorder_schedule_sync";
	/**
	 * 卷料采购入库
	 */
	public static final String JL_INSTORE = "JL_InStore";
	/**
	 * 采购入库
	 */
	public static final String PUINSTORE = "puinstore";
	/**
	 * 片料委外入库
	 */
	public static final String PLPUDELIVERYDETAIL = "plpudeliverydetail";
	/**
	 * 点检标准行
	 */
	public static final String SPOT_CHECK_STANDARDS = "spot_check_standards";
	/**
	 * 委外入库
	 */
	public static final String PUDELIVERYDETAIL = "pudeliverydetail";
	/**
	 * 完工入库
	 */
	public static final String PRODUCTION_IN = "production_in";
	/**
	 * 生产备料
	 */
	public static final String APPVOUCHDETAIL = "appvouchdetail";
	/**
	 * 生产物料配送
	 */
	public static final String PRODUCTION_MATERIAL_DISTRIBUTION = "PRODUCTION_MATERIAL_DISTRIBUTION";
	/**
	 * 生产领料
	 */
	public static final String PRODUCTION_PICKING = "PRODUCTION_PICKING";
	/**
	 * 卷料采购退货
	 */
	public static final String COILED_MATERIAL_PURCHASE_RETURN = "COILED_MATERIAL_PURCHASE_RETURN";
	/**
	 * 采购退货
	 */
	public static final String PURCHASE_RETURN = "purchase_return";
	/**
	 * 委外发料
	 */
	public static final String VIEWPMISSUERECORD = "viewpmissuerecord";
	/**
	 * 杂发领料
	 */
	public static final String OTHER_OUT = "other_out";
	/**
	 * 发货管理
	 */
	public static final String FH = "fh";
	/**
	 * 导出
	 */
	public static final String PUINSTORE_EXPORT = "puinstore_export";
	/**
	 * 导出
	 */
	public static final String PLPUDELIVERYDETAIL_EXPORT = "plpudeliverydetail_export";
	/**
	 * 导出
	 */
	public static final String PUDELIVERYDETAIL_EXPORT = "pudeliverydetail_export";
	/**
	 * 导出
	 */
	public static final String PRODUCTION_IN_EXPORT = "production_in_export";
	/**
	 * 调入单
	 */
	public static final String TRANSFERIN = "transferin";
	/**
	 * 新增
	 */
	public static final String QC_SETTING_ADD = "qc_setting_add";
	/**
	 * 编辑
	 */
	public static final String QC_SETTING_EDIT = "qc_setting_edit";
	/**
	 * 删除
	 */
	public static final String QC_SETTING_DELETE = "qc_setting_delete";
	/**
	 * 设备报修
	 */
	public static final String API_REPAIRAPPLYRECORD = "api_repairapplyrecord";
	/**
	 * 新增
	 */
	public static final String PARAMETER_ADD = "parameter_add";
	/**
	 * 编辑
	 */
	public static final String PARAMETER_EDIT = "parameter_edit";
	/**
	 * 删除
	 */
	public static final String PARAMETER_DELETE = "parameter_delete";
	/**
	 * 新增
	 */
	public static final String BASIS_ADD = "basis_add";
	/**
	 * 编辑
	 */
	public static final String BASIS_EDIT = "basis_edit";
	/**
	 * 删除
	 */
	public static final String BASIS_DELETE = "basis_delete";
	/**
	 * 新增
	 */
	public static final String RANGEBASIS_ADD = "rangebasis_add";
	/**
	 * 新增
	 */
	public static final String PATROLRECORD_ADD = "patrolrecord_add";
	/**
	 * 导出
	 */
	public static final String SPECIAL_INSPECTION_EXPORT = "special_inspection_export";
	/**
	 * 导出
	 */
	public static final String STRENGTH_INSPECTION_EXPORT = "strength_inspection_export";
	/**
	 * 导出
	 */
	public static final String FIRST_CENTRE_LAST_EXPORT = "first_centre_last_export";
	/**
	 * 导出
	 */
	public static final String MOULDSFIRSTDOC_EXPORT = "mouldsfirstdoc_export";
	/**
	 * 设备维修请购单
	 */
	public static final String API_REPAIR_APPLY_PR = "api_repair_apply_pr";
	/**
	 * 扫码报工
	 */
	public static final String CBARCODEROUTEREPORT = "cbarcoderoutereport";
	/**
	 * 设备维修
	 */
	public static final String API_REPAIR_RECORD = "api_repair_record";
	/**
	 * 设备维修委外
	 */
	public static final String API_REPAIR_APPLY_ISSUE = "api_repair_apply_issue";
	/**
	 * bom子项档案
	 */
	public static final String BOMCOMPARE = "bomcompare";
	/**
	 * 生产报工
	 */
	public static final String ROUTEREPORT = "routereport";
	/**
	 * APS采购发行
	 */
	public static final String PURCHASORDER = "purchasorder";
	/**
	 * 首末检验单(来源工单派工)
	 */
	public static final String FIRSTLASTDOC = "firstlastdoc";
	/**
	 * 生产备料单
	 */
	public static final String MOPICKLIST = "mopicklist";
	/**
	 * 参数配置
	 */
	public static final String SYSSETTING = "syssetting";
	/**
	 * 委外条码管理
	 */
	public static final String OM = "om";
	/**
	 * 计量单位档案
	 */
	public static final String UOM = "uom";
	/**
	 * 采购条码管理
	 */
	public static final String PURCHASE = "purchase";
	/**
	 * 条码管理
	 */
	public static final String BARCODEENCODING = "barcodeencoding";
	/**
	 * 条码管理细表
	 */
	public static final String BARCODEENCODINGDETAIL = "barcodeencodingdetail";
	/**
	 * 交班记录明细
	 */
	public static final String PLSHIFTWORKGROUPD = "plshiftworkgroupd";
	/**
	 * 检验标准配置
	 */
	public static final String BASISLINE1 = "basisline1";
	/**
	 * 位置档案
	 */
	public static final String LOCLISTCN = "loclistcn";
	/**
	 * 派工工艺文件记录
	 */
	public static final String ASSIGNDRAWINGRECORD = "assigndrawingrecord";
	/**
	 * 敏感词词库
	 */
	public static final String SENSITIVE_WORD = "sensitive_word";
	/**
	 * 首末检验记录
	 */
	public static final String FIRSTLASTRECORD = "firstlastrecord";
	/**
	 * 派工工位记录
	 */
	public static final String PLASSIGNPOSITIONRECORD = "plassignpositionrecord";
	/**
	 * 加工实物耗用记录
	 */
	public static final String CUTBARCODECONSUMPTION = "cutbarcodeconsumption";
	/**
	 * 菜单按钮
	 */
	public static final String PERMISSION_BTN = "permission_btn";
	/**
	 * 首末检验记录参数
	 */
	public static final String FIRSTLASTRECORDPARAMETER = "firstlastrecordparameter";
	/**
	 * 打印设置
	 */
	public static final String PRINTSETTING = "printsetting";
	/**
	 * 用户类型
	 */
	public static final String USER_TYPE = "user_type";
	/**
	 * U9同步数据时间配置
	 */
	public static final String U9DATASSYNCTASKRECORD = "u9datassynctaskrecord";
	/**
	 * 完工检验单
	 */
	public static final String FINISHDOC = "finishdoc";
	/**
	 * 委外发料行
	 */
	public static final String PMISSUERECORDLINE = "pmissuerecordline";
	/**
	 * 编辑
	 */
	public static final String WORKREGIONM_EDIT = "workregionm_edit";
	/**
	 * 删除
	 */
	public static final String WORKREGIONM_DELETE = "workregionm_delete";
	/**
	 * 导出
	 */
	public static final String WORKREGIONM_EXPORT = "workregionm_export";
	/**
	 * 导入
	 */
	public static final String WORKREGIONM_IMPORT = "workregionm_import";
	/**
	 * 出货计划分析
	 */
	public static final String SCHEDUSOURCEAPPORTION = "schedusourceapportion";
	/**
	 * 新增
	 */
	public static final String WAREHOUSE_ADD1 = "warehouse_add1";
	/**
	 * 导出
	 */
	public static final String WAREHOUSE_AREA_EXPORT = "warehouse_area_export";
	/**
	 * 导入
	 */
	public static final String WAREHOUSE_AREA_IMPORT = "warehouse_area_import";
	/**
	 * 卷料采购送货单
	 */
	public static final String DELIVERY_HEADER = "delivery_header";
	/**
	 * 拆框
	 */
	public static final String CK = "ck";
	/**
	 * 合框
	 */
	public static final String HK = "hk";
	/**
	 * 新增
	 */
	public static final String FINISHDOC_ADD = "finishdoc_add";
	/**
	 * 编辑
	 */
	public static final String FINISHDOC_EDIT = "finishdoc_edit";
	/**
	 * 删除
	 */
	public static final String FINISHDOC_DELETE = "finishdoc_delete";
	/**
	 * 新增返工单1
	 */
	public static final String PRODUCTION_TICKETS_ADD = "production_tickets_add";
	/**
	 * 导入
	 */
	public static final String PARAMETER_IMPORT = "parameter_import";
	/**
	 * 通用检验标准
	 */
	public static final String RANGEBASIS1 = "rangebasis1";
	/**
	 * 生产计划分析
	 */
	public static final String SCHEDUSOURCEAPPORTIONU9 = "schedusourceapportionu9";
	/**
	 * 来料检验单
	 */
	public static final String RCVDOCQCFORMM = "rcvdocqcformm";
	/**
	 * APP版本
	 */
	public static final String APPVERSION = "appversion";
	/**
	 * 检验参数
	 */
	public static final String QCPARAM = "qcparam";
	/**
	 * 检验项目
	 */
	public static final String QCITEM = "qcitem";
	/**
	 * 生产计划管理
	 */
	public static final String PRODUCTION_PLAN = "production_plan";
	/**
	 * 年度生产计划排产
	 */
	public static final String YEAR_PRODUCTION_SCHEDULE = "year_production_schedule";
	/**
	 * 年度生产计划汇总
	 */
	public static final String SUMMARY_OF_ANNUAL_PRODUCTION_PLAN = "summary_of_annual_production_plan";
	/**
	 * 月周生产计划排产
	 */
	public static final String MONTHLY_AND_WEEKLY_PRODUCTION_SCHEDULE = "monthly_and_weekly_production_schedule";
	/**
	 * 月周生产计划汇总
	 */
	public static final String SUMMARY_OF_MONTHLY_AND_WEEKLY_PRODUCTION = "summary_of_monthly_and_weekly_production";
	/**
	 * 生产计划及实绩管理
	 */
	public static final String PRODUCTION_PLANNING_AND_PERFORMANCE_MANA = "production_planning_and_performance_mana";
	/**
	 * 禀议建模
	 */
	public static final String PROPOSAL_MODEL = "proposal_model";
	/**
	 * 币种档案
	 */
	public static final String FOREIGN_CURRENCY_INDEX = "foreign_currency_index";
	/**
	 * 人员档案
	 */
	public static final String PERSON_INDEX = "person_index";
	/**
	 * 供应商分类
	 */
	public static final String VENDOR = "vendor";
	/**
	 * 机型档案
	 */
	public static final String EQUIPMENT_MODEL = "equipment_model";
	/**
	 * 客户档案-联系地址
	 */
	public static final String ADMIN_CUSTOMERADDR = "ADMIN_CUSTOMERADDR";
	/**
	 * 部门档案
	 */
	public static final String DEPARTMENT = "department";
	/**
	 * 容器管理
	 */
	public static final String CONTAINER_CONTROL = "container_control";
	/**
	 * 容器档案
	 */
	public static final String CONTAINER = "container";
	/**
	 * 质量建模
	 */
	public static final String QCFORM_MODEL = "qcform_model";
	/**
	 * 质量表格设置
	 */
	public static final String QCFORM = "qcform";
	/**
	 * 供应商地址明细
	 */
	public static final String VENDORADDR = "vendoraddr";
	/**
	 * 存货档案
	 */
	public static final String INVENTORY_RECORD = "inventory_record";
	/**
	 * 物料形态对照表
	 */
	public static final String INVENTORYCHANGE = "INVENTORYCHANGE";
	/**
	 * 客户订单管理
	 */
	public static final String CUSTOMER_ORDERS = "customer_orders";
	/**
	 * 年度计划
	 */
	public static final String ANNUALORDERM = "annualorderm";
	/**
	 * 结算方式
	 */
	public static final String SETTLE_STYLE = "settle_style";
	/**
	 * 基础档案
	 */
	public static final String BASICS = "basics";
	/**
	 * 收发类别
	 */
	public static final String RDSTYLE = "rdstyle";
	/**
	 * 项目管理项目分类目录
	 */
	public static final String FITEMSS97CLASS = "fitemss97class";
	/**
	 * 项目管理大类项目主目录
	 */
	public static final String FITEMSS97 = "fitemss97";
	/**
	 * 项目管理大类项目子目录
	 */
	public static final String FITEMSS97SUB = "fitemss97sub";
	/**
	 * 单据类型与收发类别对照表
	 */
	public static final String VOUCHRDCONTRAPOSE = "vouchrdcontrapose";
	/**
	 * 销售类型
	 */
	public static final String SALETYPE = "saletype";
	/**
	 * 物料清单版本记录
	 */
	public static final String MASTER_AUDIT = "master_audit";
	/**
	 * 物料清单
	 */
	public static final String BOMMASTER = "bommaster";
	/**
	 * 消息模板
	 */
	public static final String SYS_MASSSAGE_TEMPLATE = "sys_masssage_template";
	/**
	 * 备份设置
	 */
	public static final String BACKUP_LOG = "backup_log";
	/**
	 * 采购类型
	 */
	public static final String PURCHASETYPE = "purchasetype";
	/**
	 * 检验适用标准
	 */
	public static final String ADMIN_INVENTORYQCFORM = "ADMIN_INVENTORYQCFORM";
	/**
	 * 参数配置
	 */
	public static final String 	SYS_CONFIG = "	sys_config";
	/**
	 * 实体扩展字段
	 */
	public static final String DESCFLEXFIELDDEF = "descflexfielddef";
	/**
	 * 容器分类
	 */
	public static final String CONTAINERCLASS = "containerclass";
	/**
	 * 拓展字段值
	 */
	public static final String DESCFLEXSEGVALUESETVALUE = "descflexsegvaluesetvalue";
	/**
	 * 平板端管理
	 */
	public static final String PAD_CONFIG = "pad_config";
	/**
	 * 平板端配置
	 */
	public static final String PAD = "pad";
	/**
	 * 平板登录日志
	 */
	public static final String PAD_LOGINLOG = "pad_loginlog";
	/**
	 * 工艺路线
	 */
	public static final String PROUTING = "prouting";
	/**
	 * 周间客户订单
	 */
	public static final String WEEK_ORDERM = "week_orderm";
	/**
	 * 手配订单
	 */
	public static final String MANUAL_ORDER = "manual_order";
	/**
	 * 月度计划订单
	 */
	public static final String MONTHORDERM = "monthorderm";
	/**
	 * 年度生产计划排产2
	 */
	public static final String SCHEDUPRODUCTPLANYEAR = "scheduproductplanyear";
	/**
	 * 年度生产计划汇总2
	 */
	public static final String NOME = "nome";
	/**
	 * 采购/委外管理
	 */
	public static final String PS_MGR = "ps_mgr";
	/**
	 * 采购订单管理
	 */
	public static final String PS_PURCHASE_ORDER = "ps_purchase_order";
	/**
	 * 委外销售订单
	 */
	public static final String SUBCONTRACTSALEORDERM = "subcontractsaleorderm";
	/**
	 * 入库管理
	 */
	public static final String RK = "rk";
	/**
	 * 出库管理
	 */
	public static final String CHUKU = "chuku";
	/**
	 * 出货管理
	 */
	public static final String CH = "ch";
	/**
	 * 条码管理
	 */
	public static final String BARCODE = "barcode";
	/**
	 * 客户订单汇总
	 */
	public static final String CUSORDER_SUM = "cusorder_sum";
	/**
	 * 月周生产计划排产2
	 */
	public static final String SCHEDUPRODUCTPLANMONTH = "scheduproductplanmonth";
	/**
	 * 新增
	 */
	public static final String WAREHOUSE_ADD = "warehouse_add";
	/**
	 * 新增
	 */
	public static final String DEPT_ADD = "dept_add";
	/**
	 * 新增报修
	 */
	public static final String API_REPAIRAPPLYRECORD_SAVE = "api_repairapplyrecord_save";
	/**
	 * 设备报修审核
	 */
	public static final String API_REPAIRAPPLYRECORD_AUDIT1 = "api_repairapplyrecord_audit1";
	/**
	 * 设备报修修改
	 */
	public static final String API_REPAIR_APPLY_RECORD_UPDATE = "api_repair_apply_record_update";
	/**
	 * 设备报修撤回
	 */
	public static final String API_REPAIR_APPLY_RECORD_WITHDRAW = "api_repair_apply_record_withdraw";
	/**
	 * 设备维修委外
	 */
	public static final String API_REPAIR_APPLY_RECORD_OUTSOURCE = "api_repair_apply_record_outsource";
	/**
	 * 请购单【新增】
	 */
	public static final String API_REPAIR_APPLY_PR_ADD = "api_repair_apply_pr_add";
	/**
	 * 请购单【审核】
	 */
	public static final String API_REPAIRAPPLYPR_AUDIT = "api_repairapplypr_audit";
	/**
	 * 设备维修派工【新增】
	 */
	public static final String API_REPAIR_APPLY_ASSIGN_ADD = "api_repair_apply_assign_add";
	/**
	 * 设备维修记录【新增】
	 */
	public static final String API_REPAIR_RECORD_ADD = "api_repair_record_add";
	/**
	 * 设备维修审核
	 */
	public static final String API_REPAIR_RECORD_REPAIR_AUDIT = "api_repair_record_repair_audit";
	/**
	 * 设备维修委外报价
	 */
	public static final String API_REPAIR_APPLY_ISSUE_QUOTATION_ADD = "api_repair_apply_issue_quotation_add";
	/**
	 * 设备维修委外-报价审核
	 */
	public static final String API_REPAIR_APPLY_ISSUE_AUDIT = "api_repair_apply_issue_audit";
	/**
	 * 派班
	 */
	public static final String PRODUCTION_PRODUCTION_DISPATCH = "production_production_dispatch";
	/**
	 * 计划下达
	 */
	public static final String PRODUCTION_TICKETS_PLAN_RELEASE = "production_tickets_plan_release";
	/**
	 * 备料下达
	 */
	public static final String PRODUCTION_TICKETS_RELEASE_OF_MATERIAL = "production_tickets_release_of_material";
	/**
	 * 新增返工单
	 */
	public static final String PRODUCTION_TICKETS_REWORK_ORDER = "production_tickets_rework_order";
	/**
	 * 数据导出
	 */
	public static final String PRODUCTION_TICKETS_EXPORT = "production_tickets_export";
	/**
	 * 交班
	 */
	public static final String PRODUCTION_PRODUCTION_SHIFT_OF_DUTY = "production_production_Shift_of_duty";
	/**
	 * 交班记录
	 */
	public static final String PRODUCTION_PRODUCTION_PLSHIFTWORKGROUPM = "production_production_plshiftworkgroupm";
	/**
	 * 数据导出
	 */
	public static final String PRODUCTION_PRODUCTION_EXPORT = "production_production_export";
	/**
	 * 打印工序卡
	 */
	public static final String PRODUCTION_REPORT_PROCESS_CARD = "production_report_process_card";
	/**
	 * 打印实物标签
	 */
	public static final String PRODUCTION_REPORT_PHYSICAL_LABEL = "production_report_physical_label";
	/**
	 * 数据导出
	 */
	public static final String PRODUCTION_REPORT_EXPORT = "production_report_export";
	/**
	 * 数据导出
	 */
	public static final String PRODUCTION_COMPLETE_EXPORT = "production_complete_export";
	/**
	 * 新增
	 */
	public static final String CUTSTRIPPROCESS_ADD = "cutstripprocess_add";
	/**
	 * 数据导出
	 */
	public static final String CUTSTRIPPROCESS_EXPORT = "cutstripprocess_export";
	/**
	 * 新增
	 */
	public static final String SLICEINVPROCESS_ADD = "sliceinvprocess_add";
	/**
	 * 数据导出
	 */
	public static final String SLICEINVPROCESS_EXPORT = "sliceinvprocess_export";
	/**
	 * 新增
	 */
	public static final String PIECEINVPROCESS_ADD = "pieceinvprocess_add";
	/**
	 * 数据导出
	 */
	public static final String PIECEINVPROCESS_EXPORT = "pieceinvprocess_export";
	/**
	 * 数据导出
	 */
	public static final String REWORKORDER_EXPORT = "reworkorder_export";
	/**
	 * 获取U9
	 */
	public static final String PMISSUEM_GET_U9 = "pmissuem_get_u9";
	/**
	 * 合并发料
	 */
	public static final String PMISSUEM_MERGE = "pmissuem_merge";
	/**
	 * 数据导出
	 */
	public static final String PIECE_RATE_EXPORT = "piece_rate_export";
	/**
	 * 数据导出
	 */
	public static final String VIEWPMISSUERECORD_EXPORT = "viewpmissuerecord_export";
	/**
	 * 详情
	 */
	public static final String OTHER_OUT_DETAILS = "other_out_details";
	/**
	 * 详情
	 */
	public static final String TRANSFERIN_DETAILS = "transferin_details";
	/**
	 * 数据导出
	 */
	public static final String TRANSFERIN_EXPORT = "transferin_export";
	/**
	 * 详情
	 */
	public static final String CK_DETAILS = "ck_details";
	/**
	 * 详情
	 */
	public static final String HK_DETAILS = "hk_details";
	/**
	 * 获取U9
	 */
	public static final String PHE_PO_HEADER_GET_U9 = "phe_po_header_get_u9";
	/**
	 * 数据导出
	 */
	public static final String PHE_PO_HEADER_EXPORT = "phe_po_header_export";
	/**
	 * 删除
	 */
	public static final String PHE_PO_HEADER_DELETE = "phe_po_header_delete";
	/**
	 * 新增
	 */
	public static final String SRM_PO_DELIVERY_ADD = "srm_po_delivery_add";
	/**
	 * 编辑
	 */
	public static final String SRM_PO_DELIVERY_EDIT = "srm_po_delivery_edit";
	/**
	 * 删除
	 */
	public static final String SRM_PO_DELIVERY_DELETE = "srm_po_delivery_delete";
	/**
	 * 数据导出
	 */
	public static final String SRM_PO_DELIVERY_EXPORT = "srm_po_delivery_export";
	/**
	 * 新增
	 */
	public static final String SRM_FLAKE_OM_DELIVERY_ADD = "srm_flake_om_delivery_add";
	/**
	 * 编辑
	 */
	public static final String SRM_FLAKE_OM_DELIVERY_EDIT = "srm_flake_om_delivery_edit";
	/**
	 * 删除
	 */
	public static final String SRM_FLAKE_OM_DELIVERY_DELETE = "srm_flake_om_delivery_delete";
	/**
	 * 数据导出
	 */
	public static final String SRM_FLAKE_OM_DELIVERY_EXPORT = "srm_flake_om_delivery_export";
	/**
	 * 新增
	 */
	public static final String SRM_OM_DELIVERY_ADD = "srm_om_delivery_add";
	/**
	 * 编辑
	 */
	public static final String SRM_OM_DELIVERY_EDIT = "srm_om_delivery_edit";
	/**
	 * 删除
	 */
	public static final String SRM_OM_DELIVERY_DELETE = "srm_om_delivery_delete";
	/**
	 * 数据导出
	 */
	public static final String SRM_OM_DELIVERY_EXPORT = "srm_om_delivery_export";
	/**
	 * 新增
	 */
	public static final String DELIVERY_HEADER_ADD = "delivery_header_add";
	/**
	 * 编辑
	 */
	public static final String DELIVERY_HEADER_EDIT = "delivery_header_edit";
	/**
	 * 删除
	 */
	public static final String DELIVERY_HEADER_DELETE = "delivery_header_delete";
	/**
	 * 数据导出
	 */
	public static final String DELIVERY_HEADER_EXPORT = "delivery_header_export";
	/**
	 * 数据导出
	 */
	public static final String APPVOUCHDETAIL_EXPORT = "appvouchdetail_export";
	/**
	 * 详情
	 */
	public static final String APPVOUCHDETAIL_DETAILS = "appvouchdetail_details";
	/**
	 * 数据导出
	 */
	public static final String PURCHASE_RETURN_EXPORT = "purchase_return_export";
	/**
	 * 数据导出
	 */
	public static final String JL_INSTORE_EXPORT = "JL_InStore_export";
	/**
	 * 详情
	 */
	public static final String JL_INSTORE_DETAILS = "JL_InStore_details";
	/**
	 * 齐套分析
	 */
	public static final String PRODUCTION_TICKETS_ANALYSIS = "production_tickets_analysis";
	/**
	 * 新增
	 */
	public static final String APPVERSION_SAVE = "appversion_save";
	/**
	 * 修改
	 */
	public static final String APPVERSION_UPDATE = "appversion_update";
	/**
	 * 发布
	 */
	public static final String APPVERSION_PUBLISH = "appversion_publish";
	/**
	 * 强制更新
	 */
	public static final String APPVERSION_ISFORCE = "appversion_isforce";
	/**
	 * 新增
	 */
	public static final String DEPARTMENT_ADD = "department_add";
	/**
	 * 删除
	 */
	public static final String DEPARTMENT_DEL = "department_del";
	/**
	 * 编辑
	 */
	public static final String DEPARTMENT_EDIT = "department_edit";
	/**
	 * 导出
	 */
	public static final String DEPARTMENT_EXPORT = "department_export";
	/**
	 * 导入
	 */
	public static final String DEPARTMENT_IMPORT = "department_import";
	/**
	 * 新增
	 */
	public static final String INVENTORYCHANGE_ADD = "inventorychange_add";
	/**
	 * 删除
	 */
	public static final String INVENTORYCHANGE_DEL = "inventorychange_del";
	/**
	 * 修改
	 */
	public static final String INVENTORYCHANGE_EDIT = "inventorychange_edit";
	/**
	 * 导入
	 */
	public static final String INVENTORYCHANGE_IMPORT = "inventorychange_import";
	/**
	 * 导出
	 */
	public static final String INVENTORYCHANGE_EXPORT = "inventorychange_export";
	/**
	 * 新增
	 */
	public static final String ANNUALORDERM_ADD = "annualorderm_add";
}
