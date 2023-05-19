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
     LEFT JOIN Bd_Rd_Style t2 on t1.RdCode = t2.cRdCode
     LEFT JOIN (SELECT DISTINCT a.MasID,a.Whcode FROM T_Sys_PUInStoreDetail a ) t3 on t1.autoid = t3.MasID
     LEFT JOIN Bd_Warehouse t4 on t3.Whcode = t4.cWhCode
     LEFT JOIN Bd_Department t5 on t1.DeptCode = t5.cDepCode
     LEFT JOIN Bd_PurchaseType t6 on t1.BillType = t6.cPTName
     LEFT JOIN Bd_Vendor t7 on t1.VenCode = t7.cVenCode
WHERE 1=1
#if(billno)
    and t1.billno = #para(billno)
#end
#if(sourcebillno)
    and t1.sourcebillno = #para(sourcebillno)
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
#if(startTime)
    and t1.CreateDate >= #para(startTime)
#end
#if(endTime)
    and t1.CreateDate <= #para(endTime)
#end
order by t1.ModifyDate,t1.autoid desc
#end

#sql("dList")
SELECT  a.*
FROM T_Sys_PUInStoreDetail a
where 1=1
#if(masid)
    and a.MasID = #para(masid)
#end
order by a.ModifyDate desc
#end

#sql("getWareHouseName")
    select * from V_Sys_WareHouse
#end

#sql("pageDetailList")
SELECT
    a.*
FROM T_Sys_PUInStoreDetail a
where 1=1
#if(masid)
    and a.MasID = #para(masid)
#end
#if(spotticket)
    and a.spotticket = #para(spotticket)
#end
    order by a.ModifyDate desc
#end

#sql("getSysPODetail")
select t1.* from V_Sys_PODetail t1
where 1 =1
#if(id)
    and t1.id = #para(id)
#end
#if(billno)
    and t1.billno = #para(billno)
#end
#if(sourcebillno)
    and t1.sourcebillno = #para(sourcebillno)
#end
#if(billid)
    and t1.billid = #para(billid)
#end
#if(billdid)
    and t1.billdid = #para(billdid)
#end
#if(sourcebillid)
    and t1.sourcebillid = #para((sourcebillid))
#end
#if(sourcebilldid)
    and t1.sourcebilldid = #para(sourcebilldid)
#end
#if(billtype)
    and t1.billtype = #para(billtype)
#end
#if(deptcode)
    and t1.deptcode = #para(deptcode)
#end
#end