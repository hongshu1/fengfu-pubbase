#sql("paginateAdminList")
select ebi.iautoid,ebi.iexpenseid,d.cdepcode,d.cdepname,eb.ibudgetyear,eb.ibudgettype,eb.cBeginDate,eb.cenddate,
       bp.cprojectcode,bp.cprojectname,ebi.cbudgetno,bsh.csubjectname chighestsubjectname,bsl.csubjectname clowestsubjectname,ebi.citemname,eb.iAuditStatus,
       ebi.careertype,ebi.isLargeAmountExpense,ebi.cuse,ebi.cmemo,ebi.iprice,ebi.cunit,ebi.iCarryForward,ebi.icreateby,ebi.dcreatetime,
       sum(case when ebid.iyear = eb.iBudgetYear-1 and ebid.imonth = 8 then ebid.iQuantity end) 'previousyearmounthquantity8',
        sum(case when ebid.iyear = eb.iBudgetYear-1 and ebid.imonth = 8 then ebid.iamount end) 'previousyearmounthamount8',
        sum(case when ebid.iyear = eb.iBudgetYear-1 and ebid.imonth = 9 then ebid.iQuantity end) 'previousyearmounthquantity9',
        sum(case when ebid.iyear = eb.iBudgetYear-1 and ebid.imonth = 9 then ebid.iamount end) 'previousyearmounthamount9',
        sum(case when ebid.iyear = eb.iBudgetYear-1 and ebid.imonth = 10 then ebid.iQuantity end) 'previousyearmounthquantity10',
        sum(case when ebid.iyear = eb.iBudgetYear-1 and ebid.imonth = 10 then ebid.iamount end) 'previousyearmounthamount10',
        sum(case when ebid.iyear = eb.iBudgetYear-1 and ebid.imonth = 11 then ebid.iQuantity end) 'previousyearmounthquantity11',
        sum(case when ebid.iyear = eb.iBudgetYear-1 and ebid.imonth = 11 then ebid.iamount end) 'previousyearmounthamount11',
        sum(case when ebid.iyear = eb.iBudgetYear-1 and ebid.imonth = 12 then ebid.iQuantity end) 'previousyearmounthquantity12',
        sum(case when ebid.iyear = eb.iBudgetYear-1 and ebid.imonth = 12 then ebid.iamount end) 'previousyearmounthamount12',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 1 then ebid.iQuantity end) 'nowyearmounthquantity1',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 1 then ebid.iamount end) 'nowyearmounthamount1',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 2 then ebid.iQuantity end) 'nowyearmounthquantity2',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 2 then ebid.iamount end) 'nowyearmounthamount2',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 3 then ebid.iQuantity end) 'nowyearmounthquantity3',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 3 then ebid.iamount end) 'nowyearmounthamount3',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 4 then ebid.iQuantity end) 'nowyearmounthquantity4',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 4 then ebid.iamount end) 'nowyearmounthamount4',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 5 then ebid.iQuantity end) 'nowyearmounthquantity5',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 5 then ebid.iamount end) 'nowyearmounthamount5',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 6 then ebid.iQuantity end) 'nowyearmounthquantity6',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 6 then ebid.iamount end) 'nowyearmounthamount6',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 7 then ebid.iQuantity end) 'nowyearmounthquantity7',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 7 then ebid.iamount end) 'nowyearmounthamount7',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 8 then ebid.iQuantity end) 'nowyearmounthquantity8',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 8 then ebid.iamount end) 'nowyearmounthamount8',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 9 then ebid.iQuantity end) 'nowyearmounthquantity9',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 9 then ebid.iamount end) 'nowyearmounthamount9',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 10 then ebid.iQuantity end) 'nowyearmounthquantity10',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 10 then ebid.iamount end) 'nowyearmounthamount10',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 11 then ebid.iQuantity end) 'nowyearmounthquantity11',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 11 then ebid.iamount end) 'nowyearmounthamount11',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 12 then ebid.iQuantity end) 'nowyearmounthquantity12',
        sum(case when ebid.iyear = eb.iBudgetYear and ebid.imonth = 12 then ebid.iamount end) 'nowyearmounthamount12',
        sum(case when ebid.iyear = eb.iBudgetYear+1 and ebid.imonth = 1 then ebid.iQuantity end) 'nextyearmounthquantity1',
        sum(case when ebid.iyear = eb.iBudgetYear+1 and ebid.imonth = 1 then ebid.iamount end) 'nextyearmounthamount1',
        sum(case when ebid.iyear = eb.iBudgetYear+1 and ebid.imonth = 2 then ebid.iQuantity end) 'nextyearmounthquantity2',
        sum(case when ebid.iyear = eb.iBudgetYear+1 and ebid.imonth = 2 then ebid.iamount end) 'nextyearmounthamount2',
        sum(case when ebid.iyear = eb.iBudgetYear+1 and ebid.imonth = 3 then ebid.iQuantity end) 'nextyearmounthquantity3',
        sum(case when ebid.iyear = eb.iBudgetYear+1 and ebid.imonth = 3 then ebid.iamount end) 'nextyearmounthamount3',
        sum(case when ebid.iyear = eb.iBudgetYear+1 and ebid.imonth = 4 then ebid.iQuantity end) 'nextyearmounthquantity4',
        sum(case when ebid.iyear = eb.iBudgetYear+1 and ebid.imonth = 4 then ebid.iamount end) 'nextyearmounthamount4'
from PL_Expense_Budget_Item ebi
	left join PL_Expense_Budget_ItemD ebid on ebi.iautoid = ebid.iexpenseitemid
    left join PL_Expense_Budget eb on eb.iautoid = ebi.iExpenseId
    left join bd_department d on d.cdepcode = eb.cdepcode
    left join bas_project bp on bp.iautoid = ebi.iprojectid
    left join bas_subjectm bsh on bsh.iAutoId = ebi.iHighestSubjectId
    left join bas_subjectm bsl on bsl.iAutoId = ebi.iLowestSubjectId
where 1=1
    #if(ibudgetyear)
  and eb.ibudgetyear = #(ibudgetyear)
    #end
    #if(cdepcode)
  and eb.cdepcode like concat('#(cdepcode)','%')
    #end
    #if(ibudgettype)
  and eb.ibudgettype = #para(ibudgettype)
    #end
    #if(iauditstatus)
  and eb.iAuditStatus = #para(iauditstatus)
    #end
    #if(careertype)
  and ebi.careerType = #para(careertype)
    #end
    #if(keywords)
  and ebi.cbudgetno like concat('%',#para(keywords),'%')
    #end
    #if(suject)
  and bs.chighestsubjectname like concat('%',#para(suject),'%') or bs.clowestsubjectname like concat('%',#para(suject),'%')
    #end
    #if(citemname)
  and ebi.citemname like concat('%',#para(citemname),'%')
    #end
    #if(cuse)
  and ebi.cuse like concat('%',#para(cuse),'%')
    #end
    #if(cmemo)
  and ebi.cmemo like concat('%',#para(cmemo),'%')
    #end
    #if(islargeamountexpense)
  and ebi.isLargeAmountExpense = #para(islargeamountexpense)
    #end
    #if(ieffectivestatus)
  and eb.ieffectivestatus != #para(ieffectivestatus)
    #end    
	#(getDataPermissionSql("eb", "cdepcode")) 
group by
    ebi.iautoid,ebi.iexpenseid,d.cdepcode,d.cdepname,eb.ibudgetyear,eb.ibudgettype,eb.cbegindate,eb.cenddate,
    bp.cprojectcode,bp.cprojectname,ebi.cbudgetno,bsh.csubjectname,bsl.csubjectname,ebi.citemname,eb.iAuditStatus,
    ebi.careertype,ebi.isLargeAmountExpense,ebi.cuse,ebi.cmemo,ebi.iprice,ebi.cunit,ebi.iCarryForward,ebi.icreateby,ebi.dcreatetime
    #end