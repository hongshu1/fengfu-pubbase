#sql("getAdminDatas")
SELECT utm.*, wr.cWorkName, ws.cWorkShiftName
FROM Bd_UptimeTplM utm
LEFT JOIN Bd_WorkRegionM wr ON utm.iWorkRegionMid = wr.iAutoId
LEFT JOIN Bd_WorkShiftM ws ON utm.iWorkShiftMid = ws.iAutoId
WHERE utm.isDeleted = '0'
#if(iworkregionmid)
AND utm.iWorkRegionMid = #para(iworkregionmid)
#end
#if(iworkshiftmid)
AND utm.iWorkShiftMid = #para(iworkshiftmid)
#end
#if(isenabled)
AND utm.isEnabled = #para(isenabled)
#end
#end