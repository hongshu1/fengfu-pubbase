package cn.rjtech.admin.barcodeencodingd;

import static cn.hutool.core.text.StrPool.COMMA;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.beust.jcommander.ParameterException;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrSplitter;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.BarCodeEnum;
import cn.rjtech.model.momdata.Barcodeencodingd;
import cn.rjtech.util.ValidationUtils;

/**
 * 销售报价单明细扩展自定义项管理 Service
 *
 * @ClassName: BarcodeencodingdService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2022-04-15 15:52
 */
public class BarcodeencodingdService extends BaseService<Barcodeencodingd> {

    private final Barcodeencodingd dao = new Barcodeencodingd().dao();

    @Override
    protected Barcodeencodingd dao() {
        return dao;
    }

    /**
     * 后台管理分页查询true
     */
    public List<Barcodeencodingd> findBybarcodeencodingdId(Kv paras) {
        paras.set("corgcode", getOrgCode());

        return daoTemplate("barcodeencodingd.findBybarcodeencodingdId", paras).find();
    }

    /**
     * 保存
     */
    public Ret save(Barcodeencodingd barcodeencodingd) {
        ValidationUtils.assertNull(barcodeencodingd.getIautoid(), JBoltMsg.PARAM_ERROR);

        tx(() -> {
            // ValidationUtils.isTrue(notExists(columnName, value), Msg.DATA_SAME_NAME_EXIST);
            ValidationUtils.isTrue(barcodeencodingd.save(), ErrorMsg.SAVE_FAILED);

            // TODO 其他业务代码实现

            return true;
        });

        // 添加日志
        // addSystemLog(barcodeencodingd.getIautoid(), JBoltUserKit.getUserId(), SystemLog.TYPE_SAVE, SystemLog.TARGETTYPE_xxx, barcodeencodingd.getName())
        return SUCCESS;
    }

    /**
     * 根据主表id查询细表数据
     */
    public List<Record> getList(Long ibarcodeencodingmid) {
        if (ibarcodeencodingmid == null) {
            return CollUtil.empty(ArrayList.class);
        }

        List<Record> list = dbTemplate("barcodeencodingd.findBybarcodeencodingdId", Kv.by("ibarcodeencodingmid", ibarcodeencodingmid).set("corgcode", getOrgCode())).find();
        for (Record record : list) {
            String cprojectcode = record.getStr("cprojectcode");
            BarCodeEnum barCodeEnum = BarCodeEnum.toEnum(cprojectcode);
            if (barCodeEnum != null) {
                record.set("text", barCodeEnum.getText());
            }
        }
        return list;
    }

    /**
     * 更新
     */
    public Ret update(Barcodeencodingd barcodeencodingd) {
        ValidationUtils.isTrue(isOk(barcodeencodingd.getIautoid()), JBoltMsg.PARAM_ERROR);

        tx(() -> {
            // 更新时需要判断数据存在
            Barcodeencodingd dbBarcodeencodingd = findById(barcodeencodingd.getIautoid());
            ValidationUtils.notNull(dbBarcodeencodingd, JBoltMsg.DATA_NOT_EXIST);

            // TODO 其他业务代码实现
            // ValidationUtils.isTrue(notExists(columnName, value), Msg.DATA_SAME_NAME_EXIST);

            ValidationUtils.isTrue(barcodeencodingd.update(), ErrorMsg.UPDATE_FAILED);

            return true;
        });

        // 添加日志
        // addSystemLog(barcodeencodingd.getIautoid(), JBoltUserKit.getUserId(), SystemLog.TYPE_UPDATE, SystemLog.TARGETTYPE_xxx, barcodeencodingd.getName())
        return SUCCESS;
    }

    /**
     * 删除 指定多个ID
     */
    public Ret deleteByBatchIds(String ids) {
        tx(() -> {
            for (String idStr : StrSplitter.split(ids, COMMA, true, true)) {
                long iautoid = Long.parseLong(idStr);
                Barcodeencodingd dbBarcodeencodingd = findById(iautoid);
                ValidationUtils.notNull(dbBarcodeencodingd, JBoltMsg.DATA_NOT_EXIST);

                // TODO 可能需要补充校验组织账套权限
                // TODO 存在关联使用时，校验是否仍在使用

                ValidationUtils.isTrue(dbBarcodeencodingd.delete(), JBoltMsg.FAIL);
            }

            return true;
        });

        // 添加日志
        // Barcodeencodingd barcodeencodingd = ret.getAs("data");
        // addDeleteSystemLog(iautoid, JBoltUserKit.getUserId(), SystemLog.TARGETTYPE_xxx, barcodeencodingd.getName());
        return SUCCESS;
    }

    /**
     * 执行JBoltTable表格整体提交
     */
    public Ret saveTableSubmit(JBoltTable jBoltTable) {
        if (jBoltTable == null || jBoltTable.isBlank()) {
            return fail(JBoltMsg.JBOLTTABLE_IS_BLANK);
        }

        //获取额外传递参数 比如订单ID等信息 在下面数据里可能用到
        if (jBoltTable.paramsIsNotBlank()) {
            System.out.println(jBoltTable.getParams().toJSONString());
        }

        //获取Form对应的数据
        if (jBoltTable.formIsNotBlank()) {
            //这里需要根据自己的需要 从Form这个JSON里去获取自己需要的数据
            //1、如果前端Form没有使用模型驱动方式例如user.xxx这样的name属性的话 直接使用class就能获取到model和javaBean
            //User user=jBoltTable.getFormModel(User.class);
            //Record record=jBoltTable.getFormRecord();
            //xxxBean bean=jBoltTable.getFormBean(xxxBean.class);
            //2、如果前端Form使用了模型驱动 就需要使用下面方式 指定modelName
            //User user=jBoltTable.getFormModel(User.class,"user");
            //xxxBean bean=jBoltTable.getFormBean(xxxBean.class,"xxx");
        }

        //获取待保存数据 执行保存
        if (jBoltTable.saveIsNotBlank()) {
            batchSave(jBoltTable.getSaveModelList(Barcodeencodingd.class));
        }
        //获取待更新数据 执行更新
        if (jBoltTable.updateIsNotBlank()) {
            jBoltTable.getUpdateModelList(Barcodeencodingd.class);
            batchUpdate(jBoltTable.getUpdateModelList(Barcodeencodingd.class));
        }
        //获取待删除数据 执行删除
        if (jBoltTable.deleteIsNotBlank()) {
            deleteByIds(jBoltTable.getDelete());
        }
        return SUCCESS;
    }

    @Override
    protected int systemLogTargetType() {
        return 0;
    }

    /**
     * 可编辑表格填入参数
     */
    public void finishJBoltTable(JBoltTable jBoltTable, Long mid) {
        String fixed = BarCodeEnum.FIXED.getValue();
        if (jBoltTable.deleteIsNotBlank()) {
            deleteByIds(jBoltTable.getDelete());
        }
        List<Record> saveModelList = jBoltTable.getSaveRecordList();
        Set<String> columnNames = TableMapping.me().getTable(Barcodeencodingd.class).getColumnNameSet();
        if (CollUtil.isNotEmpty(saveModelList)) {
            for (int i=0; i < saveModelList.size(); i++) {
            	Record row = saveModelList.get(i);
            	row.set("mid",mid);
            	row.set("iautoid", JBoltSnowflakeKit.me.nextId());
                if (i == 0) {
                    // 避免保存不到所有字段的问题
                    for (String columnName : columnNames) {
                        if (null == row.get(columnName)) {
                            row.set(columnName, null);
                        }
                    }
                }
                String cprojectcode = row.getStr("cprojectcode");
                String cprojectvalue = row.getStr("cprojectvalue");
                if (!cprojectcode.equals(fixed)) {
                    BarCodeEnum barCodeEnum = BarCodeEnum.toEnum(cprojectcode);
                    ValidationUtils.assertBlank(cprojectvalue, "固定值以外不能填写项目值:" + barCodeEnum.getText());
                }
                row.keep("iautoid","mid","cprojectcode","iseq","cprojectvalue","csuffix","cdateformat","ibillnolen");
            }
            try{
                batchSaveRecords(saveModelList);
            }catch (Exception e){
                String message = e.getMessage();
                if(message.contains("唯一索引")){
                    throw new ParameterException("细表保存失败,不能有相同的排序!");
                }else{
                    throw new ParameterException("细表保存失败!");
                }
            }

        }
        List<Record> updateModelList = jBoltTable.getUpdateRecordList();
        if (updateModelList != null) {
            for (int i=0;i< updateModelList.size();i++) {
            	Record row = updateModelList.get(i);
                if (i == 0) {
                    // 避免保存不到所有字段的问题
                    for (String columnName : columnNames) {
                        if (null == row.get(columnName)) {
                            row.set(columnName, null);
                        }
                    }
                }
                String cprojectcode = row.getStr("cprojectcode");
                String cprojectvalue = row.getStr("cprojectvalue");
                if (!cprojectcode.equals(fixed)) {
                    BarCodeEnum barCodeEnum = BarCodeEnum.toEnum(cprojectcode);
                    ValidationUtils.assertBlank(cprojectvalue, "固定值以外不能填写项目值:" + barCodeEnum.getText());
                }
                row.keep("iautoid","mid","cprojectcode","iseq","cprojectvalue","csuffix","cdateformat","ibillnolen");
            }
        }

        if (jBoltTable.updateIsNotBlank()) {
            try{
                batchUpdateRecords(updateModelList);
            }catch (Exception e){
                String message = e.getMessage();
                if(message.contains("唯一索引")){
                    throw new ParameterException("细表保存失败,不能有相同的排序!");
                }else{
                    throw new ParameterException("细表保存失败!");

                }
            }
        }

    }
  
    /**
     * 通过主表的id得到tag
     */
    public String getTag(Long mid) {
        StringBuilder str = new StringBuilder();
        List<Record> getlist = getList(mid);
        for (Record record : getlist) {
            str.append(record.getStr("text")).append("+");
        }
        if (str.length() > 0) {
            str = new StringBuilder(str.substring(0, str.length() - 1));
            return str.toString();
        } else {
            return "";
        }
    }

}