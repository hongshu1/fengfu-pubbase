#sql("getPage")
SELECT md.iAutoId,
       md.iMoTaskId,
       md.iInventoryId,
       md.cMoDocNo,
       md.dPlanDate,
       md.iDepartmentId,
       md.iQty,
       md.iCompQty,
       md.iPersonNum,
       md.iDutyPersonId,
       md.iStatus,
       bi.cInvCode, ### 存货编码
       bi.cInvCode1, ### 客户部番
       bi.cInvName1, ### 部品名称
       bd.cDepName, ### 部门
       wr.cWorkName,  ### 产线名称
       ws.cWorkShiftName ### 班次名称
FROM dbo.Mo_MoDoc AS md
         LEFT JOIN
     dbo.Bd_Inventory AS bi
     ON
         md.iInventoryId = bi.iAutoId
         LEFT JOIN
     dbo.Bd_Department AS bd
     ON
         md.iDepartmentId = bd.iAutoId
         LEFT JOIN
     dbo.Bd_WorkRegionM AS wr
     ON
         md.iWorkRegionMid = wr.iAutoId
         LEFT JOIN
     dbo.Bd_WorkShiftM AS ws
     ON
             md.iWorkShiftMid = ws.iAutoId
WHERE 1 = 1
    #if(cMoDocNo)
      AND  md.cMoDocNo LIKE concat('%',#para(cMoDocNo),'%')
     #end

     #if(iInventoryId)
      AND md.iInventoryId LIKE concat('%',#para(iInventoryId),'%')
     #end

    #if(cInvName1)
      AND  bi.cInvName1 LIKE concat('%',#para(cInvName1),'%')
    #end

    #if(cInvCode1)
      AND  bi.cInvCode1 LIKE concat('%',#para(cInvCode1),'%')
     #end

    #if(cDepName)
      AND  bd.cDepName LIKE concat('%',#para(cDepName),'%')
    #end

    #if(startdate)
       AND md.dPlanDate >=  #para(startdate)
    #end

     #if(enddate)
       AND md.dPlanDate <= #para(enddate)
    #end

 #end
