
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
        END AS statename,so.iAuditStatus,so.BillNo,so.dcreatetime,so.Whcode,ck.cWhName as whname,so.RdCode,rd.cRdName as rdcodename,
			so.BillType,so.VenCode,v.cVenName as venname,so.dupdatetime,so.memo,so.cupdatename,so.ccreatename,so.cAuditname,
			so.dAuditTime
FROM T_Sys_OtherIn so
LEFT JOIN Bd_Warehouse ck on so.Whcode = ck.cWhCode
LEFT JOIN Bd_Rd_Style rd on so.RdCode = rd.cRdCode
LEFT JOIN Bd_Vendor v on so.VenCode = v.cVenCode
where 1=1
#if(billno)
    and so.BillNo like concat('%',#para(billno),'%')
#end
#if(whname)
    and ck.cWhName like concat('%',#para(whname),'%')
#end
#if(state)
    and so.state = #para(state)
#end
#if(startTime)
    and so.dcreatetime >= #para(startTime)
#end
#if(endTime)
    and so.dcreatetime <= #para(endTime)
#end
    order by so.dupdatetime desc
#end

#sql("dList")
select
	detail.*,
    t1.*
FROM T_Sys_OtherInDetail detail
LEFT JOIN	(
	SELECT
		m.cOrderNo AS sourcebillno,
		a.cCompleteBarcode AS barcode,
		b.cInvCode ,
		b.cInvAddCode,
		b.cInvCode1,
		b.cInvName1,
		b.cInvName,
		a.dPlanDate AS plandate,
		b.cInvStd AS cinvstd,
		a.iQty AS qty,
		a.iQty AS qtys,
		CAST ( a.iAutoId AS nvarchar ) AS autoid,
		m.iBusType AS SourceBillType,
		m.cDocNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
		m.cOrderNo AS SourceBillID,
		d.iAutoId AS SourceBillDid,
		m.iVendorId,
		v.cVenCode AS vencode,
		v.cVenName AS venname,
		uom.cUomCode,
		uom.cUomName,
		config.iWarehouseId,
		area.cAreaCode AS poscode
	FROM
		PS_PurchaseOrderDBatch a
		LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
		LEFT JOIN PS_PurchaseOrderD d ON a.iPurchaseOrderDid = d.iAutoId
		LEFT JOIN PS_PurchaseOrderM m ON m.iAutoId = d.iPurchaseOrderMid
		LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
		LEFT JOIN T_Sys_PUReceiveDetail pd ON pd.Barcode = a.cCompleteBarcode
		AND pd.isDeleted = '0'
		LEFT JOIN PS_PurchaseOrderD_Qty tc ON tc.iPurchaseOrderDid = d.iAutoId
		AND tc.iAutoId = a.iPurchaseOrderdQtyId
		LEFT JOIN Bd_Uom uom ON b.iPurchaseUomId = uom.iAutoId
		LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
		LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
		LEFT JOIN T_Sys_OtherInDetail oid ON oid.Barcode = a.cCompleteBarcode
		AND pd.isDeleted = '0'
	WHERE
		a.isEffective = '1'

		UNION
	SELECT
		m.cOrderNo AS sourcebillno,
		a.cCompleteBarcode AS barcode,
		b.cInvCode ,
		b.cInvAddCode,
		b.cInvCode1,
		b.cInvName1,
		b.cInvName,
		a.dPlanDate AS plandate,
		b.cInvStd AS cinvstd,
		a.iQty AS qty,
		a.iQty AS qtys,
		CAST ( a.iAutoId AS nvarchar ) AS autoid,
		m.iBusType AS SourceBillType,
		m.cDocNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
		m.cOrderNo AS SourceBillID,
		d.iAutoId AS SourceBillDid,
		m.iVendorId,
		v.cVenCode AS vencode,
		v.cVenName AS venname,
		uom.cUomCode,
		uom.cUomName,
		config.iWarehouseId,
		area.cAreaCode AS poscode
	FROM
		PS_SubcontractOrderDBatch a
		LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
		LEFT JOIN PS_SubcontractOrderD d ON a.iSubcontractOrderDid = d.iAutoId
		LEFT JOIN PS_SubcontractOrderM m ON m.iAutoId = d.iSubcontractOrderMid
		LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
		LEFT JOIN T_Sys_PUReceiveDetail pd ON pd.Barcode = a.cCompleteBarcode
		AND pd.isDeleted = '0'
		LEFT JOIN PS_SubcontractOrderD_Qty tc ON tc.iSubcontractOrderDid = d.iAutoId
		AND tc.iAutoId = a.iSubcontractOrderdQtyId
		LEFT JOIN Bd_Uom uom ON b.iPurchaseUomId = uom.iAutoId
		LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
		LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
		LEFT JOIN T_Sys_OtherInDetail oid ON oid.Barcode = a.cCompleteBarcode
		AND pd.isDeleted = '0'
	WHERE
		a.isEffective = '1'

		UNION
	SELECT
		ua.SourceID AS sourcebillno,
		a.barcode,
		ua.Invcode AS cInvCode,
		'' AS cInvAddCode,
		'' AS cInvCode1,
		'' AS cInvName1,
		'' AS cInvName,
		ua.plandate AS plandate,
		'' AS cinvstd,
		ua.qty AS qty,
		ua.qty AS qtys,
		a.AutoId AS autoid,
		'' AS SourceBillType,
		'' AS SourceBillNoRow,
		'' AS SourceBillID,
		'' AS SourceBillDid,
		ua.VenInvCode AS iVendorId,
		ua.VenCode AS vencode,
		'' AS venname,
		'' AS cUomCode,
		'' AS cUomName,
		'' AS iWarehouseId,
		'' AS poscode
	FROM
		T_Sys_BarcodeDetail a
		LEFT JOIN UFDATA_001_2023.dbo.V_sys_BarcodeDetail ua ON ua.Barcode = a.Barcode
		AND ua.Barcode IS NOT NULL
		where 1=1


		UNION
	SELECT
		ua.SourceID AS sourcebillno,
		a.cCompleteBarcode AS barcode,
		b.cInvCode ,
		b.cInvAddCode,
		b.cInvCode1,
		b.cInvName1,
		b.cInvName,
		ua.plandate AS plandate,
		'' AS cinvstd,
		a.iQty AS qty,
		a.iQty AS qtys,
		CAST ( a.iAutoId AS nvarchar ) AS autoid,
		'' AS SourceBillType,
		'' AS SourceBillNoRow,
		'' AS SourceBillID,
		'' AS SourceBillDid,
		ua.VenInvCode AS iVendorId,
		ua.VenCode AS vencode,
		'' AS venname,
		'' AS cUomCode,
		'' AS cUomName,
		'' AS iWarehouseId,
		'' AS poscode
	FROM
		Mo_MoInvBatch a
		LEFT JOIN UFDATA_001_2023.dbo.V_sys_BarcodeDetail ua ON ua.Barcode = a.cCompleteBarcode
		AND ua.Barcode
		IS NOT NULL LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
	WHERE
		a.isEffective = '1'
	AND a.isDeleted = '0'

	) AS t1 on detail.barcode = t1.barcode
where 1=1
#if(masid)
    and detail.MasID = #para(masid)
#end
    order by detail.dupdatetime desc
#end



#sql("billtype")
SELECT  *
from Bd_VouchTypeDic
where isDeleted = 0
	#if(q)
		and (cBTID like concat('%',#para(q),'%') OR cBTChName like concat('%',#para(q),'%'))
	#end
	order by iAutoId desc
#end


#sql("getBarcodeDatas")
SELECT top #(limit)
	t1.*
FROM
	(
	SELECT
		m.cOrderNo AS sourcebillno,
		a.cCompleteBarcode AS barcode,
		b.cInvCode ,
		b.cInvAddCode,
		b.cInvCode1,
		b.cInvName1,
		b.cInvName,
		a.dPlanDate AS plandate,
		b.cInvStd AS cinvstd,
		a.iQty AS qty,
		a.iQty AS qtys,
		CAST ( a.iAutoId AS nvarchar ) AS autoid,
		m.iBusType AS SourceBillType,
		m.cDocNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
		m.cOrderNo AS SourceBillID,
		d.iAutoId AS SourceBillDid,
		m.iVendorId,
		v.cVenCode AS vencode,
		v.cVenName AS venname,
		uom.cUomCode,
		uom.cUomName,
		config.iWarehouseId,
		area.cAreaCode AS poscode
	FROM
		PS_PurchaseOrderDBatch a
		LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
		LEFT JOIN PS_PurchaseOrderD d ON a.iPurchaseOrderDid = d.iAutoId
		LEFT JOIN PS_PurchaseOrderM m ON m.iAutoId = d.iPurchaseOrderMid
		LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
		LEFT JOIN T_Sys_PUReceiveDetail pd ON pd.Barcode = a.cCompleteBarcode
		AND pd.isDeleted = '0'
		LEFT JOIN PS_PurchaseOrderD_Qty tc ON tc.iPurchaseOrderDid = d.iAutoId
		AND tc.iAutoId = a.iPurchaseOrderdQtyId
		LEFT JOIN Bd_Uom uom ON b.iPurchaseUomId = uom.iAutoId
		LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
		LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
		LEFT JOIN T_Sys_OtherInDetail oid ON oid.Barcode = a.cCompleteBarcode
		AND pd.isDeleted = '0'
	WHERE
		a.isEffective = '1'
    #if(q)
		and (b.cinvcode like concat('%',#para(q),'%') or b.cinvcode1 like concat('%',#para(q),'%')
			or b.cinvname1 like concat('%',#para(q),'%') or a.cCompleteBarcode like concat('%',#para(q),'%')
			or v.cVenCode like concat('%',#para(q),'%')
		)
	#end
		UNION
	SELECT
		m.cOrderNo AS sourcebillno,
		a.cCompleteBarcode AS barcode,
		b.cInvCode ,
		b.cInvAddCode,
		b.cInvCode1,
		b.cInvName1,
		b.cInvName,
		a.dPlanDate AS plandate,
		b.cInvStd AS cinvstd,
		a.iQty AS qty,
		a.iQty AS qtys,
		CAST ( a.iAutoId AS nvarchar ) AS autoid,
		m.iBusType AS SourceBillType,
		m.cDocNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
		m.cOrderNo AS SourceBillID,
		d.iAutoId AS SourceBillDid,
		m.iVendorId,
		v.cVenCode AS vencode,
		v.cVenName AS venname,
		uom.cUomCode,
		uom.cUomName,
		config.iWarehouseId,
		area.cAreaCode AS poscode
	FROM
		PS_SubcontractOrderDBatch a
		LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
		LEFT JOIN PS_SubcontractOrderD d ON a.iSubcontractOrderDid = d.iAutoId
		LEFT JOIN PS_SubcontractOrderM m ON m.iAutoId = d.iSubcontractOrderMid
		LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
		LEFT JOIN T_Sys_PUReceiveDetail pd ON pd.Barcode = a.cCompleteBarcode
		AND pd.isDeleted = '0'
		LEFT JOIN PS_SubcontractOrderD_Qty tc ON tc.iSubcontractOrderDid = d.iAutoId
		AND tc.iAutoId = a.iSubcontractOrderdQtyId
		LEFT JOIN Bd_Uom uom ON b.iPurchaseUomId = uom.iAutoId
		LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
		LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
		LEFT JOIN T_Sys_OtherInDetail oid ON oid.Barcode = a.cCompleteBarcode
		AND pd.isDeleted = '0'
	WHERE
		a.isEffective = '1'
    #if(q)
		and (b.cinvcode like concat('%',#para(q),'%') or b.cinvcode1 like concat('%',#para(q),'%')
			or b.cinvname1 like concat('%',#para(q),'%') or a.cCompleteBarcode like concat('%',#para(q),'%')
			or v.cVenCode like concat('%',#para(q),'%')
		)
	#end
		UNION
	SELECT
		ua.SourceID AS sourcebillno,
		a.barcode,
		ua.Invcode AS cInvCode,
		'' AS cInvAddCode,
		'' AS cInvCode1,
		'' AS cInvName1,
		'' AS cInvName,
		ua.plandate AS plandate,
		'' AS cinvstd,
		ua.qty AS qty,
		ua.qty AS qtys,
		a.AutoId AS autoid,
		'' AS SourceBillType,
		'' AS SourceBillNoRow,
		'' AS SourceBillID,
		'' AS SourceBillDid,
		ua.VenInvCode AS iVendorId,
		ua.VenCode AS vencode,
		'' AS venname,
		'' AS cUomCode,
		'' AS cUomName,
		'' AS iWarehouseId,
		'' AS poscode
	FROM
		T_Sys_BarcodeDetail a
		LEFT JOIN UFDATA_001_2023.dbo.V_sys_BarcodeDetail ua ON ua.Barcode = a.Barcode
		AND ua.Barcode IS NOT NULL
		where 1=1
    #if(q)
		and ( a.barcode like concat('%',#para(q),'%'))
	#end

		UNION
	SELECT
		ua.SourceID AS sourcebillno,
		a.cCompleteBarcode AS barcode,
		b.cInvCode ,
		b.cInvAddCode,
		b.cInvCode1,
		b.cInvName1,
		b.cInvName,
		ua.plandate AS plandate,
		'' AS cinvstd,
		a.iQty AS qty,
		a.iQty AS qtys,
		CAST ( a.iAutoId AS nvarchar ) AS autoid,
		'' AS SourceBillType,
		'' AS SourceBillNoRow,
		'' AS SourceBillID,
		'' AS SourceBillDid,
		ua.VenInvCode AS iVendorId,
		ua.VenCode AS vencode,
		'' AS venname,
		'' AS cUomCode,
		'' AS cUomName,
		'' AS iWarehouseId,
		'' AS poscode
	FROM
		Mo_MoInvBatch a
		LEFT JOIN UFDATA_001_2023.dbo.V_sys_BarcodeDetail ua ON ua.Barcode = a.cCompleteBarcode
		AND ua.Barcode
		IS NOT NULL LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
	WHERE
		a.isEffective = '1'
	AND a.isDeleted = '0'
    #if(q)
		and ( a.cCompleteBarcode like concat('%',#para(q),'%'))
	#end
	) AS t1
LEFT JOIN T_Sys_OtherInDetail oid on oid.Barcode = t1.Barcode AND oid.isDeleted = '0'
LEFT JOIN T_Sys_PUReceiveDetail pd on pd.Barcode = t1.Barcode AND pd.isDeleted = '0'
LEFT JOIN T_Sys_ProductInDetail pde on pde.Barcode = t1.Barcode AND pde.isDeleted = '0'
where   pd.AutoID IS NULL
        AND oid.AutoID IS NULL
        AND pde.AutoID IS NULL
#end

#sql("barcodeDatas")
SELECT *
FROM T_Sys_OtherInDetail
WHERE isDeleted = 0
   	#if(barcode)
		and Barcode = #para(barcode)
	#end
#end


#sql("barcode")
SELECT
	t1.*
FROM
	(
	SELECT
		m.cOrderNo AS sourcebillno,
		a.cCompleteBarcode AS barcode,
		b.cInvCode ,
		b.cInvAddCode,
		b.cInvCode1,
		b.cInvName1,
		b.cInvName,
		a.dPlanDate AS plandate,
		b.cInvStd AS cinvstd,
		a.iQty AS qty,
		a.iQty AS qtys,
		CAST ( a.iAutoId AS nvarchar ) AS autoid,
		m.iBusType AS SourceBillType,
		m.cDocNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
		m.cOrderNo AS SourceBillID,
		d.iAutoId AS SourceBillDid,
		m.iVendorId,
		v.cVenCode AS vencode,
		v.cVenName AS venname,
		uom.cUomCode,
		uom.cUomName,
		config.iWarehouseId,
		area.cAreaCode AS poscode
	FROM
		PS_PurchaseOrderDBatch a
		LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
		LEFT JOIN PS_PurchaseOrderD d ON a.iPurchaseOrderDid = d.iAutoId
		LEFT JOIN PS_PurchaseOrderM m ON m.iAutoId = d.iPurchaseOrderMid
		LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
		LEFT JOIN T_Sys_PUReceiveDetail pd ON pd.Barcode = a.cCompleteBarcode
		AND pd.isDeleted = '0'
		LEFT JOIN PS_PurchaseOrderD_Qty tc ON tc.iPurchaseOrderDid = d.iAutoId
		AND tc.iAutoId = a.iPurchaseOrderdQtyId
		LEFT JOIN Bd_Uom uom ON b.iPurchaseUomId = uom.iAutoId
		LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
		LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
		LEFT JOIN T_Sys_OtherInDetail oid ON oid.Barcode = a.cCompleteBarcode
		AND pd.isDeleted = '0'
	WHERE
		a.isEffective = '1'
    #if(q)
		and (b.cinvcode like concat('%',#para(q),'%') or b.cinvcode1 like concat('%',#para(q),'%')
			or b.cinvname1 like concat('%',#para(q),'%') or a.cCompleteBarcode like concat('%',#para(q),'%')
			or v.cVenCode like concat('%',#para(q),'%')
		)
	#end
		UNION
	SELECT
		m.cOrderNo AS sourcebillno,
		a.cCompleteBarcode AS barcode,
		b.cInvCode ,
		b.cInvAddCode,
		b.cInvCode1,
		b.cInvName1,
		b.cInvName,
		a.dPlanDate AS plandate,
		b.cInvStd AS cinvstd,
		a.iQty AS qty,
		a.iQty AS qtys,
		CAST ( a.iAutoId AS nvarchar ) AS autoid,
		m.iBusType AS SourceBillType,
		m.cDocNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
		m.cOrderNo AS SourceBillID,
		d.iAutoId AS SourceBillDid,
		m.iVendorId,
		v.cVenCode AS vencode,
		v.cVenName AS venname,
		uom.cUomCode,
		uom.cUomName,
		config.iWarehouseId,
		area.cAreaCode AS poscode
	FROM
		PS_SubcontractOrderDBatch a
		LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
		LEFT JOIN PS_SubcontractOrderD d ON a.iSubcontractOrderDid = d.iAutoId
		LEFT JOIN PS_SubcontractOrderM m ON m.iAutoId = d.iSubcontractOrderMid
		LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
		LEFT JOIN T_Sys_PUReceiveDetail pd ON pd.Barcode = a.cCompleteBarcode
		AND pd.isDeleted = '0'
		LEFT JOIN PS_SubcontractOrderD_Qty tc ON tc.iSubcontractOrderDid = d.iAutoId
		AND tc.iAutoId = a.iSubcontractOrderdQtyId
		LEFT JOIN Bd_Uom uom ON b.iPurchaseUomId = uom.iAutoId
		LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
		LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
		LEFT JOIN T_Sys_OtherInDetail oid ON oid.Barcode = a.cCompleteBarcode
		AND pd.isDeleted = '0'
	WHERE
		a.isEffective = '1'
    #if(q)
		and (b.cinvcode like concat('%',#para(q),'%') or b.cinvcode1 like concat('%',#para(q),'%')
			or b.cinvname1 like concat('%',#para(q),'%') or a.cCompleteBarcode like concat('%',#para(q),'%')
			or v.cVenCode like concat('%',#para(q),'%')
		)
	#end
		UNION
	SELECT
		ua.SourceID AS sourcebillno,
		a.barcode,
		ua.Invcode AS cInvCode,
		'' AS cInvAddCode,
		'' AS cInvCode1,
		'' AS cInvName1,
		'' AS cInvName,
		ua.plandate AS plandate,
		'' AS cinvstd,
		ua.qty AS qty,
		ua.qty AS qtys,
		a.AutoId AS autoid,
		'' AS SourceBillType,
		'' AS SourceBillNoRow,
		'' AS SourceBillID,
		'' AS SourceBillDid,
		ua.VenInvCode AS iVendorId,
		ua.VenCode AS vencode,
		'' AS venname,
		'' AS cUomCode,
		'' AS cUomName,
		'' AS iWarehouseId,
		'' AS poscode
	FROM
		T_Sys_BarcodeDetail a
		LEFT JOIN UFDATA_001_2023.dbo.V_sys_BarcodeDetail ua ON ua.Barcode = a.Barcode
		AND ua.Barcode IS NOT NULL
		where 1=1
    #if(q)
		and ( a.barcode like concat('%',#para(q),'%'))
	#end

		UNION
	SELECT
		ua.SourceID AS sourcebillno,
		a.cCompleteBarcode AS barcode,
		b.cInvCode ,
		b.cInvAddCode,
		b.cInvCode1,
		b.cInvName1,
		b.cInvName,
		ua.plandate AS plandate,
		'' AS cinvstd,
		a.iQty AS qty,
		a.iQty AS qtys,
		CAST ( a.iAutoId AS nvarchar ) AS autoid,
		'' AS SourceBillType,
		'' AS SourceBillNoRow,
		'' AS SourceBillID,
		'' AS SourceBillDid,
		ua.VenInvCode AS iVendorId,
		ua.VenCode AS vencode,
		'' AS venname,
		'' AS cUomCode,
		'' AS cUomName,
		'' AS iWarehouseId,
		'' AS poscode
	FROM
		Mo_MoInvBatch a
		LEFT JOIN UFDATA_001_2023.dbo.V_sys_BarcodeDetail ua ON ua.Barcode = a.cCompleteBarcode
		AND ua.Barcode
		IS NOT NULL LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
	WHERE
		a.isEffective = '1'
	AND a.isDeleted = '0'
    #if(q)
		and ( a.cCompleteBarcode like concat('%',#para(q),'%'))
	#end
	) AS t1
LEFT JOIN T_Sys_OtherInDetail oid on oid.Barcode = t1.Barcode AND oid.isDeleted = '0'
LEFT JOIN T_Sys_PUReceiveDetail pd on pd.Barcode = t1.Barcode AND pd.isDeleted = '0'
LEFT JOIN T_Sys_ProductInDetail pde on pde.Barcode = t1.Barcode AND pde.isDeleted = '0'
where   pd.AutoID IS NULL
        AND oid.AutoID IS NULL
        AND pde.AutoID IS NULL
	#if(barcode)
		and t1.Barcode = #para(barcode)
	#end

#end


#sql("findU8RdRecord01Id")
select r.* from UFDATA_001_2023.dbo.RdRecord08 r where r.cCode = #para(cCode)
#end