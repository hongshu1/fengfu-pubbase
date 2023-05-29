#sql("getList")
SELECT pd.*,
       v.cvenname,
       d.cdepname AS cbudgetdepname,
       (CASE pd.iSourceType
        WHEN 1 THEN (SELECT s.csubjectname FROM PL_Expense_Budget_Item ebi
			        INNER JOIN bas_subjectm s ON ebi.iLowestSubjectid = s.iautoid
                    WHERE ebi.iautoid = pd.iSourceId)
        ELSE NULL END) AS cLowestSubjectName
FROM PL_ProposalD pd
left JOIN bd_Vendor v ON pd.cvencode = v.cvencode
left JOIN bd_Department d ON pd.cbudgetdepcode = d.cdepcode
WHERE pd.iproposalmid = #para(iproposalmid)
ORDER BY pd.iprojectcardid asc,pd.iautoid asc
#end

