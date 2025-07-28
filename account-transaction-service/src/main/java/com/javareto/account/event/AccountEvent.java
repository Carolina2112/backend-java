package com.javareto.account.event;

import java.time.LocalDateTime;

/**
 * Clase base para eventos de cuenta
 * Implementa el patrón Observer para notificaciones
 */
public abstract class AccountEvent {
    
    private final String accountNumber;
    private final LocalDateTime timestamp;
    private final String eventType;
    
    protected AccountEvent(String accountNumber, String eventType) {
        this.accountNumber = accountNumber;
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getEventType() {
        return eventType;
    }
    
    @Override
    public String toString() {
        return String.format("AccountEvent{accountNumber='%s', eventType='%s', timestamp=%s}", 
                           accountNumber, eventType, timestamp);
    }
} 