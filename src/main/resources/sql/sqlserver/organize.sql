#sql("getOrgList")
SELECT *
FROM dbo.T_Sys_Organize AS a
order by a.organizeName
#end


#sql("getOrgByCode")
SELECT *
FROM dbo.T_Sys_Organize AS a
where 1=1
#if(orgCode)
and a.organizeCode = #para(orgCode)
#end
#end



#sql("getOrgMapByCode")
SELECT *
FROM dbo.T_Sys_Organize AS a
where 1=1
#if(organizeCode)
and a.organizeCode = #para(orgCode)
#end
#end



#sql("getOrgByJboltOrgCode")
SELECT *
FROM dbo.T_Sys_Organize AS a
where 1=1
#if(jboltorgcode)
and a.jboltorgcode = #para(jboltorgcode)
#end
#end






