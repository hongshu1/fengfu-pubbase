#sql("list")
select * from
    PL_FormUploadCategory
        where
    isDeleted = 0
    #if(q)
        AND (
            cCategoryCode LIKE CONCAT('%', #para(q), '%') OR
            cCategoryName LIKE CONCAT('%', #para(q), '%')
        )
    #end
    #if(isEnabled)
    and isEnabled=#para(isEnabled == 'true' ? 1 : 0)
    #end
    #if(cCategoryName)
     and cCategoryName LIKE CONCAT('%', #para(cCategoryName), '%')
    #end
    #if(iWorkRegionMid)
     and iWorkRegionMid=#para(iWorkRegionMid)
    #end
        #if(ids)
        AND iautoid IN #(ids)
    #end
#end