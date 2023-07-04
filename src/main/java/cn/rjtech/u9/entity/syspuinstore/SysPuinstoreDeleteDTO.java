package cn.rjtech.u9.entity.syspuinstore;

import com.alibaba.fastjson.annotation.JSONField;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @version 1.0
 * @Author cc
 * @Create 2023/6/13 14:21
 * @Description 通知U8删除采购入库单
 */
@XmlRootElement(name = "SysPuinstoreDeleteDTO")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class SysPuinstoreDeleteDTO {

    @XmlElement(name = "data")
    @JSONField(name = "data")
    private data data;

    public static class data {

        /*登录密码*/
        @XmlElement(name = "password")
        @JSONField(name = "password")
        private String password;

        /*OrganizeCode*/
        @XmlElement(name = "accid")
        @JSONField(name = "accid")
        private String accid;

        /*单据id*/
        @XmlElement(name = "VouchId")
        @JSONField(name = "VouchId")
        private String VouchId;

        /*登录账号*/
        @XmlElement(name = "userID")
        @JSONField(name = "userID")
        private String userID;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public String getVouchId() {
            return VouchId;
        }

        public void setVouchId(String vouchId) {
            VouchId = vouchId;
        }

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }
    }

    public SysPuinstoreDeleteDTO.data getData() {
        return data;
    }

    public void setData(SysPuinstoreDeleteDTO.data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SysPuinstoreDeleteDTO{" + "data=" + data + '}';
    }
}
