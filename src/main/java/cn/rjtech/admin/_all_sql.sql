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

#namespace("customerclass")
#include("customerclass/customerclass.sql")
#end

#namespace("customer")
#include("customer/customer.sql")
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

#namespace("vendor")
#include("vendor/vendor.sql")
#end

#namespace("vendorclass")
#include("vendorclass/vendorclass.sql")
#end

#namespace("vendoraddr")
#include("vendoraddr/vendoraddr.sql")
#end

#namespace("calendar")
#include("calendar/calendar.sql")
#end

#namespace("spotcheckparam")
#include("spotcheckparam/spotcheckparam.sql")
#end

#namespace("inventoryspotcheckform")
#include("inventoryspotcheckform/inventoryspotcheckform.sql")
#end

#namespace("spotcheckform")
#include("spotcheckform/spotcheckform.sql")
#end

###采购类型
#namespace("purchasetype")
#include("purchasetype/purchasetype.sql")
#end

#namespace("annualorderm")
#include("annualorderm/annualorderm.sql")
#end

#namespace("annualorderd")
#include("annualorderd/annualorderd.sql")
#end

#namespace("customeraddr")
#include("customeraddr/customeraddr.sql")
#end

#namespace("descflexfielddef")
#include("descflexfielddef/descflexfielddef.sql")
#end

#namespace("fitemss97")
#include("fitemss97/fitemss97.sql")
#end

#namespace("fitemss97class")
#include("fitemss97class/fitemss97class.sql")
#end

#namespace("fitemss97sub")
#include("fitemss97sub/fitemss97sub.sql")
#end

#namespace("foreigncurrency")
#include("foreigncurrency/foreigncurrency.sql")
#end

#namespace("qcform")
#include("qcform/qcform.sql")
#end

#namespace("qcformitem")
#include("qcformitem/qcformitem.sql")
#end

#namespace("saletype")
#include("saletype/saletype.sql")
#end

#namespace("scheduproductplan")
#include("scheduproductplan/scheduproductplan.sql")
#end

#namespace("monthorderm")
#include("monthorderm/monthorderm.sql")
#end

#namespace("monthorderd")
#include("monthorderd/monthorderd.sql")
#end

#namespace("inventoryqcform")
#include("inventoryqcform/inventoryqcform.sql")
#end

#namespace("vouchrdcontrapose")
#include("vouchrdcontrapose/vouchrdcontrapose.sql")
#end

#namespace("yearproductionsummary")
#include("yearproductionsummary/yearproductionsummary.sql")
#end

#namespace("pad")
#include("pad/pad.sql")
#end

#namespace("padloginlog")
#include("padloginlog/padloginlog.sql")
#end

#namespace("bommaster")
#include("bommaster/bommaster.sql")
#end

#namespace("manualorderm")
#include("manualorderm/manualorderm.sql")
#end

#namespace("containerStockInD")
#include("containerStockInD/containerStockD.sql")
#end

#namespace("subcontractsaleorderm")
#include("subcontractsaleorderm/subcontractsaleorderm.sql")
#end

#namespace("subcontractsaleorderd")
#include("subcontractsaleorderd/subcontractsaleorderd.sql")
#end


#namespace("qcformparam")
#include("qcformparam/qcformparam.sql")
#end

#namespace("qcformtableparam")
#include("qcformtableparam/qcformtableparam.sql")
#end


