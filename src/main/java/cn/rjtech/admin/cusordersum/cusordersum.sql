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