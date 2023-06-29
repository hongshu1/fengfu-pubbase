#sql("paginateAdminDatas")
	select scsom.*, p.cPsn_Name AS cpsnname, cus.ccusname
	from Co_SubcontractSaleOrderM scsom
    LEFT JOIN Bd_Person p ON scsom.iBusPersonId = p.iAutoId
	LEFT JOIN Bd_Customer cus ON scsom.icustomerid = cus.iautoid
	where scsom.isDeleted = '0'
    #if(iautoid)
        AND scsom.iAutoId = #para(iautoid)
    #end
	#if(iorgid)
		and scsom.iorgid = #para(iorgid)
	#end
	#if(cOrderNo)
		and scsom.cOrderNo like concat('%',#para(cOrderNo),'%') 
	#end
	#if(cCusName)
		and scsom.cCusName like concat('%',#para(cCusName),'%')
	#end
    #if(iOrderStatus)
	    AND scsom.iOrderStatus = #para(iOrderStatus)
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