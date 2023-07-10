#sql("findByTable")
SELECT TOP 1 m.*
FROM Bd_CodingRuleM m 
INNER JOIN Bd_Form f ON m.iformid = f.iautoid
WHERE f.cFormCode = #para(cformcode)
    AND f.iorgid = #para(iorgid)
    AND f.isDeleted = '0'
    AND m.isDeleted = '0'
#end