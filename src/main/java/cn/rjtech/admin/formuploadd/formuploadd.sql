#sql("getAdminDatas")
select * from PL_FormUploadD where
     1=1
    and iFormUploadMid =#para(pid)
#end

