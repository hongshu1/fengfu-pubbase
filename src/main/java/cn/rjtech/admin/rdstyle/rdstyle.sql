#sql("getCvrrcodeType")
select s.cRdCode as cvrrcode,s.cRdName as vrrname from Bd_Rd_Style s
where ipid=#para(ipid)
#end

#sql("getCvrscodeType")
select s.cRdCode as cvrscode,s.cRdName as vrsname from Bd_Rd_Style s
where ipid=#para(ipid)
#end

#sql("getipidType")
 select iAutoId from Bd_Rd_Style where iPid=#para(ipid)
  and cRdName=#para(crdname)
#end

#sql("getoptions")
select * from Bd_Rd_Style where bRdFlag ='1' and IsDeleted='0'
order by dUpdateTime desc
#end

