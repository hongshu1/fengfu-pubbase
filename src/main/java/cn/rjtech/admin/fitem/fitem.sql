#sql("selectFitem")
select *
from Bd_fitem where 1=1 and isDeleted = '0'
    #if(sn)
			and citem_class = #para(sn)
		#end
    #if(iautoid)
			and iautoid = #para(iautoid)
		#end
ORder By citem_class
#end
