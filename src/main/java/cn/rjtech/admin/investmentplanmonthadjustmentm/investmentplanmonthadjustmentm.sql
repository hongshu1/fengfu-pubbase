#sql("paginateAdminDatas")
	select * from PL_InvestmentPlan_Month_Adjustmentm where 1=1 
	#if(dadjustdate)
		and concat(datename(year,dadjustdate),'-',datename(month,dadjustdate)) = #para(dadjustdate)
	#end
#end

#sql("findUnfinishInvestmentPlanItemDatas")
select 
	p.cdepcode,p.ibudgettype,p.ibudgetyear,pi.iautoid iplanid,pi.iinvestmenttype,pi.cproductline,
	pi.cmodelinvccode,pi.cparts,pi.icareertype,pi.iinvestmentdistinction,pi.cplanno,pi.citemname,
	pi.isimport,pi.iquantity,pi.cunit,pi.cassettype,pi.cpurpose,pi.ceffectamount,pi.creclaimyear,
	pi.clevel,pi.ispriorreport,pi.cpaymentprogress,(pi.itaxrate*100) itaxrate,pi.iTotalAmountPlan,pi.iTotalAmountActual,
	(pi.iTotalAmountPlan-pi.iTotalAmountActual) iTotalAmountDiff,pi.iTotalAmountDiffReason,
	pi.iYearTotalAmountPlan,pi.iYearTotalAmountActual,
	(pi.iYearTotalAmountPlan-pi.iYearTotalAmountActual) iYearTotalAmountDiff,pi.iYearTotalAmountDiffReason,
	pi.cedittype,pi.cmemo,pi.iitemyear,
	dbo.PL_GetInvestmentPlanActualDatasForMonthAdjustment(pi.iautoid,1,1) cperiodprogress1,
	dbo.PL_GetInvestmentPlanActualDatasForMonthAdjustment(pi.iautoid,1,2) dperioddate1,
	dbo.PL_GetInvestmentPlanActualDatasForMonthAdjustment(pi.iautoid,1,3) iamount1,
	dbo.PL_GetInvestmentPlanActualDatasForMonthAdjustment(pi.iautoid,2,1) cperiodprogress2,
	dbo.PL_GetInvestmentPlanActualDatasForMonthAdjustment(pi.iautoid,2,2) dperioddate2,
	dbo.PL_GetInvestmentPlanActualDatasForMonthAdjustment(pi.iautoid,2,3) iamount2,
	dbo.PL_GetInvestmentPlanActualDatasForMonthAdjustment(pi.iautoid,3,1) cperiodprogress3,
	dbo.PL_GetInvestmentPlanActualDatasForMonthAdjustment(pi.iautoid,3,2) dperioddate3,
	dbo.PL_GetInvestmentPlanActualDatasForMonthAdjustment(pi.iautoid,3,3) iamount3,
	dbo.PL_GetInvestmentPlanActualDatasForMonthAdjustment(pi.iautoid,4,1) cperiodprogress4,
	dbo.PL_GetInvestmentPlanActualDatasForMonthAdjustment(pi.iautoid,4,2) dperioddate4,
	dbo.PL_GetInvestmentPlanActualDatasForMonthAdjustment(pi.iautoid,4,3) iamount4,
	dbo.PL_GetInvestmentPlanActualDatasForMonthAdjustment(pi.iautoid,5,1) cperiodprogress5,
	dbo.PL_GetInvestmentPlanActualDatasForMonthAdjustment(pi.iautoid,5,2) dperioddate5,
	dbo.PL_GetInvestmentPlanActualDatasForMonthAdjustment(pi.iautoid,5,3) iamount5,
	dbo.PL_GetInvestmentPlanActualDatasForMonthAdjustment(pi.iautoid,6,1) cperiodprogress6,
	dbo.PL_GetInvestmentPlanActualDatasForMonthAdjustment(pi.iautoid,6,2) dperioddate6,
	dbo.PL_GetInvestmentPlanActualDatasForMonthAdjustment(pi.iautoid,6,3) iamount6
from pl_investment_plan_item pi 
	inner join pl_investment_plan p on pi.iplanid = p.iautoid
	where 1=1
	and not exists (
		select 1 from bas_project_card bp where bp.ccode = pi.cplanno and bp.istatus = 2
	)
	#if(ibudgetyear)
		and p.ibudgetyear = #para(ibudgetyear)
	#end
	#if(cdepcode)
		and p.cdepcode = #para(cdepcode)
	#end
	#if(ibudgettype)
		and p.ibudgettype = #para(ibudgettype)
	#end
	#if(cplanno)
		and pi.cplanno = #para(cplanno)
	#end
	#if(citemname)
		and pi.citemname = #para(citemname)
	#end
#end

#sql("findInvestmentPlanAdjustmentItemDatas") 
select 
	p.cdepcode,p.ibudgettype,p.ibudgetyear,pimai.iautoid,pi.iautoid iplanid,pi.iinvestmenttype,pi.cproductline,
	pi.cmodelinvccode,pi.cparts,pi.icareertype,pi.iinvestmentdistinction,pi.cplanno,pi.citemname,
	pi.isimport,pi.iquantity,pi.cunit,pi.cassettype,pi.cpurpose,pi.ceffectamount,pi.creclaimyear,
	pi.clevel,pi.ispriorreport,pi.cpaymentprogress,(pi.itaxrate*100) itaxrate,pi.iTotalAmountPlan,pi.iTotalAmountActual,
	(pi.iTotalAmountPlan-pi.iTotalAmountActual) iTotalAmountDiff,pi.iTotalAmountDiffReason,
	pi.iYearTotalAmountPlan,pi.iYearTotalAmountActual,
	(pi.iYearTotalAmountPlan-pi.iYearTotalAmountActual) iYearTotalAmountDiff,pi.iYearTotalAmountDiffReason,
	pi.cedittype,pi.cmemo,pi.iitemyear,
	(select cperiodprogress from PL_InvestmentPlan_Month_Adjustment_Itemd pimaid where pimaid.iAdjustmentItemId = pimai.iautoid and iperiodnum = 1) cperiodprogress1,
	(select dperioddate from PL_InvestmentPlan_Month_Adjustment_Itemd pimaid where pimaid.iAdjustmentItemId = pimai.iautoid and iperiodnum = 1) dperioddate1,
	(select iamount from PL_InvestmentPlan_Month_Adjustment_Itemd pimaid where pimaid.iAdjustmentItemId = pimai.iautoid and iperiodnum = 1) iamount1,
	(select cperiodprogress from PL_InvestmentPlan_Month_Adjustment_Itemd pimaid where pimaid.iAdjustmentItemId = pimai.iautoid and iperiodnum = 2) cperiodprogress2,
	(select dperioddate from PL_InvestmentPlan_Month_Adjustment_Itemd pimaid where pimaid.iAdjustmentItemId = pimai.iautoid and iperiodnum = 2) dperioddate2,
	(select iamount from PL_InvestmentPlan_Month_Adjustment_Itemd pimaid where pimaid.iAdjustmentItemId = pimai.iautoid and iperiodnum = 2) iamount2,
	(select cperiodprogress from PL_InvestmentPlan_Month_Adjustment_Itemd pimaid where pimaid.iAdjustmentItemId = pimai.iautoid and iperiodnum = 3) cperiodprogress3,
	(select dperioddate from PL_InvestmentPlan_Month_Adjustment_Itemd pimaid where pimaid.iAdjustmentItemId = pimai.iautoid and iperiodnum = 3) dperioddate3,
	(select iamount from PL_InvestmentPlan_Month_Adjustment_Itemd pimaid where pimaid.iAdjustmentItemId = pimai.iautoid and iperiodnum = 3) iamount3,
	(select cperiodprogress from PL_InvestmentPlan_Month_Adjustment_Itemd pimaid where pimaid.iAdjustmentItemId = pimai.iautoid and iperiodnum = 4) cperiodprogress4,
	(select dperioddate from PL_InvestmentPlan_Month_Adjustment_Itemd pimaid where pimaid.iAdjustmentItemId = pimai.iautoid and iperiodnum = 4) dperioddate4,
	(select iamount from PL_InvestmentPlan_Month_Adjustment_Itemd pimaid where pimaid.iAdjustmentItemId = pimai.iautoid and iperiodnum = 4) iamount4,
	(select cperiodprogress from PL_InvestmentPlan_Month_Adjustment_Itemd pimaid where pimaid.iAdjustmentItemId = pimai.iautoid and iperiodnum = 5) cperiodprogress5,
	(select dperioddate from PL_InvestmentPlan_Month_Adjustment_Itemd pimaid where pimaid.iAdjustmentItemId = pimai.iautoid and iperiodnum = 5) dperioddate5,
	(select iamount from PL_InvestmentPlan_Month_Adjustment_Itemd pimaid where pimaid.iAdjustmentItemId = pimai.iautoid and iperiodnum = 5) iamount5,
	(select cperiodprogress from PL_InvestmentPlan_Month_Adjustment_Itemd pimaid where pimaid.iAdjustmentItemId = pimai.iautoid and iperiodnum = 6) cperiodprogress6,
	(select dperioddate from PL_InvestmentPlan_Month_Adjustment_Itemd pimaid where pimaid.iAdjustmentItemId = pimai.iautoid and iperiodnum = 6) dperioddate6,
	(select iamount from PL_InvestmentPlan_Month_Adjustment_Itemd pimaid where pimaid.iAdjustmentItemId = pimai.iautoid and iperiodnum = 6) iamount6
from 
	PL_InvestmentPlan_Month_Adjustment_Item pimai
	left join pl_investment_plan_item pi on pimai.iplanid = pi.iautoid
	inner join pl_investment_plan p on pi.iplanid = p.iautoid
	where 1=1
	#if(iadjustmentmid)
		and pimai.iadjustmentmid = #para(iadjustmentmid)
	#end
#end