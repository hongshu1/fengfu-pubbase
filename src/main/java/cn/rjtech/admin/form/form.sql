#sql("paginateAdminDatas")
SELECT f.*, fc.cName AS ccategoryname
FROM Bd_Form f
INNER JOIN Bd_FormCategory fc ON f.iformcategoryid = fc.iautoid
WHERE 1=1
#if(cformcode)
    AND f.cformcode LIKE CONCAT('%', #para(cformcode), '%')
#end
#if(cformname)
    AND f.cformname LIKE CONCAT('%', #para(cformname), '%')
#end
#if(cformtypesn)
    AND f.cformtypesn = #para(cformtypesn)
#end
ORDER BY f.iautoid DESC
#end
