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


#sql("options")
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
    #if(iworkregionmid)
     and iWorkRegionMid=#para(iworkregionmid)
    #end
        #if(ids)
        AND iautoid IN #(ids)
    #end
   and  isEnabled =1
#end

#sql("workregionmOptions")
select
    m.cworkname,
    m.cworkcode,
    m.iautoid
FROM Bd_WorkRegionM m
         join PL_FormUploadCategory f on f.iWorkRegionMid=m.iAutoId
where
    f.isEnabled =1 and f.isDeleted=0
    #if(q)
        AND (
            m.cworkname LIKE CONCAT('%', #para(q), '%') OR
            m.cworkcode LIKE CONCAT('%', #para(q), '%')
        )
    #end
#end