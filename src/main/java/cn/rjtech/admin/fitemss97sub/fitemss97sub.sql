
#sql("fitemss00list")
SELECT     Bd_fitemss00class.iautoid as iautoid,
           Bd_fitemss00.citemcode as citemcode,
           Bd_fitemss00.citemname as citemname,
           Bd_fitemss00.bclose as  bclose,
           Bd_fitemss00class.citemccode as citemccode ,
           Bd_fitemss00class.citemcname as citemcname
FROM	Bd_fitemss00class
            JOIN Bd_fitemss00 ON    Bd_fitemss00class.cItemCcode=Bd_fitemss00.citemccode
WHERE   Bd_fitemss00class.isDeleted = '0'
  and Bd_fitemss00.isDeleted = '0'
    #if(fitemss97subid)
			and Bd_fitemss00class.iautoid IN (
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
SELECT     Bd_fitemss98class.iautoid as iautoid,
           Bd_fitemss98.citemcode as citemcode,
           Bd_fitemss98.citemname as citemname,
           Bd_fitemss98.bclose as  bclose,
           Bd_fitemss98.cDirection as cdirection,
           Bd_fitemss98class.citemccode as citemccode ,
           Bd_fitemss98class.citemcname as citemcname
FROM	Bd_fitemss98class
  JOIN Bd_fitemss98 ON    Bd_fitemss98class.cItemCcode=Bd_fitemss98.citemccode
WHERE   Bd_fitemss98class.isDeleted = '0'
  and Bd_fitemss98.isDeleted = '0'
    #if(fitemss97subid)
			and  Bd_fitemss98class.iautoid IN (
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
SELECT     Bd_fitemss03class.iautoid as iautoid,
           Bd_fitemss03.citemcode as citemcode,
           Bd_fitemss03.citemname as citemname,
           Bd_fitemss03.bclose as  bclose,
           Bd_fitemss03class.citemccode as citemccode ,
           Bd_fitemss03class.citemcname as citemcname
FROM	Bd_fitemss03class
            JOIN Bd_fitemss03 ON    Bd_fitemss03class.cItemCcode=Bd_fitemss03.citemccode
WHERE   Bd_fitemss03class.isDeleted = '0'
  and Bd_fitemss03.isDeleted = '0'
    #if(fitemss97subid)
			and Bd_fitemss03class.iautoid IN (
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
SELECT     Bd_fitemssZFclass.iautoid as iautoid,
           Bd_fitemssZF.citemcode as citemcode,
           Bd_fitemssZF.citemname as citemname,
           Bd_fitemssZF.bclose as  bclose,
           Bd_fitemssZF.cDirection as cdirection,
           Bd_fitemssZFclass.citemccode as citemccode ,
           Bd_fitemssZFclass.citemcname as citemcname
FROM	Bd_fitemssZFclass
            JOIN Bd_fitemssZF ON    Bd_fitemssZFclass.cItemCcode=Bd_fitemssZF.citemccode
WHERE   Bd_fitemssZFclass.isDeleted = '0'
  and Bd_fitemssZF.isDeleted = '0'
    #if(fitemss97subid)
			and Bd_fitemssZFclass.iautoid IN (
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





