package fourpetals.com.dto.response.auth;

public class JwtResponse {
	private String token;
	
    //Getter & Setter
	public JwtResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}
