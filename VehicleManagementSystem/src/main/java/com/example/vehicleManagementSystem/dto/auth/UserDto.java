package com.example.vehicleManagementSystem.dto.auth;

import lombok.Builder;
import lombok.Data;
import org.apache.tomcat.util.codec.binary.Base64;

@Data
@Builder
public class UserDto {

    private Long id;
    private String userName;
    private String name;
    private String phoneNumber;
    private String sessionToken;
    private String rememberMeToken;
    private String role;
    private byte[] image;

    public String generateBase64Image() {
        return Base64.encodeBase64String(this.image);
    }

	public String getRole() {
		// TODO Auto-generated method stub
		return null;
	}

}
