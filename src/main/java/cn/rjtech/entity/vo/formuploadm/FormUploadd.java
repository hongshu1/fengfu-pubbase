package cn.rjtech.entity.vo.formuploadm;

import java.io.Serializable;

/**
 * 记录上传从表
 */
public class FormUploadd implements Serializable {

    /**主键ID*/
    private   Long  iAutoId;
    /**上传记录主表ID*/
    private  Long  iFormUploadMid;
    /**附件;多个“,”分隔*/
    private  String cAttachments;
    /**备注*/
    private  String  cMemo;

    public Long getiAutoId(){
        return iAutoId;
    }
    public void setiAutoId(Long iAutoId){
        this.iAutoId=iAutoId;
    }

    public Long getiFormUploadMid(){
        return iFormUploadMid;
    }
    public void setiFormUploadMid(Long iFormUploadMid){
        this.iFormUploadMid=iFormUploadMid;
    }

    public String getcAttachments(){
        return cAttachments;
    }
    public void setcAttachments(String cAttachments){
        this.cAttachments=cAttachments;
    }

    public String getcMemo(){
        return cMemo;
    }
    public void setcMemo(String cMemo){
        this.cMemo=cMemo;
    }

}
