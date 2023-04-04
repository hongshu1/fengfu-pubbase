#sql("list")
select
    dep.*,
    per.cPsn_Name as cpersonname
from
    Bd_Department dep
    left join Bd_Person per ON per.iAutoId = dep.iDutyUserId
where
    dep.isDeleted = '0'
    #if(orgId)
        and dep.iOrgId = #para(orgId)
    #end
    #if(depCode)
        and dep.cDepCode like concat('%',#para(depCode),'%')
    #end
    #if(depName)
        and dep.cDepName like concat('%',#para(depName),'%')
    #end
    #if(depPerson)
        and per.cPsn_Name like concat('%',#para(depPerson),'%')
    #end
    #if(isEnabled)
        and dep.isEnabled = #para(isEnabled == 'true' ? 1 : 0)
    #end
    #if(isApsInvoled)
        and dep.isApsInvoled = #para(isApsInvoled == 'true' ? 1 : 0)
    #end

    #if(excludeId)
        and dep.iAutoId <> #para(excludeId)
    #end
    #if(id)
        and dep.iAutoId = #para(id)
    #end
    #if(null != sortColumn)
    order by #(sortColumn) #(sortType)
    #end
#end

#sql("checkRepetitionData")
SELECT COUNT(1) FROM  Bd_Department WHERE #(keyField) = #para(value) AND  isDeleted ='1'
#end
