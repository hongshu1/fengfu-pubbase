#sql("findExpensemonthadjustmentdDatas")
	select iautoid,cdepcode,chighestsubjectcode,clowestsubjectcode,iamount,cexplanation,
	(
		select top 1 csubjectname from bas_subjectm where csubjectcode = a.chighestsubjectcode
	) chighestsubjectname,
	(
		select top 1 csubjectname from bas_subjectm where csubjectcode = a.clowestsubjectcode
	) clowestsubjectname
	from pl_expense_month_adjustmentd a where 1=1
	#if(iadjustmentmid)
		and iadjustmentmid = #para(iadjustmentmid)
	#end
#end

#sql("paginateAdminDatas")
	select * from pl_expense_month_adjustmentm where 1=1
	#if(dadjustdate)
		and substring(convert(varchar(20),dadjustdate,120), 1, 7) = #para(dadjustdate)
	#end
	#if(dcreatetime)
		and substring(convert(varchar(20),dcreatetime,120), 1, 10) = #para(dcreatetime)
	#end
	#if(ieffectivestatus)
		and ieffectivestatus = #para(ieffectivestatus)
	#end
#end