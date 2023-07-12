#sql("paginateAdminDatas")
SELECT
        AuditState =
        CASE WHEN t1.iAuditStatus=0 THEN '未审核'
             WHEN t1.iAuditStatus=1 THEN '待审核'
             WHEN t1.iAuditStatus=2 THEN '审核通过'
             WHEN t1.iAuditStatus=3 THEN '审核不通过' END,
        TypeName =
        CASE WHEN t1.BillType='TransVouch' THEN '手动新增'END,
    t1.*,
    ( SELECT t5.cRdName FROM Bd_Rd_Style t5 WHERE t5.cRdCode = t1.IRdCode ) AS IRdName,
    ( SELECT t5.cRdName FROM Bd_Rd_Style t5 WHERE t5.cRdCode = t1.ORdCode ) AS ORdName
FROM
    T_Sys_TransVouch t1
WHERE 1 = 1

    #if(selectparam)
    AND (t1.BillNo LIKE CONCAT('%',#para(selectparam), '%')
    OR t1.IWhCode LIKE CONCAT('%', #para(selectparam), '%')
    OR t1.OWhCode LIKE CONCAT('%', #para(selectparam), '%')
    )
    #end
   #if(iorderstatus)
        AND t1.iAuditStatus = #para(iorderstatus)
    #end
#if(startdate)
    and CONVERT(VARCHAR(10),t1.dcreatetime,23) >='#(startdate)'
#end
#if(enddate)
    and CONVERT(VARCHAR(10),t1.dcreatetime,23) <='#(enddate)'
#end
order by t1.dcreatetime desc
    #end



#sql("workRegionMList")
SELECT
    t1.cWorkCode,
    t1.cWorkName
FROM  Bd_WorkRegionM t1
WHERE t1.cOrgCode = #para(orgCode)
    #if(cus)
  and ( t1.cWorkCode like CONCAT('%', #para(cus), '%') or t1.cWorkName like CONCAT('%', #para(cus), '%'))
    #end

    #if(null !=orderByColumn)
order by #(orderByColumn) #(orderByType)
    #end
    #end


    #sql("getBarcodeDatas")
select top #(limit)
       t1.Barcode,
        t1.SourceID as SourceBIllNoRow,
       t1.SourceBillType as SourceBillType,
       t1.BarcodeID as SourceBillID,
       t1.MasID as SourceBillDid,
       i.*,

       u.cUomClassName,
       t3.cInvCCode,
       t3.cInvCName,
       t1.IRdCode,
       t1.ORdCode
from
    V_Sys_BarcodeDetail t1
        LEFT JOIN bd_inventory i ON i.cinvcode = t1.Invcode
        LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
        LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
where 1=1
    #if(q)
		and (i.cinvcode like concat('%',#para(q),'%') or i.cinvcode1 like concat('%',#para(q),'%')
			or i.cinvname1 like concat('%',#para(q),'%') or i.cinvstd like concat('%',#para(q),'%')
			or u.cUomClassName like concat('%',#para(q),'%') or t1.Barcode like concat('%',#para(q),'%')
		)
	#end
	 AND t1.OrganizeCode = #(orgCode)
#end


#sql("getTransVouchLines")
SELECT t2.*,
       i.*,
       u.cUomClassName,
       t3.cInvCCode,
       t3.cInvCName,
       ( SELECT cUomName FROM Bd_Uom WHERE i.iPurchaseUomId = iautoid ) AS PurchasecUomName
FROM T_Sys_TransVouch t1,
     T_Sys_TransVouchDetail t2
         LEFT JOIN bd_inventory i ON i.cinvcode = t2.Invcode
         LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
         LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
WHERE
        t1.AutoID = t2.MasID AND  t1.AutoID = '#(autoid)'
    #end

#sql("warehouse")
SELECT
    t2.WhCode AS cwhcode,
    t7.cWhName AS cwhname
FROM
    T_Sys_BarcodeDetail t1
        LEFT JOIN T_Sys_StockBarcodePosition t2 ON t1.Barcode = t2.Barcode
        LEFT JOIN Bd_Warehouse t7 ON t2.WhCode = t7.cWhCode
WHERE
    1=1

#end


#sql("iwarehouse")
SELECT
    t7.cWhCode,
    t7.cWhName
FROM
    Bd_Warehouse t7
WHERE
        1=1
    #end

#sql("pushU8List")
SELECT
    t1.OrganizeCode,
    t1.IWhCode,
    (SELECT cWhName FROM Bd_Warehouse WHERE t1.IWhCode = cWhCode) AS iwhname,
    t2.IPosCode,
    t1.OWhCode,
    (SELECT cWhName FROM Bd_Warehouse WHERE t1.OWhCode = cWhCode) AS owhname,
    t2.OPosCode,
    t3.cInvName AS invname,
    t1.BillDate,
    t2.InvCode,
    t2.Qty,
    t2.Barcode,
    (SELECT cRdName FROM Bd_Rd_Style WHERE t1.ORdCode = cRdCode) AS ORdName,
    t1.ORdCode,
    (SELECT cDepName FROM Bd_Department WHERE t1.ODeptCode = cDepCode) AS ODeptName,
    t1.ODeptCode,
    (SELECT cRdName FROM Bd_Rd_Style WHERE t1.IRdCode = cRdCode) AS IRdName,
    t1.IRdCode
FROM
    T_Sys_TransVouch t1
        LEFT JOIN T_Sys_TransVouchDetail t2 ON t1.AutoID = t2.MasID
        LEFT JOIN Bd_Inventory t3 ON t3.cInvCode  = t2.InvCode
WHERE t1.AutoID = '#(autoid)'

#end