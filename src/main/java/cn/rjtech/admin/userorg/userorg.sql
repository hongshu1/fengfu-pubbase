#sql("getList")
SELECT uo.*,
       d.org_name AS name,
       u.name AS parent_psn_name
FROM base_user_org uo
    INNER JOIN jb_org d ON uo.org_id = d.id
    LEFT JOIN jb_user u ON uo.parent_psn_id = u.id
WHERE uo.user_id = #para(userId)
    AND uo.is_deleted = '0'
ORDER BY id
#end