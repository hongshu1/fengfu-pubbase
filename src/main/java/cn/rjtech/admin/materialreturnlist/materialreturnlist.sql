#sql("paginateAdminDatas")
SELECT
        AuditState =
        CASE WHEN t11.iAuditStatus=0 THEN '已保存'
             WHEN t11.iAuditStatus=1 THEN '待审核'
             WHEN t11.iAuditStatus=2 THEN '已审核'
             WHEN t11.iAuditStatus=3 THEN '审核不通过'END,

        t11.iAuditStatus,
        t2.cVenName,
        t3.cDepName,
        t4.cPTName,
        t5.cRdName,
        t6.cWhName,
        t11.BillNo,
        t11.BillDate,
        t11.SourceBillNo,
        t11.VenCode,
        t11.Memo,
        t11.CreateDate,
        t11.CreatePerson,
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
  and CONVERT(VARCHAR(10),t11.ModifyDate,23) >='#(startdate)'
    #end
    #if(enddate)
  and CONVERT(VARCHAR(10),t11.ModifyDate,23) <='#(enddate)'
    #end
GROUP BY
    t11.AutoID,
    t11.iAuditStatus,
    t2.cVenName,
    t3.cDepName,
    t4.cPTName,
    t5.cRdName,
    t6.cWhName,
    t11.BillNo,
    t11.BillDate,
    t11.SourceBillNo,
    t11.VenCode,
    t11.Memo,
    t11.CreateDate,
    t11.CreatePerson,
    t11.ModifyDate
order by t11.ModifyDate desc
    #end


#sql("getmaterialReturnLists")
SELECT
    t2.AutoID,
    t4.iQty,
    t2.Qty AS qtys,
    t2.Memo,
    t2.MasID,
    t2.spotTicket,
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
    t9.cWhName
FROM
    T_Sys_PUInStoreDetail t2
        LEFT JOIN (
        SELECT
            iAutoId as PoId,
            iPurchaseOrderDid,
            iinventoryId,
            cBarcode,
            iQty,
            cSourceld,
            cSourceBarcode
        FROM
            PS_PurchaseOrderDBatch UNION ALL
        SELECT
            iAutoId as PoId,
            iSubcontractOrderDid,
            iinventoryId,
            cBarcode,
            iQty,
            cSourceld,
            cSourceBarcode
        FROM
            PS_SubcontractOrderDBatch
    ) t4 ON t2.spotTicket = t4.cbarcode
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
    t2.spotTicket,
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
            cBarcode,
            iQty,
            cSourceld,
            cSourceBarcode
        FROM
            PS_PurchaseOrderDBatch UNION ALL
        SELECT
            iAutoId as PoId,
            iSubcontractOrderDid,
            iinventoryId,
            cBarcode,
            iQty,
            cSourceld,
            cSourceBarcode
        FROM
            PS_SubcontractOrderDBatch
    ) t4 ON t2.spotTicket = t4.cbarcode
        LEFT JOIN bd_inventory i ON i.iautoid = t4.iinventoryId
        LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
        LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
WHERE
        t1.AutoID = t2.MasID
  AND ( SELECT SUM ( Qty ) FROM T_Sys_PUInStoreDetail WHERE  spotTicket = t2.spotTicket ) > 0
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
       t5.cRdName,
       t3.cDepName,
       t4.cPTName,
       t1.BillType,
       t1.DeptCode,
       t1.VenCode,
       t6.cVenName,
       t1.CreatePerson
from T_Sys_PUInStore t1
    LEFT JOIN T_Sys_PUInStoreDetail t2 ON t1.AutoID = t2.MasID
    LEFT JOIN Bd_Rd_Style t5 ON t1.RdCode = t5.cRdCode
    LEFT JOIN Bd_Department t3 ON t1.DeptCode = t3.cDepCode
    LEFT JOIN Bd_PurchaseType t4 ON t1.BillType = t4.iAutoId
    LEFT JOIN Bd_Vendor t6 ON t1.VenCode = t6.cVenCode

where 1 =1
    AND t2.Qty > 0
    AND (SELECT SUM(Qty) FROM T_Sys_PUInStoreDetail WHERE spotTicket = t2.spotTicket) > 0
    #if(billno)
        AND t1.billno = #para(billno)
    #end
    #if(sourcebillno)
        AND t1.sourcebillno = #para(sourcebillno)
    #end

GROUP BY
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
    t1.CreatePerson
    #end



#sql("getmaterialLines")
SELECT (SELECT SUM(Qty) FROM T_Sys_PUInStoreDetail WHERE spotTicket = t2.spotTicket) AS qtys,
       0-t2.Qty as qty,
       t2.Memo,
       t2.spotTicket,
       t2.SourceBillDid,
       t2.SourceBillNoRow,
       t2.SourceBillType,
       t2.SourceBillNo,
       u.cUomName AS inventorycuomname,
       t3.cInvCCode,
       t3.cInvCName,
       i.cInvCode,
       i.cInvName,
       i.cInvStd,
       i.cInvCode1,
       i.cInvName1
FROM T_Sys_PUInStore t1,
     T_Sys_PUInStoreDetail t2
         LEFT JOIN (
         SELECT iAutoId AS PoId,
                iPurchaseOrderDid,
                iinventoryId,
                cBarcode,
                iQty,
                cSourceld,
                cSourceBarcode
         FROM PS_PurchaseOrderDBatch
         UNION ALL
         SELECT iAutoId AS PoId,
                iSubcontractOrderDid,
                iinventoryId,
                cBarcode,
                iQty,
                cSourceld,
                cSourceBarcode
         FROM PS_SubcontractOrderDBatch
     ) t4 ON t2.spotTicket = t4.cbarcode
         LEFT JOIN bd_inventory i ON i.iautoid = t4.iinventoryId
         LEFT JOIN Bd_Uom u ON i.iInventoryUomId1 = u.iautoid
         LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
WHERE
        t1.AutoID = t2.MasID
  AND t1.AutoID = '#(autoid)'
  AND (SELECT SUM (Qty) FROM T_Sys_PUInStoreDetail WHERE  spotTicket =t2.spotTicket )> 0
    #if(OrgCode)
  AND t1.OrganizeCode = #para(OrgCode)
    #end
GROUP BY
    t1.AutoID,
    t2.Memo,
    t2.MasID,
    t2.spotTicket,
    t2.SourceBillDid,
    t2.SourceBillNoRow,
    t2.SourceBillType,
    t2.SourceBillNo,
    u.cUomName,
    t3.cInvCCode,
    t3.cInvCName,
    i.cInvCode,
    i.cInvName,
    i.cInvStd,
    i.cInvCode1,
    i.cInvName1,
    t2.Qty
#end




#sql("pushU8List")
SELECT
    t1.OrganizeCode,
    t2.MasID,
    t2.Whcode AS iwhcode,
    (SELECT cWhName FROM Bd_Warehouse WHERE t2.Whcode = cWhCode) AS iwhname,
    t1.VenCode,
    (SELECT cVenName FROM Bd_Vendor WHERE t1.VenCode = cVenCode) AS VenName,
    t2.Qty,
    i.cInvCode AS InvCode,
    t1.BillNo,
    t1.BillDate,
    t2.SourceBillNo,
    t2.SourceBillID,
    t2.SourceBillDid,
    t2.SourceBillType,
    t2.SourceBillNoRow,
    t1.RdCode AS IcRdCode,
    t2.PosCode

FROM
    T_Sys_PUInStore t1
        LEFT JOIN T_Sys_PUInStoreDetail t2 ON t2.MasID = t1.AutoID
        LEFT JOIN (
        SELECT
            iPurchaseOrderDid,
            iinventoryId,
            cBarcode,
            iQty,
            cSourceld,
            cSourceBarcode
        FROM
            PS_PurchaseOrderDBatch UNION ALL
        SELECT
            iSubcontractOrderDid,
            iinventoryId,
            cBarcode,
            iQty,
            cSourceld,
            cSourceBarcode
        FROM
            PS_SubcontractOrderDBatch
    ) t4 ON t2.spotTicket = t4.cbarcode
        LEFT JOIN bd_inventory i ON i.iautoid = t4.iinventoryId
WHERE t1.AutoID = '#(autoid)'
  AND t2.Qty < 0

#end


#sql("getBarcodeDatas")
select top #(limit)
    m.cOrderNo as sourcebillno,
        a.cBarcode as barcode,
       b.cInvCode ,
       b.cInvName ,
       b.cInvCode1,
       b.cInvName1,
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
       t3.cInvCCode,
       t3.cInvCName,
       t4.cEquipmentModelName,
       (SELECT cUomName FROM Bd_Uom WHERE b.iInventoryUomId1 = iautoid) as InventorycUomName
FROM PS_PurchaseOrderDBatch a
         LEFT JOIN Bd_Inventory b on a.iinventoryId = b.iAutoId
         LEFT JOIN PS_PurchaseOrderD d on a.iPurchaseOrderDid = d.iAutoId
         LEFT JOIN PS_PurchaseOrderM m on m.iAutoId = d.iPurchaseOrderMid
         LEFT JOIN Bd_Vendor v on m.iVendorId = v.iAutoId
         LEFT JOIN Bd_InventoryClass t3 ON b.iInventoryClassId = t3.iautoid
         LEFT JOIN Bd_EquipmentModel t4 ON b.iEquipmentModelId = t4.iautoid
         LEFT JOIN T_Sys_PUReceiveDetail pd on pd.Barcode = a.cBarcode AND pd.isDeleted = '0'
         LEFT JOIN PS_PurchaseOrderD_Qty tc on tc.iPurchaseOrderDid = d.iAutoId AND tc.iAutoId = a.iPurchaseOrderdQtyId
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

