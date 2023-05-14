
#sql("wareHouse")
SELECT  a.*
FROM V_Sys_WareHouse a
where 1=1
	#if(q)
		and (a.whcode like concat('%',#para(q),'%') OR a.whName like concat('%',#para(q),'%'))
	#end
#end


#sql("vendor")
SELECT  a.*
FROM V_Sys_Vendor a
where 1=1
	#if(q)
		and (a.VenCode like concat('%',#para(q),'%') OR a.VenName like concat('%',#para(q),'%'))
	#end
#end


#sql("barcodedetail")
SELECT  a.*
FROM v_Sys_BarcodeDetail a
where 1=1
	#if(vencode)
		and a.VenCode like concat('%',#para(vencode),'%')
	#end
	#if(q)
		and (a.BarcodeID like concat('%',#para(q),'%') OR a.Barcode like concat('%',#para(q),'%'))
	#end
#end

#sql("inventory")
SELECT  a.*
FROM V_Sys_Inventory a
where 1=1
	#if(q)
		and (a.InvCode like concat('%',#para(q),'%') OR a.InvName like concat('%',#para(q),'%'))
	#end
#end

#sql("modetail")
SELECT  a.*
FROM V_Sys_MODetail a
where 1=1
	#if(q)
		and (a.InvCode like concat('%',#para(q),'%') OR a.InvName like concat('%',#para(q),'%'))
	#end
#end



#sql("podetail")
SELECT  a.*
FROM V_Sys_PODetail a
where 1=1
	#if(q)
		and (a.BillNoRow like concat('%',#para(q),'%') OR a.BillNo like concat('%',#para(q),'%'))
	#end
#end

#sql("position")
SELECT  a.*
FROM V_Sys_Position a
where 1=1
	#if(q)
		and (a.BillNoRow like concat('%',#para(q),'%') OR a.BillNo like concat('%',#para(q),'%'))
	#end
#end


#sql("customer")
SELECT  a.*
FROM V_Sys_Customer a
where 1=1
	#if(q)
		and (a.CusCode like concat('%',#para(q),'%') OR a.CusName like concat('%',#para(q),'%'))
	#end
#end

