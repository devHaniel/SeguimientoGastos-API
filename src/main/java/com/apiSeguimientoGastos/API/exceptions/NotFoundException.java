package com.apiSeguimientoGastos.API.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String msje){
        super(msje);
    }
}
