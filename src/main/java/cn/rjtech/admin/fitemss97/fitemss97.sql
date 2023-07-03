#sql("paginateAdminDatas")
	select * from Bd_fitemss97 where 1=1 and isDeleted = '0'
		#if(keywords)
			and (citemcode like concat('%',#para(keywords),'%') or citemname like concat('%',#para(keywords),'%'))
		#end
		#if(iorgid)
			and iorgid = #para(iorgid)
		#end
#end

#sql("findfitemss97classList")
select * from Bd_fitemss97class where  1=1 and isDeleted = '0'
		#if(sn)
			and cItemCcode = #para(sn)
		#end
ORder By citemccode
#end

#sql("findfitemss97List")
select * from Bd_fitemss97  where  1=1 and isDeleted = '0'
		#if(citemccode)
			and cItemCcode = #para(citemccode)
		#end
        #if(fitemss97subid)
			and iautoid = #para(fitemss97subid)
		#end
Order By citemccode
#end


    #sql("findfitemss97parent")
select * from Bd_fitemss97  where  1=1 and isDeleted = '0' and isourceid='0'
    #if(sn)
			and  cItemCcode= #para(sn)
		#end
Order By citemccode
    #end

#sql("findfitemss97sub")
SELECT * FROM Bd_fitemss97 WHERE 1=1 and isDeleted = '0'
        #if(isourceid)
			and isourceid = #para(isourceid)
		#end
    #end





