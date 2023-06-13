#sql("getList")
SELECT uo.*,
       d.org_name AS name,
       u.name AS parent_psn_name,
       bp.cpsn_name
FROM base_user_org uo
    INNER JOIN jb_org d ON uo.org_id = d.id
    LEFT JOIN jb_user u ON uo.parent_psn_id = u.id
    LEFT JOIN #(getMomdataDbName()).dbo.bd_person bp ON uo.ipersonid = bp.iautoid
WHERE uo.user_id = #para(userId)
    AND uo.is_deleted = '0'
ORDER BY id
#end

#sql("getAutocompleteDatas")
select top #(limit) id,username,name,email from jb_user
where 1=1
    #if(q)
    and (username like concat('%',#para(q),'%') or name like concat('%',#para(q),'%'))
  #end
#end
