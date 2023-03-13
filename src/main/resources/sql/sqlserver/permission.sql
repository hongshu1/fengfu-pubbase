#sql("getMenuRecordWithAlias")
SELECT id, title, pid, sort_rank, application_id
FROM jb_permission
WHERE is_menu = '1'
    #if(applicationId)
        AND application_id = #para(applicationId)
    #end
    #if(checkedIds)
        AND id IN (#(checkedIds))
    #end
ORDER BY application_id,app_id,sort_rank
#end