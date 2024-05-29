package com.eshop.backend.buildingBlocks;

public interface IBusinessRule {
    String getMessage();
    Error getError();
    boolean isBroken();
}

