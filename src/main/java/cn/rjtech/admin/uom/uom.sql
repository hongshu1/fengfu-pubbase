#sql("list")
SELECT
    uc.cUomClassName,
    uc.cUomClassSn,
    uc.cuomclasscode,
    u.*
from Bd_Uom u
    LEFT JOIN Bd_UomClass uc ON u.iUomClassId = uc.iAutoId
WHERE u.isDeleted = '0'
    #if(uomclassid)
        AND uc.iAutoId = #para(uomclassid)
    #end
    #if(ids)
        AND u.iautoid IN #(ids)
    #end
    #if(cuomcode)
        AND u.cuomcode = #para(cuomcode)
    #end
    #if(cuomname)
        AND u.cuomname = #para(cuomname)
    #end
    #if(cuomclasssn)
        AND uc.cuomclasssn = #para(cuomclasssn)
    #end
ORDER BY u.isBase DESC, u.dCreateTime DESC
#end