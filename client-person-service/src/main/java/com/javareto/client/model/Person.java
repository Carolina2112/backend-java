package com.javareto.client.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)

public class Person {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false, length = 30)
    private String name;
    
    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(Male|Female)$", message = "Gender must be Male or Female")
    @Column(name = "gender", nullable = false)
    private String gender;
    
    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 95, message = "Age must be less than 95")
    @Column(name = "age", nullable = false)
    private Integer age;
    
    @NotBlank(message = "Identification is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Identification must be 10 digits")
    @Column(name = "identification", nullable = false, unique = true, length = 10)
    private String identification;
    
    @NotBlank(message = "Address is required")
    @Column(name = "address", length = 100)
    private String address;
    
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    @Column(name = "phone", length = 10)
    private String phone;
    
    public Person() {}
    
    public Person (String name, String gender, Integer age, String identification, String adress, String phone) {
    	
    	this.name = name;
    	this.gender = gender;
    	this.age = age; 
    	this.identification = identification;
    	this.address = adress;
    	this.phone = phone;
    	
    }
    
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
}
