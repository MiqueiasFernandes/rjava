/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jriacces2.state;

import jriacces2.Protocolo;
import jriacces2.log.ILog;
import jriacces2.log.LogType;
import jriacces2.strategy.IWriter;

/**
 *
 * @author mfernandes
 */
public abstract class AbstractState {

    protected String nameOfState;
    protected ILog log;
    protected Protocolo protocolo;

    public AbstractState(Protocolo protocolo, ILog log) {
        this.protocolo = protocolo;
        this.log = log;
    }

    public void logInfoState() {
        logInfo("o JRI esta " + nameOfState + "...");
    }

    public String ler(String prompt) {
        logWarning("tentativa de leitura em estado " + nameOfState);
        return null;
    }

    public void echo(String echo) {
        logWarning("tentativa de echo em estado " + nameOfState);
    }

    public void escrever(IWriter writer) {
        logWarning("tentativa de escrever em estado " + nameOfState);

    }

    public void escrito() {
        logWarning("tentativa de finalizar escrita em estado " + nameOfState);
    }

    public void selecionar() {
        logWarning("tentativa de selecionar arquivo em estado " + nameOfState);
    }

    public void selecionado() {
        logWarning("tentativa de terminar seleção de arquivo em estado " + nameOfState);
    }

    public void fechar() {
        logWarning("tentativa de fechar conexão em estado " + nameOfState);
    }

    public void logInfo(String texto) {
        log(this, LogType.LOG_INFO, texto);
    }

    public void logWarning(String texto) {
        log(this, LogType.LOG_WARNING, texto);
    }

    public void logError(String texto) {
        log(this, LogType.LOG_ERROR, texto);
    }

    public void logDebug(String texto) {
        log(this, LogType.LOG_DEBUG, texto);
    }

    public void log(AbstractState estado, LogType tipo, String texto) {
        log.printLog(tipo, "[ " + estado.nameOfState + " ] " + texto);
    }

}
