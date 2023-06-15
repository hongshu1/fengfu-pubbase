#sql("paginateAdminDatas")
SELECT co.*,
       c.ccuscode,
       c.cCusName,
       w.cwhname,
       d.cdepname,
       p.cPsn_Name AS cpsnname
FROM Co_WeekOrderM co
         LEFT JOIN Bd_Customer c ON c.iAutoId = co.iCustomerId AND c.isDeleted = 0
         LEFT JOIN Bd_Warehouse w ON CONVERT(VARCHAR, co.ibustype) = w.cwhcode
         LEFT JOIN Bd_Department d ON co.iDepartmentId = d.iautoid
         LEFT JOIN Bd_Person p ON co.iBusPersonId = p.iAutoId
WHERE
	co.IsDeleted = 0
    #if(corderno)
        AND co.cOrderNo LIKE CONCAT('%', #para(corderno), '%')
    #end
     #if(cCreateName)
        AND co.ccreatename LIKE CONCAT('%', #para(cCreateName), '%')
    #end
    #if(ccuscode)
        AND co.iCustomerId  = #para(ccuscode)
    #end
    #if(iorderstatus)
        AND co.iOrderStatus = #para(iorderstatus)
    #end
    #if(startTime)
        AND convert(date,co.dCreateTime) >= convert(date,#para(startTime))
    #end
    #if(endTime)
        AND convert(date,co.dCreateTime) <= convert(date,#para(endTime))
    #end
     #if(iorderstatus)
        AND co.iOrderStatus = #para(iorderstatus)
    #end
    #if(iAutoId)
        AND co.iAutoId = #para(iAutoId)
    #end
    #if(ids)
        AND CHARINDEX(','+cast((select co.iAutoId) as nvarchar(20))+',' , ','+#para(ids)+',') > 0
    #end
	ORDER BY co.dCreateTime DESC
#end

#sql("updateCplanTimeDatas")
SELECT wod.iautoid, wod.ccode,wod.cdefine1, wod.cdefine2, wod.dplanaogdate, wod.cplanaogtime
FROM Co_WeekOrderM wom
LEFT JOIN Co_WeekOrderD wod ON wom.iAutoId = wod.iWeekOrderMid
WHERE wom.IsDeleted = 0
    #if(corderno)
    AND wom.corderno LIKE CONCAT('%', #para(corderno), '%')
    #end
    #if(timedatas)
    AND(
        #for(timedata:timedatas)
         #(for.first ? "" : "OR") (wod.dPlanAogDate = '#(timedata.split(',')[0])' AND wod.cPlanAogTime = '#(timedata.split(',')[1])')
        #end
    )
    #end
#end
