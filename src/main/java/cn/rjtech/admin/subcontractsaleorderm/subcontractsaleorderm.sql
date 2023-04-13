#sql("paginateAdminDatas")
	select scsom.*,c.cCusCode,c.cCusName,c.cCusAbbName from Co_SubcontractSaleOrderM scsom 
		left join Bd_Customer c on scsom.icustomerid = c.iautoid
	where 1=1 and scsom.isdeleted = 0
#end