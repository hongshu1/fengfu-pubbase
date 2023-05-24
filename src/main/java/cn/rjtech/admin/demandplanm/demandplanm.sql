#sql("findByVendorDate")
SELECT DISTINCT
	a.iVendorId,
	a.iInventoryId,
	inv.cInvStd,
	inv.cInvCode,
	inv.cInvName,
	inv.cInvName1,
	inv.cInvCode1,
	inv.isGavePresent AS isPresent,
	inv.iPkgQty,
	inv.iPurchaseUomId,
	uom.cUomName,
	addr.iAutoId iVendorAddrId,
	addr.cDistrictName
FROM
	Mrp_DemandPlanD a
	INNER JOIN Mrp_DemandPlanM m ON m.iAutoId = a.iDemandPlanMid AND m.IsDeleted = '0'
	INNER JOIN Bd_Inventory inv ON inv.iautoId = a.iInventoryId AND inv.isDeleted = '0'
	LEFT JOIN Bd_Uom uom ON uom.iAutoId = inv.iPurchaseUomId AND uom.isDeleted = '0'
	LEFT JOIN Bd_VendorAddr addr ON addr.iVendorId = a.iInventoryId AND addr.isDeleted = '0' AND addr.isDefault = 1
WHERE
    inv.iProcessType = #para(processType)
    AND a.iStatus = 1
    AND a.iGenType = 0
    AND m.iStatus = 1
    AND convert(char(10),m.dBeginDate,126) <= #para(beginDate)
    AND convert(char(10),m.dEndDate,126) >=#para(endDate)
    AND m.iOrgId = #para(orgId)
    AND a.iVendorId = #para(iVendorId)
#end
