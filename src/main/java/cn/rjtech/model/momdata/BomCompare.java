package cn.rjtech.model.momdata;

import cn.jbolt.core.annotation.TableBind;
import cn.jbolt.core.base.JBoltIDGenMode;
import cn.rjtech.model.momdata.base.BaseBomCompare;

/**
 * 物料建模-Bom清单
 * Generated by JBolt.
 */
@SuppressWarnings("serial")
@TableBind(dataSource = "momdata" , table = "Bd_BomCompare" , primaryKey = "iAutoId" , idGenMode = JBoltIDGenMode.SNOWFLAKE)
public class BomCompare extends BaseBomCompare<BomCompare> {
   
    /**编码栏 1-6*/
    public static final String CODE1 = "code1";
    public static final String CODE2 = "code2";
    public static final String CODE3 = "code3";
    public static final String CODE4 = "code4";
    public static final String CODE5 = "code5";
    public static final String CODE6 = "code6";
    
    /**
     * 部品 iRawType=0
     */
    public static final String INVITEMID = "invItemId";
    
    public static final String INVBOMCOMPAREID = "invBomCompareId";
  
    public static final String INVQTY = "invQty";
    
    public static final String INVWEIGHT = "invWeight";
    
    public static final String VENDORNAME = "vendorName";
    
    
    /**
     * 片料 iRawType=1
     */
    
    public static final String BLANKINGITEMID = "blankingItemId";
    
    public static final String BLANKINGBOMCOMPAREID = "blankingBomCompareId";
    
    public static final String BLANKINGQTY = "blankingQty";
    
    public static final String BLANKINGWEIGHT = "blankingWeight";
    
    public static final String BLANKINGVENDORNAME = "blankingVendorName";
    
    public static final String BLANKINGSTD = "blankingStd";
    
    public static final String BLANKINGITEMCODE = "blankingItemCode";
    
    public static final String BLANKINGINVNAME = "blankingInvName";
    /**
     * 分条料 iRawType=2
     */
    public static final String SLICINGINVITEMID = "slicingInvItemId";
    
    public static final String SLICINGBOMCOMPAREID = "slicingBomCompareId";
    
    public static final String SLICINGQTY = "slicingQty";
    
    public static final String SLICINGWEIGHT = "slicingWeight";
    
    public static final String SLICINGVENDORNAME = "slicingVendorName";
    
    public static final String SLICINGSTD = "slicingStd";
    
    public static final String SLICINGINVITEMCODE = "slicingItemCode";
    
    public static final String SLICINGINVNAME = "slicingInvName";
    
    /**
     * 卷料 iRawType=3
     */
    
    public static final String ORIGINALITEMID = "originalItemId";
    
    public static final String ORIGINALBOMCOMPAREID = "originalBomCompareId";
    
    public static final String ORIGINALQTY = "originalQty";
    
    public static final String ORIGINALWEIGHT = "originalWeight";
    
    public static final String ORIGINALSTD = "originalStd";
    
    public static final String ORIGINALITEMCODE = "originalItemCode";
    
    public static final String ORIGINALINVNAME = "originalInvName";
    /**
     * 供应商
     */
    public static final String ORIGINALVENDORNAME = "originalVendorName";
    
    /**
     * 厚度
     */
    public static final String THICKNESS = "thickness";
    /**
     * 材质
     */
    public static final String MATERIAL = "material";
    
    /**
     * 材质类型
     */
    public static final String MATERIALTYPE = "materialType";
    /**
     * 客户部番
     */
    public static final String CINVCODE1 = "cInvCode1";
    /**
     * UG部番
     */
    public static final String CINVADDCODE1 = "cInvAddCode1";
    /**
     * UG部品名称
     */
    public static final String CINVNAME2 = "cInvName2";
    /**
     * 部品名称
     */
    public static final String CINVNAME1 = "cInvName1";
    /**
     * 存货编码
     */
    public static final String INVITEMCODE = "invItemCode";
    
    /**
     * 部品名称
     */
    public static final String CINVNAME = "cInvName";
    
}

