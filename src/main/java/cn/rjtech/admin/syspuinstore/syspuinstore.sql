#sql("recpor")
SELECT
    t1.AutoID,t1.BillType,t1.OrganizeCode,t1.BillDate,t1.RdCode,t1.dCreateTime,t1.cCreateName,
    t1.DeptCode,t1.SourceBillNo,t1.SourceBillID,t1.VenCode,t1.Memo,t1.billno,
    t1.cAuditName,t1.AuditDate,t1.dUpdateTime,t1.cUpdateName,t1.iAuditStatus,t1.ibustype,t1.U8BillNo,
    t2.cRdCode,t2.cRdName,
    t3.Whcode,
    t4.cWhName,
    t5.cDepName,
    t6.cPTName,t6.cptcode,
    t7.cVenCode,
    t7.cVenName
FROM T_Sys_PUInStore t1
     LEFT JOIN Bd_Rd_Style t2 on t1.RdCode = t2.cRdCode
     LEFT JOIN (SELECT DISTINCT a.MasID,a.Whcode FROM T_Sys_PUInStoreDetail a ) t3 on t1.autoid = t3.MasID
     LEFT JOIN Bd_Warehouse t4 on t3.Whcode = t4.cWhCode
     LEFT JOIN Bd_Department t5 on t1.DeptCode = t5.cDepCode
     LEFT JOIN Bd_PurchaseType t6 on t1.BillType = t6.iAutoId
     LEFT JOIN Bd_Vendor t7 on t1.VenCode = t7.cVenCode
WHERE 1=1
#if(billno)
    and t1.billno = #para(billno)
#end
#if(sourcebillno)
    and t1.sourcebillno = #para(sourcebillno)
#end
#if(vencode)
    and t1.vencode = #para(vencode)
#end
#if(state)
    and t1.iAuditStatus = #para(state)
#end
#if(whcode)
    and t1.whcode = #para(whcode)
#end
#if(startTime)
    and t1.dCreateTime >= #para(startTime)
#end
#if(endTime)
    and t1.dCreateTime <= #para(endTime)
#end
order by t1.dUpdateTime desc
#end

#sql("dList")
SELECT  a.*
FROM T_Sys_PUInStoreDetail a
where 1=1
#if(masid)
    and a.MasID = #para(masid)
#end
order by a.dUpdateTime desc
#end

#sql("getWareHouseName")
    select * from V_Sys_WareHouse
#end

#sql("pageDetailList")
SELECT
    a.*,
    i.cinvname,i.cinvcode1,i.cinvstd,i.cinvname1,
    u.cUomCode,u.cUomName
FROM T_Sys_PUInStoreDetail a
left join bd_inventory i on a.invcode = i.cinvcode
left join bd_uom u on i.iPurchaseUomId = u.iautoid
where 1=1
#if(masid)
    and a.MasID = #para(masid)
#end
#if(barcode)
    and a.barcode = #para(barcode)
#end
#if(invcode)
  and a.invcode = #para(invcode)
#end
    order by a.dUpdateTime desc
#end

#sql("getSysPODetail")
select t1.* from V_Sys_PODetail t1
where 1 =1
#if(id)
    and t1.id = #para(id)
#end
#if(billno)
    and t1.billno = #para(billno)
#end
#if(sourcebillno)
    and t1.sourcebillno = #para(sourcebillno)
#end
#if(billid)
    and t1.billid = #para(billid)
#end
#if(billdid)
    and t1.billdid = #para(billdid)
#end

#if(sourcebilldid)
    and t1.sourcebilldid = #para(sourcebilldid)
#end
#if(billtype)
    and t1.billtype = #para(billtype)
#end
#if(deptcode)
    and t1.deptcode = #para(deptcode)
#end
#end

#sql("getMesSysPODetailsByPO")
SELECT
    pt.cPTName AS BillType,
    a.cOrderNo AS SourceBillNo,
    a.iAutoId AS SourceBillID,
    a.dOrderDate AS BillDate,
    a.iPurchaseTypeId,
    ven.cVenCode AS VenCode,
    ven.cVenName AS VenName,
    inv.cInvStd,inv.cInvCode,inv.cInvName,inv.cinvcode1,inv.cinvname1,
    dep.cDepCode,dep.cDepName
FROM
    PS_PurchaseOrderM a
        LEFT JOIN PS_PurchaseOrderD b ON a.iAutoId= b.iPurchaseOrderMid
        AND b.isDeleted<> 1
        LEFT JOIN PS_PurchaseOrderDBatch c ON c.iPurchaseOrderDid = b.iAutoId
        AND c.isEffective= 1
        LEFT JOIN dbo.PS_PurchaseOrderD_Qty tc ON tc.iPurchaseOrderDid = b.iAutoId
        AND tc.iAutoId = c.iPurchaseOrderdQtyId
        LEFT JOIN Bd_Inventory inv ON inv.iAutoId = b.iInventoryId
        LEFT JOIN Bd_PurchaseType pt ON  a.iPurchaseTypeId = pt.iAutoId
        LEFT JOIN Bd_Vendor ven ON  a.iVendorId = ven.iAutoId
        LEFT JOIN Bd_Department dep on dep.iAutoId = a.iDepartmentId
        WHERE 1 = 1
#if(corderno)
and a.cOrderNo = #para(corderno)
#end
#if(sourcebillno)
and a.cOrderNo = #para(sourcebillno)
#end
#if(cinvcode)
and inv.cinvcode like concat('%',#para(cinvcode),'%')
#end
order by a.dUpdateTime desc
#end

#sql("getMesSysPODetailsByOM")
select
    t6.cPTName AS BillType,
    t1.cOrderNo AS SourceBillNo,
    t1.iAutoId AS SourceBillID,
    t1.dOrderDate AS BillDate,
    t1.iPurchaseTypeId,
    t7.cVenCode as vencode,
    t7.cVenName as venname,
    t5.cInvStd,
    t5.cInvCode,
    t5.cInvName,
    t5.cinvcode1,
    t5.cinvname1,
    t8.cDepCode,
    t8.cDepName
    from
    PS_SubcontractOrderM t1
left join PS_SubcontractOrderD t2 on t1.iAutoId=t2.iSubcontractOrderMid
AND t2.isDeleted<> 1
left join PS_SubcontractOrderDBatch t3 on t3.iSubcontractOrderDid=t2.iAutoId
AND t3.isEffective= 1
left join PS_SubcontractOrderD_Qty t4 on t4.iSubcontractOrderDid=t2.iAutoId
AND t4.iAutoId = t3.iSubcontractOrderdQtyId
left join Bd_Inventory t5 on t5.iAutoId=t2.iInventoryId
left join Bd_PurchaseType t6 on t1.iPurchaseTypeId=t6.iAutoId
left join Bd_Vendor t7 on t7.iAutoId = t1.iVendorId
left join Bd_Department t8 on t8.iAutoId = t1.iDepartmentId
WHERE 1 = 1
#if(corderno)
and t1.cOrderNo = #para(corderno)
#end
#if(sourcebillno)
and t1.cOrderNo = #para(sourcebillno)
#end
#if(cinvcode)
and t5.cinvcode like concat('%',#para(cinvcode),'%')
#end
order by t1.dUpdateTime desc
#end

#sql("getPrintData")
select * from T_Sys_PUInStore t1
where 1=1
#if(ids)
  AND CHARINDEX(','+cast((select t1.iAutoId) as nvarchar(20))+',' , ','+#para(ids)+',') > 0
#end
#end

#sql("getInsertPuinstoreDetailByPO")
SELECT
    t1.cOrderNo AS SourceBillNo,
    t1.iAutoId AS SourceBillID,
    t1.dOrderDate AS BillDate,
    t1.iPurchaseTypeId,
    t4.cPTName AS BillType,
    t5.cVenCode AS VenCode,
    t5.cVenName AS VenName,
    t3.cInvStd,
    t3.cInvCode,
    t3.cInvName,
    t3.cinvcode1,
    t3.cinvname1,
    t6.cDepCode,
    t6.cDepName,
    t7.cuomname as puunitname,
    t7.cuomcode as puunitcode
FROM
PS_PurchaseOrderM t1
LEFT JOIN PS_PurchaseOrderD t2 on t1.iAutoId = t2.iPurchaseOrderMid
LEFT JOIN Bd_Inventory t3 on t2.iInventoryId = t3.iAutoId
LEFT JOIN Bd_PurchaseType t4 on t1.iPurchaseTypeId = t4.iAutoId
LEFT JOIN Bd_Vendor t5 on t1.iVendorId = t5.iAutoId
LEFT JOIN Bd_Department t6 on t1.iDepartmentId = t6.iAutoId
LEFT JOIN Bd_uom t7 on t3.iPurchaseUomId = t7.iAutoId
where t1.IsDeleted = '0'
#if(corderno)
and t1.cOrderNo = #para(corderno)
#end
#if(sourcebillno)
and t1.cOrderNo = #para(sourcebillno)
#end
#if(q)
    and (t3.cinvcode like concat('%',#para(q),'%') or t3.cinvcode1 like concat('%',#para(q),'%')
        or t3.cinvname1 like concat('%',#para(q),'%') or t3.cinvstd like concat('%',#para(q),'%')
        )
    order by t3.dUpdateTime desc
#end
#end

#sql("getInsertPuinstoreDetailByOM")
SELECT
t1.cOrderNo AS SourceBillNo,
    t1.iAutoId AS SourceBillID,
    t1.dOrderDate AS BillDate,
    t1.iPurchaseTypeId,
	t4.cPTName AS BillType,
	t5.cVenCode AS VenCode,
    t5.cVenName AS VenName,
	t3.cInvStd,
    t3.cInvCode,
    t3.cInvName,
    t3.cinvcode1,
    t3.cinvname1,
	t6.cDepCode,
	t6.cDepName,
	t7.cuomname as puunitname,
    t7.cuomcode as puunitcode
FROM
PS_SubcontractOrderM t1
LEFT JOIN PS_SubcontractOrderD t2 on t1.iAutoId = t2.iSubcontractOrderMid
LEFT JOIN Bd_Inventory t3 on t2.iInventoryId = t3.iAutoId
LEFT JOIN Bd_PurchaseType t4 on t1.iPurchaseTypeId = t4.iAutoId
LEFT JOIN Bd_Vendor t5 on t1.iVendorId = t5.iAutoId
LEFT JOIN Bd_Department t6 on t1.iDepartmentId = t6.iAutoId
LEFT JOIN Bd_uom t7 on t3.iPurchaseUomId = t7.iAutoId
where t1.IsDeleted = '0'
#if(corderno)
and t1.cOrderNo = #para(corderno)
#end
#if(sourcebillno)
and t1.cOrderNo = #para(sourcebillno)
#end
#if(q)
    and (t3.cinvcode like concat('%',#para(q),'%') or t3.cinvcode1 like concat('%',#para(q),'%')
        or t3.cinvname1 like concat('%',#para(q),'%') or t3.cinvstd like concat('%',#para(q),'%')
        )
    order by t3.dUpdateTime desc
#end
#end

#sql("findEditAndOnlySeeByAutoid")
select t1.*,t2.cvenname,t3.cDepName,t4.crdname,t5.cptcode,t5.cptname
    from T_Sys_PUInStore t1
    left join bd_vendor t2 on t1.vencode = t2.cvencode
    left join Bd_Department t3 on t1.DeptCode = t3.cDepCode
    left join Bd_Rd_Style t4 on t1.RdCode = t4.cRdCode
    left join Bd_PurchaseType t5 on t1.billtype = t5.iAutoId
where 1=1
    #if(autoid)
    and t1.AutoID = #para(autoid)
    #end
    order by t1.dUpdateTime desc
#end

#sql("getBarcodeVersion")
SELECT t1.cOrderNo,t1.iBusType,t1.iPurchaseTypeId,t1.iVendorId,t1.iDepartmentId,t1.cDocNo,
      t2.sourceid,t2.barcode,t2.invcode
    from PS_PurchaseOrderM t1
left join V_Sys_BarcodeDetail t2 on t1.iAutoId = t2.barcodeid
    where 1=1 and EncodingName = 'PurchaseOrderM'
    #if(corderno)
    and t1.corderno = #para(corderno)
    #end
    #if(barcode)
    and t2.barcode like concat('%',#para(barcode),'%')
    #end
order by t1.dUpdateTime desc
#end

#sql("getSourceBillIdAndDid")
SELECT m.iautoid as SourceBillID ,
d.iAutoId as SourceBillDid
from PS_PurchaseOrderM m
left join PS_PurchaseOrderD d on m.iautoid = d.iPurchaseOrderMid
where 1=1
#if(sourcebillno)
and m.corderno=#para(sourcebillno)
#end
#if(iInventoryId)
and d.iInventoryId=#para(iInventoryId)
#end
#end

#sql("findU8UserByUserCode")
select u.* from UFDATA_001_2023.dbo.V_Sys_User u where u.userCode = #para(usercode)
#end

#sql("findU8RdRecord01Id")
select r.* from UFDATA_001_2023.dbo.RdRecord01 r where r.cCode = #para(cCode)
#end

#sql("scanPurchaseOrderBarcode")
select
    t1.cCompleteBarcode as barcode,
    t1.iQty as qty,
    t1.iAutoId as autoid,
    t1.iinventoryId,
	t2.iAutoId as SourceBillDid,
	t3.cOrderNo as SourceBillNo,
    t3.iBusType as SourceBillType,
    t3.cDocNo+'-'+CAST(t5.iseq AS NVARCHAR(10)) as SourceBillNoRow,
    t3.cOrderNo as SourceBillID,
	t4.cinvcode as invcode,
    t4.cinvname,
    t4.cInvCode ,
    t4.cInvCode1,
    t4.cInvName1,
    t4.cinvstd,
	t6.cUomCode as puunitcode,
    t6.cUomName as purchasecuomname,
    t6.cUomName as  puunitname
from PS_PurchaseOrderDBatch t1
LEFT JOIN PS_PurchaseOrderD t2 on t1.iPurchaseOrderDid=t2.iAutoId
LEFT JOIN PS_PurchaseOrderM t3 on t2.iPurchaseOrderMid = t3.iAutoId
LEFT JOIN Bd_Inventory t4 on t1.iinventoryId = t4.iAutoId
LEFT JOIN PS_PurchaseOrderD_Qty t5 on t5.iPurchaseOrderDid = t3.iAutoId AND t5.iAutoId = t1.iPurchaseOrderdQtyId
LEFT JOIN Bd_Uom t6 on t4.iPurchaseUomId = t6.iAutoId
where t1.isEffective = '1'
#if(corderno)
and t3.cOrderNo = #para(corderno)
#end
#if(sourcebillno)
and t3.cOrderNo = #para(sourcebillno)
#end
#if(barcode)
and t1.cCompleteBarcode = #para(barcode)
#end
#if(q)
and (
    t4.cinvcode like concat('%',#para(q),'%') or t4.cinvcode1 like concat('%',#para(q),'%')
    or t4.cinvname1 like concat('%',#para(q),'%') or t1.cCompleteBarcode like concat('%',#para(q),'%')
	)
#end
and t4.cOrgCode = #(orgCode)
#end

#sql("scanSubcontractOrderBarcode")
select
    t1.cCompleteBarcode as barcode,
    t1.iQty as qty,
    t1.iAutoId as autoid,
    t1.iinventoryId,
	t2.iAutoId as SourceBillDid,
	t3.cOrderNo as SourceBillNo,
    t3.iBusType as SourceBillType,
    t3.cDocNo+'-'+CAST(t5.iseq AS NVARCHAR(10)) as SourceBillNoRow,
    t3.cOrderNo as SourceBillID,
	t4.cinvcode as invcode,
    t4.cinvname,
    t4.cInvCode ,
    t4.cInvCode1,
    t4.cInvName1,
    t4.cinvstd,
	t6.cUomCode  as puunitcode,,
    t6.cUomName as purchasecuomname,
    t6.cUomName as  puunitname
from PS_SubcontractOrderDBatch t1
left join PS_SubcontractOrderD t2 on t1.iSubcontractOrderDid = t2.iautoid
left join PS_SubcontractOrderM t3 on t2.iSubcontractOrderMid = t3.iautoid
left join Bd_Inventory t4 on t1.iinventoryId = t4.iAutoId
left join PS_SubcontractOrderD_Qty t5 on t5.iSubcontractOrderDid = t3.iAutoId AND t5.iAutoId = t1.iSubcontractOrderdQtyId
LEFT JOIN Bd_Uom t6 on t4.iPurchaseUomId = t6.iAutoId
where t1.isEffective = '1'
#if(corderno)
and t3.cOrderNo = #para(corderno)
#end
#if(sourcebillno)
and t3.cOrderNo = #para(sourcebillno)
#end
#if(barcode)
and t1.cCompleteBarcode = #para(barcode)
#end
#if(q)
and (
    t4.cinvcode like concat('%',#para(q),'%') or t4.cinvcode1 like concat('%',#para(q),'%')
	or t4.cinvname1 like concat('%',#para(q),'%') or t1.cCompleteBarcode like concat('%',#para(q),'%')
	)
#end
and t4.cOrgCode = #(orgCode)
#end