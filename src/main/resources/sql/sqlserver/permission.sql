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

#sql("findProposalTopNabMenu")
select p.* from jb_topnav ta
	inner join jb_topnav_menu tm on ta.id = tm.topnav_id
	inner join jb_permission p on tm.permission_id = p.id
 where ta.name = '禀议'
#end

#sql("findBasicForm")
SELECT
  c.id,
  c.pid,
  c.title
FROM
  jb_permission b
  INNER JOIN jb_permission c ON b.id = c.pid
WHERE b.title IN ('基础档案',
  '组织建模',
  '排产建模',
  '往来单位',
  '物料建模',
  '仓库建模',
  '生产建模',
  '设备管理',
  '点检建模',
  '禀议建模',
  '容器管理',
  '质量建模')
UNION ALL
SELECT
  id,
  pid,
  title
FROM jb_permission
WHERE title IN ('基础档案',
  '组织建模',
  '排产建模',
  '往来单位',
  '物料建模',
  '仓库建模',
  '生产建模',
  '设备管理',
  '点检建模',
  '禀议建模',
  '容器管理',
  '质量建模')
#end

#sql("findForm")
SELECT
  c.id,
  c.pid,
  c.title
FROM
  jb_permission b
  INNER JOIN jb_permission c ON b.id = c.pid
WHERE b.title IN ('采购/委外管理',
  '入库管理',
  '出库管理',
  '发货管理')
AND c.title NOT IN ('生产备料','双码扫码出货','扫码出货')
UNION ALL
SELECT
  id,
  pid,
  title
FROM jb_permission
WHERE title IN ('采购/委外管理',
  '制造管理',
  '制造工单管理',
  '制造工单批量编辑',
  '质量管理',
	'来料检验单',
	'出货检',
	'在库检',
	'来料异常品记录',
	'制程异常品记录',
	'出库异常记录',
	'在库异常记录',
	'工程内品质巡查',
  '入库管理',
  '出库管理',
  '发货管理')
#end