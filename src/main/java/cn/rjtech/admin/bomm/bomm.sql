#sql("datas")
SELECT
	master.iAutoId,
	NULL AS iPid,
	master.iInventoryId,
	master.cInvCode,
	master.cInvName,
	NULL AS iBomMid,
	NULL AS iInvPartBomMid
FROM
	Bd_BomM master
WHERE
	master.IsDeleted = '0'
	#if(isView)
	    AND master.isView = #para(isView)
	#end
    #if(orgId)
	AND master.iOrgId = #para(orgId)
	#end
UNION ALL
SELECT
	compare.iAutoId,
	compare.iPid,
	compare.iInventoryId,
	compare.cInvCode,
	compare.cInvName,
	compare.iBomMid,
	compare.iInvPartBomMid
FROM
	Bd_BomD compare
WHERE
	compare.isDeleted = '0'
	#if(orgId)
	AND compare.iOrgId = #para(orgId)
	#end
#end

#sql("findMasterDatas")
SELECT
	master.iAutoId,
	NULL AS iPid,
	master.iInventoryId,
	master.cInvCode,
	master.cInvName,
	NULL AS iBomMid,
	NULL AS iInvPartBomMid
FROM
	Bd_BomM master
WHERE
	master.IsDeleted = '0'
	AND master.isView = '1'
    #if(orgId)
	    AND master.iOrgId = #para(orgId)
	#end
#end

#sql("getVersionRecord")
SELECT
	master.iAutoId,
	master.cVersion,
	master.iType,
	master.isView,
	master.dEnableDate,
	master.dDisableDate,
	master.iAuditStatus,
	master.cCreateName,
	master.dCreateTime,
	master.cInvCode,
	master.cInvName,
	minv.cInvStd,
	minv.cInvCode1,
	minv.cInvAddCode1,
	minv.cInvName1,
	minv.cInvName2,
	uom.cUomName
FROM
	Bd_BomM master
	INNER JOIN Bd_Inventory minv ON minv.iAutoId = master.iInventoryId
	LEFT JOIN Bd_Uom uom ON uom.iAutoId = minv.iUomClassId
WHERE
	master.IsDeleted = '0'
	#if(orgId)
	AND  master.iOrgId = #para(orgId)
	#end
	#if(cInvCode)
	    AND master.cInvCode LIKE CONCAT('%',#para(cInvCode),'%')
	#end
	#if(cInvCode1)
	    AND minv.cInvCode1 LIKE CONCAT('%',#para(cInvCode1),'%')
	#end
	#if(cInvName1)
	    AND minv.cInvName1 LIKE CONCAT('%',#para(cInvName1),'%')
	#end
	ORDER BY master.dCreateTime DESC
#end

#sql("findByVersion")
SELECT
	m.IAUTOID,
	m.iInventoryId,
	m.cVersion
FROM
	Bd_BomM m
    INNER JOIN ( SELECT iInventoryId, MAX ( cVersion ) cVersion FROM Bd_BomM WHERE isDeleted = '0' GROUP BY iInventoryId ) m1 ON m1.iInventoryId = m.iInventoryId
	AND m.cVersion = m1.cVersion
WHERE m.isDeleted = '0'
	#if(orgId)
	    AND m.iOrgId = #para(orgId)
	#end
	#if(invId)
        AND m.iInventoryId = #para(invId)
	#end
#end

#sql("findByInvId")
SELECT
     master.cVersion,
	master.dEnableDate,
	master.dDisableDate
FROM
	Bd_BomM master
WHERE
    master.IsDeleted = '0'
    AND  master.iInventoryId = #para(iInventoryId)
    #if(orgId)
	    AND  master.iOrgId = #para(orgId)
    #end
    #if(iAutoId)
     AND  master.iAutoId <> #para(iAutoId)
    #end
#end

#sql("findMaxVersionByInvId")
SELECT
    MAX(master.cVersion) AS cVersion
FROM
    Bd_BomM master
WHERE
    master.IsDeleted = '0'
    AND master.iInventoryId = #para(iInventoryId)
    #if(orgId)
	    AND  master.iOrgId = #para(orgId)
    #end
#end

#sql("findVersionByInvId")
SELECT
    master.cVersion
FROM
    Bd_BomM master
WHERE
    master.IsDeleted = '0'
    AND master.iInventoryId = #para(iInventoryId)
    #if(orgId)
	    AND  master.iOrgId = #para(orgId)
    #end
    #if(iAutoId)
     AND  master.iAutoId <> #para(iAutoId)
    #end
#end
