#sql("CurrentStockByDatas")
select c.*, inv.InvName
from UFDATA_001_2023.dbo.V_Sys_CurrentStock c
left join UFDATA_001_2023.dbo.V_Sys_Inventory inv on c.Invcode = inv.InvCode
where c.OrganizeCode = #para(organizecode) #if(whcode)
and c.whcode =#para(whcode)
#end
#if(poscode)
and c.poscode =#para(poscode)
#end
#if(posname)
and c.posname =#para(posname)
#end
#if(invcode)
and c.invcode like concat ('%',#para(invcode),'%')
#end
#if(invname)
and inv.invname like concat ('%',#para(invname),'%')
#end
#end


#sql("CurrentStockd")
select c.*, inv.InvName
from UFDATA_001_2023.dbo.V_Sys_CurrentStock c
left join UFDATA_001_2023.dbo.V_Sys_Inventory inv on c.Invcode = inv.InvCode
where c.OrganizeCode = #para(organizecode)
#if(whcode)
and c.whcode =#para(whcode)
#end
#if(poscodeSql)
and c.poscode in (#(poscodeSql))
#end
#end


#sql("getdatas")
select t1.*,
                AuditState =
		        CASE
				WHEN t1.iAuditStatus = 0 THEN '盘点中'
				WHEN t1.iAuditStatus = 1 THEN '待审核'
				WHEN t1.iAuditStatus = 2 THEN '已完成'
				WHEN t1.iAuditStatus = 3 THEN '审核不通过' END,
       wh.cWhName AS whname,
       (SELECT SUM(area.iMaxCapacity) FROM Bd_Warehouse_Area area WHERE area.iWarehouseId = wh.iAutoId) AS iMaxCapacity,
       (SELECT SUM(StockQty)FROM T_Sys_StockCheckVouchDetail WHERE MasID = t1.AutoID) AS StockQty,
       COALESCE((SELECT SUM(area.iMaxCapacity) FROM Bd_Warehouse_Area area WHERE area.iWarehouseId = wh.iAutoId),0)-COALESCE((SELECT SUM(StockQty)FROM T_Sys_StockCheckVouchDetail WHERE MasID = t1.AutoID),0) AS MaxQty,
       (SELECT SUM(PlqtyQty)FROM T_Sys_StockCheckVouchDetail WHERE MasID = t1.AutoID) AS PlqtyQty
from T_Sys_StockCheckVouch t1
LEFT JOIN Bd_Warehouse wh ON wh.cWhCode = t1.WhCode
where 1 = 1
  and t1.isDeleted = '0'
  AND t1.OrganizeCode = #para(organizecode)
    #if(billno)
        AND t1.BillNo like '%#(billno)%'
    #end
    #if(checktype)
        AND t1.CheckType = #para(checktype)
    #end
    #if(whname)
        AND wh.cWhName = #para(whname)
    #end
    #if(iorderstatus)
        AND t1.iAuditStatus = #para(iorderstatus)
    #end
    #if(startdate)
        AND CONVERT(VARCHAR(10),t1.dcreatetime,23) >='#(startdate)'
    #end
    #if(enddate)
        AND CONVERT(VARCHAR(10),t1.dcreatetime,23) <='#(enddate)'
    #end
order by t1.dcreatetime desc #end



#sql("datas")
select s.*, w.whname
from #(getMomdataDbName()).dbo.T_Sys_StockCheckVouch s
left join UFDATA_001_2023.dbo.V_Sys_WareHouse w
on s.whcode=w.whcode
where s.isDeleted='0'
#if(checktype)
and s.checktype = #para(checktype)
#end
#if(whname)
and ( w.whname like concat('%', #para(whname), '%')or w.whcode like concat('%' , #para(whname) , '%') )
#end
#if(billno)
and s.billno like concat('%', #para(billno), '%')
#end
#if(sdate)
and s.dcreatetime >= #para(sdate)
#end
#if(edate)
and s.dcreatetime <= #para(edate)
#end
order by dcreatetime desc
#end


#sql("autocompleteWareHouse")
SELECT
    wh.iAutoId,
    wh.cWhCode AS whcode,
    wh.cWhName AS whname
FROM
    Bd_Warehouse wh
WHERE 1=1
    AND  wh.isDeleted = 0
    AND  wh.isEnabled = 1
    AND  wh.cOrgCode = #para(OrgCode)
    #if(q)
        and( w.cWhName like concat ('%',#para(q),'%') or w.cWhCode like concat ('%',#para(q),'%'))
    #end
ORDER BY
    wh.dCreateTime DESC
#end

#sql("autocompletePosition")
SELECT
    p.cAreaCode AS PosCode,
    p.cAreaName AS PosName
FROM
    Bd_Warehouse wh
        LEFT JOIN Bd_Warehouse_Area p ON p.iWarehouseId = wh.iAutoId
WHERE 1=1
        AND p.cOrgCode = #para(OrgCode)
#if(q)
and p.PosName like concat ('%',#para(q),'%')
#end
and wh.cWhCode=#para(whcode)
#end


#sql("autocompleteUser")
SELECT p.id, p.username
FROM  jb_user p
WHERE 1=1
#if(q)
and p.username like concat ('%'
, #para(q)
, '%')
#end
group by p.id, p.username
#end


#sql("invDatas")
select inv.invname,
inv.invstd,
inv.unitName,
inv.InvClassCode,
p.PosName,
d.*
from
#(getMomdataDbName()).dbo.T_Sys_StockCheckVouchDetail d
left join UFDATA_001_2023.dbo.V_Sys_Inventory inv
on inv.invcode=d.invcode
LEFT JOIN  #(getMomdataDbName()).dbo.T_Sys_Position p ON p.PosCode = d.PosCode
where masid=#(mid)
and (p.OrganizeCode=#para(orgcode)
OR p.OrganizeCode IS NULL)
#if(invcode)
and inv.invcode like concat ('%'
, #para(invcode)
, '%')
#end
#if(invname)
and inv.invname like concat ('%'
, #para(invname)
, '%')
#end
#if(posname)
and p.posname like concat ('%', #para(posname), '%')
#end
#if(status)
and d.status like concat ('%', #para(status), '%')
#end


order by d.poscode, d.invcode
#end





#sql("barcodeDatas_total")
select w.whname,
p.posname,
inv.invname,
inv.invstd,
inv.unitName,
t.*

from (select d.invcode,
d.qty,
d.barcode,
d.createdate,
m.whcode,
s.poscode,
COALESCE(d.GenerateType, 'app') AS source,
d.realqty,
d.adjustqty,
d.sourceid,
d.autoid
from
#(getMomdataDbName()).dbo.T_Sys_StockCheckVouch m
left join #(getMomdataDbName()).dbo.T_Sys_StockCheckVouchDetail s
on s.masid=m.autoid
left join #(getMomdataDbName()).dbo.T_Sys_StockCheckDetail d on s.autoid=d.sourceid
where
d.sourceid = #para(stockcheckvouchdid)

union all

SELECT top 10
bc.invcode, bcpos.qty, bcpos.barcode, bcpos.chgdate as createdate, bcpos.whcode, bcpos.poscode, 'web' as source, null as realqty, null as adjustqty, null as sourceid, null as autoid
FROM
#(getMomdataDbName()).dbo.T_Sys_StockBarcodePosition bcpos
INNER JOIN UFDATA_001_2023.dbo.V_Sys_BarcodeDetail bc
ON bc.barcode= bcpos.barcode
where 1=1
and bcpos.whcode=#para(whcode)
and bcpos.poscode=#para(poscode)
and bcpos.invcode=#para(invcode)
and bcpos.barcode not in (select barcode from #(getMomdataDbName()).dbo.T_Sys_StockCheckDetail)) t

LEFT JOIN UFDATA_001_2023.dbo.v_sys_inventory inv ON inv.invcode = t.invcode
left join UFDATA_001_2023.dbo.V_Sys_WareHouse w on w.whcode = t.whcode
LEFT JOIN #(getMomdataDbName()).dbo.T_Sys_Position p
ON p.PosCode = t.PosCode and p.poscode!= null
#end


#sql("invDatasByIds")
select inv.invname,
inv.invstd,
inv.unitName,
inv.InvClassCode,
p.PosName,
d.*
from
#(getMomdataDbName()).dbo.T_Sys_StockCheckVouchDetail d
left join UFDATA_001_2023.dbo.V_Sys_Inventory inv
on inv.invcode=d.invcode
LEFT JOIN  #(getMomdataDbName()).dbo.T_Sys_Position p ON p.PosCode = d.PosCode
where masid in (#(ids)) and ( p.OrganizeCode=#para(orgcode) OR p.OrganizeCode IS NULL)
order by d.poscode, d.invcode
#end


    #sql("paginateAdminDatas")
SELECT
    area.cAreaCode AS poscode,
    area.cAreaName AS posname,
    class.cInvCCode,
    class.cInvCName,
    t3.cInvCode AS InvCode,
    t3.cInvCode1,
    t3.cInvName1,
    t3.cInvStd,
    uom.cUomName,
    t2.Qty,
    t1.CheckType,
    t2.StockQty,
    t2.PlqtyQty,
    t2.StockStatus,
    t2.MasID,
    t2.AutoId
FROM
    T_Sys_StockCheckVouch t1
    LEFT JOIN T_Sys_StockCheckVouchDetail t2 ON t2.MasID = t1.AutoId
    LEFT JOIN Bd_Inventory t3 ON t2.InvCode = t3.cInvCode
        LEFT JOIN Bd_InventoryClass class ON t3.iInventoryClassId = class.iAutoId
        LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = t3.iAutoId
        LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
        LEFT JOIN Bd_Warehouse wh ON wh.iAutoId = config.iWarehouseId
        LEFT JOIN Bd_Uom uom ON uom.iAutoId = t3.iInventoryUomId1
WHERE t3.isDeleted = 0
    #if(autoid)
    AND t1.AutoId = #para(autoid)
    #end
    AND wh.cWhCode = #para(whcode)
    #if(poscode)
    AND area.cAreaCode IN(#(poscode))
    #end
    #if(posname)
    AND area.cAreaName  like '%#(posname)%'
    #end
    #if(cinvcname)
    AND class.cInvCName like '%#(cinvcname)%'
    #end
    #if(invcode)
    AND t3.cInvCode = '#(invcode)'
    #end
     #if(cinvcode1)
    AND t3.cInvCode1 = '#(cinvcode1)'
    #end
     #if(cinvname1)
    AND t3.cInvName1 = '#(cinvname1)'
    #end
    #if(status == '0')
    AND t2.StockStatus is NOT NULL
    AND t2.StockQty = t2.Qty
    #end
    # if(status == '1')
    AND t2.StockStatus is NOT NULL
    AND t2.StockQty != t2.Qty
    #end
    #if(status == '2')
    AND t2.StockStatus is NULL
    AND area.iMaxCapacity > 0
    #end
    #if(status == '3')
    AND t2.StockStatus is NULL
    AND area.iMaxCapacity = 0
    #end
    #if(status == '4')
    AND t2.StockStatus is NULL
    AND area.iMaxCapacity < 0
    #end
    #end

#sql("getStockCheckVouchBarcodeLines")
SELECT
    t2.*,
    wh.cWhCode AS whcode,
    wh.cWhName AS whname,
    area.cAreaCode AS poscode,
    area.cAreaName AS posname,
    class.cInvCCode,
    class.cInvCName,
    i.cInvCode,
    i.cInvCode1,
    i.cInvName1,
    i.cInvStd,
    uom.cUomName,
    t4.iQty,
    t2.Qty,
    t4.cVersion,
    t4.dPlanDate,
    t4.cCompleteBarcode
FROM
    T_Sys_StockCheckVouch t1
        LEFT JOIN T_Sys_StockCheckVouchBarcode t2 ON t2.MasID = t1.AutoId
        LEFT JOIN (
        SELECT
            iAutoId as PoId,
            iPurchaseOrderDid,
            iinventoryId,
            cCompleteBarcode,
            iQty,
            cVersion,
            dPlanDate,
            cSourceld,
            cSourceBarcode
        FROM
            PS_PurchaseOrderDBatch UNION ALL
        SELECT
            iAutoId as PoId,
            iSubcontractOrderDid,
            iinventoryId,
            cCompleteBarcode,
            iQty,
            cVersion,
            dPlanDate,
            cSourceld,
            cSourceBarcode
        FROM
            PS_SubcontractOrderDBatch
    ) t4 ON t2.BarCode = t4.cCompleteBarcode
        LEFT JOIN bd_inventory i ON i.iautoid = t4.iinventoryId
        LEFT JOIN Bd_InventoryClass class ON i.iInventoryClassId = class.iAutoId
        LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = i.iAutoId
        LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
        LEFT JOIN Bd_Warehouse wh ON wh.iAutoId = config.iWarehouseId
        LEFT JOIN Bd_Uom uom ON uom.iAutoId = i.iInventoryUomId1
WHERE
 1 = 1
    AND t1.isDeleted = 0
    #if(OrgCode)
    AND t1.OrganizeCode = #para(OrgCode)
    #end
    #if(autoid)
    AND t2.MasID = #para(autoid)
    #end
    #if(sqlids)
    AND t2.AutoID in (#(sqlids))
    #end
#end

    #sql("barcodeDatas")
SELECT
    wh.cWhCode AS whcode,
    wh.cWhName AS whname,
    area.cAreaCode AS poscode,
    area.cAreaName AS posname,
    class.cInvCCode,
    class.cInvCName,
    t3.cInvCode AS InvCode,
    t3.cInvCode1,
    t3.cInvName1,
    t3.cInvStd,
    uom.cUomName,
    t4.iQty,
    t4.iQty AS Qty,
    t4.cCompleteBarcode AS barcode,
    t4.cVersion,
    t4.dPlanDate

FROM
    Bd_Inventory t3
        LEFT JOIN (
        SELECT
            iAutoId as PoId,
            iPurchaseOrderDid,
            iinventoryId,
            cCompleteBarcode,
            iQty,
            cVersion,
            dPlanDate,
            cSourceld,
            cSourceBarcode
        FROM
            PS_PurchaseOrderDBatch UNION ALL
        SELECT
            iAutoId as PoId,
            iSubcontractOrderDid,
            iinventoryId,
            cCompleteBarcode,
            iQty,
            cVersion,
            dPlanDate,
            cSourceld,
            cSourceBarcode
        FROM
            PS_SubcontractOrderDBatch
    ) t4 ON t3.iautoid = t4.iinventoryId

        LEFT JOIN Bd_InventoryClass class ON t3.iInventoryClassId = class.iAutoId
        LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = t3.iAutoId
        LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
        LEFT JOIN Bd_Warehouse wh ON wh.iAutoId = config.iWarehouseId
        LEFT JOIN Bd_Uom uom ON uom.iAutoId = t3.iInventoryUomId1
WHERE t3.isDeleted = 0
        and wh.cWhCode = #para(whcode)
    #if(poscode)
        and area.cAreaCode IN(#(poscode))
    #end
    #if(barcode)
	    and t4.cCompleteBarcode = #para(barcode)
	#end
	 #if(invcode)
	    and t3.cInvCode = #para(invcode)
	#end
#end


 #sql("Detail")
SELECT t2.*
FROM T_Sys_StockCheckVouch t1
         LEFT JOIN T_Sys_StockCheckVouchDetail t2 ON t2.MasID = t1.AutoId
WHERE t1.AutoId = #para(autoid)
#end


    #sql("WarehouseData")
SELECT
    area.cAreaCode AS poscode,
    area.cAreaName AS posname,
    class.cInvCCode,
    class.cInvCName,
    t3.cInvCode AS InvCode,
    t3.cInvCode1,
    t3.cInvName1,
    t3.cInvStd,
    uom.cUomName,
    area.iMaxCapacity AS Qty
FROM
        Bd_Inventory t3
        LEFT JOIN Bd_InventoryClass class ON t3.iInventoryClassId = class.iAutoId
        LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = t3.iAutoId
        LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
        LEFT JOIN Bd_Warehouse wh ON wh.iAutoId = config.iWarehouseId
        LEFT JOIN Bd_Uom uom ON uom.iAutoId = t3.iInventoryUomId1
WHERE t3.isDeleted = 0

    AND wh.cWhCode = #para(whcode)
    #if(poscode)
    AND area.cAreaCode IN(#(poscode))
    #end
    #if(posname)
    AND area.cAreaName  like '%#(posname)%'
    #end
    #if(cinvcname)
    AND class.cInvCName like '%#(cinvcname)%'
    #end
    #if(invcode)
    AND t3.cInvCode = '#(invcode)'
    #end
     #if(cinvcode1)
    AND t3.cInvCode1 = '#(cinvcode1)'
    #end
     #if(cinvname1)
    AND t3.cInvName1 = '#(cinvname1)'
    #end
    #end

#sql("findCheckVouchBarcodeByMasIdAndInvcode")
select t1.* from T_Sys_StockCheckVouchBarcode t1
where 1=1
    and t1.isDeleted = '0'
    #if(masid)
    and t1.masid = #para(masid)
#end
#if(invcode)
    and t1.invcode = #para(invcode)
#end
#end

#sql("posHouse")
SELECT  a.*,
        a.cAreaCode AS poscode,
        a.cAreaName AS posname
FROM Bd_Warehouse_Area a
where 1=1
    #if(q)
		and (a.cAreaCode like concat('%',#para(q),'%') OR a.cAreaName like concat('%',#para(q),'%'))
	#end
#end

#sql("InventoryClass")
SELECT  class.*
FROM Bd_InventoryClass class
where 1=1
    #if(q)
		and (class.cInvCCode like concat('%',#para(q),'%') OR class.cInvCName like concat('%',#para(q),'%'))
	#end
#end

