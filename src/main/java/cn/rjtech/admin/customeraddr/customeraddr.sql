#sql("paginateAdminDatas")

#end


#sql("wareHouse")
SELECT  a.*
FROM Bd_Warehouse a
where 1=1
    AND a.cOrgCode = #para(orgCode)
    #if(q)
		and (a.cWhCode like concat('%',#para(q),'%') OR a.cWhName like concat('%',#para(q),'%'))
	#end
#end

