#sql("getAdminDatas")
select *,CASE iAuditStatus
             WHEN 0 THEN
                 '已保存'
             WHEN 1 THEN
                 '待审核'
             WHEN 2 THEN
                 '审核通过'
             WHEN 3 THEN
                 '审核不通过'
    END AS statename from PL_FormUploadM where
    1=1
    #if(iWorkRegionMid)
    and iWorkRegionMid =#para(iWorkRegionMid)
    #end
    #if(iCategoryId)
    and iCategoryId =#para(iCategoryId)
    #end
    #if(dcreatetime)
        AND convert(date,ddate) >= convert(date,#para(dcreatetime))
    #end
    #if(ecreatetime)
        AND convert(date,ddate) <= convert(date,#para(ecreatetime))
    #end
    #if(ccreatename)
     and cCreateName LIKE concat('%',#para(ccreatename),'%')
    #end

#end

#sql("getDatasByIds")
select mom.* from PL_FormUploadM mom
where mom.IsDeleted = '0'
    #if(sqlids)
and mom.iAutoId in (#(sqlids))
#end
#end