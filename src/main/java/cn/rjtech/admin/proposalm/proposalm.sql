#sql("paginateAdminDatas")
SELECT m.*, d.cdepname
FROM PL_Proposalm m
    left JOIN bd_Department d ON m.cdepcode = d.cdepcode
WHERE m.iorgid = #para(iorgid)

### 超级管理员不过滤权限部门
#if(!isSystemAdmin)
    ### 存在角色部门配置过滤处理
    #if(accessCdepCodes && accessCdepCodes.size() > 0)
        AND m.cDepCode IN (
            #for(code:accessCdepCodes)
                '#(code)' #(for.last?'':',')
            #end
        )
    #end
#end

#if(audit)
    AND m.iauditstatus > 0 AND m.iauditstatus < 3
#end
#if(cdepcode)
    AND m.cdepcode = #para(cdepcode)
#end
#if(cproposalno)
    AND m.cproposalno LIKE CONCAT('%', #para(cproposalno), '%')
#end
#if(csourceproposalno)
    AND m.csourceproposalno LIKE CONCAT('%', #para(csourceproposalno), '%')
#end
#if(capplypersoncode)
    AND m.capplypersoncode = #para(capplypersoncode)
#end
#if(cprojectcode)
    AND m.cprojectcode LIKE CONCAT('%', #para(cprojectcode), '%')
#end
#if(cprojectname)
    AND m.cprojectname LIKE CONCAT('%', #para(cprojectname), '%')
#end
#if(iauditstatus)
    AND m.iauditstatus = #para(iauditstatus)
#end
#if(ieffectivestatus)
    AND m.ieffectivestatus = #para(ieffectivestatus)
#end
#if(isscheduled)
    AND m.isscheduled = #para(isscheduled)
#end
ORDER BY m.iautoid DESC
#end

### 获取禀议数据
#sql("paginateDetails")
select * from (
select pm.cproposalno,mdp.cdepname,pm.cApplyPersonName,pm.dApplyDate,pm.cprojectcode,
	pm.cprojectname,bpc.cCategoryName,
	(select top 1 ccode from Bas_Project_Card where iautoid = pd.iProjectCardId) cBudgetNo,
	(select top 1 sm.csubjectname from Bas_Project_Card pc
	left join PL_Expense_Budget_Item ebi on pc.iautoid = ebi.iProjectCardId
	left join Bas_SubjectM sm on sm.iautoid = ebi.iLowestSubjectId
	where pc.iautoid = pd.iProjectCardId) cLowestSubjectname,
	(select min(case when cBudgetNo is not null then ibudgetsum end) from pl_Proposald where iProjectCardId = pd.iProjectCardId GROUP BY iProjectCardId) ibudgetsum,	
	(select min(case when cBudgetNo is not null then ibudgetmoney end) from pl_Proposald where iProjectCardId = pd.iProjectCardId GROUP BY iProjectCardId) ibudgetmoney,
	pd.iNatSum sumamounttaxinclu,
	pd.iNatMoney sumamountnottaxinclu,
	pd.cItemName,
	pd.iquantity,
	pd.iNatUnitPrice,
	pd.iNatSum iamounttaxinclu,
	pd.iNatMoney iamountnottaxinclu,
	bv.cvenname,
	pd.dDemandDate,
	bdp.cdepname cbudgetdepname
from pl_proposalm pm
	inner join pl_proposald pd on pm.iautoid = pd.iProposalMid
	left join Bd_Department mdp on pm.cdepcode = mdp.cdepcode
	LEFT JOIN Bas_ProposalCategory bpc ON pm.iCategoryId = bpc.iAutoId
	left join bd_vendor bv on bv.cvencode = pd.cVenCode
	left join Bd_Department bdp on pd.cBudgetDepCode = bdp.cdepcode
	where pd.isubitem = 1 and pm.iorgid = #para(iorgid)
#if(iautoids)
    AND pm.iAutoId IN ( #(iautoids) )
#end
#if(dapplydate)
    AND DATEDIFF(day, pm.dapplydate, #para(dapplydate)) = 0
#end
#if(cdepcode)
    AND pm.cdepcode = #para(cdepcode)
#end
#if(iauditstatus)
    AND pm.iauditstatus = #para(iauditstatus)
#end
#if(isscheduled)
    AND pm.isscheduled = #para(isscheduled)
#end
#if(cproposalno)
    AND pm.cproposalno LIKE CONCAT('%', #para(cproposalno), '%')
#end
#if(cprojectname)
    AND pm.cprojectname LIKE CONCAT('%', #para(cprojectname), '%')
#end
#if(cpurposename)
    AND pm.cpurposesn = #para(cpurposename)
#end
#if(citemname)
    AND pd.citemname LIKE CONCAT('%', #para(citemname), '%')
#end
#if(ccategoryname)
    AND bpc.ccategoryname LIKE CONCAT('%', #para(ccategoryname), '%')
#end
) T where 1=1
#if(cbudgetno)
    AND cbudgetno LIKE CONCAT('%', #para(cbudgetno), '%')
#end
#end

### 获取明细金额
#sql("getDetailsMoney")
SELECT ISNULL(SUM(expid.iAmount), 0) AS budgetAmount,
       ISNULL(SUM(expid.iAmount)/ (1 + prod.iTaxRate), 0) AS budgetAmountNotTaxInclu,
       ISNULL(SUM(prod.iNatUnitPrice), 0) AS sumAmountTaxInclu,
       ISNULL(SUM(prod.iNatUnitPrice)/ (1 + prod.iTaxRate), 0) AS sumAmountNotTaxInclu,
       ISNULL(CASE WHEN prod.iStatus = 1  THEN SUM(prod.iNatUnitPrice) END, 0) AS iAmountTaxInclu,
       ISNULL(CASE WHEN prod.iStatus = 1  THEN SUM(prod.iNatUnitPrice) / (1 + prod.iTaxRate) END, 0) AS iAmountNotTaxInclu
FROM PL_ProposalM prom
LEFT JOIN PL_ProposalD prod ON prom.iAutoId = prod.iProposalMid
LEFT JOIN PL_Expense_Budget_Item expi  ON prod.iSourceId = expi.iAutoId
LEFT JOIN PL_Expense_Budget_ItemD expid ON expi.iAutoId = expid.iExpenseItemId
WHERE prom.iorgid = #para(iorgid)
AND prom.iAutoId = #para(iautoid)
GROUP BY prod.iTaxRate, prod.iStatus
#end

#sql("updateAuditting")
UPDATE PL_ProposalM
SET iauditstatus = #para(auditting)
WHERE iautoid = #para(iautoid)
    AND ( iauditstatus = #para(unaudit1) OR iauditstatus = #para(unaudit2) )
#end

#sql("updateStatusEditting")
UPDATE PL_ProposalM
SET iauditstatus = #para(editting)
WHERE iautoid = #para(iautoid)
    AND iauditstatus = #para(auditting)
#end

#sql("updateAuditted")
UPDATE PL_ProposalM
SET iauditstatus = #para(approved)
WHERE iautoid = #para(iautoid)
    AND iauditstatus = #para(auditting)
#end

#sql("paginateMdatas")
SELECT *
FROM v_proposal_choose
WHERE iorgid = #para(iorgid) ### 超级管理员不过滤权限部门
#if(!isSystemAdmin)
    ### 存在角色部门配置过滤处理
    #if(accessCdepCodes && accessCdepCodes.size() > 0)
        AND cDepCode IN (
            #for(code:accessCdepCodes)
                '#(code)' #(for.last?'':',')
            #end
        )
    #end
#end
#if(cdepcode)
    AND cDepCode = #para(cdepcode)
#end
#if(ibudgettype)
    AND iBudgetType = #para(ibudgettype)
#end
#if(iproposalchoosetype)
    AND iProposalChooseType = #para(iproposalchoosetype)
#end
#if(ibudgetyear)
    AND iBudgetYear = #para(ibudgetyear)
#end
#if(noiniautoids)
    AND iautoid NOT IN (#(noiniautoids))
#end
#if(cno)
    AND cno LIKE CONCAT( '%',#para(cno), '%')
#end
#if(clowestsubjectname)
    AND clowestsubjectname LIKE CONCAT( '%',#para(clowestsubjectname), '%')
#end
ORDER BY dCreateTime DESC
    #end
  
    
#sql("findExpenseBudgetOrInvestmentCdepcode")
select T.cdepcode from (
	select eb.cdepcode,ebi.iautoid from pl_expense_budget_item ebi
		inner join pl_expense_budget eb on ebi.iexpenseid = eb.iautoid
		where ebi.iautoid in (#(iautoids))
	union all
	select ip.cdepcode,ipi.iautoid from pl_investment_plan_item ipi
		inner join pl_investment_plan ip on ip.iautoid = ipi.iplanid
		where ipi.iautoid in (#(iautoids))
) T group by T.cdepcode
#end

#sql("findProposalDatasByExpenseId")
	select distinct pm.cproposalno from pl_proposald pd 
		inner join pl_proposalm pm on pm.iautoid = pd.iproposalmid
	where iSourceType = 1 and exists (
		select 1 from pl_expense_budget_item ebi where iexpenseid = #para(iexpenseid) and ebi.iautoid = pd.isourceid
	)
#end

#sql("findProposalDatasByPlanId")
	select distinct pm.cproposalno from pl_proposald pd 
		inner join pl_proposalm pm on pm.iautoid = pd.iproposalmid
	where iSourceType = 2 and exists (
		select 1 from pl_investment_plan_item ipi where iplanid = #para(iplanid) and ipi.iautoid = pd.isourceid
	)
#end

#sql("findInvalidPurchase")
select * from PL_PurchaseM where 1=1 and iproposalmid = #para(iproposalmid)
#if(ieffectivestatus)
	and ieffectivestatus = #para(ieffectivestatus)
#end
#end

#sql("findProposalMoneySumByProjectIds")
select sum(pd.imoney) imoney from pl_proposald pd 
	inner join pl_proposalm pm on pd.iproposalmid = pm.iautoid
	where pd.iprojectcardid in (#(iprojectcardids)) and pm.ieffectivestatus != #para(ieffectivestatus)
	#if(ifirstsourceproposalid)
		and pm.ifirstsourceproposalid not in (#(ifirstsourceproposalid))
	#end
#end