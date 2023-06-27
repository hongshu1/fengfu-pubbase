#sql("findBySourceId")
select * from
T_Sys_StockCheckDetail
where sourceid=#para(sourceid)
#end



#sql("findRecordByMasid")
select m.whcode,vd.poscode,d.*
from #(getMomdataDbName()).dbo.T_Sys_StockCheckVouch m
left join  #(getMomdataDbName()).dbo.T_Sys_StockCheckVouchDetail vd on vd.MasID=m.autoid
inner join #(getMomdataDbName()).dbo.T_Sys_StockCheckDetail d on d.sourceid=vd.autoid
where vd.masid = #para(masid)
#end


#sql("findByMasid")
select d.*
from  #(getMomdataDbName()).dbo.T_Sys_StockCheckVouchDetail vd
inner join #(getMomdataDbName()).dbo.T_Sys_StockCheckDetail d on d.sourceid=vd.autoid
where vd.masid = #para(masid)
#end


#sql("findByMasid_app")
select d.*
from  #(getMomdataDbName()).dbo.T_Sys_StockCheckVouchDetail vd
inner join #(getMomdataDbName()).dbo.T_Sys_StockCheckDetail d on d.sourceid=vd.autoid
where vd.masid = #para(masid) and generatetype is null
#end


#sql("findByMBillno")
select dd.* from
T_Sys_StockCheckVouch m
left join T_Sys_StockCheckVouchDetail d on d.MasID=m.autoid
inner join T_Sys_StockCheckDetail   dd on dd.sourceid=d.autoid
where m.billno=#para(billno)
#end
