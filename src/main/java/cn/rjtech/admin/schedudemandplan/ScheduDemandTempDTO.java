package cn.rjtech.admin.schedudemandplan;

import java.io.Serializable;
import java.math.BigDecimal;

public class ScheduDemandTempDTO implements Serializable {

	//子件id
	private Long invId;
	//子件编码
	private String invCode;
	//子件客户部番
	private String cInvCode1;
	//子件部品名称
	private String cInvName1;
	//子件销售类型
	private Integer iSaleType;
	//自己层级
	private Integer iLevel;
	//子件供应商id
	private Long iVendorId;
	//子件供应商编码
	private String cVenCode;
	//子件供应商名称
	private String cVenName;
	//子件包装数量
	private Integer iPkgQty;
	//子件标准在库天数
	private Integer iInnerInStockDays;

	//母件id
	private Long pinvId;
	//母件编码
	private String pinvCode;
	//子件使用量
	private BigDecimal useRate;
	//顺序号
	private Integer sort;


	public Integer getiLevel() {
		return iLevel;
	}

	public void setiLevel(Integer iLevel) {
		this.iLevel = iLevel;
	}

	public Integer getiPkgQty() {
		return iPkgQty;
	}

	public void setiPkgQty(Integer iPkgQty) {
		this.iPkgQty = iPkgQty;
	}

	public Long getInvId() {
		return invId;
	}

	public void setInvId(Long invId) {
		this.invId = invId;
	}

	public String getInvCode() {
		return invCode;
	}

	public void setInvCode(String invCode) {
		this.invCode = invCode;
	}

	public String getcInvCode1() {
		return cInvCode1;
	}

	public void setcInvCode1(String cInvCode1) {
		this.cInvCode1 = cInvCode1;
	}

	public String getcInvName1() {
		return cInvName1;
	}

	public void setcInvName1(String cInvName1) {
		this.cInvName1 = cInvName1;
	}

	public Integer getiSaleType() {
		return iSaleType;
	}

	public void setiSaleType(Integer iSaleType) {
		this.iSaleType = iSaleType;
	}

	public Long getiVendorId() {
		return iVendorId;
	}

	public void setiVendorId(Long iVendorId) {
		this.iVendorId = iVendorId;
	}

	public String getcVenCode() {
		return cVenCode;
	}

	public void setcVenCode(String cVenCode) {
		this.cVenCode = cVenCode;
	}

	public String getcVenName() {
		return cVenName;
	}

	public void setcVenName(String cVenName) {
		this.cVenName = cVenName;
	}

	public Integer getiInnerInStockDays() {
		return iInnerInStockDays;
	}

	public void setiInnerInStockDays(Integer iInnerInStockDays) {
		this.iInnerInStockDays = iInnerInStockDays;
	}

	public Long getPinvId() {
		return pinvId;
	}

	public void setPinvId(Long pinvId) {
		this.pinvId = pinvId;
	}

	public String getPinvCode() {
		return pinvCode;
	}

	public void setPinvCode(String pinvCode) {
		this.pinvCode = pinvCode;
	}

	public BigDecimal getUseRate() {
		return useRate;
	}

	public void setUseRate(BigDecimal useRate) {
		this.useRate = useRate;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
}

