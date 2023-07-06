#sql("AdminDatas")
SELECT *
    FROM Bd_QcForm
WHERE 1 = 1
    #if(cQcFormName)
  AND cQcFormName like '%#(cQcFormName)%'
  #end
order by dUpdateTime desc
#end




 #sql("lists")
SELECT distinct
    *
FROM
    Bd_QcForm t1
WHERE
    t1.cOrgCode = '#(orgCode)'

    #if(ids)
and t1.AutoID in (
#for(v:ids.split(','))
'#(v)' #(for.last?'':',')
#end
)
#end
order by t1.CreateDate desc
#end


#sql("qcformtableparamOneTitle")
SELECT
    b.cQcItemName AS pcname,
    COUNT ( 1 ) AS cn
FROM
    Bd_QcFormParam t3,
    Bd_QcFormItem a
        LEFT JOIN Bd_QcItem b ON a.iQcItemId = b.iAutoId
WHERE a.iQcFormId = '#(iQcFormId)'
  AND a.iAutoId = t3.iQcFormItemId

GROUP BY
    b.cQcItemName,
    a.iSeq
ORDER BY
    a.iSeq ASC
#end
#sql("qcformtableparamTwoTitle")

SELECT
    t2.cQcParamName
FROM
    Bd_QcFormItem t1,
    Bd_QcFormParam t3
        LEFT JOIN Bd_QcParam t2 ON t3.iQcParamId = t2.iAutoId
WHERE t1.iQcFormId = '#(iQcFormId)'
  AND t3.iQcFormItemId = t1.iAutoId
#end

#sql ("getBycQcFormName")
select * from Bd_QcForm where cQcFormName=#para(cTypeNames)
#end
