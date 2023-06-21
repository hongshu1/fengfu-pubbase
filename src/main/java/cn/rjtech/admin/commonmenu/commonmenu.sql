#sql("getDashBoardCommonMenu")
select jp.title,jp.url,jp.icons,row_number() over(order by jp.sort_rank asc) num from base_common_menu bcm
	 inner join jb_permission jp on bcm.menu_id = jp.id
	 where bcm.type = 1 and jp.is_deleted = 0 
	union all select null title, 'admin/commonmenu/add/' url, 'fa fa-plus' icons,null num
	
#end

#sql("findProposalTopNabMenu")
select p.* from jb_topnav ta
	inner join jb_topnav_menu tm on ta.id = tm.topnav_id
	inner join jb_permission p on tm.permission_id = p.id
 where ta.name = '禀议'
#end