package cn.rjtech.cache;

import cn.jbolt._admin.dictionary.DictionaryService;
import cn.jbolt.core.cache.JBoltCache;
import cn.jbolt.core.model.Dictionary;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.model.momdata.Vendor;
import com.jfinal.aop.Aop;

import java.util.Optional;

public class PurchasedCache extends JBoltCache {
    public static final PurchasedCache me = new PurchasedCache();
    private final String TYPE_NAME = "purchased";
    private final DictionaryService dictionaryService = Aop.get(DictionaryService.class);
    private final VendorService vendorRecordService = Aop.get(VendorService.class);

    @Override
    public String getCacheTypeName() {
        return TYPE_NAME;
    }

    /**
     * 获取币种名称
     */
    public String getCurrencyName(String ccurrency) {
        return Optional.ofNullable(dictionaryService.getCacheByKey(ccurrency, "currency")).map(Dictionary::getName).orElse("");
    }

    /**
     * 获取供应商名称
     * @return
     */
    public String getCvenname(String cvencode) {
    	Vendor vendor = vendorRecordService.findByCode(cvencode);
    	if(vendor != null) return vendor.getCVenName();
    	return null;
    }
}
