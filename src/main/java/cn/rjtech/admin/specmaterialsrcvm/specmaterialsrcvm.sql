#sql("getApiSpecmaterialsrcvmDatas")
SELECT
	srm.cSpecRcvDocNo,
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
WHERE modoc.iAutoId = #(imodocid)
#end

#sql("getInventoryDatasByDocid")
SELECT
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