package cn.rjtech.admin.sysenumeration;


import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.model.User;
import cn.jbolt.core.model.base.JBoltBaseModel;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;


public class SysEnumerationService  extends BaseService {


    @Override
    protected JBoltBaseModel dao() {
        return null;
    }

    @Override
    protected int systemLogTargetType() {
        return 0;
    }

    @Inject
    private UserService userService;

    /**
     * 根据仓库 whcode 查询仓库名称
     * @param whCode
     * @return
     */
    public String getWhname(String whCode) {
        Record first = Db.use(u8SourceConfigName()).findFirst("select * from V_Sys_WareHouse where WhCode=?", whCode);
        if ((null==first)){
            return "";
        }
        return first.get("whname");
    }


    /**
     *  根据供应商 vencode 查询供应商名字
     * @param vencode
     * @return
     */
    public String getVenName(String vencode) {
        Record first = Db.use(u8SourceConfigName()).findFirst("select * from V_Sys_Vendor where VenCode=?", vencode);
        if ((null==first)){
            return "";
        }
        return first.get("VenName");
    }

    /**
     *  根据用户 username 查询名字
     * @param username
     * @return
     */
    public String getUserName(Long username) {
        User user = userService.findFirst("select * from  jb_user where id = ?", username);
        return user.getName();
    }



    public List<Record> getwareHouseDatas(Kv kv) {
        return dbTemplate(u8SourceConfigName(), "sysenumeration.wareHouse", kv).find();
    }

    public List<Record> getvendorDatas(Kv kv) {
        return dbTemplate(u8SourceConfigName(), "sysenumeration.vendor", kv).find();
    }

    public List<Record>  getbarcodedetailDatas(Kv kv) {
        return dbTemplate(u8SourceConfigName(), "sysenumeration.barcodedetail", kv).find();
    }

    public Object getinventoryDatas(Kv kv) {
        return dbTemplate(u8SourceConfigName(), "sysenumeration.inventory", kv).find();
    }

    public Object getmodetailDatas(Kv kv) {
        return dbTemplate(u8SourceConfigName(), "sysenumeration.modetail", kv).find();
    }

    public Object getpodetailDatas(Kv kv) {
        return dbTemplate(u8SourceConfigName(), "sysenumeration.podetail", kv).find();
    }

    public Object getpositionDatas(Kv kv) {
        return dbTemplate(u8SourceConfigName(), "sysenumeration.position", kv).find();
    }

    public Object getcustomerDatas(Kv kv) {
        return dbTemplate(u8SourceConfigName(), "sysenumeration.customer", kv).find();
    }
}
