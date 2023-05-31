package cn.rjtech.api.qcinspection;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.base.NullDataResult;
import cn.rjtech.entity.vo.qcinspection.QcInspectionVo;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.upload.UploadFile;
import io.github.yedaxia.apidocs.ApiDoc;

import java.util.Date;

/**
 * @Description 工程内品质巡查api接口
 */
@ApiDoc
public class QcInspectionApiController extends BaseApiController{
    @Inject
    private QcInspectionApiService qcInspectionApiService;

    /**
     * 查询主表明细
     * @param pageNumber 页码
     * @param pageSize 每页显示条数
     * @param cdocno 巡查单号
     * @param cchainname 分类
     * @param cchainno 连锁No
     */
    @ApiDoc(result = QcInspectionVo.class)
    @UnCheck
    public void datas(@Para(value = "pageNumber",defaultValue = "1") Integer pageNumber,
                      @Para(value = "pageSize",defaultValue = "15") Integer pageSize,
                      @Para(value = "cdocno") String cdocno,
                      @Para(value = "cchainname") String cchainname,
                      @Para(value = "cchainno") String cchainno,
                      @Para(value = "starttime") String starttime, @Para(value = "endtime") String endtime) {
        ValidationUtils.validateIdInt(pageNumber,"页码");
        ValidationUtils.validateIdInt(pageSize,"每页显示条数");
        Kv kv = new Kv();
        kv.set("cdocno", cdocno);
        kv.set("cchainname", cchainname);
        kv.set("cchainno", cchainno);
        kv.set("starttime", starttime);
        kv.set("endtime", endtime);
        renderJBoltApiRet(qcInspectionApiService.getAdminDatas(pageNumber,pageSize,kv));
    }

    /**
     * 点击查看/修改按钮，跳转到“查看”页面
     */
    @ApiDoc(result = QcInspectionVo.class)
    @UnCheck
    public void edit(@Para(value = "iautoid") Long iautoid) {
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(qcInspectionApiService.edit(iautoid));
    }

    /**
     *点击保存，保存页面明细
     * @param iautoid              工程内品质巡查ID
     * @param cchainno             连锁号
     * @param cchainname           连锁名称
     * @param drecorddate          发现日期
     * @param psnnameid            担当人员ID
     * @param depnameid            部门ID
     * @param cplace               位置区域
     * @param cproblem             问题点
     * @param canalysis            原因分析
     * @param cmeasure             对策
     * @param ddate                日期
     * @param idutypersonid        担当
     * @param iestimate            判定: 1. OK 2. NG
     * @param fileData            对策上传
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void updateEditTable(@Para(value = "iautoid") Long iautoid,
                                @Para(value = "cchainno") String cchainno,
                                @Para(value = "cchainname") String cchainname,
                                @Para(value = "isfirstcase") Boolean isfirstcase,
                                @Para(value = "drecorddate") Date drecorddate,
                                @Para(value = "psnnameid") Long psnnameid,
                                @Para(value = "depnameid") Long depnameid,
                                @Para(value = "cplace") Long cplace,
                                @Para(value = "cproblem") String cproblem,
                                @Para(value = "canalysis") String canalysis,
                                @Para(value = "cmeasure") String cmeasure,
                                @Para(value = "ddate") Date ddate,
                                @Para(value = "idutypersonid") Long idutypersonid,
                                @Para(value = "iestimate") Integer iestimate,
                                @Para(value = "fileData") String fileData
    ) {
        Kv kv = new Kv();
        kv.set("iautoid", iautoid);
        kv.set("cchainno", cchainno);
        kv.set("cchainname", cchainname);
        kv.set("isfirstcase", isfirstcase);
        kv.set("drecorddate", drecorddate);
        kv.set("depnameid", depnameid);
        kv.set("psnnameid", psnnameid);
        kv.set("cplace", cplace);
        kv.set("cproblem", cproblem);
        kv.set("canalysis", canalysis);
        kv.set("cmeasure", cmeasure);
        kv.set("ddate", ddate);
        kv.set("idutypersonid", idutypersonid);
        kv.set("iestimate", iestimate);
        kv.set("fileData", fileData);
        renderJBoltApiRet(qcInspectionApiService.update(kv));
    }


    /**
     * 查询人员和部门
     *  @param cus        输入人员或者部门搜索
     * */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void DepartmentList(@Para(value = "cus") String cus){
        Kv kv = new Kv();
        kv.set("cus", cus);
        renderJBoltApiRet(qcInspectionApiService.DepartmentList(kv));
    }

    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void deletes(@Para(value = "iautoid") String iautoid){
        ValidationUtils.notNull(iautoid, JBoltMsg.PARAM_ERROR);
        renderJBoltApiRet(qcInspectionApiService.deletes(iautoid));
    }


    /**
     * 上传文件
     *
     * @param file   文件
     * @param folder 文件夹
     * @param type   1. 图片 2. 视频 3. 音频 4. office文档 5. 其它附件
     */
    @ApiDoc(result = NullDataResult.class)
    @UnCheck
    public void flies(@Para(value = "file") UploadFile file,
                      @Para(value = "folder") String folder,
                      @Para(value = "type") Integer type){

        renderJBoltApiRet(qcInspectionApiService.flies(file,folder,type));
    }


}







