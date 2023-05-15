#sql("findRoutingAll")
SELECT
	inv.cInvName,
	inv.cInvCode,
	inv.cInvCode1,
	inv.cInvAddCode,
	inv.cInvAddCode1,
	inv.cInvName1,
	inv.cInvName2,
	a.*
FROM
	(
	SELECT
		routingc.iRsInventoryId invId,
		routingc.iAutoId,
		NULL AS iPid,
		routingc.iSeq,
		routingc.cMergedSeq,
		routingc.cOperationName,
		routingc.iType,
		routingc.cProductSn,
		routingc.cProductTechSn,
		routingc.iMergedNum,
		routingc.iMergeRate,
		routingc.iMergeSecs,
		routingc.iSecs,
		routingc.cMemo
	FROM
		Bd_InventoryRoutingConfig routingc
		INNER JOIN Bd_InventoryRouting routing ON routing.iAutoId = routingc.iInventoryRoutingId
		AND routing.isEnabled = 1
		INNER JOIN Bd_Inventory inv ON inv.iAutoId = routing.iInventoryId
		AND inv.iAutoId = routingc.iRsInventoryId
	WHERE
		routingc.isEnabled = 1
		AND inv.iAutoId = 451617187998654 UNION ALL
	SELECT
		NULL invId,
		routingc.iAutoId,
		routingc.iPid AS iPid,
		routingc.iSeq,
		routingc.cMergedSeq,
		routingc.cOperationName,
		routingc.iType,
		routingc.cProductSn,
		routingc.cProductTechSn,
		routingc.iMergedNum,
		routingc.iMergeRate,
		routingc.iMergeSecs,
		routingc.iSecs,
		routingc.cMemo
	FROM
		Bd_InventoryRoutingConfig routingc
		INNER JOIN Bd_InventoryRouting routing ON routing.iAutoId = routingc.iInventoryRoutingId
		AND routing.isEnabled = 1
	WHERE
		routingc.isEnabled = 1
		AND ISNULL( routingc.iRsInventoryId, '' ) = ''
		AND routing.iInventoryId = 451617187998654 UNION ALL
	SELECT
		routinginvc.iInventoryId invId,
		routinginvc.iAutoId,
		routinginvc.iInventoryRoutingConfigId AS iPid,
		routingc.iSeq,
		routingc.cMergedSeq,
		routingc.cOperationName,
		routingc.iType,
		routingc.cProductSn,
		routingc.cProductTechSn,
		routingc.iMergedNum,
		routingc.iMergeRate,
		routingc.iMergeSecs,
		routingc.iSecs,
		routingc.cMemo
	FROM
		Bd_InventoryRoutingInvc routinginvc
		INNER JOIN Bd_InventoryRoutingConfig routingc ON routingc.iAutoId = routinginvc.iInventoryRoutingConfigId
		INNER JOIN Bd_InventoryRouting routing ON routing.iAutoId = routingc.iInventoryRoutingId
		AND routing.isEnabled = 1
	WHERE
		routingc.isEnabled = 1
		AND routing.iInventoryId = 451617187998654
	) a
	LEFT JOIN Bd_Inventory inv ON inv.iAutoId = a.invId
ORDER BY
	a.iSeq,
	a.cMergedSeq ASC
#end
