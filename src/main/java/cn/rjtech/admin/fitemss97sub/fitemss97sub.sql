
#sql("fitemss00list")
SELECT
    Bd_fitemss97.iautoid as iautoid,
    Bd_fitemss00.citemcode as citemcode,
    Bd_fitemss00.citemname as citemname,
    Bd_fitemss00.bclose as  bclose,
    Bd_fitemss97.citemcode as citemccode ,
    Bd_fitemss97.citemname as citemcname
FROM Bd_fitemss97
         JOIN Bd_fitemss00class ON Bd_fitemss97.citemcode=Bd_fitemss00class.cItemCcode
         JOIN Bd_fitemss00 ON  Bd_fitemss00class.cItemCcode=Bd_fitemss00.citemccode
WHERE Bd_fitemss97.citemccode='00' and Bd_fitemss97.isDeleted = '0' and Bd_fitemss00class.isDeleted = '0'
  and Bd_fitemss00.isDeleted = '0'
    #if(fitemss97subid)
			and Bd_fitemss97.iAutoId IN (
            #for(v:fitemss97subid.split(','))
            '#(v)' #(for.last?'':',')
            #end
        )
		#end
#if(keywords)
			and (Bd_fitemss00.citemcode like concat('%',#para(keywords),'%') or Bd_fitemss00.citemname  like concat('%',#para(keywords),'%'))
		#end
Order By citemcode asc
    #end



    #sql("fitemss98list")
SELECT
    Bd_fitemss97.iautoid as iautoid,
    Bd_fitemss98.citemcode as citemcode,
    Bd_fitemss98.citemname as citemname,
    Bd_fitemss98.bclose as  bclose,
    Bd_fitemss98.cDirection as cdirection,
    Bd_fitemss97.citemcode as citemccode ,
    Bd_fitemss97.citemname as citemcname
FROM Bd_fitemss97
         JOIN Bd_fitemss98class ON Bd_fitemss97.citemcode=Bd_fitemss98class.cItemCcode
         JOIN Bd_fitemss98 ON  Bd_fitemss98class.cItemCcode=Bd_fitemss98.citemccode
WHERE Bd_fitemss97.citemccode='98' and Bd_fitemss97.isDeleted = '0' and Bd_fitemss98class.isDeleted = '0'
  and Bd_fitemss98.isDeleted = '0'
    #if(fitemss97subid)
			and Bd_fitemss97.iAutoId IN (
            #for(v:fitemss97subid.split(','))
            '#(v)' #(for.last?'':',')
            #end
        )
		#end
#if(keywords)
			and (Bd_fitemss98.citemcode like concat('%',#para(keywords),'%') or  Bd_fitemss98.citemname like concat('%',#para(keywords),'%'))
		#end
		Order By citemcode asc
#end


 #sql("fitemss03list")
SELECT
    Bd_fitemss97.iautoid as iautoid,
    Bd_fitemss03.citemcode as citemcode,
    Bd_fitemss03.citemname as citemname,
    Bd_fitemss03.bclose as  bclose,
    Bd_fitemss97.citemcode as citemccode ,
    Bd_fitemss97.citemname as citemcname
FROM Bd_fitemss97
         JOIN Bd_fitemss03class ON Bd_fitemss97.citemcode=Bd_fitemss03class.cItemCcode
         JOIN Bd_fitemss03 ON  Bd_fitemss03class.cItemCcode=Bd_fitemss03.citemccode
WHERE Bd_fitemss97.citemccode='03' and Bd_fitemss97.isDeleted = '0' and Bd_fitemss03class.isDeleted = '0'
  and Bd_fitemss03.isDeleted = '0'
    #if(fitemss97subid)
			and Bd_fitemss97.iAutoId IN (
            #for(v:fitemss97subid.split(','))
            '#(v)' #(for.last?'':',')
            #end
        )
		#end
#if(keywords)
			and (Bd_fitemss03.citemcode like concat('%',#para(keywords),'%') or Bd_fitemss03.citemname like concat('%',#para(keywords),'%'))
		#end
Order By citemcode asc
    #end

#sql("fitemssZFlist")
SELECT
    Bd_fitemss97.iautoid as iautoid,
    Bd_fitemssZF.citemcode as citemcode,
    Bd_fitemssZF.citemname as citemname,
    Bd_fitemssZF.bclose as  bclose,
    Bd_fitemssZF.cDirection as cdirection,
    Bd_fitemss97.citemcode as citemccode ,
    Bd_fitemss97.citemname as citemcname
FROM Bd_fitemss97
         JOIN Bd_fitemssZFclass ON Bd_fitemss97.citemcode=Bd_fitemssZFclass.cItemCcode
         JOIN Bd_fitemssZF ON  Bd_fitemssZFclass.cItemCcode=Bd_fitemssZF.citemccode
WHERE Bd_fitemss97.citemccode='ZF' and Bd_fitemss97.isDeleted = '0' and Bd_fitemssZFclass.isDeleted = '0'
  and Bd_fitemssZF.isDeleted = '0'
    #if(fitemss97subid)
			and Bd_fitemss97.iAutoId IN (
            #for(v:fitemss97subid.split(','))
            '#(v)' #(for.last?'':',')
            #end
        )
		#end
#if(keywords)
			and (Bd_fitemssZF.citemcode like concat('%',#para(keywords),'%') or Bd_fitemssZF.citemname like concat('%',#para(keywords),'%'))
		#end
Order By citemcode asc
    #end





