#sql("findEditTableDatas")
	select aod.iautoid,i.iautoid iinventoryid,i.cInvCode,i.cinvcode1,i.cinvname1,i.cinvstd,u.cUomName,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 1) iqty1,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 2) iqty2,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 3) iqty3,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 4) iqty4,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 5) iqty5,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 6) iqty6,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 7) iqty7,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 8) iqty8,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 9) iqty9,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 10) iqty10,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 11) iqty11,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 12) iqty12,
	aod.iYear1Sum iqtytotal,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear+1 and imonth = 1) inextyearmonthamount1,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear+1 and imonth = 2) inextyearmonthamount2,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear+1 and imonth = 3) inextyearmonthamount3,
	aod.iYear2Sum inextyearmonthamounttotal	
from Co_AnnualOrderD aod
	left join Co_AnnualOrderM aom on aod.iAnnualOrderMid = aom.iautoid
	left join Bd_Inventory i on aod.iInventoryId = i.iautoid
	left join Bd_Uom u on i.iInventoryUomId1 = u.iautoid
		where 1=1 and aod.isdeleted = 0 and aom.isdeleted = 0
	#if(iAnnualOrderMid)
		and  aom.iautoid = #para(iAnnualOrderMid)
	#end
#end