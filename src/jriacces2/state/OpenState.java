/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jriacces2.state;

import jriacces2.Protocolo;
import jriacces2.log.ILog;
import jriacces2.strategy.IWriter;
import jriacces2.strategy.PromptWriter;

public class OpenState extends AbstractState {

    public OpenState(Protocolo protocolo, ILog log) {
        super(protocolo, log);
        nameOfState = "Aberto";
        logInfoState();
    }

    @Override
    public String ler(String prompt) {
        logDebug("escrevendo prompt. {OpenState.java/23}");
        escrever(new PromptWriter(prompt));
        logDebug("criando estado de leitura. {OpenState.java/25}");
        ReadingState state = new ReadingState(protocolo, log);
        protocolo.setState(state);
        logDebug("novo estado atribuido. {OpenState.java/28}");
        String line = state.readLine();
        logDebug("linha lida. {OpenState.java/30}");
        return line;
    }

    @Override
    public void escrever(IWriter writer) {
        logDebug("inicializando para escrever. {OpenState.java/36}");
        WritingState state = new WritingState(protocolo, log);
        logDebug("alterando estado. {OpenState.java/38}");
        protocolo.setState(state);
        logDebug("escrevendo. {OpenState.java/40}");
        state.writeLine(writer);
    }

    @Override
    public void selecionar() {
        logDebug("selecionando arquivo. {OpenState.java/46}");
        SelectingState state = new SelectingState(protocolo, log);
        logDebug("alterando estado. {OpenState.java/48}");
        protocolo.setState(state);
        logDebug("estado alterado. {OpenState.java/50}");
    }

    @Override
    public void fechar() {
        logDebug("encerrando. {OpenState.java/55}");
        protocolo.setState(new ClosedState(protocolo, log));
    }

}
