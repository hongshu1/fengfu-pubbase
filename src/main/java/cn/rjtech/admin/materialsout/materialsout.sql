#sql("paginateAdminDatas")
SELECT
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
        AND t1.State = #para(iorderstatus)
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