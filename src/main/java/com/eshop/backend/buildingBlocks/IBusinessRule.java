package com.eshop.backend.buildingBlocks;

//Interfaz que representa lo que es una regla de negocio. 
//Posee el mensaje que indica por qué la regla de negocio fue rota
//Posee un Error que representa el error que se devolverá
//Posee un método "isBroken" para evaluar si la regla de negocio ha sido rota o no. Si ha sido rota, retorna true, sino, false.
public interface IBusinessRule {
    String getMessage();
    Error getError();
    boolean isBroken();
}

