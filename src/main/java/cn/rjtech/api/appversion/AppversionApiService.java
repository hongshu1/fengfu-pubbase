package cn.rjtech.api.appversion;

import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.api.JBoltApiBaseService;
import cn.jbolt.core.api.JBoltApiRet;
import cn.rjtech.admin.appversion.AppversionService;
import cn.rjtech.model.main.Appversion;
import com.jfinal.aop.Inject;
import com.jfinal.kit.StrKit;

/**
 * 应用版本
 *
 * @author Kephon
 */
public class AppversionApiService extends JBoltApiBaseService {

    @Inject
    private AppversionService appversionService;

    public JBoltApiRet getLatestVersion(String appcode) {
        Appversion appversion = appversionService.getAppNewVersion(appcode);
        if (ObjUtil.isNull(appversion)) {
            return JBoltApiRet.API_SUCCESS;
        }

        appversion.keep("AppCode", "VersionCode", "VersionDate", "DownloadUrl", "VersionIntro", "CdnUrl");

        // 设置站点下载链接

       // appversion.setDownloadUrl(StrKit.isBlank(appversion.getCdnUrl()) ? Constants.getFullUrl(appversion.getDownloadUrl()) : appversion.getCdnUrl());
        appversion.setDownloadUrl(StrKit.isBlank(appversion.getCdnUrl()) ? appversion.getDownloadUrl() : appversion.getCdnUrl());

        return JBoltApiRet.API_SUCCESS_WITH_DATA(appversion);
    }

}
