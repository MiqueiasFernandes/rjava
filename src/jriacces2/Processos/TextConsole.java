/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jriacces2.Processos;

import jriacces2.Protocolo;
import jriacces2.log.LogType;
import jriacces2.strategy.BusyWriter;
import jriacces2.strategy.ConsoleWriter;
import jriacces2.strategy.FlushWriter;
import jriacces2.strategy.MessageWriter;
import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;

///vide http://rforge.net/org/doc/org/rosuda/JRI/RMainLoopCallbacks.html for details
/**
 *
 * @author mfernandes
 */
public class TextConsole implements RMainLoopCallbacks {

    private final Protocolo protocolo;

    public TextConsole(Protocolo protocolo) {
        this.protocolo = protocolo;
    }

    @Override
    public void rWriteConsole(Rengine re, String text, int oType) {
        protocolo.getLog().printLog(LogType.LOG_DEBUG, "escrevendo "
                + (oType == 0 ? "regular" : "error/warning")
                + ". {TectConsole.java/30}");
        protocolo.escrever(new ConsoleWriter(oType, text));
    }

    @Override
    public void rBusy(Rengine re, int which) {
        protocolo.getLog().printLog(LogType.LOG_DEBUG, "console esta "
                + (which == 0 ? "trabalhando" : "pronto")
                + ". {TextConsole.java/36}");
        protocolo.escrever(new BusyWriter(which));
    }

    @Override
    public String rReadConsole(Rengine re, String prompt, int addToHistory) {
        protocolo.getLog().printLog(LogType.LOG_DEBUG, "lendo. {TextConsole.java/42}");
        return protocolo.ler(prompt) + System.lineSeparator();
    }

    @Override
    public void rShowMessage(Rengine re, String message) {
        protocolo.getLog().printLog(LogType.LOG_DEBUG, "informando mensagem. {TextConsole.java/48}");
        protocolo.escrever(new MessageWriter(message));
    }

    @Override
    public String rChooseFile(Rengine re, int newFile) {
        protocolo.getLog().printLog(LogType.LOG_DEBUG, "escolhendo arquivo. {TextConsole.java/54}");
        return protocolo.selecionar((newFile == 0) ? "Select a file" : "Select a new file");
    }

    @Override
    public void rFlushConsole(Rengine rngn) {
        protocolo.getLog().printLog(LogType.LOG_DEBUG, "limpando console. {TextConsole.java/60}");
        protocolo.escrever(new FlushWriter());
    }

    @Override
    public void rSaveHistory(Rengine rngn, String string) {
        protocolo.getLog().printLog(LogType.LOG_DEBUG, "salvando history. {TextConsole.java/66}");
        protocolo.escrever(new MessageWriter("Salvar arquivo history: " + string));
    }

    @Override
    public void rLoadHistory(Rengine rngn, String string) {
        protocolo.getLog().printLog(LogType.LOG_DEBUG, "carregando history. {TextConsole.java/72}");
        protocolo.escrever(new MessageWriter("Abrir arquivo history: " + string));
    }

}
