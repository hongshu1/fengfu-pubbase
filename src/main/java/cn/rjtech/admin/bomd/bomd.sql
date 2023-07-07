#sql("getBomComparePageData")
SELECT
    d.iAutoId,
    d.iInventoryId,
	d.iBomMid,
	d.iPid,
	d.cInvCode,
	d.cInvName,
	d.cInvStd,
	d.cCode,
	d.iCodeLevel,
	d.cVersion,
	d.iInventoryUomId1,
	d.dEnableDate,
	d.dDisableDate,
	d.iPartType,
	d.iBaseQty,
	d.iWeight,
	d.cVenName,
	d.isVirtual,
	d.bProxyForeign,
	d.cMemo,
	d.iInvPartBomMid,
	uom.cUomName AS cuomname
FROM
	Bd_BomD d
	LEFT JOIN Bd_Uom uom ON uom.iAutoId = d.iInventoryUomId1
WHERE
	d.isDeleted = '0'
	#if(iBomMid)
	    AND d.iPid = #para(iBomMid)
	#end
	#if(invCode)
        AND d.cInvCode LIKE CONCAT('%',#para(invCode),'%')
	#end
	#if(invName)
        AND d.cInvName LIKE CONCAT('%',#para(invName),'%')
	#end
	ORDER BY d.iAutoId ASC
#end

#sql("getEffectiveBomCompare")
SELECT
	d.*
FROM
	Bd_BomD d
	INNER JOIN (
        SELECT
        a.iAutoId,
        a.iInventoryId
    FROM
        Bd_BomM a
        INNER JOIN ( SELECT iInventoryId, MAX ( cVersion ) cversion FROM Bd_BomM b WHERE b.isDeleted = '0' AND b.iOrgId = #para(orgId) GROUP BY b.iInventoryId ) v ON a.iInventoryId = v.iInventoryId
        AND a.cVersion = v.cversion
    WHERE
        a.isDeleted = '0'
        AND a.iOrgId = #para(orgId)
	) v ON v.iAutoId = d.iPid
WHERE
	isDeleted = '0'
    #if(invIds)
        AND v.iInventoryId IN (
            #for(id:invIds)
                '#(id)' #(for.last?'':',')
            #end
        )
    #end
#end

#sql("getParentBomList")
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
	#if(orgId)
	AND master.iOrgId = #para(orgId)
	#end
#end
