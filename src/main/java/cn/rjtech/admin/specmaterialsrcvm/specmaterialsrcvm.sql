#sql("getApiSpecmaterialsrcvmDatas")
SELECT
    srm.iAutoId,
	srm.cSpecRcvDocNo,
	srm.iStatus,
	srm.iAuditStatus,
	modoc.cMoDocNo,
	srm.dDemandDate,
	dep.cDepName,
	srm.cCreateName,
	srm.dCreateTime
FROM
	Mo_SpecMaterialsRcvM srm
	LEFT JOIN Mo_MoDoc modoc ON srm.iMoDocId = modoc.iAutoId
	LEFT JOIN Bd_Department dep ON srm.iDepartmentId= dep.iAutoId
WHERE modoc.iAutoId = #(imodocid) AND srm.IsDeleted=0
#end

#sql("getInventoryDatasByDocid")
SELECT
    tory.IAutoId,
	tory.cInvCode,
	tory.cInvCode1,
	tory.cInvName1,
	tory.cInvStd,
	uom.cUomName
FROM
	Bd_Inventory tory
	LEFT JOIN Mo_MoRoutingInvc invc ON invc.iInventoryId = tory.iAutoId
	LEFT JOIN Mo_MoRoutingConfig config ON invc.iMoRoutingConfigId = config.iAutoId
	LEFT JOIN Mo_MoRouting ting ON config.iMoRoutingId = ting.iAutoId
	LEFT JOIN Bd_Uom uom ON uom.iAutoId= tory.iInventoryUomId2
WHERE
	ting.iMoDocId = #(imodocid)
#if(cinvcode)
	AND tory.cInvCode LIKE CONCAT('%',#para(cinvcode),'%')
#end
#if(cinvcode1)
	AND tory.cInvCode1 LIKE CONCAT('%',#para(cinvcode1),'%')
#end
#if(cinvname1)
	AND tory.cInvName1 LIKE CONCAT('%',#para(cinvname1),'%')
#end
#end

#sql("getDepartmentIdByPersonId")
SELECT
	ment.iAutoId
FROM
	Bd_Department ment
	LEFT JOIN Bd_Person son ON ment.cDepCode= son.cDept_num
WHERE son.iAutoId=#(id)
#end

#sql("getRcvdIdByRevmId")
SELECT iAutoId FROM Mo_SpecMaterialsRcvD WHERE iSpecRcvMid=#(id)
#end

#sql("getRcvdDatasByRevmId")
SELECT
	tory.IAutoId,
	tory.cInvCode,
	tory.cInvCode1,
	tory.cInvName1,
	tory.cInvStd,
	uom.cUomName,
	rd.iQty
FROM
	Mo_SpecMaterialsRcvD rd
	LEFT JOIN Bd_Inventory tory ON rd.iInventoryId= tory.iAutoId
	LEFT JOIN Bd_Uom uom ON uom.iAutoId= tory.iInventoryUomId2
	WHERE rd.iSpecRcvMid=#(id)
#end