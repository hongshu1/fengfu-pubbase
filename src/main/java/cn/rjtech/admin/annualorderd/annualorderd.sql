#sql("findEditTableDatas")
	select aod.iautoid,i.iautoid iinventoryid,i.cInvCode,i.cinvcode1,i.cinvname1,i.cinvstd,u.cUomName,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 1) inowyearmonthamount1,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 2) inowyearmonthamount2,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 3) inowyearmonthamount3,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 4) inowyearmonthamount4,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 5) inowyearmonthamount5,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 6) inowyearmonthamount6,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 7) inowyearmonthamount7,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 8) inowyearmonthamount8,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 9) inowyearmonthamount9,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 10) inowyearmonthamount10,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 11) inowyearmonthamount11,
	(select iqty from Co_AnnualOrderD_Qty where iAnnualOrderDid = aod.iautoid and iyear = aom.iyear and imonth = 12) inowyearmonthamount12,
	aod.iYear1Sum inowyearmonthamounttotal,
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