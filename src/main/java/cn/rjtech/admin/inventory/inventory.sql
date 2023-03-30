#sql("getAutocompleteData")
	select i.*,u.cuomname from bd_inventory i
		left join bd_uom u on i.iInventoryUomId1 = u.iautoid
	where 1=1 
	#if(q)
		and (i.cinvcode like concat('%',#para(q),'%') or i.cinvcode1 like concat('%',#para(q),'%') 
			or i.cinvname1 like concat('%',#para(q),'%') or i.cinvstd like concat('%',#para(q),'%')
			or u.cuomname like concat('%',#para(q),'%')
		)
	#end
#end