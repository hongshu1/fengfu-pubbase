### 支持在java源代码目录中定义sql模板，便于快速定位
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

#namespace("foreigncurrency")
#include("foreigncurrency/foreigncurrency.sql")
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

#namespace("inventory")
#include("inventory/inventory.sql")
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

#namespace("containerclass")
#include("containerclass/containerclass.sql")
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

#namespace("spotcheckformitem")
#include("spotcheckformitem/spotcheckformitem.sql")
#end

#namespace("spotcheckformparam")
#include("spotcheckformparam/spotcheckformparam.sql")
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

#namespace("fitemss97")
#include("fitemss97/fitemss97.sql")
#end

#namespace("fitemss97class")
#include("fitemss97class/fitemss97class.sql")
#end

#namespace("fitemss97sub")
#include("fitemss97sub/fitemss97sub.sql")
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

#namespace("rcvdocqcformm")
#include("rcvdocqcformm/rcvdocqcformm.sql")
#end

#namespace("bomcompare")
#include("bomcompare/bomcompare.sql")
#end

#namespace("manualorderm")
#include("manualorderm/manualorderm.sql")
#end

#namespace("containerstockind")
#include("containerstockind/containerstockd.sql")
#end

#namespace("weekorderm")
#include("weekorderm/weekorderm.sql")
#end

#namespace("weekorderd")
#include("weekorderd/weekorderd.sql")
#end

#namespace("qcformparam")
#include("qcformparam/qcformparam.sql")
#end

#namespace("qcformtableparam")
#include("qcformtableparam/qcformtableparam.sql")
#end

#namespace("subcontractsaleorderm")
#include("subcontractsaleorderm/subcontractsaleorderm.sql")
#end

#namespace("subcontractsaleorderd")
#include("subcontractsaleorderd/subcontractsaleorderd.sql")
#end

#namespace("routing")
#include("routing/routing.sql")
#end

#namespace("rcvdocdefect")
#include("rcvdocdefect/rcvdocdefect.sql")
#end

#namespace("processdefect")
#include("processdefect/processdefect.sql")
#end

#namespace("stockoutqcformm")
#include("stockoutqcformm/stockoutqcformm.sql")
#end

#namespace("demandplanm")
#include("demandplanm/demandplanm.sql")
#end

#namespace("cusordersum")
#include("cusordersum/cusordersum.sql")
#end

#namespace("cusorderresult")
#include("cusorderresult/cusorderresult.sql")
#end

#namespace("demandpland")
#include("demandpland/demandpland.sql")
#end

#namespace("purchaseorderm")
#include("purchaseorderm/purchaseorderm.sql")
#end

#namespace("purchaseorderref")
#include("purchaseorderref/purchaseorderref.sql")
#end

#namespace("purchaseorderdbatch")
#include("purchaseorderdbatch/purchaseorderdbatch.sql")
#end

#namespace("purchaseorderdbatchversion")
#include("purchaseorderdbatchversion/purchaseorderdbatchversion.sql")
#end

#namespace("purchaseorderdqty")
#include("purchaseorderdqty/purchaseorderdqty.sql")
#end

#namespace("purchaseorderd")
#include("purchaseorderd/purchaseorderd.sql")
#end

#namespace("subcontractorderm")
#include("subcontractorderm/subcontractorderm.sql")
#end

#namespace("subcontractorderd")
#include("subcontractorderd/subcontractorderd.sql")
#end

#namespace("subcontractorderref")
#include("subcontractorderref/subcontractorderref.sql")
#end

#namespace("subcontractorderdqty")
#include("subcontractorderdqty/subcontractorderdqty.sql")
#end

#namespace("subcontractorderdbatch")
#include("subcontractorderdbatch/subcontractorderdbatch.sql")
#end

#namespace("subcontractorderdbatchversion")
#include("subcontractorderdbatchversion/subcontractorderdbatchversion.sql")
#end

#namespace("stockoutdefect")
#include("stockoutdefect/stockoutdefect.sql")
#end

#namespace("instockdefect")
#include("instockdefect/instockdefect.sql")
#end

#namespace("qcinspection")
#include("qcinspection/qcinspection.sql")
#end

#namespace("modoc")
#include("modoc/modoc.sql")
#end

#namespace("modocbatch")
#include("modocbatch/modocbatch.sql")
#end


#namespace("rcvplanm")
#include("rcvplanm/rcvplanm.sql")
#end

#namespace("goodspaymentm")
#include("goodspaymentm/goodspaymentm.sql")
#end


#namespace("schedudemandplan")
#include("schedudemandplan/schedudemandplan.sql")
#end

#namespace("instockqcformm")
#include("instockqcformm/instockqcformm.sql")
#end

#namespace("sysotherin")
#include("sysotherin/sysotherin.sql")
#end


#namespace("otherout")
#include("otherout/otherout.sql")
#end


#namespace("sysproductin")
#include("sysproductin/sysproductin.sql")
#end

#namespace("sysassem")
#include("sysassem/sysassem.sql")
#end

#namespace("syspuinstore")
#include("syspuinstore/syspuinstore.sql")
#end

#namespace("syspureceive")
#include("syspureceive/syspureceive.sql")
#end


#namespace("auditformconfig")
#include("auditformconfig/auditformconfig.sql")
#end

#namespace("approvalform")
#include("approvalform/approvalform.sql")
#end

#namespace("approvald")
#include("approvald/approvald.sql")
#end

#namespace("formapproval")
#include("formapproval/formapproval.sql")
#end

#namespace("formapprovald")
#include("formapprovald/formapprovald.sql")
#end

#namespace("transvouch")
#include("transvouch/transvouch.sql")
#end


#namespace("sysenumeration")
#include("sysenumeration/sysenumeration.sql")
#end


#namespace("materialsprepare")
#include("sysmaterialsprepare/materialsprepare.sql")
#end

#namespace("materialsout")
#include("materialsout/materialsout.sql")
#end

#namespace("scancodereceive")
#include("scancodereceive/scancodereceive.sql")
#end

###出库列表
#namespace("sysSaleDeliver")
#include("syssaledeliver/sysSaleDeliver.sql")
#end

#namespace("syssaledeliverplan")
#include("syssaledeliverplan/syssaledeliverplan.sql")
#end

#namespace("splitbarcode")
#include("splitbarcode/splitbarcode.sql")
#end

#namespace("mergebarcode")
#include("mergebarcode/mergebarcode.sql")
#end

#namespace("otherdeliverylist")
#include("otherdeliverylist/otherdeliverylist.sql")
#end

#namespace("materialreturnlist")
#include("materialreturnlist/materialreturnlist.sql")
#end

#namespace("patchworkbarcode")
#include("patchworkbarcode/patchworkbarcode.sql")
#end

#namespace("otheroutreturnlist")
#include("otheroutreturnlist/otheroutreturnlist.sql")
#end

#namespace("stockcheckvouch")
#include("stockcheckvouch/stockcheckvouch.sql")
#end

#namespace("momaterialsscansum")
#include("momaterialsscansum/momaterialsscansum.sql")
#end

#namespace("sysscandeliver")
#include("sysscandeliver/sysscandeliver.sql")
#end

#namespace("sysscandeliverone")
#include("sysscandeliverone/sysscandeliverone.sql")
#end

#namespace("warehousebeginofperiod")
#include("warehousebeginofperiod/warehousebeginofperiod.sql")
#end

#namespace("moroutingconfigoperation")
#include("moroutingconfigoperation/moroutingconfigoperation.sql")
#end

#namespace("vouchtypedic")
#include("vouchtypedic/vouchtypedic.sql")
#end

#namespace("rdstyle")
#include("rdstyle/rdstyle.sql")
#end

#namespace("expensebudget")
#include("expensebudget/expensebudget.sql")
#end

#namespace("expensebudgetitem")
#include("expensebudgetitem/expensebudgetitem.sql")
#end

#namespace("expensebudgetitemd")
#include("expensebudgetitemd/expensebudgetitemd.sql")
#end

#namespace("proposalcategory")
#include("proposalcategory/proposalcategory.sql")
#end

#namespace("period")
#include("period/period.sql")
#end

#namespace("barcodeencodingd")
#include("barcodeencodingd/barcodeencodingd.sql")
#end

#namespace("barcodeencodingm")
#include("barcodeencodingm/barcodeencodingm.sql")
#end

#namespace("expensebudgetmanage")
#include("expensebudgetmanage/expensebudgetmanage.sql")
#end

#namespace("purchasem")
#include("purchasem/purchasem.sql")
#end

#namespace("proposalm")
#include("proposalm/proposalm.sql")
#end

#namespace("proposald")
#include("proposald/proposald.sql")
#end

#namespace("investmentplan")
#include("investmentplan/investmentplan.sql")
#end

#namespace("investmentplanitem")
#include("investmentplanitem/investmentplanitem.sql")
#end

#namespace("investmentplanitemd")
#include("investmentplanitemd/investmentplanitemd.sql")
#end

#namespace("investmentplanmanage")
#include("investmentplanmanage/investmentplanmanage.sql")
#end

#namespace("subjectm")
#include("subjectm/subjectm.sql")
#end

#namespace("projectcard")
#include("projectcard/projectcard.sql")
#end

#namespace("appenditem")
#include("appenditem/appenditem.sql")
#end

#namespace("expensemonthadjustmentm")
#include("expensemonthadjustmentm/expensemonthadjustmentm.sql")
#end

#namespace("expensemonthadjustmentd")
#include("expensemonthadjustmentd/expensemonthadjustmentd.sql")
#end

#namespace("investmentplanmonthadjustmentm")
#include("investmentplanmonthadjustmentm/investmentplanmonthadjustmentm.sql")
#end

#namespace("investmentplanmonthadjustmentitem")
#include("investmentplanmonthadjustmentitem/investmentplanmonthadjustmentitem.sql")
#end

#namespace("investmentplanmonthadjustmentitemd")
#include("investmentplanmonthadjustmentitemd/investmentplanmonthadjustmentitemd.sql")
#end

#namespace("project")
#include("project/project.sql")
#end

#namespace("exch")
#include("exch/exch.sql")
#end

#namespace("formuploadcategory")
#include("formuploadcategory/formuploadcategory.sql")
#end

#namespace("momaterialscanusedlog")
#include("momaterialscanusedlog/momaterialscanusedlog.sql")
#end

#namespace("formuploadm")
#include("formuploadm/formuploadm.sql")
#end

#namespace("formuploadd")
#include("formuploadd/formuploadd.sql")
#end

#namespace("depref")
#include("depref/depref.sql")
#end

#namespace("form")
#include("form/form.sql")
#end

#namespace("momoinvbatch")
#include("momoinvbatch/momoinvbatch.sql")
#end

#namespace("bomm")
#include("bomm/bomm.sql")
#end

#namespace("bomd")
#include("bomd/bomd.sql")
#end


#namespace("momaterialsreturnm")
#include("momaterialsreturnm/momaterialsreturnm.sql")
#end

#namespace("inventoryworkregion")
#include("inventoryworkregion/inventoryworkregion.sql")
#end

#namespace("commonmenu")
#include("commonmenu/commonmenu.sql")
#end


#namespace("currentstock")
#include("currentstock/currentstock.sql")
#end


#namespace("stockcheckdetail")
#include("stockcheckdetail/stockcheckdetail.sql")
#end

#namespace("formextendfields")
#include("formextendfields/formextendfields.sql")
#end

#namespace("uptimeparam")
#include("uptimeparam/uptimeparam.sql")
#end

#namespace("uptimecategory")
#include("uptimecategory/uptimecategory.sql")
#end

#namespace("prodparam")
#include("prodparam/prodparam.sql")
#end

#namespace("proditem")
#include("proditem/proditem.sql")
#end

#namespace("prodform")
#include("prodform/prodform.sql")
#end

#namespace("prodformitem")
#include("prodformitem/prodformitem.sql")
#end

#namespace("prodformparam")
#include("prodformparam/prodformparam.sql")
#end

#namespace("momopatchweldm")
#include("momopatchweldm/momopatchweldm.sql")
#end

#namespace("prodformm")
#include("prodformm/prodformm.sql")
#end

#namespace("uptimem")
#include("uptimem/uptimem.sql")
#end

#namespace("specmaterialsrcvm")
#include("specmaterialsrcvm/specmaterialsrcvm.sql")
#end

#namespace("uptimetplm")
#include("uptimetplm/uptimetplm.sql")
#end

#namespace("uptimetpltable")
#include("uptimetpltable/uptimetpltable.sql")
#end

#namespace("uptimetplcategory")
#include("uptimetplcategory/uptimetplcategory.sql")
#end

#namespace("spotcheckformm")
#include("spotcheckformm/spotcheckformm.sql")
#end

#namespace("fitem")
#include("fitem/fitem.sql")
#end

#namespace("inventorymfginfo")
#include("inventorymfginfo/inventorymfginfo.sql")
#end

#namespace("codingrulem")
#include("codingrulem/codingrulem.sql")
#end