#sql("getFormCategoryByCname")
select cname from BD_formCategory where iautoid=#para(iatuoid)
#end


#sql("paginateAdminDatas")
select * from Bd_Form
#end
