#sql("paginatePermissions")
SELECT dp.id,
       dp.busobject_id,
       dp.object_type,
       dp.object_id,
       '#(busobjectname)' AS busobject_name,

        #switch(datasource)
            #case('1')
                ISNULL(dp.busobject_value_id, p.id) AS busobject_value_id,
                ISNULL(dp.busobject_value_name, p.name) AS busobject_value_name,
                p.sn AS code,
            #case('2')
                ISNULL(dp.busobject_value_id, p.iautoid) AS busobject_value_id,
                ISNULL(dp.busobject_value_name, p.name) AS busobject_value_name,
                p.cdepcode AS code,
        #end

       dp.is_view_enabled,
       dp.is_edit_enabled,
       dp.is_delete_enabled,
       dp.version_num
FROM base_data_permission dp

    #switch(datasource)
        ### 用户
        #case('1')
            RIGHT JOIN jb_user p ON dp.busobject_value_id = p.id
        ### 部门
        #case('2')
            RIGHT JOIN #(getMomdataDbName()).dbo.Bd_Department p ON dp.busobject_value_id = p.iautoid
    #end

    AND dp.object_type = #para(objecttype)
    AND dp.object_id = #para(objectid)
    AND dp.busobject_id = #para(busobjectid)
    AND dp.is_deleted = '0'
WHERE 1=1
    #if(q)
        #switch(datasource)
            ### 用户
            #case('1')
                AND (
                    p.sn LIKE CONCAT('%', #para(q), '%') OR
                    p.username LIKE CONCAT('%', #para(q), '%')
                )
            ### 部门
            #case('2')
                AND (
                    p.cdepcode LIKE CONCAT('%', #para(q), '%') OR
                    p.cdepname LIKE CONCAT('%', #para(q), '%')
                )
        #end
    #end
#end

#sql("getAccessCdepcodes")
SELECT
    DISTINCT
    #switch(busobjectCode)
        ### 用户
        #case('01')

        ### 部门
        #case('02')
            d.cdepcode
    #end
FROM base_data_permission dp
    #switch(busobjectCode)
        ### 用户
        #case('01')

        ### 部门
        #case('02')
            INNER JOIN #(getMomdataDbName()).dbo.Bd_Department d ON dp.busobject_value_id = d.iautoid
    #end
WHERE dp.is_deleted = '0'
    AND dp.#(fieldName) = '1'
    AND (

        ### 角色数据权限
        (dp.object_type = 1 AND dp.object_id IN (#(roles)))

        ### 用户数据权限
        #if(roles)
        OR (dp.object_type = 2 AND dp.object_id = #para(userId))
        #end

    )
#end

#sql("getAccessDeptIdList")
SELECT DISTINCT busobject_value_id
FROM base_data_permission dp
WHERE dp.is_deleted = '0'
    AND dp.#(fieldName) = '1'
    AND (

        (dp.object_type = 1 AND dp.object_id IN (#(roles)))

        #if(roles)
        OR (dp.object_type = 2 AND dp.object_id = #para(userId))
        #end
    )
#end
