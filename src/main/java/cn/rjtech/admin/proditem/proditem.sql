#sql("list")
SELECT *
FROM Bd_ProdItem
WHERE isDeleted = '0'
    #if(iautoid)
  AND iautoid =#para(iautoid)
  #end
  #if(cproditemcode)
  AND cproditemcode = #para(cProdItemCode)
  #end
  #if(cproditemname)
  AND cproditemname = #para(cproditemname)
  #end
ORDER BY dUpdateTime DESC
    #end