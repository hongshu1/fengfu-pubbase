#sql("getDataExport")
SELECT
	a.cCode,
	a.cName,
	b.cCode cCoded,
	b.cName cNamed
FROM
	Bd_CustomerClass a
	LEFT JOIN Bd_CustomerClass b ON b.iAutoId = a.iPid
	WHERE 1=1
#end