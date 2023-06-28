#sql("getAdminDatas")
SELECT *
FROM Bd_UptimeCategory
WHERE isDeleted = '0'
ORDER BY dCreateTime DESC
#end

#sql("uptimeTplTableDatas")
SELECT up.iAutoId AS iuptimeparamid, up.cUptimeParamName, uc.iAutoId AS iuptimecategoryid, uc.cUptimeCategoryName
FROM Bd_UptimeParam up
LEFT JOIN Bd_UptimeCategory uc ON up.iUptimeCategoryId = uc.iAutoId
WHERE up.isEnabled = '1'
AND uc.isDeleted = '0'
#if(iuptimecategoryid)
AND uc.iAutoId = #para(iuptimecategoryid)
#end
#end