#sql("findByVendorDate")
SELECT
	a.iAutoId,
	a.dBeginDate,
	a.dEndDate,
	a.iVendorId,
	a.iInventoryId,
	inv.cInvStd,
	inv.cInvCode,
	inv.cInvName,
	inv.cInvCode1,
	inv.isGavePresent,
	inv.iPurchaseUomId,
	addr.cDistrictName
FROM
	Mrp_DemandPlanM a
	INNER JOIN Bd_Inventory inv ON inv.iautoId = a.iInventoryId AND inv.isDeleted = '0'
	LEFT JOIN Bd_Uom uom ON uom.iAutoId = inv.iPurchaseUomId AND uom.isDeleted = '0'
	LEFT JOIN Bd_VendorAddr addr ON addr.iVendorId = a.iInventoryId AND addr.isDeleted = '0'
WHERE
	a.IsDeleted = '0'
	AND a.iOrgId = #para(orgId)
	AND a.iVendorId = #para(iVendorId)
	AND convert(char(10),a.dBeginDate,126) = #para(beginDate)
	AND convert(char(10),a.dEndDate,126) =#para(endDate)
#end
