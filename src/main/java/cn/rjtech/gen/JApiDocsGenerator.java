package cn.rjtech.gen;

import cn.rjtech.plugins.PostmanDocPlugin;
import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;

/**
 * JApiDocs 无需额外注解的 API 文档生成工具
 *
 * @author Kephon
 *  <a href="https://github.com/YeDaxia/JApiDocs">源码</a>
 *  <a href="https://japidocs.agilestudio.cn/#/zh-cn/">文档</a>
 */
public class JApiDocsGenerator {

    /**
     * JApiDocs 生成器
     * 如果报错，做如下检查：
     * 1 javadoc @param 后是否有注释
     * 2 src.main.java 目录中非 .java 扩展名文件内容要 // 注释起来
     * 3 删除 config.setDocsPath 目录中的文件，再生成试试
     * 如果生成的 api 文档不是预期的，作如下检查：
     * 1 必须在 configRoute(Routes me) 中已该方式 me.add("/xx/yy", xx.class, "/"); 定义 Controller
     * 2 在需要生成 api 的 Controller 中添加 @ApiDoc 注解
     * 3 如果要忽略某 action，在 action 上添加 @Ignore
     */
    public static void main(String[] args) {
        DocsConfig config = new DocsConfig();

        String projectPath = System.getProperty("user.dir");

        // root project path
        config.setProjectPath(projectPath + "/src");
        // project name
        config.setProjectName("瑞杰MOM平台");
        // api version
        config.setApiVersion("v1.0");
        // 这是项目下的目录，生成完之后。前端人员可以直接访问，查看接口文档进行接口对接
        // api docs target path
        config.setDocsPath(projectPath + "/target/apidocs");
        // config.addPlugin(new MarkdownDocPlugin())
        config.addPlugin(new PostmanDocPlugin());
        // auto generate
        config.setAutoGenerate(Boolean.FALSE);
        config.setMvcFramework("jfinal");
        // execute to generate
        Docs.buildHtmlDocs(config);
    }

}