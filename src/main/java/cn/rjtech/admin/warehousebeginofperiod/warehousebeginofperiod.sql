#sql("datas")
select t1.*,
       t2.cInvName,t2.cInvCode1,t2.cInvName1,t2.iPkgQty,t2.cInvStd,t2.cBarcode,
       t3.cUomCode,t3.cUomName,
       t4.cInvCCode,t4.cInvCName,
       t6.cAreaCode,t6.cAreaName
from UFDATA_001_2023.dbo.V_Sys_CurrentStock t1
left join bd_inventory t2 on t1.invcode = t2.cInvCode
left join Bd_Uom t3 on t2.iInventoryUomId1 = t3.iAutoId
left join bd_inventoryclass t4 on t2.iAutoId = t4.iPid
left join Bd_Warehouse t5 on t1.whcode = t5.cwhcode
left join Bd_Warehouse_Area t6 on  t5.iautoid = t6.iWarehouseId
#end

#sql("detailDatas")
SELECT t1.*,t2.*,t3.*
from T_Sys_BarcodeMaster t1
left join T_Sys_BarcodeDetail t2 on t1.autoid = t2.masid
left join T_Sys_StockBarcodePosition t3 on t2.Invcode = t3.Invcode and t2.Barcode = t3.Barcode
where 1=1
#if(autoid)
t1.autoid = #para(autoid)
#end
order by t1.ModifyDate desc
#end

#sql("whoptions")
select t1.*,
       t2.cInvName,t2.cInvCode1,t2.cInvAddCode,t2.cInvName1,t2.cInvStd,t2.iPkgQty,t2.iInventoryClassId,
       t3.cUomName
from UFDATA_001_2023.dbo.V_Sys_CurrentStock t1
left join bd_inventory t2 on t1.invcode = t2.cInvCode
left join Bd_Uom t3 on t2.iInventoryUomId1 = t3.iAutoId
where 1=1
#end

#sql("findAreaByWhcode")
select t2.cAreaCode,t2.cAreaName,t2.iWarehouseId
from Bd_Warehouse t1
left join Bd_Warehouse_Area t2 on t1.iAutoId = t2.iWarehouseId
where 1=1
#if(cwhcode)
and t1.cwhcode = #para(cwhcode)
#end
#end