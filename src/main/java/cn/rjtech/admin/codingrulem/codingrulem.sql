#sql("findByTable")
SELECT TOP 1 m.*
FROM Bd_CodingRuleM m
INNER JOIN Bd_Form f ON m.iformid = f.iautoid
WHERE f.cFormCode = #para(cformcode)
    AND f.iorgid = #para(iorgid)
    AND f.isDeleted = '0'
    AND m.isDeleted = '0'
#end

#sql("datas")
SELECT
	f.cFormCode,
	f.cFormName,
	m.*
FROM
	Bd_CodingRuleM m
	LEFT JOIN Bd_Form f ON f.iAutoId = m.iFormId
WHERE
	m.IsDeleted = '0'
	#if(iCodingType)
	    AND m.iCodingType = #para(iCodingType)
	#end
	#if(cFormTypeSn)
	    AND m.cFormTypeSn = #para(cFormTypeSn)
	#end
	#if(cFormName)
	    AND f.cFormName LIKE concat('%',#para(cFormName),'%')
	#end
	ORDER BY m.dCreateTime DESC
#end
