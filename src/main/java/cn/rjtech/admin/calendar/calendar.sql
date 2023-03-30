#sql("getTakeDateList")
SELECT CONVERT(varchar(20),dTakeDate,120) AS dTakeDate
FROM BD_Calendar
WHERE 1 = 1
    #if(csourcecode)
        AND cSourceCode = #para(csourcecode)
    #end
    #if(icaluedartype)
        AND iCaluedarType = #para(icaluedartype)
    #end
    #if(date_s)
        AND CONVERT(VARCHAR(10),dTakeDate,120) >= CONVERT(VARCHAR(10),#para(date_s),120)
    #end
    #if(date_e)
        AND CONVERT(VARCHAR(10),dTakeDate,120) <= CONVERT(VARCHAR(10),#para(date_e),120)
    #end
	ORDER BY dTakeDate ASC
#end

#sql("getTakeDateListByYear")
SELECT CONVERT(varchar(20),dTakeDate,120) AS dTakeDate
FROM BD_Calendar
WHERE cSourceCode = #para(type) AND cSourceType = #para(type) AND iCaluedarType = 1
  AND CONVERT(varchar(10),dTakeDate,120) >= CONVERT(varchar(10),#para(startdate),120)
  AND CONVERT(varchar(10),dTakeDate,120) <= CONVERT(varchar(10),#para(enddate),120)
ORDER BY dTakeDate
#end


#sql("delCalendar")
###删除所需月份的日历计划
DELETE FROM BD_Calendar
WHERE cSourceCode = #para(type) AND cSourceType = #para(type) AND iCaluedarType = 1
  AND CONVERT(VARCHAR(10),dTakeDate,120) >= CONVERT(VARCHAR(10),#para(startdate),120)
  AND CONVERT(VARCHAR(10),dTakeDate,120) <= CONVERT(VARCHAR(10),#para(enddate),120)
#end
