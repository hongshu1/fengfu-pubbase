#sql("paginateMdatas")
select pm.cdepcode,pm.ibudgetyear,pm.ibudgettype,pi.iautoid,pi.iinvestmenttype,pi.cproductline,
	pi.cmodelinvccode,pi.cparts,pi.icareertype,pi.iinvestmentdistinction,pi.cplanno,pi.citemname,
	pi.isimport,pi.iquantity,pi.cunit,pi.cassettype,pi.cpurpose,pi.ceffectamount,pi.creclaimyear,
	pi.clevel,pi.ispriorreport,pi.cpaymentprogress,(pi.itaxrate*100) itaxrate,pi.itotalamountplan,pi.itotalamountactual,
	(pi.itotalamountplan-pi.itotalamountactual) itotalamountdiff,pi.itotalamountdiffreason,
	pi.iyeartotalamountplan,pi.iyeartotalamountactual,
	(pi.iyeartotalamountplan-pi.iyeartotalamountactual) iyeartotalamountdiff,pi.iyeartotalamountdiffreason,
	pi.cedittype,pi.cmemo,pi.iitemyear,pi.isscheduled,pi.iamounttotal,
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
	left join pl_investment_plan pm on pi.iplanid = pm.iautoid
where 1=1 and pm.iorgid = #para(iorgid)
	and exists (
		select 1 from bas_project_card pc where pc.ccode = pi.cplanno 
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
		and pm.iperiodid = #para(iperiodid)
	#end
	#if(cdepcode)
		and pm.cdepcode = #para(cdepcode)
	#end
	#if(iauditstatus)
		and pm.iauditstatus = #para(iauditstatus)
	#end
	#if(ieffectivestatus)
		and pm.ieffectivestatus = #para(ieffectivestatus)
	#end
	#if(isourceproposalid)
		and not exists (
			select 1 from pl_proposald where iproposalmid = #para(isourceproposalid) and iprojectcardid = pi.iprojectcardid
		)
	#end
	#if(iprojectcardids)
		and pi.iprojectcardid not in (#(iprojectcardids))
	#end
	#(getDataPermissionSql("pm", "cdepcode"))
#end

#sql("getEdittingPlanItems")
SELECT it.*
FROM PL_ProposalD d
    INNER JOIN PL_Proposalm m ON d.iproposalmid = m.iautoid
    INNER JOIN PL_Investment_Plan_Item it ON d.isourceid = it.iautoid
WHERE d.isourcetype = #para(isourcetype)
  AND m.iauditstatus IN (0, 3) ### 草稿状态、不通过
  AND d.isourceid IN (#(iinvestmentplanitemids))
#end

#sql("findNotExistsProjectCardInvestmentItem")
	select * from pl_investment_plan_item pi where 1=1
		and not exists (
			select 1 from bas_project_card pc where pi.cplanno = pc.ccode
				#if(iorgid)
					and pc.iorgid = #para(iorgid)
				#end
				#if(iservicetype)
					and pc.iservicetype = #(iservicetype)
				#end
		)
		#if(iplanid)
			and pi.iplanid = #para(iplanid)
		#end
#end