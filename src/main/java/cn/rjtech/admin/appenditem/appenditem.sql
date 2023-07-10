#sql("paginateAdminDatas")
	select iautoid,cdepcode,iservicetype,ibudgettype,ibudgetyear,ccode,cmemo,isscheduled,
		ibudgetmoney,ieffectivestatus,icreateby,dcreatetime
	from pl_append_item where 1=1
	#if(iorgid)
		and iorgid = #(iorgid)
	#end
	#if(cdepcode)
		and cdepcode = #para(cdepcode)
	#end
	#if(iservicetype)
		and iservicetype = #para(iservicetype)
	#end
	#if(ibudgettype)
		and ibudgettype = #para(ibudgettype)
	#end
	#if(ibudgetyear)
		and ibudgetyear = #para(ibudgetyear)
	#end
	#(getDataPermissionSql("", "cdepcode"))
#end

#sql("findForEditById")
	select *,
	(
		select top 1 csubjectname from bas_subjectm b where a.ihighestsubjectid = b.iautoid
	) chighestsubjectname,
	(
		select top 1 csubjectname from bas_subjectm b where a.ilowestsubjectid = b.iautoid
	) clowestsubjectname
	from pl_append_item a
	where iautoid = #para(iautoid)
#end