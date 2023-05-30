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

#sql("whoptions")
select t1.whcode,t1.whname,t1.qty,t1.poscode,t1.posname
from UFDATA_001_2023.dbo.V_Sys_CurrentStock t1;
#end