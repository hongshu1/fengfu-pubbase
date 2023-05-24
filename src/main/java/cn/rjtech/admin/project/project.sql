#sql("paginateAdminDatas")
select p.*,d.cDepName,
       (select top 1 i.cInvName from bd_Inventory i where i.cInvCode=p.cInvCode ) as cInvName
from Bas_Project p
    left join bd_Department d on d.cDepCode=p.cdepcode
where 1=1
    #if(cproject)
  		and (p.cprojectcode like concat('%',#para(cproject),'%') or p.cprojectname like concat('%',#para(cproject),'%'))
    #end
    #if(cdepcode)
    	and d.cDepcode = #para(cdepcode)
    #end
#end


#sql("u8InventoryList")
select  cInvCode ,cInvName
from Inventory
where
1=1
    #if(cinvcode)
        and cInvCode like concat('%',#para(cinvcode),'%')
    #end
      #if(cinvname)
        and cInvName like concat('%',#para(cinvname),'%')
    #end
#end

#sql("getAutocompleteList")
select top #(limit) iautoid,cprojectcode,cprojectname
from bas_project where 1=1
    #if(keyword)
        and (
            cprojectcode like concat('%',#para(keyword),'%') or
            cprojectname like concat('%',#para(keyword),'%')
        )
    #end
#end