#sql("selectFitem")
select *
from Bd_fitem where 1=1 and isDeleted = '0'
    #if(sn)
			and citem_class = #para(sn)
		#end
    #if(iautoid)
			and iautoid = #para(iautoid)
		#end
    #if(citem_name)
  and citem_name like CONCAT('%', #para(citem_name), '%')
  #end

    #if(citem_class)
  and citem_class like CONCAT('%', #para(citem_class), '%')
  #end
ORder By citem_class
#end
