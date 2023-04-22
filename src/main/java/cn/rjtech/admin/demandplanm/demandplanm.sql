#sql("findByVendorDate")
SELECT DISTINCT
	a.iVendorId,
	a.iInventoryId,
	inv.cInvStd,
	inv.cInvCode,
	inv.cInvName,
	inv.cInvName1,
	inv.cInvCode1,
	inv.isGavePresent,
	inv.iPkgQty,
	inv.iPurchaseUomId,
	uom.cUomName,
	addr.iAutoId iVendorAddrId,
	addr.cDistrictName
FROM
	Mrp_DemandPlanM a
	INNER JOIN Bd_Inventory inv ON inv.iautoId = a.iInventoryId AND inv.isDeleted = '0'
	LEFT JOIN Bd_Uom uom ON uom.iAutoId = inv.iPurchaseUomId AND uom.isDeleted = '0'
	LEFT JOIN Bd_VendorAddr addr ON addr.iVendorId = a.iInventoryId AND addr.isDeleted = '0' AND addr.isDefault = 1
#end
