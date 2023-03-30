### 支持在Java源代码目录中定义sql模板，便于快速定位
### 定义方式
### 1. 在模块包中声明sql模板文件
### 2. 在本文件中引入
### 以下示例以org模块为例

#namespace("busobject")
#include("busobject/busobject.sql")
#end

#namespace("userorg")
#include("userorg/userorg.sql")
#end

#namespace("buttonpermission")
#include("buttonpermission/buttonpermission.sql")
#end

#namespace("datapermission")
#include("datapermission/datapermission.sql")
#end

#namespace("emailreceiver")
#include("emailreceiver/emailreceiver.sql")
#end

#namespace("menupermission")
#include("menupermission/menupermission.sql")
#end

#namespace("workclass")
#include("workclass/workclass.sql")
#end

#namespace("operation")
#include("operation/operation.sql")
#end

#namespace("qcitem")
#include("qcitem/qcitem.sql")
#end

#namespace("equipment")
#include("equipment/equipment.sql")
#end

#namespace("qcparam")
#include("qcparam/qcparam.sql")
#end

#namespace("warehouseshelves")
#include("warehouseshelves/warehouseshelves.sql")
#end

#namespace("forgeigncurrency")
#include("forgeigncurrency/forgeigncurrency.sql")
#end


#namespace("warehouseposition")
#include("warehouseposition/warehouseposition.sql")
#end

#namespace("warehousearea")
#include("warehousearea/warehousearea.sql")
#end

#namespace("warehouse")
#include("warehouse/warehouse.sql")
#end

#namespace("workshiftd")
#include("workshiftd/workshiftd.sql")
#end

#namespace("workshiftm")
#include("workshiftm/workshiftm.sql")
#end

#namespace("monthweekproductionschedule")
#include("monthweekproductionschedule/monthweekproductionschedule.sql")
#end

#namespace("workregionm")
#include("workregionm/workregionm.sql")
#end

#namespace("person")
#include("person/person.sql")
#end

#namespace("personequipment")
#include("personequipment/personequipment.sql")
#end

#namespace("org")
#include("org/org.sql")
#end

#namespace("settlestyle")
#include("settlestyle/settlestyle.sql")
#end


#namespace("department")
#include("department/department.sql")
#end


#namespace("equipment_model")
#include("equipmentmodel/equipment_model.sql")
#end

#namespace("uom")
#include("uom/uom.sql")
#end

#namespace("inventoryclass")
#include("inventoryclass/inventoryclass.sql")
#end

#namespace("inventorychange")
#include("inventorychange/inventorychange.sql")
#end

#namespace("container")
#include("container/container.sql")
#end

#namespace("fitemss97class")
#include("fitemss97class/fitemss97class.sql")
#end

#namespace("fitemss97")
#include("fitemss97/fitemss97.sql")
#end

#namespace("fitemss97sub")
#include("fitemss97sub/fitemss97sub.sql")
#end

#namespace("vouchrdcontrapose")
#include("vouchrdcontrapose/vouchrdcontrapose.sql")
#end

#namespace("qcform")
#include("qcform/qcform.sql")
#end

#namespace("qcformitem")
#include("qcformitem/qcformitem.sql")
#end