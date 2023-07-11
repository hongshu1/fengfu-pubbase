#sql("paginateAdminDatas")
SELECT  *
FROM dbo.Bd_WorkShiftM wm
WHERE wm.iOrgId = #para(iOrgId)
#if(isdeleted)
    AND wm.isDeleted = #para(isdeleted)
#end
#if(cworkshiftcode)
    AND wm.cWorkShiftCode LIKE CONCAT('%', #para(cworkshiftcode), '%')
#end
#if(cworkshiftname)
    AND wm.cWorkShiftName LIKE CONCAT('%', #para(cworkshiftname), '%')
#end
#if(isenabled)
    AND wm.isEnabled = #para(isenabled)
#end
ORDER BY wm.cWorkShiftCode ASC
#end

#sql("getSelect")
SELECT 	wm.iAutoId,
          wm.cWorkShiftCode,
          wm.cWorkShiftName
FROM Bd_WorkShiftM wm
WHERE

     wm.isDeleted = '0'
    AND wm.isEnabled = '1'

ORDER BY wm.dCreateTime DESC
#end

#sql("getDataExport")
SELECT
	m.*,
	d.iType,
	d.dStartTime dStartTimed,
	d.dEndTime dEndTimed,
	d.iMinute,
	d.cMemo cMemod,
	jd.name
FROM
	Bd_WorkShiftM m
	LEFT JOIN Bd_WorkShiftD d ON m.iAutoId = d.iWorkShiftMId
	LEFT JOIN #(getBaseDbName()).dbo.jb_dictionary jd ON jd.type_key = 'work_time_type' AND jd.sn = d.iType
	WHERE 1=1
	AND m.isDeleted = '0'
	#if(ids)
        AND m.iAutoId IN (#(ids))
	#end
	#if(cworkshiftcode)
        AND m.cWorkShiftCode LIKE CONCAT('%', #para(cworkshiftcode), '%')
    #end
    #if(cworkshiftname)
        AND m.cWorkShiftName LIKE CONCAT('%', #para(cWorkShiftName), '%')
    #end
    #if(isenabled)
        AND m.isEnabled = #para(isenabled)
    #end
#end


#sql("getDataExport2")
SELECT
    m.*
FROM
    Bd_WorkShiftM m
WHERE 1=1
  AND m.isDeleted = '0'
    #if(ids)
  AND m.iAutoId IN (#(ids))
    #end
    #if(cworkshiftcode)
  AND m.cWorkShiftCode LIKE CONCAT('%', #para(cworkshiftcode), '%')
    #end
    #if(cworkshiftname)
  AND m.cWorkShiftName LIKE CONCAT('%', #para(cWorkShiftName), '%')
    #end
    #if(isenabled)
  AND m.isEnabled = #para(isenabled)
    #end
#end
