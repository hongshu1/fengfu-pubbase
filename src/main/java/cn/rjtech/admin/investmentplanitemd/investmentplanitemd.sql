#sql("paginateAdminDatas")
SELECT
    pm.cdepcode,
    d.cdepname,
    pm.ibudgettype,
    pm.ibudgetyear,
    pi.iautoid,
    pi.iinvestmenttype,
    bp.cprojectcode,
    bp.cprojectname,
    pi.cproductline,
    pi.cmodelinvccode,
    pi.cparts,
    pi.icareertype,
    pi.iinvestmentdistinction,
    pi.cplanno,
    pi.citemname,
    pi.isimport,
    pi.iquantity,
    pi.cunit,
    pi.cassettype,
    pi.cpurpose,
    pi.ceffectamount,
    pi.creclaimyear,
    pi.clevel,
    pi.ispriorreport,
    pi.cpaymentprogress,
    pi.iTotalAmountPlan,
    pi.iTotalAmountActual,
    ( pi.iTotalAmountPlan- pi.iTotalAmountActual ) iTotalAmountDiff,
    pi.iTotalAmountDiffReason,
    pi.iYearTotalAmountPlan,
    pi.iYearTotalAmountActual,
    ( pi.iYearTotalAmountPlan- pi.iYearTotalAmountActual ) iYearTotalAmountDiff,
    pi.iYearTotalAmountDiffReason,
    pi.cedittype,
    pi.cmemo,
    pi.iitemyear,
    pi.itaxrate,
    pm.iauditstatus,
    MIN ( CASE iperiodnum WHEN 1 THEN cperiodprogress END ) cperiodprogress1,
    MIN ( CASE iperiodnum WHEN 1 THEN concat ( YEAR ( dperioddate ), '-', MONTH ( dperioddate )) END ) dperioddate1,
    SUM ( CASE iperiodnum WHEN 1 THEN iamount END ) iamount1,
    MIN ( CASE iperiodnum WHEN 2 THEN cperiodprogress END ) cperiodprogress2,
    MIN ( CASE iperiodnum WHEN 2 THEN concat ( YEAR ( dperioddate ), '-', MONTH ( dperioddate )) END ) dperioddate2,
    SUM ( CASE iperiodnum WHEN 2 THEN iamount END ) iamount2,
    MIN ( CASE iperiodnum WHEN 3 THEN cperiodprogress END ) cperiodprogress3,
    MIN ( CASE iperiodnum WHEN 3 THEN concat ( YEAR ( dperioddate ), '-', MONTH ( dperioddate )) END ) dperioddate3,
    SUM ( CASE iperiodnum WHEN 3 THEN iamount END ) iamount3,
    MIN ( CASE iperiodnum WHEN 4 THEN cperiodprogress END ) cperiodprogress4,
    MIN ( CASE iperiodnum WHEN 4 THEN concat ( YEAR ( dperioddate ), '-', MONTH ( dperioddate )) END ) dperioddate4,
    SUM ( CASE iperiodnum WHEN 4 THEN iamount END ) iamount4,
    MIN ( CASE iperiodnum WHEN 5 THEN cperiodprogress END ) cperiodprogress5,
    MIN ( CASE iperiodnum WHEN 5 THEN concat ( YEAR ( dperioddate ), '-', MONTH ( dperioddate )) END ) dperioddate5,
    SUM ( CASE iperiodnum WHEN 5 THEN iamount END ) iamount5,
    MIN ( CASE iperiodnum WHEN 6 THEN cperiodprogress END ) cperiodprogress6,
    MIN ( CASE iperiodnum WHEN 6 THEN concat ( YEAR ( dperioddate ), '-', MONTH ( dperioddate )) END ) dperioddate6,
    SUM ( CASE iperiodnum WHEN 6 THEN iamount END ) iamount6
FROM
    PL_Investment_Plan_Item pi
    LEFT JOIN pl_investment_Plan_itemd pid ON pi.iautoid = pid.iplanitemid
    LEFT JOIN bas_project bp ON pi.iprojectid = bp.iautoid
    LEFT JOIN PL_Investment_Plan pm  ON pi.iPlanId = pm.iAutoId
    LEFT JOIN bd_department d on pm.cdepcode = d.cdepcode

WHERE
    1 = 1
    #if(cdepcode)
  and  pm.cdepcode like concat('#(cdepcode)','%')
    #end
    #if(ibudgetyear)
  and  pm.ibudgetyear = #para(ibudgetyear)
    #end
    #if(ibudgettype)
  and  pm.ibudgettype = #para(ibudgettype)
    #end
    #if(iauditstatus)
  and  pm.iauditstatus = #para(iauditstatus)
    #end
    #if(iinvestmenttype)
  and  pi.iinvestmenttype = #para(iinvestmenttype)
    #end
    #if(careertype)
  and  pi.icareertype = #para(careertype)
    #end
    #if(cplanno)
  and  pi.cplanno   like concat('%',#para(cplanno),'%')
    #end
    #if(cproject)
  and  bp.cprojectcode like concat('%',#para(cproject),'%') or  bp.cprojectname like concat('%',#para(cproject),'%')
    #end
    #if(iinvestmentdistinction)
  and  pi.iinvestmentdistinction = #para(iinvestmentdistinction)
    #end
    #if(citemname)
  and  pi.citemname = #para(citemname)
    #end
    #if(ieffectivestatus)
  and pm.ieffectivestatus != #para(ieffectivestatus)
    #end
	#(getDataPermissionSql("pm", "cdepcode")) 
GROUP BY
    pi.iautoid,
    pi.iinvestmenttype,
    bp.cprojectcode,
    bp.cprojectname,
    pi.cproductline,
    pi.cmodelinvccode,
    pi.cparts,
    pi.icareertype,
    pi.iinvestmentdistinction,
    pi.cplanno,
    pi.citemname,
    pi.isimport,
    pi.iquantity,
    pi.cunit,
    pi.cassettype,
    pi.cpurpose,
    pi.ceffectamount,
    pi.creclaimyear,
    pi.clevel,
    pi.ispriorreport,
    pi.cpaymentprogress,
    pi.iTotalAmountPlan,
    pi.iTotalAmountActual,
    pi.iTotalAmountDiffReason,
    pi.iYearTotalAmountPlan,
    pi.iYearTotalAmountActual,
    pi.iYearTotalAmountDiffReason,
    pi.cedittype,
    pi.cmemo,
    pi.iitemyear,
    pi.iTotalAmountPlan- pi.iTotalAmountActual,
    pi.iYearTotalAmountPlan- pi.iYearTotalAmountActual ,
    pm.cDepCode,
    pm.iBudgetType,
    pm.iBudgetYear,
    d.cdepname,
    pm.iauditstatus,pi.itaxrate

    #end