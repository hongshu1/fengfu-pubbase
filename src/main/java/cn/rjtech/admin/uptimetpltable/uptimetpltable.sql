#sql("uptimeTplTableDatas")
SELECT ut.*, up.cUptimeParamName, uc.cUptimeCategoryName
FROM Bd_UptimeTplTable ut
LEFT JOIN Bd_UptimeParam up on ut.iUptimeParamId = up.iAutoId
LEFT JOIN Bd_UptimeCategory uc on ut.iUptimeCategoryId = uc.iAutoId
WHERE 1 = 1
#if(iuptimetplmid)
AND ut.iUptimeTplMid = #para(iuptimetplmid)
#end
#end