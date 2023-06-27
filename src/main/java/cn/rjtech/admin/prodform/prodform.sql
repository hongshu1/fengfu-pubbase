#sql("list")
SELECT * from Bd_ProdForm where isDeleted= 0 and isEnabled=1
    ORDER BY dUpdateTime DESC
#end

#sql("AdminDatas")
SELECT *
FROM Bd_ProdForm
WHERE 1 = 1
    #if(cQcFormName)
  AND cSpotCheckFormName like '%#(cQcFormName)%'
  #end
order by dUpdateTime desc
    #end




    #sql("lists")
SELECT distinct
    *
FROM
    Bd_ProdForm t1
WHERE
        t1.cOrgCode = '#(orgCode)'

    #if(ids)
and t1.iAutoId in (
#for(v:ids.split(','))
'#(v)' #(for.last?'':',')
#end
)
#end
order by t1.dCreateTime desc
    #end


    #sql("qcformtableparamOneTitle")
SELECT
    b.cQcItemName AS pcname,
    COUNT ( 1 ) AS cn
FROM
    Bd_ProdFormParam t3,
    Bd_ProdFormItem a
        LEFT JOIN Bd_ProdItem b ON a.iProdItemId = b.iAutoId
WHERE a.iProdFormId = '#(iQcFormId)'
  AND a.iAutoId = t3.iProdFormId

GROUP BY
    b.cProdItemName,
    a.iSeq
ORDER BY
    a.iSeq ASC
    #end

#sql("qcformtableparamTwoTitle")

SELECT
    t2.cQcParamName
FROM
    Bd_ProdFormItem t1,
    Bd_ProdFormParam t3
        LEFT JOIN Bd_ProdParam t2 ON t3.iProdParamId = t2.iAutoId
WHERE t1.iProdFormId = '#(iQcFormId)'
  AND t3.iProdFormItemId = t1.iAutoId
    #end
