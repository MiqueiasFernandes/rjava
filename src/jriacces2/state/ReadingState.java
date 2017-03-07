/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jriacces2.state;

import java.util.Scanner;
import jriacces2.Protocolo;
import jriacces2.log.ILog;
import jriacces2.strategy.EchoWriter;

public class ReadingState extends AbstractState {

    public ReadingState(Protocolo protocolo, ILog log) {
        super(protocolo, log);
        nameOfState = "Lendo";
        logInfoState();
    }

    @Override
    public void echo(String echo) {
        WritingState state = new WritingState(protocolo, log);
        protocolo.setState(state);
        state.writeLine(new EchoWriter(echo));
        logDebug("echo enviado. {ReadingState.java/26}");
    }

    public String readLine() {
        String line = new Scanner(System.in).nextLine();
        logInfo("leu: " + line);
        echo(line);
        return line;
    }

}
