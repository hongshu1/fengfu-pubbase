#sql("paginateAdminDatas")
SELECT vc.*, VRR.cRdName, VRS.cRdName as vrsname, dic.cvtchname, dic.cbtchname
FROM Bd_VouchRdContrapose vc
LEFT JOIN bd_rd_Style VRR ON VRR.cRdCode = vc.cVRRCode AND VRR.bRdFlag = 1
AND VRR.isDeleted = '0' AND VRR.iorgid = #para(iorgid) ###-- 1 收类型
LEFT JOIN bd_rd_Style VRS ON VRS.cRdCode = vc.cVRSCode AND VRS.bRdFlag = 0
AND VRS.isDeleted = '0' AND VRS.iorgid = #para(iorgid)  ###-- 0 发类型
LEFT JOIN Bd_VouchTypeDic dic ON vc.cVBTID = dic.cVBTID
AND dic.isDeleted = '0' AND dic.iorgid = #para(iorgid)
WHERE vc.isDeleted = '0' AND vc.iorgid = #para(iorgid)
#if(keywords)
AND (vc.cVRRCode like concat('%',#para(keywords),'%') or vc.cVRSCode like concat('%',#para(keywords),'%')
or VRR.cRdName like concat('%',#para(keywords),'%') or VRS.cRdName like concat('%',#para(keywords),'%')
)
#end
ORDER BY vc.iautoid DESC
#end

#sql("getCvrrName")
 select cRdName from Bd_Rd_Style where cRdCode=#para(cvrrcode)
#end

 #sql("selectCvrrcodeAndCvrsCode")
 SELECT * 
 FROM Bd_VouchRdContrapose 
 WHERE iorgid = #para(iorgid)
    AND cvbtid = #para(cvbtid)
    AND cVRRCode=#para(cvrrCode) 
    AND cVRSCode=#para(cvrsCode)
 #end

 #sql("selectCvrrcodeOnCvrsCodeList")
 select cvrrCode,cvrsCode from Bd_VouchRdContrapose
 #end