#sql("getcdepcode")
SELECT hhp.cDept_num
FROM #(getBaseDbName()).dbo.jb_user u
    LEFT JOIN Ua_User uu ON u.cuser_id = uu.cuser_id
    LEFT JOIN UserHrPersonContro uhpc ON uhpc.cUser_Id  = uu.cUser_Id
    LEFT JOIN hr_hi_person hhp ON hhp.cPsn_Num = uhpc.cPsn_Num AND hhp.rCheckInFlag = 1 AND isnull( hhp.cLeaveType, '' ) = ''
WHERE uu.nState = 0
    AND u.id = #para(id)
#end