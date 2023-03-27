#sql("getTableList")
SELECT
	ROW_NUMBER ( ) OVER ( ORDER BY a.name ) AS NO,
	a.name AS EntityTable,
	CONVERT ( NVARCHAR ( 100 ), isnull( g.[value], '-' ) ) AS EntityName
FROM
	sys.tables a
	LEFT JOIN sys.extended_properties g ON ( a.object_id = g.major_id AND g.minor_id = 0 )
#end

#sql("getDescflexfielddefList")
    SELECT * FROM Bd_DescFlexFieldDef WHERE 1=1
     #if(isenabled)
        AND isEnabled = #para(isenabled)
     #end
     #if(centityname)
        AND cEntityName  LIKE CONCAT('%', #para(centityname), '%')
     #end
     #if(leftcentityname)
        AND cEntityTable  LIKE CONCAT('%', #para(leftcentityname), '')
     #end
#end