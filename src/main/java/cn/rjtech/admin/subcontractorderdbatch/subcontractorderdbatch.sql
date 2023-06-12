#sql("findBySubcontractOrderMId")
SELECT
    derm.cOrderNo,
	inv.cInvCode,
	inv.cInvName,
	inv.cInvCode1,
	inv.cInvName1,
	a.*
FROM
	PS_SubcontractOrderDBatch a
	INNER JOIN PS_SubcontractOrderD d ON d.iAutoId = a.iSubcontractOrderDid
	LEFT JOIN PS_SubcontractOrderM derm ON derm.iAutoId= d.iSubcontractOrderMid
	INNER JOIN Bd_Inventory inv ON inv.iAutoId = a.iinventoryId
WHERE
	d.isDeleted = 0
	#if(iSubcontractOrderMid)
	    AND d.iSubcontractOrderMid = #(iSubcontractOrderMid)
	#end
    #if(isEffective)
        AND a.isEffective = #para(isEffective)
    #end
	order by a.dPlanDate,inv.cInvCode,a.cVersion asc
#end

#sql("getVersionFindBySubcontractOrderMId")
SELECT
	ROW_NUMBER ( ) OVER ( ORDER BY inv.cInvCode ) AS SequenceNumber,
	derm.cOrderNo,
	inv.cInvCode,
	inv.cInvName,
	inv.cInvCode1,
	inv.cInvName1,
	inv.cInvStd,
	vendor.cVenName,
	d.cAddress,
	derm.dOrderDate,
	version.cSourceBarcode versionBarcode,
	version.cSourceVersion Version,
	version.iSourceQty versionQty,
	a.*
FROM
	PS_SubcontractOrderDBatchVersion version
	LEFT JOIN PS_SubcontractOrderDBatch a ON a.cVersion = version.cVersion AND a.cBarcode = version.cBarcode
	INNER JOIN PS_SubcontractOrderD d ON d.iAutoId = a.iSubcontractOrderDid
	LEFT JOIN PS_SubcontractOrderM derm ON derm.iAutoId= d.iSubcontractOrderMid
	LEFT JOIN Bd_Vendor vendor ON derm.iVendorId= vendor.iAutoId
	INNER JOIN Bd_Inventory inv ON inv.iAutoId = a.iinventoryId
WHERE
	version.iSubcontractOrderMid= #(iSubcontractOrderMid)
ORDER BY
	a.dPlanDate,
	inv.cInvCode,
	a.cVersion ASC
#end

#sql("getPdfBySubcontractOrderMId")
SELECT
	ROW_NUMBER ( ) OVER ( ORDER BY inv.cInvCode ) AS SequenceNumber,
	derm.cOrderNo,
	inv.cInvCode,
	inv.cInvName,
	inv.cInvCode1,
	inv.cInvName1,
	inv.cInvStd,
	vendor.cVenName,
	d.cAddress,
	derm.dOrderDate,
	a.*
FROM
	PS_SubcontractOrderDBatch a
	INNER JOIN PS_SubcontractOrderD d ON d.iAutoId = a.iSubcontractOrderDid
	LEFT JOIN PS_SubcontractOrderM derm ON derm.iAutoId= d.iSubcontractOrderMid
	LEFT JOIN Bd_Vendor vendor ON derm.iVendorId= vendor.iAutoId
	INNER JOIN Bd_Inventory inv ON inv.iAutoId = a.iinventoryId
WHERE
	d.isDeleted = 0
	#if(iSubcontractOrderMid)
	    AND d.iSubcontractOrderMid = #para(iSubcontractOrderMid)
	#end
    #if(isEffective)
        AND a.isEffective = #para(isEffective)
    #end
	order by a.dPlanDate,inv.cInvCode,a.cVersion asc
#end
