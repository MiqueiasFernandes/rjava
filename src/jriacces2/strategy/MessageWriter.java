/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jriacces2.strategy;

public class MessageWriter extends AbstractWriter {

    public MessageWriter(String text) {
        super(text);
    }

    @Override
    public String getTipo() {
        return "MESSAGE";
    }

}
