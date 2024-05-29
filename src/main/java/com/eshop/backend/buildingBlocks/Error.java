package com.eshop.backend.buildingBlocks;

public class Error {
    
    private final String code;
    private final String description;
    
    private Error(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static Error NotFound(String code, String description){
        return new Error(code, description);
    } 
    
    public static Error Validation(String code, String description){
        return new Error(code, description);
    } 
}

