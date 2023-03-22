#sql("getList")
SELECT
	a.*,
	d.sn,
	d.name
FROM
	Bd_WorkShiftD a
	LEFT JOIN #(getBaseDbName()).dbo.jb_dictionary d ON a.iType = d.sn
	AND d.type_key = 'work_time_type'
WHERE
	a.iWorkShiftMId = #para(iworkshiftmid)
#end