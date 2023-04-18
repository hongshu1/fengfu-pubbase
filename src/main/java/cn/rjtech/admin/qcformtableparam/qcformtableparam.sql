#sql("formTableParamList")
SELECT
    t2.cQcParamName,
    t4.*
FROM
    Bd_QcFormTableParam t4,
    Bd_QcFormItem t1,
    Bd_QcFormParam t3
        LEFT JOIN Bd_QcParam t2 ON t3.iQcParamId = t2.iAutoId
WHERE
        t1.iQcFormId = '#(iqcformid)'
  AND t1.iAutoId = t3.iQcFormItemId
ORDER BY
    t1.iSeq asc
    #end



#sql("customerList")

SELECT
    t2.cQcParamName
FROM
    Bd_QcFormItem t1
        LEFT JOIN Bd_QcParam t2 ON t1.iQcItemId = t2.iQcItemId
WHERE t1.iQcFormId = '#(iQcFormId)'
    #end



