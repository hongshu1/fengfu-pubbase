#sql("list")
SELECT
    ob.cTypeNames,
    w.cWorkClassName,
    o.*
FROM Bd_Operation o
    LEFT JOIN Bd_WorkClass w ON w.iAutoId=o.iWorkClassId
    LEFT JOIN (
        SELECT  iOperationId,ctypenames = (
             stuff((
                SELECT ',' + cast(a.ctypename AS VARCHAR)
                FROM (
                    SELECT ob.iOperationId, b.ctypename
                    FROM Bd_BadnessClass b
                        LEFT JOIN Bd_OperationBadness ob ON ob.iBadnessClassId = b.iautoid
                    ) a
                WHERE a.iOperationId = Test.iOperationId FOR xml path('')
            ), 1, 1, '')
        ) FROM Bd_OperationBadness AS Test
        GROUP BY iOperationId
    ) ob ON ob.iOperationId=o.iAutoId
WHERE o.isDeleted = '0'
#if(ids)
    AND o.iautoid IN #(ids)
#end
#if(coperationcode)
    AND o.coperationcode = #para(coperationcode)
#end
#if(coperationname)
    AND o.coperationname = #para(coperationname)
#end
#if(iworkclassid)
    AND o.iworkclassid = #para(iworkclassid)
#end
#if(isenabled)
    AND o.isenabled = #para(isenabled == 'true' ? 1 : 0)
#end
ORDER BY o.dCreateTime DESC
#end

#sql("badnessOptions")
SELECT *
FROM Bd_BadnessClass
WHERE isDeleted = '0'
    AND isEnabled = '1'
#end

#sql("findBadnessClassById")
SELECT *
FROM Bd_BadnessClass
WHERE iautoid in (
    SELECT iBadnessClassId
    FROM Bd_OperationBadness
    WHERE iOperationId = #para(iautoid)
)
#end

#sql("findItemOperationDatas")
SELECT
	a.iItemId,
	item.cItemCode,
	item.cItemName,
	 w.cWorkClassName,
	 b.iSeq,
	o.*
FROM
	Bd_ItemRouting a
	LEFT JOIN Bd_ItemRoutingConfig b ON a.iAutoId = b.iItemRoutingId
	LEFT JOIN Bd_ItemRoutingConfig_Operation c ON c.iItemRoutingConfigId = b.iAutoId
	LEFT JOIN Bd_Operation o ON c.iOperationId = o.iAutoId
	LEFT JOIN Bd_WorkClass w ON w.iAutoId=o.iWorkClassId
	LEFT JOIN Bd_ItemMaster item ON item.iAutoid = a.iItemId
WHERE
	o.iAutoId IS NOT NULL
	#if(iitemid)
	    AND a.iItemId = #para(iitemid)
	#end
#end