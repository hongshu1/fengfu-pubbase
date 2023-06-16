#sql("recpor")
select so.AutoID, CASE so.iAuditStatus
        WHEN 0 THEN
        '未审核'
				WHEN 1 THEN
        '待审核'
				WHEN 2 THEN
        '审核通过'
				WHEN 3 THEN
        '审核不通过'
        END AS statename,so.iAuditStatus as state,so.BillNo as billno, CONVERT(VARCHAR(10), so.dcreatetime, 120) as createdate,so.iAuditStatus,
        so.VenCode as vencode
		,so.ccreatename as name,so.cupdatename as sname,v.cVenName as venname,so.Type as type,so.SourceBillNo
FROM T_Sys_PUReceive so
LEFT JOIN Bd_Vendor v on so.VenCode = v.cVenCode
where so.isDeleted = '0'
	#if(billno)
		and so.BillNo like concat('%',#para(billno),'%')
	#end
	#if(state)
		and so.iAuditStatus = #para(state)
	#end
	#if(startTime)
		and so.dcreatetime >= #para(startTime)
	#end
	#if(endTime)
		and so.dcreatetime <= #para(endTime)
	#end
ORDER BY so.dcreatetime DESC
#end


#sql("dList")
SELECT  a.*,
    m.cOrderNo as sourcebillno,
    aa.cBarcode as barcode,
    b.cInvCode ,
    b.cInvCode1,
    b.cInvName1,
    CONVERT(VARCHAR(10), aa.dPlanDate, 120) as plandate,
    aa.iQty as qtys,
    m.cOrderNo as SourceBillNo,
    m.iBusType as SourceBillType,
    m.cDocNo+'-'+CAST(tc.iseq AS NVARCHAR(10)) as SourceBillNoRow,
    m.cOrderNo as SourceBillID,
    d.iAutoId as SourceBillDid,
    m.iVendorId,
	v.cVenCode as vencode,
	v.cVenName as venname
FROM T_Sys_PUReceiveDetail a
LEFT JOIN PS_PurchaseOrderDBatch aa on aa.cBarcode = a.Barcode
LEFT JOIN Bd_Inventory b on aa.iinventoryId = b.iAutoId
LEFT JOIN PS_PurchaseOrderD d on aa.iPurchaseOrderDid = d.iAutoId
LEFT JOIN PS_PurchaseOrderM m on m.iAutoId = d.iPurchaseOrderMid
LEFT JOIN Bd_Vendor v on m.iVendorId = v.iAutoId
LEFT JOIN PS_PurchaseOrderD_Qty tc on tc.iPurchaseOrderDid = d.iAutoId AND tc.iAutoId = aa.iPurchaseOrderdQtyId
where a.isDeleted = '0'
	#if(masid)
		and a.MasID = #para(masid)
	#end
     #if(spotticket)
    and a.spotticket = #para(spotticket)
     #end
ORDER BY a.dupdatetime DESC
#end


#sql("venCode")
SELECT
    a.cVenCode as vencode,a.cVenName as venname,
    a.*
FROM Bd_Vendor a
where 1=1
	#if(q)
		and (a.cVenCode like concat('%',#para(q),'%') OR a.cVenName like concat('%',#para(q),'%'))
	#end
#end


#sql("Whcode")
SELECT  a.*,a.cWhCode as whcode,a.cWhName as whname,dt.cDepName
FROM Bd_Warehouse a
LEFT JOIN bd_department dt ON a.cDepCode = dt.iAutoId and dt.isDeleted = 0
where a.isDeleted = 0
	#if(q)
		and (a.cWhCode like concat('%',#para(q),'%') OR a.cWhName like concat('%',#para(q),'%'))
	#end
	ORDER BY a.cWhCode DESC
#end


#sql("wareHousepos")
SELECT  a.*
FROM Bd_Warehouse_Area a
LEFT JOIN Bd_Warehouse wh ON a.iWarehouseId = wh.iAutoId
where a.isDeleted = 0
	#if(q)
		and (a.cAreaCode like concat('%',#para(q),'%') OR a.cOrgName like concat('%',#para(q),'%'))
	#end
	#if(whcodeid)
		and a.iWarehouseId = #para(whcodeid)
	#end
#end



#sql("getBarcodeDatas")
select top #(limit)
    m.cOrderNo as sourcebillno,
    a.cBarcode as barcode,
    b.cInvCode ,
    b.cInvCode1,
    b.cInvName1,
	b.cInvName,
    a.dPlanDate as plandate,
    b.cInvStd as cinvstd,
    a.iQty as qty,
    a.iQty as qtys,
    m.cOrderNo as SourceBillNo,
    m.iBusType as SourceBillType,
    m.cDocNo+'-'+CAST(tc.iseq AS NVARCHAR(10)) as SourceBillNoRow,
    m.cOrderNo as SourceBillID,
    d.iAutoId as SourceBillDid,
    m.iVendorId,
	v.cVenCode as vencode,
	v.cVenName as venname,
	uom.cUomCode as purchasecuomcode,uom.cUomName as purchasecuomname
FROM PS_PurchaseOrderDBatch a
LEFT JOIN Bd_Inventory b on a.iinventoryId = b.iAutoId
LEFT JOIN PS_PurchaseOrderD d on a.iPurchaseOrderDid = d.iAutoId
LEFT JOIN PS_PurchaseOrderM m on m.iAutoId = d.iPurchaseOrderMid
LEFT JOIN Bd_Vendor v on m.iVendorId = v.iAutoId
LEFT JOIN T_Sys_PUReceiveDetail pd on pd.Barcode = a.cBarcode AND pd.isDeleted = '0'
LEFT JOIN PS_PurchaseOrderD_Qty tc on tc.iPurchaseOrderDid = d.iAutoId AND tc.iAutoId = a.iPurchaseOrderdQtyId
LEFT JOIN Bd_Uom uom on b.iPurchaseUomId = uom.iAutoId
where a.isEffective = '1'
    #if(q)
		and (b.cinvcode like concat('%',#para(q),'%') or b.cinvcode1 like concat('%',#para(q),'%')
			or b.cinvname1 like concat('%',#para(q),'%') or a.cBarcode like concat('%',#para(q),'%')
			or v.cVenCode like concat('%',#para(q),'%')
		)
	#end

		AND b.cOrgCode = #(orgCode)
        AND pd.AutoID IS NULL

	#if(vencode)
		and m.iVendorId = #para(vencode)
	#end

#end



#sql("getsourcebillno")
SELECT  a.*
FROM V_Sys_PODetail a
where 1=1
	#if(sourcebillno)
		and a.mesbillno = #para(sourcebillno)
	#end
#end




#sql("selectRdCode")
SELECT
    p.*,
    s.iAutoId as iAutoId2,
    s.cRdName,
    s.cRdCode
FROM
    Bd_PurchaseType p
        LEFT JOIN Bd_Rd_Style s ON s.cRdCode = p.cRdCode
WHERE p.IsDeleted = '0'
#if(cPTCode)
  and p.cPTCode like CONCAT('%', #para(cPTCode), '%')
#end
#if(cPTName)
and p.cPTName like CONCAT('%', #para(cPTName), '%')
#end
#end



#sql("tuibarcode")
select
    m.cOrderNo as sourcebillno,
    a.cBarcode as barcode,
    a.cBarcode as spotticket,
    b.cInvCode as invcode,
    b.cinvname,
    b.cInvCode ,
    b.cInvCode1,
    b.cInvName1,
    a.dPlanDate as plandate,
    b.cInvStd as cinvstd,
    a.iQty as qtys,
    a.iQty as qty,
    a.iinventoryId,
    b.iAutoId,
    m.cOrderNo as SourceBillNo,
    m.iBusType as SourceBillType,
    m.cDocNo+'-'+CAST(tc.iseq AS NVARCHAR(10)) as SourceBillNoRow,
    m.cOrderNo as SourceBillID,
    d.iAutoId as SourceBillDid,
    m.iVendorId,
	v.cVenCode as vencode,
	v.cVenName as venname,
    uom.cUomCode,uom.cUomName as purchasecuomname,uom.cUomName as  puunitname
FROM PS_PurchaseOrderDBatch a
LEFT JOIN Bd_Inventory b on a.iinventoryId = b.iAutoId
LEFT JOIN PS_PurchaseOrderD d on a.iPurchaseOrderDid = d.iAutoId
LEFT JOIN PS_PurchaseOrderM m on m.iAutoId = d.iPurchaseOrderMid
LEFT JOIN Bd_Vendor v on m.iVendorId = v.iAutoId
LEFT JOIN PS_PurchaseOrderD_Qty tc on tc.iPurchaseOrderDid = d.iAutoId AND tc.iAutoId = a.iPurchaseOrderdQtyId
LEFT JOIN T_Sys_PUReceiveDetail pd on pd.Barcode = a.cBarcode  AND pd.isDeleted = '0'
LEFT JOIN Bd_Uom uom on b.iPurchaseUomId = uom.iAutoId
where a.isEffective = '1'

	#if(barcode)
		and a.cBarcode = #para(barcode)
	#end

#end


#sql("barcode")
select
    m.cOrderNo as sourcebillno,
    a.cBarcode as barcode,
    a.cBarcode as spotticket,
    b.cInvCode as invcode,
    b.cinvname,
    b.cInvCode ,
    b.cInvCode1,
    b.cInvName1,
    a.dPlanDate as plandate,
    b.cInvStd as cinvstd,
    a.iQty as qtys,
    a.iQty as qty,
    a.iinventoryId,
    b.iAutoId,
    m.cOrderNo as SourceBillNo,
    m.iBusType as SourceBillType,
    m.cDocNo+'-'+CAST(tc.iseq AS NVARCHAR(10)) as SourceBillNoRow,
    m.cOrderNo as SourceBillID,
    d.iAutoId as SourceBillDid,
    m.iVendorId,
	v.cVenCode as vencode,
	v.cVenName as venname,
    uom.cUomCode,uom.cUomName as purchasecuomname,uom.cUomName as  puunitname
FROM PS_PurchaseOrderDBatch a
LEFT JOIN Bd_Inventory b on a.iinventoryId = b.iAutoId
LEFT JOIN PS_PurchaseOrderD d on a.iPurchaseOrderDid = d.iAutoId
LEFT JOIN PS_PurchaseOrderM m on m.iAutoId = d.iPurchaseOrderMid
LEFT JOIN Bd_Vendor v on m.iVendorId = v.iAutoId
LEFT JOIN PS_PurchaseOrderD_Qty tc on tc.iPurchaseOrderDid = d.iAutoId AND tc.iAutoId = a.iPurchaseOrderdQtyId
LEFT JOIN T_Sys_PUReceiveDetail pd on pd.Barcode = a.cBarcode  AND pd.isDeleted = '0'
LEFT JOIN Bd_Uom uom on b.iPurchaseUomId = uom.iAutoId
where a.isEffective = '1'

	#if(barcode)
		and a.cBarcode = #para(barcode)
	#end
        AND pd.AutoID IS NULL

#end

#sql("paginateAdminDatas")
SELECT wa.*,wh.cWhCode,wh.cWhName
FROM
	Bd_Warehouse_Area wa
LEFT JOIN Bd_Warehouse wh ON wa.iWarehouseId = wh.iAutoId
WHERE wa.isDeleted = 0
    AND wa.cAreaCode = #para(careacode)

#end

#sql("barcodeDatas")
SELECT *
FROM T_Sys_PUReceiveDetail
WHERE isDeleted = 0
   	#if(barcode)
		and Barcode = #para(barcode)
	#end
#end


#sql("purchaseOrderD")
select
    d.*,
    m.*,
    m.cOrderNo as sourcebillno,
    a.cBarcode as barcode,
    b.cInvCode ,
    b.cInvCode1,
    b.cInvName1,
    a.dPlanDate as plandate,
    b.cInvStd as cinvstd,
    a.iQty as qtys,
    a.iQty as qty,
    m.cOrderNo as SourceBillNo,
    m.iBusType as SourceBillType,
    m.cDocNo+'-'+CAST(tc.iseq AS NVARCHAR(10)) as SourceBillNoRow,
    m.iAutoId as SourceBillID,
    d.iAutoId as SourceBillDid,
    m.iVendorId,
	v.cVenCode as vencode,
	v.cVenName as venname,
	u.cUomCode as puunitcode,
	u.cUomName as puunitname,
	p.iAutoId,
	s.iAutoId as siautoid,
    s.cRdName as scrdname,
    s.cRdCode as scrdcode,
    dep.cdepcode,dep.cdepname,
    a.iinventoryId
FROM PS_PurchaseOrderDBatch a
LEFT JOIN Bd_Inventory b on a.iinventoryId = b.iAutoId
LEFT JOIN PS_PurchaseOrderD d on a.iPurchaseOrderDid = d.iAutoId
LEFT JOIN PS_PurchaseOrderM m on m.iAutoId = d.iPurchaseOrderMid
LEFT JOIN Bd_Vendor v on m.iVendorId = v.iAutoId
LEFT JOIN PS_PurchaseOrderD_Qty tc on tc.iPurchaseOrderDid = d.iAutoId AND tc.iAutoId = a.iPurchaseOrderdQtyId
LEFT JOIN Bd_Uom u on b.iPurchaseUomId = u.iAutoId
LEFT JOIN Bd_PurchaseType p on p.iAutoId = m.iPurchaseTypeId
LEFT JOIN Bd_Rd_Style s ON s.cRdCode = p.cRdCode
left join Bd_Department dep on m.idepartmentid = dep.iautoid
where 1=1
   	#if(barcode)
		and a.cBarcode = #para(barcode)
	#end
#end


#sql("inventoryMfgInfo")
select top 1 o.*
from Bd_InventoryMfgInfo o
LEFT JOIN Bd_Inventory y on y.iAutoId = o.iInventoryId
where 1=1
   	#if(cinvcode)
		and y.cInvCode = #para(cinvcode)
	#end

order by o.iAutoId DESC

#end


#sql("InventoryQcForm")
SELECT  t1.iAutoId,qc.cQcFormName,
inv.cInvCode, inv.cInvCode1, inv.cInvName, inv.cInvName1, inv.cInvStd, uom.cUomName,t1.cDcCode,
t2.cEquipmentModelName as machineName
FROM Bd_InventoryQcForm t1
INNER JOIN Bd_QcForm qc ON qc.iAutoId = t1.iQcFormId
INNER JOIN Bd_Inventory inv on inv.iAutoId = t1.iInventoryId
LEFT JOIN Bd_Uom uom ON uom.iAutoId = inv.iUomClassId
left join Bd_EquipmentModel t2 on inv.iEquipmentModelId = t2.iAutoId
WHERE t1.IsDeleted = '0' and t1.cTypeIds ='1'
   	#if(cinvcode)
		and inv.cInvCode = #para(cinvcode)
	#end
#end





