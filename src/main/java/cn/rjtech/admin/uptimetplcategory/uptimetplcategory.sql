#sql("getAdminDatas")
SELECT utc.*, uc.cUptimeCategoryName
FROM Bd_UptimeTplCategory utc
LEFT JOIN Bd_UptimeCategory uc ON utc.iUptimeCategoryId = uc.iAutoId
WHERE 1 = 1
#if(iuptimetplmid)
AND utc.iUptimeTplMid = #para(iuptimetplmid)
#end
#end