#sql("getAdminDatas")
select * from PL_ProdFormM
where
      1=1
    #if(cworkid)
    and iWorkRegionMid =#para(cworkid)
    #end
    #if(cworkshiftid)
    and iWorkShiftMid =#para(cworkshiftid)
    #end
     #if(iprodformid)
    and iProdFormId =#para(iprodformid)
    #end
     #if(iauditstatus)
    and iAuditStatus =#para(iauditstatus)
    #end
    #if(startdate)
        AND convert(date,dCreateTime) >= convert(date,#para(startdate))
    #end
    #if(enddate)
        AND convert(date,dCreateTime) <= convert(date,#para(enddate))
    #end
#end