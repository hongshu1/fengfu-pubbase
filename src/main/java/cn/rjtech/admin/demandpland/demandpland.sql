#sql("findByDemandPlanMList")
SELECT
	*
FROM
	Mrp_DemandPlanD
WHERE
	iDemandPlanMid IN (
	SELECT
		a.iAutoId
	FROM
		Mrp_DemandPlanM a
	WHERE
		a.IsDeleted = 0
        AND a.iOrgId = #para(orgId)
        AND a.iVendorId = #para(iVendorId)
        AND convert(char(10),a.dBeginDate,126) <= #para(beginDate)
        AND convert(char(10),a.dEndDate,126) >=#para(endDate)
	)
#end
