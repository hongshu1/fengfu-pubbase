#sql("paginateAdminDatas")
select * from bd_person where 1=1 and isdeleted = 0
	#if(cpsnnum)
		and cpsn_num like concat('%',#para(cpsnnum),'%')
	#end
	#if(cpsnname)
		and cpsn_name like concat('%',#para(cpsnname),'%')
	#end	
	#if(cpsnmobilephone)
		and cpsnmobilephone like concat('%',#para(cpsnmobilephone),'%')
	#end
	#if(jobnumber)
		and jobnumber like concat('%',#para(jobnumber),'%')
	#end
	#if(cecardno)
		and cecardno like concat('%',#para(cecardno),'%')
	#end	
	#if(isenabled)
		and isenabled = #para(isenabled)
	#end
	#if(cdeptnum)
		and cDept_num = #para(cdeptnum)
	#end
	#if(remploystate)
		and remploystate = #para(remploystate)
	#end
	#if(starttime)
		and convert(date,dhiredate) >= convert(date,#para(starttime))
	#end
	#if(endtime)
		and convert(date,dhiredate) <= convert(date,#para(endtime))
	#end
#end