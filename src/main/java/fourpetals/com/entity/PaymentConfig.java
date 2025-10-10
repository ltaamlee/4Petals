package fourpetals.com.entity;


public class PaymentConfig {
    private boolean codEnabled;
    private String codDescription;
    private String shopName;

    private boolean bankTransferEnabled;
    private String bankAccountName;
    private String bankAccountNumber;
    private String bankName;
    private String bankBranch;
    private String bankTransferContent;

    private boolean momoEnabled;
    private String momoPartnerCode;
    private String momoAccessKey;
    private String momoSecretKey;
    private String momoEndpoint;

    private boolean vnpayEnabled;
    private String vnpayMerchantId;
    private String vnpayHashSecret;
    private String vnpayApiUrl;
    private String vnpayReturnUrl;

    private boolean zalopayEnabled;
    private String zalopayAppId;
    private String zalopayKey1;
    private String zalopayKey2;
    private String zalopayEndpoint;

    private boolean paypalEnabled;
    private String paypalClientId;
    private String paypalSecretKey;
    private String paypalMode;
	public boolean isCodEnabled() {
		return codEnabled;
	}
	public void setCodEnabled(boolean codEnabled) {
		this.codEnabled = codEnabled;
	}
	public String getCodDescription() {
		return codDescription;
	}
	public void setCodDescription(String codDescription) {
		this.codDescription = codDescription;
	}
	public boolean isBankTransferEnabled() {
		return bankTransferEnabled;
	}
	public void setBankTransferEnabled(boolean bankTransferEnabled) {
		this.bankTransferEnabled = bankTransferEnabled;
	}
	public String getBankAccountName() {
		return bankAccountName;
	}
	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankBranch() {
		return bankBranch;
	}
	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}
	public String getBankTransferContent() {
		return bankTransferContent;
	}
	public void setBankTransferContent(String bankTransferContent) {
		this.bankTransferContent = bankTransferContent;
	}
	public boolean isMomoEnabled() {
		return momoEnabled;
	}
	public void setMomoEnabled(boolean momoEnabled) {
		this.momoEnabled = momoEnabled;
	}
	public String getMomoPartnerCode() {
		return momoPartnerCode;
	}
	public void setMomoPartnerCode(String momoPartnerCode) {
		this.momoPartnerCode = momoPartnerCode;
	}
	public String getMomoAccessKey() {
		return momoAccessKey;
	}
	public void setMomoAccessKey(String momoAccessKey) {
		this.momoAccessKey = momoAccessKey;
	}
	public String getMomoSecretKey() {
		return momoSecretKey;
	}
	public void setMomoSecretKey(String momoSecretKey) {
		this.momoSecretKey = momoSecretKey;
	}
	public String getMomoEndpoint() {
		return momoEndpoint;
	}
	public void setMomoEndpoint(String momoEndpoint) {
		this.momoEndpoint = momoEndpoint;
	}
	public boolean isVnpayEnabled() {
		return vnpayEnabled;
	}
	public void setVnpayEnabled(boolean vnpayEnabled) {
		this.vnpayEnabled = vnpayEnabled;
	}
	public String getVnpayMerchantId() {
		return vnpayMerchantId;
	}
	public void setVnpayMerchantId(String vnpayMerchantId) {
		this.vnpayMerchantId = vnpayMerchantId;
	}
	public String getVnpayHashSecret() {
		return vnpayHashSecret;
	}
	public void setVnpayHashSecret(String vnpayHashSecret) {
		this.vnpayHashSecret = vnpayHashSecret;
	}
	public String getVnpayApiUrl() {
		return vnpayApiUrl;
	}
	public void setVnpayApiUrl(String vnpayApiUrl) {
		this.vnpayApiUrl = vnpayApiUrl;
	}
	public String getVnpayReturnUrl() {
		return vnpayReturnUrl;
	}
	public void setVnpayReturnUrl(String vnpayReturnUrl) {
		this.vnpayReturnUrl = vnpayReturnUrl;
	}
	public boolean isZalopayEnabled() {
		return zalopayEnabled;
	}
	public void setZalopayEnabled(boolean zalopayEnabled) {
		this.zalopayEnabled = zalopayEnabled;
	}
	public String getZalopayAppId() {
		return zalopayAppId;
	}
	public void setZalopayAppId(String zalopayAppId) {
		this.zalopayAppId = zalopayAppId;
	}
	public String getZalopayKey1() {
		return zalopayKey1;
	}
	public void setZalopayKey1(String zalopayKey1) {
		this.zalopayKey1 = zalopayKey1;
	}
	public String getZalopayKey2() {
		return zalopayKey2;
	}
	public void setZalopayKey2(String zalopayKey2) {
		this.zalopayKey2 = zalopayKey2;
	}
	public String getZalopayEndpoint() {
		return zalopayEndpoint;
	}
	public void setZalopayEndpoint(String zalopayEndpoint) {
		this.zalopayEndpoint = zalopayEndpoint;
	}
	public boolean isPaypalEnabled() {
		return paypalEnabled;
	}
	public void setPaypalEnabled(boolean paypalEnabled) {
		this.paypalEnabled = paypalEnabled;
	}
	public String getPaypalClientId() {
		return paypalClientId;
	}
	public void setPaypalClientId(String paypalClientId) {
		this.paypalClientId = paypalClientId;
	}
	public String getPaypalSecretKey() {
		return paypalSecretKey;
	}
	public void setPaypalSecretKey(String paypalSecretKey) {
		this.paypalSecretKey = paypalSecretKey;
	}
	public String getPaypalMode() {
		return paypalMode;
	}
	public void setPaypalMode(String paypalMode) {
		this.paypalMode = paypalMode;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

    
}