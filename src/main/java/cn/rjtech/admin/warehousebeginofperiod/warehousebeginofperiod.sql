#sql("datas")
SELECT t2.MasID,t2.AutoID,t2.VenCode,t2.Barcode,
       t2.Invcode,t2.BarcodeDate,t2.Batch,t2.Qty,t2.PrintNum,
       t2.ReportFileName,t2.CreatePerson,t2.CreateDate,t2.ModifyDate,t2.ModifyPerson,
       t3.WhCode,t7.cWhName,t3.PosCode,t6.cAreaName,
       t4.cInvName,t4.cInvCode1,t4.cInvName1,t4.iPkgQty,t4.cInvStd,t4.iInventoryClassId,
       t5.cUomCode,t5.cUomName,t8.cInvCName
from T_Sys_BarcodeMaster t1
         LEFT JOIN T_Sys_BarcodeDetail t2 on t1.AutoID = t2.MasID
         LEFT JOIN T_Sys_StockBarcodePosition t3 on t2.Barcode = t3.Barcode and t2.Batch = t3.Batch
         LEFT JOIN bd_inventory t4 on t2.Invcode = t4.cInvCode
         LEFT JOIN Bd_Uom t5 on t4.iInventoryUomId1 = t5.iAutoId
         LEFT JOIN Bd_Warehouse_Area t6 on t3.PosCode = t6.cAreaCode
         LEFT JOIN Bd_Warehouse t7 on t3.WhCode = t7.cWhCode
         LEFT JOIN Bd_InventoryClass t8 on t4.iInventoryClassId = t8.iAutoId
WHERE 1=1 and t1.memo = '仓库期初'
#if(organizecode)
  and t1.organizecode = #para(organizecode)
#end
#if(cinvcode)
  and t2.Invcode = #para(cinvcode)
#end
#if(cinvcode1)
  and t4.cinvcode1 = #para(cinvcode1)
#end
#if(cinvname1)
  and t4.cinvname1 = #para(cinvname1)
#end
#if(whcode)
  and t3.whcode = #para(whcode)
#end
#if(careacode)
  and t3.poscode = #para(careacode)
#end
#if(iinventoryclassid)
  and t4.iInventoryClassId = #para(iinventoryclassid)
#end
#if(starttime)
 and t1.ModifyDate >= #para(starttime)
#end
#if(endtime)
 and t1.ModifyDate <= #para(endtime)
#end
#end

#sql("findGeneratedstockqtyByCodes")
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
#if(organizecode)
  and organizecode = #para(organizecode)
#end
#end

#sql("detailDatas")
SELECT
    t2.AutoID,
    t2.MasID,
    t2.VenCode,
    t2.Barcode,
    t2.Invcode,
    t2.BarcodeDate,
    t2.SourceBillType,
    t2.Batch,
    t2.Qty,
    t2.PrintNum,
    t2.ReportFileName,
    t2.CreatePerson,
    t2.CreateDate,
    t2.ModifyDate,
    t2.ModifyPerson,
    t3.WhCode,
    t3.PosCode,
    t4.cInvName,
    t4.cInvCode1,
    t4.cInvName1,
    t4.iPkgQty,
    t4.cInvStd,
    t5.cUomCode,
    t5.cUomName,
    t6.cvenname
from T_Sys_BarcodeMaster t1
         LEFT JOIN T_Sys_BarcodeDetail t2 on t1.AutoID = t2.MasID
         LEFT JOIN T_Sys_StockBarcodePosition t3 on t2.Barcode = t3.Barcode and t2.Batch = t3.Batch
         LEFT JOIN bd_inventory t4 on t2.Invcode = t4.cInvCode
         LEFT JOIN Bd_Uom t5 on t4.iInventoryUomId1 = t5.iAutoId
         LEFT JOIN Bd_vendor t6 on t2.vencode = t6.cvencode
     where 1=1 and t1.memo = '仓库期初'
#if(organizecode)
and t1.organizecode = #para(organizecode)
#end
#if(masid)
and t1.autoid = #para(masid)
#end
#if(autoid)
and t2.autoid = #para(autoid)
#end
#if(starttime)
 and t2.CreateDate >= #para(starttime)
#end
#if(endtime)
 and t2.CreateDate <= #para(endtime)
#end
#if(sourcebilltype)
and t2.sourcebilltype = #para(sourcebilltype)
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
#if(corgcode)
and t1.corgcode = #para(corgcode)
#end
order by t1.dUpdateTime desc
#end

#sql("printData")
SELECT t1.autoid,t3.masid,t3.Barcode as cbarcode,
       t3.Batch as cbatch,t3.qty as planiqty,t3.BarcodeDate,t3.CreatePerson,t3.CreateDate,t3.ReportFileName,t3.PrintNum,
       t4.cInvName,t4.cInvCode1,t4.cInvName1,t4.iPkgQty,t4.cInvStd,
       t5.cUomCode,t5.cUomName,
       t6.cVenCode,t6.cVenName,
       t7.autoid as positionautoid
from T_Sys_BarcodeMaster t1
         left join T_Sys_BarcodeDetail t3 on t1.autoid = t3.masid
         left join bd_inventory t4 on t3.invcode = t4.cinvcode
         left join Bd_Uom t5 on t4.iInventoryUomId1 = t5.iAutoId
         left join Bd_vendor t6 on t3.VenCode = t6.cVenCode
         left join T_Sys_StockBarcodePosition t7 on t3.invcode = t7.invcode and t3.Barcode = t7.Barcode
    where 1=1 and t1.memo = '仓库期初'
#if(ids)
         and CHARINDEX(','+cast((select t3.autoid) as nvarchar(20))+',' , ','+#para(ids)+',') > 0
#end
#if(organizecode)
    and t1.organizecode = #para(organizecode)
#end
order by t3.CreateDate desc
#end

#sql("addPrintData")
SELECT t3.autoid,t3.masid,t3.Barcode as cbarcode,
       t3.Batch as cbatch,t3.qty as planiqty,t3.BarcodeDate,t3.CreatePerson,t3.CreateDate,t3.ReportFileName,t3.PrintNum,
        t4.cInvName,t4.cInvCode1,t4.cInvName1,t4.iPkgQty,t4.cInvStd,
        t5.cUomCode,t5.cUomName,
        t6.cVenCode,t6.cVenName,
        t7.autoid as positionautoid
from T_Sys_BarcodeDetail t3
         left join bd_inventory t4 on t3.invcode = t4.cinvcode
         left join Bd_Uom t5 on t4.iInventoryUomId1 = t5.iAutoId
         left join Bd_vendor t6 on t3.VenCode = t6.cVenCode
         left join T_Sys_StockBarcodePosition t7 on t3.invcode = t7.invcode and t3.Barcode = t7.Barcode
    where 1=1
#if(ids)
         and CHARINDEX(','+cast((select t3.autoid) as nvarchar(20))+',' , ','+#para(ids)+',') > 0
#end
order by t3.CreateDate desc
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

#sql("findPosCodeByWhcodeAndInvcode")
select t1.* from Bd_Warehouse_Position t1
where 1=1
#if(iwarehouseid)
and t1.iWarehouseId = #para(iwarehouseid)
#end
#if(iwarehouseareaid)
and t1.iwarehouseareaid = #para(iwarehouseareaid)
#end
#end

#sql("findBarcodePositionByKvs")
select t1.* from T_Sys_StockBarcodePosition t1
where 1=1
and t1.InvCode = #para(invcode)
and t1.VenCode = #para(vencode)
and t1.WhCode = #para(whcode)
and t1.PosCode = #para(poscode)
and t1.OrganizeCode = #para(organizecode)
and t1.LockType = #para(locksource)
#end

#sql("wareHouseOptions")
SELECT
    wh.*
FROM Bd_Warehouse wh
WHERE wh.isDeleted = 0
    #if(iautoid)
	    AND wh.iautoid = #para(iautoid)
	#end
	#if(cwhcode)
	    AND wh.cWhCode = #para(cwhcode)
	#end
	#if(cwhname)
	    AND wh.cWhName = #para(cwhname)
	#end
    #if(isenabled)
        AND wh.isenabled = #para(isenabled == 'true' ? 1 : 0)
    #end
    #if(q)
    AND (
      wh.cWhCode like concat('%',#para(q),'%') or wh.cWhName like concat('%',#para(q),'%')
        )
    #end
ORDER BY dCreateTime DESC
#end