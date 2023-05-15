#sql("findEditTableDatas")
	select aod.*,i.cInvCode,i.cinvcode1,i.cinvname1,i.cinvstd,u.cUomName
from co_monthorderd aod
	left join co_monthorderm aom on aod.imonthordermid = aom.iautoid
	left join Bd_Inventory i on aod.iInventoryId = i.iautoid
	left join Bd_Uom u on i.iInventoryUomId1 = u.iautoid
		where aod.isDeleted = '0' and aom.isDeleted = '0'
	#if(imonthordermid)
		and aom.iautoid = #para(imonthordermid)
	#end
#end

#sql("cusOrderSumIncCusPlan")
SELECT
	cmd.*,cmm.iMonth
FROM
	Co_MonthOrderD cmd
	LEFT JOIN Co_MonthOrderM cmm ON cmm.iAutoId = cmd.iMonthOrderMid
	AND cmm.IsDeleted= 0
WHERE
	cmd.IsDeleted = 0
	AND cmm.iAuditStatus = 2
	#if(inventoryIdList)
		AND cmd.iInventoryId IN (#(inventoryIdList))
	#end
	#if(iYear)
		AND cmm.iYear = #para(iYear)
	#end
#end