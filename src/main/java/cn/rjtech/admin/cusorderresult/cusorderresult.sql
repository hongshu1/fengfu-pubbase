
#sql("paginate")

SELECT

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


    GROUP BY
    t.iInventoryId

#end


#sql("dayDatas")
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
