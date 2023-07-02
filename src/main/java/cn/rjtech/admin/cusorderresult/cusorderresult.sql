
#sql("paginate")
SELECT
    bi.cInvCode
FROM
    (
        SELECT t.iInventoryId FROM Co_CusOrderSum as t
        WHERE
                (CAST(t.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN t.iMonth<10 THEN '0'+CAST(t.iMonth AS NVARCHAR(30) )
                ELSE CAST(t.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN t.iDate<10 THEN '0'+CAST(t.iDate AS NVARCHAR(30) )
                ELSE CAST(t.iDate AS NVARCHAR(30) )
                END AS NVARCHAR(30)) ) >= '2020-04-01'
          AND
                (CAST(t.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN t.iMonth<10 THEN '0'+CAST(t.iMonth AS NVARCHAR(30) )
                ELSE CAST(t.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN t.iDate<10 THEN '0'+CAST(t.iDate AS NVARCHAR(30) )
                ELSE CAST(t.iDate AS NVARCHAR(30) )
                END AS NVARCHAR(30)) ) <= '2024-04-30'
        GROUP by  t.iInventoryId
    )
     AS os
	LEFT JOIN dbo.Bd_Inventory bi ON os.iInventoryId = bi.iAutoId
    LEFT JOIN Bd_Customer cus ON cus.iAutoId = bi.iCustomerMId
    where 1=1
    #if(cinvcode)
        AND bi.cInvCode LIKE CONCAT('%', #para(cinvcode), '%')
    #end
    #if(cinvcode1)
        AND bi.cInvCode1 LIKE CONCAT('%', #para(cinvcode1), '%')
    #end
    #if(cinvname1)
        AND bi.cInvName1 LIKE CONCAT('%', #para(cinvname1), '%')
    #end
    #if(cinvname1)
        AND cus.cCusName LIKE CONCAT('%', #para(cCusName), '%')
    #end
#end


#sql("dayCusOrderSum")
SELECT
    bi.cInvCode, ### 存货编码
    bi.cInvCode1,  ### 客户部番
    bi.cInvName1,  ### 部品名称
    cus.cCusName,   ### 客户名称
    os.*
FROM
    (
        SELECT
            t.iYear,
            t.iMonth,
            t.iDate,
            SUM ( t.iQty2 ) AS CustomerOrderCount,  ### 订单数
            t.iInventoryId
        FROM
            dbo.Co_CusOrderSum  t
        where

        (CAST(t.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN t.iMonth<10 THEN '0'+CAST(t.iMonth AS NVARCHAR(30) )
		ELSE CAST(t.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN t.iDate<10 THEN '0'+CAST(t.iDate AS NVARCHAR(30) )
		ELSE CAST(t.iDate AS NVARCHAR(30) )
		END AS NVARCHAR(30)) ) >= #para(beginDate)
	    AND
		(CAST(t.iYear  AS NVARCHAR(30))+'-'+CAST(CASE WHEN t.iMonth<10 THEN '0'+CAST(t.iMonth AS NVARCHAR(30) )
		ELSE CAST(t.iMonth AS NVARCHAR(30) ) END AS NVARCHAR(30)) +'-'+CAST( CASE WHEN t.iDate<10 THEN '0'+CAST(t.iDate AS NVARCHAR(30) )
		ELSE CAST(t.iDate AS NVARCHAR(30) )
		END AS NVARCHAR(30)) ) <= #para(endDate)

        ORDER BY t.iYear,t.iMonth,t.iDate

        GROUP BY
            iYear,
            iMonth,
            iDate,
            iInventoryId
    ) AS os
        LEFT JOIN dbo.Bd_Inventory bi ON os.iInventoryId = bi.iAutoId
        LEFT JOIN Bd_Customer cus on cus.iAutoId = bi.iCustomerMId

    #end

#sql("datas")
SELECT inv.iAutoId , inv.cInvCode, inv.cInvCode1,  inv.cinvname1
FROM Bd_Inventory inv
WHERE 1 = 1
#if(iinventoryid)
    AND inv.iAutoId = #para(iinventoryid)
#end
  AND( inv.iAutoId IN (SELECT iInventoryId FROM Co_WeekOrderD
                       WHERE 1 = 1
                         #if(startdate)
                         AND dPlanAogDate >= #para(startdate)
                         #end
                         #if(enddate)
                         AND dPlanAogDate <= #para(enddate)
                         #end
                         AND iWeekOrderMid IN (
                           SELECT iAutoId FROM Co_WeekOrderM
                           WHERE isDeleted = '0'
                             AND iOrderStatus = 3
                             #if(icustomerid)
                             AND iCustomerId = #para(icustomerid)
                             #end
                       ) GROUP BY iInventoryId)
    OR inv.iAutoId IN (SELECT iInventoryId FROM Co_SubcontractSaleOrderD
                       WHERE iSubcontractSaleOrderMid IN (
                           SELECT iAutoId FROM Co_SubcontractSaleOrderM
                           WHERE isDeleted = '0'
                             AND iOrderStatus = 3
                              #if(icustomerid)
                             AND iCustomerId = #para(icustomerid)
                             #end
                             #if(startdate)
                             AND (iYear >= #para(startdate.split('-')[0]) AND iMonth >= #para(startdate.split('-')[1]))
                             #end
                              #if(enddate)
                             AND (iYear <= #para(enddate.split('-')[0]) AND iMonth <= #para(enddate.split('-')[1]))
                             #end
                       ) GROUP BY iInventoryId)
    OR inv.iAutoId IN (SELECT iInventoryId FROM Co_ManualOrderD
                       WHERE iManualOrderMid IN (
                           SELECT iAutoId FROM Co_ManualOrderM
                           WHERE isDeleted = '0'
                             AND iOrderStatus = 3
                            #if(icustomerid)
                             AND iCustomerId = #para(icustomerid)
                             #end
                            #if(startdate)
                             AND (iYear >= #para(startdate.split('-')[0]) AND iMonth >= #para(startdate.split('-')[1]))
                             #end
                              #if(enddate)
                             AND (iYear <= #para(enddate.split('-')[0]) AND iMonth <= #para(enddate.split('-')[1]))
                             #end
                        ) GROUP BY iInventoryId)
    OR inv.iAutoId IN (SELECT iInventoryId FROM SM_RcvPlanD WHERE 1 =1
                      #if(startdate)
                      AND cRcvDate >= #para(startdate)
                      #end
                      #if(enddate)
                      AND cRcvDate <= #para(enddate)
                       #end
                      AND iRcvPlanMid IN(
                            SELECT iAutoId
                            FROM SM_RcvPlanM
                            WHERE IsDeleted = '0'
                            AND iStatus = 3
                            #if(icustomerid)
                            AND iCustomerId = #para(icustomerid)
                            #end
                      ) GROUP BY iInventoryId)
    OR inv.cInvCode IN (SELECT InvCode FROM T_Sys_SaleDeliverDetail WHERE 1 = 1
                        ### 时间查询限制(待优化)
                        AND MasID IN (
                            SELECT AutoID
                            FROM T_Sys_SaleDeliver
                            WHERE isDeleted = '0'
                             ### 只查询已审核数据(待优化)
                              AND iAuditStatus = 2
                             ### 客户名称查询(待优化)
                        )GROUP BY InvCode)
    )
#end

### 周间客户订单数据
#sql("weekOrderDatas")
SELECT wm.iCustomerId, wd.iInventoryId, wd.dPlanAogDate AS time, SUM(wd.iQty) AS qtysum
FROM Co_WeekOrderM wm
LEFT JOIN Co_WeekOrderD  wd On wm.iAutoId = wd.iWeekOrderMid
WHERE wm.IsDeleted = '0'
AND wm.iOrderStatus = 3
#if(startdate)
AND  wd.dPlanAogDate >= #para(startdate)
#end
#if(enddate)
AND  wd.dPlanAogDate <= #para(enddate)
#end
#if(icustomerid)
AND    wm.iCustomerId = #para(icustomerid)
#end
#if(iinventoryid)
AND  wd.iInventoryId = #para(iinventoryid)
#end
#if(iinventoryids)
AND  wd.iInventoryId IN (#para(iinventoryids))
#end
GROUP BY wm.iCustomerId, wd.iInventoryId, wd.dPlanAogDate
#end

### 手配、委外数据
#sql("SubcontractSaleOrderOrManualOrderDatas")
SELECT iCustomerId, iInventoryId,iYear,iMonth,
       #for (x : roleArray.split(',')) SUM(iQty#(x)) AS qty#(x)sum
        #(for.last?'':',')
       #end
FROM (
    SELECT ssom.iCustomerId, ssom.iYear, ssom.iMonth, ssod.iInventoryId,
    #for (x : roleArray.split(',')) SUM(ssod.iQty#(x)) AS iQty#(x) #(for.last?'':',') #end
    FROM Co_SubcontractSaleOrderM ssom
    LEFT JOIN Co_SubcontractSaleOrderD ssod ON ssom.iAutoId = ssod.iSubcontractSaleOrderMid
    WHERE ssom.IsDeleted = '0'
    AND ssom.iOrderStatus = 3
    #if(startdate)
    AND (ssom.iYear >= #para(startdate.split('-')[0]) AND ssom.iMonth >= #para(startdate.split('-')[1]))
    #end
    #if(enddate)
    AND (ssom.iYear <= #para(enddate.split('-')[0]) AND ssom.iMonth <= #para(enddate.split('-')[1]))
    #end
    #if(icustomerid)
    AND ssom.iCustomerId = #para(icustomerid)
    #end
    #if(iinventoryid)
    AND ssod ON.iInventoryId = #para(iinventoryid)
    #end
    #if(iinventoryids)
    AND ssod.iInventoryId IN (#para(iinventoryids))
    #end
GROUP BY ssom.iCustomerId, ssom.iYear, ssom.iMonth, ssod.iInventoryId
UNION
    SELECT mom.iCustomerId, mom.iYear, mom.iMonth, mod.iInventoryId,
    #for (x : roleArray.split(',')) SUM(mod.iQty#(x)) AS iQty#(x)
    #(for.last?'':',')
    #end
    FROM Co_ManualOrderM mom
    LEFT JOIN Co_ManualOrderD mod ON mom.iAutoId = mod.iManualOrderMid
    WHERE mom.IsDeleted = '0'
    AND mom.iOrderStatus = 3
    #if(startdate)
    AND (mom.iYear >= #para(startdate.split('-')[0]) AND mom.iMonth >= #para(startdate.split('-')[1]))
    #end
    #if(enddate)
    AND (mom.iYear <= #para(enddate.split('-')[0]) AND mom.iMonth <= #para(enddate.split('-')[1]))
    #end
    #if(icustomerid)
    AND mom.iCustomerId = #para(icustomerid)
    #end
    #if(iinventoryid)
    AND mod.iInventoryId = #para(iinventoryid)
    #end
    #if(iinventoryids)
    AND mod.iInventoryId IN (#para(iinventoryids))
    #end
    GROUP BY mom.iCustomerId, mom.iYear, mom.iMonth, mod.iInventoryId
    ) x
GROUP BY iCustomerId, iYear,iMonth, iInventoryId
#end

### 取货计划数据
#sql("")
SELECT  rpm.iCustomerId, rpd.iInventoryId, rpd.cRcvDate AS time, SUM(rpd.iQty) AS qtysum
FROM SM_RcvPlanM rpm
         LEFT JOIN SM_RcvPlanD rpd ON rpm.iAutoId = rpd.iRcvPlanMid
WHERE rpm.IsDeleted = '0'
AND rpm.iStatus = 3
#if(startdate)
AND  rpd.cRcvDate >= #para(startdate)
#end
#if(enddate)
AND  rpd.cRcvDate <= #para(enddate)
#end
#if(icustomerid)
AND rpm.iCustomerId = #para(icustomerid)
#end
#if(iinventoryid)
AND rpd.iInventoryId = #para(iinventoryid)
#end
#if(iinventoryids)
AND rpd.iInventoryId IN (#para(iinventoryids))
#end
GROUP BY rpm.iCustomerId, rpd.iInventoryId, rpd.cRcvDate
#end

### 出货实绩数据   时间、存货主键、客户主键待优化、待沟通
#sql("actualshipmentqtyDatas")
SELECT NULL AS iCustomerId, (SELECT iAutoId FROM Bd_Inventory WHERE cInvCode = sdd.InvCode) AS iInventoryId, sdd.CreateDate AS time,SUM(sdd.Qty) AS qtysum
FROM T_Sys_SaleDeliver sd
    LEFT JOIN T_Sys_SaleDeliverDetail sdd ON sd.AutoID = sdd.MasID
WHERE sd.isDeleted ='0'
AND iAuditStatus = 2
#if(startdate)
AND sdd.CreateDate >= #para(startdate)
#end
#if(cinvcodes)
AND sdd.InvCode IN (
    #for(cinvcode:cinvcodes)
    '#(cinvcode)' #(for.last?'':',')
    #end
)
#end
GROUP BY sdd.InvCode, sdd.CreateDate
#end