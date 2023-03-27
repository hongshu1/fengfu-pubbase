package cn.jbolt._admin.dictionary;

import java.util.Collections;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;

import cn.jbolt.core.cache.JBoltDictionaryTypeCache;
import cn.jbolt.core.model.Dictionary;
import cn.jbolt.core.model.DictionaryType;
import cn.jbolt.core.service.JBoltDictionaryService;
import cn.rjtech.util.ValidationUtils;
/**
 * 字典Service
 * @ClassName:  DictionaryService   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2019年11月9日   
 */
public class DictionaryService extends JBoltDictionaryService{
	/**
     * 通过typeKey 转换为类似枚举的Record
     */
    public Record convertEnumByTypeKey(String typeKey) {
        List<Dictionary> optionList =  getOptionListByTypeKey(typeKey);
        ValidationUtils.notNull(optionList, "没有该字典Key：" + typeKey);

        Record resultRecord = new Record();
        for (Dictionary dictionary : optionList) {
            resultRecord.set(dictionary.getName(),  dictionary.getSn());
        }
        return resultRecord;
    }
    /**
	 * 根据类型获得字典数据
	 * @param typeKey
	 * @return
	 */
	public List<Dictionary> getOptionListByTypeKey(String typeKey) {
		DictionaryType dictionaryType= JBoltDictionaryTypeCache.me.getByKey(typeKey);
		if(dictionaryType==null){return Collections.emptyList();}
		return getListByTypeId(dictionaryType.getId(),null);
	}
	
}