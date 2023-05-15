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

#sql("getPermissionIds")
SELECT
	btn_id id
FROM
	[base_button_permission]
WHERE
	object_type = #para(objecttype)
	AND object_id IN( #(objectid) )
	AND is_deleted = '0'
UNION ALL
SELECT
	mp.menu_id id
FROM
	[base_menu_permission] mp
WHERE
	mp.object_type  = #para(objecttype)
	AND mp.object_id IN( #(objectid) )
	AND mp.is_deleted= '0'
#end

#sql("getPermissionList")
SELECT
 id,title,permission_key,pkey
FROM
 (
 SELECT
  p.id,
  p.title,
  p.permission_key,
  p1.permission_key pkey
 FROM
  jb_permission p
	LEFT JOIN jb_permission p1 ON p.pid = p1.id
 WHERE
  p.is_menu = '1'
UNION ALL
 SELECT
  bt.id,
  bt.btn_name AS title,
  bt.permission_key,
  p1.permission_key pkey
 FROM
  base_permission_btn bt
  INNER JOIN jb_permission p ON bt.permission_id = p.id
  LEFT JOIN jb_permission p1 ON p.pid = p1.id
 WHERE
  p.is_menu = '1'
 ) a
WHERE a.id IN ( #(ids) )
 #if(permissionkey)
    AND a.permission_key = #para(permissionkey)
 #end
 #if(pkey)
    AND a.pkey LIKE '%#(pkey)%'
 #end
 group by id,title,permission_key,pkey
#end