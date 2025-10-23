package fourpetals.com.dto.response.stats;

public class SupplierStatsResponse {
	private long totalSuppliers;
	private long activeSuppliers;
	private long inactiveSuppliers;
	private long blockedSuppliers;
	public long getTotalSuppliers() {
		return totalSuppliers;
	}
	public void setTotalSuppliers(long totalSuppliers) {
		this.totalSuppliers = totalSuppliers;
	}
	public long getActiveSuppliers() {
		return activeSuppliers;
	}
	public void setActiveSuppliers(long activeSuppliers) {
		this.activeSuppliers = activeSuppliers;
	}
	public long getInactiveSuppliers() {
		return inactiveSuppliers;
	}
	public void setInactiveSuppliers(long inactiveSuppliers) {
		this.inactiveSuppliers = inactiveSuppliers;
	}
	public long getBlockedSuppliers() {
		return blockedSuppliers;
	}
	public void setBlockedSuppliers(long blockedSuppliers) {
		this.blockedSuppliers = blockedSuppliers;
	}
	public SupplierStatsResponse(long totalSuppliers, long activeSuppliers, long inactiveSuppliers,
			long blockedSuppliers) {
		super();
		this.totalSuppliers = totalSuppliers;
		this.activeSuppliers = activeSuppliers;
		this.inactiveSuppliers = inactiveSuppliers;
		this.blockedSuppliers = blockedSuppliers;
	}
	public SupplierStatsResponse() {
		super();
	}
	
	
}
