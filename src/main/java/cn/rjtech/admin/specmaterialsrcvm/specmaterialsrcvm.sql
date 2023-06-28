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