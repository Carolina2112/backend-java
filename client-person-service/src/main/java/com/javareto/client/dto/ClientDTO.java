package com.javareto.client.dto;

import jakarta.validation.constraints.*;

public class ClientDTO {
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Gender is required")
    private String gender;
    
    @NotNull(message = "Age is required")
    @Min(18) @Max(100)
    private Integer age;
    
    @NotBlank(message = "Identification is required")
    @Size(min = 10, max = 10)
    private String identification;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10}$")
    private String phone;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    private Boolean status;
    
    
    public ClientDTO() {}
    
    
    public Long getId() { 
    	return id; 
    }
    
    public void setId(Long id) { 
    	this.id = id; 
    }
    
    public String getName() { 
    	return name; 
    }
    
    public void setName(String name) { 
    	this.name = name; 
    }
    
    public String getGender() { 
    	return gender; 
    }
    
    public void setGender(String gender) { 
    	this.gender = gender; 
    }
    
    public Integer getAge() { 
    	return age; 
    }
    
    public void setAge(Integer age) { 
    	this.age = age; 
    }
    
    public String getIdentification() { 
    	return identification; 
    }
    
    public void setIdentification(String identification) { 
    	this.identification = identification; 
    }
    
    public String getAddress() { 
    	return address; 
    }
    
    public void setAddress(String address) { 
    	this.address = address; 
    }
    
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
}