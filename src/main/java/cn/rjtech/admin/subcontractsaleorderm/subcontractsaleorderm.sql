#sql("paginateAdminDatas")
	select scsom.*,c.cCusCode,c.cCusName,c.cCusAbbName from Co_SubcontractSaleOrderM scsom 
		left join Bd_Customer c on scsom.icustomerid = c.iautoid
	where scsom.isDeleted = '0'
	#if(iorgid)
		and scsom.iorgid = #para(iorgid)
	#end
	#if(cOrderNo)
		and scsom.cOrderNo like concat('%',#para(cOrderNo),'%') 
	#end
	#if(cCusName)
		and c.cCusName like concat('%',#para(cCusName),'%') 
	#end
	#if(iAuditStatus)
		and scsom.iAuditStatus = #para(iAuditStatus) 
	#end
	#if(iYear)
		and scsom.iYear = #para(iYear) 
	#end
	#if(iMonth)
		and scsom.iMonth = #para(iMonth) 
	#end
	#if(cCreateName)
		and scsom.cCreateName like concat('%',#para(cCreateName),'%') 
	#end
	#if(startTime)
		and convert(date,scsom.dCreateTime) >= convert(date,#para(startTime)) 
	#end
	#if(endTime)
		and convert(date,scsom.dCreateTime) <= convert(date,#para(endTime)) 
	#end
	order by scsom.dCreateTime desc
#end