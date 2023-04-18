#sql("paginateAdminDatas")
	select aom.iautoid,aom.iAuditStatus,aom.iOrderStatus,aom.corderno,
		c.cCusName,aom.iyear,aom.imonth,aom.cCreateName,aom.dCreateTime
	from co_monthorderm aom
		left join Bd_Customer c on aom.iCustomerId = c.iautoid
	where 1=1 and aom.isdeleted = 0
	#if(iorgid)
		and aom.iorgid = #para(iorgid)
	#end
	#if(cOrderNo)
		and aom.cOrderNo like concat('%',#para(cOrderNo),'%') 
	#end
	#if(cCusName)
		and c.cCusName like concat('%',#para(cCusName),'%') 
	#end
	#if(iAuditStatus)
		and aom.iAuditStatus = #para(iAuditStatus) 
	#end
	#if(iYear)
		and aom.iYear = #para(iYear) 
	#end
	#if(iMonth)
		and aom.iMonth = #para(iMonth) 
	#end
	#if(cCreateName)
		and aom.cCreateName like concat('%',#para(cCreateName),'%') 
	#end
	#if(startTime)
		and convert(date,aom.dCreateTime) >= convert(date,#para(startTime)) 
	#end
	#if(endTime)
		and convert(date,aom.dCreateTime) <= convert(date,#para(endTime)) 
	#end
#end