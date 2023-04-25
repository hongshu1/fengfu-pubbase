#sql("getCusOrderSumList")
###根据层级及销售类型获取客户计划汇总表数据
SELECT
    t.iYear,
    t.iMonth,
    t.iDate,
    t.iQty3,
    a.iAutoId AS invId,
    a.cInvCode,
    a.cInvCode1,  ### 客户部属
    a.cInvName1,
    a.iSaleType

FROM Co_CusOrderSum AS t
         LEFT JOIN Bd_Inventory AS a ON a.iAutoId = t.iInventoryId

WHERE 1=1

    AND t.iYear >= #para(startyear) AND t.iYear <= #para(endyear)
    AND t.iMonth >= #para(startmonth) AND t.iMonth <= #para(endmonth)
    AND t.iDate >= #para(startday) AND t.iDate <= #para(endday)
ORDER BY t.iYear,t.iMonth,t.iDate
    #end


#sql("paginateAdminDatas")
SELECT
    os.*,
    bi.cInvCode1,  ### 客户部属
    bi.cInvName1,  ### 部品名称
    bi.iCustomerMId  ### 客户id
FROM
    (
        SELECT
            t.iYear,
            t.iMonth,
            t.iDate,
            SUM ( t.iQty1 ) AS CustomerPlanCount,
            SUM ( t.iQty2 ) AS CustomerOrderCount,
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

    #end
