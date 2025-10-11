package fourpetals.com.dto.response.users;

import lombok.Data;

@Data
public class UserStatsResponse {
	private long totalUsers;
    private long activeUsers;
    private long inactiveUsers;
    private long blockedUsers;

    public UserStatsResponse(long totalUsers, long activeUsers, long inactiveUsers, long blockedUsers) {
        this.totalUsers = totalUsers;
        this.activeUsers = activeUsers;
        this.inactiveUsers = inactiveUsers;
        this.blockedUsers = blockedUsers;
    }

	public long getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(long totalUsers) {
		this.totalUsers = totalUsers;
	}

	public long getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(long activeUsers) {
		this.activeUsers = activeUsers;
	}

	public long getInactiveUsers() {
		return inactiveUsers;
	}

	public void setInactiveUsers(long inactiveUsers) {
		this.inactiveUsers = inactiveUsers;
	}

	public long getBlockedUsers() {
		return blockedUsers;
	}

	public void setBlockedUsers(long blockedUsers) {
		this.blockedUsers = blockedUsers;
	}
}
