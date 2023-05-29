#sql("paginateAdminDatas")
select *
from Bas_SubjectM
where
1=1
 #if(isdelete)
  	and isDelete=#para(isdelete)
 #end
 #if(keywords)
  	and cSubjectCode like concat('%',#para(keywords),'%') or   cSubjectName like concat('%',#para(keywords),'%')
  #end
 #if(cversion)
 	and cversion =#para(cversion)
 #end

#if(isenabled)
 and isenabled =#para(isenabled)
 #end
 #if(copycondition)
 and iParentId is null
 #end

order by csubjectcode,clevel
#end

#sql("u8MajorSubjectsList")
select
    c.ccode,c.ccode_name
from code c
where
    1=1
  and iyear in ((select top 1 max(iyear)   from code))
  and bend =1
  #if(keywords)
  and ccode like concat('%',#para(keywords),'%') or   ccode_name like concat('%',#para(keywords),'%')
   #end
order by c.ccode
    #end

    #sql("u8getcoding")
select  * from GradeDef
where
        keyword ='code' and  iyear=year(getdate())
    #end
    
#sql("getAutocompleteList")
	select top #(limit) iautoid, csubjectcode clowestsubjectcode,csubjectname clowestsubjectname,
		dbo.PL_GetHighestSubject(iautoid,1) ihighestsubjectid,
		dbo.PL_GetHighestSubject(iautoid,2) chighestsubjectname,
		dbo.PL_GetHighestSubject(iautoid,3) chighestsubjectcode
	from bas_subjectm where 1=1 and isend = 1 and isenabled = '1' and isdelete = 0
		#if(keyword)
			and (csubjectcode like concat('%',#para(keyword),'%') or csubjectname like concat('%',#para(keyword),'%'))
		#end
		#if(iorgid)
			and iorgid = #para(iorgid)
		#end
		#if(isend)
			and isend = #para(isend)
		#end
#end

#sql("highestsubjectAutocomplete")
	select top #(limit) iautoid, csubjectcode,csubjectname from bas_subjectm where clevel = 1 and isend = 0 and isenabled = '1'
		and isdelete = 0
	#if(keyword)
		and (csubjectcode like concat('%',#para(keyword),'%') or csubjectname like concat('%',#para(keyword),'%'))
	#end
	#if(iorgid)
		and iorgid = #para(iorgid)
	#end
#end

#sql("lowestsubjectAutocomplete")
	select * from (
		select top #(limit) iautoid,csubjectcode,csubjectname,dbo.PL_GetHighestSubject(iautoid,1) ihighestsubjectid
				,dbo.PL_GetHighestSubject(iautoid,2) chighestsubjectname
			from bas_subjectm where isend = 1 and isenabled = '1' and isdelete = 0
		#if(keyword)
			and (csubjectcode like concat('%',#para(keyword),'%') or csubjectname like concat('%',#para(keyword),'%'))
		#end
		#if(iorgid)
			and iorgid = #para(iorgid)
		#end
	) T where 1=1 
	#if(ihighestsubjectid)
		and T.ihighestsubjectid = #para(ihighestsubjectid)
	#end
	
#end

#sql("getHighestSubject")
select dbo.PL_GetHighestSubject(#(ilowestsubjectid), 1) ihighestsubjectid
#end

#sql("maxCversion")
select max(Cversion) as Cversion from Bas_SubjectM
#end

#sql("getMaxDatas")
select * from Bas_SubjectM where Cversion=#para(cversion)
#end

#sql("u8SubjectAutocomplete")
select #if(limit) top #(limit) #end
    c.ccode,c.ccode_name
from code c
where
    1=1
  and iyear in ((select top 1 max(iyear)   from code))
  and bend =1
  #if(keywords)
  	and (ccode like concat('%',#para(keywords),'%') or ccode_name like concat('%',#para(keywords),'%'))
  #end
order by c.ccode
#end