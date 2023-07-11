#sql("paginateAdminDatas")
SELECT
	wh.*,
	dt.cDepName
FROM
	Bd_Warehouse wh
	LEFT JOIN bd_department dt ON wh.cDepCode = dt.cDepCode
#if(iorgId)
	and wh.iOrgId = #para(iorgId)
#end
	 and dt.isDeleted = 0
WHERE wh.isDeleted = 0
	#if(cwhcode)
	    AND wh.cWhCode  LIKE CONCAT('%', #para(cwhcode), '%')
	#end
	#if(cwhname)
	    AND wh.cWhName  LIKE CONCAT('%', #para(cwhname), '%')
	#end
	#if(idepid)
	    AND dt.iAutoId = #(idepid)
	#end
	#if(imaxstockMin)
    AND wh.iMaxStock >= #para(imaxstockMin)
    #end
    #if(imaxstockMax)
        AND wh.iMaxStock <= #para(imaxstockMax)
    #end
    #if(imaxspaceMin)
        AND wh.iMaxSpace >= #para(imaxspaceMin)
    #end
    #if(imaxspaceMax)
        AND wh.iMaxSpace <= #para(imaxspaceMax)
    #end
    #if(isenabled)
        AND wh.isEnabled = #para(isenabled)
    #end

    #if(ids)
        AND CHARINDEX(','+cast((select wh.iAutoId) as nvarchar(20))+',' , ','+#para(ids)+',') > 0
    #end

ORDER BY wh.dCreateTime DESC
#end

#sql("findByMouldsId")
SELECT wh.*
FROM (
        SELECT iWarehouseId
        FROM Bd_MouldsRecord
        WHERE isDeleted = '0'
            AND iType = 1 AND iMouldsId = #para(iMouldsId)
        group by iWarehouseId
    ) whr
    LEFT JOIN Bd_Warehouse wh ON whr.iWarehouseId = wh.iAutoId
WHERE wh.isDeleted = '0'
    AND wh.isEnabled = '1'
ORDER BY wh.dCreateTime
#end

#sql("findByWareHouseClassList")
SELECT
    DISTINCT
	ware.cWhCode,
	ware.cWhName,
	ware.iAutoId,
	ware.iSource,
	ware.iSourceId,
	dict2.sn
FROM
	Bd_Warehouse ware
	INNER JOIN #(getBaseDbName()).dbo.jb_dictionary dict ON dict.type_key = 'warehouse_class'
	AND dict.sn = ware.cWhClassSn
	AND dict.enable = 1
	INNER JOIN #(getBaseDbName()).dbo.jb_dictionary dict2 ON dict2.type_key = 'analysis_warehouse_class'
	AND dict2.enable = 1
	AND dict2.name = dict.Name
WHERE
    ware.iOrgId = #para(orgId)
	AND ware.isDeleted = 0
	AND ware.isEnabled = 1
#end

#sql("options")
SELECT
	wh.*
FROM Bd_Warehouse wh
WHERE wh.isDeleted = 0
	#if(iautoid)
	    AND wh.iautoid = #para(iautoid)
	#end
	#if(cwhcode)
	    AND wh.cWhCode = #para(cwhcode)
	#end
	#if(cwhname)
	    AND wh.cWhName = #para(cwhname)
	#end
    #if(isenabled)
        AND wh.isenabled = #para(isenabled == 'true' ? 1 : 0)
    #end
ORDER BY dCreateTime DESC
#end

#sql("findByWarehouse")
SELECT
	iAutoId,
	cWhCode,
	cWhName
FROM
	Bd_Warehouse
	WHERE
	    iOrgId = #para(orgId)
	AND isDeleted = '0'
	AND isEnabled = '1'
#end

#sql("verifyDuplication")
SELECT
	ISNULL( COUNT ( iAutoId ), 0 )
FROM
	Bd_Warehouse
	WHERE isDeleted=0
#if(cwhcode)
    AND cWhCode = #para(cwhcode)
#end
#if(cwhname)
    AND cWhName = #para(cwhname)
#end
#if(iautoid)
    AND iautoid != #para(iautoid)
#end

#end

#sql("getCdepnameByCdepcode")
select cDepCode from bd_department where cDepName = #para(cdepname)
#end

#sql("getCpsnnameByCpsnnum")
SELECT cPsn_Num FROM Bd_Person WHERE cPsn_Name = #para(cdepname)
#end

#sql("getWarehouseareaById")
select ISNULL(SUM(iAutoId), 0) FROM Bd_Warehouse_Area WHERE iWarehouseId=#(id) AND isDeleted=0
#end
