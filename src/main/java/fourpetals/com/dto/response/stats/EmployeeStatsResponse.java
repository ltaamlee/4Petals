package fourpetals.com.dto.response.stats;

import lombok.Data;

@Data
public class EmployeeStatsResponse {

	private long totalEmployees;
	private long activeEmployees;
	private long inactiveEmployees;
	private long blockedEmployees;

	public long getTotalEmployees() {
		return totalEmployees;
	}

	public void setTotalEmployees(long totalEmployees) {
		this.totalEmployees = totalEmployees;
	}

	public long getActiveEmployees() {
		return activeEmployees;
	}

	public void setActiveEmployees(long activeEmployees) {
		this.activeEmployees = activeEmployees;
	}

	public long getInactiveEmployees() {
		return inactiveEmployees;
	}

	public void setInactiveEmployees(long inactiveEmployees) {
		this.inactiveEmployees = inactiveEmployees;
	}

	public long getBlockedEmployees() {
		return blockedEmployees;
	}

	public void setBlockedEmployees(long blockedEmployees) {
		this.blockedEmployees = blockedEmployees;
	}

	public EmployeeStatsResponse() {
		super();
	}

	public EmployeeStatsResponse(long totalEmployees, long activeEmployees, long inactiveEmployees,
			long blockedEmployees) {
		super();
		this.totalEmployees = totalEmployees;
		this.activeEmployees = activeEmployees;
		this.inactiveEmployees = inactiveEmployees;
		this.blockedEmployees = blockedEmployees;
	}

}
