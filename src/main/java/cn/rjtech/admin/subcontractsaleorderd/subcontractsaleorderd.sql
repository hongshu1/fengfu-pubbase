#sql("findEditTableDatas")
	select aod.*,i.cInvCode,i.cinvcode1,i.cinvname1,i.cinvstd,u.cUomName
from Co_SubcontractSaleOrderD aod
	left join Co_SubcontractSaleOrderm aom on aod.isubcontractsaleordermid = aom.iautoid
	left join Bd_Inventory i on aod.iInventoryId = i.iautoid
	left join Bd_Uom u on i.iInventoryUomId1 = u.iautoid
		where aod.isDeleted = '0' and aom.isDeleted = '0'
	#if(isubcontractsaleordermid)
		and aom.iautoid = #para(isubcontractsaleordermid)
	#end
#end