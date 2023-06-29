#sql("list")
SELECT * from Bd_SpotCheckForm where isDeleted= 0 and isEnabled=1
    ORDER BY dUpdateTime DESC
#end

#sql("AdminDatas")
SELECT *
FROM Bd_SpotCheckForm
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
    Bd_SpotCheckForm t1
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
    Bd_SpotCheckFormParam t3,
    Bd_SpotCheckFormItem a
        LEFT JOIN Bd_QcItem b ON a.iQcItemId = b.iAutoId
WHERE a.iSpotCheckFormId = '#(iQcFormId)'
  AND a.iAutoId = t3.iSpotCheckFormId

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
    Bd_SpotCheckFormParam t3
        LEFT JOIN Bd_SpotCheckParam t2 ON t3.iSpotCheckParamId = t2.iAutoId
WHERE t1.iSpotCheckFormId = '#(iQcFormId)'
  AND t3.iSpotCheckFormItemId = t1.iAutoId
    #end

#sql("findByIdGetDetail")
SELECT
    b.iAutoId,
    b.iSeq,
    b.iType,
    b.iStdVal,
    b.iMaxVal,
    b.iMinVal,
    b.cOptions,
    c.cQcParamName,
    d.ISEQ as ProdItemIseq
        ,c.iAutoId as SpotCheckParamid
FROM
    Bd_SpotCheckFormTableItem a
        JOIN Bd_SpotCheckFormTableParam b ON a.iSpotCheckFormTableParamId= b.iAutoId
        JOIN Bd_SpotCheckParam c ON a.iSpotCheckFormParamId= c.iAutoId
        JOIN  Bd_SpotCheckFormItem d on d.iQcItemId = a.iSpotCheckFormItemId and  d.iSpotCheckFormId = a.iSpotCheckFormId
        JOIN Bd_QcItem t3 ON t3.iAutoId = d.iQcItemId
WHERE
        1=1
    #if("iprodformid")
    and  a.iSpotCheckFormId = #para(iprodformid)
     #end
ORDER BY b.iseq ,d.ISEQ
    #end