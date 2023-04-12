#sql("processNote")
select * from T_Sys_VouchProcessNote
where Seq=#para(seq) and VouchBusinessID=#para(vouchBusinessID);
#end