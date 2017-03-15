/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jriacces2.state;

import java.util.Base64;
import jriacces2.Protocolo;
import jriacces2.log.ILog;
import jriacces2.strategy.IWriter;

public class WritingState extends AbstractState {

    public WritingState(Protocolo protocolo, ILog log) {
        super(protocolo, log);
        nameOfState = "Escrevendo";
        logInfoState();
    }

    @Override
    public void escrito() {
        protocolo.setState(new OpenState(protocolo, log));
        logDebug("escrito texto. {WritingState.java/23}");
    }

    void writeLine(IWriter writer) {
        String texto = "[" + writer.getTipo() + "]" + writer.getText();
        System.out.println(Base64.getEncoder().encodeToString(texto.getBytes()));
        logInfo("escreveu: " + texto);
        escrito();
    }

}
