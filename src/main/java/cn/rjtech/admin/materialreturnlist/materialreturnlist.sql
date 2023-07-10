#sql("paginateAdminDatas")
SELECT
        AuditState =
        CASE WHEN t11.iAuditStatus=0 THEN '未审核'
             WHEN t11.iAuditStatus=1 THEN '待审核'
             WHEN t11.iAuditStatus=2 THEN '已审核'
             WHEN t11.iAuditStatus=3 THEN '审核不通过'END,
        SourceType =
        CASE WHEN t11.SourceType=0 THEN '物料退货'
             WHEN t11.SourceType=1 THEN '整单退货'
             END,

        t11.iAuditStatus,
        t2.cVenName,
        t3.cDepName,
        t4.cPTName,
        t5.cRdName,
        t6.cWhName,
        t11.BillNo,
        t11.U8BillNo,
        t11.BillDate,
        t11.SourceBillNo,
        t11.VenCode,
        t11.Memo,
        t11.dCreateTime,
        t11.cCreateName,
        t11.AutoID
FROM

    T_Sys_PUInStore t11
        LEFT JOIN Bd_Vendor t2 ON t11.VenCode = t2.cVenCode
        LEFT JOIN Bd_Department t3 ON t11.DeptCode = t3.cDepCode
        LEFT JOIN Bd_PurchaseType t4 ON t11.BillType = t4.cRdCode
        LEFT JOIN Bd_Rd_Style t5 ON t11.RdCode = t5.cRdCode
        LEFT JOIN T_Sys_PUInStoreDetail t22 ON  t11.AutoID = t22.MasID
        LEFT JOIN Bd_Warehouse t6 ON t22.Whcode = t6.cWhCode
WHERE 1 = 1
  AND t22.Qty < 0
  AND t11.isDeleted = 0
    #if(deptcode)
  AND  t3.cDepName like '%#(deptcode)%'
    #end
    #if(whcode)
  AND  t6.cWhName like '%#(whcode)%'
    #end
    #if(billno)
  AND  t11.BillNo like '%#(billno)%'
    #end
    #if(sourcebillno)
  AND  t11.SourceBillNo like '%#(sourcebillno)%'
    #end
    #if(iorderstatus)
  AND t11.iAuditStatus = #para(iorderstatus)
    #end
    #if(OrgCode)
  AND t11.OrganizeCode = #para(OrgCode)
    #end
    #if(startdate)
  and CONVERT(VARCHAR(10),t11.dCreateTime,23) >='#(startdate)'
    #end
    #if(enddate)
  and CONVERT(VARCHAR(10),t11.dCreateTime,23) <='#(enddate)'
    #end
GROUP BY
    t11.AutoID,
    t11.iAuditStatus,
    t11.SourceType,
    t2.cVenName,
    t3.cDepName,
    t4.cPTName,
    t5.cRdName,
    t6.cWhName,
    t11.BillNo,
    t11.U8BillNo,
    t11.BillDate,
    t11.SourceBillNo,
    t11.VenCode,
    t11.Memo,
    t11.cCreateName,
    t11.dCreateTime
order by t11.dCreateTime desc
    #end


#sql("getmaterialReturnLists")
SELECT
    t2.AutoID,
    t4.iQty,
    t2.Qty AS qtys,
    t2.Memo,
    t2.MasID,
    t2.BarCode,
    t2.SourceBillDid,
    t2.SourceBillNoRow,
    t2.SourceBillType,
    t2.SourceBillNo,
    u.cUomClassName,
    t3.cInvCCode,
    t3.cInvCName,
    i.cInvCode,
    i.cInvName,
    i.cInvCode1,
    i.cInvName1,
    i.cInvStd,
    (SELECT cUomName FROM Bd_Uom WHERE i.iInventoryUomId1 = iautoid) as InventorycUomName,

    t1.RdCode,
    t5.cRdName,
    t1.BillType,
    t8.cPTName,
    t1.DeptCode,
    t7.cDepName,
    t1.VenCode,
    t6.cVenName,
    t2.Whcode,
    t2.PosCode,
    t9.cWhName
FROM
    T_Sys_PUInStoreDetail t2
        LEFT JOIN (
        SELECT
            iAutoId as PoId,
            iPurchaseOrderDid,
            iinventoryId,
            cCompleteBarcode,
            iQty,
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
            cSourceld,
            cSourceBarcode
        FROM
            PS_SubcontractOrderDBatch
    ) t4 ON t2.BarCode = t4.cCompleteBarcode
        LEFT JOIN bd_inventory i ON i.iautoid = t4.iinventoryId
        LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
        LEFT JOIN Bd_Warehouse t9 ON t2.Whcode = t9.cWhCode
        LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid,
    T_Sys_PUInStore t1
	left join Bd_Rd_Style t5 on t1.RdCode = t5.cRdCode
    left join Bd_Department t7 on t1.DeptCode = t7.cDepCode
    left join Bd_PurchaseType t8 on t1.BillType = t8.iAutoId
    left join Bd_Vendor t6 on t1.VenCode = t6.cVenCode
WHERE
        t1.AutoID = t2.MasID
        AND t2.Qty < 0
        AND  t1.AutoID = '#(autoid)'
    #if(OrgCode)
        AND t1.OrganizeCode = #para(OrgCode)
    #end
    #end


#sql("getmaterialReturnListLines")
SELECT
    t4.iQty,
    t2.Qty AS qtys,
    t2.Memo,
    t2.MasID,
    t2.BarCode,
    t2.SourceBillDid,
    t2.SourceBillNoRow,
    t2.SourceBillType,
    t2.SourceBillNo,
    u.cUomClassName,
    t3.cInvCCode,
    t3.cInvCName,
    i.cInvCode,
    i.cInvName,
    i.cInvCode1,
    i.cInvName1
FROM
    T_Sys_PUInStore t1,
    T_Sys_PUInStoreDetail t2
        LEFT JOIN (
        SELECT
            iAutoId as PoId,
            iPurchaseOrderDid,
            iinventoryId,
            cCompleteBarcode,
            iQty,
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
            cSourceld,
            cSourceBarcode
        FROM
            PS_SubcontractOrderDBatch
    ) t4 ON t2.BarCode = t4.cCompleteBarcode
        LEFT JOIN bd_inventory i ON i.iautoid = t4.iinventoryId
        LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
        LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
WHERE
        t1.AutoID = t2.MasID
  AND ( SELECT SUM ( Qty ) FROM T_Sys_PUInStoreDetail WHERE  BarCode = t2.BarCode ) > 0
  AND t2.Qty > 0
  AND  t1.AutoID = '#(autoid)'
    #if(OrgCode)
        AND t1.OrganizeCode = #para(OrgCode)
    #end
    #end

#sql("getSysPODetail")
select t1.AutoID,
       t1.BillNo,
       t1.SourceBillNo,
       t1.RdCode,
       t1.BillDate,
       t1.WhCode,
       t1.WhName,
       t5.cRdName,
       t3.cDepName,
       t4.cPTName,
       t1.BillType,
       t1.DeptCode,
       t1.VenCode,
       t6.cVenName,
       t1.cCreateName
from T_Sys_PUInStore t1
    LEFT JOIN T_Sys_PUInStoreDetail t2 ON t1.AutoID = t2.MasID
    LEFT JOIN Bd_Rd_Style t5 ON t1.RdCode = t5.cRdCode
    LEFT JOIN Bd_Department t3 ON t1.DeptCode = t3.cDepCode
    LEFT JOIN Bd_PurchaseType t4 ON t1.BillType = t4.iAutoId
    LEFT JOIN Bd_Vendor t6 ON t1.VenCode = t6.cVenCode

where 1 =1
    AND t2.Qty > 0
    AND iAuditStatus = 2
    AND (SELECT SUM(Qty) FROM T_Sys_PUInStoreDetail WHERE BarCode = t2.BarCode) > 0
    #if(billno)
        AND t1.billno = #para(billno)
    #end
    #if(sourcebillno)
        AND t1.sourcebillno = #para(sourcebillno)
    #end
GROUP BY
    t1.WhCode,
    t1.WhName,
    t1.BillType,
    t1.DeptCode,
    t1.VenCode,
    t1.RdCode,
    t1.AutoID,
    t1.BillNo,
    t1.BillDate,
    t1.SourceBillNo,
    t5.cRdName,
    t3.cDepName,
    t4.cPTName,
    t6.cVenName,
    t1.cCreateName,
    t1.dCreateTime
ORDER BY t1.dCreateTime DESC
    #end



    #sql("getmaterialLines")
SELECT
    ( SELECT SUM ( Qty ) FROM T_Sys_PUInStoreDetail WHERE BarCode = pd.BarCode ) AS qtys,
    0 - pd.Qty AS qty,
    pd.BarCode,
    pd.PosCode,
    b.cInvCode ,
    b.cInvName ,
    b.cInvCode1,
    b.cInvName1,
    a.dPlanDate AS plandate,
    b.cInvStd AS cinvstd,
    m.cOrderNo AS SourceBillNo,
    m.iBusType AS SourceBillType,
    m.cOrderNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
    m.iAutoId AS SourceBillID,
    d.iAutoId AS SourceBillDid,
    m.iVendorId,
    v.cVenCode AS vencode,
    v.cVenName AS venname,
    t3.cInvCCode,
    t3.cInvCName,
    t4.cEquipmentModelName,
    wh.cWhCode AS WhCode,
    wh.cWhName AS whname,
    area.cAreaCode AS poscode,
    area.cAreaName AS posname,
    ( SELECT cUomName FROM Bd_Uom WHERE b.iInventoryUomId1 = iautoid ) AS InventorycUomName ,
    ( SELECT cUomName FROM Bd_Uom WHERE b.iPurchaseUomId = iautoid ) AS PuUnitName ,
    ( SELECT cUomCode FROM Bd_Uom WHERE b.iPurchaseUomId = iautoid ) AS PuUnitCode
FROM
    T_Sys_PUInStore t1
        LEFT JOIN T_Sys_PUInStoreDetail pd ON t1.AutoID = pd.MasID
        LEFT JOIN PS_PurchaseOrderDBatch a ON pd.BarCode = a.cCompleteBarcode
        AND a.isEffective = '1'
        LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
        LEFT JOIN PS_PurchaseOrderD d ON a.iPurchaseOrderDid = d.iAutoId
        LEFT JOIN PS_PurchaseOrderM m ON m.iAutoId = d.iPurchaseOrderMid
        LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
        LEFT JOIN Bd_InventoryClass t3 ON b.iInventoryClassId = t3.iautoid
        LEFT JOIN Bd_EquipmentModel t4 ON b.iEquipmentModelId = t4.iautoid
        LEFT JOIN PS_PurchaseOrderD_Qty tc ON tc.iPurchaseOrderDid = d.iAutoId
        AND tc.iAutoId = a.iPurchaseOrderdQtyId

        LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
        LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
        LEFT JOIN Bd_Warehouse wh ON wh.iAutoId = config.iWarehouseId
WHERE
    1 = 1
  AND t1.AutoID = '#(autoid)'
  AND (SELECT SUM (Qty) FROM T_Sys_PUInStoreDetail WHERE  BarCode =pd.BarCode )> 0
    #if(OrgCode)
  AND t1.OrganizeCode = #para(OrgCode)
    #end
#end




#sql("pushU8List")
SELECT
    m.cOrderNo AS SourceBillNo,
    m.cOrderNo + '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
    m.iAutoId AS SourceBillID,
    d.iAutoId AS SourceBillDid,
    tc.iSeq AS RowNo,
    b.cInvCode AS invcode,
    b.cInvName AS invname,
    b.cInvCode1,
    b.cInvName1,
    b.cInvStd AS cinvstd,
    uom.cUomCode,
    uom.cUomName,
    change.iBeforeInventoryId,
    change.iAfterInventoryId,
    wh.cWhCode AS whcode,
    wh.cWhName AS whname,
    area.cAreaCode AS poscode,
    area.cAreaName AS posname,
    t1.VenCode,
    bv.cVenName AS VenName,
    t1.BillNo,
    t1.AutoID AS BillID,
    t1.BillDate,
    t2.AutoID AS BillDid,
    t1.RdCode,
    t2.Qty,
    t2.BarCode,
    t1.cCreateName,
    t1.dCreateTime,
    t1.OrganizeCode,
    BillType =
    CASE WHEN t2.SourceBillType=0 THEN 'PO'
         WHEN t2.SourceBillType=1 THEN 'MO' END
FROM
    T_Sys_PUInStore t1
        LEFT JOIN T_Sys_PUInStoreDetail t2 ON t2.MasID = t1.AutoID
        LEFT JOIN (
        SELECT
            iinventoryId,
            iPurchaseOrderDid,
            iPurchaseOrderdQtyId,
            cCompleteBarcode AS barcode,
            CONVERT ( VARCHAR ( 10 ), dPlanDate, 120 ) AS dPlanDate,
            iQty AS qty,
            iQty AS quantity,
            isEffective
        FROM
            PS_PurchaseOrderDBatch UNION ALL
        SELECT
            sub.iinventoryId,
            sub.iSubcontractOrderdQtyId,
            sub.iSubcontractOrderDid,
            sub.cCompleteBarcode AS barcode,
            CONVERT ( VARCHAR ( 10 ), sub.dPlanDate, 120 ) AS dPlanDate,
            sub.iQty AS qty,
            sub.iQty AS quantity,
            sub.isEffective
        FROM
            PS_SubcontractOrderDBatch sub
                LEFT JOIN PS_SubcontractOrderD sd ON sub.iSubcontractOrderDid = sd.iAutoId
                LEFT JOIN PS_SubcontractOrderM sm ON sm.iAutoId = sd.iSubcontractOrderMid
                LEFT JOIN PS_SubcontractOrderD_Qty sc ON sc.iSubcontractOrderDid = sd.iAutoId AND sc.iAutoId = sub.iSubcontractOrderdQtyId
    ) a ON t2.BarCode = a.barcode
        LEFT JOIN Bd_Vendor bv ON bv.cvencode = t1.VenCode
        LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
        LEFT JOIN PS_PurchaseOrderD d ON a.iPurchaseOrderDid = d.iAutoId
        LEFT JOIN PS_PurchaseOrderM m ON m.iAutoId = d.iPurchaseOrderMid
        LEFT JOIN PS_PurchaseOrderD_Qty tc ON tc.iPurchaseOrderDid = d.iAutoId AND tc.iAutoId = a.iPurchaseOrderdQtyId

        LEFT JOIN Bd_Uom uom ON b.iPurchaseUomId = uom.iAutoId
        LEFT JOIN Bd_InventoryChange change ON change.iBeforeInventoryId= b.iAutoId
        LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
        LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
        LEFT JOIN Bd_Warehouse wh ON wh.iAutoId = config.iWarehouseId
WHERE t1.AutoID = '#(autoid)'
    AND a.isEffective = '1'
    AND t2.Qty < 0

#end


#sql("getBarcodeDatas")
select top #(limit)
       a.cCompleteBarcode AS barcode,
       b.cInvCode AS InvCode,
       b.cInvName ,
       b.cInvCode1,
       b.cInvAddCode,
       b.cInvName1,
       a.dPlanDate AS plandate,
       b.cInvStd AS cinvstd,
       0 - a.iQty AS qty,
       a.iQty,
       m.cOrderNo AS SourceBillNo,
       m.iBusType AS SourceBillType,
       m.cOrderNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
       tc.iseq AS RowNo,
       m.iAutoId AS SourceBillID,
       d.iAutoId AS SourceBillDid,
       m.iVendorId,
       v.cVenCode AS vencode,
       v.cVenName AS venname,
       t3.cInvCCode,
       t3.cInvCName,
       t4.cEquipmentModelName,
       wh.cWhCode AS WhCode,
       wh.cWhName AS whname,
       area.cAreaCode AS poscode,
       area.cAreaName AS posname,
       ( SELECT cUomName FROM Bd_Uom WHERE b.iInventoryUomId1 = iautoid ) AS InventorycUomName,
       ( SELECT cUomName FROM Bd_Uom WHERE b.iPurchaseUomId = iautoid ) AS PuUnitName,
       ( SELECT cUomCode FROM Bd_Uom WHERE b.iPurchaseUomId = iautoid ) AS PuUnitCode
FROM
    T_Sys_PUInStoreDetail pd
        LEFT JOIN PS_PurchaseOrderDBatch a ON pd.BarCode = a.cCompleteBarcode
        LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
        LEFT JOIN PS_PurchaseOrderD d ON a.iPurchaseOrderDid = d.iAutoId
        LEFT JOIN PS_PurchaseOrderM m ON m.iAutoId = d.iPurchaseOrderMid
        LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
        LEFT JOIN Bd_InventoryClass t3 ON b.iInventoryClassId = t3.iautoid
        LEFT JOIN Bd_EquipmentModel t4 ON b.iEquipmentModelId = t4.iautoid
        LEFT JOIN PS_PurchaseOrderD_Qty tc ON tc.iPurchaseOrderDid = d.iAutoId
        AND tc.iAutoId = a.iPurchaseOrderdQtyId

        LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
        LEFT JOIN Bd_Warehouse_Area area ON area.iAutoId = config.iWarehouseAreaId
        LEFT JOIN Bd_Warehouse wh ON wh.iAutoId = config.iWarehouseId
WHERE
    1=1
    AND pd.MasID = '#(autoid)'
    AND b.cOrgCode = #(orgCode)
    #if(barcodes != null)
        AND pd.barcode not in ( #(barcodes) )
    #end
    #if(q)
		and (b.cinvcode like concat('%',#para(q),'%') or b.cinvcode1 like concat('%',#para(q),'%')
			or b.cinvname1 like concat('%',#para(q),'%') or a.cCompleteBarcode like concat('%',#para(q),'%')
			or v.cVenCode like concat('%',#para(q),'%')
		)
	#end
	#if(vencode)
		and m.iVendorId = #para(vencode)
	#end
#end

#sql("barcodeDatas")
SELECT *
FROM T_Sys_PUInStoreDetail
WHERE isDeleted = 0
    AND MasID = '#(autoid)'
    #if(barcode)
		and barcode = #para(barcode)
	#end
#end

#sql("getBarcodes")
select
    pd.PosCode,
    a.cCompleteBarcode as barcode,
    b.cInvCode ,
    b.cInvName ,
    b.cInvCode1,
    b.cInvName1,
    a.dPlanDate as plandate,
    b.cInvStd as cinvstd,
    0 - a.iQty AS qty,
    a.iQty,
    m.cOrderNo as SourceBillNo,
    m.iBusType as SourceBillType,
    m.cDocNo+'-'+CAST(tc.iseq AS NVARCHAR(10)) as SourceBillNoRow,
    tc.iseq as RowNo,
    m.iAutoId AS SourceBillID,
    d.iAutoId AS SourceBillDid,
    m.iVendorId,
    v.cVenCode as vencode,
    v.cVenName as venname,
    t3.cInvCCode,
    t3.cInvCName,
    t4.cEquipmentModelName,
    (SELECT cUomName FROM Bd_Uom WHERE b.iInventoryUomId1 = iautoid) as InventorycUomName,
    ( SELECT cUomName FROM Bd_Uom WHERE b.iPurchaseUomId = iautoid ) AS PuUnitName ,
    ( SELECT cUomCode FROM Bd_Uom WHERE b.iPurchaseUomId = iautoid ) AS PuUnitCode
FROM T_Sys_PUInStoreDetail pd
         LEFT JOIN PS_PurchaseOrderDBatch a ON pd.barcode = a.cCompleteBarcode
         LEFT JOIN Bd_Inventory b on a.iinventoryId = b.iAutoId
         LEFT JOIN PS_PurchaseOrderD d on a.iPurchaseOrderDid = d.iAutoId
         LEFT JOIN PS_PurchaseOrderM m on m.iAutoId = d.iPurchaseOrderMid
         LEFT JOIN Bd_Vendor v on m.iVendorId = v.iAutoId
         LEFT JOIN Bd_InventoryClass t3 ON b.iInventoryClassId = t3.iautoid
         LEFT JOIN Bd_EquipmentModel t4 ON b.iEquipmentModelId = t4.iautoid
         LEFT JOIN PS_PurchaseOrderD_Qty tc on tc.iPurchaseOrderDid = d.iAutoId AND tc.iAutoId = a.iPurchaseOrderdQtyId
where
    1=1
    AND pd.MasID = '#(autoid)'
    #if(detailHidden != null)
    AND pd.barcode not in ( #(detailHidden) )
    #end

#end
