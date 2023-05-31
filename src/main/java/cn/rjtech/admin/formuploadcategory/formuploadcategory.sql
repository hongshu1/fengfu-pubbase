#sql("list")
select * from
    PL_FormUploadCategory
        where
              1=1
    #if(q)
        AND (
            cCategoryCode LIKE CONCAT('%', #para(q), '%') OR
            cCategoryName LIKE CONCAT('%', #para(q), '%')
        )
    #end
#end