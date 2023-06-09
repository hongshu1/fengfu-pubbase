#sql("paginateAdminDatas")
	select pm.* from pl_investment_plan pm
		where 1=1
	#if(cdepcode)
		and pm.cdepcode = #para(cdepcode)
	#end
	#if(ibudgetyear)
		and pm.ibudgetyear = #(ibudgetyear)
	#end
	#if(ibudgettype)
		and pm.ibudgettype = #para(ibudgettype)
	#end
	#if(iauditstatus)
		and pm.iauditstatus in (#(iauditstatus))
	#end
	### 超级管理员不过滤权限部门
	#if(!isSystemAdmin)
	    ### 存在角色部门配置过滤处理
	    #if(accessCdepCodes && accessCdepCodes.size() > 0)
	        AND pm.cDepCode IN (
	            #for(code:accessCdepCodes)
	                '#(code)' #(for.last?'':',')
	            #end
	        )
	    #end
	#end
	order by pm.iautoid desc
#end

#sql("findExportInvestmentPlanDatas")
select pi.iautoid,pi.iinvestmenttype,pi.iprojectid,bp.cprojectcode,bp.cprojectname,pi.cproductline,
	pi.cmodelinvccode,pi.cparts,pi.icareertype,pi.iinvestmentdistinction,pi.cplanno,pi.citemname,
	pi.isimport,pi.iquantity,pi.cunit,pi.cassettype,pi.cpurpose,pi.ceffectamount,pi.creclaimyear,
	pi.clevel,pi.ispriorreport,pi.cpaymentprogress,(pi.itaxrate*100) itaxrate,pi.iTotalAmountPlan,pi.iTotalAmountActual,
	(pi.iTotalAmountPlan-pi.iTotalAmountActual) iTotalAmountDiff,pi.iTotalAmountDiffReason,
	pi.iYearTotalAmountPlan,pi.iYearTotalAmountActual,
	(pi.iYearTotalAmountPlan-pi.iYearTotalAmountActual) iYearTotalAmountDiff,pi.iYearTotalAmountDiffReason,
	pi.cedittype,pi.cmemo,pi.iitemyear,
	min(case iperiodnum when 1 then cperiodprogress end) cperiodprogress1,
	min(case iperiodnum when 1 then concat(year(dperioddate),'-',month(dperioddate))  end) dperioddate1,
	sum(case iperiodnum when 1 then iamount  end) iamount1,
	min(case iperiodnum when 2 then cperiodprogress end) cperiodprogress2,
	min(case iperiodnum when 2 then concat(year(dperioddate),'-',month(dperioddate))  end) dperioddate2,
	sum(case iperiodnum when 2 then iamount  end) iamount2,
	min(case iperiodnum when 3 then cperiodprogress end) cperiodprogress3,
	min(case iperiodnum when 3 then concat(year(dperioddate),'-',month(dperioddate))  end) dperioddate3,
	sum(case iperiodnum when 3 then iamount  end) iamount3,
	min(case iperiodnum when 4 then cperiodprogress end) cperiodprogress4,
	min(case iperiodnum when 4 then concat(year(dperioddate),'-',month(dperioddate))  end) dperioddate4,
	sum(case iperiodnum when 4 then iamount  end) iamount4,
	min(case iperiodnum when 5 then cperiodprogress end) cperiodprogress5,
	min(case iperiodnum when 5 then concat(year(dperioddate),'-',month(dperioddate))  end) dperioddate5,
	sum(case iperiodnum when 5 then iamount  end) iamount5,
	min(case iperiodnum when 6 then cperiodprogress end) cperiodprogress6,
	min(case iperiodnum when 6 then concat(year(dperioddate),'-',month(dperioddate))  end) dperioddate6,
	sum(case iperiodnum when 6 then iamount  end) iamount6
from PL_Investment_Plan_Item pi
left join pl_investment_Plan_itemd pid on pi.iautoid = pid.iplanitemid
left join bas_project bp on pi.iprojectid = bp.iautoid
where 1=1
#if(iplanid)
	and pi.iplanid = #para(iplanid)
#end
group by pi.iautoid,pi.iinvestmenttype,pi.iprojectid,bp.cprojectcode,bp.cprojectname,pi.cproductline,
	pi.cmodelinvccode,pi.cparts,pi.icareertype,pi.iinvestmentdistinction,pi.cplanno,pi.citemname,
	pi.isimport,pi.iquantity,pi.cunit,pi.cassettype,pi.cpurpose,pi.ceffectamount,pi.creclaimyear,
	pi.clevel,pi.ispriorreport,pi.cpaymentprogress,pi.itaxrate,pi.iTotalAmountPlan,pi.iTotalAmountActual,pi.iTotalAmountDiffReason,
	pi.iYearTotalAmountPlan,pi.iYearTotalAmountActual,pi.iYearTotalAmountDiffReason,
	pi.cedittype,pi.cmemo,pi.iitemyear,pi.iTotalAmountPlan-pi.iTotalAmountActual,pi.iYearTotalAmountPlan-pi.iYearTotalAmountActual
#end

#sql("findInvestmentPlanItemForDetail")
select pi.iautoid,pi.iinvestmenttype,pi.iprojectid,bp.cprojectcode,bp.cprojectname,pi.cproductline,
	pi.cmodelinvccode,pi.cparts,pi.icareertype,pi.iinvestmentdistinction,pi.cplanno,pi.citemname,
	pi.isimport,pi.iquantity,pi.cunit,pi.cassettype,pi.cpurpose,pi.ceffectamount,pi.creclaimyear,
	pi.clevel,pi.ispriorreport,pi.cpaymentprogress,(pi.itaxrate*100) itaxrate,pi.iTotalAmountPlan,pi.iTotalAmountActual,
	(pi.iTotalAmountPlan-pi.iTotalAmountActual) iTotalAmountDiff,pi.iTotalAmountDiffReason,
	pi.iYearTotalAmountPlan,pi.iYearTotalAmountActual,
	(pi.iYearTotalAmountPlan-pi.iYearTotalAmountActual) iYearTotalAmountDiff,pi.iYearTotalAmountDiffReason,
	pi.cedittype,pi.cmemo,pi.iitemyear
from PL_Investment_Plan_Item pi
left join bas_project bp on pi.iprojectid = bp.iautoid
where 1=1
#if(investmentplanid)
	and pi.iplanid = #para(investmentplanid)
#end	
#end

#sql("findInvestmentNextPeriodEditExportDownTplData")
	select pi.iautoid,pi.iinvestmenttype,pi.iprojectid,bp.cprojectcode,bp.cprojectname,pi.cproductline,
	pi.cmodelinvccode,pi.cparts,pi.icareertype,pi.iinvestmentdistinction,pi.cplanno,pi.citemname,
	pi.isimport,pi.iquantity,pi.cunit,pi.cassettype,pi.cpurpose,pi.ceffectamount,pi.creclaimyear,
	pi.clevel,pi.ispriorreport,pi.cpaymentprogress,(pi.itaxrate*100) itaxrate,pi.iTotalAmountPlan,pi.iTotalAmountActual,
	(pi.iTotalAmountPlan-pi.iTotalAmountActual) iTotalAmountDiff,pi.iTotalAmountDiffReason,
	pi.iYearTotalAmountPlan,pi.iYearTotalAmountActual,
	(pi.iYearTotalAmountPlan-pi.iYearTotalAmountActual) iYearTotalAmountDiff,pi.iYearTotalAmountDiffReason,
	pi.cedittype,pi.cmemo,pi.iitemyear,
	min(case iperiodnum when 1 then cperiodprogress end) cperiodprogress1,
	min(case iperiodnum when 1 then concat(year(dperioddate),'-',month(dperioddate))  end) dperioddate1,
	sum(case iperiodnum when 1 then iamount  end) iamount1,
	min(case iperiodnum when 2 then cperiodprogress end) cperiodprogress2,
	min(case iperiodnum when 2 then concat(year(dperioddate),'-',month(dperioddate))  end) dperioddate2,
	sum(case iperiodnum when 2 then iamount  end) iamount2,
	min(case iperiodnum when 3 then cperiodprogress end) cperiodprogress3,
	min(case iperiodnum when 3 then concat(year(dperioddate),'-',month(dperioddate))  end) dperioddate3,
	sum(case iperiodnum when 3 then iamount  end) iamount3,
	min(case iperiodnum when 4 then cperiodprogress end) cperiodprogress4,
	min(case iperiodnum when 4 then concat(year(dperioddate),'-',month(dperioddate))  end) dperioddate4,
	sum(case iperiodnum when 4 then iamount  end) iamount4,
	min(case iperiodnum when 5 then cperiodprogress end) cperiodprogress5,
	min(case iperiodnum when 5 then concat(year(dperioddate),'-',month(dperioddate))  end) dperioddate5,
	sum(case iperiodnum when 5 then iamount  end) iamount5,
	min(case iperiodnum when 6 then cperiodprogress end) cperiodprogress6,
	min(case iperiodnum when 6 then concat(year(dperioddate),'-',month(dperioddate))  end) dperioddate6,
	sum(case iperiodnum when 6 then iamount  end) iamount6
from  bas_project_card pc 
left join PL_Investment_Plan_Item pi on pi.cPlanNo = pc.cCode
left join pl_investment_plan p on pi.iPlanId = p.iautoid
left join pl_investment_Plan_itemd pid on pi.iautoid = pid.iplanitemid
left join bas_project bp on pi.iprojectid = bp.iautoid
where 1=1
#if(ibudgettype)
	and p.ibudgettype = #para(ibudgettype)
#end
#if(ibudgetyear)
	and p.ibudgetyear <= #para(ibudgetyear)
#end
group by pi.iautoid,pi.iinvestmenttype,pi.iprojectid,bp.cprojectcode,bp.cprojectname,pi.cproductline,
	pi.cmodelinvccode,pi.cparts,pi.icareertype,pi.iinvestmentdistinction,pi.cplanno,pi.citemname,
	pi.isimport,pi.iquantity,pi.cunit,pi.cassettype,pi.cpurpose,pi.ceffectamount,pi.creclaimyear,
	pi.clevel,pi.ispriorreport,pi.cpaymentprogress,pi.itaxrate,pi.iTotalAmountPlan,pi.iTotalAmountActual,pi.iTotalAmountDiffReason,
	pi.iYearTotalAmountPlan,pi.iYearTotalAmountActual,pi.iYearTotalAmountDiffReason,
	pi.cedittype,pi.cmemo,pi.iitemyear,pi.iTotalAmountPlan-pi.iTotalAmountActual,pi.iYearTotalAmountPlan-pi.iYearTotalAmountActual
#end

#sql("appendItemIndexDatas")
select * from (
	select a.iautoid,1 iservicetype,b.cdepcode,b.ibudgettype,b.ibudgetyear,
a.cbudgetno,a.cmemo,a.iseffective,a.icreateby,a.dcreatetime 
	from pl_expense_budget_item a
	inner join pl_expense_budget b on a.iexpenseid = b.iautoid
	where 1=1
	#if(isscheduled)
		and a.isscheduled = #para(isscheduled)
	#end
	union all 
	select a.iautoid,2 iservicetype,b.cdepcode,b.ibudgettype,b.ibudgetyear,
a.cplanno cbudgetno,a.cmemo,a.iseffective,a.icreateby,a.dcreatetime 
	from pl_investment_plan_item a
	inner join pl_investment_plan b on a.iplanid = b.iautoid
where 1=1
	#if(isscheduled)
		and a.isscheduled = #para(isscheduled)
	#end
) T 
#end

#sql("findAppendExpenseBudgetItemDetailData")
	select ebi.iautoid,ebi.cbudgetno,bs.chighestsubjectname,bs.clowestsubjectname,
		ebi.citemname,ebi.careertype,ebi.islargeamountexpense,ebi.cuse,ebi.cmemo,ebi.iprice,ebi.cunit,
		ebi.chighestsubjectcode,ebi.clowestsubjectcode
	from pl_expense_budget_item ebi 
		left join bas_subject bs on bs.chighestsubjectcode = ebi.chighestsubjectcode and bs.clowestsubjectcode = ebi.clowestsubjectcode 
	where 1=1
		#if(iexpensebudgetitemid)
			and ebi.iautoid = #para(iexpensebudgetitemid)
		#end
#end

#sql("findAppendInvestmentPlanItemDetailData")
select pi.iautoid,pi.iinvestmenttype,pi.cproductline,
	pi.cmodelinvccode,pi.cparts,pi.icareertype,pi.iinvestmentdistinction,pi.cplanno,pi.citemname,
	pi.isimport,pi.iquantity,pi.cunit,pi.cassettype,pi.cpurpose,pi.ceffectamount,pi.creclaimyear,
	pi.clevel,pi.ispriorreport,pi.cpaymentprogress,(pi.itaxrate*100) itaxrate,pi.iTotalAmountPlan,pi.iTotalAmountActual,
	(pi.iTotalAmountPlan-pi.iTotalAmountActual) iTotalAmountDiff,pi.iTotalAmountDiffReason,
	pi.iYearTotalAmountPlan,pi.iYearTotalAmountActual,
	(pi.iYearTotalAmountPlan-pi.iYearTotalAmountActual) iYearTotalAmountDiff,pi.iYearTotalAmountDiffReason,
	pi.cedittype,pi.cmemo,pi.iitemyear
from PL_Investment_Plan_Item pi
where 1=1
#if(iplanitemid)
	and pi.iautoid = #para(iplanitemid)
#end	
#end

#sql("findInvestmentPlanItemDatas")
select pi.iautoid,pi.iinvestmenttype,pi.cproductline,
	pi.cmodelinvccode,pi.cparts,pi.icareertype,pi.iinvestmentdistinction,pi.cplanno,pi.citemname,
	pi.isimport,pi.iquantity,pi.cunit,pi.cassettype,pi.cpurpose,pi.ceffectamount,pi.creclaimyear,pi.citemtype,
	pi.clevel,pi.ispriorreport,pi.cpaymentprogress,pi.itaxrate,pi.itotalamountplan,pi.itotalamountactual,
	(pi.itotalamountplan-pi.itotalamountactual) itotalamountdiff,pi.itotalamountdiffreason,pi.isscheduled,
	pi.iyeartotalamountplan,pi.iyeartotalamountactual,
	(pi.iyeartotalamountplan-pi.iyeartotalamountactual) iyeartotalamountdiff,pi.iyeartotalamountdiffreason,
	pi.cedittype,pi.cmemo,pi.iitemyear,pi.iprojectcardid,
	(select top 1 isfreeze from bas_project_card where ccode = pi.cplanno) isfreeze,
	(select cperiodprogress from pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 1) cperiodprogress1,
	(select dperioddate from pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 1) dperioddate1,
	(select iamount from pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 1) iamount1,
	(select cperiodprogress from pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 2) cperiodprogress2,
	(select dperioddate from pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 2) dperioddate2,
	(select iamount from pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 2) iamount2,
	(select cperiodprogress from pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 3) cperiodprogress3,
	(select dperioddate from pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 3) dperioddate3,
	(select iamount from pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 3) iamount3,
	(select cperiodprogress from pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 4) cperiodprogress4,
	(select dperioddate from pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 4) dperioddate4,
	(select iamount from pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 4) iamount4,
	(select cperiodprogress from pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 5) cperiodprogress5,
	(select dperioddate from pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 5) dperioddate5,
	(select iamount from pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 5) iamount5,
	(select cperiodprogress from pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 6) cperiodprogress6,
	(select dperioddate from pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 6) dperioddate6,
	(select iamount from pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 6) iamount6
from pl_investment_plan_item pi
where 1=1
#if(iplanid)
	and pi.iplanid = #para(iplanid)
#end	
#end

#sql("findUnfinishInvestmentPlanItemDatas")
select 
pi.iautoid,pi.iinvestmenttype,pi.cproductline,
	pi.cmodelinvccode,pi.cparts,pi.icareertype,pi.iinvestmentdistinction,pi.cplanno,pi.citemname,
	pi.isimport,pi.iquantity,pi.cunit,pi.cassettype,pi.cpurpose,pi.ceffectamount,pi.creclaimyear,
	pi.clevel,pi.ispriorreport,pi.cpaymentprogress,pi.itaxrate,
	case when pi.itotalamountactual = 0 or pi.itotalamountactual is null then pi.itotalamountplan else pi.itotalamountactual end itotalamountplan,
	pi.itotalamountactual,(pi.itotalamountplan-pi.itotalamountactual) itotalamountdiff,pi.itotalamountdiffreason,
	case when pi.iyeartotalamountactual = 0 or pi.iyeartotalamountactual is null then pi.iyeartotalamountplan else pi.iyeartotalamountactual end iyeartotalamountplan,
	pi.iyeartotalamountactual,(pi.iyeartotalamountplan-pi.iyeartotalamountactual) iyeartotalamountdiff,pi.iyeartotalamountdiffreason,
	pi.cedittype,pi.cmemo,pi.iitemyear,
	case when dbo.PL_GetInvestmentPlanActualDatasByPeriodNum(pi.cplanno,1,1) is not null then null else  
	(select cperiodprogress from #(getMomdataDbName()).dbo.pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 1) end cperiodprogress1,
	coalesce(dbo.PL_GetInvestmentPlanActualDatasByPeriodNum(pi.cplanno,1,1),
	(select concat(datename(year,dperioddate),'-',datename(month,dperioddate)) from #(getMomdataDbName()).dbo.pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 1)) dperioddate1,
	coalesce(dbo.PL_GetInvestmentPlanActualDatasByPeriodNum(pi.cplanno,1,2),
	(select iamount from #(getMomdataDbName()).dbo.pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 1)) iamount1,
	case when dbo.PL_GetInvestmentPlanActualDatasByPeriodNum(pi.cplanno,2,1) is not null then null else  
	(select cperiodprogress from #(getMomdataDbName()).dbo.pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 2) end cperiodprogress2,
	coalesce(dbo.PL_GetInvestmentPlanActualDatasByPeriodNum(pi.cplanno,2,1),
	(select concat(datename(year,dperioddate),'-',datename(month,dperioddate)) from #(getMomdataDbName()).dbo.pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 2)) dperioddate2,
	coalesce(dbo.PL_GetInvestmentPlanActualDatasByPeriodNum(pi.cplanno,2,2),
	(select iamount from #(getMomdataDbName()).dbo.pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 2)) iamount2,
	case when dbo.PL_GetInvestmentPlanActualDatasByPeriodNum(pi.cplanno,3,1) is not null then null else  
	(select cperiodprogress from #(getMomdataDbName()).dbo.pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 3) end cperiodprogress3,
	coalesce(dbo.PL_GetInvestmentPlanActualDatasByPeriodNum(pi.cplanno,3,1),
	(select concat(datename(year,dperioddate),'-',datename(month,dperioddate)) from #(getMomdataDbName()).dbo.pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 3)) dperioddate3,
	coalesce(dbo.PL_GetInvestmentPlanActualDatasByPeriodNum(pi.cplanno,3,2),
	(select iamount from #(getMomdataDbName()).dbo.pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 3)) iamount3,
	case when dbo.PL_GetInvestmentPlanActualDatasByPeriodNum(pi.cplanno,4,1) is not null then null else  
	(select cperiodprogress from #(getMomdataDbName()).dbo.pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 4) end cperiodprogress4,
	coalesce(dbo.PL_GetInvestmentPlanActualDatasByPeriodNum(pi.cplanno,4,1),
	(select concat(datename(year,dperioddate),'-',datename(month,dperioddate)) from #(getMomdataDbName()).dbo.pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 4)) dperioddate4,
	coalesce(dbo.PL_GetInvestmentPlanActualDatasByPeriodNum(pi.cplanno,4,2),
	(select iamount from #(getMomdataDbName()).dbo.pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 4)) iamount4,
	case when dbo.PL_GetInvestmentPlanActualDatasByPeriodNum(pi.cplanno,5,1) is not null then null else  
	(select cperiodprogress from #(getMomdataDbName()).dbo.pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 5) end cperiodprogress5,
	coalesce(dbo.PL_GetInvestmentPlanActualDatasByPeriodNum(pi.cplanno,5,1),
	(select concat(datename(year,dperioddate),'-',datename(month,dperioddate)) from #(getMomdataDbName()).dbo.pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 5)) dperioddate5,
	coalesce(dbo.PL_GetInvestmentPlanActualDatasByPeriodNum(pi.cplanno,5,2),
	(select iamount from #(getMomdataDbName()).dbo.pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 5)) iamount5,
	case when dbo.PL_GetInvestmentPlanActualDatasByPeriodNum(pi.cplanno,6,1) is not null then null else  
	(select cperiodprogress from #(getMomdataDbName()).dbo.pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 6) end cperiodprogress6,
	coalesce(dbo.PL_GetInvestmentPlanActualDatasByPeriodNum(pi.cplanno,6,1),
	(select concat(datename(year,dperioddate),'-',datename(month,dperioddate)) from #(getMomdataDbName()).dbo.pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 6)) dperioddate6,
	coalesce(dbo.PL_GetInvestmentPlanActualDatasByPeriodNum(pi.cplanno,6,2),
	(select iamount from #(getMomdataDbName()).dbo.pl_investment_plan_itemd pid where pid.iplanitemid = pi.iautoid and iperiodnum = 6)) iamount6
from #(getMomdataDbName()).dbo.pl_investment_plan_item pi 
	inner join #(getMomdataDbName()).dbo.pl_investment_plan p on pi.iplanid = p.iautoid
	where 1=1
	#if(iplanid)
		and p.iautoid = #para(iplanid)
	#end
	and exists (
		select 1 from #(getMomdataDbName()).dbo.bas_project_card pc where pc.ccode = pi.cplanno 
		#if(istatus)
			and istatus = #para(istatus)
		#end
		#if(iservicetype)
			and iservicetype = #para(iservicetype)
		#end
	)
#end

#sql("findBudgetActualDifferenceDatas")
EXEC	[dbo].[P_InvestmentPlanBudgetActualDifference]
		@iBudgetYear = #(ibudgetyear),
		@cDepCode = #para(cdepcode),
		@investmentdiffbudgettype1 = #para(investmentdiffbudgettype1),
		@investmentdiffbudgettype2 = #para(investmentdiffbudgettype2),
		@u8DbName = #para(u8dbname)
#end

#sql("findInvestmentPlanGroupSummaryDatas")
EXEC	[dbo].[P_InvestmentPlanGroupSummary]
		@cGroupKey = #para(cgroupkey),
		@iBudgetYear = #(ibudgetyear),
		@u8DbName = #para(u8dbname)
#end


#sql("findInvestmentPlanItemSituationDatas")
select * from (
select d.cdepname,ip.iBudgetType,ip.cdepcode,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'iInvestmentType') iInvestmentType,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'iCareerType') iCareerType,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'iInvestmentDistinction') iInvestmentDistinction,
	ipi.cplanno,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'cItemName') cItemName,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'cEditType') cEditType,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'iQuantity') iQuantity,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'cUnit') cUnit,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'cAssetType') cAssetType,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'iItemYear') iItemYear,
	convert(int,#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'isScheduled')) isScheduled,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'iTotalAmountPlan') iTotalAmountPlan,
	1 ibudgettype1,ipi.citemtype,
	concat(ip.ibudgetyear,'年',jd_ibt.name) cbudgettypedesc,
	null previousamounttotal,
	null afteramounttotal
	#if(monthlist && monthlist.size() > 0)
		#for(monthnum:monthlist)
			,min(case when datepart(year,dateadd(month,#(for.index),convert(date,#para(dstartdate)))) = datepart(year,ipid.dPeriodDate) and
			datepart(month,dateadd(month,#(for.index),convert(date,#para(dstartdate)))) = datepart(month,ipid.dPeriodDate)
			then ipid.cperiodprogress end) 'imonthamount#(monthnum)'			
		#end
	#end
	#if(yearlist && yearlist.size() > 0)
		#for(yearnum:yearlist)
			,null 'itotal#(yearnum)'
		#end
	#end
from #(getMomdataDbName()).dbo.pl_investment_plan_item ipi 
	inner join #(getMomdataDbName()).dbo.pl_investment_plan ip ON ipi.iplanid=ip.iautoid 
	left JOIN #(getMomdataDbName()).dbo.pl_investment_plan_itemd ipid ON ipid.iplanitemid=ipi.iautoid 
	left join #(getBaseDbName()).dbo.jb_dictionary jd_ibt on jd_ibt.sn=ip.ibudgettype and jd_ibt.type_key='investment_budget_type'
	left join #(getMomdataDbName()).dbo.bd_department d on d.cdepcode = ip.cdepcode
WHERE 1=1 and ip.iEffectiveStatus != 3 and convert(date,ipid.dPeriodDate) >= convert(date,#para(dstartdate)) and convert(date,ipid.dPeriodDate) <= convert(date,#para(denddate))
	GROUP BY ip.cdepcode,d.cdepname,ip.ibudgetyear,ip.iBudgetType,ipi.cplanno,ipi.citemtype,jd_ibt.name
	union all
	select d.cdepname,ip.iBudgetType,ip.cdepcode,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'iInvestmentType') iInvestmentType,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'iCareerType') iCareerType,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'iInvestmentDistinction') iInvestmentDistinction,
	ipi.cplanno,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'cItemName') cItemName,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'cEditType') cEditType,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'iQuantity') iQuantity,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'cUnit') cUnit,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'cAssetType') cAssetType,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'iItemYear') iItemYear,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'isScheduled') isScheduled,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'iTotalAmountPlan') iTotalAmountPlan,
	2 ibudgettype1,ipi.citemtype,
	concat(ip.ibudgetyear,'年',jd_ibt.name) cbudgettypedesc,
	convert(varchar(20),sum(isnull(case when ipid.dPeriodDate < convert(date,#para(dstartdate)) then ipid.iamount end,0))) previousamounttotal,
	convert(varchar(20),sum(isnull(case when ipid.dPeriodDate > convert(date,#para(denddate)) then ipid.iamount end,0))) afteramounttotal
	#if(monthlist && monthlist.size() > 0)
		#for(monthnum:monthlist)
			,convert(varchar(20),sum(isnull(case when datepart(year,dateadd(month,#(for.index),convert(date,#para(dstartdate)))) = datepart(year,ipid.dPeriodDate) and
			datepart(month,dateadd(month,#(for.index),convert(date,#para(dstartdate)))) = datepart(month,ipid.dPeriodDate)
			then ipid.iamount end,0))) 'imonthamount#(monthnum)'			
		#end
	#end
	#if(yearlist && yearlist.size() > 0)
		#for(yearnum:yearlist)
			,convert(varchar(20),sum(isnull(case when datepart(year,ipid.dPeriodDate) = datepart(year,dateadd(year,#(for.index),convert(date,#para(dstartdate)))) then ipid.iamount end,0))) 'itotal#(yearnum)'
		#end
	#end
from #(getMomdataDbName()).dbo.pl_investment_plan_item ipi 
	inner join #(getMomdataDbName()).dbo.pl_investment_plan ip ON ipi.iplanid=ip.iautoid 
	left JOIN #(getMomdataDbName()).dbo.pl_investment_plan_itemd ipid ON ipid.iplanitemid=ipi.iautoid 
	left join #(getBaseDbName()).dbo.jb_dictionary jd_ibt on jd_ibt.sn=ip.ibudgettype and jd_ibt.type_key='investment_budget_type'
	left join #(getMomdataDbName()).dbo.bd_department d on d.cdepcode = ip.cdepcode
WHERE 1=1 and ip.iEffectiveStatus != 3
	GROUP BY ip.cdepcode,d.cdepname,ip.ibudgetyear,ip.iBudgetType,ipi.cplanno,ipi.citemtype,jd_ibt.name
	union all
	select d.cdepname,ip.cdepcode,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'iBudgetType') iBudgetType,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'iInvestmentType') iInvestmentType,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'iCareerType') iCareerType,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'iInvestmentDistinction') iInvestmentDistinction,
	ipi.cplanno,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'cItemName') cItemName,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'cEditType') cEditType,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'iQuantity') iQuantity,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'cUnit') cUnit,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'cAssetType') cAssetType,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'iItemYear') iItemYear,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'isScheduled') isScheduled,
	#(getMomdataDbName()).dbo.PL_GetNewestVersionInvestmentPlanColumnValue(ipi.cplanno,'iTotalAmountPlan') iTotalAmountPlan,
	2 ibudgettype1,'3' citemtype,
	'实绩' cbudgettypedesc,
	convert(varchar(20),sum(dbo.PL_GetInvestmentPlanActualDatasByDateCompare(ipi.cplanno,convert(date,#para(dstartdate)),1))) previousamounttotal,
	convert(varchar(20),sum(dbo.PL_GetInvestmentPlanActualDatasByDateCompare(ipi.cplanno,convert(date,#para(denddate)),2))) afteramounttotal
	#if(monthlist && monthlist.size() > 0)
		#for(monthnum:monthlist)
			,convert(varchar(20),sum(dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ipi.cplanno,datepart(year,dateadd(month,#(for.index),convert(date,#para(dstartdate)))),datepart(month,dateadd(month,#(for.index),convert(date,#para(dstartdate))))))) 'imonthamount#(monthnum)'			
		#end
	#end
	#if(yearlist && yearlist.size() > 0)
		#for(yearnum:yearlist)
			,convert(varchar(20),sum(dbo.PL_GetInvestmentPlanActualDatasByYearAndMonth(ipi.cplanno,datepart(year,dateadd(month,#(for.index),convert(date,#para(dstartdate)))),null))) 'itotal#(yearnum)'
		#end
	#end
from #(getMomdataDbName()).dbo.pl_investment_plan_item ipi 
	inner join #(getMomdataDbName()).dbo.pl_investment_plan ip ON ipi.iplanid=ip.iautoid 
	left join #(getMomdataDbName()).dbo.bd_department d on d.cdepcode = ip.cdepcode
WHERE 1=1 and ip.iEffectiveStatus != 3
	GROUP BY ipi.cplanno,ip.cdepcode,d.cdepname
) T where 1=1
#if(ibudgettype)
	and T.ibudgettype = #para(ibudgettype)
#end
#if(cdepcode)
	and T.cdepcode = #para(cdepcode)
#end
#if(iinvestmenttype)
	and T.iinvestmenttype = #para(iinvestmenttype)
#end
#if(cedittype)
	and T.cedittype = #para(cedittype)
#end
#if(iitemyear)
	and T.iitemyear = #para(iitemyear)
#end
#if(icareertype)
	and T.icareertype = #para(icareertype)
#end
#if(icareertype)
	and T.icareertype = #para(icareertype)
#end
#if(cassettype)
	and T.cassettype = #para(cassettype)
#end
#if(iinvestmentdistinction)
	and T.iinvestmentdistinction = #para(iinvestmentdistinction)
#end
order by cplanno,ibudgettype1,citemtype

#end

#sql("findExecutionProgressTrackingDatas")
select * from (
select eb.cdepcode,1 iservicetype,
	(select top 1 cdepname from bd_department where cdepcode =eb.cdepcode) cdepname,
	eb.ibudgetyear,ebi.cBudgetNo ccode,
	isnull((select istatus from Bas_Project_Card where ccode = ebi.cBudgetNo and iservicetype = 1),1) finishstatus,
	(select csubjectname from bas_subjectm where iautoid = min(ebi.iLowestSubjectId)) clowestsubjectname,
	min(ebi.citemname) citemname,
	min(ebi.isScheduled) isScheduled,
	(
		select sum(iquantity) from PL_Expense_Budget_ItemD where iexpenseitemid = min(case when eb.ibudgettype = 1 then ebi.iautoid end)
	) fullyearbudgetQuantity,
	'千元' fullyearbudgetUnit,
	(
		select sum(iamount) from PL_Expense_Budget_ItemD where iexpenseitemid = min(case when eb.ibudgettype = 1 then ebi.iautoid end)
	) fullyearbudgetAmount,
	(
		select sum(iquantity) from PL_Expense_Budget_ItemD where iexpenseitemid = min(case when eb.ibudgettype = 2 then ebi.iautoid end)
	) nextperiodbudgetQuantity,
	'千元' nextperiodbudgetUnit,
	(
		select sum(iamount) from PL_Expense_Budget_ItemD where iexpenseitemid = min(case when eb.ibudgettype = 2 then ebi.iautoid end)
	) nextperiodbudgetAmount,
	null monthplanQuantity,
	null monthplanUnit,
	null monthplanAmount,
	sum(dbo.PL_GetProposalForProgressTracking(1,ebi.iautoid,1)) proposalNatmoney,
	sum(dbo.PL_GetProposalForProgressTracking(1,ebi.iautoid,2)) proposalNatsum,
	sum(dbo.PL_GetPurchaseForProgressTracking(1,ebi.iautoid,1)) purchaseNatmoney,
	sum(dbo.PL_GetPurchaseForProgressTracking(1,ebi.iautoid,2)) purchaseNatsum,
	sum(#(u8dbname).dbo.PL_GetPoDetailForProgressTracking(1,ebi.iautoid,1)) podetailsNatmoney,
	sum(#(u8dbname).dbo.PL_GetPoDetailForProgressTracking(1,ebi.iautoid,2)) podetailsNatsum,
	null contractNatmoney,
	null contractNatsum,
	(sum(dbo.PL_GetPurchaseForProgressTracking(1,ebi.iautoid,2)) - sum(dbo.PL_GetProposalForProgressTracking(1,ebi.iautoid,2))) diffNatsum,
	concat(convert(decimal(20,2),isnull(sum(dbo.PL_GetProposalForProgressTracking(1,ebi.iautoid,2)) / sum(dbo.PL_GetPurchaseForProgressTracking(1,ebi.iautoid,2)) * 100,0)),'%') diffratio,
	sum(#(u8dbname).dbo.PL_GetRdRecordsForProgressTracking(1,ebi.iautoid)) RdRecordsNatsum,
	sum(#(u8dbname).dbo.PL_GetPurbillvouchsForProgressTracking(1,ebi.iautoid)) purbillvouchsNatsum,
	null actualpayamount,
	null payremain
from PL_Expense_Budget_Item ebi
	inner join PL_Expense_Budget eb on eb.iautoid = ebi.iexpenseid
		where eb.iEffectiveStatus != 4
	group by eb.cdepcode,eb.ibudgetyear,ebi.cBudgetNo
union all
select ip.cdepcode,2 iservicetype,
	(select top 1 cdepname from bd_department where cdepcode =ip.cdepcode) cdepname,
	ip.ibudgetyear,ipi.cplanno ccode,
	isnull((select istatus from Bas_Project_Card where ccode = ipi.cplanno and iservicetype = 2),1) finishstatus,
	null clowestsubjectname,min(ipi.citemname) citemname,min(ipi.isScheduled) isScheduled,
	sum(case when ip.ibudgettype = 1 then ipi.iquantity end) fullyearbudgetQuantity,
	'千元' fullyearbudgetUnit,
	(
		select sum(iamount) from pl_investment_Plan_itemd where iPlanItemId = min(case when ip.ibudgettype = 1 then ipi.iautoid end)
	) fullyearbudgetAmount,
	sum(case when ip.ibudgettype = 2 then ipi.iquantity end) nextperiodbudgetQuantity,
	'千元' nextperiodbudgetUnit,
	(
		select sum(iamount) from pl_investment_Plan_itemd where iPlanItemId = min(case when ip.ibudgettype = 2 then ipi.iautoid end)
	) nextperiodbudgetAmount,
	null monthplanQuantity,
	null monthplanUnit,
	null monthplanAmount,
	sum(dbo.PL_GetProposalForProgressTracking(2,ipi.iautoid,1)) proposalNatmoney,
	sum(dbo.PL_GetProposalForProgressTracking(2,ipi.iautoid,2)) proposalNatsum,
	sum(dbo.PL_GetPurchaseForProgressTracking(2,ipi.iautoid,1)) purchaseNatmoney,
	sum(dbo.PL_GetPurchaseForProgressTracking(2,ipi.iautoid,2)) purchaseNatsum,
	sum(#(u8dbname).dbo.PL_GetPoDetailForProgressTracking(2,ipi.iautoid,1)) podetailsNatmoney,
	sum(#(u8dbname).dbo.PL_GetPoDetailForProgressTracking(2,ipi.iautoid,2)) podetailsNatsum,
	null contractNatmoney,
	null contractNatsum,
	(sum(dbo.PL_GetPurchaseForProgressTracking(2,ipi.iautoid,2)) - sum(dbo.PL_GetProposalForProgressTracking(2,ipi.iautoid,2))) diffNatsum,
	concat(convert(decimal(20,2),isnull(sum(dbo.PL_GetProposalForProgressTracking(2,ipi.iautoid,2)) / sum(dbo.PL_GetPurchaseForProgressTracking(2,ipi.iautoid,2)) * 100,0)),'%') diffratio,
	sum(#(u8dbname).dbo.PL_GetRdRecordsForProgressTracking(2,ipi.iautoid)) RdRecordsNatsum,
	sum(#(u8dbname).dbo.PL_GetPurbillvouchsForProgressTracking(2,ipi.iautoid)) purbillvouchsNatsum,
	null actualpayamount,
	null payremain
from pl_investment_plan_item ipi
	inner join pl_investment_plan ip on ip.iautoid = ipi.iplanid
		where ip.iEffectiveStatus != 4
	group by ip.cdepcode,ip.ibudgetyear,ipi.cplanno
) T where 1=1
#if(iservicetype)
	and iservicetype = #para(iservicetype)
#end
#if(ibudgetyear)
	and ibudgetyear = #para(ibudgetyear)
#end
#if(cdepcode)
	and cdepcode = #para(cdepcode)
#end
#if(ccode)
	and ccode like concat('%',#para(ccode),'%')
#end
#if(clowestsubject)
	and clowestsubjectname like concat('%',#para(clowestsubject),'%') 
#end
#if(citemname)
	and citemname like concat('%',#para(citemname),'%') 
#end
#end