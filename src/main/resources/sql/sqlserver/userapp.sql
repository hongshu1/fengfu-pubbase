#sql("getUserByUserCode")
SELECT
a.UserCode as user_code,
a.UserName as user_name,
a.Password as password
FROM #(erpDBName).#(erpDBSchemas).v_sys_user AS a
where 1=1
#if(userCode)
and (a.userCode = #para(userCode) or a.username=#para(userCode))
#end
#end















