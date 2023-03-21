#sql("paginateAdminDatas")
SELECT  wd.*,d.sn,d.name AS holidayname FROM dbo.Bd_WorkCalendarD wd
	LEFT JOIN #(getBaseDbName()).dbo.jb_dictionary d ON wd.cCalendarSn = d.sn
	AND d.type_key = 'holiday_date'
WHERE 1=1
    AND wd.iWorkCalendarId = #para(iworkcalendarid)
#end