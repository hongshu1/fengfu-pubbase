#sql("getPage")
SELECT mt.iAutoId,
       mt.cMoPlanNo,
       mt.dBeginDate,
       mt.dEndDate,
       mt.iStatus,
       mt.cCreateName,
       mt.dCreateTime,
       dp.cDepName
FROM dbo.Mo_MoTask AS mt
         LEFT JOIN
     dbo.Bd_Department AS dp
     ON
         mt.iDepartmentId = dp.iAutoId
WHERE mt.IsDeleted = '0' #if(startdate)
       AND mt.dBeginDate >=  #para(startdate)
    #end

     #if(enddate)
       AND mt.dEndDate <= #para(enddate)
    #end
    #if(cmoplanno)
      AND mt.cMoPlanNo LIKE concat('%',#para(cmoplanno),'%')
    #end
    #if(istatus)
      AND  mt.istatus = #para(istatus)
    #end
    #if(cdepname)
      AND  dp.cDepName LIKE concat('%',#para(cdepname),'%')
    #end
#end


#sql("getPersonPage")
SELECT
       md.iInventoryId,
       md.iWorkShiftMid,
       md.iDepartmentId,
       bi.cInvName1,
       bi.cInvCode1,
       wr.cWorkName
FROM dbo.Mo_MoDoc AS md
         LEFT JOIN
     dbo.Mo_MoTask AS mt
     ON
         mt.iAutoId = md.iMoTaskId
         LEFT JOIN
     dbo.Bd_Inventory AS bi
     ON
         md.iInventoryId = bi.iAutoId
         LEFT JOIN
     dbo.Bd_WorkRegionM AS wr
     ON
         md.iWorkRegionMid = wr.iAutoId

WHERE mt.iAutoId = #para(iAutoId)
group by  md.iInventoryId,
                 md.iWorkShiftMid,
                 md.iDepartmentId,
                 bi.cInvName1,
                 bi.cInvCode1,
                 wr.cWorkName
#end

#sql("getDocList")
SELECT
    doc.cMoDocNo,
    doc.dPlanDate,
    doc.iAutoId,
       doc.iQty
FROM
    dbo.Mo_MoDoc AS doc
WHERE
        doc.iMoTaskId = #para(iMoTaskId)  AND
        doc.iInventoryId = #para(iInventoryId)
ORDER BY
    dPlanDate ASC
#end


#sql("findRoutingConfig")
SELECT
    mrc.cOperationName,
    mrc.cMergedSeq,
    mrc.iMergedNum,
    mrc.iType,
    mrc.iSeq,
    be.cEquipmentName,
    mre.iEquipmentId,
    mrc.iAutoId
FROM
    dbo.Mo_MoRouting AS mr
        LEFT JOIN
    dbo.Mo_MoRoutingConfig AS mrc
    ON
            mrc.iMoRoutingId = mr.iAutoId
        LEFT JOIN
    dbo.Mo_MoRoutingEquipment AS mre
    ON
            mrc.iAutoId = mre.iMoRoutingConfigId
        LEFT JOIN
    dbo.Bd_Equipment AS be
    ON
            mre.iEquipmentId = be.iAutoId

WHERE mr.iMoDocId = #para(docId) order by  mrc.iSeq
#end



