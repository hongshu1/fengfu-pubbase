#sql("RdStyle")
SELECT  a.*
FROM Bd_Rd_Style a
where a.bRdFlag = '0'
	#if(q)
		and (a.cRdCode like concat('%',#para(q),'%') OR a.cRdName like concat('%',#para(q),'%'))
	#end
#end