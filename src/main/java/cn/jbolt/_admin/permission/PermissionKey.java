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
	 * 生产日历
	 */
	public static final String WORKCALENDARM = "workcalendarm";
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
	 * 工位档案
	 */
	public static final String WORKPOSITION = "workposition";
	/**
	 * 班组档案
	 */
	public static final String TEAM = "team";
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
	public static final String SUPPLIER = "supplier";
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
	 * 料品档案
	 */
	public static final String ITEMCATEGORY = "itemcategory";
	/**
	 * U9生产计划
	 */
	public static final String SCHEDUSOURCEORDER = "schedusourceorder";
	/**
	 * 物料清单
	 */
	public static final String BOMMASTER = "bommaster";
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
	 * 不良项目
	 */
	public static final String BADNESS_PROJECT = "badness_project";
	/**
	 * 工序
	 */
	public static final String OPERATION = "operation";
	/**
	 * 工艺档案
	 */
	public static final String ITEMROUTING = "itemrouting";
	/**
	 * 排程工作日历
	 */
	public static final String CALENDAR = "calendar";
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
	 * 设备维修
	 */
	public static final String REPAIR_APPLY_RECORD = "repair_apply_record";
	/**
	 * 设备点检
	 */
	public static final String EQUIPMENTQCRECORD = "equipmentqcrecord";
	/**
	 * 设备保养
	 */
	public static final String KEEPINGRECORD = "keepingrecord";
	/**
	 * 工装档案
	 */
	public static final String WORKCLOTHES = "workclothes";
	/**
	 * 模具档案
	 */
	public static final String MOULDS = "moulds";
	/**
	 * 夹具档案
	 */
	public static final String FIXTURE = "fixture";
	/**
	 * 检具档案
	 */
	public static final String QCTOOLS = "qctools";
	/**
	 * 质量管理
	 */
	public static final String QUALITY = "quality";
	/**
	 * 工装管理
	 */
	public static final String MOULDSOTHERS = "mouldsothers";
	/**
	 * 工装保养
	 */
	public static final String KEEPINGRECORDMOULDS = "keepingrecordmoulds";
	/**
	 * 不良事项
	 */
	public static final String TPM_BADNESS_MODEL = "tpm_badness_model";
	/**
	 * 维修不良
	 */
	public static final String TPM_BADNESS = "tpm_badness";
	/**
	 * 点检事项
	 */
	public static final String EQUIPMENTQC = "equipmentqc";
	/**
	 * 点检参数
	 */
	public static final String EQUIPMENTQCPARAM = "equipmentqcparam";
	/**
	 * 点检标准
	 */
	public static final String EQUIPMENTQCBASIS = "equipmentqcbasis";
	/**
	 * 其他事项
	 */
	public static final String NOME = "nome";
	/**
	 * 维修人档案
	 */
	public static final String REPAIRMAN = "repairman";
	/**
	 * 保养人档案
	 */
	public static final String CARINGMAN = "caringman";
	/**
	 * 采购备件库存
	 */
	public static final String REPAIR_SPARE_PARTS = "repair_spare_parts";
	/**
	 * 检验配置
	 */
	public static final String QC_SETTING = "qc_setting";
	/**
	 * 审核人设置
	 */
	public static final String APPROVAL_PROCESS = "approval_process";
	/**
	 * 载具管理
	 */
	public static final String UIHIG = "uihig";
	/**
	 * 载具档案
	 */
	public static final String LOADMOULD = "loadmould";
	/**
	 * 检验参数
	 */
	public static final String PARAMETER = "parameter";
	/**
	 * 保养事项
	 */
	public static final String MAINTAIN = "maintain";
	/**
	 * 保养参数
	 */
	public static final String MAINTAIN_PARAMETER = "maintain_parameter";
	/**
	 * 检验标准
	 */
	public static final String BASIS = "basis";
	/**
	 * 保养标准
	 */
	public static final String MAINTAIN_STANDARD = "maintain_standard";
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
	 * 工艺档案工位集
	 */
	public static final String ITEM_ROUTING_WORKPOSITION = "item_routing_workposition";
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
	 * 检具图片
	 */
	public static final String QCTOOLS_IMG = "qctools_img";
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
	 * 设备档案文件
	 */
	public static final String EQUIPMENTDRAWING = "equipmentdrawing";
	/**
	 * 设备点检纪录
	 */
	public static final String EQUIPMENTQCRECORDLINE = "equipmentqcrecordline";
	/**
	 * 料品附加档案
	 */
	public static final String ITEMAPPEND = "itemappend";
	/**
	 * 料品组合关系明细
	 */
	public static final String ITEMCOMBINED = "itemcombined";
	/**
	 * 料品库存档案
	 */
	public static final String ITEMINVENTORYINFO = "iteminventoryinfo";
	/**
	 * 料品载具信息
	 */
	public static final String ITEMLOADMOULDS = "itemloadmoulds";
	/**
	 * 料品生产档案
	 */
	public static final String ITEMMFGINFO = "itemmfginfo";
	/**
	 * 料品图片配置
	 */
	public static final String ITEMPICTURE = "itempicture";
	/**
	 * 料品计划档案
	 */
	public static final String ITEMPLAN = "itemplan";
	/**
	 * 工艺不良项目
	 */
	public static final String ITEMROUTINGBADNESS = "itemroutingbadness";
	/**
	 * 料品工艺档案配置
	 */
	public static final String ITEMROUTINGCONFIG = "itemroutingconfig";
	/**
	 * 工艺方法
	 */
	public static final String ITEMROUTINGCONFIGOPERATION = "itemroutingconfigoperation";
	/**
	 * 工艺文件
	 */
	public static final String ITEMROUTINGDRAWING = "itemroutingdrawing";
	/**
	 * 设备集
	 */
	public static final String ITEMROUTINGEQUIPMENT = "itemroutingequipment";
	/**
	 * 工序物料集
	 */
	public static final String ITEMROUTINGINVC = "itemroutinginvc";
	/**
	 * 工艺工装集
	 */
	public static final String ITEMROUTINGMOULDS = "itemroutingmoulds";
	/**
	 * 保养纪录文件
	 */
	public static final String KEEPINGRECORDDRAWING = "keepingrecorddrawing";
	/**
	 * 保养记录行
	 */
	public static final String KEEPINGRECORDLINE = "keepingrecordline";
	/**
	 * 生产返工单工序
	 */
	public static final String MOREWORKOPERATION = "moreworkoperation";
	/**
	 * 生产返工单工序物料集
	 */
	public static final String MOREWORKOPERATIONINVC = "moreworkoperationinvc";
	/**
	 * 模具图片
	 */
	public static final String MOULDSIMG = "mouldsimg";
	/**
	 * 模具档案配置
	 */
	public static final String MOULDSRECORD = "mouldsrecord";
	/**
	 * 工艺方法不良项目关联配置
	 */
	public static final String OPERATIONBADNESS = "operationbadness";
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
	 * 检具料品
	 */
	public static final String QCTOOLSITEM = "qctoolsitem";
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
	 * 供应商主表
	 */
	public static final String SUPPLIERM = "supplierm";
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
	 * 工位档案关联配置
	 */
	public static final String WORKPOSITIONEM = "workpositionem";
	/**
	 * 导出
	 */
	public static final String WAREHOUSE_SHELVES_EXPORT = "warehouse_shelves_export";
	/**
	 * 导入
	 */
	public static final String WAREHOUSE_SHELVES_IMPORT = "warehouse_shelves_import";
	/**
	 * 备用工位关联配置
	 */
	public static final String WORKPOSITIONSPARE = "workpositionspare";
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
	public static final String WORKPOSITION_ADD = "workposition_add";
	/**
	 * 编辑
	 */
	public static final String WORKPOSITION_EDIT = "workposition_edit";
	/**
	 * 删除
	 */
	public static final String WORKPOSITION_DELETE = "workposition_delete";
	/**
	 * 导出
	 */
	public static final String WORKPOSITION_EXPORT = "workposition_export";
	/**
	 * 导入
	 */
	public static final String WORKPOSITION_IMPORT = "workposition_import";
	/**
	 * 打印
	 */
	public static final String WORKPOSITION_PRINT = "workposition_print";
	/**
	 * 新增
	 */
	public static final String TEAM_ADD = "team_add";
	/**
	 * 编辑
	 */
	public static final String TEAM_EDIT = "team_edit";
	/**
	 * 删除
	 */
	public static final String TEAM_DELETE = "team_delete";
	/**
	 * 导出
	 */
	public static final String TEAM_EXPORT = "team_export";
	/**
	 * 导入
	 */
	public static final String TEAM_IMPORT = "team_import";
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
	public static final String SUPPLIER_ADD = "supplier_add";
	/**
	 * 编辑
	 */
	public static final String SUPPLIER_EDIT = "supplier_edit";
	/**
	 * 删除
	 */
	public static final String SUPPLIER_DELETE = "supplier_delete";
	/**
	 * 导出
	 */
	public static final String SUPPLIER_EXPORT = "supplier_export";
	/**
	 * 导入
	 */
	public static final String SUPPLIER_IMPORT = "supplier_import";
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
	 * 新增
	 */
	public static final String ITEMCATEGORY_ADD = "itemcategory_add";
	/**
	 * 编辑
	 */
	public static final String ITEMCATEGORY_EDIT = "itemcategory_edit";
	/**
	 * 删除
	 */
	public static final String ITEMCATEGORY_DELETE = "itemcategory_delete";
	/**
	 * 导出
	 */
	public static final String ITEMCATEGORY_EXPORT = "itemcategory_export";
	/**
	 * 导入
	 */
	public static final String ITEMCATEGORY_IMPORT = "itemcategory_import";
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
	 * 新增
	 */
	public static final String MAINTAIN_PARAMETER_ADD = "maintain_parameter_add";
	/**
	 * 导入
	 */
	public static final String WORKCLASS_IMPORT = "workclass_import";
	/**
	 * 删除
	 */
	public static final String WORKCLASS_DELETE = "workclass_delete";
	/**
	 * 编辑
	 */
	public static final String MAINTAIN_PARAMETER_EDIT = "maintain_parameter_edit";
	/**
	 * 删除
	 */
	public static final String MAINTAIN_PARAMETER_DELETE = "maintain_parameter_delete";
	/**
	 * 导出
	 */
	public static final String MAINTAIN_PARAMETER_EXPORT = "maintain_parameter_export";
	/**
	 * 新增
	 */
	public static final String BADNESS_PROJECT_ADD = "badness_project_add";
	/**
	 * 导出
	 */
	public static final String BADNESS_PROJECT_EXPORT = "badness_project_export";
	/**
	 * 导入
	 */
	public static final String MAINTAIN_PARAMETER_IMPORT = "maintain_parameter_import";
	/**
	 * 导入
	 */
	public static final String BADNESS_PROJECT_IMPORT = "badness_project_import";
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
	 * 导入
	 */
	public static final String MAINTAIN_STANDARD_IMPORT = "maintain_standard_import";
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
	 * 导出
	 */
	public static final String EQUIPMENT_EXPORT = "equipment_export";
	/**
	 * 导入
	 */
	public static final String EQUIPMENT_IMPORT = "equipment_import";
	/**
	 * 编辑
	 */
	public static final String MAINTAIN_STANDARD_EDIT = "maintain_standard_edit";
	/**
	 * 删除
	 */
	public static final String MAINTAIN_STANDARD_DELETE = "maintain_standard_delete";
	/**
	 * 导出
	 */
	public static final String MAINTAIN_STANDARD_EXPORT = "maintain_standard_export";
	/**
	 * 入库
	 */
	public static final String EQUIPMENT_ENTER = "equipment_enter";
	/**
	 * 新增
	 */
	public static final String MAINTAIN_STANDARD_ADD = "maintain_standard_add";
	/**
	 * 出库
	 */
	public static final String EQUIPMENT_OUT = "equipment_out";
	/**
	 * 新增
	 */
	public static final String LOADMOULD_ADD = "loadmould_add";
	/**
	 * 库存记录
	 */
	public static final String EQUIPMENT_STORAGE = "equipment_storage";
	/**
	 * 编辑
	 */
	public static final String LOADMOULD_EDIT = "loadmould_edit";
	/**
	 * 删除
	 */
	public static final String LOADMOULD_DELETE = "loadmould_delete";
	/**
	 * 导出
	 */
	public static final String LOADMOULD_EXPORT = "loadmould_export";
	/**
	 * 导入
	 */
	public static final String LOADMOULD_IMPORT = "loadmould_import";
	/**
	 * 打印
	 */
	public static final String LOADMOULD_PRINT = "loadmould_print";
	/**
	 * 入库
	 */
	public static final String LOADMOULD_ENTER = "loadmould_enter";
	/**
	 * 出库
	 */
	public static final String LOADMOULD_OUT = "loadmould_out";
	/**
	 * 库存记录
	 */
	public static final String LOADMOULD_STOCK = "loadmould_stock";
	/**
	 * 新增
	 */
	public static final String REPAIRMAN_ADD = "repairman_add";
	/**
	 * 导出
	 */
	public static final String REPAIRMAN_EXPORT = "repairman_export";
	/**
	 * 导入
	 */
	public static final String REPAIRMAN_IMPORT = "repairman_import";
	/**
	 * 入库
	 */
	public static final String MOULDS_ENTER = "moulds_enter";
	/**
	 * 出库
	 */
	public static final String MOULDS_OUT = "moulds_out";
	/**
	 * 库存记录
	 */
	public static final String MOULDS_STORAGE = "moulds_storage";
	/**
	 * 编辑
	 */
	public static final String MOULDS_EDIT = "moulds_edit";
	/**
	 * 导出
	 */
	public static final String MOULDS_EXPORT = "moulds_export";
	/**
	 * 入库
	 */
	public static final String FIXTURE_ENTER = "fixture_enter";
	/**
	 * 出库
	 */
	public static final String FIXTURE_OUT = "fixture_out";
	/**
	 * 库存记录
	 */
	public static final String FIXTURE_STORAGE = "fixture_storage";
	/**
	 * 编辑
	 */
	public static final String FIXTURE_EDIT = "fixture_edit";
	/**
	 * 导出
	 */
	public static final String FIXTURE_EXPORT = "fixture_export";
	/**
	 * 新增
	 */
	public static final String QCTOOLS_ADD = "qctools_add";
	/**
	 * 编辑
	 */
	public static final String QCTOOLS_EDIT = "qctools_edit";
	/**
	 * 删除
	 */
	public static final String QCTOOLS_DELETE = "qctools_delete";
	/**
	 * 入库
	 */
	public static final String QCTOOLS_ENTER = "qctools_enter";
	/**
	 * 出库
	 */
	public static final String QCTOOLS_OUT = "qctools_out";
	/**
	 * 库存记录
	 */
	public static final String QCTOOLS_STORAGE = "qctools_storage";
	/**
	 * 导出
	 */
	public static final String QCTOOLS_EXPORT = "qctools_export";
	/**
	 * 导入
	 */
	public static final String QCTOOLS_IMPORT = "qctools_import";
	/**
	 * 新增
	 */
	public static final String TPM_BADNESS_ADD = "tpm_badness_add";
	/**
	 * 导出
	 */
	public static final String TPM_BADNESS_EXPORT = "tpm_badness_export";
	/**
	 * 导入
	 */
	public static final String TPM_BADNESS_IMPORT = "tpm_badness_import";
	/**
	 * 新增
	 */
	public static final String EQUIPMENTQCPARAM_ADD = "equipmentqcparam_add";
	/**
	 * 导出
	 */
	public static final String EQUIPMENTQCPARAM_EXPORT = "equipmentqcparam_export";
	/**
	 * 导入
	 */
	public static final String EQUIPMENTQCPARAM_IMPORT = "equipmentqcparam_import";
	/**
	 * 新增
	 */
	public static final String EQUIPMENTQCBASIS_ADD = "equipmentqcbasis_add";
	/**
	 * 导出
	 */
	public static final String EQUIPMENTQCBASIS_EXPORT = "equipmentqcbasis_export";
	/**
	 * 导入
	 */
	public static final String EQUIPMENTQCBASIS_IMPORT = "equipmentqcbasis_import";
	/**
	 * 新增
	 */
	public static final String CARINGMAN_ADD = "caringman_add";
	/**
	 * 导出
	 */
	public static final String CARINGMAN_EXPORT = "caringman_export";
	/**
	 * 导入
	 */
	public static final String CARINGMAN_IMPORT = "caringman_import";
	/**
	 * 新增
	 */
	public static final String REPAIR_SPARE_PARTS_ADD = "repair_spare_parts_add";
	/**
	 * 导出
	 */
	public static final String REPAIR_SPARE_PARTS_EXPORT = "repair_spare_parts_export";
	/**
	 * 导入
	 */
	public static final String REPAIR_SPARE_PARTS_IMPORT = "repair_spare_parts_import";
	/**
	 * 新增
	 */
	public static final String APPROVAL_PROCESS_ADD = "approval_process_add";
	/**
	 * 导出
	 */
	public static final String APPROVAL_PROCESS_EXPORT = "approval_process_export";
	/**
	 * 导入
	 */
	public static final String APPROVAL_PROCESS_IMPORT = "approval_process_import";
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
	 * 设备委外维修验收保存
	 */
	public static final String REPAIR_APPLY_ISSUE_ASSIGN_ACCEPT_SAVE = "repair_apply_issue_assign_accept_save";
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
	 * 排程物料BOM
	 */
	public static final String BOMMASTERFILTER = "bommasterfilter";
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
	 * 设备记录
	 */
	public static final String EQUIPMENTRECORD = "equipmentrecord";
	/**
	 * 维修派工
	 */
	public static final String REPAIR_APPLY_ASSIGN = "repair_apply_assign";
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
	 * 料品档案
	 */
	public static final String ITEMMASTER = "itemmaster";
	/**
	 * 新增委外维修处理
	 */
	public static final String REPAIR_APPLY_ISSUE_ASSIGN_ADD = "repair_apply_issue_assign_add";
	/**
	 * 委外维修下达
	 */
	public static final String REPAIR_APPLYISSUE_ORDER = "repair_applyissue_order";
	/**
	 * 委外维修审核
	 */
	public static final String REPAIR_APPLY_ISSUE_ASSIGN_AUDIT = "repair_apply_issue_assign_audit";
	/**
	 * 存货条码管理
	 */
	public static final String INVENTORY = "inventory";
	/**
	 * 委外条码管理
	 */
	public static final String OM = "om";
	/**
	 * 审批定义明细
	 */
	public static final String VERIFYDEFINED = "verifydefined";
	/**
	 * 计量单位档案
	 */
	public static final String UOM = "uom";
	/**
	 * 采购条码管理
	 */
	public static final String PURCHASE = "purchase";
	/**
	 * 维修申请委外分配
	 */
	public static final String REPAIR_APPLY_ISSUE_ASSIGN = "repair_apply_issue_assign";
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
	 * 报修审核
	 */
	public static final String REPAIR_APPLY_RECORD_REPAIRAUDIT = "repair_apply_record_repairaudit";
	/**
	 * 位置档案
	 */
	public static final String LOCLISTCN = "loclistcn";
	/**
	 * 派工工艺文件记录
	 */
	public static final String ASSIGNDRAWINGRECORD = "assigndrawingrecord";
	/**
	 * 审批进度
	 */
	public static final String VERIFYPROGRESS = "verifyprogress";
	/**
	 * 不良项目分类
	 */
	public static final String BADNESS_PROJECT_CLASS = "badness_project_class";
	/**
	 * 敏感词词库
	 */
	public static final String SENSITIVE_WORD = "sensitive_word";
	/**
	 * 设备分类
	 */
	public static final String EQUIPMENTCLASS = "equipmentclass";
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
	 * 工艺档案工位集
	 */
	public static final String ITEMROUTINGWORKPOSITION = "itemroutingworkposition";
	/**
	 * 维修记录
	 */
	public static final String REPAIR_RECORD = "repair_record";
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
	 * 维修申请记录ID
	 */
	public static final String API_REPAIR_APPLY_RECORD_WIHDRAW = "api_repair_apply_record_wihdraw";
	/**
	 * 维修申请审核记录
	 */
	public static final String REPAIR_APPLY_AUDIT_RECORD = "repair_apply_audit_record";
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
	 * 检具档案
	 */
	public static final String APPLYAUDITRECORD = "applyauditrecord";
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
	 * 编辑
	 */
	public static final String REPAIR_APPLY_RECORD_EDIT1 = "repair_apply_record_edit1";
	/**
	 * 新增返工单1
	 */
	public static final String PRODUCTION_TICKETS_ADD = "production_tickets_add";
	/**
	 * 导入
	 */
	public static final String PARAMETER_IMPORT = "parameter_import";
	/**
	 * 设备委外维修
	 */
	public static final String REPAIR_APPLY_ISSUE = "repair_apply_issue";
	/**
	 * 通用检验标准
	 */
	public static final String RANGEBASIS1 = "rangebasis1";
	/**
	 * 删除
	 */
	public static final String MOULDS_DELETE = "moulds_delete";
	/**
	 * 获取U9
	 */
	public static final String MOULDS_U9 = "moulds_U9";
	/**
	 * 生产计划分析
	 */
	public static final String SCHEDUSOURCEAPPORTIONU9 = "schedusourceapportionu9";
	/**
	 * 设备调拨记录
	 */
	public static final String EQUIPMENTALLOCATION = "equipmentallocation";
	/**
	 * 工装调拨记录
	 */
	public static final String MOULDSOTHERSALLOCATION = "mouldsothersallocation";
	/**
	 * 来料检验单
	 */
	public static final String RCVRPTDOC = "rcvrptdoc";
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
	 * 保存
	 */
	public static final String APPVERSION_SAVE = "appversion_save";
	/**
	 * 更新
	 */
	public static final String APPVERSION_UPDATE = "appversion_update";
	/**
	 * 改变发布状态
	 */
	public static final String APPVERSION_PUBLISH = "appversion_publish";
	/**
	 * 是否强制更新
	 */
	public static final String APPVERSION_ISFORCE = "appversion_isforce";
	/**
	 * 人员档案
	 */
	public static final String PERSON_INDEX = "person_index";	
	/**
	 * 币种档案
	 */
	public static final String FORGEIGN_CURRENCY_INDEX = "forgeign_currency_index";	

	/**
	 * 实体扩展字段
	 */
	public static final String DESCFLEXFIELDDEF = "descflexfielddef";

	/**
	 * 实体扩展字段值集
	 */
	public static final String DESCFLEXSEGVALUESETVALUE = "descflexsegvaluesetvalue";

	/**
	 * 人员档案
	 */
	public static final String PERSON = "person";

	/**
	 * 新增
	 */
	public static final String WAREHOUSE_ADD = "warehouse_add";
	/**
	 * 机型档案
	 */
	public static final String EQUIPMENT_MODEL = "equipment_model";
}