#sql("getSourceYearOrderList")
###根据客户id集查询年度订单 并进行行转列
SELECT a.iCustomerId,d.cCusCode,
       e.iEquipmentModelId,f.cEquipmentModelCode,
       b.iInventoryId,e.cInvCode,e.cInvCode1,e.cInvName1,
       b.iYear1 AS nowyear,
       (
           SELECT iQty
           FROM Co_AnnualOrderD_Qty
           WHERE iAnnualOrderDid = b.iautoid
             AND iyear = a.iyear
             AND imonth = 1
       ) AS nowmonth1,
       (
           SELECT iQty
           FROM Co_AnnualOrderD_Qty
           WHERE iAnnualOrderDid = b.iautoid
             AND iyear = a.iyear
             AND imonth = 2
       ) AS nowmonth2,
       (
           SELECT iQty
           FROM Co_AnnualOrderD_Qty
           WHERE iAnnualOrderDid = b.iautoid
             AND iyear = a.iyear
             AND imonth = 3
       ) AS nowmonth3,
       (
           SELECT iQty
           FROM Co_AnnualOrderD_Qty
           WHERE iAnnualOrderDid = b.iautoid
             AND iyear = a.iyear
             AND imonth = 4
       ) AS nowmonth4,
       (
           SELECT iQty
           FROM Co_AnnualOrderD_Qty
           WHERE iAnnualOrderDid = b.iautoid
             AND iyear = a.iyear
             AND imonth = 5
       ) AS nowmonth5,
       (
           SELECT iQty
           FROM Co_AnnualOrderD_Qty
           WHERE iAnnualOrderDid = b.iautoid
             AND iyear = a.iyear
             AND imonth = 6
       ) AS nowmonth6,
       (
           SELECT iQty
           FROM Co_AnnualOrderD_Qty
           WHERE iAnnualOrderDid = b.iautoid
             AND iyear = a.iyear
             AND imonth = 7
       ) AS nowmonth7,
       (
           SELECT iQty
           FROM Co_AnnualOrderD_Qty
           WHERE iAnnualOrderDid = b.iautoid
             AND iyear = a.iyear
             AND imonth = 8
       ) AS nowmonth8,
       (
           SELECT iQty
           FROM Co_AnnualOrderD_Qty
           WHERE iAnnualOrderDid = b.iautoid
             AND iyear = a.iyear
             AND imonth = 9
       ) AS nowmonth9,
       (
           SELECT iQty
           FROM Co_AnnualOrderD_Qty
           WHERE iAnnualOrderDid = b.iautoid
             AND iyear = a.iyear
             AND imonth = 10
       ) AS nowmonth10,
       (
           SELECT iQty
           FROM Co_AnnualOrderD_Qty
           WHERE iAnnualOrderDid = b.iautoid
             AND iyear = a.iyear
             AND imonth = 11
       ) AS nowmonth11,
       (
           SELECT iQty
           FROM Co_AnnualOrderD_Qty
           WHERE iAnnualOrderDid = b.iautoid
             AND iyear = a.iyear
             AND imonth = 12
       ) AS nowmonth12, b.iYear1Sum AS nowMonthSum,
       b.iYear2 AS nextyear,
       (
           SELECT iQty
           FROM Co_AnnualOrderD_Qty
           WHERE iAnnualOrderDid = b.iautoid
             AND iyear = a.iyear + 1
             AND imonth = 1
       ) AS nextmonth1,
       (
           SELECT iQty
           FROM Co_AnnualOrderD_Qty
           WHERE iAnnualOrderDid = b.iautoid
             AND iyear = a.iyear + 1
             AND imonth = 2
       ) AS nextmonth2,
       (
           SELECT iQty
           FROM Co_AnnualOrderD_Qty
           WHERE iAnnualOrderDid = b.iautoid
             AND iyear = a.iyear + 1
             AND imonth = 3
       ) AS nextmonth3, b.iYear2Sum AS nextmonthSum,
       'PP' AS planTypeCode
FROM Co_AnnualOrderM AS a
         LEFT JOIN Co_AnnualOrderD AS b ON a.iAutoId = b.iAnnualOrderMid
         LEFT JOIN Bd_Customer AS d ON a.iCustomerId = d.iAutoId
         LEFT JOIN Bd_Inventory AS e ON b.iInventoryId = e.iAutoId
         LEFT JOIN Bd_EquipmentModel AS f ON e.iEquipmentModelId = f.iAutoId
WHERE a.isDeleted = '0'
  AND a.iYear = #para(startyear) AND a.iCustomerId IN (#(customerids))
#end


















#sql("getPinvScheduPlanList")
###查询当前物料的所有父级物料三个月的计划数
select a.iAutoId,a.iItemId,cTargetMonth,cPlanCode,date1,date2,date3,date4,date5,date6,
       date7,date8,date9,date10,date11,date12,date13,date14,date15,date16,date17,date18,date19,
       date20,date21,date22,date23,date24,date25,date26,date27,date28,date29,date30,date31,
       monthSum,cItemCode,c.iUsageUOM as date0
from t_Sys_ScheduBasePlan as a
         left join Bd_BOMMasterFilter as b on a.iItemId = b.iItemId
         left join Bd_BOMCompareFilter as c on b.iAutoId = c.iBOMMasterId
where a.iItemId IS NOT NULL and a.monthSum IS NOT NULL
  and a.monthSum <> '0' and a.cPlanCode IN ('PC','CP') and
        a.cTargetMonth in #(months) and c.iItemId = #para(iitemid)
#end

#sql("getInvScheduPlanList")
###查询所需物料三个月的计划数与计划变更数
SELECT *
FROM t_Sys_ScheduBasePlan
WHERE monthSum IS NOT NULL AND monthSum <> '0' and cPlanCode IN ('PC','CP')
  AND cTargetMonth IN #(months)
  AND iItemId IN #(ids)
#end

#sql("getPinvInfoList")
###查询所需物料所有父级物料及其用量提前期信息
select b.iItemId as invId,e.cItemCode as invCode,
       c.iAutoId as pInvId,c.cItemCode as pInvCode,
       cProdAdvance as leadtime,b.iUsageUOM as Realqty
from Bd_BOMMasterFilter as a
         left join Bd_BOMCompareFilter as b on a.iAutoId = b.iBOMMasterId
         left join Bd_ItemMaster as c on a.iItemId = c.iAutoId
         left join Bd_ItemMfgInfo as d on b.iItemId = d.iItemId
         left join Bd_ItemMaster as e on b.iItemId = e.iAutoId
where b.iItemId in #(ids)
#end

#sql("delInvScheduPlan")
###删除所需物料所需月份的排程计划
DELETE FROM t_Sys_ScheduBasePlan
WHERE cPlanCode IN #(plancodes) and
convert(varchar(20),cTargetMonth,120) >= #para(startmonth) and
convert(varchar(20),cTargetMonth,120) <= #para(endmonth) and
iItemId in #(ids)
#end

#sql("updateScheduPlan")
UPDATE t_Sys_ScheduBasePlan SET #(strsql)
#end


#sql("selectBOMCompare")
SELECT
    bomc.iBOMMasterId,
    bomc.iItemId ,
    iUsageUOM = ISNULL(bomc.iUsageUOM, 1)/ISNULL(bomc.iParentQty, 1),
    mfg.cProdAdvance,
    iBOMMasterIdListStr= (
        STUFF(
                (SELECT ',' + cast(iAutoId as varchar)
                 FROM Bd_BOMMaster
                 WHERE iItemId= bomc.iItemId
                    FOR xml path('')
            ),1,1,''
                    )
        )
FROM
    Bd_BOMCompare AS bomc
        LEFT JOIN Bd_ItemMfgInfo AS mfg ON bomc.iItemId = mfg.iItemId
WHERE isDeleted = '0'
#end
