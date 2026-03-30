package com.onlinepharmacy.identity.dto.response;


public class UserResponseDto {
    private Long id;
    private String email;
    private String name;
	
    public UserResponseDto() {
		super();
	}

	public UserResponseDto(Long id, String email, String name) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
    
}
