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
	 * 系统配置
	 */
	public static final String  SYSTEMMGR = " systemmgr";
	/**
	 * 组织账套
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
	 * 物料建模
	 */
	public static final String MATERIAL = "material";
	/**
	 * 计量单位
	 */
	public static final String UOMCLASS = "uomclass";
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
	 * 制造管理
	 */
	public static final String WORKSHOP_EXECUTE = "workshop_execute";
	/**
	 * 制造工单管理
	 */
	public static final String MO_DOC = "mo_doc";
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
	 * 点检建模
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
	 * 客户档案明细
	 */
	public static final String CUSTOMERD = "customerd";
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
	 * 收料管理
	 */
	public static final String SL = "sl";
	/**
	 * 发料管理
	 */
	public static final String FL = "fl";
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
	public static final String DELIVERY_MANAGE = "delivery_manage";
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
	 * 设备报修
	 */
	public static final String API_REPAIRAPPLYRECORD = "api_repairapplyrecord";
	/**
	 * 设备维修请购单
	 */
	public static final String API_REPAIR_APPLY_PR = "api_repair_apply_pr";
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
	 * 生产备料单
	 */
	public static final String MOPICKLIST = "mopicklist";
	/**
	 * 计量单位档案
	 */
	public static final String UOM = "uom";
	/**
	 * 采购条码管理
	 */
	public static final String PURCHASE = "purchase";
	/**
	 * 条码管理细表
	 */
	public static final String BARCODEENCODINGDETAIL = "barcodeencodingdetail";
	/**
	 * 位置档案
	 */
	public static final String LOCLISTCN = "loclistcn";
	/**
	 * 敏感词词库
	 */
	public static final String SENSITIVE_WORD = "sensitive_word";
	/**
	 * 菜单按钮
	 */
	public static final String PERMISSION_BTN = "permission_btn";
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
	 * 生产计划及实绩管理
	 */
	public static final String PLANANDACTUAL = "planandactual";
	/**
	 * 禀议建模
	 */
	public static final String PROPOSAL_MODEL = "proposal_model";
	/**
	 * 外币设置
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
	 * 项目档案
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
	 * 容器分类
	 */
	public static final String CONTAINERCLASS = "containerclass";
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
	 * 年度生产计划排产
	 */
	public static final String SCHEDUPRODUCTPLANYEAR = "scheduproductplanyear";
	/**
	 * 年度生产计划汇总
	 */
	public static final String NOME = "nome";
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
	 * 条码管理
	 */
	public static final String BARCODE = "barcode";
	/**
	 * 客户订单汇总
	 */
	public static final String CUSORDER_SUM = "cusorder_sum";
	/**
	 * 月周生产计划排产
	 */
	public static final String SCHEDUPRODUCTPLANMONTH = "scheduproductplanmonth";
	/**
	 * 工艺路线
	 */
	public static final String ROUTING_RECORD = "routing_record";
	/**
	 * 出库检
	 */
	public static final String STOCKOUTQCFORMM = "stockoutqcformm";
	/**
	 * 在库检
	 */
	public static final String INSTOCKQCFORMM = "instockqcformm";
	/**
	 * 来料异常品记录
	 */
	public static final String RCVDOCDEFECT = "rcvdocdefect";
	/**
	 * 采购/委外管理
	 */
	public static final String ORDER_MANAGEMENT = "order_management";
	/**
	 * 采购订单管理
	 */
	public static final String PU_ORDER_MANAGEMENT = "pu_order_management";
	/**
	 * 委外订单管理
	 */
	public static final String OM_ORDER_MANAGEMENT = "om_order_management";
	/**
	 * 审批流/审核配置
	 */
	public static final String AUDIT_CONFIG = "audit_config";
	/**
	 * 取货计划管理
	 */
	public static final String PICKUP_PLAN_MANAGE = "pickup_plan_manage";
	/**
	 * 货款核对管理
	 */
	public static final String PAYMENT_CHECK_MANAGE = "payment_check_manage";
	/**
	 * 双码扫码出货
	 */
	public static final String DOUBLE_CODE_SCANNING_SHIPMENT = "double_code_scanning_shipment";
	/**
	 * 扫码出货
	 */
	public static final String SCAN_CODE_SHIPMENT = "scan_code_shipment";
	/**
	 * 销售出库单列表
	 */
	public static final String SALES_DELIVERY_LIST = "sales_delivery_list";
	/**
	 * 销售发货单列表
	 */
	public static final String SALES_SHIPMENT_LIST = "sales_shipment_list";
	/**
	 * 条码报表
	 */
	public static final String BARCODE_REPORT = "barcode_report";
	/**
	 * 仓库报表
	 */
	public static final String WAREHOUSE_REPORT = "warehouse_report";
	/**
	 * 库存盘点
	 */
	public static final String CURRENTSTOCK = "currentstock";
	/**
	 * 制程异常品记录
	 */
	public static final String PROCESSDEFECT = "processdefect";
	/**
	 * 表单配置
	 */
	public static final String FORM_CONFIG = "form_config";
	/**
	 * 客户计划及实绩管理
	 */
	public static final String CUSORDER_RESULT = "cusorder_result";
	/**
	 * 月周生产计划汇总
	 */
	public static final String SCHEDUPRODUCTPLANMONTH_PLAN_MONTH_SUM = "scheduproductplanmonth_plan_month_sum";
	/**
	 * 出库异常记录
	 */
	public static final String STOCKOUTDEFECT = "stockoutdefect";
	/**
	 * 在库异常记录
	 */
	public static final String INSTOCKDEFECT = "instockdefect";
	/**
	 * 工程内品质巡查
	 */
	public static final String QCINSPECTION = "qcinspection";
	/**
	 * 系统字段管理
	 */
	public static final String FORM_FIELD = "form_field";
	/**
	 * 表单类别
	 */
	public static final String FORM_CATEGORY = "form_category";
	/**
	 * 导入字段配置
	 */
	public static final String CUS_FIELDS_MAPPINGM = "cus_fields_mappingm";
	/**
	 * 制造工单批量编辑
	 */
	public static final String MO_DOCBATCH = "mo_docbatch";
	/**
	 * 备料一览表
	 */
	public static final String ADMIN_SYSMATERIALSPREPARE = "ADMIN_SYSMATERIALSPREPARE";
	/**
	 * 编码规则配置
	 */
	public static final String CODING_RULEM = "coding_rulem";
	/**
	 * 双码扫码出货
	 */
	public static final String DOUBLECODESCANNINGSHIPMENT = "doubleCodeScanningShipment";
	/**
	 * 双码扫码出货1
	 */
	public static final String DOUBLECODESCANNINGSHIPMENT1 = "doubleCodeScanningShipment1";
	/**
	 * 收料单列表
	 */
	public static final String MATERIAL_RECEIPT_LIST = "material_receipt_list";
	/**
	 * 采购入库单列表
	 */
	public static final String PURCHASE_RECEIPT_LIST = "purchase_receipt_list";
	/**
	 * 形态转换单列表
	 */
	public static final String FORM_CONVERSION_LIST = "form_conversion_list";
	/**
	 * 产成品入库单列表
	 */
	public static final String RODUCT_IN_LIST = "roduct_in_list";
	/**
	 * 其他入库单列表
	 */
	public static final String OTHER_IN_LIST = "other_in_list";
	/**
	 * 生产备料
	 */
	public static final String ADMIN_SYSMATERIALSPREPAREDETAIL = "ADMIN_SYSMATERIALSPREPAREDETAIL";
	/**
	 * 材料出库单列表
	 */
	public static final String MATERIAL_DELIVERY_LIST = "material_delivery_list";
	/**
	 * 其他出库单列表
	 */
	public static final String OTHER_DELIVERY_LIST = "other_delivery_list";
	/**
	 * 物料退货列表
	 */
	public static final String MATERIAL_RETURN_LIST = "material_return_list";
	/**
	 * 调拨单列表
	 */
	public static final String TRANSVOUCH = "transvouch";
	/**
	 * 特殊领料单列表
	 */
	public static final String OTHEROUT = "otherout";
	/**
	 * 仓库期初
	 */
	public static final String WAREHOUSEBEGINOFPERIOD = "warehousebeginofperiod";
	/**
	 * 拆分条码
	 */
	public static final String SPLIT_BARCODE = "split_barcode";
	/**
	 * 合并条码
	 */
	public static final String MERGE_BARCODE = "merge_barcode";
	/**
	 * 其他入库单列表
	 */
	public static final String MATERIAL_RECEIPT_LIST1 = "material_receipt_list1";
	/**
	 * 需求计划管理
	 */
	public static final String DEMAND_PLAN = "demand_plan";
	/**
	 * 物料需求计划预示
	 */
	public static final String DEMAND_FORECAST = "demand_forecast";
	/**
	 * 物料到货计划
	 */
	public static final String DEMAND_PLAN2 = "demand_plan2";
	/**
	 * 材料出库单
	 */
	public static final String ADMIN_SYSMATERIALSOUT = "ADMIN_SYSMATERIALSOUT";
	/**
	 * 产成品入库单列表
	 */
	public static final String PRODUCTINLIST = "productInList";
	/**
	 * 形态转换单列表
	 */
	public static final String FORMCONVERSIONLIST = "formConversionList";
	/**
	 * 形态转换单从表
	 */
	public static final String FORMCONVERSIONLIST1 = "formConversionList1";
	/**
	 * 采购入库单列表
	 */
	public static final String PURCHASERECEIPTLIST = "purchaseReceiptList";
	/**
	 * 采购入库单列表1
	 */
	public static final String PURCHASERECEIPTLIST1 = "purchaseReceiptList1";
	/**
	 * 收料单列表
	 */
	public static final String MATERIALRECEIPTLIST = "materialReceiptList";
	/**
	 * 收料单列表
	 */
	public static final String MATERIALRECEIPTLIST1 = "materialReceiptList1";
	/**
	 * 开发文档
	 */
	public static final String ADMIN_DEV_DOC = "admin_dev_doc";
	/**
	 * 系统管理-枚举值
	 */
	public static final String ENUM_VALS = "enum_vals";
	/**
	 * 物料需求计划计算
	 */
	public static final String DEMAND_ALGORITHM = "demand_algorithm";
	/**
	 * 补打条码
	 */
	public static final String PATCHWORK_BARCODE = "patchwork_barcode";
	/**
	 * 月周排产记录时间
	 */
	public static final String APS_WEEK_SCHEDULE = "aps_week_schedule";
	/**
	 * 特殊领料出库
	 */
	public static final String OTHER_OUT_RETURN_LIST = "other_out_return_list";
	/**
	 * 项目档案
	 */
	public static final String PROJECT = "project";
	/**
	 * 禀议类别区分
	 */
	public static final String PROPOSALCATEGORY = "proposalcategory";
	/**
	 * 期间管理
	 */
	public static final String PERIOD = "period";
	/**
	 * 科目对照表
	 */
	public static final String SUBJECTM = "subjectm";
	/**
	 * 预算编制
	 */
	public static final String EXPENSE_BUDGET = "expense_budget";
	/**
	 * 费用预算编制
	 */
	public static final String EXPENSE_BUDGET_FORMULATE = "expense_budget_formulate";
	/**
	 * 费用预算管理
	 */
	public static final String EXPENSE_BUDGET_MANAGE = "expense_budget_manage";
	/**
	 * 费用月度实绩调整
	 */
	public static final String EXPENSE_MONTH_ADJUSTMENTM = "expense_month_adjustmentm";
	/**
	 * 投资计划编制
	 */
	public static final String INVESTMENT_PLAN_FORMULATE = "investment_plan_formulate";
	/**
	 * 投资计划管理
	 */
	public static final String INVESTMENT_PLAN_MANAGE = "investment_plan_manage";
	/**
	 * 投资月度实绩管理
	 */
	public static final String INVESTMENT_MONTH_ADJUSTMENTM = "investment_month_adjustmentm";
	/**
	 * 追加项目
	 */
	public static final String APPEND_ITEM = "append_item";
	/**
	 * 禀议管理
	 */
	public static final String ADVICE_MANAGEMENT = "advice_management";
	/**
	 * 禀议书编制
	 */
	public static final String PROPOSALM = "proposalm";
	/**
	 * 禀议书管理
	 */
	public static final String PROPOSALM_AUDIT_INDEX = "proposalm_audit_index";
	/**
	 * 申购单编制
	 */
	public static final String PURCHASE_INSTRUMENT = "purchase_instrument";
	/**
	 * 申购单管理
	 */
	public static final String PURCHASE_MANAGEMENT = "purchase_management";
	/**
	 * 统计分析
	 */
	public static final String STATISTIC_ANALYSIS = "statistic_analysis";
	/**
	 * 物料需求计划汇总
	 */
	public static final String DEMAND_PLANSUM = "demand_plansum";
	/**
	 * 禀议明细表
	 */
	public static final String STATISTIC_ANALYSIS_PROPOSAL_DETAIL = "statistic_analysis_proposal_detail";
	/**
	 * 费用预算明细
	 */
	public static final String STATISTIC_ANALYSIS_EXPENSEBUDGETITEMD = "statistic_analysis_expensebudgetitemd";
	/**
	 * 投资计划明细
	 */
	public static final String STATISTIC_ANALYSIS_INVESTMENTPLANITEMD = "statistic_analysis_investmentplanitemd";
	/**
	 * 执行进度跟踪
	 */
	public static final String EXECUTION_PROGRESS_TRACKING = "execution_progress_tracking";
	/**
	 * 费用预算差异管理
	 */
	public static final String EXPENSE_BUDGET_DIFFERENCES = "expense_budget_differences";
	/**
	 * 费用预算期间对比
	 */
	public static final String EXPENSEBUDGET_CONTRAST = "expensebudget_contrast";
	/**
	 * 投资预实差异管理表
	 */
	public static final String INVESTMENT_BUDGET_ACTUAL_DIFFERENCE = "investment_budget_actual_difference";
	/**
	 * 投资汇总表
	 */
	public static final String INVESTMENT_PLAN_SUMMARY = "investment_plan_summary";
	/**
	 * 投资情况查询表
	 */
	public static final String INVESTMENT_PLAN_SITUATION_INDEX = "investment_plan_situation_index";
	/**
	 * 基础设置
	 */
	public static final String BASISARCHIVES = "basisarchives";
	/**
	 * 编码规则
	 */
	public static final String BARCODEENCODINGM = "barcodeencodingm";
	/**
	 * 单据业务类型
	 */
	public static final String VOUCHTYPEDIC = "vouchtypedic";
	/**
	 * 记录上传
	 */
	public static final String FORM_UPLOADM = "form_uploadm";
	/**
	 * 点检表格
	 */
	public static final String SPOTCHECK_FORM = "spotcheck_form";
	/**
	 * 拓展字段配置
	 */
	public static final String FIELD_CONFIG = "field_config";
	/**
	 * 部门对照表
	 */
	public static final String DEPREF = "depref";
	/**
	 * 拓展字段配置
	 */
	public static final String FORM_EXTEND_FIELDS = "form_extend_fields";
	/**
	 * 精度设置
	 */
	public static final String PRECISION_CONFIG = "precision_config";
	/**
	 * 上传记录-分类管理
	 */
	public static final String FORM_UPLOAD_CATEGORY = "form_upload_category";
	/**
	 * 枚举类型
	 */
	public static final String ENUM_TYPE = "enum_type";
	/**
	 * 关于我们
	 */
	public static final String ABOUT_US = "about_us";
	/**
	 * 平板菜单权限管理
	 */
	public static final String PAD_APP = "PAD_APP";
	/**
	 * 产线刷卡
	 */
	public static final String NFCSWIPECARD = "nfcswipecard";
	/**
	 * 单据审批权限
	 */
	public static final String FORM_APPROVAL_PERMISSION = "form_approval_permission";
	/**
	 * 物料现品票汇总管理
	 */
	public static final String INVENTORY_BARCODE_TRACEPAGE = "Inventory_barcode_tracepage";
	/**
	 * 制造工单
	 */
	public static final String MODOC = "modoc";
	/**
	 * 记录上传
	 */
	public static final String FORMUPLOADM = "formuploadm";
	/**
	 * 来料检
	 */
	public static final String RCVDOCQCFORMM_APP = "rcvdocqcformm_app";
	/**
	 * 在库检
	 */
	public static final String INSTOCKQCFORMM_APP = "instockqcformm_app";
	/**
	 * 出货检
	 */
	public static final String STOCKOUTQCFORMM_APP = "stockoutqcformm_app";
	/**
	 * 来料异常品管理
	 */
	public static final String RCVDOCDEFECT_APP = "rcvdocdefect_app";
	/**
	 * 制程异常品管理
	 */
	public static final String PROCESSDEFECT_APP = "processdefect_app";
	/**
	 * 在库异常品管理
	 */
	public static final String INSTOCKDEFECT_APP = "instockdefect_app";
	/**
	 * 出货异常品管理
	 */
	public static final String STOCKOUTDEFECT_APP = "stockoutdefect_app";
	/**
	 * 工程内品质巡查管理
	 */
	public static final String QCINSPECTION_APP = "qcinspection_app";
	/**
	 * 工作台
	 */
	public static final String PROPOSAL_DASHBOARD = "proposal_dashboard";
	/**
	 * 客户传票汇总管理
	 */
	public static final String BILLNO_BARCODE_TRACEPAGE = "billno_barcode_tracepage";
	/**
	 * 社内现品票汇总管理
	 */
	public static final String BARCODE_TRACEPAGE = "barcode_tracepage";
	/**
	 * 生产表单参数
	 */
	public static final String PRODPARAM = "prodparam";
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
	/**
	 * 审批流列表
	 */
	public static final String APPROVALM = "approvalm";
	/**
	 * 新增
	 */
	public static final String EXPENSE_BUDGET_FORMULATE_ADD = "expense_budget_formulate_add";
	/**
	 * 修改
	 */
	public static final String EXPENSE_BUDGET_FORMULATE_EDIT = "expense_budget_formulate_edit";
	/**
	 * 删除
	 */
	public static final String EXPENSE_BUDGET_FORMULATE_DELETE = "expense_budget_formulate_delete";
	/**
	 * 导出
	 */
	public static final String EXPENSE_BUDGET_FORMULATE_EXPORT = "expense_budget_formulate_export";
	/**
	 * 提交审核
	 */
	public static final String EXPENSE_BUDGET_FORMULATE_SUBMITAUDIT = "expense_budget_formulate_submitaudit";
	/**
	 * 新增
	 */
	public static final String PROPOSALM_SAVE = "proposalm_save";
	/**
	 * 修改
	 */
	public static final String PROPOSALM_EDIT = "proposalm_edit";
	/**
	 * 删除
	 */
	public static final String PROPOSALM_DELETE = "proposalm_delete";
	/**
	 * 查看
	 */
	public static final String PROPOSALM_VIEW = "proposalm_view";
	/**
	 * 提审
	 */
	public static final String PROPOSALM_SUBMIT = "proposalm_submit";
	/**
	 * 撤销提审
	 */
	public static final String PROPOSALM_WITHDRAW = "proposalm_withdraw";
	/**
	 * 详情
	 */
	public static final String EXPENSE_BUDGET_MANAGE_DETAIL = "expense_budget_manage_detail";
	/**
	 * 生效
	 */
	public static final String EXPENSE_BUDGET_MANAGE_EFFECT = "expense_budget_manage_effect";
	/**
	 * 新增
	 */
	public static final String EXPENSE_MONTH_ADJUSTMENTM_ADD = "expense_month_adjustmentm_add";
	/**
	 * 修改
	 */
	public static final String EXPENSE_MONTH_ADJUSTMENTM_EDIT = "expense_month_adjustmentm_edit";
	/**
	 * 生效
	 */
	public static final String EXPENSE_MONTH_ADJUSTMENTM_EFFECT = "expense_month_adjustmentm_effect";
	/**
	 * 撤销
	 */
	public static final String EXPENSE_MONTH_ADJUSTMENTM_UNEFFECT = "expense_month_adjustmentm_uneffect";
	/**
	 * 删除
	 */
	public static final String EXPENSE_MONTH_ADJUSTMENTM_DELETE = "expense_month_adjustmentm_delete";
	/**
	 * 新增
	 */
	public static final String INVESTMENT_PLAN_FORMULATE_ADD = "investment_plan_formulate_add";
	/**
	 * 修改
	 */
	public static final String INVESTMENT_PLAN_FORMULATE_EDIT = "investment_plan_formulate_edit";
	/**
	 * 删除
	 */
	public static final String INVESTMENT_PLAN_FORMULATE_DELETE = "investment_plan_formulate_delete";
	/**
	 * 导出
	 */
	public static final String INVESTMENT_PLAN_FORMULATE_EXPORT = "investment_plan_formulate_export";
	/**
	 * 提交审核
	 */
	public static final String INVESTMENT_PLAN_FORMULATE_SUBMITAUDIT = "investment_plan_formulate_submitaudit";
	/**
	 * 详情
	 */
	public static final String INVESTMENT_PLAN_MANAGE_DETAIL = "investment_plan_manage_detail";
	/**
	 * 生效
	 */
	public static final String INVESTMENT_PLAN_MANAGE_EFFECT = "investment_plan_manage_effect";
	/**
	 * 新增
	 */
	public static final String INVESTMENT_MONTH_ADJUSTMENTM_ADD = "investment_month_adjustmentm_add";
	/**
	 * 详情
	 */
	public static final String INVESTMENT_MONTH_ADJUSTMENTM_EDIT = "investment_month_adjustmentm_edit";
	/**
	 * 删除
	 */
	public static final String INVESTMENT_MONTH_ADJUSTMENTM_DELETE = "investment_month_adjustmentm_delete";
	/**
	 * 生效
	 */
	public static final String INVESTMENT_MONTH_ADJUSTMENTM_EFFECT = "investment_month_adjustmentm_effect";
	/**
	 * 撤销
	 */
	public static final String INVESTMENT_MONTH_ADJUSTMENTM_UNEFFECT = "investment_month_adjustmentm_uneffect";
	/**
	 * 新增
	 */
	public static final String APPEND_ITEM_ADD = "append_item_add";
	/**
	 * 修改
	 */
	public static final String APPEND_ITEM_EDIT = "append_item_edit";
	/**
	 * 删除
	 */
	public static final String APPEND_ITEM_DELETE = "append_item_delete";
	/**
	 * 生效
	 */
	public static final String APPEND_ITEM_EFFECT = "append_item_effect";
	/**
	 * 撤销
	 */
	public static final String APPEND_ITEM_UNEFFECT = "append_item_uneffect";
	/**
	 * 生效
	 */
	public static final String PROPOSALM_EFFECT = "proposalm_effect";
	/**
	 * 新增-参考禀议书
	 */
	public static final String PURCHASE_INSTRUMENT_CHOOSE_PROPOSALM = "purchase_instrument_choose_proposalm";
	/**
	 * 编辑
	 */
	public static final String PURCHASE_INSTRUMENT_EDIT = "purchase_instrument_edit";
	/**
	 * 提交审核
	 */
	public static final String PURCHASE_INSTRUMENT_SUBMIT = "purchase_instrument_submit";
	/**
	 * 上传附件
	 */
	public static final String PURCHASE_INSTRUMENT_UPLOAD_FILE = "purchase_instrument_upload_file";
	/**
	 * 删除
	 */
	public static final String PURCHASE_INSTRUMENT_DELETE = "purchase_instrument_delete";
	/**
	 * 撤销提审
	 */
	public static final String PURCHASE_WITHDRAW = "purchase_withdraw";
	/**
	 * 撤销提交
	 */
	public static final String PURCHASE_MANAGEMENT_REVOCATIONSUBMIT = "purchase_management_revocationsubmit";
	/**
	 * 提交采购
	 */
	public static final String PURCHASE_MANAGEMENT_SUMBITPURCHASE = "purchase_management_sumbitpurchase";
	/**
	 * 导出
	 */
	public static final String PROPOSAL_DETAIL_EXPORT = "proposal_detail_export";
	/**
	 * 导出
	 */
	public static final String EXECUTION_PROGRESS_TRACKING_EXPORT = "execution_progress_tracking_export";
	/**
	 * 作废
	 */
	public static final String EXPENSE_BUDGET_MANAGE_CANCLE = "expense_budget_manage_cancle";
	/**
	 * 作废
	 */
	public static final String INVESTMENT_PLAN_MANAGE_CANCLE = "investment_plan_manage_cancle";
	/**
	 * 通过
	 */
	public static final String FORM_APP_APPROVE = "form_app_approve";
	/**
	 * 提审
	 */
	public static final String MONTHORDERM_SUBMIT = "monthorderm_submit";
	/**
	 * 通过
	 */
	public static final String MONTHORDERM_APPROVE = "monthorderm_approve";
	/**
	 * 不通过
	 */
	public static final String MONTHORDERM_REJECT = "monthorderm_reject";
	/**
	 * 批量审核通过
	 */
	public static final String MONTHORDERM_BATCH_APPROVE = "monthorderm_batch_approve";
	/**
	 * 批量审核不通过
	 */
	public static final String MONTHORDERM_BATCH_REJECT = "monthorderm_batch_reject";
	/**
	 * 不通过
	 */
	public static final String FORM_APP_REJECT = "form_app_reject";
	/**
	 * 反审批
	 */
	public static final String FORM_APP_REVERSEAPPROVE = "form_app_reverseApprove";
	/**
	 * 编辑
	 */
	public static final String FORM_APP_EDIT = "form_app_edit";
	/**
	 * 提审
	 */
	public static final String ANNUALORDERM_SUBMIT = "annualorderm_submit";
	/**
	 * 通过
	 */
	public static final String ANNUALORDERM_APPROVE = "annualorderm_approve";
	/**
	 * 不通过
	 */
	public static final String ANNUALORDERM_REJECT = "annualorderm_reject";
	/**
	 * 批量审批通过
	 */
	public static final String ANNUALORDERM_BATCH_APPROVE = "annualorderm_batch_approve";
	/**
	 * 批量审批不通过
	 */
	public static final String ANNUALORDERM_BATCH_REJECT = "annualorderm_batch_reject";
	/**
	 * 提审
	 */
	public static final String WEEKORDERM_SUBMIT = "weekorderm_submit";
	/**
	 * 通过
	 */
	public static final String WEEKORDERM_APPROVE = "weekorderm_approve";
	/**
	 * 不通过
	 */
	public static final String WEEKORDERM_REJECT = "weekorderm_reject";
	/**
	 * 批量审核通过
	 */
	public static final String WEEKORDERM_BATCH_APPROVE = "weekorderm_batch_approve";
	/**
	 * 批量审核不通过
	 */
	public static final String WEEKORDERM_BATCH_REJECT = "weekorderm_batch_reject";
	/**
	 * 提审
	 */
	public static final String MANUALORDERM_SUBMIT = "manualorderm_submit";
	/**
	 * 通过
	 */
	public static final String MANUALORDERM_APPROVE = "manualorderm_approve";
	/**
	 * 不通过
	 */
	public static final String MANUALORDERM_REJECT = "manualorderm_reject";
	/**
	 * 批量审核通过
	 */
	public static final String MANUALORDERM_BATCH_APPROVE = "manualorderm_batch_approve";
	/**
	 * 批量审核不通过
	 */
	public static final String MANUALORDERM_BATCH_REJECT = "manualorderm_batch_reject";
	/**
	 * 反审核
	 */
	public static final String MONTHORDERM_REVERSE_APPROVE = "monthorderm_reverse_approve";
	/**
	 * 提审
	 */
	public static final String FORMUPLOADM_SUBMIT = "formuploadm_submit";
	/**
	 * 审核通过
	 */
	public static final String FORMUPLOADM_APPROVE = "formuploadm_approve";
	/**
	 * 审核不通过
	 */
	public static final String FORMUPLOADM_REJECT = "formuploadm_reject";
	/**
	 * 反审核
	 */
	public static final String FORMUPLOADM_REVERSE_APPROVE = "formuploadm_reverse_approve";
	/**
	 * 批量审核通过
	 */
	public static final String FORMUPLOADM_BATCH_APPROVE = "formuploadm_batch_approve";
	/**
	 * 批量审核不通过
	 */
	public static final String FORMUPLOADM_BATCH_REJECT = "formuploadm_batch_reject";
	/**
	 * 反审批
	 */
	public static final String ANNUALORDERM_REVERSE_APPROVE = "annualorderm_reverse_approve";
	/**
	 * 批量反审批
	 */
	public static final String ANNUALORDERM_BATCH_REVERSE_APPROVE = "annualorderm_batch_reverse_approve";
	/**
	 * 反审
	 */
	public static final String WEEKORDERM_REVERSE_APPROVE = "weekorderm_reverse_approve";
	/**
	 * 批量反审
	 */
	public static final String WEEKORDERM_BATCH_REVERSE_APPROVE = "weekorderm_batch_reverse_approve";
	/**
	 * 批量反审
	 */
	public static final String MONTHORDERM_BATCH_REVERSE_APPROVE = "monthorderm_batch_reverse_approve";
	/**
	 * 反审
	 */
	public static final String MANUALORDERM_REVERSE_APPROVE = "manualorderm_reverse_approve";
	/**
	 * 批量反审
	 */
	public static final String MANUALORDERM_BATCH_REVERSE_APPROVE = "manualorderm_batch_reverse_approve";
	/**
	 * 提审
	 */
	public static final String SUBCONTRACTSALEORDERM_SUBMIT = "subcontractsaleorderm_submit";
	/**
	 * 通过
	 */
	public static final String SUBCONTRACTSALEORDERM_APPROVE = "subcontractsaleorderm_approve";
	/**
	 * 不通过
	 */
	public static final String SUBCONTRACTSALEORDERM_REJECT = "subcontractsaleorderm_reject";
	/**
	 * 批量审核通过
	 */
	public static final String SUBCONTRACTSALEORDERM_BATCH_APPROVE = "subcontractsaleorderm_batch_approve";
	/**
	 * 批量审核不通过
	 */
	public static final String SUBCONTRACTSALEORDERM_BATCH_REJECT = "subcontractsaleorderm_batch_reject";
	/**
	 * 反审
	 */
	public static final String SUBCONTRACTSALEORDERM_REVERSE_APPROVE = "subcontractsaleorderm_reverse_approve";
	/**
	 * 批量反审
	 */
	public static final String SUBCONTRACTSALEORDERM_BATCH_REVERSE_APPR = "subcontractsaleorderm_batch_reverse_appr";
	/**
	 * 撤回审核处理
	 */
	public static final String MONTHORDERM_WITHDRAW = "monthorderm_withdraw";
	/**
	 * 撤销提审
	 */
	public static final String EXPENSEBUDGET_WITHDRAW = "expensebudget_withdraw";
	/**
	 * 查看审批
	 */
	public static final String EXPENSE_FORMAPPROVALFLOW_INDEX = "expense_formapprovalflow_index";
	/**
	 * 撤销审核
	 */
	public static final String INVESTMENTPLAN_WITHDRAW = "investmentplan_withdraw";
	/**
	 * 查看审批
	 */
	public static final String INVESTMENT_FORMAPPROVALFLOW_INDEX = "investment_formapprovalflow_index";
	/**
	 * 查看审批
	 */
	public static final String PURCHASEM_FORMAPPROVALFLOW_INDEX = "purchasem_formapprovalflow_index";
	/**
	 * 查看审批
	 */
	public static final String PROPOSALM_FORMAPPROVALFLOW_INDEX = "proposalm_formapprovalflow_index";
	/**
	 * 提审
	 */
	public static final String SYSPURECEIVE_SUBMIT = "sysPureceive_submit";
	/**
	 * 通过
	 */
	public static final String SYSPURECEIVE_APPROVE = "sysPureceive_approve";
	/**
	 * 不通过
	 */
	public static final String SYSPURECEIVE_REJECT = "sysPureceive_reject";
	/**
	 * 反审
	 */
	public static final String SYSPURECEIVE_REVERSE_APPROVE = "sysPureceive_reverse_approve";
	/**
	 * 批量通过
	 */
	public static final String SYSPURECEIVE_BATCH_APPROVE = "sysPureceive_batch_approve";
	/**
	 * 批量不通过
	 */
	public static final String SYSPURECEIVE_BATCH_REJECT = "sysPureceive_batch_reject";
	/**
	 * 批量通过
	 */
	public static final String FORM_APP_BATCH_APPROVE = "form_app_batch_approve";
	/**
	 * 批量不通过
	 */
	public static final String FORM_APP_BATCH_REJECT = "form_app_batch_reject";
	/**
	 * 批量撤销
	 */
	public static final String FORM_APP_BATCH_BACKOUT = "form_app_batch_backout";
	/**
	 * 撤回
	 */
	public static final String ANNUALORDERM_WITHDRAW = "annualorderm_withdraw";
	/**
	 * 撤回
	 */
	public static final String WEEKORDERM_WITHDRAW = "weekorderm_withdraw";
	/**
	 * 撤回
	 */
	public static final String MANUALORDERM_WITHDRAW = "manualorderm_withdraw";
	/**
	 * 撤回
	 */
	public static final String SUBCONTRACTSALEORDERM_WITHDRAW = "subcontractsaleorderm_withdraw";
	/**
	 * 撤回审核
	 */
	public static final String FORMUPLOADM_WITHDRAW = "formuploadm_withdraw";
	/**
	 * 提审
	 */
	public static final String MATERIALSOUT_SUBMIT = "materialsOut_submit";
	/**
	 * 通过
	 */
	public static final String MATERIALSOUT_APPROVE = "materialsOut_approve";
	/**
	 * 不通过
	 */
	public static final String MATERIALSOUT_REJECT = "materialsOut_reject";
	/**
	 * 批量审核通过
	 */
	public static final String MATERIALSOUT_BATCH_APPROVE = "materialsOut_batch_approve";
	/**
	 * 批量审核不通过
	 */
	public static final String MATERIALSOUT_BATCH_REJECT = "materialsOut_batch_reject";
	/**
	 * 反审核
	 */
	public static final String MATERIALSOUT_REVERSE_APPROVE = "materialsOut_reverse_approve";
	/**
	 * 撤回
	 */
	public static final String SYSPUINSTORE_WITHDRAW = "syspuinstore_withdraw";
	/**
	 * 提审
	 */
	public static final String SYSPUINSTORE_SUBMIT = "syspuinstore_submit";
	/**
	 * 审核通过
	 */
	public static final String SYSPUINSTORE_APPROVE = "syspuinstore_approve";
	/**
	 * 审核不通过
	 */
	public static final String SYSPUINSTORE_REJECT = "syspuinstore_reject";
	/**
	 * 批量审核通过
	 */
	public static final String SYSPUINSTORE_BATCH_APPROVE = "syspuinstore_batch_approve";
	/**
	 * 反审核
	 */
	public static final String SYSPUINSTORE_REVERSE_APPROVE = "syspuinstore_reverse_approve";
	/**
	 * 撤回
	 */
	public static final String MATERIALSOUT_WITHDRAW = "materialsOut_withdraw";
	/**
	 * 批量审核不通过
	 */
	public static final String SYSPUINSTORE_BATCH_REJECT = "syspuinstore_batch_reject";
	/**
	 * 撤回
	 */
	public static final String SYSPURECEIVE_WITHDRAW = "sysPureceive_withdraw";
	/**
	 * 提审
	 */
	public static final String OTHEROUT_SUBMIT = "otherOut_submit";
	/**
	 * 通过
	 */
	public static final String OTHEROUT_APPROVE = "otherOut_approve";
	/**
	 * 不通过
	 */
	public static final String OTHEROUT_REJECT = "otherOut_reject";
	/**
	 * 批量审核通过
	 */
	public static final String OTHEROUT_BATCH_APPROVE = "otherOut_batch_approve";
	/**
	 * 批量审核不通过
	 */
	public static final String OTHEROUT_BATCH_REJECT = "otherOut_batch_reject";
	/**
	 * 反审核
	 */
	public static final String OTHEROUT_REVERSE_APPROVE = "otherOut_reverse_approve";
	/**
	 * 撤回
	 */
	public static final String OTHEROUT_WITHDRAW = "otherOut_withdraw";
	/**
	 * 提审
	 */
	public static final String TRANSVOUCH_SUBMIT = "transVouch_submit";
	/**
	 * 通过
	 */
	public static final String TRANSVOUCH_APPROVE = "transVouch_approve";
	/**
	 * 不通过
	 */
	public static final String TRANSVOUCH_REJECT = "transVouch_reject";
	/**
	 * 批量审核通过
	 */
	public static final String TRANSVOUCH_BATCH_APPROVE = "transVouch_batch_approve";
	/**
	 * 批量审核不通过
	 */
	public static final String TRANSVOUCH_BATCH_REJECT = "transVouch_batch_reject";
	/**
	 * 反审核
	 */
	public static final String TRANSVOUCH_REVERSE_APPROVE = "transVouch_reverse_approve";
	/**
	 * 撤回
	 */
	public static final String TRANSVOUCH_WITHDRAW = "transVouch_withdraw";
	/**
	 * 失效
	 */
	public static final String INVESTMENT_PLAN_MANAGE_UNEFFECT = "investment_plan_manage_uneffect";
	/**
	 * 失效
	 */
	public static final String EXPENSE_BUDGET_MANAGE_UNEFFECT = "expense_budget_manage_uneffect";
	/**
	 * 失效
	 */
	public static final String PROPOSALM_UNEFFECT = "proposalm_uneffect";
	/**
	 * 提审
	 */
	public static final String FORM_APP_SUBMIT = "form_app_submit";
	/**
	 * 提审
	 */
	public static final String MATERIALRETURN_SUBMIT = "materialreturn_submit";
	/**
	 * 通过
	 */
	public static final String MATERIALRETURN_APPROVE = "materialreturn_approve";
	/**
	 * 不通过
	 */
	public static final String MATERIALRETURN_REJECT = "materialreturn_reject";
	/**
	 * 批量审核通过
	 */
	public static final String MATERIALRETURN_BATCH_APPROVE = "materialreturn_batch_approve";
	/**
	 * 批量审核不通过
	 */
	public static final String MATERIALRETURN_BATCH_REJECT = "materialreturn_batch_reject";
	/**
	 * 反审核
	 */
	public static final String MATERIALRETURN_REVERSE_APPROVE = "materialreturn_reverse_approve";
	/**
	 * 撤回
	 */
	public static final String MATERIALRETURN_WITHDRAW = "materialreturn_withdraw";
	/**
	 * 提审
	 */
	public static final String CURRENT_STOCK_SUBMIT = "current_stock_submit";
	/**
	 * 通过
	 */
	public static final String CURRENT_STOCK_APPROVE = "current_stock_approve";
	/**
	 * 不通过
	 */
	public static final String CURRENT_STOCK_APPROVE = "current_stock_approve";
	/**
	 * 反审核
	 */
	public static final String CURRENT_STOCK_REVERSE_APPROVE = "current_stock_reverse_approve";
}