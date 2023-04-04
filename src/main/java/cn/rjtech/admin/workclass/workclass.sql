#sql("list")
SELECT
    t1.*,
    Convert(decimal(18,2),t1.isalary) AS isalarydes,
    t2.name as ileveldes
FROM Bd_WorkClass t1
    LEFT JOIN #(getBaseDbName()).dbo.jb_dictionary AS t2 ON t1.ilevel = t2.sn AND  t2.type_key ='work_level'
WHERE t1.isDeleted = '0'
#if(iautoid)
    AND t1.iautoid =#para(iautoid)
#end
#if(cworkclasscode)
    AND t1.cworkclasscode = #para(cworkclasscode)
#end
#if(cworkclassname)
    AND t1.cworkclassname = #para(cworkclassname)
#end
#if(ilevel)
    AND t1.ilevel = #para(ilevel)
#end
#if(isenabled)
    AND t1.isenabled = #para(isenabled == 'true' ? 1 : 0)
#end
    ORDER BY t1.dUpdateTime DESC
#end