#sql("getAutocompleteData")
select top #(limit)
    i.*,
    u.cUomClassName,
    (SELECT cUomName FROM Bd_Uom WHERE i.iInventoryUomId1 = iautoid) as InventorycUomName,
    (SELECT cUomName FROM Bd_Uom WHERE i.iPurchaseUomId = iautoid) as PurchasecUomName,
    t3.cInvCCode,
    t3.cInvCName,
    t4.cEquipmentModelName
from bd_inventory i
         LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
         LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
         LEFT JOIN Bd_EquipmentModel t4 ON i.iEquipmentModelId = t4.iautoid

where 1=1
    #if(q)
		and (i.cinvcode like concat('%',#para(q),'%') or i.cinvcode1 like concat('%',#para(q),'%')
			or i.cinvname1 like concat('%',#para(q),'%') or i.cinvstd like concat('%',#para(q),'%')
			or u.cUomClassName like concat('%',#para(q),'%')
		)
	#end
#end



#sql("getBarcodeDatas")
select top #(limit)
       t1.Barcode,
       t1.SourceID as SourceBIllNoRow,
       t1.SourceBillType as SourceBillType,
       t1.SourceBillNo,t1.Qty,t1.BarcodeDate,
       t1.BarcodeID as SourceBillID,
       t1.MasID as SourceBillDid,
       t1.pat,
       i.*,
       (SELECT cUomName FROM Bd_Uom WHERE i.iInventoryUomId1 = iautoid) as InventorycUomName,
       (SELECT cUomName FROM Bd_Uom WHERE i.iPurchaseUomId = iautoid) as PurchasecUomName,
       (SELECT cContainerCode FROM Bd_Container WHERE i.iContainerClassId = iautoid) as cContainerCode,
       u.cUomClassName,
       t3.cInvCCode,
       t3.cInvCName,
       t4.cEquipmentModelName
from
    V_Sys_BarcodeDetail t1
         LEFT JOIN bd_inventory i ON i.cinvcode = t1.Invcode
         LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
         LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
         LEFT JOIN Bd_EquipmentModel t4 ON i.iEquipmentModelId = t4.iautoid
where 1=1
    #if(q)
		and (i.cinvcode like concat('%',#para(q),'%') or i.cinvcode1 like concat('%',#para(q),'%')
			or i.cinvname1 like concat('%',#para(q),'%') or i.cinvstd like concat('%',#para(q),'%')
			or u.cUomClassName like concat('%',#para(q),'%') or t1.Barcode like concat('%',#para(q),'%')
		)
	#end
	 AND t1.OrganizeCode = #(orgCode)
#end


#sql("paginateAdminDatas")
SELECT
        AuditState =
        CASE WHEN t1.iAuditStatus=0 THEN '未审批'
             WHEN t1.iAuditStatus=1 THEN '待审批'
             WHEN t1.iAuditStatus=2 THEN '已审批'
             WHEN t1.iAuditStatus=3 THEN '审批不通过' END,
        TypeName =
        CASE WHEN t1.Type='OtherOutMES' THEN '手动新增'END,
        t1.*,
        dt.cDepName,
    m.cMoDocNo  ###工单号
FROM
    T_Sys_OtherOut t1
    LEFT JOIN Bd_Department dt ON dt.iAutoId = t1.DeptCode
    LEFT JOIN  Mo_MoDoc m ON t1.sourcebilldid=m.iAutoId
WHERE 1 = 1
    AND t1.Type = 'OtherOutMES'
    #if(selectparam)
    AND (t1.BillNo LIKE CONCAT('%',#para(selectparam), '%')
    OR t1.SourceBillType LIKE CONCAT('%', #para(selectparam), '%')
    OR  dt.cDepName LIKE CONCAT('%', #para(selectparam), '%')
    )
    #end
   #if(iorderstatus)
        AND t1.Status = #para(iorderstatus)
    #end
#if(startdate)
    and CONVERT(VARCHAR(10),t1.ModifyDate,23) >='#(startdate)'
#end
#if(enddate)
    and CONVERT(VARCHAR(10),t1.ModifyDate,23) <='#(enddate)'
#end
#if(sourcebilldid)
    and    t1.SourceBillDid = #para(sourcebilldid)
#end
#if(idepartmentid)
  AND  t4.iDepartmentId =#para(idepartmentid)
#end
order by t1.dCreateTime desc,t1.BillNo desc
    #end



#sql("getOtherOutLines")
SELECT
    i.*,
    t2.AutoID,
    u.cUomClassName,
    t3.cInvCCode,
    t3.cInvCName,
    t2.Qty AS qtys,
    t2.Qty as qty,
    t2.Barcode,
    t2.InvCode
FROM T_Sys_OtherOut t1,
     T_Sys_OtherOutDetail t2
         LEFT JOIN bd_inventory i ON i.cinvcode = t2.Invcode
         LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
         LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
WHERE
    t1.AutoID = t2.MasID AND  t1.AutoID = '#(autoid)'
    AND t2.Qty > 0
#end


#sql("TableBarcodeData")
select m.cOrderNo AS sourcebillno,
       a.cCompleteBarcode AS barcode,
       b.cInvCode AS invcode,
       b.cInvCode1,
       b.cInvName1,
       b.cInvName,
       b.cInvAddCode,
       a.dPlanDate AS plandate,
       b.cInvStd AS cinvstd,
       a.iQty AS qty,
       a.iQty AS qtys,
       m.cOrderNo AS SourceBillNo,
       m.iBusType AS SourceBillType,
       m.cDocNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
       m.cOrderNo AS SourceBillID,
       d.iAutoId AS SourceBillDid,
       m.iVendorId,
       v.cVenCode AS vencode,
       v.cVenName AS venname,
       uom.cUomCode AS purchasecuomcode,
       uom.cUomName AS purchasecuomname,
       config.iWarehouseId
FROM
    PS_PurchaseOrderDBatch a
        LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
        LEFT JOIN PS_PurchaseOrderD d ON a.iPurchaseOrderDid = d.iAutoId
        LEFT JOIN PS_PurchaseOrderM m ON m.iAutoId = d.iPurchaseOrderMid
        LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
        LEFT JOIN T_Sys_PUReceiveDetail pd ON pd.Barcode = a.cBarcode
        AND pd.isDeleted = '0'
        LEFT JOIN PS_PurchaseOrderD_Qty tc ON tc.iPurchaseOrderDid = d.iAutoId
        AND tc.iAutoId = a.iPurchaseOrderdQtyId
        LEFT JOIN Bd_Uom uom ON b.iPurchaseUomId = uom.iAutoId
        LEFT JOIN Bd_InventoryStockConfig config ON config.iInventoryId = b.iAutoId
where 1=1
	#if(invcode != null)
        AND b.cInvCode  in ( #(invcode) )
    #end
    #if(barcode != null)
        AND a.cCompleteBarcode = '#(barcode)'
    #end
    #if(orgCode != null)
    AND b.cOrgCode = '#(orgCode)'
    #end
#end