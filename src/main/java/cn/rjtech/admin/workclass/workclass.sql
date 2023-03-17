#sql("list")
SELECT *
FROM Bd_WorkClass
WHERE isDeleted = '0'
    #if(ids)
        AND iautoid IN #(ids)
    #end
    #if(cworkclasscode)
        AND cworkclasscode = #para(cworkclasscode)
    #end
    #if(cworkclassname)
        AND cworkclassname = #para(cworkclassname)
    #end
    #if(ilevel)
        AND ilevel = #para(ilevel)
    #end
    #if(isenabled)
        AND isenabled = #para(isenabled == 'true' ? 1 : 0)
    #end
ORDER BY dCreateTime DESC
#end