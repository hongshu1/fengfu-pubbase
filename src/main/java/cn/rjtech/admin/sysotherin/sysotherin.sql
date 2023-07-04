
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
    m.cOrderNo as sourcebillno,
    a.cCompleteBarcode as barcode,
    b.cInvCode ,
    b.cInvAddCode,
    b.cInvCode1,
    b.cInvName1,
	b.cInvName,
    a.dPlanDate as plandate,
    b.cInvStd as cinvstd,
    a.iQty as qty,
    a.iQty as qtys,
    a.iAutoId as autoid,
    m.cOrderNo as SourceBillNo,
    m.iBusType as SourceBillType,
    m.cDocNo+'-'+CAST(tc.iseq AS NVARCHAR(10)) as SourceBillNoRow,
    m.cOrderNo as SourceBillID,
    d.iAutoId as SourceBillDid,
    m.iVendorId,
	v.cVenCode as vencode,
	v.cVenName as venname,
	 uom.cUomCode,uom.cUomName,
	config.iWarehouseId,
	area.cAreaCode as poscode
FROM T_Sys_OtherInDetail detail
LEFT JOIN	PS_PurchaseOrderDBatch a on detail.Barcode = a.cCompleteBarcode
LEFT JOIN Bd_Inventory b on a.iinventoryId = b.iAutoId
LEFT JOIN PS_PurchaseOrderD d on a.iPurchaseOrderDid = d.iAutoId
LEFT JOIN PS_PurchaseOrderM m on m.iAutoId = d.iPurchaseOrderMid
LEFT JOIN Bd_Vendor v on m.iVendorId = v.iAutoId
LEFT JOIN PS_PurchaseOrderD_Qty tc on tc.iPurchaseOrderDid = d.iAutoId AND tc.iAutoId = a.iPurchaseOrderdQtyId
LEFT JOIN Bd_Uom uom on b.iPurchaseUomId = uom.iAutoId
LEFT JOIN Bd_InventoryStockConfig config on config.iInventoryId = b.iAutoId
LEFT JOIN Bd_Warehouse_Area area on area.iAutoId =config.iWarehouseAreaId
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
select top #(limit)
    m.cOrderNo as sourcebillno,
    a.cCompleteBarcode as barcode,
    b.cInvCode ,
    b.cInvAddCode,
    b.cInvCode1,
    b.cInvName1,
	b.cInvName,
    a.dPlanDate as plandate,
    b.cInvStd as cinvstd,
    a.iQty as qty,
    a.iQty as qtys,
    a.iAutoId as autoid,
    m.cOrderNo as SourceBillNo,
    m.iBusType as SourceBillType,
    m.cDocNo+'-'+CAST(tc.iseq AS NVARCHAR(10)) as SourceBillNoRow,
    m.cOrderNo as SourceBillID,
    d.iAutoId as SourceBillDid,
    m.iVendorId,
	v.cVenCode as vencode,
	v.cVenName as venname,
	 uom.cUomCode,uom.cUomName,
	config.iWarehouseId,
	area.cAreaCode as poscode
FROM PS_PurchaseOrderDBatch a
LEFT JOIN Bd_Inventory b on a.iinventoryId = b.iAutoId
LEFT JOIN PS_PurchaseOrderD d on a.iPurchaseOrderDid = d.iAutoId
LEFT JOIN PS_PurchaseOrderM m on m.iAutoId = d.iPurchaseOrderMid
LEFT JOIN Bd_Vendor v on m.iVendorId = v.iAutoId
LEFT JOIN T_Sys_PUReceiveDetail pd on pd.Barcode = a.cCompleteBarcode AND pd.isDeleted = '0'
LEFT JOIN PS_PurchaseOrderD_Qty tc on tc.iPurchaseOrderDid = d.iAutoId AND tc.iAutoId = a.iPurchaseOrderdQtyId
LEFT JOIN Bd_Uom uom on b.iPurchaseUomId = uom.iAutoId
LEFT JOIN Bd_InventoryStockConfig config on config.iInventoryId = b.iAutoId
LEFT JOIN Bd_Warehouse_Area area on area.iAutoId =config.iWarehouseAreaId
LEFT JOIN T_Sys_OtherInDetail oid on oid.Barcode = a.cCompleteBarcode AND pd.isDeleted = '0'
where a.isEffective = '1'
    #if(q)
		and (b.cinvcode like concat('%',#para(q),'%') or b.cinvcode1 like concat('%',#para(q),'%')
			or b.cinvname1 like concat('%',#para(q),'%') or a.cCompleteBarcode like concat('%',#para(q),'%')
			or v.cVenCode like concat('%',#para(q),'%')
		)
	#end

		AND b.cOrgCode = #(orgCode)
        AND pd.AutoID IS NULL
        AND oid.AutoID IS NULL


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
select
    m.cOrderNo as sourcebillno,
    a.cCompleteBarcode as barcode,
    b.cInvCode ,
    b.cInvAddCode,
    b.cInvCode1,
    b.cInvName1,
	b.cInvName,
    a.dPlanDate as plandate,
    b.cInvStd as cinvstd,
    a.iQty as qty,
    a.iQty as qtys,
    a.iAutoId as autoid,
    m.cOrderNo as SourceBillNo,
    m.iBusType as SourceBillType,
    m.cDocNo+'-'+CAST(tc.iseq AS NVARCHAR(10)) as SourceBillNoRow,
    m.cOrderNo as SourceBillID,
    d.iAutoId as SourceBillDid,
    m.iVendorId,
	v.cVenCode as vencode,
	v.cVenName as venname,
	 uom.cUomCode,uom.cUomName,
	config.iWarehouseId,
	area.cAreaCode as poscode
FROM PS_PurchaseOrderDBatch a
LEFT JOIN Bd_Inventory b on a.iinventoryId = b.iAutoId
LEFT JOIN PS_PurchaseOrderD d on a.iPurchaseOrderDid = d.iAutoId
LEFT JOIN PS_PurchaseOrderM m on m.iAutoId = d.iPurchaseOrderMid
LEFT JOIN Bd_Vendor v on m.iVendorId = v.iAutoId
LEFT JOIN T_Sys_PUReceiveDetail pd on pd.Barcode = a.cCompleteBarcode AND pd.isDeleted = '0'
LEFT JOIN PS_PurchaseOrderD_Qty tc on tc.iPurchaseOrderDid = d.iAutoId AND tc.iAutoId = a.iPurchaseOrderdQtyId
LEFT JOIN Bd_Uom uom on b.iPurchaseUomId = uom.iAutoId
LEFT JOIN Bd_InventoryStockConfig config on config.iInventoryId = b.iAutoId
LEFT JOIN Bd_Warehouse_Area area on area.iAutoId =config.iWarehouseAreaId
LEFT JOIN T_Sys_OtherInDetail oid on oid.Barcode = a.cCompleteBarcode AND pd.isDeleted = '0'
where a.isEffective = '1'
	#if(barcode)
		and a.cCompleteBarcode = #para(barcode)
	#end

        AND pd.AutoID IS NULL
        AND oid.AutoID IS NULL


#end


#sql("findU8RdRecord01Id")
select r.* from UFDATA_001_2023.dbo.RdRecord08 r where r.cCode = #para(cCode)
#end