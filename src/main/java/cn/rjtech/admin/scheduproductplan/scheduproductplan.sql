###---------------------------------------------------------年度生产计划排产---------------------

#sql("getSourceYearOrderList")
###根据客户id集查询年度订单 并进行行转列
SELECT a.iCustomerId,d.cCusCode,d.cCusName,
       e.iEquipmentModelId,f.cEquipmentModelCode,f.cEquipmentModelName,
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
       ) AS nextmonth3,
       (
           SELECT iQty
           FROM Co_AnnualOrderD_Qty
           WHERE iAnnualOrderDid = b.iautoid
             AND iyear = a.iyear + 1
             AND imonth = 4
       ) AS nextmonth4,
       'PP' AS planTypeCode
FROM Co_AnnualOrderM AS a
         LEFT JOIN Co_AnnualOrderD AS b ON a.iAutoId = b.iAnnualOrderMid
         LEFT JOIN Bd_Customer AS d ON a.iCustomerId = d.iAutoId
         LEFT JOIN Bd_Inventory AS e ON b.iInventoryId = e.iAutoId
         LEFT JOIN Bd_EquipmentModel AS f ON e.iEquipmentModelId = f.iAutoId
WHERE a.isDeleted = '0'
  AND a.iYear = #para(startyear) AND a.iCustomerId IN (#(customerids))
#end

#sql("getInvInfoList")
###查询物料集信息
SELECT a.iAutoId,a.cInvCode,
       b.cProdCalendarTypeSn,c.iMinInStockDays,c.iMaxInStockDays
FROM Bd_Inventory AS a
         LEFT JOIN Bd_InventoryMfgInfo AS b ON b.iInventoryId = a.iAutoId
         LEFT JOIN Bd_InventoryPlan AS c ON c.iInventoryId = a.iAutoId
WHERE a.iAutoId IN (#(invids))
#end

#sql("getCusWorkMonthNumList")
###根据客户id集查询客户年度每月工作天数
SELECT iCustomerId,iYear,
       iMonth1Days AS month1,iMonth2Days AS month2,iMonth3Days AS month3,
       iMonth4Days AS month4,iMonth5Days AS month5,iMonth6Days AS month6,
       iMonth7Days AS month7,iMonth8Days AS month8,iMonth9Days AS month9,
       iMonth10Days AS month10,iMonth11Days AS month11,iMonth12Days AS month12
FROM Bd_CustomerWorkDays
WHERE iCustomerId IN (#(customerids))
  AND CONVERT(VARCHAR(4),iYear,120) >= #para(startyear)
  AND CONVERT(VARCHAR(4),iYear,120) <= #para(endyear)
#end

#sql("getApsYearPlanQtyList")
###根据条件查询客户年度生产计划排程明细
SELECT a.iCustomerId,d.cCusCode,d.cCusName,
       b.iEquipmentModelId,f.cEquipmentModelCode,f.cEquipmentModelName,
       b.iInventoryId,e.cInvCode,e.cInvCode1,e.cInvName1,
       planTypeCode = CASE WHEN c.iType = 1 THEN 'PP'
                           WHEN c.iType = 2 THEN 'CP'
                           WHEN c.iType = 3 THEN 'ZK'
                           ELSE '' END,
       c.iYear,c.iMonth,c.iQty,
       b.iYear11Qty,b.iYear12Qty,b.iYear13Qty,b.iYear21Qty,b.iYear22Qty,b.iYear23Qty,
       c.iAnnualPlanDid
FROM Aps_AnnualPlanM AS a
         LEFT JOIN Aps_AnnualPlanD AS b ON a.iAutoId = b.iAnnualPlanMid
         LEFT JOIN Aps_AnnualPlanD_Qty AS c ON b.iAutoId = c.iAnnualPlanDid
         LEFT JOIN Bd_Customer AS d ON a.iCustomerId = d.iAutoId
         LEFT JOIN Bd_Inventory AS e ON b.iInventoryId = e.iAutoId
         LEFT JOIN Bd_EquipmentModel AS f ON b.iEquipmentModelId = f.iAutoId
WHERE a.isDeleted = '0'
  AND CONVERT(VARCHAR(4),c.iYear,120) >= #para(startyear)
  AND CONVERT(VARCHAR(4),c.iYear,120) <= #para(endyear)
    #if(cplanorderno)
        AND a.cPlanOrderNo = #para(cplanorderno)
    #end
    #if(icustomerid)
        AND a.iCustomerId = #para(icustomerid)
    #end
    #if(cinvcode)
        AND e.cInvCode LIKE CONCAT('%', #para(cinvcode), '%')
    #end
    #if(cinvcode1)
        AND e.cInvCode1 LIKE CONCAT('%', #para(cinvcode1), '%')
    #end
    #if(cinvname1)
        AND e.cInvName1 LIKE CONCAT('%', #para(cinvname1), '%')
    #end
    #if(iequipmentmodelid)
        AND b.iEquipmentModelId = #para(iequipmentmodelid)
    #end
ORDER BY f.cEquipmentModelCode,e.cInvCode,c.iType,c.iYear,c.iMonth
#end

#sql("getApsYearPlanMasterPage")
SELECT a.iAutoId,a.iCustomerId,d.cCusCode,d.cCusName,a.iYear,a.cPlanOrderNo,
       a.iPlanOrderStatus,a.iAuditStatus,a.cCreateName,a.dCreateTime
FROM Aps_AnnualPlanM AS a
         LEFT JOIN Bd_Customer AS d ON a.iCustomerId = d.iAutoId
WHERE a.isDeleted = '0'
    #if(cplanorderno)
        AND a.cPlanOrderNo LIKE CONCAT('%', #para(cplanorderno), '%')
    #end
    #if(icustomerid)
        AND a.iCustomerId = #para(icustomerid)
    #end
    #if(ccusname)
        AND d.cCusName LIKE CONCAT('%', #para(ccusname), '%')
    #end
    #if(iplanorderstatus)
        AND a.iPlanOrderStatus = #para(iplanorderstatus)
    #end
    #if(ccreatename)
        AND a.cCreateName LIKE CONCAT('%', #para(ccreatename), '%')
    #end
    #if(startcreatetime)
        AND CONVERT(VARCHAR(10),a.dCreateTime,120) >= #para(startcreatetime)
    #end
    #if(endcreatetime)
        AND CONVERT(VARCHAR(10),a.dCreateTime,120) <= #para(endcreatetime)
    #end
ORDER BY a.cPlanOrderNo DESC
#end




###---------------------------------------------------------年度生产计划汇总---------------------

#sql("getApsYearPlanSumPage")
###年度生产计划汇总
SELECT d.iWorkRegionMid,e.cWorkCode,e.cWorkName,
       a.iInventoryId,c.cInvCode,c.cInvCode1,c.cInvName1,
       planTypeCode = CASE WHEN b.iType = 1 THEN 'PP'
                           WHEN b.iType = 2 THEN 'CP'
                           WHEN b.iType = 3 THEN 'ZK'
                           ELSE '' END,
       b.iYear,b.iMonth,SUM(b.iQty) AS iQty,
       SUM(a.iYear12Qty) AS iYear12Qty,
       SUM(a.iYear22Qty) AS iYear22Qty
FROM Aps_AnnualPlanD AS a
         LEFT JOIN Aps_AnnualPlanD_Qty AS b ON a.iAutoId = b.iAnnualPlanDid
         LEFT JOIN Bd_Inventory AS c ON a.iInventoryId = c.iAutoId
         LEFT JOIN Bd_InventoryWorkRegion AS d ON a.iAutoId = d.iInventoryId AND d.isDefault = '1' AND d.isDeleted = '0'
         LEFT JOIN Bd_WorkRegionM AS e ON d.iWorkRegionMid = e.iAutoId
         LEFT JOIN Aps_AnnualPlanM AS f ON a.iAnnualPlanMid = f.iAutoId
WHERE b.iType = 2 AND f.isDeleted = '0'
  AND CONVERT(VARCHAR(4),b.iYear,120) >= #para(startyear)
  AND CONVERT(VARCHAR(4),b.iYear,120) <= #para(endyear)
    #if(cworkname)
        AND e.cWorkName LIKE CONCAT('%', #para(cworkname), '%')
    #end
    #if(cinvcode)
        AND c.cInvCode LIKE CONCAT('%', #para(cinvcode), '%')
    #end
    #if(cinvcode1)
        AND c.cInvCode1 LIKE CONCAT('%', #para(cinvcode1), '%')
    #end
    #if(cinvname1)
        AND c.cInvName1 LIKE CONCAT('%', #para(cinvname1), '%')
    #end
GROUP BY d.iWorkRegionMid,e.cWorkCode,e.cWorkName,a.iInventoryId,c.cInvCode,c.cInvCode1,c.cInvName1,b.iType,b.iYear,b.iMonth
ORDER BY e.cWorkCode,c.cInvCode,b.iType,b.iYear,b.iMonth
#end



###---------------------------------------------------------月周生产计划排产---------------------

#sql("getCusOrderSumList")
###根据层级及销售类型获取客户计划汇总表数据
SELECT
    t.iYear,
    t.iMonth,
    t.iDate,
    t.iQty3,
    a.iAutoId AS invId,
    a.cInvCode,
    a.cInvCode1,
    a.cInvName1,
    a.iSaleType,
    b.iWorkRegionMid,
    c.cWorkCode,
    c.cWorkName,
    c.iPsLevel
FROM Co_CusOrderSum AS t
         LEFT JOIN Bd_Inventory AS a ON a.iAutoId = t.iInventoryId
         LEFT JOIN Bd_InventoryWorkRegion AS b ON a.iAutoId = b.iInventoryId AND b.isDefault = 1 AND b.isDeleted = 0
         LEFT JOIN Bd_WorkRegionM AS c ON b.iWorkRegionMid = c.iAutoId AND c.isDeleted = 0
WHERE
    c.iPsLevel = #para(ipslevel)
    #if(isaletype)
        AND a.iSaleType = #para(isaletype)
    #end
    AND t.iYear >= #para(startyear) AND t.iYear <= #para(endyear)
    AND t.iMonth >= #para(startmonth) AND t.iMonth <= #para(endmonth)
    AND t.iDate >= #para(startday) AND t.iDate <= #para(endday)
ORDER BY c.cWorkCode,a.cInvCode,t.iYear,t.iMonth,t.iDate
#end

#sql("getApsWeekschedule")
###获取当前层级上次排产截止日期
SELECT TOP 1 iLevel,dScheduleEndTime,dLockEndTime
FROM Aps_WeekSchedule
WHERE iLevel = #para(level)
ORDER BY dCreateTime DESC
#end

#sql("getInvCapacityList")
###根据物料集查询各班次产能
SELECT b.cInvCode,c.cWorkShiftCode,c.cWorkShiftName,a.iCapacity
FROM Bd_InventoryCapacity AS a
         LEFT JOIN Bd_Inventory AS b ON a.iInventoryId = b.iAutoId
         LEFT JOIN Bd_WorkShiftM AS c ON a.iWorkShiftMid = c.iAutoId
WHERE a.iInventoryId IN #(ids)
#end

















#sql("getCalendarMonthNumList")
###查询年度每月的工作天数
SELECT
    CONVERT(VARCHAR(4),dTakeDate,120) AS dTakeYear,
    SUBSTRING(CONVERT(VARCHAR(7),dTakeDate,120), 6, 2) AS month,
COUNT(dTakeDate) AS monthNum
FROM Bd_Calendar
WHERE iCaluedarType = #para(icaluedartype)
  AND cSourceCode = #para(csourcecode)
  AND CONVERT(VARCHAR(4),dTakeDate,120) >= #para(startyear)
  AND CONVERT(VARCHAR(4),dTakeDate,120) <= #para(endyear)
GROUP BY
    CONVERT(VARCHAR(4),dTakeDate,120),
    SUBSTRING(CONVERT(VARCHAR(7),dTakeDate,120), 6, 2)
ORDER BY
    CONVERT(VARCHAR(4),dTakeDate,120)
#end

#sql("getCalendarYearNumList")
###查询年度的工作天数
SELECT
    CONVERT(VARCHAR(4),dTakeDate,120) AS dTakeYear,
    COUNT(dTakeDate) AS yearNum
FROM Bd_Calendar
WHERE iCaluedarType = #para(icaluedartype)
  AND cSourceCode = #para(csourcecode)
  AND CONVERT(VARCHAR(4),dTakeDate,120) >= #para(startyear)
  AND CONVERT(VARCHAR(4),dTakeDate,120) <= #para(endyear)
GROUP BY
    CONVERT(VARCHAR(4),dTakeDate,120)
ORDER BY
    CONVERT(VARCHAR(4),dTakeDate,120)
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
