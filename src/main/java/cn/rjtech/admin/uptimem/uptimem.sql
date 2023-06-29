#sql("getUptimeMList")
###根据条件查询稼动时间主表记录
SELECT a.*,
       b.cWorkCode,b.cWorkName,c.cWorkShiftCode,c.cWorkShiftName,
       CASE a.iAuditStatus
           WHEN 0 THEN
               '已保存'
           WHEN 1 THEN
               '待审核'
           WHEN 2 THEN
               '审核通过'
           WHEN 3 THEN
               '审核不通过'
           END AS statename
FROM PL_UptimeM AS a
         LEFT JOIN Bd_WorkRegionM AS b ON b.iAutoId = a.iWorkRegionMid
         LEFT JOIN Bd_WorkShiftM AS c ON c.iAutoId = a.iWorkShiftMid
WHERE a.isDeleted = 0
    #if(mid)
        AND a.iAutoId = #para(mid)
    #end
    #if(cworkname)
        AND b.cWorkName LIKE CONCAT('%', #para(cworkname), '%')
    #end
    #if(cworkshiftname)
        AND c.cWorkShiftName LIKE CONCAT('%', #para(cworkshiftname), '%')
    #end
    #if(iauditstatus)
        AND a.iAuditStatus = #para(iauditstatus)
    #end
    #if(startdate)
        AND a.dCreateTime >= #para(startdate)
    #end
    #if(enddate)
        AND a.dCreateTime <= #para(enddate)
    #end
    ORDER BY a.dCreateTime DESC
#end

#sql("getUptimeTplInfo")
###根据条件查询稼动时间模板明细
SELECT a.iWorkRegionMid,a.iWorkShiftMid,a.iBaseMins,a.iStopMins,a.iSwitchMins,a.iOtMins,a.iWorkMins
FROM Bd_UptimeTplM AS a
WHERE a.iWorkRegionMid = #para(iworkregionmid)  AND iWorkShiftMid = #para(iworkshiftmid)
#end

#sql("getUptimeDList")
###根据条件查询稼动时间记录明细
SELECT a.iWorkRegionMid,a.iWorkShiftMid,a.iBaseMins,a.iStopMins,a.iSwitchMins,a.iOtMins,a.iWorkMins,
       c.iUptimeCategoryId,d.cUptimeCategoryName,c.iUptimeParamId,e.cUptimeParamName,c.iSeq,c.iMins AS iStdMins,0 AS iMins
FROM Bd_UptimeTplM AS a
         LEFT JOIN Bd_UptimeTplCategory AS b ON b.iUptimeTplMid = a.iAutoId
         RIGHT JOIN Bd_UptimeTplTable AS c ON c.iUptimeTplMid = a.iAutoId
         LEFT JOIN Bd_UptimeCategory AS d ON d.iAutoId = c.iUptimeCategoryId
         LEFT JOIN Bd_UptimeParam AS e ON e.iAutoId = c.iUptimeParamId
WHERE a.iWorkRegionMid = #para(iworkregionmid)  AND iWorkShiftMid = #para(iworkshiftmid)
ORDER BY c.iSeq
#end

#sql("getUptimeDList2")
###根据条件查询稼动时间记录明细
SELECT
    b.iAutoId,a.iWorkRegionMid,a.iWorkShiftMid,a.iBaseMins,a.iStopMins,a.iSwitchMins,a.iOtMins,a.iWorkMins,a.iRate1,a.iRate2,
    b.iUptimeCategoryId,c.cUptimeCategoryName,b.iUptimeParamId,d.cUptimeParamName,b.iSeq,b.iStdMins,b.iMins
FROM PL_UptimeM AS a
         RIGHT JOIN PL_UptimeD AS b ON b.iUptimeMid = a.iAutoId
         LEFT JOIN Bd_UptimeCategory AS c ON c.iAutoId = b.iUptimeCategoryId
         LEFT JOIN Bd_UptimeParam AS d ON d.iAutoId = b.iUptimeParamId
WHERE a.iAutoId = #para(iautoid)
ORDER BY b.iSeq
#end