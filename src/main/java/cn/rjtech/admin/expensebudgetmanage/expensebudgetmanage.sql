#sql("paginateAdminDatas")
	select eb.* from pl_expense_budget eb
	where 1=1
	#if(iorgid)
		and eb.iorgid = #para(iorgid)
	#end	
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
	#if(ieffectivestatus)
		and eb.ieffectivestatus in (#(ieffectivestatus))
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
	order by eb.iautoid desc
#end

#sql("findExpenseBudgetItemForDetail")
	select ebi.iautoid,ebi.cbudgetno,bsh.csubjectname chighestsubjectname,bsl.csubjectname clowestsubjectname,
		ebi.citemname,ebi.careertype,ebi.islargeamountexpense,ebi.cuse,ebi.cmemo,
		ebi.iprice,ebi.cunit,ebi.isscheduled,ebi.iamounttotal
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
	from pl_expense_budget_item ebi
		left join PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
		left join PL_Expense_Budget_ItemD ebid on ebi.iautoid = ebid.iexpenseitemid
		left join bas_subjectm bsh on bsh.csubjectcode = ebi.chighestsubjectcode
		left join bas_subjectm bsl on bsl.csubjectcode = ebi.clowestsubjectcode 
	where 1=1
	#if(expensebudgetid)
		and ebi.iexpenseid = #para(expensebudgetid)
	#end
	group by 
	ebi.iautoid,ebi.iexpenseid,eb.cbegindate,ebi.cbudgetno,bsh.csubjectname,bsl.csubjectname,ebi.citemname,ebi.chighestsubjectcode,ebi.clowestsubjectcode,
	ebi.careertype,ebi.isLargeAmountExpense,ebi.cuse,ebi.cmemo,ebi.iprice,ebi.cunit,ebi.iCarryForward,ebi.icreateby,ebi.dcreatetime,ebi.isscheduled,
	ebi.iamounttotal
#end
