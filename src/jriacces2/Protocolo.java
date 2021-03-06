/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jriacces2;

import jriacces2.Processos.RJava;
import jriacces2.Processos.TextConsole;
import java.util.Base64;
import jriacces2.log.ILog;
import jriacces2.log.LogType;
import jriacces2.state.AbstractState;
import jriacces2.state.OpenState;
import jriacces2.strategy.IWriter;

/**
 *
 * @author mfernandes
 */
public class Protocolo {

    private AbstractState estado;
    private RJava rJava;
    private final TextConsole console;
    private final ILog log;
    private int lineNull = 0;

    public Protocolo(String[] args, ILog log) {
        this.log = log;
        log.printLog(LogType.LOG_DEBUG, "inicializando text console. {Protocolo.java/27}");
        console = new TextConsole(this);
        log.printLog(LogType.LOG_DEBUG, "inicializando rjava. {Protocolo.java/29}");
        rJava = new RJava(console, log);
        log.printLog(LogType.LOG_DEBUG, "inicializando estado aberto. {Protocolo.java/31}");
        estado = new OpenState(this, log);
        rJava.init(args);
    }

    public void setState(AbstractState state) {
        log.printLog(LogType.LOG_DEBUG, "novo estado atribuido. {Protocolo.java/37}");
        this.estado = state;
    }

    public String ler(String prompt) {
        log.printLog(LogType.LOG_DEBUG, "lendo texto. {Protocolo.java/42}");
        String ret = estado.ler(prompt);
        rJava.inUse = true;

        if ((ret == null || ret.isEmpty()) && (lineNull++ > 1000)) {
            log.printLog(LogType.LOG_ERROR, "limite de linhas nulas excedido: " + lineNull);
            fechar();
        }

        lineNull = 0;

        try {
            ret = new String(Base64.getDecoder().decode(ret));
        } catch (Exception ex) {
            getLog().printLog(LogType.LOG_ERROR, "indecodificavel: " + ret + " error: " + ex);
        }

        return ret;
    }

    public void escrever(IWriter writer) {
        log.printLog(LogType.LOG_DEBUG, "escrevendo texto. {Protocolo.java/49}");
        estado.escrever(writer);
    }

    public String selecionar(String message) {
        log.printLog(LogType.LOG_DEBUG, "selecionando arquivo. {Protocolo.java/54}");
        return null;
    }

    public void fechar() {
        log.printLog(LogType.LOG_DEBUG, "encerrando protocolo. {Protocolo.java/59}");
        rJava.getRengine().end();
    }

    public ILog getLog() {
        return log;
    }

    public RJava getrJava() {
        return rJava;
    }

}
