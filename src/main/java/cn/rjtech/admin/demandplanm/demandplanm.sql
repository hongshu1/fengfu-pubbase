#sql("findByVendorDate")
SELECT
	a.*
FROM
	Mrp_DemandPlanM a
WHERE
	a.IsDeleted = 1
	AND a.iOrgId = #para(orgId)
	AND a.iVendorId = #para(iVendorId)
	AND a.dBeginDate = #para(beginDate)
	AND a.dEndDate =#para(endDate)
#end
