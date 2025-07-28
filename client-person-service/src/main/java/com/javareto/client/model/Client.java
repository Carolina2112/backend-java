package com.javareto.client.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "client")
@PrimaryKeyJoinColumn(name = "client_id")

public class Client extends Person{
	
    @NotBlank(message = "Password is required")
    @Column(name = "password", nullable = false)
    private String password;
    
    @NotNull(message = "Status is required")
    @Column(name = "status", nullable = false)
    private Boolean status = true;
    
    @Transient
    private List<Long> accountIds;
    
    public Client() {
        super();
    }
    
    public Client(String name, String gender, Integer age, String identification, 
                  String address, String phone, String password, Boolean status) {
        super(name, gender, age, identification, address, phone);
        this.password = password;
        this.status = status;
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
    
    public List<Long> getAccountIds() {
        return accountIds;
    }
    
    public void setAccountIds(List<Long> accountIds) {
        this.accountIds = accountIds;
    }
    
	
}
