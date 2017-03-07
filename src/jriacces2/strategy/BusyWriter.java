/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jriacces2.strategy;

public class BusyWriter implements IWriter {

    private final int numero;

    public BusyWriter(int numero) {
        this.numero = numero;
    }

    @Override
    public String getTipo() {
        return "BUSY";
    }

    @Override
    public String getText() {
        return "Busy (" + numero + ")";
    }

}
