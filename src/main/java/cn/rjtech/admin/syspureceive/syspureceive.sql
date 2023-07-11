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
SELECT  a.*,t.*,area.cAreaName as careaname,a.PosCode as poscode
FROM T_Sys_PUReceiveDetail a
LEFT JOIN
(
SELECT
	m.cOrderNo AS sourcebillno,
	a.cCompleteBarcode AS barcode,
	b.cInvCode ,
	b.cInvCode1,
	b.cInvName1,
	b.cInvName,
	a.dPlanDate AS plandate,
	b.cInvStd AS cinvstd,
	a.iQty AS qty,
	a.iQty AS qtys,
	a.iAutoId AS autoid,
	m.iBusType AS SourceBillType,
	m.cDocNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
	m.cOrderNo AS SourceBillID,
	d.iAutoId AS SourceBillDid,
	m.iVendorId,
	v.cVenCode AS vencode,
	v.cVenName AS venname,
	uom.cUomCode AS purchasecuomcode,
	uom.cUomName AS purchasecuomname,
	config.iWarehouseId,
	area.cAreaCode AS poscode
FROM
	PS_PurchaseOrderDBatch a
	LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
	LEFT JOIN PS_PurchaseOrderD d ON a.iPurchaseOrderDid = d.iAutoId
	LEFT JOIN PS_PurchaseOrderM m ON m.iAutoId = d.iPurchaseOrderMid
	LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
	LEFT JOIN PS_PurchaseOrderD_Qty tc ON tc.iPurchaseOrderDid = d.iAutoId AND tc.iAutoId = a.iPurchaseOrderdQtyId
	LEFT JOIN Bd_Uom uom ON b.iPurchaseUomId = uom.iAutoId
	LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
	LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId


UNION

SELECT
	m.cOrderNo AS sourcebillno,
	a.cCompleteBarcode AS barcode,
	b.cInvCode ,
	b.cInvCode1,
	b.cInvName1,
	b.cInvName,
	a.dPlanDate AS plandate,
	b.cInvStd AS cinvstd,
	a.iQty AS qty,
	a.iQty AS qtys,
	a.iAutoId AS autoid,
	m.iBusType AS SourceBillType,
	m.cDocNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
	m.cOrderNo AS SourceBillID,
	d.iAutoId AS SourceBillDid,
	m.iVendorId,
	v.cVenCode AS vencode,
	v.cVenName AS venname,
	uom.cUomCode AS purchasecuomcode,
	uom.cUomName AS purchasecuomname,
	config.iWarehouseId,
	area.cAreaCode AS poscode
FROM
	PS_SubcontractOrderDBatch a
	LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
	LEFT JOIN PS_SubcontractOrderD d ON a.iSubcontractOrderDid = d.iAutoId
	LEFT JOIN PS_SubcontractOrderM m ON m.iAutoId = d.iSubcontractOrderMid
	LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
	LEFT JOIN PS_SubcontractOrderD_Qty tc ON tc.iSubcontractOrderDid = d.iAutoId AND tc.iAutoId = a.iSubcontractOrderDid
	LEFT JOIN Bd_Uom uom ON b.iPurchaseUomId = uom.iAutoId
	LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
	LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
) as t on a.Barcode = t.barcode
left join Bd_Warehouse_Area area on area.cAreaCode = a.PosCode
where a.isDeleted = '0'
	#if(masid)
		and a.MasID = #para(masid)
	#end
     #if(barcode)
    and a.barcode = #para(barcode)
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
SELECT  a.*,a.cWhCode as whcode,a.cWhName as whname
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
    'PO' as SourceBillType,
    m.cOrderNo AS sourcebillno,
	a.cCompleteBarcode AS barcode,
	b.cInvCode ,
	b.cInvCode1,
	b.cInvName1,
	b.cInvName,
	a.dPlanDate AS plandate,
	b.cInvStd AS cinvstd,
	a.iQty AS qty,
	a.iQty AS qtys,
	a.iAutoId AS autoid,
	m.iBusType AS SourceBillType,
	m.cDocNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
	m.cOrderNo AS SourceBillID,
	d.iAutoId AS SourceBillDid,
	m.iVendorId,
	v.cVenCode AS vencode,
	v.cVenName AS venname,
	uom.cUomCode AS purchasecuomcode,
	uom.cUomName AS purchasecuomname,
	config.iWarehouseId,
	area.cAreaCode AS poscode
FROM
	PS_PurchaseOrderDBatch a
	LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
	LEFT JOIN PS_PurchaseOrderD d ON a.iPurchaseOrderDid = d.iAutoId
	LEFT JOIN PS_PurchaseOrderM m ON m.iAutoId = d.iPurchaseOrderMid
	LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
	LEFT JOIN PS_PurchaseOrderD_Qty tc ON tc.iPurchaseOrderDid = d.iAutoId AND tc.iAutoId = a.iPurchaseOrderdQtyId
	LEFT JOIN Bd_Uom uom ON b.iPurchaseUomId = uom.iAutoId
	LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
	LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
	LEFT JOIN T_Sys_PUReceiveDetail pd ON pd.Barcode = a.cCompleteBarcode AND pd.isDeleted = '0'
	LEFT JOIN T_Sys_AssemBarcode ad ON ad.Barcode = a.cCompleteBarcode AND ad.isDeleted = '0'
	LEFT JOIN T_Sys_OtherInDetail od ON od.Barcode = a.cCompleteBarcode AND od.isDeleted = '0'
where a.isEffective = '1'
    #if(q)
		and (b.cinvcode like concat('%',#para(q),'%') or b.cinvcode1 like concat('%',#para(q),'%')
			or b.cinvname1 like concat('%',#para(q),'%') or a.cCompleteBarcode like concat('%',#para(q),'%')
			or v.cVenCode like concat('%',#para(q),'%')
		)
	#end
        and a.cCompleteBarcode IS NOT NULL
		AND b.cOrgCode = #(orgCode)
        AND pd.AutoID IS NULL
        AND ad.AutoID IS NULL
        AND od.AutoID IS NULL

	#if(vencode)
		and m.iVendorId = #para(vencode)
	#end

union


SELECT
    'OM' as SourceBillType,
	m.cOrderNo AS sourcebillno,
	a.cCompleteBarcode AS barcode,
	b.cInvCode ,
	b.cInvCode1,
	b.cInvName1,
	b.cInvName,
	a.dPlanDate AS plandate,
	b.cInvStd AS cinvstd,
	a.iQty AS qty,
	a.iQty AS qtys,
	a.iAutoId AS autoid,
	m.iBusType AS SourceBillType,
	m.cDocNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
	m.cOrderNo AS SourceBillID,
	d.iAutoId AS SourceBillDid,
	m.iVendorId,
	v.cVenCode AS vencode,
	v.cVenName AS venname,
	uom.cUomCode AS purchasecuomcode,
	uom.cUomName AS purchasecuomname,
	config.iWarehouseId,
	area.cAreaCode AS poscode
FROM
	PS_SubcontractOrderDBatch a
	LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
	LEFT JOIN PS_SubcontractOrderD d ON a.iSubcontractOrderDid = d.iAutoId
	LEFT JOIN PS_SubcontractOrderM m ON m.iAutoId = d.iSubcontractOrderMid
	LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
	LEFT JOIN PS_SubcontractOrderD_Qty tc ON tc.iSubcontractOrderDid = d.iAutoId AND tc.iAutoId = a.iSubcontractOrderDid
	LEFT JOIN Bd_Uom uom ON b.iPurchaseUomId = uom.iAutoId
	LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
	LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
	LEFT JOIN T_Sys_PUReceiveDetail pd ON pd.Barcode = a.cCompleteBarcode AND pd.isDeleted = '0'
	LEFT JOIN T_Sys_AssemBarcode ad ON ad.Barcode = a.cCompleteBarcode AND ad.isDeleted = '0'
	LEFT JOIN T_Sys_OtherInDetail od ON od.Barcode = a.cCompleteBarcode AND od.isDeleted = '0'
where a.isEffective = '1'
    #if(q)
		and (b.cinvcode like concat('%',#para(q),'%') or b.cinvcode1 like concat('%',#para(q),'%')
			or b.cinvname1 like concat('%',#para(q),'%') or a.cCompleteBarcode like concat('%',#para(q),'%')
			or v.cVenCode like concat('%',#para(q),'%')
		)
	#end
        and a.cCompleteBarcode IS NOT NULL
		AND b.cOrgCode = #(orgCode)
        AND pd.AutoID IS NULL
        AND ad.AutoID IS NULL
        AND od.AutoID IS NULL

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
SELECT
	t.*
FROM
	(
	SELECT
		m.cOrderNo AS sourcebillno,
		a.cCompleteBarcode AS barcode,
		b.cInvCode AS invcode,
		b.cinvname,
		b.cInvCode ,
		b.cInvCode1,
		b.cInvName1,
		a.dPlanDate AS plandate,
		b.cInvStd AS cinvstd,
		a.iQty AS qtys,
		a.iQty AS qty,
		a.iinventoryId,
		b.iAutoId,
		m.iBusType AS SourceBillType,
		m.cDocNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
		m.cOrderNo AS SourceBillID,
		d.iAutoId AS SourceBillDid,
		m.iVendorId,
		v.cVenCode AS vencode,
		v.cVenName AS venname,
		uom.cUomCode,
		uom.cUomName AS purchasecuomname,
		uom.cUomName AS puunitname
	FROM
		PS_PurchaseOrderDBatch a
		LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
		LEFT JOIN PS_PurchaseOrderD d ON a.iPurchaseOrderDid = d.iAutoId
		LEFT JOIN PS_PurchaseOrderM m ON m.iAutoId = d.iPurchaseOrderMid
		LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
		LEFT JOIN PS_PurchaseOrderD_Qty tc ON tc.iPurchaseOrderDid = d.iAutoId
		AND tc.iAutoId = a.iPurchaseOrderdQtyId
		LEFT JOIN Bd_Uom uom ON b.iPurchaseUomId = uom.iAutoId
	WHERE
		a.isEffective = '1' UNION
	SELECT
		m.cOrderNo AS sourcebillno,
		a.cCompleteBarcode AS barcode,
		b.cInvCode AS invcode,
		b.cinvname,
		b.cInvCode ,
		b.cInvCode1,
		b.cInvName1,
		a.dPlanDate AS plandate,
		b.cInvStd AS cinvstd,
		a.iQty AS qtys,
		a.iQty AS qty,
		a.iinventoryId,
		b.iAutoId,
		m.iBusType AS SourceBillType,
		m.cDocNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
		m.cOrderNo AS SourceBillID,
		d.iAutoId AS SourceBillDid,
		m.iVendorId,
		v.cVenCode AS vencode,
		v.cVenName AS venname,
		uom.cUomCode,
		uom.cUomName AS purchasecuomname,
		uom.cUomName AS puunitname
	FROM
		PS_SubcontractOrderDBatch a
		LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
		LEFT JOIN PS_SubcontractOrderD d ON a.iSubcontractOrderDid = d.iAutoId
		LEFT JOIN PS_SubcontractOrderM m ON m.iAutoId = d.iSubcontractOrderMid
		LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
		LEFT JOIN PS_SubcontractOrderD_Qty tc ON tc.iSubcontractOrderDid = d.iAutoId
		AND tc.iAutoId = a.iSubcontractOrderdQtyId
		LEFT JOIN Bd_Uom uom ON b.iPurchaseUomId = uom.iAutoId
	WHERE
	a.isEffective = '1'
	) AS t
where 1=1

	#if(barcode)
		and t.barcode = #para(barcode)
	#end

#end


#sql("barcode")
SELECT
	t.*
FROM
	(
	SELECT
		m.cOrderNo AS sourcebillno,
		a.cCompleteBarcode AS barcode,
		b.cInvCode AS invcode,
		b.cinvname,
		b.cInvCode ,
		b.cInvCode1,
		b.cInvName1,
		a.dPlanDate AS plandate,
		b.cInvStd AS cinvstd,
		a.iQty AS qtys,
		a.iQty AS qty,
		a.iAutoId AS autoid,
		a.iinventoryId,
		b.iAutoId,
		m.iBusType AS SourceBillType,
		m.cDocNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
		m.cOrderNo AS SourceBillID,
		d.iAutoId AS SourceBillDid,
		m.iVendorId,
		v.cVenCode AS vencode,
		v.cVenName AS venname,
		uom.cUomCode,
		uom.cUomName AS purchasecuomname,
		uom.cUomName AS puunitname,
		config.iWarehouseId,
		area.cAreaCode AS poscode
	FROM
		PS_PurchaseOrderDBatch a
		LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
		LEFT JOIN PS_PurchaseOrderD d ON a.iPurchaseOrderDid = d.iAutoId
		LEFT JOIN PS_PurchaseOrderM m ON m.iAutoId = d.iPurchaseOrderMid
		LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
		LEFT JOIN PS_PurchaseOrderD_Qty tc ON tc.iPurchaseOrderDid = d.iAutoId
		AND tc.iAutoId = a.iPurchaseOrderdQtyId
		LEFT JOIN Bd_Uom uom ON b.iPurchaseUomId = uom.iAutoId
		LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
		LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
		LEFT JOIN T_Sys_PUReceiveDetail pd ON pd.Barcode = a.cCompleteBarcode AND pd.isDeleted = '0'
        LEFT JOIN T_Sys_AssemBarcode ad ON ad.Barcode = a.cCompleteBarcode AND ad.isDeleted = '0'
        LEFT JOIN T_Sys_OtherInDetail od ON od.Barcode = a.cCompleteBarcode AND od.isDeleted = '0'
	WHERE
		a.isEffective = '1'
		AND pd.AutoID IS NULL
        AND ad.AutoID IS NULL
        AND od.AutoID IS NULL

		UNION
	SELECT
		m.cOrderNo AS sourcebillno,
		a.cCompleteBarcode AS barcode,
		b.cInvCode AS invcode,
		b.cinvname,
		b.cInvCode ,
		b.cInvCode1,
		b.cInvName1,
		a.dPlanDate AS plandate,
		b.cInvStd AS cinvstd,
		a.iQty AS qtys,
		a.iQty AS qty,
		a.iAutoId AS autoid,
		a.iinventoryId,
		b.iAutoId,
		m.iBusType AS SourceBillType,
		m.cDocNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
		m.cOrderNo AS SourceBillID,
		d.iAutoId AS SourceBillDid,
		m.iVendorId,
		v.cVenCode AS vencode,
		v.cVenName AS venname,
		uom.cUomCode,
		uom.cUomName AS purchasecuomname,
		uom.cUomName AS puunitname,
		config.iWarehouseId,
		area.cAreaCode AS poscode
	FROM
		PS_SubcontractOrderDBatch a
		LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
		LEFT JOIN PS_SubcontractOrderD d ON a.iSubcontractOrderDid = d.iAutoId
		LEFT JOIN PS_SubcontractOrderM m ON m.iAutoId = d.iSubcontractOrderMid
		LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
		LEFT JOIN PS_SubcontractOrderD_Qty tc ON tc.iSubcontractOrderDid = d.iAutoId
		LEFT JOIN Bd_Uom uom ON b.iPurchaseUomId = uom.iAutoId
		LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
		LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
		LEFT JOIN T_Sys_PUReceiveDetail pd ON pd.Barcode = a.cCompleteBarcode AND pd.isDeleted = '0'
        LEFT JOIN T_Sys_AssemBarcode ad ON ad.Barcode = a.cCompleteBarcode AND ad.isDeleted = '0'
        LEFT JOIN T_Sys_OtherInDetail od ON od.Barcode = a.cCompleteBarcode AND od.isDeleted = '0'
	WHERE
	a.isEffective = '1'
	    AND pd.AutoID IS NULL
        AND ad.AutoID IS NULL
        AND od.AutoID IS NULL
	) AS t
where 1 = 1

	#if(barcode)
		and t.barcode = #para(barcode)
	#end


#end

#sql("paginateAdminDatas")
SELECT wa.*,wh.cWhCode,wh.cWhName
FROM
	Bd_Warehouse_Area wa
LEFT JOIN Bd_Warehouse wh ON wa.iWarehouseId = wh.iAutoId
WHERE wa.isDeleted = 0
    AND wa.cAreaCode = #para(careacode)

#end

#sql("getWhCode")
SELECT wa.*
FROM Bd_Warehouse wa
WHERE wa.isDeleted = 0  AND wa.cWhCode = #para(careacode)
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
SELECT
	t.*
FROM
	(
	SELECT
		m.iPurchaseTypeId,
		m.cOrderNo AS sourcebillno,
		a.cCompleteBarcode AS barcode,
		b.cInvCode ,
		b.cInvCode1,
		b.cInvName1,
		a.dPlanDate AS plandate,
		b.cInvStd AS cinvstd,
		a.iQty AS qtys,
		a.iQty AS qty,
		m.iBusType,
		m.iBusType AS SourceBillType,
		m.cDocNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
		m.iAutoId AS SourceBillID,
		d.iAutoId AS SourceBillDid,
		m.iVendorId,
		v.cVenCode AS vencode,
		v.cVenName AS venname,
		u.cUomCode AS puunitcode,
		u.cUomName AS puunitname,
		s.iAutoId AS siautoid,
		s.cRdName AS scrdname,
		s.cRdCode AS scrdcode,
		dep.cdepcode,
		dep.cdepname,
		a.iinventoryId
	FROM
		PS_PurchaseOrderDBatch a
		LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
		LEFT JOIN PS_PurchaseOrderD d ON a.iPurchaseOrderDid = d.iAutoId
		LEFT JOIN PS_PurchaseOrderM m ON m.iAutoId = d.iPurchaseOrderMid
		LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
		LEFT JOIN PS_PurchaseOrderD_Qty tc ON tc.iPurchaseOrderDid = d.iAutoId
		AND tc.iAutoId = a.iPurchaseOrderdQtyId
		LEFT JOIN Bd_Uom u ON b.iPurchaseUomId = u.iAutoId
		LEFT JOIN Bd_PurchaseType p ON p.iAutoId = m.iPurchaseTypeId
		LEFT JOIN Bd_Rd_Style s ON s.cRdCode = p.cRdCode
		LEFT JOIN Bd_Department dep ON m.idepartmentid = dep.iautoid UNION
	SELECT
		m.iPurchaseTypeId,
		m.cOrderNo AS sourcebillno,
		a.cCompleteBarcode AS barcode,
		b.cInvCode ,
		b.cInvCode1,
		b.cInvName1,
		a.dPlanDate AS plandate,
		b.cInvStd AS cinvstd,
		a.iQty AS qtys,
		a.iQty AS qty,
		m.iBusType,
		m.iBusType AS SourceBillType,
		m.cDocNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
		m.iAutoId AS SourceBillID,
		d.iAutoId AS SourceBillDid,
		m.iVendorId,
		v.cVenCode AS vencode,
		v.cVenName AS venname,
		u.cUomCode AS puunitcode,
		u.cUomName AS puunitname,
		s.iAutoId AS siautoid,
		s.cRdName AS scrdname,
		s.cRdCode AS scrdcode,
		dep.cdepcode,
		dep.cdepname,
		a.iinventoryId
	FROM
		PS_SubcontractOrderDBatch a
		LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
		LEFT JOIN PS_SubcontractOrderD d ON a.iSubcontractOrderDid = d.iAutoId
		LEFT JOIN PS_SubcontractOrderM m ON m.iAutoId = d.iSubcontractOrderMid
		LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
		LEFT JOIN PS_SubcontractOrderD_Qty tc ON tc.iSubcontractOrderDid = d.iAutoId
		AND tc.iAutoId = a.iSubcontractOrderDid
		LEFT JOIN Bd_Uom u ON b.iPurchaseUomId = u.iAutoId
		LEFT JOIN Bd_PurchaseType p ON p.iAutoId = m.iPurchaseTypeId
		LEFT JOIN Bd_Rd_Style s ON s.cRdCode = p.cRdCode
	LEFT JOIN Bd_Department dep ON m.idepartmentid = dep.iautoid
	) AS t
where 1=1
   	#if(barcode)
		and t.barcode = #para(barcode)
	#end
#end

#sql("InventoryQcForm")
SELECT  qc.iAutoId,qc.cQcFormName,
inv.cInvCode, inv.cInvCode1, inv.cInvName, inv.cInvName1, inv.cInvStd, uom.cUomName,t1.cDcCode,
t2.cEquipmentModelName as machineName
FROM Bd_InventoryQcForm t1
INNER JOIN Bd_QcForm qc ON qc.iAutoId = t1.iQcFormId
INNER JOIN Bd_Inventory inv on inv.iAutoId = t1.iInventoryId
LEFT JOIN Bd_Uom uom ON uom.iAutoId = inv.iUomClassId
left join Bd_EquipmentModel t2 on inv.iEquipmentModelId = t2.iAutoId
WHERE t1.IsDeleted = '0'
    AND t1.cTypeIds like '%1%'
   	#if(cinvcode)
		and inv.cInvCode = #para(cinvcode)
	#end
#end





