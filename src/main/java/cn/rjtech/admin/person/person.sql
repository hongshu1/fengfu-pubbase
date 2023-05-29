#sql("paginateAdminDatas")
select p.*, d.cdepname
from bd_person p
LEFT JOIN Bd_Department d ON p.cdept_num = d.cdepcode AND p.corgcode = '#(corgcode)' AND d.isDeleted = '0'
WHERE p.isDeleted = '0'
	#if(iorgid)
		and p.iorgid = #para(iorgid)
	#end
	#if(cpsnnum)
		and p.cpsn_num like concat('%',#para(cpsnnum),'%')
	#end
	#if(cpsnname)
		and p.cpsn_name like concat('%',#para(cpsnname),'%')
	#end	
	#if(cpsnmobilephone)
		and p.cpsnmobilephone like concat('%',#para(cpsnmobilephone),'%')
	#end
	#if(jobnumber)
		and p.jobnumber like concat('%',#para(jobnumber),'%')
	#end
	#if(cecardno)
		and p.cecardno like concat('%',#para(cecardno),'%')
	#end	
	#if(isenabled)
		and p.isenabled = #para(isenabled)
	#end
	#if(cdeptnum)
		and p.cDept_num = #para(cdeptnum)
	#end
	#if(remploystate)
		and p.remploystate = #para(remploystate)
	#end
	#if(starttime)
		and convert(date,p.dhiredate) >= convert(date,#para(starttime))
	#end
	#if(endtime)
		and convert(date,p.dhiredate) <= convert(date,#para(endtime))
	#end
#end

#sql("findAll")
SELECT
    A.*
FROM
    Bd_Person A
WHERE
    A.isEnabled = '1'
    AND A.isDeleted = '0'
    #if(cpersonname)
        AND A.cPsn_Name LIKE CONCAT('%', #para(cpersonname),'%')
    #end
#end

#sql("getpersonByCpsnnum")
SELECT
	person.*,
	jbuser.username,
	jbuser.name,
	ment.cDepName
FROM
	Bd_Person person
	LEFT JOIN #(getBaseDbName()).dbo.jb_user jbuser ON jbuser.id= person.iUserId
	LEFT JOIN Bd_Department ment ON ment.cDepCode= person.cDept_num
WHERE
	cPsn_Num =#para(cpsnnum)
#end

#sql("getAutocompleteListWithDept")
SELECT TOP #(limit) hp.cpsn_num, hp.cpsn_name, d.cdepcode, d.cdepname
FROM Bd_Person hp
    INNER JOIN bd_Department d ON hp.cdept_num = d.cdepcode
WHERE 1=1
    #if(q)
        AND ( hp.cpsn_num LIKE CONCAT('%', #para(q), '%') OR hp.cpsn_name LIKE CONCAT('%', #para(q), '%') )
    #end
    #if(cdepcode)
    	and d.cdepcode = #para(cdepcode)
    #end
ORDER BY hp.cpsn_num
#end

#sql("getAutocompleteListWithDept")
SELECT TOP #(limit) hp.cpsn_num, hp.cpsn_name, d.cdepcode, d.cdepname
FROM Bd_Person hp
    INNER JOIN bd_Department d ON hp.cdept_num = d.cdepcode
WHERE 1=1
    #if(q)
        AND ( hp.cpsn_num LIKE CONCAT('%', #para(q), '%') OR hp.cpsn_name LIKE CONCAT('%', #para(q), '%') )
    #end
    #if(cdepcode)
    	and d.cdepcode = #para(cdepcode)
    #end
ORDER BY hp.cpsn_num
#end