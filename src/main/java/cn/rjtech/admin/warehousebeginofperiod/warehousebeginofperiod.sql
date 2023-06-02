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

#sql("findByWhCodeAndInvCode")
select * from T_Sys_StockBarcodePosition
where 1=1
#if(whcode)
and whcode = #para(whcode)
#end
#if(invcode)
and invcode = #para(invcode)
#end
#if(poscode)
and poscode = #para(poscode)
#end
#end

#sql("detailDatas")
SELECT t1.autoid,t3.masid,t2.*,t3.Barcode,
       t3.Batch as cbatch,t3.qty as pringqty,t3.BarcodeDate,t3.CreatePerson,t3.CreateDate,t3.ReportFileName,
       t4.cInvName,t4.cInvCode1,t4.cInvName1,t4.iPkgQty,t4.cInvStd,
       t5.cUomCode,t5.cUomName,
       t6.cVenCode,t6.cVenName,
       t7.autoid as positionautoid
from T_Sys_BarcodeMaster t1
         inner join UFDATA_001_2023.dbo.V_Sys_CurrentStock t2 on t1.sourceid = t2.id
         left join T_Sys_BarcodeDetail t3 on t1.autoid = t3.masid
         left join bd_inventory t4 on t2.invcode = t4.cinvcode
         left join Bd_Uom t5 on t4.iInventoryUomId1 = t5.iAutoId
         left join Bd_vendor t6 on t3.VenCode = t6.cVenCode
         left join T_Sys_StockBarcodePosition t7 on t3.invcode = t7.invcode and t3.Barcode = t7.Barcode
where 1=1
#if(sourceid)
and t1.sourceid = #para(sourceid)
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
#if(whcode)
and t1.whcode = #para(whcode)
#end
#end

#sql("findAreaByWhcode")
select t1.*
from Bd_Warehouse_Area t1
where 1=1
order by t1.dUpdateTime desc
#end

#sql("printData")
SELECT t1.autoid,t3.masid,t2.*,t3.Barcode,
       t3.Batch as cbatch,t3.qty as pringqty,t3.BarcodeDate,t3.CreatePerson,t3.CreateDate,t3.ReportFileName,
       t4.cInvName,t4.cInvCode1,t4.cInvName1,t4.iPkgQty,t4.cInvStd,
       t5.cUomCode,t5.cUomName,
       t6.cVenCode,t6.cVenName,
       t7.autoid as positionautoid
from T_Sys_BarcodeMaster t1
         inner join UFDATA_001_2023.dbo.V_Sys_CurrentStock t2 on t1.sourceid = t2.id
         left join T_Sys_BarcodeDetail t3 on t1.autoid = t3.masid
         left join bd_inventory t4 on t2.invcode = t4.cinvcode
         left join Bd_Uom t5 on t4.iInventoryUomId1 = t5.iAutoId
         left join Bd_vendor t6 on t3.VenCode = t6.cVenCode
         left join T_Sys_StockBarcodePosition t7 on t3.invcode = t7.invcode and t3.Barcode = t7.Barcode
    where 1=1
#if(ids)
         and CHARINDEX(','+cast((select t1.autoid) as nvarchar(20))+',' , ','+#para(ids)+',') > 0
#end
order by t3.CreateDate
#end


#sql("warehouseareaoptions")
SELECT
wa.cAreaCode,wa.cAreaName,wa.iWarehouseId
FROM
Bd_Warehouse_Area wa
where wa.isEnabled = '1' and wa.isDeleted = '0'
    #if(iwarehouseid)
    wa.iWarehouseId = #para(iwarehouseid)
    #end
order by wa.dUpdateTime desc
#end