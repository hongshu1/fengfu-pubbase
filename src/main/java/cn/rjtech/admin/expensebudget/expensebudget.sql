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
	select ebi.iautoid,ebi.iexpenseid,ebi.cbudgetno,bsh.csubjectcode chighestsubjectcode,bsh.csubjectname chighestsubjectname,bsl.csubjectcode clowestsubjectcode,bsl.csubjectname clowestsubjectname,ebi.citemname,
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
          SELECT
              contrast='①',
              t.cdepcode,
              t.chighestsubjectname,
              t.clowestsubjectname,
              t.ibudgettype,t.iYear,
              isnull(SUM( t.previousyearmounthamount8 ),0) AS previousyearmounthamount8,
              isnull(SUM( t.previousyearmounthamount9 ),0) AS previousyearmounthamount9,
              isnull(SUM( t.previousyearmounthamount10 ),0) AS previousyearmounthamount10,
              isnull(SUM( t.previousyearmounthamount11 ),0) AS previousyearmounthamount11,
              isnull(SUM( t.previousyearmounthamount12 ),0) AS previousyearmounthamount12,
              isnull(SUM( t.nowyearmounthamount1 ),0) AS nowyearmounthamount1,
              isnull(SUM( t.nowyearmounthamount2 ),0) AS nowyearmounthamount2,
              isnull(SUM( t.nowyearmounthamount3 ),0) AS nowyearmounthamount3,
              isnull(SUM( t.nowyearmounthamount4 ),0) AS nowyearmounthamount4,
              isnull(SUM( t.nowyearmounthamount5 ),0) AS nowyearmounthamount5,
              isnull(SUM( t.nowyearmounthamount6 ),0) AS nowyearmounthamount6,
              isnull(SUM( t.nowyearmounthamount7 ),0) AS nowyearmounthamount7,
              isnull(SUM( t.nowyearmounthamount8 ),0) AS nowyearmounthamount8,
              isnull(SUM( t.nowyearmounthamount9 ),0) AS nowyearmounthamount9,
              isnull(SUM( t.nowyearmounthamount10 ),0) AS nowyearmounthamount10,
              isnull(SUM( t.nowyearmounthamount11 ),0) AS nowyearmounthamount11,
              isnull(SUM( t.nowyearmounthamount12 ),0) AS nowyearmounthamount12,
              SUM( t.nextyearmounthamount1 ) AS nextyearmounthamount1,
              SUM( t.nextyearmounthamount2 ) AS nextyearmounthamount2,
              SUM( t.nextyearmounthamount3 ) AS nextyearmounthamount3,
              SUM( t.nextyearmounthamount4 ) AS nextyearmounthamount4
                  FROM
                      (
                          SELECT
                              bsl.csubjectname clowestsubjectname,
                              ebi.iautoid,
                              ebi.iexpenseid,
                              ebid.iExpenseItemId,
                              eb.ibudgettype,
                              eb.cdepcode,
                              ebi.cbudgetno,
                              bsh.csubjectname chighestsubjectname,
                              ebi.icreateby,
                              ebid.iYear,
                              ebid.iMonth,
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear - 1 AND ebid.imonth = 8 THEN ebid.iamount END )/1000) 'previousyearmounthamount8',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear - 1 AND ebid.imonth = 9 THEN ebid.iamount END )/1000) 'previousyearmounthamount9',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear - 1 AND ebid.imonth = 10 THEN ebid.iamount END )/1000) 'previousyearmounthamount10',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear - 1 AND ebid.imonth = 11 THEN ebid.iamount END )/1000) 'previousyearmounthamount11',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear - 1 AND ebid.imonth = 12 THEN ebid.iamount END )/1000) 'previousyearmounthamount12',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 1 THEN ebid.iamount END )/1000) 'nowyearmounthamount1',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 2 THEN ebid.iamount END )/1000) 'nowyearmounthamount2',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 3 THEN ebid.iamount END )/1000) 'nowyearmounthamount3',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 4 THEN ebid.iamount END )/1000) 'nowyearmounthamount4',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 5 THEN ebid.iamount END )/1000) 'nowyearmounthamount5',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 6 THEN ebid.iamount END )/1000) 'nowyearmounthamount6',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 7 THEN ebid.iamount END )/1000) 'nowyearmounthamount7',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 8 THEN ebid.iamount END )/1000) 'nowyearmounthamount8',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 9 THEN ebid.iamount END )/1000) 'nowyearmounthamount9',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 10 THEN ebid.iamount END )/1000) 'nowyearmounthamount10',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 11 THEN ebid.iamount END )/1000) 'nowyearmounthamount11',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 12 THEN ebid.iamount END )/1000) 'nowyearmounthamount12',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear + 1 AND ebid.imonth = 1 THEN ebid.iamount END )/1000) 'nextyearmounthamount1',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear + 1 AND ebid.imonth = 2 THEN ebid.iamount END )/1000) 'nextyearmounthamount2',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear + 1 AND ebid.imonth = 3 THEN ebid.iamount END )/1000) 'nextyearmounthamount3',
                              Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear + 1 AND ebid.imonth = 4 THEN ebid.iamount END )/1000) 'nextyearmounthamount4'
                          FROM
                              #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
                                  LEFT JOIN #(getMomdataDbName()).dbo.PL_Expense_Budget_ItemD ebid ON ebi.iautoid = ebid.iexpenseitemid
                              LEFT JOIN #(getMomdataDbName()).dbo.PL_Expense_Budget eb ON eb.iautoid = ebi.iExpenseId
                              LEFT JOIN #(getMomdataDbName()).dbo.bas_project bp ON bp.iautoid = ebi.iprojectid
                              LEFT JOIN #(getMomdataDbName()).dbo.bas_subjectm bsh ON bsh.iautoid = ebi.ihighestsubjectid
                              LEFT JOIN #(getMomdataDbName()).dbo.bas_subjectm bsl ON bsl.iautoid = ebi.ilowestsubjectid
                          WHERE
                              1 = 1

                            AND (
                              ebid.iYear >= YEAR ( #para(dstarttime1) )
                            AND ebid.iMonth >= MONTH ( #para(dstarttime1))
                             OR (ebid.iYear <= YEAR ( #para(dendtime1) )
                            AND ebid.iMonth <= MONTH ( #para(dendtime1) ))

                              )

                              #if(cdepcode)
                            AND eb.cdepcode = #para(cdepcode)
                              #end
                              #if(ibudgettype)
                            AND  eb.ibudgettype = #para(ibudgettype)
                              #end
                          GROUP BY
                              bsl.csubjectname,
                              ebi.iautoid,
                              ebi.iexpenseid,
                              eb.ibudgettype,
                              eb.cdepcode,
                              ebi.cbudgetno,
                              ebid.iExpenseItemId,
                              ebid.iYear,
                              ebid.iMonth,
                              bsh.csubjectname,
                              ebi.icreateby
                      ) T
                  GROUP BY
                      t.cdepcode,
                      t.chighestsubjectname,
                      t.clowestsubjectname,
                      t.ibudgettype,t.iYear

                  Union all
                  SELECT
                      contrast='②',
                      t.cdepcode,
                      t.chighestsubjectname,
                      t.clowestsubjectname,
                      t.ibudgettype,t.iYear,
                      isnull(SUM( t.previousyearmounthamount8 ),0) AS previousyearmounthamount8,
                      isnull(SUM( t.previousyearmounthamount9 ),0) AS previousyearmounthamount9,
                      isnull(SUM( t.previousyearmounthamount10 ),0) AS previousyearmounthamount10,
                      isnull(SUM( t.previousyearmounthamount11 ),0) AS previousyearmounthamount11,
                      isnull(SUM( t.previousyearmounthamount12 ),0) AS previousyearmounthamount12,
                      isnull(SUM( t.nowyearmounthamount1 ),0) AS nowyearmounthamount1,
                      isnull(SUM( t.nowyearmounthamount2 ),0) AS nowyearmounthamount2,
                      isnull(SUM( t.nowyearmounthamount3 ),0) AS nowyearmounthamount3,
                      isnull(SUM( t.nowyearmounthamount4 ),0) AS nowyearmounthamount4,
                      isnull(SUM( t.nowyearmounthamount5 ),0) AS nowyearmounthamount5,
                      isnull(SUM( t.nowyearmounthamount6 ),0) AS nowyearmounthamount6,
                      isnull(SUM( t.nowyearmounthamount7 ),0) AS nowyearmounthamount7,
                      isnull(SUM( t.nowyearmounthamount8 ),0) AS nowyearmounthamount8,
                      isnull(SUM( t.nowyearmounthamount9 ),0) AS nowyearmounthamount9,
                      isnull(SUM( t.nowyearmounthamount10 ),0) AS nowyearmounthamount10,
                      isnull(SUM( t.nowyearmounthamount11 ),0) AS nowyearmounthamount11,
                      isnull(SUM( t.nowyearmounthamount12 ),0) AS nowyearmounthamount12,
                      SUM( t.nextyearmounthamount1 ) AS nextyearmounthamount1,
                      SUM( t.nextyearmounthamount2 ) AS nextyearmounthamount2,
                      SUM( t.nextyearmounthamount3 ) AS nextyearmounthamount3,
                      SUM( t.nextyearmounthamount4 ) AS nextyearmounthamount4
                  FROM
                      (
               SELECT
                   bsl.csubjectname clowestsubjectname,
                   ebi.iautoid,
                   ebi.iexpenseid,
                   ebid.iExpenseItemId,
                   eb.ibudgettype,
                   eb.cdepcode,
                   ebi.cbudgetno,
                   bsh.csubjectname chighestsubjectname,
                   ebi.icreateby,
                   ebid.iYear,
                   ebid.iMonth,
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear - 1 AND ebid.imonth = 8 THEN ebid.iamount END )/1000) 'previousyearmounthamount8',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear - 1 AND ebid.imonth = 9 THEN ebid.iamount END )/1000) 'previousyearmounthamount9',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear - 1 AND ebid.imonth = 10 THEN ebid.iamount END )/1000) 'previousyearmounthamount10',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear - 1 AND ebid.imonth = 11 THEN ebid.iamount END )/1000) 'previousyearmounthamount11',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear - 1 AND ebid.imonth = 12 THEN ebid.iamount END )/1000) 'previousyearmounthamount12',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 1 THEN ebid.iamount END )/1000) 'nowyearmounthamount1',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 2 THEN ebid.iamount END )/1000) 'nowyearmounthamount2',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 3 THEN ebid.iamount END )/1000) 'nowyearmounthamount3',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 4 THEN ebid.iamount END )/1000) 'nowyearmounthamount4',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 5 THEN ebid.iamount END )/1000) 'nowyearmounthamount5',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 6 THEN ebid.iamount END )/1000) 'nowyearmounthamount6',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 7 THEN ebid.iamount END )/1000) 'nowyearmounthamount7',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 8 THEN ebid.iamount END )/1000) 'nowyearmounthamount8',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 9 THEN ebid.iamount END )/1000) 'nowyearmounthamount9',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 10 THEN ebid.iamount END )/1000) 'nowyearmounthamount10',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 11 THEN ebid.iamount END )/1000) 'nowyearmounthamount11',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 12 THEN ebid.iamount END )/1000) 'nowyearmounthamount12',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear + 1 AND ebid.imonth = 1 THEN ebid.iamount END )/1000) 'nextyearmounthamount1',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear + 1 AND ebid.imonth = 2 THEN ebid.iamount END )/1000) 'nextyearmounthamount2',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear + 1 AND ebid.imonth = 3 THEN ebid.iamount END )/1000) 'nextyearmounthamount3',
                   Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear + 1 AND ebid.imonth = 4 THEN ebid.iamount END )/1000) 'nextyearmounthamount4'
               FROM
                   #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
                              LEFT JOIN #(getMomdataDbName()).dbo.PL_Expense_Budget_ItemD ebid ON ebi.iautoid = ebid.iexpenseitemid
                              LEFT JOIN #(getMomdataDbName()).dbo.PL_Expense_Budget eb ON eb.iautoid = ebi.iExpenseId
                              LEFT JOIN #(getMomdataDbName()).dbo.bas_project bp ON bp.iautoid = ebi.iprojectid
                              LEFT JOIN #(getMomdataDbName()).dbo.bas_subjectm bsh ON bsh.iautoid = ebi.ihighestsubjectid
                              LEFT JOIN #(getMomdataDbName()).dbo.bas_subjectm bsl ON bsl.iautoid = ebi.ilowestsubjectid
                          WHERE
                              1 = 1
                            AND (
                                        ebid.iYear >= YEAR ( #para(dstarttime2) )
                                         AND ebid.iMonth >= MONTH ( #para(dstarttime2))
                                         OR (ebid.iYear <= YEAR ( #para(dendtime2) )
                                         AND ebid.iMonth <= MONTH ( #para(dendtime2) ))
                                )

                          #if(cdepcode)
                            AND eb.cdepcode = #para(cdepcode)
                          #end
                          #if(ibudgettype)
                            AND  eb.ibudgettype = #para(ibudgettype)
                          #end
                             GROUP BY
                              bsl.csubjectname,
                              ebi.iautoid,
                              ebi.iexpenseid,
                              eb.ibudgettype,
                              eb.cdepcode,
                              ebi.cbudgetno,
                              ebid.iExpenseItemId,
                              ebid.iYear,
                              ebid.iMonth,
                              bsh.csubjectname,
                              ebi.icreateby
                      ) T
                  GROUP BY
                      t.cdepcode,
                      t.chighestsubjectname,
                      t.clowestsubjectname,
                      t.ibudgettype,t.iYear
          Union all
          SELECT
                  contrast='③',
                  t.cdepcode,
                  t.chighestsubjectname,
                  t.clowestsubjectname,
                  t.ibudgettype,t.iYear,
                  isnull(SUM( t.previousyearmounthamount8 ),0) AS previousyearmounthamount8,
                  isnull(SUM( t.previousyearmounthamount9 ),0) AS previousyearmounthamount9,
                  isnull(SUM( t.previousyearmounthamount10 ),0) AS previousyearmounthamount10,
                  isnull(SUM( t.previousyearmounthamount11 ),0) AS previousyearmounthamount11,
                  isnull(SUM( t.previousyearmounthamount12 ),0) AS previousyearmounthamount12,
                  isnull(SUM( t.nowyearmounthamount1 ),0) AS nowyearmounthamount1,
                  isnull(SUM( t.nowyearmounthamount2 ),0) AS nowyearmounthamount2,
                  isnull(SUM( t.nowyearmounthamount3 ),0) AS nowyearmounthamount3,
                  isnull(SUM( t.nowyearmounthamount4 ),0) AS nowyearmounthamount4,
                  isnull(SUM( t.nowyearmounthamount5 ),0) AS nowyearmounthamount5,
                  isnull(SUM( t.nowyearmounthamount6 ),0) AS nowyearmounthamount6,
                  isnull(SUM( t.nowyearmounthamount7 ),0) AS nowyearmounthamount7,
                  isnull(SUM( t.nowyearmounthamount8 ),0) AS nowyearmounthamount8,
                  isnull(SUM( t.nowyearmounthamount9 ),0) AS nowyearmounthamount9,
                  isnull(SUM( t.nowyearmounthamount10 ),0) AS nowyearmounthamount10,
                  isnull(SUM( t.nowyearmounthamount11 ),0) AS nowyearmounthamount11,
                  isnull(SUM( t.nowyearmounthamount12 ),0) AS nowyearmounthamount12,
                  SUM( t.nextyearmounthamount1 ) AS nextyearmounthamount1,
                  SUM( t.nextyearmounthamount2 ) AS nextyearmounthamount2,
                  SUM( t.nextyearmounthamount3 ) AS nextyearmounthamount3,
                  SUM( t.nextyearmounthamount4 ) AS nextyearmounthamount4
          FROM
              (
                  SELECT
                      bsl.csubjectname clowestsubjectname,
                      ebi.iautoid,
                      ebi.iexpenseid,
                      ebid.iExpenseItemId,
                      eb.ibudgettype,
                      eb.cdepcode,
                      ebi.cbudgetno,
                      bsh.csubjectname chighestsubjectname,
                      ebi.icreateby,
                      ebid.iYear,
                      ebid.iMonth,
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear - 1 AND ebid.imonth = 8 THEN ebid.iamount END )/1000) 'previousyearmounthamount8',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear - 1 AND ebid.imonth = 9 THEN ebid.iamount END )/1000) 'previousyearmounthamount9',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear - 1 AND ebid.imonth = 10 THEN ebid.iamount END )/1000) 'previousyearmounthamount10',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear - 1 AND ebid.imonth = 11 THEN ebid.iamount END )/1000) 'previousyearmounthamount11',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear - 1 AND ebid.imonth = 12 THEN ebid.iamount END )/1000) 'previousyearmounthamount12',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 1 THEN ebid.iamount END )/1000) 'nowyearmounthamount1',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 2 THEN ebid.iamount END )/1000) 'nowyearmounthamount2',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 3 THEN ebid.iamount END )/1000) 'nowyearmounthamount3',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 4 THEN ebid.iamount END )/1000) 'nowyearmounthamount4',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 5 THEN ebid.iamount END )/1000) 'nowyearmounthamount5',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 6 THEN ebid.iamount END )/1000) 'nowyearmounthamount6',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 7 THEN ebid.iamount END )/1000) 'nowyearmounthamount7',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 8 THEN ebid.iamount END )/1000) 'nowyearmounthamount8',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 9 THEN ebid.iamount END )/1000) 'nowyearmounthamount9',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 10 THEN ebid.iamount END )/1000) 'nowyearmounthamount10',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 11 THEN ebid.iamount END )/1000) 'nowyearmounthamount11',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear AND ebid.imonth = 12 THEN ebid.iamount END )/1000) 'nowyearmounthamount12',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear + 1 AND ebid.imonth = 1 THEN ebid.iamount END )/1000) 'nextyearmounthamount1',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear + 1 AND ebid.imonth = 2 THEN ebid.iamount END )/1000) 'nextyearmounthamount2',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear + 1 AND ebid.imonth = 3 THEN ebid.iamount END )/1000) 'nextyearmounthamount3',
                          Convert(decimal(30,5),SUM( CASE WHEN ebid.iyear = eb.iBudgetYear + 1 AND ebid.imonth = 4 THEN ebid.iamount END )/1000) 'nextyearmounthamount4'
                  FROM
                      #(getMomdataDbName()).dbo.PL_Expense_Budget_Item ebi
                              LEFT JOIN #(getMomdataDbName()).dbo.PL_Expense_Budget_ItemD ebid ON ebi.iautoid = ebid.iexpenseitemid
                      LEFT JOIN #(getMomdataDbName()).dbo.PL_Expense_Budget eb ON eb.iautoid = ebi.iExpenseId
                      LEFT JOIN #(getMomdataDbName()).dbo.bas_project bp ON bp.iautoid = ebi.iprojectid
                      LEFT JOIN #(getMomdataDbName()).dbo.bas_subjectm bsh ON bsh.iautoid = ebi.ihighestsubjectid
                      LEFT JOIN #(getMomdataDbName()).dbo.bas_subjectm bsl ON bsl.iautoid = ebi.ilowestsubjectid
                  WHERE
                      1 = 1
                    AND (
                      ebid.iYear >= YEAR ( #para(dstarttime3) )
                    AND ebid.iMonth >= MONTH ( #para(dstarttime3))
                     OR (ebid.iYear <= YEAR ( #para(dendtime3) )
                    AND ebid.iMonth <= MONTH ( #para(dendtime3) ))
                      )

                      #if(cdepcode)
                    AND eb.cdepcode = #para(cdepcode)
                      #end
                      #if(ibudgettype)
                    AND  eb.ibudgettype = #para(ibudgettype)
                      #end
                  GROUP BY
                      bsl.csubjectname,
                      ebi.iautoid,
                      ebi.iexpenseid,
                      eb.ibudgettype,
                      eb.cdepcode,
                      ebi.cbudgetno,
                      ebid.iExpenseItemId,
                      ebid.iYear,
                      ebid.iMonth,
                      bsh.csubjectname,
                      ebi.icreateby
              ) T
          GROUP BY
              t.cdepcode,
              t.chighestsubjectname,
              t.clowestsubjectname,
              t.ibudgettype,t.iYear
              )A
              order  by  A.cdepcode, A.clowestsubjectname,A.iYear,A.ibudgettype
    #end
