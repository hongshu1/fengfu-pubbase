#sql("paginateAdminDatas")
SELECT
        AuditState =
        CASE WHEN t1.iAuditStatus=0 THEN '未审核'
             WHEN t1.iAuditStatus=1 THEN '待审核'
             WHEN t1.iAuditStatus=2 THEN '审核通过'
             WHEN t1.iAuditStatus=3 THEN '审核不通过' END,
    t1.*,
    t3.InvCode as MoInvCode,
    t3.MONoRow,
    t3.qty as Moqty,
    t4.cDepName,
    t2.cWhName,
    t5.cVenName
FROM
    #(getMomdataDbName()).dbo.T_Sys_MaterialsOut t1
    LEFT JOIN V_Sys_MODetail t3 ON t3.MOId = t1.SourceBillDid
    LEFT JOIN UGCFF_MOM_DATA.dbo.Bd_Warehouse t2 ON t2.cWhCode = t1.Whcode
    LEFT JOIN UGCFF_MOM_DATA.dbo.Bd_Department t4 ON t4.cDepCode = t1.DeptCode
    LEFT JOIN UGCFF_MOM_DATA.dbo.Bd_Vendor t5 ON t5.cVenCode = t1.VenCode
WHERE 1 = 1

    #if(selectparam)
    AND (t1.BillNo LIKE CONCAT('%',#para(selectparam), '%')
    OR t2.cWhName LIKE CONCAT('%', #para(selectparam), '%')
    OR t3.MONoRow LIKE CONCAT('%', #para(selectparam), '%')
    OR  t4.cDepName LIKE CONCAT('%', #para(selectparam), '%')
    OR t5.cVenName LIKE CONCAT('%', #para(selectparam), '%')
    )
    #end
   #if(iorderstatus)
        AND t1.iAuditStatus = #para(iorderstatus)
    #end
    #if(OrgCode)
        AND t1.OrganizeCode = #para(OrgCode)
    #end
#if(startdate)
    and CONVERT(VARCHAR(10),t1.dcreatetime,23) >='#(startdate)'
#end
#if(enddate)
    and CONVERT(VARCHAR(10),t1.dcreatetime,23) <='#(enddate)'
#end
order by t1.dcreatetime desc
    #end

#sql("getMaterialsOutLines")
select
       t2.barcode,
       b.cInvCode as invcode,
       b.cInvName ,
       b.cInvCode1,
       b.cInvAddCode,
       b.cInvName1,
       a.dPlanDate as plandate,
       b.cInvStd as cinvstd,
       a.iQty as qty,
       a.iQty as qtys,
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
       ( SELECT cUomName FROM Bd_Uom WHERE b.iPurchaseUomId = iautoid ) AS PuUnitName,
       ( SELECT cUomCode FROM Bd_Uom WHERE b.iPurchaseUomId = iautoid ) AS PuUnitCode
FROM
    T_Sys_MaterialsOut t1
        LEFT JOIN T_Sys_MaterialsOutDetail t2 ON t2.MasID = t1.AutoId
        LEFT JOIN PS_PurchaseOrderDBatch a ON t2.Barcode = concat ( a.cBarcode, concat ( '-', a.cVersion ))
        LEFT JOIN Bd_Inventory b on a.iinventoryId = b.iAutoId
        LEFT JOIN PS_PurchaseOrderD d on a.iPurchaseOrderDid = d.iAutoId
        LEFT JOIN PS_PurchaseOrderM m on m.iAutoId = d.iPurchaseOrderMid
        LEFT JOIN Bd_Vendor v on m.iVendorId = v.iAutoId
        LEFT JOIN Bd_InventoryClass t3 ON b.iInventoryClassId = t3.iautoid
        LEFT JOIN Bd_EquipmentModel t4 ON b.iEquipmentModelId = t4.iautoid
        LEFT JOIN PS_PurchaseOrderD_Qty tc on tc.iPurchaseOrderDid = d.iAutoId AND tc.iAutoId = a.iPurchaseOrderdQtyId
where
        1=1
    AND  t1.AutoID = '#(autoid)'
    #if(OrgCode)
        AND t1.OrganizeCode = #para(OrgCode)
    #end
    #end



#sql("moDetailData")
SELECT
    md.iType AS SourceBillType,
    md.iAutoId AS SourceBillDid,
    md.cMoDocNo,
    bt.cDepCode,
    bt.cDepName,
    md.iQty
FROM
    Mo_MoDoc md
        LEFT JOIN Bd_Department bt ON bt.iAutoId= md.iDepartmentId
WHERE
        1 = 1
    #if(monorow)
        AND md.cMoDocNo like '%#(monorow)%'
    #end
ORDER BY
    md.dUpdateTime DESC
#end


#sql("getrcvMODetailList")
SELECT
    t1.*,
    md.cMoDocNo,
    md.iQty,
    rs.cRdName,
    dt.cDepName,
    wh.cWhName
FROM
    T_Sys_MaterialsOut t1
        LEFT JOIN Mo_MoDoc md ON md.iAutoId = t1.SourceBillDid
        LEFT JOIN Bd_Rd_Style rs ON rs.cRdCode = t1.RdCode
        LEFT JOIN Bd_Department dt ON dt.cDepCode = t1.DeptCode
        LEFT JOIN Bd_Warehouse wh ON wh.cWhCode = t1.Whcode
WHERE t1.AutoID = #(autoid)
#end

#sql("getRDStyleDatas")
SELECT cRdCode,
       cRdName
FROM Bd_Rd_Style
WHERE cOrgCode = #(OrgCode)
AND bRdFlag = #(bRdFlag)
#end

#sql("pushU8List")
SELECT
    t2.Qty,
    t2.Barcode,
    t1.AutoID AS billid,
    t1.SourceBillDid AS billdid,
    t1.OrganizeCode,
    t1.BillNo,
    dt.cDepName,
    t1.DeptCode,
    t1.Whcode,
    t2.InvCode,
    i.cInvName AS InvName
FROM
    T_Sys_MaterialsOut t1
        LEFT JOIN T_Sys_MaterialsOutDetail t2 ON t2.MasID = t1.AutoID
        LEFT JOIN Bd_Department dt ON dt.iAutoId = t1.DeptCode
        LEFT JOIN bd_inventory i ON i.cinvcode = t2.Invcode
        LEFT JOIN Mo_MoDoc md ON md.iAutoId = t1.SourceBillDid
WHERE t1.AutoID IN (#(autoid))
    #end

#sql("getBarcodeDatas")
SELECT
    concat ( a.cBarcode, concat ( '-', a.cVersion ))  AS barcode,
    b.cInvCode as invcode,
    b.cInvName ,
    b.cInvAddCode,
    b.cInvCode1,
    b.cInvName1,
    a.dPlanDate AS plandate,
    b.cInvStd AS cinvstd,
    a.iQty AS qty,
    a.iQty AS qtys,
    m.cOrderNo AS SourceBillNo,
    m.iBusType AS SourceBillType,
    m.cDocNo+ '-' + CAST ( tc.iseq AS NVARCHAR ( 10 ) ) AS SourceBillNoRow,
    tc.iseq AS RowNo,
    m.iAutoId AS SourceBillID,
    d.iAutoId AS SourceBillDid,
    m.iVendorId,
    v.cVenCode AS vencode,
    v.cVenName AS venname,
    t3.cInvCCode,
    t3.cInvCName,
    t4.cEquipmentModelName,
    ( SELECT cUomName FROM Bd_Uom WHERE b.iInventoryUomId1 = iautoid ) AS InventorycUomName,
    ( SELECT cUomName FROM Bd_Uom WHERE b.iPurchaseUomId = iautoid ) AS PuUnitName,
    ( SELECT cUomCode FROM Bd_Uom WHERE b.iPurchaseUomId = iautoid ) AS PuUnitCode
FROM
    PS_PurchaseOrderDBatch a
        LEFT JOIN Bd_Inventory b ON a.iinventoryId = b.iAutoId
        LEFT JOIN PS_PurchaseOrderD d ON a.iPurchaseOrderDid = d.iAutoId
        LEFT JOIN PS_PurchaseOrderM m ON m.iAutoId = d.iPurchaseOrderMid
        LEFT JOIN Bd_Vendor v ON m.iVendorId = v.iAutoId
        LEFT JOIN Bd_InventoryClass t3 ON b.iInventoryClassId = t3.iautoid
        LEFT JOIN Bd_EquipmentModel t4 ON b.iEquipmentModelId = t4.iautoid
        LEFT JOIN PS_PurchaseOrderD_Qty tc ON tc.iPurchaseOrderDid = d.iAutoId
        AND tc.iAutoId = a.iPurchaseOrderdQtyId
WHERE
        1 = 1
  AND b.cOrgCode = #(orgCode)
    #if(barcodes != null)
        AND concat ( a.cBarcode, concat ( '-', a.cVersion )) not in ( #(barcodes) )
    #end
    #if(q)
		and (concat ( a.cBarcode, concat ( '-', a.cVersion )) like concat('%',#para(q),'%') or b.cinvcode1 like concat('%',#para(q),'%')
			or b.cinvname1 like concat('%',#para(q),'%') or a.cBarcode like concat('%',#para(q),'%')
			or v.cVenCode like concat('%',#para(q),'%')
		)
	#end
	#if(vencode)
		and m.iVendorId = #para(vencode)
	#end
#end