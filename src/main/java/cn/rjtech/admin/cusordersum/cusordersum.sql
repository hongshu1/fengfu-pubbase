#sql("paginateAdminDatas")
SELECT
	ccs.iInventoryId,
	ccs.iYear,
	bi.cinvcode,
	bi.cinvcode1,
	bi.cinvname1
FROM
	Co_CusOrderSum ccs
	LEFT JOIN Bd_Inventory bi ON ccs.iInventoryId = bi.iAutoId
	AND bi.isDeleted=0
WHERE NOT EXISTS ( SELECT 1 FROM Co_CusOrderSum WHERE iInventoryId = ccs.iInventoryId AND iAutoId < ccs.iAutoId )
   #if(cinvcode)
      and  bi.cinvcode  LIKE CONCAT('%',#para(cinvcode), '%')
  #end
     #if(cinvcode1)
      and  bi.cinvcode1  LIKE CONCAT('%',#para(cinvcode1), '%')
  #end
     #if(cinvname1)
      and  bi.cinvname1  LIKE CONCAT('%',#para(cinvname1), '%')
  #end
  #if(dateMap)
    and (
    #for (d: dateMap)
       #(for.first ? "" : "or") (iYear = '#(d.key)' and iMonth in ( #(d.value) ))
    #end )
  #end
ORDER BY ccs.dCreateTime
#end

#sql("getYearMouth")
SELECT
	iYear,
	iMonth,
	MAX(iDate) as iDate
FROM
	Co_CusOrderSum
WHERE 1=1
	GROUP BY iYear,iMonth
	ORDER BY iYear
#end

#sql("getiQty1")
    SELECT
        iMonth,iyear,iInventoryId,SUM (  iQty1  ) as iQtySum,
        #for (x : roleArray.split(','))
            SUM(CASE iDate WHEN '#(x)' THEN iQty1 ELSE 0 END) as 'iQty#(x)' #(for.last?'':',')
        #end
        FROM
            Co_CusOrderSum
        WHERE 1=1
          #if(iInventoryId)
              and  iInventoryId  = #para(iInventoryId)
          #end
          #if(dateMap)
            and (
            #for (d: dateMap)
               #(for.first ? "" : "or") (iYear = '#(d.key)' and iMonth in ( #(d.value) ))
            #end )
          #end
    GROUP BY
        iMonth,iyear,iInventoryId
    ORDER BY
	    iMonth
#end

#sql("getiQty2")
    SELECT
        iMonth,iyear,iInventoryId,SUM (  iQty2  ) as iQtySum,
        #for (x : roleArray.split(','))
            SUM(CASE iDate WHEN '#(x)' THEN iQty2 ELSE 0 END) as 'iQty#(x)' #(for.last?'':',')
        #end
        FROM
            Co_CusOrderSum
        WHERE 1=1
          #if(iInventoryId)
              and  iInventoryId  = #para(iInventoryId)
          #end
          #if(dateMap)
            and (
            #for (d: dateMap)
               #(for.first ? "" : "or") (iYear = '#(d.key)' and iMonth in ( #(d.value) ))
            #end )
          #end
    GROUP BY
        iMonth,iyear,iInventoryId
    ORDER BY
	    iMonth
#end

#sql("getIDate")
   SELECT
	iDate
FROM
	Co_CusOrderSum
WHERE 1=1
    #if(iMonth)
        and iMonth = #para(iMonth)
    #end
    #if(iYear)
        and iYear = #para(iYear)
    #end
GROUP BY
	iYear,
	iMonth,
	iDate
ORDER BY
	iYear
#end


###-----------------------------------------------------------客户计划汇总------------------------------------------------
#sql("getYearOrderList")
###根据年份获取客户年度订单 相同客户相同物料相同年月汇总
SELECT iCustomerId,cCusCode,cCusName,
       iInventoryId,cInvCode,cInvCode1,cInvName1,
    year,
    SUM(month1) AS month1,SUM(month2) AS month2,SUM(month3) AS month3,SUM(month4) AS month4,
    SUM(month5) AS month5,SUM(month6) AS month6,SUM(month7) AS month7,SUM(month8) AS month8,
    SUM(month9) AS month9,SUM(month10) AS month10,SUM(month11) AS month11,SUM(month12) AS month12
FROM (
    SELECT a.iCustomerId,d.cCusCode,d.cCusName,
    b.iInventoryId,e.cInvCode,e.cInvCode1,e.cInvName1,
    b.iYear1 AS year,
    (
    SELECT iQty
    FROM Co_AnnualOrderD_Qty
    WHERE iAnnualOrderDid = b.iautoid
    AND iyear = a.iyear
    AND imonth = 1
    ) AS month1,
    (
    SELECT iQty
    FROM Co_AnnualOrderD_Qty
    WHERE iAnnualOrderDid = b.iautoid
    AND iyear = a.iyear
    AND imonth = 2
    ) AS month2,
    (
    SELECT iQty
    FROM Co_AnnualOrderD_Qty
    WHERE iAnnualOrderDid = b.iautoid
    AND iyear = a.iyear
    AND imonth = 3
    ) AS month3,
    (
    SELECT iQty
    FROM Co_AnnualOrderD_Qty
    WHERE iAnnualOrderDid = b.iautoid
    AND iyear = a.iyear
    AND imonth = 4
    ) AS month4,
    (
    SELECT iQty
    FROM Co_AnnualOrderD_Qty
    WHERE iAnnualOrderDid = b.iautoid
    AND iyear = a.iyear
    AND imonth = 5
    ) AS month5,
    (
    SELECT iQty
    FROM Co_AnnualOrderD_Qty
    WHERE iAnnualOrderDid = b.iautoid
    AND iyear = a.iyear
    AND imonth = 6
    ) AS month6,
    (
    SELECT iQty
    FROM Co_AnnualOrderD_Qty
    WHERE iAnnualOrderDid = b.iautoid
    AND iyear = a.iyear
    AND imonth = 7
    ) AS month7,
    (
    SELECT iQty
    FROM Co_AnnualOrderD_Qty
    WHERE iAnnualOrderDid = b.iautoid
    AND iyear = a.iyear
    AND imonth = 8
    ) AS month8,
    (
    SELECT iQty
    FROM Co_AnnualOrderD_Qty
    WHERE iAnnualOrderDid = b.iautoid
    AND iyear = a.iyear
    AND imonth = 9
    ) AS month9,
    (
    SELECT iQty
    FROM Co_AnnualOrderD_Qty
    WHERE iAnnualOrderDid = b.iautoid
    AND iyear = a.iyear
    AND imonth = 10
    ) AS month10,
    (
    SELECT iQty
    FROM Co_AnnualOrderD_Qty
    WHERE iAnnualOrderDid = b.iautoid
    AND iyear = a.iyear
    AND imonth = 11
    ) AS month11,
    (
    SELECT iQty
    FROM Co_AnnualOrderD_Qty
    WHERE iAnnualOrderDid = b.iautoid
    AND iyear = a.iyear
    AND imonth = 12
    ) AS month12
    FROM Co_AnnualOrderM AS a
    LEFT JOIN Co_AnnualOrderD AS b ON a.iAutoId = b.iAnnualOrderMid
    LEFT JOIN Bd_Customer AS d ON a.iCustomerId = d.iAutoId
    LEFT JOIN Bd_Inventory AS e ON b.iInventoryId = e.iAutoId
    WHERE a.isDeleted = '0' AND b.isDeleted = '0' AND a.iAuditStatus = 2
    AND a.iYear >= #para(year)
    ) AS t
GROUP BY iCustomerId,cCusCode,cCusName,
    iInventoryId,cInvCode,cInvCode1,cInvName1,
    year
#end

#sql("getMonthOrderList")
###根据年份获取客户月度订单-每一天-月 相同客户相同物料相同年月日汇总
SELECT a.iCustomerId,d.cCusCode,d.cCusName,
       b.iInventoryId,e.cInvCode,e.cInvCode1,e.cInvName1,
       a.iYear AS year,a.iMonth AS month,
             SUM(b.iQty1) AS day1,SUM(b.iQty2) AS day2,SUM(b.iQty3) AS day3,SUM(b.iQty4) AS day4,SUM(b.iQty5) AS day5,
             SUM(b.iQty6) AS day6,SUM(b.iQty7) AS day7,SUM(b.iQty8) AS day8,SUM(b.iQty9) AS day9,SUM(b.iQty10) AS day10,
             SUM(b.iQty11) AS day11,SUM(b.iQty12) AS day12,SUM(b.iQty13) AS day13,SUM(b.iQty14) AS day14,SUM(b.iQty15) AS day15,
             SUM(b.iQty16) AS day16,SUM(b.iQty17) AS day17,SUM(b.iQty18) AS day18,SUM(b.iQty19) AS day19,SUM(b.iQty20) AS day20,
             SUM(b.iQty21) AS day21,SUM(b.iQty22) AS day22,SUM(b.iQty23) AS day23,SUM(b.iQty24) AS day24,
             SUM(b.iQty25) AS day25,SUM(b.iQty26) AS day26,SUM(b.iQty27) AS day27,SUM(b.iQty28) AS day28,SUM(b.iQty29) AS day29,
             SUM(b.iQty30) AS day30,SUM(b.iQty31) AS day31,
			 SUM(b.iMonth1Qty) AS nextMonth1Qty,SUM(b.iMonth2Qty) AS nextMonth2Qty
FROM Co_MonthOrderM AS a
    LEFT JOIN Co_MonthOrderD AS b ON a.iAutoId = b.iMonthOrderMid
    LEFT JOIN Bd_Customer AS d ON a.iCustomerId = d.iAutoId
    LEFT JOIN Bd_Inventory AS e ON b.iInventoryId = e.iAutoId
WHERE a.IsDeleted = 0 AND a.iAuditStatus = 2
  AND a.iYear >= #para(year)
GROUP BY a.iCustomerId,d.cCusCode,d.cCusName,
    b.iInventoryId,e.cInvCode,e.cInvCode1,e.cInvName1,
    a.iYear,a.iMonth
ORDER BY a.iYear,a.iMonth
#end


#sql("getWeekOrderList")
###根据年份获取客户周间订单 相同物料相同年月日汇总
SELECT b.iInventoryId,c.cInvCode,c.cInvCode1,c.cInvName1,
       CONVERT(VARCHAR(10),b.dPlanAogDate,120) AS dPlanAogDate,SUM(b.iQty) AS iQty
FROM Co_WeekOrderM AS a
         LEFT JOIN Co_WeekOrderD AS b ON a.iAutoId = b.iWeekOrderMid
         LEFT JOIN Bd_Inventory AS c ON b.iInventoryId = c.iAutoId
WHERE a.IsDeleted = 0 AND a.iAuditStatus = 2 AND b.isDeleted = 0
  AND b.dPlanAogDate >= #para(year)
GROUP BY
    b.iInventoryId,c.cInvCode,c.cInvCode1,c.cInvName1,b.dPlanAogDate
#end


#sql("getManualOrderList")
###根据年份获取客户手配订单 相同物料相同年月日汇总
SELECT
    b.iInventoryId,e.cInvCode,e.cInvCode1,e.cInvName1,
    a.iYear AS year,a.iMonth AS month,
             SUM(b.iQty1) AS day1,SUM(b.iQty2) AS day2,SUM(b.iQty3) AS day3,SUM(b.iQty4) AS day4,SUM(b.iQty5) AS day5,
             SUM(b.iQty6) AS day6,SUM(b.iQty7) AS day7,SUM(b.iQty8) AS day8,SUM(b.iQty9) AS day9,SUM(b.iQty10) AS day10,
             SUM(b.iQty11) AS day11,SUM(b.iQty12) AS day12,SUM(b.iQty13) AS day13,SUM(b.iQty14) AS day14,SUM(b.iQty15) AS day15,
             SUM(b.iQty16) AS day16,SUM(b.iQty17) AS day17,SUM(b.iQty18) AS day18,SUM(b.iQty19) AS day19,SUM(b.iQty20) AS day20,
             SUM(b.iQty21) AS day21,SUM(b.iQty22) AS day22,SUM(b.iQty23) AS day23,SUM(b.iQty24) AS day24,
             SUM(b.iQty25) AS day25,SUM(b.iQty26) AS day26,SUM(b.iQty27) AS day27,SUM(b.iQty28) AS day28,SUM(b.iQty29) AS day29,
             SUM(b.iQty30) AS day30,SUM(b.iQty31) AS day31
FROM Co_ManualOrderM AS a
    LEFT JOIN Co_ManualOrderD AS b ON a.iAutoId = b.iManualOrderMid
    LEFT JOIN Bd_Inventory AS e ON b.iInventoryId = e.iAutoId
WHERE a.IsDeleted = 0 AND a.iAuditStatus = 2
  AND a.iYear >= #para(year)
GROUP BY
    b.iInventoryId,e.cInvCode,e.cInvCode1,e.cInvName1,
    a.iYear,a.iMonth
ORDER BY a.iYear,a.iMonth
#end

#sql("getSubcontractSaleOrderList")
###根据年份获取客户委外销售订单 相同物料相同年月日汇总
SELECT
    b.iInventoryId,e.cInvCode,e.cInvCode1,e.cInvName1,
    a.iYear AS year,a.iMonth AS month,
             SUM(b.iQty1) AS day1,SUM(b.iQty2) AS day2,SUM(b.iQty3) AS day3,SUM(b.iQty4) AS day4,SUM(b.iQty5) AS day5,
             SUM(b.iQty6) AS day6,SUM(b.iQty7) AS day7,SUM(b.iQty8) AS day8,SUM(b.iQty9) AS day9,SUM(b.iQty10) AS day10,
             SUM(b.iQty11) AS day11,SUM(b.iQty12) AS day12,SUM(b.iQty13) AS day13,SUM(b.iQty14) AS day14,SUM(b.iQty15) AS day15,
             SUM(b.iQty16) AS day16,SUM(b.iQty17) AS day17,SUM(b.iQty18) AS day18,SUM(b.iQty19) AS day19,SUM(b.iQty20) AS day20,
             SUM(b.iQty21) AS day21,SUM(b.iQty22) AS day22,SUM(b.iQty23) AS day23,SUM(b.iQty24) AS day24,
             SUM(b.iQty25) AS day25,SUM(b.iQty26) AS day26,SUM(b.iQty27) AS day27,SUM(b.iQty28) AS day28,SUM(b.iQty29) AS day29,
             SUM(b.iQty30) AS day30,SUM(b.iQty31) AS day31
FROM Co_SubcontractSaleOrderM AS a
    LEFT JOIN Co_SubcontractSaleOrderD AS b ON a.iAutoId = b.iSubcontractSaleOrderMid
    LEFT JOIN Bd_Inventory AS e ON b.iInventoryId = e.iAutoId
WHERE a.IsDeleted = 0 AND a.iAuditStatus = 2
  AND a.iYear >= #para(year)
GROUP BY
    b.iInventoryId,e.cInvCode,e.cInvCode1,e.cInvName1,
    a.iYear,a.iMonth
ORDER BY a.iYear,a.iMonth
#end


#sql("getCalendarByYearList")
###根据年份获取排产工作日历
SELECT
    CONVERT(VARCHAR(7),dTakeDate,120) AS dTakeYearMonth,
    CONVERT(VARCHAR(10),dTakeDate,120) AS dTakeDate
FROM Bd_Calendar
WHERE iCaluedarType = '1'
  AND cSourceCode = #para(csourcecode)
  AND CONVERT(VARCHAR(4),dTakeDate,120) >= #para(year)
ORDER BY
    CONVERT(VARCHAR(4),dTakeDate,120)
#end