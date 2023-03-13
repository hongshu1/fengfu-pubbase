#sql("findMenuBtnWithAlias")
SELECT
    bt.id,
    bt.permission_id AS pid,
    bt.view_type,
    bt.btn_code,
    bt.btn_name AS title,
    bt.sort_rank,
    bt.position,
    bt.func,
    bt.tooltip
FROM base_permission_btn bt
    INNER JOIN jb_permission p ON bt.permission_id = p.id
WHERE p.is_menu = '1'
    #if(applicationId)
        AND p.application_id = #para(applicationId)
    #end
    #if(checkedIds)
        AND bt.id IN (#(checkedIds))
    #end
#end