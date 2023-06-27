#sql("getDashBoardCommonMenu")
select jp.title,jp.url,jp.icons,row_number() over(order by jp.sort_rank asc) num from base_common_menu bcm
	 inner join jb_permission jp on bcm.menu_id = jp.id
	 where bcm.type = 1 and user_id = #para(iuserid) and jp.is_deleted = 0
	union all select null title, 'admin/commonmenu/add/' url, 'fa fa-plus' icons,null num
	
#end