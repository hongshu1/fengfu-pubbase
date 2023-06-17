#sql("checkOk")
select * from
Bd_InventoryWorkRegion (nolock)
where  isDefault=1 and isdeleted =0 and
iInventoryId=#para(iInventoryId)
#end
