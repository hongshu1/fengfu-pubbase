#sql("findList")
SELECT *
FROM T_Sys_ColumsMap
where 1=1
#if(organizeCode)
AND organizeCode =#para(organizeCode)
#end
#if(sourceTupe)
AND sourceTupe =#para(sourceTupe)
#end
#if(ItemCode)
AND ItemCode =#para(ItemCode)
#end
#end

#sql("vouchTypeF")
SELECT *
FROM T_Sys_VouchTypeF
where 1=1
#if(vouchCode)
AND VouchCode =#para(vouchCode)
#end
order by seq
#end

#sql("vouchType")
SELECT *
FROM T_Sys_VouchType
where 1=1
#if(vouchCode)
AND VouchCode =#para(vouchCode)
#end
#end


#sql("generalQuerySeting")
select AutoID,OrganizeCode,Type,Process,CreatePerson,CreateDate from T_Sys_GeneralQuerySeting where 1=1
#end





