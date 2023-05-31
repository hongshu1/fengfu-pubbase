package cn.rjtech.entity.vo.upload;

import java.io.Serializable;

/**
 * 上传接口返回数据
 *
 * @author Kephon
 */
public class UploadData implements Serializable {

    private String uri;

    public UploadData() {
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "UploadData{" +
                "uri='" + uri + '\'' +
                '}';
    }

}
