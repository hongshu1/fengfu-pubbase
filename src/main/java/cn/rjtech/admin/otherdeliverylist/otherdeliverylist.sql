#sql("paginateAdminDatas")
SELECT
        AuditState =
        CASE WHEN t1.AuditStatus=0 THEN '未审核'
             WHEN t1.AuditStatus=1 THEN '待审核'
             WHEN t1.AuditStatus=2 THEN '审核通过'
             WHEN t1.AuditStatus=3 THEN '审核不通过' END,
        TypeName =
        CASE WHEN t1.Type='OtherOutMES' THEN '特殊领料单'
             WHEN t1.Type='OtherOut' THEN '手动新增'END,
    t1.*,
    t4.cDepName,
    t2.cWhName,
    t5.cRdName
FROM
    T_Sys_OtherOut t1
    LEFT JOIN Bd_Warehouse t2 ON t2.cWhCode = t1.Whcode
    LEFT JOIN Bd_Department t4 ON t4.iAutoId = t1.DeptCode
    LEFT JOIN Bd_Rd_Style t5 ON t5.cRdCode = t1.RdCode
WHERE 1 = 1

    #if(billno)
        AND t1.BillNo like '%#(billno)%'
    #end
    #if(whname)
        AND t2.cWhName like '%#(whname)%'
    #end
    #if(deptname)
        AND t4.cDepName like '%#(deptname)%'
    #end
   #if(iorderstatus)
        AND t1.AuditStatus = #para(iorderstatus)
    #end
    #if(OrgCode)
        AND t1.OrganizeCode = #para(OrgCode)
    #end
#if(startdate)
    and CONVERT(VARCHAR(10),t1.ModifyDate,23) >='#(startdate)'
#end
#if(enddate)
    and CONVERT(VARCHAR(10),t1.ModifyDate,23) <='#(enddate)'
#end
order by t1.ModifyDate desc
    #end

#sql("getOtherOutLines")
SELECT t2.*,
       i.*,
       u.cUomClassName,
       t3.cInvCCode,
       t3.cInvCName
FROM T_Sys_OtherOut t1,
     T_Sys_OtherOutDetail t2
         LEFT JOIN bd_inventory i ON i.cinvcode = t2.Invcode
         LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
         LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
WHERE
        t1.AutoID = t2.MasID AND  t1.AutoID = '#(autoid)'
    #end




#sql("getMaterialsOutLines")
SELECT t2.*,
       i.*,
       u.cUomClassName,
       t3.cInvCCode,
       t3.cInvCName
FROM T_Sys_MaterialsOut t1,
     T_Sys_MaterialsOutDetail t2
         LEFT JOIN bd_inventory i ON i.cinvcode = t2.Invcode
         LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
         LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
WHERE
    t1.AutoID = t2.MasID
    AND  t1.AutoID = '#(autoid)'
    #if(OrgCode)
        AND t1.OrganizeCode = #para(OrgCode)
    #end
    #end



#sql("moDetailData")
SELECT *
FROM V_Sys_MODetail
WHERE 1 = 1
    #if(monorow)
        AND MONoRow like '%#(monorow)%'
    #end
#end


#sql("getrcvMODetailList")
SELECT *
FROM V_Sys_MODetail
WHERE MOId = #(iautoid)
#end

#sql("getRDStyleDatas")
SELECT cRdCode,
       cRdName
FROM Bd_Rd_Style
WHERE cOrgCode = #(OrgCode)
AND bRdFlag = #(bRdFlag)
#end


#sql("otherOutBarcodeDatas")
select t1.AutoID AS SourceID,
       t1.SourceID AS SourceBIllNoRow,
       t1.SourceBillType,
       t2.Qty,
       t2.Barcode,
       t1.CreateDate,
       i.*,
       ( SELECT cUomName FROM Bd_Uom WHERE i.iInventoryUomId1 = iautoid ) AS InventorycUomName,
       ( SELECT cUomName FROM Bd_Uom WHERE i.iPurchaseUomId = iautoid ) AS PurchasecUomName,
       ( SELECT cContainerCode FROM Bd_Container WHERE i.iContainerClassId = iautoid ) AS cContainerCode,
       u.cUomClassName,
       t3.cInvCCode,
       t3.cInvCName,
       t4.cEquipmentModelName,
       t1.VenCode,
       vd.cVenName AS VenName
from
    T_Sys_BarcodeMaster t1
        LEFT JOIN T_Sys_BarcodeDetail t2 ON t2.MasID = t1.AutoID
        LEFT JOIN Bd_Vendor vd ON vd.cVenCode = t1.VenCode
        LEFT JOIN bd_inventory i ON i.cinvcode = t2.Invcode
        LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
        LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
        LEFT JOIN Bd_EquipmentModel t4 ON i.iEquipmentModelId = t4.iautoid
where 1=1
    #if(q)
		and (i.cinvcode like concat('%',#para(q),'%') or i.cinvcode1 like concat('%',#para(q),'%')
			or i.cinvname1 like concat('%',#para(q),'%') or i.cinvstd like concat('%',#para(q),'%')
			or u.cUomClassName like concat('%',#para(q),'%') or t2.Barcode like concat('%',#para(q),'%')
		)
	#end
	 AND t1.OrganizeCode = #(orgCode)
#end

#sql("pushU8List")
SELECT
    t1.AutoID,
    t1.Whcode,
    t1.OrganizeCode,
    t1.VenCode,
    vd.cVenName AS VenName,
    t1.BillDate,
    t2.Qty,
    t1.Type,
    t2.Barcode,
    t1.RdCode,
    rs.cRdName AS RdName,
    dt.cDepName,
    dt.cDepCode
FROM
    T_Sys_OtherOut t1
        LEFT JOIN T_Sys_OtherOutDetail t2 ON t2.MasID = t1.AutoID
        LEFT JOIN Bd_Vendor vd ON vd.cVenCode = t1.VenCode
        LEFT JOIN Bd_Rd_Style rs ON rs.cRdCode = t1.RdCode
        LEFT JOIN Bd_Department dt ON dt.iAutoId = t1.DeptCode
WHERE t1.AutoID IN (#(autoid))
    #end