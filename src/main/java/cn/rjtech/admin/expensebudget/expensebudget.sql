#sql("paginateAdminDatas")
	select eb.*
from PL_Expense_Budget eb 
	where 1=1
	#if(ibudgetyear)
		and eb.ibudgetyear = #(ibudgetyear)
	#end
	#if(cdepcode)
		and eb.cdepcode = #para(cdepcode)
	#end
	#if(ibudgettype)
		and eb.ibudgettype = #para(ibudgettype)
	#end
	#if(iauditstatus)
		and eb.iauditstatus in (#(iauditstatus))
	#end
	#if(iorgid)
		and eb.iorgid = #para(iorgid)
	#end
	#(getDataPermissionSql("eb", "cdepcode")) 
	order by eb.iautoid desc
#end


#sql("findExpenseBudgetItemDatas")
	select ebi.iautoid,ebi.iexpenseid,ebi.cbudgetno,bsh.cSubjectName chighestsubjectname,bsl.cSubjectName clowestsubjectname,ebi.citemname,ebi.ihighestsubjectid,ebi.ilowestsubjectid,
	ebi.careertype,ebi.isLargeAmountExpense,ebi.cuse,ebi.cmemo,ebi.iprice,ebi.cunit,ebi.iCarryForward,ebi.icreateby,ebi.dcreatetime,ebi.iamounttotal,ebi.isscheduled,
	iprojectcardid,(select top 1 isfreeze from bas_project_card where ccode = ebi.cbudgetno) isfreeze 
	#if(monthlist && monthlist.size() > 0)
		#for(monthnum:monthlist)
			,sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),eb.cbegindate)) and ebid.imonth = datepart(month,dateadd(month,#(for.index),eb.cbegindate)) then ebid.iQuantity end) 'iquantity#(monthnum)'
			,convert(decimal(12,2),sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),eb.cbegindate)) and ebid.imonth = datepart(month,dateadd(month,#(for.index),eb.cbegindate)) then ebid.iamount/1000 end)) 'iamount#(monthnum)'
		#end
	#end
	#if(yearlist && yearlist.size() > 0)
		#for(yearnum:yearlist)
			,convert(decimal(12,2),sum(case when ebid.iyear = datepart(year,dateadd(year,#(for.index),eb.cbegindate)) then ebid.iamount/1000 end)) 'itotal#(yearnum)'
		#end
	#end
from PL_Expense_Budget_Item ebi
	left join PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join PL_Expense_Budget_ItemD ebid on ebi.iautoid = ebid.iexpenseitemid
	left join bas_subjectm bsh on bsh.iautoid = ebi.ihighestsubjectid
	left join bas_subjectm bsl on bsl.iautoid = ebi.ilowestsubjectid
	where 1=1
	#if(iexpenseid)
		and ebi.iexpenseid = #(iexpenseid)
	#end
	group by 
	ebi.iautoid,ebi.iexpenseid,eb.cbegindate,ebi.cbudgetno,bsh.csubjectname,bsl.csubjectname,ebi.citemname,ebi.ihighestsubjectid,ebi.ilowestsubjectid,
	ebi.careertype,ebi.isLargeAmountExpense,ebi.cuse,ebi.cmemo,ebi.iprice,ebi.cunit,ebi.iCarryForward,ebi.icreateby,
	ebi.dcreatetime,ebi.iamounttotal,ebi.isscheduled,iprojectcardid
#end


#sql("findUnfinishExpenseBudgetItemDatas")
	select ebi.iautoid,ebi.iexpenseid,ebi.cbudgetno,bsh.csubjectname chighestsubjectname,bsl.csubjectname clowestsubjectname,ebi.citemname,ebi.ihighestsubjectid,ebi.ilowestsubjectid,
	ebi.careertype,ebi.isLargeAmountExpense,ebi.cuse,ebi.cmemo,ebi.iprice,ebi.cunit,ebi.iCarryForward,ebi.icreateby,ebi.dcreatetime
	#if(monthlist && monthlist.size() > 0)
		#for(monthnum:monthlist)
			,case when dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),eb.cbegindate)),datepart(month,dateadd(month,#(for.index),eb.cbegindate))) > 0
			then null else
			sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),eb.cbegindate)) and ebid.imonth = datepart(month,dateadd(month,#(for.index),eb.cbegindate)) then ebid.iQuantity end) end 'iquantity#(monthnum)'
			,case when dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),eb.cbegindate)),datepart(month,dateadd(month,#(for.index),eb.cbegindate))) > 0
			then convert(decimal(12,2),dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),eb.cbegindate)),datepart(month,dateadd(month,#(for.index),eb.cbegindate))) / 1000)
			else sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),eb.cbegindate)) and ebid.imonth = datepart(month,dateadd(month,#(for.index),eb.cbegindate)) then convert(decimal(12,2),ebid.iamount/1000) end) end 'iamount#(monthnum)'
		#end
	#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget_ItemD ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.bas_subjectm bsh on bsh.iautoid = ebi.ihighestsubjectid
	left join #(getMomdataDbName()).dbo.bas_subjectm bsl on bsl.iautoid = ebi.ilowestsubjectid
	where 1=1
	#if(iexpenseid)
		and ebi.iexpenseid = #(iexpenseid)
	#end
	#if(iorgid)
		and eb.iorgid = #para(iorgid)
	#end
	and exists (
		select 1 from #(getMomdataDbName()).dbo.bas_project_card pc where pc.ccode = ebi.cbudgetno 
		#if(istatus)
			and istatus = #para(istatus)
		#end
		#if(iservicetype)
			and iservicetype = #para(iservicetype)
		#end
	)
	group by 
	ebi.iautoid,ebi.iexpenseid,eb.cbegindate,ebi.cbudgetno,bsh.csubjectname,bsl.csubjectname,ebi.citemname,ebi.ihighestsubjectid,ebi.ilowestsubjectid,
	ebi.careertype,ebi.isLargeAmountExpense,ebi.cuse,ebi.cmemo,ebi.iprice,ebi.cunit,ebi.iCarryForward,ebi.icreateby,ebi.dcreatetime
#end

#sql("findPreviousPeriodExpenseBudgetItemDatas")
	select ebi.iautoid,ebi.iexpenseid,ebi.cbudgetno,bsh.iautoid ihighestsubjectid,bsh.csubjectcode chighestsubjectcode,bsh.csubjectname chighestsubjectname,
	bsl.iautoid ilowestsubjectid,bsl.csubjectcode clowestsubjectcode,bsl.csubjectname clowestsubjectname,ebi.citemname,
	ebi.careertype,ebi.isLargeAmountExpense,ebi.cuse,ebi.cmemo,ebi.iprice,ebi.cunit,ebi.iCarryForward,ebi.icreateby,ebi.dcreatetime
from PL_Expense_Budget_Item ebi
	left join PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join bas_subjectm bsh on bsh.iautoid = ebi.ihighestsubjectid
	left join bas_subjectm bsl on bsl.iautoid = ebi.ilowestsubjectid
	where 1=1
	#if(iexpenseid)
		and ebi.iexpenseid = #(iexpenseid)
	#end
	#if(iorgid)
		and eb.iorgid = #para(iorgid)
	#end
#end
    
#sql("periodContrastDatas")
select * from (
select chighestsubjectname,clowestsubjectname,ibudgettype,icontrastnum
#if(monthlist && monthlist.size() > 0)
	#for(monthnum:monthlist)
 		,imonthamount#(for.index+1)
 	#end
#end
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	#(for.first ? ",(0" : "") +imonthamount#(for.index+1)
	#(for.last ? ") itotalamount" : "")
#end
#end
from (
 		select null chighestsubjectname,null clowestsubjectname,null ibudgettype,null icontrastnum
 		#if(monthlist && monthlist.size() > 0)
 		#for(monthnum:monthlist)
 		,null 'imonthamount#(for.index+1)'
 		#end
 		#end
#if(ibudgettype1 == '1')
union all
		select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,1 icontrastnum
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime1),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime1),'-01'))
		and ibudgettype = 1
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype1 == '2')
union all		 
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,1 icontrastnum
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime1),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime1),'-01'))
		and ibudgettype = 2
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype1 == '3')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,3 ibudgettype,1 icontrastnum
#if(monthlist && monthlist.size() > 0)		
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime1),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime1),'-01'))	
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end		
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype1 == '4')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,4 ibudgettype,1 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime1),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime1),'-01'))
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end	
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype2 == '1')
union all
		select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,2 icontrastnum
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime2),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime2),'-01'))
		and ibudgettype = 1
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype2 == '2')
union all		 
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,2 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime2),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime2),'-01'))
		and ibudgettype = 2
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end	
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype2 == '3')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,3 ibudgettype,2 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime2),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime2),'-01'))	
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end		
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype2 == '4')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,4 ibudgettype,2 icontrastnum
#if(monthlist && monthlist.size() > 0)		
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime2),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime2),'-01'))
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end		
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype3 == '1')
union all
		select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,3 icontrastnum
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime3),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime3),'-01'))
		and ibudgettype = 1
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype3 == '2')
union all		 
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,3 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime3),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime3),'-01'))
		and ibudgettype = 2
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end	
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype3 == '3')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,3 ibudgettype,3 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime3),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime3),'-01'))	
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end		
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype3 == '4')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,4 ibudgettype,3 icontrastnum
#if(monthlist && monthlist.size() > 0)		
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime3),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime3),'-01'))
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end	
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
) T where icontrastnum is not null
union all
select chighestsubjectname,clowestsubjectname,5 ibudgettype,null icontrastnum
#if(monthlist && monthlist.size() > 0)
	#for(monthnum:monthlist)
 		,isnull(sum(case when icontrastnum = 2 then imonthamount#(for.index+1) end) - sum(case when icontrastnum = 1 then imonthamount#(for.index+1) end),0) imonthamount#(for.index+1)
 	#end
#end
,isnull(sum(case when icontrastnum = 2 then itotalamount end) - sum(case when icontrastnum = 1 then itotalamount end),0) itotalamount
from (
select T.*
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	#(for.first ? ",(0" : "") +imonthamount#(for.index+1)
	#(for.last ? ") itotalamount" : "")
#end
#end
from (
 		select null chighestsubjectname,null clowestsubjectname,null ibudgettype,null icontrastnum
 		#if(monthlist && monthlist.size() > 0)
 		#for(monthnum:monthlist)
 		,null 'imonthamount#(for.index+1)'
 		#end
 		#end
#if(ibudgettype1 == '1')
union all
		select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,1 icontrastnum
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime1),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime1),'-01'))
		and ibudgettype = 1
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype1 == '2')
union all		 
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,1 icontrastnum
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime1),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime1),'-01'))
		and ibudgettype = 2
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end	
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype1 == '3')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,3 ibudgettype,1 icontrastnum
#if(monthlist && monthlist.size() > 0)		
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime1),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime1),'-01'))	
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end		
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype1 == '4')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,4 ibudgettype,1 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime1),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime1),'-01'))
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end	
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype2 == '1')
union all
		select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,2 icontrastnum
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime2),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime2),'-01'))
		and ibudgettype = 1
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype2 == '2')
union all		 
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,2 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime2),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime2),'-01'))
		and ibudgettype = 2
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end		
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype2 == '3')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,3 ibudgettype,2 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime2),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime2),'-01'))	
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end		
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype2 == '4')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,4 ibudgettype,2 icontrastnum
#if(monthlist && monthlist.size() > 0)		
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime2),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime2),'-01'))
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end	
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype3 == '1')
union all
		select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,3 icontrastnum
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime3),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime3),'-01'))
		and ibudgettype = 1
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype3 == '2')
union all		 
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,3 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime3),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime3),'-01'))
		and ibudgettype = 2
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype3 == '3')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,3 ibudgettype,3 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime3),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime3),'-01'))	
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end	
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype3 == '4')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,4 ibudgettype,3 icontrastnum
#if(monthlist && monthlist.size() > 0)		
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime3),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime3),'-01'))
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end		
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
) T where icontrastnum is not null
) contrastdiff5 group by chighestsubjectname,clowestsubjectname
having sum(case when icontrastnum = 2 then 1 end) is not null
		and sum(case when icontrastnum = 1 then 1 end) is not null
union all
select chighestsubjectname,clowestsubjectname,6 ibudgettype,null icontrastnum
#if(monthlist && monthlist.size() > 0)
	#for(monthnum:monthlist)
 		,isnull(sum(case when icontrastnum = 3 then imonthamount#(for.index+1) end) - sum(case when icontrastnum = 1 then imonthamount#(for.index+1) end),0) imonthamount#(for.index+1)
 	#end
#end
,isnull(sum(case when icontrastnum = 3 then itotalamount end) - sum(case when icontrastnum = 1 then itotalamount end),0) itotalamount
from (
select T.*
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	#(for.first ? ",(0" : "") +imonthamount#(for.index+1)
	#(for.last ? ") itotalamount" : "")
#end
#end
from (
 		select null chighestsubjectname,null clowestsubjectname,null ibudgettype,null icontrastnum
 		#if(monthlist && monthlist.size() > 0)
 		#for(monthnum:monthlist)
 		,null 'imonthamount#(for.index+1)'
 		#end
 		#end
#if(ibudgettype1 == '1')
union all
		select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,1 icontrastnum
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime1),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime1),'-01'))
		and ibudgettype = 1
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype1 == '2')
union all		 
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,1 icontrastnum
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime1),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime1),'-01'))
		and ibudgettype = 2
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype1 == '3')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,3 ibudgettype,1 icontrastnum
#if(monthlist && monthlist.size() > 0)		
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime1),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime1),'-01'))	
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end	
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype1 == '4')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,4 ibudgettype,1 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime1),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime1),'-01'))
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end	
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype2 == '1')
union all
		select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,2 icontrastnum
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime2),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime2),'-01'))
		and ibudgettype = 1
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype2 == '2')
union all		 
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,2 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime2),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime2),'-01'))
		and ibudgettype = 2
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end		
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype2 == '3')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,3 ibudgettype,2 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime2),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime2),'-01'))	
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype2 == '4')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,4 ibudgettype,2 icontrastnum
#if(monthlist && monthlist.size() > 0)		
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime2),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime2),'-01'))
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end		
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype3 == '1')
union all
		select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,3 icontrastnum
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime3),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime3),'-01'))
		and ibudgettype = 1
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype3 == '2')
union all		 
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,3 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime3),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime3),'-01'))
		and ibudgettype = 2
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end	
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype3 == '3')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,3 ibudgettype,3 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime3),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime3),'-01'))	
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end	
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype3 == '4')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,4 ibudgettype,3 icontrastnum
#if(monthlist && monthlist.size() > 0)		
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime3),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime3),'-01'))
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end	
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
) T where icontrastnum is not null
) contrastdiff6 group by chighestsubjectname,clowestsubjectname
having sum(case when icontrastnum = 3 then 1 end) is not null
		and sum(case when icontrastnum = 1 then 1 end) is not null	
union all
select chighestsubjectname,clowestsubjectname,7 ibudgettype,null icontrastnum
#if(monthlist && monthlist.size() > 0)
	#for(monthnum:monthlist)
 		,isnull(sum(case when icontrastnum = 3 then imonthamount#(for.index+1) end) - sum(case when icontrastnum = 2 then imonthamount#(for.index+1) end),0) imonthamount#(for.index+1)
 	#end
#end
,isnull(sum(case when icontrastnum = 3 then itotalamount end) - sum(case when icontrastnum = 2 then itotalamount end),0) itotalamount
from (
select T.*
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	#(for.first ? ",(0" : "") +imonthamount#(for.index+1)
	#(for.last ? ") itotalamount" : "")
#end
#end
from (
 		select null chighestsubjectname,null clowestsubjectname,null ibudgettype,null icontrastnum
 		#if(monthlist && monthlist.size() > 0)
 		#for(monthnum:monthlist)
 		,null 'imonthamount#(for.index+1)'
 		#end
 		#end
#if(ibudgettype1 == '1')
union all
		select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,1 icontrastnum
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime1),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime1),'-01'))
		and ibudgettype = 1
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype1 == '2')
union all		 
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,1 icontrastnum
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime1),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime1),'-01'))
		and ibudgettype = 2
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end	
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype1 == '3')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,3 ibudgettype,1 icontrastnum
#if(monthlist && monthlist.size() > 0)		
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime1),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime1),'-01'))	
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end	
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype1 == '4')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,4 ibudgettype,1 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime1),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime1),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime1),'-01'))
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end		
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype2 == '1')
union all
		select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,2 icontrastnum
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime2),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime2),'-01'))
		and ibudgettype = 1
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype2 == '2')
union all		 
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,2 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime2),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime2),'-01'))
		and ibudgettype = 2
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype2 == '3')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,3 ibudgettype,2 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime2),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime2),'-01'))	
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype2 == '4')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,4 ibudgettype,2 icontrastnum
#if(monthlist && monthlist.size() > 0)		
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime2),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime2),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime2),'-01'))
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype3 == '1')
union all
		select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,3 icontrastnum
#if(monthlist && monthlist.size() > 0)
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime3),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime3),'-01'))
		and ibudgettype = 1
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype3 == '2')
union all		 
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,eb.ibudgettype,3 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,isnull(sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))) and ebid.imonth = datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))) then iamount end),0) 'imonthamount#(for.index+1)'
#end
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime3),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime3),'-01'))
		and ibudgettype = 2
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,eb.ibudgettype
#end
#if(ibudgettype3 == '3')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,3 ibudgettype,3 icontrastnum
#if(monthlist && monthlist.size() > 0)	
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime3),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime3),'-01'))	
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
#if(ibudgettype3 == '4')
union all
		 select hsm.csubjectname chighestsubjectname,lsm.csubjectname clowestsubjectname,4 ibudgettype,3 icontrastnum
#if(monthlist && monthlist.size() > 0)		
#for(monthnum:monthlist)
	,dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ebi.cbudgetno,datepart(year,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01')))),datepart(month,dateadd(month,#(for.index),convert(date,concat(#para(dstarttime3),'-01'))))) 'imonthamount#(for.index+1)'
#end	
#end
from #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
	left join #(getMomdataDbName()).dbo.PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join #(getMomdataDbName()).dbo.pl_expense_budget_itemd ebid on ebi.iautoid = ebid.iexpenseitemid
	left join #(getMomdataDbName()).dbo.Bas_SubjectM hsm on hsm.iautoid = ebi.iHighestSubjectId and hsm.IsEnabled = 1
	left join #(getMomdataDbName()).dbo.bas_subjectm lsm on lsm.iautoid = ebi.iLowestSubjectId and lsm.isenabled = 1
		where 1=1 and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) >= convert(date,concat(#para(dstarttime3),'-01'))
		and convert(date,concat(ebid.iyear,'-',ebid.imonth,'-01')) <= convert(date,concat(#para(dendtime3),'-01'))
		#if(cdepcode)
			and cdepcode like concat('#(cdepcode)','%')
		#end
		#if(accesscdepcodes)
			and cdepcode in (#(accesscdepcodes))
		#end		
		 GROUP BY eb.ibudgetyear,ebi.iHighestSubjectId, ebi.iLowestSubjectId,hsm.csubjectname,lsm.csubjectname,ebi.cbudgetno
#end
) T where icontrastnum is not null
) contrastdiff7 group by chighestsubjectname,clowestsubjectname
having sum(case when icontrastnum = 3 then 1 end) is not null
		and sum(case when icontrastnum = 2 then 1 end) is not null
) RS order by clowestsubjectname asc,icontrastnum desc,ibudgettype desc
#end
