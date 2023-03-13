package cn.rjtech.base.controller;

import cn.rjtech.kit.RestResult;

/**
 * 适配返回code/data/message格式的Controller
 *
 * @author Kephon
 */
public class BaseRestController extends BaseAdminController {

    @Override
    public void renderJsonSuccess() {
        renderJson(RestResult.ok());
    }

    @Override
    public void renderJsonSuccess(String msg) {
        renderJson(RestResult.ok(msg));
    }

    /**
     * 返回操作成功的对象
     *
     * @param data 返回数据对象
     */
    public void renderJsonSuccess(Object data) {
        renderJson(RestResult.data(data).setOk());
    }

    @Override
    public void renderJsonFail(String msg) {
        renderJson(RestResult.fail(msg));
    }

    /**
     * 返回指定错误码的错误信息
     */
    public void renderJsonFail(Integer code, String msg) {
        renderJson(RestResult.msg(msg).set("code", code));
    }

}