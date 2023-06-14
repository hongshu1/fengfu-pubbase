### 采购管理数据源
#sql("paginateAdminDatas")
    SELECT pm.*,
    (SELECT top 1 cptname FROM bd_PurchaseType pt WHERE pt.cptcode = pm.iPurchaseType) AS ipurchasetypename,
    (SELECT top 1 cDepName FROM bd_Department WHERE cDepCode = pm.cdepcode) AS cdepname,
    (SELECT top 1 cpsn_name FROM Bd_Person WHERE cpsn_num = pm.cPersonCode) AS cpersonname
    FROM PL_PurchaseM pm
    WHERE 1 = 1
#if(iorgid)
	and pm.iorgid = #para(iorgid)
#end
#if(iautoid)
    AND pm.iAutoId = #para(iautoid)
#end
#if(cdepcodes)
    AND pm.cdepcode IN (
        #for(code:cdepcodes)
            '#(code)' #(for.last?'':',')
        #end
    )
#end
#if(cpurchaseno)
	and pm.cpurchaseno like concat('%',#para(cpurchaseno),'%')
#end
#if(cpurchasedate)
	and convert(date,pm.cpurchasedate) = convert(date,#para(cpurchasedate))
#end
ORDER BY pm.iautoid DESC
#end

#sql("getMoney")
	SELECT 
	effectedProm.ifirstsourceproposalid,
	effectedProm.cproposalno,
	isnull((
		select sum(inatmoney) from pl_purchasem pcm where pcm.ifirstsourceproposalid = effectedProm.ifirstsourceproposalid
		#if(ipurchasemid) 
			and pcm.iautoid not in (#(ipurchasemid))
		#end
	),0) ialreadypurchasenatmoney,
	isnull(effectedProm.inatmoney,0) iproposalnatmoney,
	isnull(effectedProm.ibudgetmoney,0) ibudgetmoney,
	isnull((
		select sum(inatmoney) from pl_purchased where iprojectcardid in (
			select iprojectcardid from pl_proposald where iproposalmid = effectedProm.iautoid
		)
		#if(ipurchasemid) 
			and ipurchaseid not in (#(ipurchasemid))
		#end
	),0) ibudgetalreadypurchasemoney
	FROM PL_ProposalM effectedProm
	WHERE 1 = 1
	#if(ieffectivestatus)
		and effectedProm.ieffectivestatus = #para(ieffectivestatus)
	#end
	#if(ifirstsourceproposalid)
		AND effectedProm.ifirstsourceproposalid = #para(ifirstsourceproposalid)
	#end
#end


### 获取U8请购单ID
#sql("getAppVouchId")
SELECT id
FROM PU_AppVouch
WHERE 1 = 1
#if(ccodes)
AND cCode IN ( #for(code:ccodes)
    '#(code)' #(for.last?'':',')
        #end )
 #end
#if(ccode)
AND cCode = #para(ccode)
#end
#end

### 删除请购单主表数据
#sql("delectAppVouchByids")
DELETE
FROM PU_AppVouch
WHERE id in (
    #for(code:ids)
        '#(code)' #(for.last?'':',')
    #end
    )
#end

### 删除请购单细表数据
#sql("delectAppVouchsByids")
DELETE
FROM PU_AppVouchs
WHERE id in ( #for(code:ids)
    '#(code)' #(for.last?'':',')
    #end)
#end

### 删除请购额外表数据
#sql("delectAppVouchExtraDefineByids")
DELETE
FROM PU_AppVouch_ExtraDefine
WHERE id in ( #for(code:ids)
    '#(code)' #(for.last?'':',')
    #end)
#end

### 修改请购单状态
#sql("updateAppVouch")
UPDATE PU_AppVouch
SET cAuditTime = GetDate( ),
    cAuditDate = CONVERT(varchar,GETDATE(),23),
    cVerifier = #para(cverifier),
    iverifystateex = '2'
WHERE ID = #para(id)
AND isnull( cVerifier, '' ) = ''
AND isnull( cCloser, '' ) = ''
#end

### 校验是否有采购单信息
#sql("checkIsCreataPurchase")
SELECT distinct app.cCode
FROM po_podetails ppds
	left join PU_AppVouchs pavs on ppds.iappids = pavs.autoid
	left join PU_AppVouch app on app.id = pavs.id
WHERE 1 = 1
     #if(ids)
     AND app.id in (
        #for(code:ids)
            '#(code)' #(for.last?'':',')
        #end
        )
    #end
#end

### 參照预算
#sql("purchaseChoose")
SELECT *
FROM v_proposal_choose
WHERE iautoid IN ( #(iautoids) )
#end

#sql("chooseProposalmDatas")
SELECT m.*, d.cdepname
FROM PL_Proposalm m
    left JOIN bd_Department d ON m.cdepcode = d.cdepcode
WHERE 1=1 
#if(iorgid)
	and m.iorgid = #para(iorgid)
#end
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
#if(ieffectivestatus)
    AND m.ieffectivestatus = #para(ieffectivestatus)
#end
#if(ifirstsourceproposalid)
	and m.ifirstsourceproposalid = #para(ifirstsourceproposalid)
#end
#if(dstartdate)
	and m.dApplyDate >= convert(date,#para(dstartdate))
#end
#if(denddate)
	and m.dApplyDate <= convert(date,#para(denddate))
#end
#if(cproposalno)
	and m.cproposalno like concat('%',#para(cproposalno),'%')
#end
#if(capplypersonname)
	and m.capplypersonname like concat('%',#para(capplypersonname),'%')
#end
ORDER BY m.iautoid DESC
#end

#sql("chooseProposalmDatasDetail")
SELECT pd.*,pd.iautoid iproposaldid,
       v.cvenname,
       d.cdepname AS cbudgetdepname,
       (CASE pd.iSourceType
        WHEN 1 THEN (SELECT cbudgetno FROM  PL_Expense_Budget_Item WHERE iautoid = pd.iSourceId)
        WHEN 2 THEN (SELECT cplanno FROM  PL_Investment_Plan_Item WHERE iautoid = pd.iSourceId)
        ELSE NULL END) AS cbudgetno,
       (CASE pd.iSourceType
        WHEN 1 THEN (SELECT s.csubjectname FROM PL_Expense_Budget_Item ebi
			        INNER JOIN bas_subjectm s ON ebi.iLowestSubjectid = s.iautoid
                    WHERE ebi.iautoid = pd.iSourceId)
        ELSE NULL END) AS cLowestSubjectName,0 isubitem
FROM PL_ProposalD pd
left JOIN bd_Vendor v ON pd.cvencode = v.cvencode
left JOIN bd_Department d ON pd.cbudgetdepcode = d.cdepcode
WHERE 1=1 and pd.inatmoney > 0 
#if(iproposalmid)
	and pd.iproposalmid = #para(iproposalmid)
#end
ORDER BY pd.iautoid
#end

#sql("findExpenseBudgetItemDatas")
	select eb.cdepcode,eb.ibudgettype,eb.ibudgetyear,ebi.iautoid,ebi.iexpenseid,ebi.cbudgetno,bsh.cSubjectName chighestsubjectname,
	bsl.cSubjectName clowestsubjectname,ebi.citemname,ebi.ihighestsubjectid,ebi.ilowestsubjectid,
	ebi.careertype,ebi.isLargeAmountExpense,ebi.cuse,ebi.cmemo,ebi.iprice,ebi.cunit,ebi.iCarryForward,ebi.icreateby,ebi.dcreatetime,
	ebi.iamounttotal ibudgetmoney,ebi.isscheduled,1 isourcetype,ebi.iautoid isourceid,ebi.iprojectcardid,
	(select config_value from #(getBaseDbName()).dbo.jb_global_config where config_key = 'tax_rate') itaxrate,0 isubitem,
	isnull((select sum(inatmoney) from pl_purchased where iprojectcardid = ebi.iprojectcardid),0) ibudgetalreadypurchasemoney
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
	#if(ieffectivestatus)
		and eb.ieffectivestatus = #para(ieffectivestatus)
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
ebi.careertype,ebi.isLargeAmountExpense,ebi.cuse,ebi.cmemo,ebi.iprice,ebi.cunit,ebi.iCarryForward,ebi.icreateby,ebi.dcreatetime,ebi.iamounttotal,ebi.isscheduled,ebi.iprojectcardid
#end

#sql("findInvestmentPlanItemDatas")
select pm.cdepcode,pm.ibudgetyear,pm.ibudgettype,pi.iautoid,pi.iinvestmenttype,pi.cproductline,
	pi.cmodelinvccode,pi.cparts,pi.icareertype,pi.iinvestmentdistinction,pi.cplanno cbudgetno,pi.citemname,
	pi.isimport,pi.iquantity,pi.cunit,pi.cassettype,pi.cpurpose,pi.ceffectamount,pi.creclaimyear,
	pi.clevel,pi.ispriorreport,pi.cpaymentprogress,pi.itaxrate,pi.itotalamountplan,pi.itotalamountactual,
	(pi.itotalamountplan-pi.itotalamountactual) itotalamountdiff,pi.itotalamountdiffreason,
	pi.iyeartotalamountplan,pi.iyeartotalamountactual,
	(pi.iyeartotalamountplan-pi.iyeartotalamountactual) iyeartotalamountdiff,pi.iyeartotalamountdiffreason,
	pi.cedittype,pi.cmemo,pi.iitemyear,pi.isscheduled,pi.iamounttotal ibudgetmoney,2 isourcetype,pi.iautoid isourceid,pi.iprojectcardid,0 isubitem,
	isnull((select sum(inatmoney) from pl_purchased where iprojectcardid = pi.iprojectcardid),0) ibudgetalreadypurchasemoney,
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
#end

#sql("refBudgetItemDatas")
	select iautoid,cbudgetno,1 isourcetype,iamounttotal ibudgetmoney,iprojectcardid,
	(select config_value from #(getBaseDbName()).dbo.jb_global_config where config_key = 'tax_rate') itaxrate,
	isnull((select sum(inatmoney) from pl_purchased where iprojectcardid = ebi.iprojectcardid),0) ibudgetalreadypurchasemoney
	from pl_expense_budget_item ebi where iautoid in (#(budgetitemids))
	union all
	select iautoid,cplanno cbudgetno,2 isourcetype,iamounttotal ibudgetmoney,iprojectcardid,
	itaxrate,
	isnull((select sum(inatmoney) from pl_purchased where iprojectcardid = pi.iprojectcardid),0) ibudgetalreadypurchasemoney
	from pl_investment_plan_item pi where iautoid in (#(budgetitemids))
#end

#sql("findCBudgetNosOfRefProposal")
	select pc.ccode cbudgetno,pd.ibudgetmoney,pc.iservicetype isourcetype,pd.iprojectcardid,pd.isourceid,
		min(case when pd.isubitem = 0 then pd.iautoid end) iproposaldid
	from pl_proposald pd
	left join pl_proposalm pm on pd.iproposalmid = pm.iautoid
	left join bas_project_card pc on pc.iautoid = pd.iprojectcardid
		where 1=1 
		and pm.ifirstsourceproposalid = #para(ifirstsourceproposalid) 
		and pm.ieffectivestatus = #para(ieffectivestatus)
	group by pc.ccode,pd.ibudgetmoney,pc.iservicetype,pd.iprojectcardid,pd.isourceid
#end

#sql("findEnableImportDatasOfRefBudget")
select ebi.cbudgetno,pc.iservicetype isourcetype,ebi.iamounttotal ibudgetmoney,ebi.iprojectcardid,ebi.iautoid isourceid from PL_Expense_Budget_Item ebi
	left join PL_Expense_Budget eb on ebi.iexpenseid = eb.iautoid
	left join bas_project_card pc on pc.iautoid = ebi.iprojectcardid
	where 1=1 and eb.iorgid = #para(iorgid)
	#if(istatus)
		and pc.istatus = #para(istatus)
	#end
	#if(isfreeze)
		and pc.isfreeze = #para(isfreeze)
	#end
	#if(cdepcode)
		and eb.cdepcode = #para(cdepcode)
	#end
	#if(ieffectivestatus)
		and eb.ieffectivestatus = #para(ieffectivestatus)
	#end
union all
select pi.cplanno cbudgetno,pc.iservicetype isourcetype,pi.iamounttotal ibudgetmoney,pi.iprojectcardid,pi.iautoid isourceid from pl_investment_plan_item pi
	left join pl_investment_plan p on p.iautoid = pi.iplanid
	left join bas_project_card pc on pc.iautoid = pi.iprojectcardid
	where 1=1 and p.iorgid = #para(iorgid)
	#if(istatus)
		and pc.istatus = #para(istatus)
	#end
	#if(isfreeze)
		and pc.isfreeze = #para(isfreeze)
	#end
	#if(cdepcode)
		and p.cdepcode = #para(cdepcode)
	#end
	#if(ieffectivestatus)
		and p.ieffectivestatus = #para(ieffectivestatus)
	#end
#end

#sql("getiunitprice")
SELECT *
FROM Ven_Inv_Price
WHERE dEnableDate < GETDATE()
AND ISNULL(dDisableDate, '9999-12-31 23:59:59') > GETDATE()
AND cInvCode = '#(cinvcode)'
AND cVenCode = '#(cvencode)'
#if(cfree2)
    AND cfree2 = LIKE '%#(cfree2)%'
#end
ORDER BY dEnableDate, AutoID DESC
#end