#sql("paginateAdminDatas")
SELECT
	co.*,
	c.cCusName
FROM
	Co_WeekOrderM co
	LEFT JOIN Bd_Customer c ON c.iAutoId = co.iCustomerId
	AND c.isDeleted = 0
WHERE
	co.IsDeleted = 0
    #if(corderno)
        AND co.cOrderNo LIKE CONCAT('%', #para(corderno), '%')
    #end
     #if(cCreateName)
        AND co.ccreatename LIKE CONCAT('%', #para(cCreateName), '%')
    #end
    #if(ccuscode)
        AND co.iCustomerId  = #para(ccuscode)
    #end
    #if(iorderstatus)
        AND co.iOrderStatus = #para(iorderstatus)
    #end
    #if(startTime)
        AND convert(date,co.dCreateTime) >= convert(date,#para(startTime))
    #end
    #if(endTime)
        AND convert(date,co.dCreateTime) <= convert(date,#para(endTime))
    #end
     #if(iorderstatus)
        AND co.iOrderStatus = #para(iorderstatus)
    #end
    #if(ids)
        AND CHARINDEX(','+cast((select c.iAutoId) as nvarchar(20))+',' , ','+#para(ids)+',') > 0
    #end
	ORDER BY c.dCreateTime DESC
#end
