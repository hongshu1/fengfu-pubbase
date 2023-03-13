#sql("paginateUserAndRoleDatas")
SELECT *
FROM
(
    #if(null == roletype)

        SELECT
            u.id,
            u.username AS sn,
            u.name,
            2 AS roletype
        FROM jb_user u
        WHERE 1=1
        #if(sn)
            AND u.username LIKE CONCAT('%', #para(sn), '%')
        #end
        #if(name)
            AND u.name LIKE CONCAT('%', #para(name), '%')
        #end
        #if(keywords)
            AND (
                u.username LIKE CONCAT('%', #para(keywords), '%') OR
                u.name LIKE CONCAT('%', #para(keywords), '%')
            )
        #end

        UNION

        SELECT
            r.id,
            r.sn,
            r.name,
            1 AS roletype
        FROM jb_role r
        WHERE 1=1
        #if(sn)
            AND r.sn LIKE CONCAT('%', #para(sn), '%')
        #end
        #if(name)
            AND r.name LIKE CONCAT('%', #para(name), '%')
        #end
        #if(keywords)
            AND (
                r.sn LIKE CONCAT('%', #para(keywords), '%') OR
                AND r.name LIKE CONCAT('%', #para(keywords), '%')
            )
        #end
    #else

        #switch(roletype)
            #case('1')
                SELECT
                    r.id,
                    r.sn,
                    r.name,
                    1 AS roletype
                FROM jb_role r
                WHERE 1=1
                #if(sn)
                    AND r.sn LIKE CONCAT('%', #para(sn), '%')
                #end
                #if(name)
                    AND r.name LIKE CONCAT('%', #para(name), '%')
                #end
                #if(keywords)
                    AND (
                        r.sn LIKE CONCAT('%', #para(keywords), '%') OR
                        AND r.name LIKE CONCAT('%', #para(keywords), '%')
                    )
                #end
            #case('2')
                SELECT
                    u.id,
                    u.username AS sn,
                    u.name,
                    2 AS roletype
                FROM jb_user u
                WHERE 1=1
                #if(sn)
                    AND u.username LIKE CONCAT('%', #para(sn), '%')
                #end
                #if(name)
                    AND u.name LIKE CONCAT('%', #para(name), '%')
                #end
                #if(keywords)
                    AND (
                        u.username LIKE CONCAT('%', #para(keywords), '%') OR
                        u.name LIKE CONCAT('%', #para(keywords), '%')
                    )
                #end
        #end

    #end

) a

#end