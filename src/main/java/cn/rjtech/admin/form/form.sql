#sql("paginateAdminDatas")
SELECT f.*, fc.cName AS ccategoryname
FROM Bd_Form f
INNER JOIN Bd_FormCategory fc ON f.iformcategoryid = fc.iautoid
ORDER BY f.iautoid DESC
#end
