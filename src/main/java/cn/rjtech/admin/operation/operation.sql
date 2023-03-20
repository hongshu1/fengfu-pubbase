#sql("list")
SELECT o.*,w.cWorkClassName
FROM Bd_Operation o
         LEFT JOIN Bd_WorkClass w
                   on o.iWorkClassId = w.iautoid
WHERE o.isDeleted = '0'
  #if(iworkclassid)
  AND o.iWorkClassId = #para(iworkclassid)
  #end
  #if(coperationcode)
  AND o.coperationcode = #para(coperationcode)
  #end
  #if(coperationname)
  AND o.coperationname = #para(coperationname)
  #end
  #if(ilevel)
  AND o.ilevel = #para(ilevel)
  #end
  #if(isenabled)
  AND o.isenabled = #para(isenabled == 'true' ? 1 : 0)
  #end
ORDER BY o.dUpdateTime
DESC
#end