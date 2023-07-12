###---------------------------------------------------------年度生产计划排产---------------------

#sql("getSourceYearOrderList")
###根据客户id集查询年度订单 并进行行转列
SELECT iCustomerId,cCusCode,cCusName,iEquipmentModelId,cEquipmentModelCode,cEquipmentModelName,
       iInventoryId,cInvCode,cInvCode1,cInvName1,nowyear,nextyear,planTypeCode,
       SUM(nowmonth1) AS nowmonth1,SUM(nowmonth2) AS nowmonth2,SUM(nowmonth3) AS nowmonth3,SUM(nowmonth4) AS nowmonth4,
       SUM(nowmonth5) AS nowmonth5,SUM(nowmonth6) AS nowmonth6,SUM(nowmonth7) AS nowmonth7,SUM(nowmonth8) AS nowmonth8,
       SUM(nowmonth9) AS nowmonth9,SUM(nowmonth10) AS nowmonth10,SUM(nowmonth11) AS nowmonth11,SUM(nowmonth12) AS nowmonth12,
       SUM(nowMonthSum) AS nowMonthSum,SUM(nextmonth1) AS nextmonth1,SUM(nextmonth2) AS nextmonth2,SUM(nextmonth3) AS nextmonth3,
       SUM(nextmonth4) AS nextmonth4
FROM (
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
         WHERE a.isDeleted = '0' AND a.iAuditStatus = 2
           AND a.iYear = #para(startyear) AND a.iCustomerId IN (#(customerids))
           AND a.iAutoId IN (SELECT MAX(iAutoId) AS iAutoId
                             FROM Co_AnnualOrderM
                             WHERE isDeleted = '0' AND iAuditStatus = 2
                             AND iYear >= #para(startyear) AND iCustomerId IN (#(customerids))
                             GROUP BY iCustomerId,iYear )
     ) AS t
GROUP BY
    iCustomerId,cCusCode,cCusName,iEquipmentModelId,cEquipmentModelCode,cEquipmentModelName,
    iInventoryId,cInvCode,cInvCode1,cInvName1,nowyear,nextyear,planTypeCode
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

#sql("getLastYearZKQtyList")
###根据客户id集查询上一年度12月份的在库计划数
SELECT a.iCustomerId,d.cCusCode,d.cCusName,
       b.iInventoryId,e.cInvCode,e.cInvCode1,e.cInvName1,
       planTypeCode = CASE WHEN c.iType = 3 THEN 'ZK'
                           ELSE '' END,
       c.iYear,c.iMonth,c.iQty
FROM Aps_AnnualPlanM AS a
         LEFT JOIN Aps_AnnualPlanD AS b ON a.iAutoId = b.iAnnualPlanMid
         LEFT JOIN Aps_AnnualPlanD_Qty AS c ON b.iAutoId = c.iAnnualPlanDid
         LEFT JOIN Bd_Customer AS d ON a.iCustomerId = d.iAutoId
         LEFT JOIN Bd_Inventory AS e ON b.iInventoryId = e.iAutoId
WHERE a.isDeleted = '0'
  AND c.iType = 3
  AND CONVERT(VARCHAR(4),c.iYear,120) = #para(lastyear)
  AND c.iMonth = '12'
  AND a.iCustomerId IN (#(customerids))
  AND b.iInventoryId IN (#(invids))
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

#sql("getApsYearPlanMasterList")
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

#sql("getApsYearPlanSumList")
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
WHERE b.iType = 2 AND f.isDeleted = '0' AND f.iAuditStatus = 2
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

#sql("getInvInfoByLevelList")
###根据层级查询物料集信息
SELECT
    a.iAutoId AS invId, ###物料id
    a.cInvCode,  ###物料编码
    a.cInvCode1,  ###客户部番
    a.cInvName1,  ###部品名称
    a.iEquipmentModelId,  ###机型id
    g.cEquipmentModelCode,  ###机型编码
    g.cEquipmentModelName,  ###机型名称
    a.iSaleType,  ###销售类型
    e.cProdCalendarTypeSn,  ###生产日历
    e.cSupplyCalendarTypeSn,  ###供应日历
    f.iInnerInStockDays,  ###标准在库天数
    f.iMinInStockDays,  ###最小在库天数
    f.iMaxInStockDays,  ###最大在库天数
    b.iWorkRegionMid,  ###默认产线id
    c.cWorkCode,  ###产线编码
    c.cWorkName,  ###产线名称
    c.iPsLevel,  ###排产层级
    b.iDepId,  ###部门id
    d.cDepCode,  ###部门编码
    d.cDepName  ###部门名称
FROM Bd_Inventory AS a
         LEFT JOIN Bd_InventoryWorkRegion AS b ON a.iAutoId = b.iInventoryId AND b.isDefault = 1 AND b.isDeleted = 0
         LEFT JOIN Bd_WorkRegionM AS c ON b.iWorkRegionMid = c.iAutoId AND c.isDeleted = 0
         LEFT JOIN Bd_Department AS d ON b.iDepId = d.iAutoId AND d.isDeleted = 0
         LEFT JOIN Bd_InventoryMfgInfo AS e ON e.iInventoryId = a.iAutoId
         LEFT JOIN Bd_InventoryPlan AS f ON f.iInventoryId = a.iAutoId
         LEFT JOIN Bd_EquipmentModel AS g ON a.iEquipmentModelId = g.iAutoId AND g.IsDeleted = 0
WHERE a.isDeleted = 0
  AND c.iPsLevel = #para(ipslevel)
    #if(isaletype)
        AND a.iSaleType = #para(isaletype)
    #end
#end

#sql("getPinvInfoByinvList")
###根据物料集id查询父级物料信息及用量
SELECT
    c.iInventoryId AS invId,
    d.cInvCode AS invCode,
    a.iInventoryId AS pinvId,
    b.cInvCode AS pinvCode,
    c.iBaseQty AS iQty
FROM Bd_BomM AS a
         LEFT JOIN Bd_Inventory AS b ON a.iInventoryId = b.iAutoId
         LEFT JOIN Bd_BomD AS c ON a.iAutoId = c.iPid
         LEFT JOIN Bd_Inventory AS d ON c.iInventoryId = d.iAutoId
WHERE a.isDeleted = 0
  AND CONVERT(DATE, a.dEnableDate) <= CONVERT(DATE, GETDATE())
  AND CONVERT(DATE, a.dDisableDate) >= CONVERT(DATE, GETDATE())
  AND c.iInventoryId IN #(ids)
#end

#sql("getCusOrderSumList")
###根据物料集id及日期获取客户计划汇总表数据
SELECT
    t.iYear,
    t.iMonth,
    t.iDate,
    ISNULL(t.iQty3,0) AS iQty3,
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
    t.iInventoryId IN #(ids)
    AND
		(CAST(t.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN t.iMonth<10 THEN '0'+CAST(t.iMonth AS NVARCHAR(30) )
		ELSE CAST(t.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN t.iDate<10 THEN '0'+CAST(t.iDate AS NVARCHAR(30) )
		ELSE CAST(t.iDate AS NVARCHAR(30) )
		END AS NVARCHAR(30)) ) >= #para(startdate)
	AND
		(CAST(t.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN t.iMonth<10 THEN '0'+CAST(t.iMonth AS NVARCHAR(30) )
		ELSE CAST(t.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN t.iDate<10 THEN '0'+CAST(t.iDate AS NVARCHAR(30) )
		ELSE CAST(t.iDate AS NVARCHAR(30) )
		END AS NVARCHAR(30)) ) <= #para(enddate)
#end

#sql("getWeekScheduSumList")
###根据物料集id及日期获取月周生产计划表数据
SELECT
    b.iYear,
    b.iMonth,
    b.iDate,
    (b.iQty2 + b.iQty3 + b.iQty4) AS iQty3,
    a.iInventoryId AS invId,
    c.cInvCode,
    c.cInvCode1,
    c.cInvName1,
    c.iSaleType,
    d.iWorkRegionMid,
    e.cWorkCode,
    e.cWorkName,
    e.iPsLevel
FROM Aps_WeekScheduleDetails AS a
         LEFT JOIN Aps_WeekScheduleD_Qty AS b ON a.iAutoId = b.iWeekScheduleDid
         LEFT JOIN Bd_Inventory AS c ON a.iInventoryId = c.iAutoId
         LEFT JOIN Bd_InventoryWorkRegion AS d ON c.iAutoId = d.iInventoryId AND d.isDefault = 1 AND d.isDeleted = 0
         LEFT JOIN Bd_WorkRegionM AS e ON d.iWorkRegionMid = e.iAutoId AND e.isDeleted = 0
WHERE a.isDeleted = '0'
  AND a.iInventoryId IN #(ids)
  AND
      (CAST(b.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN b.iMonth<10 THEN '0'+CAST(b.iMonth AS NVARCHAR(30) )
      ELSE CAST(b.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN b.iDate<10 THEN '0'+CAST(b.iDate AS NVARCHAR(30) )
      ELSE CAST(b.iDate AS NVARCHAR(30) )
      END AS NVARCHAR(30)) ) >= #para(startdate)
  AND
      (CAST(b.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN b.iMonth<10 THEN '0'+CAST(b.iMonth AS NVARCHAR(30) )
      ELSE CAST(b.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN b.iDate<10 THEN '0'+CAST(b.iDate AS NVARCHAR(30) )
      ELSE CAST(b.iDate AS NVARCHAR(30) )
      END AS NVARCHAR(30)) ) <= #para(enddate)
#end

#sql("getApsWeekschedule")
###获取当前层级上次排产截止日期
SELECT TOP 1 *
FROM Aps_WeekSchedule
WHERE IsDeleted = 0 AND iLevel = #para(level)
ORDER BY dCreateTime DESC
#end

#sql("getCalendarDateList")
###根据日历类型字典查询工作日历集合
SELECT CONVERT(VARCHAR(10),dTakeDate,120) AS dTakeDate
FROM Bd_Calendar
WHERE iCaluedarType = '1'
  AND cSourceCode = #para(csourcecode)
  AND CONVERT(VARCHAR(10),dTakeDate,120) >= #para(startdate)
  AND CONVERT(VARCHAR(10),dTakeDate,120) <= #para(enddate)
#end

#sql("getInvCapacityList")
###根据物料集查询各班次产能
SELECT b.cInvCode,c.iAutoId AS iWorkShiftMid,c.cWorkShiftCode,c.cWorkShiftName,a.iCapacity
FROM Bd_InventoryCapacity AS a
         LEFT JOIN Bd_Inventory AS b ON a.iInventoryId = b.iAutoId
         LEFT JOIN Bd_WorkShiftM AS c ON a.iWorkShiftMid = c.iAutoId
WHERE a.iInventoryId IN #(ids)
#end

#sql("getLastDateZKQtyList")
###根据物料集查询前一天的在库计划数
SELECT a.iInventoryId,c.cInvCode,
       b.iYear,b.iMonth,b.iDate,b.iQty5
FROM Aps_WeekScheduleDetails AS a
         LEFT JOIN Aps_WeekScheduleD_Qty AS b ON a.iAutoId = b.iWeekScheduleDid
         LEFT JOIN Bd_Inventory AS c ON a.iInventoryId = c.iAutoId
WHERE a.isDeleted = '0'
  AND CONVERT(VARCHAR(4),b.iYear,120) = #para(lastyear)
  AND b.iMonth = #para(lastmonth)
  AND b.iDate = #para(lastday)
  AND a.iInventoryId IN #(ids)
#end

#sql("getYearMonthQtySumByinvList")
###根据物料集id及年月查询年度生产计划
SELECT a.iInventoryId,c.cInvCode,c.cInvCode1,c.cInvName1,
       planTypeCode = CASE WHEN b.iType = 2 THEN 'CP' ELSE '' END,
       b.iYear,b.iMonth,SUM(b.iQty) AS iQty
FROM Aps_AnnualPlanD AS a
         LEFT JOIN Aps_AnnualPlanD_Qty AS b ON a.iAutoId = b.iAnnualPlanDid
         LEFT JOIN Bd_Inventory AS c ON a.iInventoryId = c.iAutoId
         LEFT JOIN Bd_InventoryWorkRegion AS d ON a.iAutoId = d.iInventoryId AND d.isDefault = '1' AND d.isDeleted = '0'
         LEFT JOIN Bd_WorkRegionM AS e ON d.iWorkRegionMid = e.iAutoId
         LEFT JOIN Aps_AnnualPlanM AS f ON a.iAnnualPlanMid = f.iAutoId
WHERE b.iType = 2 AND f.isDeleted = '0'
  AND b.iYear = #para(nextyear)
  AND b.iMonth = #para(nextmonth)
  AND a.iInventoryId IN #(ids)
GROUP BY d.iWorkRegionMid,e.cWorkCode,e.cWorkName,a.iInventoryId,c.cInvCode,c.cInvCode1,c.cInvName1,b.iType,b.iYear,b.iMonth
ORDER BY e.cWorkCode,c.cInvCode,b.iType,b.iYear,b.iMonth
#end

#sql("getConfigValue")
###查询全局参数
SELECT config_value FROM #(getBaseDbName()).dbo.jb_global_config WHERE config_key = #para(configkey)
#end


#sql("getApsWeekscheduleList")
###获取月周排产历史纪录
SELECT iAutoId,iOrgId,cOrgCode,cOrgName,iLevel,isLocked,iCreateBy,cCreateName,dCreateTime,iUpdateBy,cUpdateName,dUpdateTime,IsDeleted,
       CONVERT(VARCHAR(10),dScheduleBeginTime,120) AS dScheduleBeginTime,
       CONVERT(VARCHAR(10),dScheduleEndTime,120) AS dScheduleEndTime,
       CONVERT(VARCHAR(10),dLockEndTime,120) AS dLockEndTime
FROM Aps_WeekSchedule
WHERE IsDeleted = 0
ORDER BY iLevel ASC
#end


#sql("getWeekScheduPlanList")
###根据层级及日期获取月周生产计划表数据
SELECT
    b.iYear,
    b.iMonth,
    b.iDate,
    b.iQty1,
    b.iQty2,
    b.iQty3,
    b.iQty4,
    b.iQty5,
    b.iQty6,
    b.isLocked,
    a.iInventoryId AS invId,
    c.cInvCode,
    c.cInvCode1,
    c.cInvName1,
    d.iWorkRegionMid,
    e.cWorkCode,
    e.cWorkName,
    f.iInnerInStockDays,
    a.iLevel AS iPsLevel
    ###e.iPsLevel
FROM Aps_WeekScheduleDetails AS a
         LEFT JOIN Aps_WeekScheduleD_Qty AS b ON a.iAutoId = b.iWeekScheduleDid
         LEFT JOIN Bd_Inventory AS c ON a.iInventoryId = c.iAutoId
         LEFT JOIN Bd_InventoryWorkRegion AS d ON c.iAutoId = d.iInventoryId AND d.isDefault = 1 AND d.isDeleted = 0
         LEFT JOIN Bd_WorkRegionM AS e ON d.iWorkRegionMid = e.iAutoId AND e.isDeleted = 0
         LEFT JOIN Bd_InventoryPlan AS f ON c.iAutoId = f.iInventoryId
WHERE a.isDeleted = '0'
  AND a.iLevel = #para(level)
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
  AND
      (CAST(b.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN b.iMonth<10 THEN '0'+CAST(b.iMonth AS NVARCHAR(30) )
      ELSE CAST(b.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN b.iDate<10 THEN '0'+CAST(b.iDate AS NVARCHAR(30) )
      ELSE CAST(b.iDate AS NVARCHAR(30) )
      END AS NVARCHAR(30)) ) >= #para(startdate)
  AND
      (CAST(b.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN b.iMonth<10 THEN '0'+CAST(b.iMonth AS NVARCHAR(30) )
      ELSE CAST(b.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN b.iDate<10 THEN '0'+CAST(b.iDate AS NVARCHAR(30) )
      ELSE CAST(b.iDate AS NVARCHAR(30) )
      END AS NVARCHAR(30)) ) <= #para(enddate)
#end

#sql("getApsWeekscheduleLock")
###获取当前层级上次排产锁定日期
SELECT TOP 1 iLevel,dScheduleEndTime,dLockEndTime
FROM Aps_WeekSchedule
WHERE IsDeleted = 0 AND iLevel = #para(level)
ORDER BY dLockEndTime DESC
#end

#sql("getApsScheduPlanList")
###根据层级及日期获取月周生产计划表数据及部门信息
SELECT
    b.iYear,
    b.iMonth,
    b.iDate,
    b.iQty2 AS Qty1S,
    b.iQty3 AS Qty2S,
    b.iQty4 AS Qty3S,
    b.isLocked,
    a.iInventoryId AS invId,
    c.cInvCode,
    c.cInvCode1,
    c.cInvName1,
    d.iWorkRegionMid,
    e.cWorkCode,
    e.cWorkName,
    d.iDepId,
    f.cDepCode,
    f.cDepName,
    g.iAutoId AS iInventoryRoutingId,
    g.cRoutingName,
    g.cVersion
FROM Aps_WeekScheduleDetails AS a
         LEFT JOIN Aps_WeekScheduleD_Qty AS b ON a.iAutoId = b.iWeekScheduleDid
         LEFT JOIN Bd_Inventory AS c ON a.iInventoryId = c.iAutoId
         LEFT JOIN Bd_InventoryWorkRegion AS d ON c.iAutoId = d.iInventoryId AND d.isDefault = 1 AND d.isDeleted = 0
         LEFT JOIN Bd_WorkRegionM AS e ON d.iWorkRegionMid = e.iAutoId AND e.isDeleted = 0
         LEFT JOIN Bd_Department AS f ON d.iDepId = f.iAutoId
         LEFT JOIN Bd_InventoryRouting AS g ON c.iAutoId = g.iInventoryId AND g.isEnabled = 1
WHERE a.isDeleted = '0'
  AND a.iLevel = #para(level)
  AND
        (CAST(b.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN b.iMonth<10 THEN '0'+CAST(b.iMonth AS NVARCHAR(30) )
        ELSE CAST(b.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN b.iDate<10 THEN '0'+CAST(b.iDate AS NVARCHAR(30) )
        ELSE CAST(b.iDate AS NVARCHAR(30) )
        END AS NVARCHAR(30)) ) >= #para(startdate)
  AND
        (CAST(b.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN b.iMonth<10 THEN '0'+CAST(b.iMonth AS NVARCHAR(30) )
        ELSE CAST(b.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN b.iDate<10 THEN '0'+CAST(b.iDate AS NVARCHAR(30) )
        ELSE CAST(b.iDate AS NVARCHAR(30) )
        END AS NVARCHAR(30)) ) <= #para(enddate)
#end

#sql("getInvRoutingList")
###根据工艺路线id集查询各存货工艺路线
SELECT b.cInvCode,a.*
FROM Bd_InventoryRouting AS a
         LEFT JOIN Bd_Inventory AS b ON a.iInventoryId = b.iAutoId
WHERE a.iAutoId IN #(ids)
#end

#sql("getInvRoutingConfigList")
###根据工艺路线id集查询各存货工艺路线配置
SELECT *
FROM Bd_InventoryRoutingConfig
WHERE isEnabled = 1
  AND iInventoryRoutingId IN #(ids)
#end

#sql("getInvRoutingConfigOperationList")
###根据工艺路线配置id集查询各工艺工序
SELECT *
FROM Bd_InventoryRoutingConfig_Operation
WHERE iInventoryRoutingConfigId IN #(ids)
#end

#sql("getInvRoutingConfigEquipmentList")
###根据工艺路线配置id集查询各工艺设备
SELECT *
FROM Bd_InventoryRoutingEquipment
WHERE iInventoryRoutingConfigId IN #(ids)
#end

#sql("getInvRoutingConfigInvcList")
###根据工艺路线配置id集查询各工艺物料
SELECT *
FROM Bd_InventoryRoutingInvc
WHERE iInventoryRoutingConfigId IN #(ids)
#end

#sql("getInvRoutingConfigSopList")
###根据工艺路线配置id集查询各工艺作业指导书
SELECT *
FROM Bd_InventoryRoutingSop
WHERE isEnabled = 1
  AND iInventoryRoutingConfigId IN #(ids)
#end


#sql("getMotaskByEndDateList")
###查询任务工单表结束日期 > 解锁开始日期&&未审核数据
SELECT iAutoId,dBeginDate,dEndDate
FROM Mo_MoTask
WHERE iAuditStatus = 0
  AND CONVERT(VARCHAR(10),dEndDate,120) > #para(unlockstartdate)
#end

#sql("getScheduDByinvsList")
###根据物料集查询排产物料明细
SELECT
    c.cInvCode,b.iAutoId AS iWeekScheduleDid
FROM Aps_WeekScheduleDetails AS b
         LEFT JOIN Bd_Inventory AS c ON b.iInventoryId = c.iAutoId
WHERE c.cInvCode IN #(invs)
#end

#sql("getScheduPlanBydidsList")
###根据排产明细id查询排产结果纪录
SELECT
    (CAST(iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN iMonth<10 THEN '0'+CAST(iMonth AS NVARCHAR(30) )
    ELSE CAST(iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN iDate<10 THEN '0'+CAST(iDate AS NVARCHAR(30) )
    ELSE CAST(iDate AS NVARCHAR(30) )
    END AS NVARCHAR(30)) ) AS planDate,
    iWeekScheduleDid,iAutoId AS qtyId
FROM Aps_WeekScheduleD_Qty
WHERE iWeekScheduleDid IN #(schedudids)
#end


###---------------------------------------------------------月周生产计划汇总---------------------

#sql("getApsMonthPlanSumList")
###根据日期及条件获取月周生产计划表数据三班汇总
SELECT
    b.iYear,
    b.iMonth,
    b.iDate,
    (b.iQty2 + b.iQty3 + b.iQty4) AS iQty3,
    a.iInventoryId AS invId,
    c.cInvCode,
    c.cInvCode1,
    c.cInvName1,
    c.iSaleType,
    d.iWorkRegionMid,
    e.cWorkCode,
    e.cWorkName,
    e.iPsLevel
FROM Aps_WeekScheduleDetails AS a
         LEFT JOIN Aps_WeekScheduleD_Qty AS b ON a.iAutoId = b.iWeekScheduleDid
         LEFT JOIN Bd_Inventory AS c ON a.iInventoryId = c.iAutoId
         LEFT JOIN Bd_InventoryWorkRegion AS d ON c.iAutoId = d.iInventoryId AND d.isDefault = 1 AND d.isDeleted = 0
         LEFT JOIN Bd_WorkRegionM AS e ON d.iWorkRegionMid = e.iAutoId AND e.isDeleted = 0
WHERE a.isDeleted = '0'
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
  AND
      (CAST(b.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN b.iMonth<10 THEN '0'+CAST(b.iMonth AS NVARCHAR(30) )
      ELSE CAST(b.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN b.iDate<10 THEN '0'+CAST(b.iDate AS NVARCHAR(30) )
      ELSE CAST(b.iDate AS NVARCHAR(30) )
      END AS NVARCHAR(30)) ) >= #para(startdate)
  AND
      (CAST(b.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN b.iMonth<10 THEN '0'+CAST(b.iMonth AS NVARCHAR(30) )
      ELSE CAST(b.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN b.iDate<10 THEN '0'+CAST(b.iDate AS NVARCHAR(30) )
      ELSE CAST(b.iDate AS NVARCHAR(30) )
      END AS NVARCHAR(30)) ) <= #para(enddate)
#end

#sql("getInvMergeRateSumList")
###根据物料集查询默认工艺路线工序的人数汇总
SELECT a.iAutoId,a.cInvCode,SUM(c.iMergeRate) AS iMergeRateSum
FROM Bd_Inventory AS a
         LEFT JOIN Bd_InventoryRouting AS b ON a.iAutoId = b.iInventoryId
         LEFT JOIN Bd_InventoryRoutingConfig AS c ON b.iAutoId = c.iInventoryRoutingId
WHERE b.isEnabled = 1 AND c.isEnabled = 1
  AND a.iAutoId IN #(ids)
GROUP BY a.iAutoId,a.cInvCode
#end

###---------------------------------------------------------生产计划及实绩管理---------------------

#sql("getApsMonthPlanList")
###根据日期及条件获取月周生产计划表数据
SELECT
    b.iYear,
    b.iMonth,
    b.iDate,
    b.iQty1 AS QtyPP,
    b.iQty2 AS Qty1S,
    b.iQty3 AS Qty2S,
    b.iQty4 AS Qty3S,
    b.iQty5 AS QtyZK,
    (b.iQty2 + b.iQty3 + b.iQty4) AS QtySUM,
    a.iInventoryId AS invId,
    c.cInvCode,
    c.cInvCode1,
    c.cInvName1,
    c.iSaleType,
    d.iWorkRegionMid,
    e.cWorkCode,
    e.cWorkName,
    e.iPsLevel
FROM Aps_WeekScheduleDetails AS a
         LEFT JOIN Aps_WeekScheduleD_Qty AS b ON a.iAutoId = b.iWeekScheduleDid
         LEFT JOIN Bd_Inventory AS c ON a.iInventoryId = c.iAutoId
         LEFT JOIN Bd_InventoryWorkRegion AS d ON c.iAutoId = d.iInventoryId AND d.isDefault = 1 AND d.isDeleted = 0
         LEFT JOIN Bd_WorkRegionM AS e ON d.iWorkRegionMid = e.iAutoId AND e.isDeleted = 0
WHERE a.isDeleted = '0'
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
  AND
      (CAST(b.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN b.iMonth<10 THEN '0'+CAST(b.iMonth AS NVARCHAR(30) )
      ELSE CAST(b.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN b.iDate<10 THEN '0'+CAST(b.iDate AS NVARCHAR(30) )
      ELSE CAST(b.iDate AS NVARCHAR(30) )
      END AS NVARCHAR(30)) ) >= #para(startdate)
  AND
      (CAST(b.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN b.iMonth<10 THEN '0'+CAST(b.iMonth AS NVARCHAR(30) )
      ELSE CAST(b.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN b.iDate<10 THEN '0'+CAST(b.iDate AS NVARCHAR(30) )
      ELSE CAST(b.iDate AS NVARCHAR(30) )
      END AS NVARCHAR(30)) ) <= #para(enddate)
#end


#sql("getMoDocMonthActualList")
###根据物料集及日期及条件获取APS下发的计划工单实绩数(三个班次 已完工数)
SELECT
    a.dPlanDate,
    a.iYear,
    a.iMonth,
    a.iDate,
    a.iQty,
    a.iCompQty,
    a.iInventoryId AS invId,
    c.cInvCode,
    c.cInvCode1,
    c.cInvName1,
    a.iWorkShiftMid,
    d.cWorkShiftCode,
    d.cWorkShiftName
FROM Mo_MoDoc AS a
         LEFT JOIN Mo_MoTask AS b ON a.iMoTaskId = b.iAutoId
         LEFT JOIN Bd_Inventory AS c ON a.iInventoryId = c.iAutoId
         LEFT JOIN Bd_WorkShiftM AS d ON a.iWorkShiftMid = d.iAutoId
WHERE b.IsDeleted = 0
  AND a.iType = 1
  AND a.iInventoryId IN #(ids)
  AND CONVERT(VARCHAR(10),a.dPlanDate,120) >= #para(startdate)
  AND CONVERT(VARCHAR(10),a.dPlanDate,120) <= #para(enddate)
#end

#sql("getMoDocMonthActualSumList")
###根据物料集及日期及条件获取APS下发的计划工单实绩数(三个班次汇总 已完工数)
SELECT
    a.dPlanDate,
    a.iYear,
    a.iMonth,
    a.iDate,
    SUM(a.iQty) AS QtySUM,
    SUM(a.iCompQty) AS CompQtySUM,
    a.iInventoryId AS invId,
    c.cInvCode,
    c.cInvCode1,
    c.cInvName1
FROM Mo_MoDoc AS a
         LEFT JOIN Mo_MoTask AS b ON a.iMoTaskId = b.iAutoId
         LEFT JOIN Bd_Inventory AS c ON a.iInventoryId = c.iAutoId
WHERE b.IsDeleted = 0
  AND a.iType = 1
  AND a.iInventoryId IN #(ids)
  AND CONVERT(VARCHAR(10),a.dPlanDate,120) >= #para(startdate)
  AND CONVERT(VARCHAR(10),a.dPlanDate,120) <= #para(enddate)
GROUP BY
    a.dPlanDate,a.iYear,a.iMonth,a.iDate,
    a.iInventoryId,c.cInvCode,c.cInvCode1,c.cInvName1
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
