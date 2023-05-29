#sql("selectNotCvbtId")
select a.cVBTID from Bd_VouchTypeDic a where CVBTID=#para(cvbtid)
#end

#sql("selectByIautoId")
select a.cVBTID from Bd_VouchTypeDic a where iAutoId=#para(iAutoId)
#end

#sql("selectListCvbtid")
select Cvbtid from  Bd_VouchTypeDic
#end

