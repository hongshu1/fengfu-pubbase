#sql("list")
SELECT t1.*, t2.cProdItemName
FROM Bd_ProdParam t1
         left join Bd_ProdItem t2 on t1.iProdItemId = t2.iAutoId
WHERE t1.isDeleted = '0'
    #if(iautoid)
  AND t1.iautoid =#para(iautoid)
  #end
  #if(cprodparamname)
  AND t1.cProdParamName = #para(cprodparamname)
  #end
    #if(cproditemname)
  AND t2.cProdItemName = #para(cproditemname)
  #end
  #if(isenabled)
  AND t1.isenabled = #para(isenabled == 'true' ? 1 : 0)
#end
#if(ids)
    AND t1.iautoid IN #(ids)
#end
ORDER BY t1.dUpdateTime
    DESC
    #end
