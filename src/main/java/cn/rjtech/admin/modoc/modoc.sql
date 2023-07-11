#sql("getPage")
SELECT
    md.*,
       bi.cInvAddCode,
       jdh.name as iStatusname,### 状态说明
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
     ###(getBaseDbName()).dbo.jb_dept AS bd
    Bd_Department bd ON md.iDepartmentId = bd.iAutoId


    LEFT JOIN (
    SELECT name , sn
    FROM #(getBaseDbName()).dbo.jb_dictionary
    WHERE type_key='modoc_state' AND enable = '1'
    ) jdh ON jdh.sn=md.iStatus
    LEFT JOIN
    dbo.Bd_WorkRegionM AS wr
    ON
    md.iWorkRegionMid = wr.iAutoId
    LEFT JOIN
    dbo.Bd_WorkShiftM AS ws
    ON
    md.iWorkShiftMid = ws.iAutoId
WHERE 1 = 1
and md.isDeleted=0
    #if(cMoDocNo)
  AND  md.cMoDocNo LIKE concat('%',#para(cMoDocNo),'%')
    #end

    ###if(iInventoryId)
    ####AND md.iInventoryId LIKE concat('%',#para(iInventoryId),'%')
    ###end

    #if(cInvName1)
  AND  bi.cInvName1 LIKE concat('%',#para(cInvName1),'%')
    #end

    #if(cInvCode1)
  AND  bi.cInvCode1 LIKE concat('%',#para(cInvCode1),'%')
    #end

    #if(cDepName)
  AND  bd.name LIKE concat('%',#para(cDepName),'%')
    #end
    #if(cDepCode)
  AND  bd.sn LIKE concat('%',#para(cDepCode),'%')
    #end
    #if(iDepartmentId)
  AND  md.iDepartmentId LIKE concat('%',#para(iDepartmentId),'%')
    #end
    #if(startdate)
  AND md.dPlanDate >=  #para(startdate)
    #end

    #if(enddate)
  AND md.dPlanDate <= #para(enddate)
    #end
    #if(istatus)
  AND md.istatus <= #para(istatus)
    #end
    #if(cinvaddcode)
  AND md.cinvaddcode = #para(cinvaddcode)
    #end
    #if(cinvcode)
  AND bi.cInvCode = #para(cinvcode)
    #end
    #if(iWorkRegionMid)
  AND md.iWorkRegionMid = #para(iWorkRegionMid)
    #end
order by md.dCreateTime desc, md.iAutoId  desc
    #end

    #sql("getJob")
SELECT cMoJobSn, iPlanQty, iRealQty, iStatus, dUpdateTime
FROM dbo.Mo_MoJob
WHERE iMoDocId = #para(id)
    #end

#sql("getMoworkshiftdByUserAnRegionid")
SELECT
    shiftd.*
FROM
    Mo_MoDoc doc
        LEFT JOIN Mo_MoTask task ON doc.iMoTaskId= task.iAutoId
        LEFT JOIN Mo_MoWorkShiftM shiftm ON shiftm.iMoTaskId= task.iAutoId
        LEFT JOIN Mo_MoWorkShiftD shiftd ON shiftd.iMoWorkShiftMid= shiftm.iAutoId
WHERE
        task.IsDeleted= 0
  AND task.iStatus = 4
  AND doc.iStatus IN ( 4, 5, 6, 7 )
  AND shiftd.iPersonId= ( SELECT iAutoId FROM Bd_Person WHERE cPsn_Num = #para(cpsnnum) )
  AND doc.iWorkRegionMid= #(iworkregionmid)
    #end

#sql("getMoroutingconfigpersonByUserAnRegionid")
SELECT
    person.*
FROM
    Mo_MoDoc moc
        LEFT JOIN Mo_MoRouting ting ON moc.iAutoId = ting.iMoDocId
        LEFT JOIN Mo_MoRoutingConfig config ON ting.iAutoId = config.iMoRoutingId
        LEFT JOIN Mo_MoRoutingConfig_Person person ON config.iAutoId = person.iMoRoutingConfigId
WHERE
        doc.iStatus IN ( 4, 5, 6, 7 )
  AND doc.iWorkRegionMid= #(iworkregionmid)
  AND person.iPersonId= ( SELECT iAutoId FROM Bd_Person WHERE cPsn_Num = #para(cpsnnum) )
    #end

#sql("getapicoperationnamebymodocid")
SELECT DISTINCT
    roufing.iAutoId,
    roufing.cOperationName
FROM
    Bd_InventoryRoutingConfig roufing
        LEFT JOIN Bd_InventoryRouting invrouting ON roufing.iInventoryRoutingId = invrouting.iAutoId
        LEFT JOIN Mo_MoRouting morouting ON morouting.iInventoryRoutingId = invrouting.iAutoId
WHERE
    roufing.cOperationName IS NOT NULL
  AND roufing.cOperationName <> ''
  AND morouting.iAutoId = #para(modocid)
    #end

#sql("getmoroutingsopbyinventoryroutingconfigid")
SELECT
    sop.cName,
    sop.cPath,
    sop.cSuffix,
    sop.iVersion,
    sop.dFromDate,
    sop.dToDate
FROM
    ###Bd_MoRoutingSop sop
    Mo_MoRoutingSop sop
WHERE
    sop.iInventoryRoutingConfigId=#para(inventoryroutingconfigid)
    #end

    #sql("findDocdetail")
SELECT
    a.*,
    b.name AS itypename,
    c.name AS cproductsnname,
    d.name AS cproducttechsnname,
    ri.invcs,
    rs.drawings,
    re.equipments,
    rp.persons
FROM
    Mo_MoRoutingConfig a
        LEFT JOIN UGCFF_MOM_System.dbo.jb_dictionary b ON a.iType = b.sn
        AND b.type_key = 'process_type'
        LEFT JOIN UGCFF_MOM_System.dbo.jb_dictionary c ON a.cProductSn = c.sn
        AND c.type_key = 'cproductsn_type'
        LEFT JOIN UGCFF_MOM_System.dbo.jb_dictionary d ON a.cProductTechSn = d.sn
        AND d.type_key = 'product_tech'
        LEFT JOIN Mo_MoRouting m ON a.iMoRoutingId= m.iAutoId
        LEFT JOIN ( SELECT COUNT ( iMoRoutingConfigId ) AS invcs, iMoRoutingConfigId AS configid FROM Mo_MoRoutingInvc GROUP BY iMoRoutingConfigId ) ri ON a.iAutoId = ri.configid
        LEFT JOIN ( SELECT COUNT ( iMoRoutingConfigId ) AS drawings, iMoRoutingConfigId AS configid FROM Mo_MoRoutingSop GROUP BY iMoRoutingConfigId ) rs ON a.iAutoId = rs.configid
        LEFT JOIN ( SELECT COUNT ( iMoRoutingConfigId ) AS equipments, iMoRoutingConfigId AS configid FROM Mo_MoRoutingEquipment GROUP BY iMoRoutingConfigId ) re ON a.iAutoId = re.configid
        LEFT JOIN ( SELECT COUNT ( iMoRoutingConfigId ) AS persons, iMoRoutingConfigId AS configid FROM Mo_MoRoutingConfig_Person GROUP BY iMoRoutingConfigId ) rp ON a.iAutoId = rp.configid
WHERE 1=1
    #if(iMoDocId)
 and  m.iMoDocId=#para(iMoDocId)
#end
#if(iInventoryRoutingId)
 and  m.iInventoryRoutingId=#para(iInventoryRoutingId)
#end
#end

    #sql("findMoJobByImodocId")
SELECT m.cMoJobSn,b.name as cMoJobSnName,  m.iPlanQty, m.iRealQty, m.iStatus,
       CASE m.iStatus
           WHEN '1' THEN '未完成'
           WHEN '2' THEN '已完成'
            ELSE  '' END as iStatusName,
       m.dUpdateTime
FROM dbo.Mo_MoJob m
         LEFT JOIN UGCFF_MOM_System.dbo.jb_dictionary b ON m.cMoJobSn = b.sn
    AND b.type_key = 'mojob_type'
WHERE 1=1
    #if(imodocdd)
      m.iMoDocId = #para(imodocdd)
    #end
#end

#sql("getInventoryList")
SELECT
	inv.iAutoId,
	cInvCode,
	cInvName,
	cInvCode1,
	cInvName1,
	wm.iAutoId iworkregionmid,
	wm.cWorkCode,
	wm.cWorkName,
	dt.iAutoId iDepartmentId,
    dt.cDepCode,
	dt.cDepName,
	rt.iAutoId iinventoryroutingid
FROM
	Bd_Inventory inv
	LEFT JOIN ( SELECT * FROM Bd_InventoryWorkRegion WHERE isDeleted = 0 AND isDefault = 1 ) wk ON wk.iInventoryId = inv.iAutoId
	LEFT JOIN BD_WORKREGIONM WM ON WM.IAUTOID = WK.IWORKREGIONMID
	LEFT JOIN (SELECT iAutoId,iInventoryId FROM Bd_InventoryRouting WHERE  getdate () >= dFromDate AND getdate ( ) <= dToDate  AND iAuditStatus=2) rt on rt.iInventoryId = inv.iAutoId
    LEFT JOIN Bd_Department dt on dt.iAutoId =wk.iDepId
WHERE
	inv.isDeleted = 0
	AND inv.isEnabled = 1
        #if(q)
            AND (inv.cInvCode LIKE CONCAT('%', #para(q), '%') OR inv.cInvCode1 LIKE CONCAT('%', #para(q), '%') OR inv.cInvName1 LIKE CONCAT('%', #para(q), '%'))
	    #end
	    #if(itemId)
	        AND inv.iautoId = #para(itemId)
	    #end
	    #if(iEquipmentModelId)
	        AND INV.iEquipmentModelId = #para(iEquipmentModelId)
	    #end
	    #if(cInvCode)
             AND inv.cInvCode LIKE CONCAT('%', #para(cInvCode), '%')
	    #end
	    #if(cInvCode1)
             AND inv.cInvCode LIKE CONCAT('%', #para(cInvCode1), '%')
	    #end
	    #if(cInvName)
             AND invG.cInvName LIKE CONCAT('%', #para(cInvName), '%')
	    #end
	    #if(cVenName)
	        AND ven.cVenName LIKE CONCAT('%', #para(cVenName), '%')
	    #end
#end

#sql("getEquipments")
select * from Bd_InventoryRoutingEquipment  where iInventoryRoutingConfigId=#para(iEquipmentIds)
#end

#sql("inventoryAutocompleteDatas")
SELECT
    top #(limit)
	inv.iAutoId,
	cInvCode,
	cInvName,
	cInvCode1,
	cInvName1,
	wm.iAutoId iworkregionmid,
	wm.cWorkCode,
	wm.cWorkName,
	dt.iAutoId iDepartmentId,
    dt.cDepCode,
	dt.cDepName,
	rt.iAutoId iinventoryroutingid
FROM
	Bd_Inventory inv
	LEFT JOIN ( SELECT * FROM Bd_InventoryWorkRegion WHERE isDeleted = 0 AND isDefault = 1 ) wk ON wk.iInventoryId = inv.iAutoId
	left JOIN Bd_WorkRegionM wm ON wm.iAutoId = wk.iWorkRegionMid
	left JOIN (SELECT iAutoId,iInventoryId FROM Bd_InventoryRouting WHERE  getdate () >= dFromDate AND getdate ( ) <= dToDate  AND iAuditStatus=2) rt on rt.iInventoryId = inv.iAutoId
    LEFT JOIN Bd_Department dt on dt.iAutoId =wk.iDepId
WHERE
    1=1
	AND inv.isDeleted = 0
	AND inv.isEnabled = 1
	#if(q)
		and (
			cInvCode like concat('%',#para(q),'%') or cInvName like concat('%',#para(q),'%')
		)
	#end
#end

#sql("getMoDocEquipments")
	select * from Mo_MoRoutingEquipment where iMoRoutingConfigId=#para(iEquipmentIds)
#end


#sql("getPersonByEquipment")
SELECT * FROM Bd_Person  per
WHERE iAutoId IN (SELECT iPersonId FROM Bd_PersonEquipment EQ  WHERE EQ.iEquipmentId IN (#(iEquipmentIds)) AND isDeleted=0)
#end


#sql("getmoDocupdata")
SELECT  MO.*,
    inv.iAutoId iinventoryid,
	cInvCode,
	cInvName,
	cInvCode1,
	cInvName1,
	wm.iAutoId iworkregionmid,
	wm.cWorkCode,
	wm.cWorkName,
	dt.iAutoId iDepartmentId,
    dt.cDepCode,
	dt.cDepName,
	rt.iAutoId iinventoryroutingid FROM Mo_MoDoc MO INNER JOIN Bd_Inventory  INV
	ON MO.iInventoryId=INV.iAutoId
	LEFT JOIN ( SELECT * FROM Bd_InventoryWorkRegion WHERE isDeleted = 0 AND isDefault = 1 ) wk ON wk.iInventoryId = inv.iAutoId
	left    JOIN Bd_WorkRegionM wm ON wm.iAutoId = wk.iWorkRegionMid
	left   JOIN (SELECT iAutoId,iInventoryId FROM Bd_InventoryRouting WHERE  getdate () >= dFromDate AND getdate ( ) <= dToDate  AND iAuditStatus=2) rt on rt.iInventoryId = inv.iAutoId
    LEFT JOIN Bd_Department dt on dt.iAutoId =wk.iDepId
	WHERE MO.iAutoId = #para(iautoid) AND 	inv.isDeleted = 0
	AND inv.isEnabled = 1
#end

#sql("getMoDocby")
SELECT * FROM Mo_MoDoc DOC
INNER JOIN Mo_MoRouting ROU ON DOC.iAutoId=ROU.iMoDocId
INNER JOIN Mo_MoRoutingConfig FIG ON FIG.iMoRoutingId=ROU.iAutoId
INNER JOIN Mo_MoRoutingEquipment ME ON ME.iMoRoutingConfigId=FIG.iAutoId
WHERE DOC.iAutoId=#para(iautoid)
#end


#sql("getMoDocbyIinventoryRoutingId")
SELECT
    mf.*,
    rs.equipments,
    invs.invcs,
    persons.configperson,
    persons2.configpersonids
FROM
    Mo_MoRouting mr
    INNER JOIN Mo_MoRoutingConfig mf ON mr.iAutoId = mf.iMoRoutingId
    LEFT JOIN (SELECT COUNT(iMoRoutingConfigId) equipments, iMoRoutingConfigId FROM Mo_MoRoutingEquipment GROUP BY iMoRoutingConfigId) rs ON rs.iMoRoutingConfigId = mf.iAutoId
    LEFT JOIN (SELECT COUNT(iMoRoutingConfigId) invcs, iMoRoutingConfigId FROM Mo_MoRoutingInvc GROUP BY iMoRoutingConfigId) invs ON invs.iMoRoutingConfigId = mf.iAutoId
    LEFT JOIN (
        SELECT mp1.iMoRoutingConfigId,
            STUFF((SELECT '/' + per.cPsn_Name
                   FROM (
                        SELECT iMoRoutingConfigId, CAST(iPersonId AS BIGINT) AS iPersonId
                        FROM Mo_MoRoutingConfig_Person
                        WHERE ISNUMERIC(iPersonId) = 1
                   ) mp2
                   INNER JOIN Bd_Person per ON mp2.iPersonId = per.iAutoId
                   WHERE mp2.iMoRoutingConfigId = mp1.iMoRoutingConfigId
                   FOR XML PATH('')), 1, 1, '') AS configperson
        FROM Mo_MoRoutingConfig_Person mp1
        GROUP BY mp1.iMoRoutingConfigId
    ) persons ON persons.iMoRoutingConfigId = mf.iAutoId
    LEFT JOIN (
        SELECT mp1.iMoRoutingConfigId,
            STUFF((SELECT ',' + CAST(mp2.iPersonId AS VARCHAR(MAX))
                   FROM (
                        SELECT iMoRoutingConfigId, CAST(iPersonId AS BIGINT) AS iPersonId
                        FROM Mo_MoRoutingConfig_Person
                        WHERE ISNUMERIC(iPersonId) = 1
                   ) mp2
                   INNER JOIN Bd_Person per ON mp2.iPersonId = per.iAutoId
                   WHERE mp2.iMoRoutingConfigId = mp1.iMoRoutingConfigId
                   FOR XML PATH('')), 1, 1, '') AS configpersonids
        FROM Mo_MoRoutingConfig_Person mp1
        GROUP BY mp1.iMoRoutingConfigId
    ) persons2 ON persons2.iMoRoutingConfigId = mf.iAutoId
WHERE
	iMoDocId = #para(iMoDocId)
#end


#sql("getMoDocinv")
   SELECT *
FROM Mo_MoRoutingInvc mi
INNER JOIN  Bd_Inventory inv on mi.iInventoryId=inv.iAutoId
where iMoRoutingConfigId=#para(iautoid)
#end

#sql("getMoDocEquipment")
SELECT * FROM Mo_MoRoutingEquipment ME
INNER JOIN Bd_Equipment EQ ON ME.iEquipmentId=EQ.iAutoId
where iMoRoutingConfigId=#para(iautoid)
#end

#sql("moDocInventoryRouting")
SELECT * FROM Mo_MoRoutingEquipment ME
INNER JOIN Bd_Equipment EQ ON ME.iEquipmentId=EQ.iAutoId
where iMoRoutingConfigId=#para(iautoid)
#end


#sql("getPersonsByIds")
select * from Bd_Person per where iautoid  IN (#(configpersonids))
#end

#sql("getPsnNameById")
SELECT *
FROM Bd_Person
WHERE iAutoId = (
    SELECT TOP 1 iPersonId
    FROM Mo_MoRouting mr
             LEFT JOIN Mo_MoRoutingConfig mrc ON mr.iAutoId = mrc.iMoRoutingId
             LEFT JOIN Mo_MoRoutingConfig_Person mrcp ON mrc.iAutoId = mrcp.iMoRoutingConfigId
    WHERE mr.iMoDocId = #para(iautoid)
    ORDER BY mrc.iSeq DESC
)
#end

#sql("getConfigPersonById")
    select * from Mo_MoRoutingConfig_Person where iMoRoutingConfigId = #para(id)
#end
#sql("findByModecIdProcessDatas")
select a.iMoDocId as modocid, a.iinventoryroutingid, a.iinventoryid, b.iAutoId as RoutingConfigid, b.cOperationName

from Mo_MoRouting a
         join Mo_MoRoutingConfig b on b.iMoRoutingId = a.iAutoId
    where
          1=1
        #if(imodocid)
        and a.iMoDocId=#para(imodocid)
        #end
#end

#sql("getModocByid")
SELECT cMoDocNo,MO.dCreateTime,WH.cWhCode,DT.cDepCode,INV.cInvCode,INV.cInvName,INV.cInvStd,
'' iquantity,MO.dPlanDate DStartDate,MO.dPlanDate DDueDate,10 irowno,''cBatch,iAuditStatus,iAuditWay
FROM Mo_MoDoc MO
LEFT JOIN Bd_WorkRegionM WM ON MO.iWorkRegionMid=WM.iAutoId
LEFT JOIN Bd_Department DT ON DT.iAutoId=MO.iDepartmentId
LEFT JOIN Bd_Warehouse WH ON WM.iWarehouseId=WH.iAutoId
LEFT JOIN Bd_Inventory INV  on INV.iAutoId=MO.iInventoryId
where MO.iAutoId = #para(iautoid)
#end