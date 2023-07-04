#sql("list")
select
    dep.*,
    per.cPsn_Name as cpersonname,
    jd.name as typename
from
    Bd_Department dep
    left join Bd_Person per ON per.iAutoId = dep.iDutyUserId
    left join #(getBaseDbName()).dbo.jb_dictionary jd on dep.cType  = jd.sn and jd.type_key = 'org_type'
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
    #if(isProposal)
        and dep.isProposal = #para(isProposal == 'true' ? 1 : 0)
    #end
    #if(idepgrade)
        and dep.iDepGrade = #para(idepgrade)
    #end
    #if(bdepend)
        and dep.bdepend = #para(bdepend)
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
    order by cdepcode ASC
#end

#sql("checkRepetitionData")
SELECT COUNT(1) FROM  Bd_Department WHERE #(keyField) = #para(value) AND  isDeleted ='1'
#end

#sql("getSelectIpid")
select * from Bd_Department where ipid=#para(ipid)
#end


#sql("refreshAllEndGrade")
select * from Bd_Department
#end


#sql ("updateEndGrade")
update Bd_Department set iDepGrade=1 where iAutoId=#para(iautoid)
#end

#sql("refreshAll0")
select * from Bd_Department where ipid=0
#end

#sql("selectByIautoid")
select * from Bd_Department where ipid=#para(ipid)
#end


#sql("findERPPersons")
SELECT * FROM Person where cpersoncode in (#(sqlPersonCode))
#end

#sql("getU8DepByCode")
	select * from department where 1=1
	#if(cdepcode)
		and cdepcode = #para(cdepcode)
	#end
#end