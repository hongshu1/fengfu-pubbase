#sql("datas")
select t1.*,t2.cWhName,t3.cPsn_Name
from T_Sys_StockCheckVouch t1
left join Bd_Warehouse t2 on t1.WhCode = t2.cWhCode
left join bd_person t3 on t1.checkperson = t3.iautoid
     where 1=1
#if(billno)
  and t1.BillNo like CONCAT('%', #para(billno), '%')
#end
#if(whcode)
  and t1.whcode = #para(whcode)
#end
#if(checktype)
  and t1.checktype = #para(checktype)
#end
#if(startTime)
 and t1.CreateDate >= #para(startTime)
#end
#if(endTime)
  and t1.CreateDate <= #para(endTime)
#end
order by t1.ModifyDate desc
#end