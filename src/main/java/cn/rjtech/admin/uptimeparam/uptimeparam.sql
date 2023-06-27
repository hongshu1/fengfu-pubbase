#sql("getAdminDatas")
SELECT up.*, uc.cUptimeCategoryName
FROM Bd_UptimeParam up
LEFT JOIN Bd_UptimeCategory uc ON up.iUptimeCategoryId = uc.iAutoId
WHERE up.isDeleted = '0'
#if(cuptimeparamname)
AND up.cUptimeParamName = #para(cuptimeparamname)
#end
#if(isenabled)
AND up.isEnabled = #para(isenabled)
#end
#if(cuptimecategoryname)
AND uc.cUptimeCategoryName LIKE CONCAT('%',#para(cuptimecategoryname),'%')
#end
ORDER BY up.dCreateTime DESC
#end