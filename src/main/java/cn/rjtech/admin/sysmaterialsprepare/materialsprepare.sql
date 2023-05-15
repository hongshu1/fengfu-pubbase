
#sql("recpor")
SELECT mp.AutoID as iAutoID,
       mp.OrganizeCode,
       mp.BillNo,
       mp.BillType,
       mp.BillDate,
       mp.AuditPerson,
       mp.AuditDate,
       mp.CreatePerson,
       mp.CreateDate,
       mp.ModifyPerson,
       mp.ModifyDate
FROM T_Sys_MaterialsPrepare mp
         LEFT JOIN T_Sys_MaterialsPrepareDetail mpd ON mpd.MasID = mp.AutoID
where 1 = 1

ORDER BY mp.CreateDate DESC
    #end

    #sql("dList")
SELECT a.*
FROM T_Sys_OtherInDetail a
         left join T_Sys_OtherIn i on a.MasID = i.AutoID
where 1 = 1
  and a.isdeleted = 0 #if(masid)
		and a.MasID = #para(masid)
	#end
ORDER BY a.ModifyDate DESC
    #end

