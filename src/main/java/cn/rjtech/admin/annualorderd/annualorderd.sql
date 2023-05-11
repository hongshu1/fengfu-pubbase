#sql("findEditTableDatas")
SELECT aod.iautoid, i.iautoid iinventoryid, i.cInvCode, i.cinvcode1, i.cinvname1, i.cinvstd, u.cUomName,
	(SELECT iqty FROM Co_AnnualOrderD_Qty WHERE iAnnualOrderDid = aod.iautoid AND iyear = aom.iyear AND imonth = 1) iqty1,
	(SELECT iqty FROM Co_AnnualOrderD_Qty WHERE iAnnualOrderDid = aod.iautoid AND iyear = aom.iyear AND imonth = 2) iqty2,
	(SELECT iqty FROM Co_AnnualOrderD_Qty WHERE iAnnualOrderDid = aod.iautoid AND iyear = aom.iyear AND imonth = 3) iqty3,
	(SELECT iqty FROM Co_AnnualOrderD_Qty WHERE iAnnualOrderDid = aod.iautoid AND iyear = aom.iyear AND imonth = 4) iqty4,
	(SELECT iqty FROM Co_AnnualOrderD_Qty WHERE iAnnualOrderDid = aod.iautoid AND iyear = aom.iyear AND imonth = 5) iqty5,
	(SELECT iqty FROM Co_AnnualOrderD_Qty WHERE iAnnualOrderDid = aod.iautoid AND iyear = aom.iyear AND imonth = 6) iqty6,
	(SELECT iqty FROM Co_AnnualOrderD_Qty WHERE iAnnualOrderDid = aod.iautoid AND iyear = aom.iyear AND imonth = 7) iqty7,
	(SELECT iqty FROM Co_AnnualOrderD_Qty WHERE iAnnualOrderDid = aod.iautoid AND iyear = aom.iyear AND imonth = 8) iqty8,
	(SELECT iqty FROM Co_AnnualOrderD_Qty WHERE iAnnualOrderDid = aod.iautoid AND iyear = aom.iyear AND imonth = 9) iqty9,
	(SELECT iqty FROM Co_AnnualOrderD_Qty WHERE iAnnualOrderDid = aod.iautoid AND iyear = aom.iyear AND imonth = 10) iqty10,
	(SELECT iqty FROM Co_AnnualOrderD_Qty WHERE iAnnualOrderDid = aod.iautoid AND iyear = aom.iyear AND imonth = 11) iqty11,
	(SELECT iqty FROM Co_AnnualOrderD_Qty WHERE iAnnualOrderDid = aod.iautoid AND iyear = aom.iyear AND imonth = 12) iqty12,
	aod.iYear1Sum iqtytotal
FROM Co_AnnualOrderD aod
	LEFT JOIN Co_AnnualOrderM aom ON aod.iAnnualOrderMid = aom.iautoid
	LEFT JOIN Bd_Inventory i ON aod.iInventoryId = i.iautoid
	LEFT JOIN Bd_Uom u on i.iInventoryUomId1 = u.iautoid
WHERE aod.isdeleted = '0' AND aom.isdeleted = '0'
	#if(iAnnualOrderMid)
		AND aom.iautoid = #para(iAnnualOrderMid)
	#end
ORDER BY aod.iautoid
#end