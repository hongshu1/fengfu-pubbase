#sql("paginateAdminDatas")
SELECT
        AuditState =
        CASE WHEN t1.State=1 THEN '已保存'
             WHEN t1.State=2 THEN '待审核'
             WHEN t1.State=3 THEN '已审核' END,
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
        AND t1.State = #para(iorderstatus)
    #end
#if(startdate)
    and CONVERT(VARCHAR(10),t1.ModifyDate,23) >='#(startdate)'
#end
#if(enddate)
    and CONVERT(VARCHAR(10),t1.ModifyDate,23) <='#(enddate)'
#end
order by t1.ModifyDate desc
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
       t3.cInvCName
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
       t3.cInvCName
FROM T_Sys_TransVouch t1,
     T_Sys_TransVouchDetail t2
         LEFT JOIN bd_inventory i ON i.cinvcode = t2.Invcode
         LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
         LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
WHERE
        t1.AutoID = t2.MasID AND  t1.AutoID = '#(autoid)'
    #end

#sql("pushU8List")


#end

#sql("pushU8List")


#end