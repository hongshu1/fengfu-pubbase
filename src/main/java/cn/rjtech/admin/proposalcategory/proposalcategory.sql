#sql("downPaginateAdminDatas")
select * from #(getMesDbName()).dbo.Bas_ProposalCategory
where
1=1
    and  iPid = #para(typeId)
    #if(keywords)
    and (cCategoryName like concat('%',#para(keywords),'%') or   cCategoryCode like concat('%',#para(keywords),'%'))
    #end
#end

#sql("paginateAdminDatas")
select * from #(getMesDbName()).dbo.Bas_ProposalCategory
where
    1=1 and  iPid = 0
    #if(keywords)
     and (cCategoryName like concat('%',#para(keywords),'%') or   cCategoryCode like concat('%',#para(keywords),'%'))
    #end
#end

#sql("numberSelect")
select * from #(getMesDbName()).dbo.Bas_ProposalCategory
where 1=1
 and iPid = (select iAutoId from #(getMesDbName()).dbo.Bas_ProposalCategory  where  cCategoryCode = #(cCategoryCode) )
#end