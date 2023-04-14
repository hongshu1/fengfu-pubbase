#sql("weekOrderDData")
SELECT
	bi.cInvAddCode,
	bi.cinvcode,
	bi.cInvCode1,
	bi.cInvName1,
	bi.cinvStd,
	bi.iSalesUomId,
	cwd.*
FROM
	Co_WeekOrderD cwd
	LEFT JOIN bd_inventory bi ON bi.iAutoId = cwd.iInventoryId 	AND bi.isDeleted = 0
	LEFT JOIN Co_WeekOrderM cwm ON cwd.iWeekOrderMid = cwm.iAutoId AND cwm.IsDeleted=0
WHERE
	cwd.IsDeleted = 0
	 #if(cCode)
        AND cwd.cCode LIKE CONCAT('%', #para(cCode), '%')
    #end
    #if(iWeekOrderMid)
        AND cwd.iWeekOrderMid = #para(iWeekOrderMid)
    #end
     #if(cinvname1)
        AND bi.cinvname1 LIKE CONCAT('%', #para(cinvname1), '%')
    #end
     #if(cinvcode1)
        AND bi.cinvcode1 LIKE CONCAT('%', #para(cinvcode1), '%')
    #end
     #if(cOrderNo)
        AND cwm.cOrderNo LIKE CONCAT('%', #para(cOrderNo), '%')
    #end
     #if(startTime)
        AND convert(date,cwm.dCreateTime) >= convert(date,#para(startTime))
    #end
    #if(endTime)
        AND convert(date,cwm.dCreateTime) <= convert(date,#para(endTime))
    #end
    #if(ids)
        AND CHARINDEX(','+cast((select c.iAutoId) as nvarchar(20))+',' , ','+#para(ids)+',') > 0
    #end
    ORDER BY cwm.dCreateTime DESC
#end