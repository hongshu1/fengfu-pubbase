
#sql("paginateAdminDatas")
SELECT
        AuditState =
        CASE
             WHEN t1.Status=5 THEN '已出库'
             WHEN t1.Status=3 THEN '已审批 /未完成' END,
      t1.*,
      t4.cDepName
FROM
    T_Sys_OtherOut t1
    LEFT JOIN Bd_Department t4 ON t4.iAutoId = t1.DeptCode
WHERE 1 = 1
  AND  EXISTS (
        SELECT
            1
        FROM
            T_Sys_OtherOut
        WHERE
                    t1.Type = 'OtherOutMES'
                AND t1.Status = 3 OR t1.Status = 5)
    #if(billno)
    AND  t1.BillNo LIKE  '%#(billno)%'
    #end
    #if(sourcebilltype)
    AND  t1.SourceBillType LIKE '%#(sourcebilltype)%'
    #end
     #if(deptname)
    AND  t4.cDepName LIKE '%#(deptname)%'
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
order by t1.ModifyDate desc
    #end

#sql("pushu")
SELECT
    t1.*
FROM
    T_Sys_OtherOut t1
WHERE 1=1
#if(ids)
    AND t1.AutoID in (#para(ids))
#end
#end


#sql("pushudetail")
SELECT
    t1.*
FROM
    T_Sys_OtherOutDetail t1
WHERE 1=1
#if(id)
    AND t1.MasID = #para(id)
#end
#end

#sql("getOtherOutLinesReturnLines")
SELECT
    i.*,
    #if(Detailautoid)
    t2.AutoID,
    #end
    u.cUomClassName,
    t3.cInvCCode,
    t3.cInvCName,
    ( SELECT sum(case when Qty >0 then Qty else 0 end ) FROM T_Sys_OtherOutDetail WHERE  MasID = '#(autoid)' ) AS qtys,
    ( CASE WHEN t2.Qty > 0 THEN 0 ELSE t2.Qty END ) AS returnqty,
    t2.Barcode,
    t2.InvCode
FROM T_Sys_OtherOut t1,
     T_Sys_OtherOutDetail t2
         LEFT JOIN bd_inventory i ON i.cinvcode = t2.Invcode
         LEFT JOIN Bd_UomClass u ON i.iUomClassId = u.iautoid
         LEFT JOIN Bd_InventoryClass t3 ON i.iInventoryClassId = t3.iautoid
WHERE
        t1.AutoID = t2.MasID AND  t1.AutoID = '#(autoid)'
    #if(Detailautoid == null)
  AND t2.Qty > 0
   #end
    #if(Detailautoid)
    AND t2.AutoID = #para(Detailautoid)
    #end

    #end

#sql("treturnQty")
SELECT
    *
FROM
    T_Sys_OtherOutDetail
WHERE
        MasID = '#(autoid)'
#end