package cn.jbolt._admin.role;

import cn.jbolt.core.model.Role;
import cn.jbolt.core.service.JBoltRoleService;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 角色管理Service
 * @ClassName:  RoleService   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2019年3月27日 上午11:54:25   
 */
public class RoleService extends JBoltRoleService {

    public Page<Record> paginateUserAndRoleDatas(Integer pageNumber, Integer pageSize, Kv para) {
        return dbTemplate("role.paginateUserAndRoleDatas", para).paginate(pageNumber, pageSize);
    }




    public List<Record> autocomplete(Kv para) {
        return dbTemplate("role.autocompletelist",para).find();
    }


    public Role findFirstByName(String roleStr) {
        return findFirst(selectSql().eq("name", roleStr).first());
    }
    
}
