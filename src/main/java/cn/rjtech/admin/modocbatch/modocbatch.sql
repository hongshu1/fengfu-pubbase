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

#sql("editplanviewdatas")
SELECT DISTINCT
	doc.iYear,
	doc.iMonth,
	doc.iDate,
	wm.cWorkShiftName,
	wm.cWorkShiftCode,
	concat ( doc.iYear, '-', doc.iMonth, '-', doc.iDate ) YearToDate
FROM
	Mo_MoDoc doc
	LEFT JOIN Bd_WorkShiftM wm ON wm.iAutoId= doc.iWorkShiftMid
	WHERE doc.iMoTaskId=#(id)
ORDER BY
	YearToDate ASC
#end

#sql("getPersonPage")
SELECT
	md.iInventoryId,
	md.iWorkShiftMid,
	md.iDepartmentId,
	bi.cInvName1,
	bi.cInvCode1,
	wr.cWorkName
FROM
	dbo.Mo_MoDoc AS md
	LEFT JOIN dbo.Mo_MoTask AS mt ON mt.iAutoId = md.iMoTaskId
	LEFT JOIN dbo.Bd_Inventory AS bi ON md.iInventoryId = bi.iAutoId
	LEFT JOIN dbo.Bd_WorkRegionM AS wr ON md.iWorkRegionMid = wr.iAutoId
WHERE
	mt.iAutoId = #para(iAutoId)
GROUP BY
	md.iInventoryId,
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
	doc.iMoTaskId = #para(iMoTaskId)
	AND doc.iInventoryId = #para(iInventoryId)
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

#sql("getModocDateShiftDatas")
SELECT DISTINCT
    doc.iAutoId,
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
	doc.iMoTaskId=#(taskid) and doc.iInventoryId=#(iinventoryid) and doc.iWorkRegionMid=#(iworkregionmid)
ORDER BY dates ASC
#end

#sql("getModocAllDatasByTaskid")
SELECT * FROM Mo_MoDoc WHERE iMoTaskId = #(taskid)
#end

#sql("getModocConfigtypeByModocid")
SELECT DISTINCT
	moconfig.iAutoId,
	moconfig.iType,
	routing.iMoDocId
FROM
	Mo_MoRoutingConfig moconfig
	LEFT JOIN Mo_MoRouting AS routing ON moconfig.iMoRoutingId = routing.iAutoId
WHERE
	moconfig.iMoRoutingId IN ( SELECT iAutoId FROM Mo_MoRouting WHERE iMoDocId IN (#(imodocid)) )
#end

#sql("getEquipmentnameByConfigid")
SELECT DISTINCT
	moment.iEquipmentId,
	ment.cEquipmentName,
	moment.iMoRoutingConfigId
FROM
	Mo_MoRoutingEquipment moment
	LEFT JOIN Bd_Equipment ment ON moment.iEquipmentId= ment.iAutoId
WHERE
	moment.iMoRoutingConfigId IN (#(configid))
#end

#sql("getOperationnameByConfigid")
SELECT DISTINCT
	operation.iOperationId,
	bdop.cOperationName,
	operation.iMoRoutingConfigId
FROM
	Mo_MoRoutingConfig_Operation operation
	LEFT JOIN Bd_Operation bdop ON operation.iOperationId= bdop.iAutoId
WHERE
	operation.iMoRoutingConfigId IN (#(configid))
#end

#sql("getModocPersonnameByDocid")
SELECT DISTINCT
	doc.iAutoId,
	doc.iYear,
	doc.iMonth,
	doc.iDate,
	doc.iWorkShiftMid,
	bdperson.cPsn_Num psnnum,
	bdperson.cPsn_Name psnname,
	concat ( doc.iYear, doc.iMonth, doc.iDate, doc.iWorkShiftMid ) dateSplicing
FROM
	Mo_MoDoc doc
	LEFT JOIN Mo_MoRouting routing ON routing.iMoDocId= doc.iAutoId
	LEFT JOIN Mo_MoRoutingConfig config ON config.iMoRoutingId= routing.iAutoId
	LEFT JOIN Mo_MoRoutingConfig_Person moperson ON moperson.iMoRoutingConfigId= config.iAutoId
	LEFT JOIN Bd_Person bdperson ON moperson.iPersonId= bdperson.iAutoId
WHERE
	bdperson.cPsn_Num IS NOT NULL
	AND bdperson.cPsn_Name IS NOT NULL
	AND doc.iAutoId IN (#(docid))
#end

#sql("getModocDutyPersonnameByDocid")
SELECT DISTINCT
	doc.iAutoId,
	doc.iDutyPersonId,
	person.cPsn_Name
FROM
	Mo_MoDoc doc
	LEFT JOIN Bd_Person person ON doc.iDutyPersonId= person.iAutoId
WHERE
	doc.iMoTaskId=#(taskid) and doc.iInventoryId=#(iinventoryid) and doc.iWorkRegionMid=#(iworkregionmid)
#end

#sql("getModocNoQtyNumByDocid")
SELECT
	iAutoId,
	cMoDocNo,
	iQty,
	iPersonNum,
	iYear,
	iMonth,
	iDate,
	iWorkShiftMid,
	concat ( iYear, iMonth, iDate, iWorkShiftMid ) dateSplicing
FROM
	Mo_MoDoc
WHERE
	iMoTaskId = #(taskid)
#end


















