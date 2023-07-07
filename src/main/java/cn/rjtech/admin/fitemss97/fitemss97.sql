#sql("paginateAdminDatas")
	select * from Bd_fitemss97 where 1=1 and isDeleted = '0'
		#if(keywords)
			and (citemcode like concat('%',#para(keywords),'%') or citemname like concat('%',#para(keywords),'%'))
		#end
		#if(iorgid)
			and iorgid = #para(iorgid)
		#end
#end

#sql("findfitemss97classList")
select * from Bd_fitemss97class where  1=1 and isDeleted = '0'
		#if(sn)
			and cItemCcode = #para(sn)
		#end
ORder By citemccode
#end

#sql("findfitemss97List")
select * from Bd_fitemss97  where  1=1 and isDeleted = '0'
		#if(citemccode)
			and cItemCcode = #para(citemccode)
		#end
        #if(fitemss97subid)
			and iautoid = #para(fitemss97subid)
		#end
Order By citemccode
#end


    #sql("findfitemss97parent")
select * from Bd_fitemss97  where  1=1 and isDeleted = '0' and isourceid='0'
    #if(sn)
			and  cItemCcode= #para(sn)
		#end
Order By citemccode
    #end

#sql("findfitemss97sub")
SELECT * FROM Bd_fitemss97 WHERE 1=1 and isDeleted = '0'
        #if(isourceid)
			and isourceid = #para(isourceid)
		#end
    #end

#sql("findsub")
SELECT * FROM (
                  SELECT
                      ( CASE WHEN iPid1 IS NULL THEN iPid2 ELSE iPid1 END ) IPid,*
                  FROM
                      (
                          SELECT
                              (
                                  SELECT TOP
                                      1 b.iAutoId
                                  FROM
                                      Bd_fitemss03class b
                                  WHERE
                                          a.cItemCcode LIKE CONCAT ( '', b.cItemCcode, '%' )
                                    AND b.iAutoId != a.iAutoId
                          ORDER BY
                              b.cItemCcode ASC
                      ) iPid1,
                      ( SELECT iAutoId FROM Bd_fitem WHERE citem_class = '03' ) iPid2,
                      ( SELECT citem_class FROM Bd_fitem WHERE citem_class = '03' ) sn,
                      iAutoId,
                      iOrgId,
                      cOrgCode,
                      cOrgName,
                      cItemCcode,
                      cItemCname,
                      iSource,
                      iSourceId,
                      IsDeleted
                      FROM
		Bd_fitemss03class a UNION ALL
                  SELECT
                      (
                      SELECT TOP
                      1 b.iAutoId
                      FROM
                      Bd_fitemssZFclass b
                      WHERE
                      a.cItemCcode LIKE CONCAT ( '', b.cItemCcode, '%' )
                      AND b.iAutoId != a.iAutoId
                      ORDER BY
                      b.cItemCcode ASC
                      ) iPid1,
                      ( SELECT iAutoId FROM Bd_fitem WHERE citem_class = 'ZF' ) iPid2,
                      ( SELECT citem_class FROM Bd_fitem WHERE citem_class = 'ZF' ) sn,
                      iAutoId,
                      iOrgId,
                      cOrgCode,
                      cOrgName,
                      cItemCcode,
                      cItemCname,
                      iSource,
                      iSourceId,
                      IsDeleted
                  FROM
                      Bd_fitemssZFclass a UNION ALL
                  SELECT
                      (
                      SELECT TOP
                      1 b.iAutoId
                      FROM
                      Bd_fitemss00class b
                      WHERE
                      a.cItemCcode LIKE CONCAT ( '', b.cItemCcode, '%' )
                      AND b.iAutoId != a.iAutoId
                      ORDER BY
                      b.cItemCcode ASC
                      ) iPid1,
                      ( SELECT iAutoId FROM Bd_fitem WHERE citem_class = '00' ) iPid2,
                      ( SELECT citem_class FROM Bd_fitem WHERE citem_class = '00' ) sn,
                      iAutoId,
                      iOrgId,
                      cOrgCode,
                      cOrgName,
                      cItemCcode,
                      cItemCname,
                      iSource,
                      iSourceId,
                      IsDeleted
                  FROM
                      Bd_fitemss00class a UNION ALL
                  SELECT
                      (
                      SELECT TOP
                      1 b.iAutoId
                      FROM
                      Bd_fitemss98class b
                      WHERE
                      a.cItemCcode LIKE CONCAT ( '', b.cItemCcode, '%' )
                      AND b.iAutoId != a.iAutoId
                      ORDER BY
                      b.cItemCcode ASC
                      ) iPid1,
                      ( SELECT iAutoId FROM Bd_fitem WHERE citem_class = '98' ) iPid2,
                      ( SELECT citem_class FROM Bd_fitem WHERE citem_class = '98' ) sn,
                      iAutoId,
                      iOrgId,
                      cOrgCode,
                      cOrgName,
                      cItemCcode,
                      cItemCname,
                      iSource,
                      iSourceId,
                      IsDeleted
                  FROM
                      Bd_fitemss98class a
              ) A
    )v
WHERE 1=1 and IsDeleted='0'
    #if(sn)
      and sn = #para(sn)
    #end
    #if(iautoid)
  and iautoid = #para(iautoid)
    #end
    #if(IPid)
  and IPid = #para(IPid)
    #end
    order by cItemCcode
 #end







