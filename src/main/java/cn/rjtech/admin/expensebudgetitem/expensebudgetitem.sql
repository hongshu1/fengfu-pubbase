#sql("paginateMdatas")
	select eb.cdepcode,eb.ibudgettype,eb.ibudgetyear,ebi.iautoid,ebi.iexpenseid,ebi.cbudgetno,bsh.cSubjectName chighestsubjectname,
	bsl.cSubjectName clowestsubjectname,ebi.citemname,ebi.ihighestsubjectid,ebi.ilowestsubjectid,
	ebi.careertype,ebi.isLargeAmountExpense,ebi.cuse,ebi.cmemo,ebi.iprice,ebi.cunit,ebi.iCarryForward,ebi.icreateby,ebi.dcreatetime,
	ebi.iamounttotal,ebi.isscheduled
	#if(monthlist && monthlist.size() > 0)
		#for(monthnum:monthlist)
			,sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),eb.cbegindate)) and ebid.imonth = datepart(month,dateadd(month,#(for.index),eb.cbegindate)) then ebid.iQuantity end) 'iquantity#(monthnum)'
			,convert(decimal(12,3),sum(case when ebid.iyear = datepart(year,dateadd(month,#(for.index),eb.cbegindate)) and ebid.imonth = datepart(month,dateadd(month,#(for.index),eb.cbegindate)) then ebid.iamount/1000 end)) 'iamount#(monthnum)'
		#end
	#end
	#if(yearlist && yearlist.size() > 0)
		#for(yearnum:yearlist)
			,convert(decimal(12,3),sum(case when ebid.iyear = datepart(year,dateadd(year,#(for.index),eb.cbegindate)) then ebid.iamount/1000 end)) 'itotal#(yearnum)'
		#end
	#end
from PL_Expense_Budget_Item ebi
	left join PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join PL_Expense_Budget_ItemD ebid on ebi.iautoid = ebid.iexpenseitemid
	left join bas_subjectm bsh on bsh.iautoid = ebi.ihighestsubjectid
	left join bas_subjectm bsl on bsl.iautoid = ebi.ilowestsubjectid
	where 1=1 and eb.iorgid = #para(iorgid)
	and exists (
		select 1 from bas_project_card pc where pc.ccode = ebi.cbudgetno 
		#if(istatus)
			and istatus = #para(istatus)
		#end
		#if(iservicetype)
			and iservicetype = #para(iservicetype)
		#end
		#if(isfreeze)
			and isfreeze = #para(isfreeze)
		#end
	)
	#if(iperiodid)
		and eb.iperiodid = #para(iperiodid)
	#end
	#if(cdepcode)
		and eb.cdepcode = #para(cdepcode)
	#end
	#if(iauditstatus)
		and eb.iauditstatus = #para(iauditstatus)
	#end
	#if(ieffectivestatus)
		and eb.ieffectivestatus = #para(ieffectivestatus)
	#end
	#if(isourceproposalid)
		and not exists (
			select 1 from pl_proposald where iproposalmid = #para(isourceproposalid) and iprojectcardid = ebi.iprojectcardid
		)
	#end
	#if(iprojectcardids)
		and ebi.iprojectcardid not in (#(iprojectcardids))
	#end
### 超级管理员不过滤权限部门
#if(!isSystemAdmin)
    ### 存在角色部门配置过滤处理
    #if(accessCdepCodes && accessCdepCodes.size() > 0)
        AND eb.cDepCode IN (
            #for(code:accessCdepCodes)
                '#(code)' #(for.last?'':',')
            #end
        )
    #end
#end
	group by eb.cdepcode,eb.ibudgettype,eb.ibudgetyear,ebi.iautoid,ebi.iexpenseid,eb.cbegindate,ebi.cbudgetno,bsh.csubjectname,bsl.csubjectname,ebi.citemname,ebi.ihighestsubjectid,ebi.ilowestsubjectid,
ebi.careertype,ebi.isLargeAmountExpense,ebi.cuse,ebi.cmemo,ebi.iprice,ebi.cunit,ebi.iCarryForward,ebi.icreateby,ebi.dcreatetime,ebi.iamounttotal,ebi.isscheduled
#end

#sql("getEdittingBudgetItems")
SELECT it.*
FROM PL_ProposalD d
    INNER JOIN PL_Proposalm m ON d.iproposalmid = m.iautoid
    INNER JOIN PL_Expense_Budget_Item it ON d.isourceid = it.iautoid
WHERE d.isourcetype = #para(isourcetype)
    AND m.iauditstatus IN (0, 3) ### 草稿状态、不通过
    AND d.isourceid IN (#(iexpensebudgetitemids))
#end

#sql("getByProposaldIatuoid")
SELECT it.*
FROM PL_ProposalD d
INNER JOIN PL_Proposalm m ON d.iproposalmid = m.iautoid
INNER JOIN PL_Expense_Budget_Item it ON d.isourceid = it.iautoid
WHERE d.isourcetype = #para(isourcetype)
AND  d.iAutoId = #para(iautoid)
#end

#sql("findNotExistsProjectCardExpenseBudgetItem")
select * from pl_expense_budget_item ebi where 1=1
	#if(iexpenseid)
		and ebi.iexpenseid = #para(iexpenseid)
	#end
	and not exists (
		select 1 from bas_project_card pc where pc.ccode = ebi.cbudgetno
		#if(iservicetype)
			and pc.iservicetype = #para(iservicetype)
		#end
		#if(iorgid)
			and pc.iorgid = #para(iorgid)
		#end
	)

#end

#sql("differencesManagementDatas")
EXEC	[dbo].[P_ExpenseBudgetActualDifference]
		@iBudgetYear = #(ibudgetyear),
		@cDepCode = #para(cdepcode),
		@u8DbName = #para(u8dbname)
#end