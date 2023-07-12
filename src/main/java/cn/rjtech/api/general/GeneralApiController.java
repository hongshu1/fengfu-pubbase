package cn.rjtech.api.general;

import cn.jbolt.core.cache.JBoltDictionaryCache;
import cn.jbolt.core.permission.UnCheck;
import cn.rjtech.base.controller.BaseApiController;
import cn.rjtech.entity.vo.rcvdocqcformm.RcvDocDataDictionaryVo;
import com.jfinal.core.paragetter.Para;
import io.github.yedaxia.apidocs.ApiDoc;

/**
 * @Description 通用接口
 * @Author：XLL
 * @Date:2023/5/13 16:58
 * @Version:1.0
 */
@ApiDoc
public class GeneralApiController extends BaseApiController {

  /**
   * 获取数据字典的数据
   *
   * @param key 数据字典标识KEY
   */
  @ApiDoc(result = RcvDocDataDictionaryVo.class)
  @UnCheck
  public void options(@Para(value = "key") String key) {
    renderJsonData(JBoltDictionaryCache.me.getListByTypeKey(key, true));
  }



}
