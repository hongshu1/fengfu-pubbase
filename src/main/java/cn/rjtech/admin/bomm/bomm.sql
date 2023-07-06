#sql("datas")
SELECT
	master.iAutoId,
	NULL AS iPid,
	master.iInventoryId,
	master.cInvCode,
	master.cInvName
FROM
	Bd_BomM master
WHERE
	master.IsDeleted = '0'
    AND CONVERT(DATE, master.dEnableDate) <= CONVERT(DATE, GETDATE())
	AND CONVERT(DATE, master.dDisableDate) >= CONVERT(DATE, GETDATE())
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
	compare.cInvName
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
    *
FROM
(
    SELECT
        master.iAutoId,
        CASE
        WHEN CONVERT(DATE, master.dEnableDate) <= CONVERT(DATE, GETDATE())
            AND CONVERT(DATE, master.dDisableDate) >= CONVERT(DATE, GETDATE()) THEN 1
            ELSE 0
        END AS isEffective,
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
        ) A
WHERE 1 = 1

	#if(cInvCode)
	    AND A.cInvCode LIKE CONCAT('%',#para(cInvCode),'%')
	#end
	#if(cInvCode1)
	    AND A.cInvCode1 LIKE CONCAT('%',#para(cInvCode1),'%')
	#end
	#if(cInvName1)
	    AND A.cInvName1 LIKE CONCAT('%',#para(cInvName1),'%')
	#end
    #if(isEffective)
        AND A.isEffective = #para(isEffective59)
	#end
	#if(sonInvCode)
	    AND EXISTS (
	        SELECT 1 FROM Bd_BomD c WHERE c.iPid = a.iAutoId AND c.cInvCode LIKE CONCAT('%',#para(sonInvCode),'%') AND isDeleted = '0'
	    )
	#end
	ORDER BY a.dCreateTime DESC
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

#sql("getEffectiveBomM")
SELECT
	*
FROM
	Bd_BomM
WHERE
	isDeleted = '0'
	AND iOrgId = #para(orgId)
	AND dEnableDate  <= CONVERT(DATE,  GETDATE())
	AND dDisableDate >= CONVERT(DATE,  GETDATE())
    #if(invIds)
        AND iInventoryId NOT IN (
            #for(id:invIds)
                '#(id)' #(for.last?'':',')
            #end
        )
    #end
#end

#sql("getFileRecord")
SELECT
	minv.cInvCode,
	minv.cInvName,
	minv.cInvStd,
	minv.cInvCode1,
	minv.cInvAddCode1,
	minv.cInvName1,
	minv.cInvName2,
	m.dCreateTime,
	m.cCreateName,
	trl.*
FROM
	Bd_BomM_Trl trl
	INNER JOIN Bd_BomM m ON m.iAutoId = trl.iBomMid
	INNER JOIN Bd_Inventory minv ON minv.iAutoId = m.iInventoryId
WHERE
	1 = 1
	#if(orgId)
	AND  m.iOrgId = #para(orgId)
	#end
	#if(cInvCode)
	    AND minv.cInvCode LIKE CONCAT('%',#para(cInvCode),'%')
	#end
	#if(cInvCode1)
	    AND minv.cInvCode1 LIKE CONCAT('%',#para(cInvCode1),'%')
	#end
	#if(cInvName1)
	    AND minv.cInvName1 LIKE CONCAT('%',#para(cInvName1),'%')
	#end
	ORDER BY m.dCreateTime DESC
#end
