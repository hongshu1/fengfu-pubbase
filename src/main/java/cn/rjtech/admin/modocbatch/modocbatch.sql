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

#sql("getModocDateDatas")
SELECT DISTINCT
	doc.iYear,
	doc.iMonth,
	doc.iDate,
	doc.iWorkShiftMid,
	shiftm.cWorkShiftCode,
	shiftm.cWorkShiftName,
	concat ( doc.iYear, '-', doc.iMonth, '-', doc.iDate ) dates
FROM
	Mo_MoDoc doc
	LEFT JOIN Bd_WorkShiftM shiftm ON doc.iWorkShiftMid= shiftm.iAutoId
WHERE
	iMoTaskId=#(taskid)
ORDER BY shiftm.cWorkShiftCode ASC
#end

#sql("getModocDatas")
SELECT DISTINCT
	modoc.iWorkRegionMid ,
	regionm.cWorkName ,
	modoc.iInventoryId,
	inventory.cInvCode ,
	inventory.cInvCode1 ,
	inventory.cInvName1
FROM
	Mo_MoDoc modoc
	LEFT JOIN Bd_WorkRegionM regionm ON modoc.iWorkRegionMid= regionm.iAutoId
	LEFT JOIN Bd_Inventory inventory ON modoc.iInventoryId= inventory.iAutoId
WHERE
	modoc.iMoTaskId=#(taskid)
#end

#sql("getModocid")
SELECT DISTINCT
	iAutoId
FROM
	Mo_MoDoc
WHERE
	iMoTaskId = #(taskid)
	AND iWorkRegionMid = #(iworkregionmid)
	AND iInventoryId = #(iinventoryid)
#end

#sql("getMoroutingconfigDatas")
SELECT DISTINCT
	moconfig.iAutoId,
	moconfig.iType
FROM
	Mo_MoRoutingConfig moconfig
WHERE
	moconfig.iMoRoutingId = ( SELECT iAutoId FROM Mo_MoRouting WHERE iMoDocId = #(modocid) )
#end

#sql("getMoequipmentDatas")
SELECT  DISTINCT
    moment.iEquipmentId,
	ment.cEquipmentName
FROM
	Mo_MoRoutingEquipment moment
	LEFT JOIN Bd_Equipment ment ON moment.iEquipmentId= ment.iAutoId
WHERE
	moment.iMoRoutingConfigId = #(moconfigid)
#end

#sql("getMooperationDatas")
SELECT  DISTINCT
    operation.iOperationId,
	bdop.cOperationName
FROM
	Mo_MoRoutingConfig_Operation operation
	LEFT JOIN Bd_Operation bdop ON operation.iOperationId= bdop.iAutoId
WHERE
	operation.iMoInventoryRoutingConfigId = #(moconfigid)
#end

#sql("getModocpersonnelDatas")
SELECT DISTINCT
	bdperson.cPsn_Num psnnum,
	bdperson.cPsn_Name psnname
FROM
	Mo_MoDoc doc
	LEFT JOIN Mo_MoRouting routing ON routing.iMoDocId= doc.iAutoId
	LEFT JOIN Mo_MoRoutingConfig config ON config.iMoRoutingId= routing.iAutoId
	LEFT JOIN Mo_MoRoutingConfig_Person moperson ON moperson.iMoRoutingConfigId= config.iAutoId
	LEFT JOIN Bd_Person bdperson ON moperson.iPersonId= bdperson.iAutoId
WHERE
    doc.iAutoId = #para(docid)
#end

#sql("getModocidBydates")
SELECT DISTINCT
    doc.iAutoId
FROM
	Mo_MoDoc doc
WHERE
	doc.iYear = #para(iyear)
	AND doc.iMonth = #para(imonth)
	AND doc.iDate = #para(idate)
	AND doc.iWorkShiftMid = #para(iworkshiftmid)
	AND doc.iMoTaskId= #para(taskid)
#end

#sql("getModocnoByid")
SELECT cMoDocNo FROM Mo_MoDoc WHERE iAutoId = #para(docid)
#end

#sql("getModociqtyByid")
SELECT iQty FROM Mo_MoDoc WHERE iAutoId = #para(docid)
#end

#sql("getModocpernumByid")
SELECT iPersonNum FROM Mo_MoDoc WHERE iAutoId = #para(docid)
#end

#sql("getModocpersonByid")
SELECT DISTINCT
	modoc.iDutyPersonId,
	person.cPsn_Name
FROM
	Mo_MoDoc modoc
	LEFT JOIN Bd_Person person ON modoc.iDutyPersonId= person.iAutoId
WHERE
	modoc.iAutoId= #para(docid)
#end