#sql("AdminDatas")
SELECT *
    FROM Bd_QcForm
WHERE 1 = 1
    #if(formName)
  AND cQcFormName =#para(formName)
  #end
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