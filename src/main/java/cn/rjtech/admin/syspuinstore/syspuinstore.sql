#sql("recpor")
SELECT
    t1.*,
    t2.cRdCode,t2.cRdName,
    t3.Whcode,
    t4.cWhName,
    t5.cDepName,
    t6.cPTName,
    t7.cVenCode,
    t7.cVenName
FROM T_Sys_PUInStore t1
         LEFT JOIN Bd_Rd_Style t2 on t1.RdCode = t2.iAutoId
         LEFT JOIN T_Sys_PUInStoreDetail t3 on t1.autoid = t3.MasID
         LEFT JOIN Bd_Warehouse t4 on t3.Whcode = t4.cWhCode
         LEFT JOIN Bd_Department t5 on t1.DeptCode = t5.cDepCode
         LEFT JOIN Bd_PurchaseType t6 on t1.BillType = t6.cPTName
         LEFT JOIN Bd_Vendor t7 on t1.VenCode = t7.cVenCode
    WHERE 1=1
	#if(billno)
		and t1.billno like concat('%',#para(billno),'%')
	#end
	#if(sourcebillno)
		and t1.sourcebillno like concat('%',#para(sourcebillno),'%')
	#end
    #if(vencode)
        and t1.vencode = #para(vencode)
    #end
	#if(state)
		and t1.state = #para(state)
	#end
    #if(whcode)
        and t1.whcode = #para(whcode)
    #end
    #if(cinvaddcode)
        and t1.cinvaddcode = #para(cinvaddcode)
    #end
    #if(cinvcode1)
        and t1.cinvcode1 = #para(cinvcode1)
    #end
    #if(cinvname)
        and t1.cinvname = #para(cinvname)
    #end
	#if(startTime)
		and t1.CreateDate >= #para(startTime)
	#end
	#if(endTime)
		and t1.CreateDate <= #para(endTime)
	#end
ORDER BY t1.ModifyDate DESC
#end

#sql("dList")
SELECT  a.*
FROM T_Sys_PUInStoreDetail a
where 1=1
	#if(masid)
		and a.MasID = #para(masid)
	#end
ORDER BY a.ModifyDate DESC
#end

#sql("getWareHouseName")
    select * from V_Sys_WareHouse
#end

#sql("pageDetailList")
    SELECT  a.*
    FROM T_Sys_PUInStoreDetail a
    where 1=1
     #if(masid)
    and a.MasID = #para(masid)
     #end
     #if(spotticket)
    and a.spotticket = #para(spotticket)
     #end
    ORDER BY a.ModifyDate DESC
#end