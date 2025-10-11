package fourpetals.com.dto.request.users;

import lombok.Data;

@Data
public class UserSearchRequest {
	private String keyword;
	private String status;
	private Integer roleId;
	
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	private int page = 0;
	private int size = 10;
	
	
}