/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jriacces2.strategy;

/**
 *
 * @author mfernandes
 */
public abstract class AbstractWriter implements IWriter {

    protected String text;

    public AbstractWriter(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

}
