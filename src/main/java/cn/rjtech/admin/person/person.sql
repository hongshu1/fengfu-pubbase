#sql("findAll")
SELECT
    A.*
FROM
    Bd_Person A
WHERE
    A.isEnabled = '1'
    AND A.isDeleted = '0'
    #if(cpersonname)
        AND A.cPsn_Name LIKE CONCAT('%', #para(cpersonname),'%')
    #end
#end
