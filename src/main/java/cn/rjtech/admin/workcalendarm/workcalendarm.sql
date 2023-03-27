#sql("paginateAdminDatas")
SELECT *
FROM dbo.Bd_WorkCalendarM wm
WHERE 1=1
#if(isdeleted)
    AND wm.isDeleted = #para(isdeleted)
#end
#if(cyear)
    AND wm.cYear LIKE CONCAT('%', #para(cyear), '%')
#end
#if(ccalendarname)
    AND wm.cCalendarName LIKE CONCAT('%', #para(ccalendarname), '%')
#end
#if(isenabled)
    AND wm.isEnabled = #para(isenabled)
#end
ORDER BY wm.iautoid DESC
#end

#sql("getDataExport")
SELECT
	wm.*,
	wd.dStartTime dStartTimed,
	wd.dEndTime dEndTimed,
	wd.cMemo cMemod,
	jd.name
FROM
	Bd_WorkCalendarM wm
	LEFT JOIN Bd_WorkCalendarD wd ON wm.iAutoId = wd.iWorkCalendarId
	LEFT JOIN #(getBaseDbName()).dbo.jb_dictionary jd ON wd.cCalendarSn = jd.sn
	AND jd.type_key = 'holiday_date'
	WHERE wm.isDeleted = '0'
	#if(ids)
        AND wm.iAutoId IN (#(ids))
	#end
    #if(cyear)
        AND wm.cYear LIKE CONCAT('%', #para(cyear), '%')
    #end
    #if(ccalendarname)
        AND wm.cCalendarName LIKE CONCAT('%', #para(ccalendarname), '%')
    #end
    #if(isenabled)
        AND wm.isEnabled = #para(isenabled)
    #end
#end