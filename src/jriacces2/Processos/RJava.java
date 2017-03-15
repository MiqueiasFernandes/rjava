/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jriacces2.Processos;

import jriacces2.JRIacces2;
import jriacces2.log.Errors;
import jriacces2.log.ILog;
import jriacces2.log.LogType;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author mfernandes
 */
public class RJava {

    public boolean inUse = false;
    private final TextConsole console;
    private final ILog log;
    private final int timeout_iteration = 100;
    private Rengine re;
    private String pid = null;

    public RJava(TextConsole console, ILog log) {
        this.console = console;
        this.log = log;
    }

    public void init(String[] args) {
        // just making sure we have the right version of everything
        if (!Rengine.versionCheck()) {
            logError("** Version mismatch - Java files don't match library version.");
            System.exit(Errors.R_VERSIOIN_ERROR.ordinal());
        }
        logInfo("Creating Rengine (with arguments)");
        // 1) we pass the arguments from the command line
        // 2) we won't use the main loop at first, we'll start it later
        //    (that's the "false" as second argument)
        // 3) the callbacks are implemented by the TextConsole class above
        re = new Rengine(args, false, console);
        logInfo("Rengine created, waiting for R");
        // the engine creates R is a new thread, so we should wait until it's ready
        if (!re.waitForR()) {
            logError("Cannot load R");
            return;
        }

        System.out.println("pid:" + JRIacces2.getPID() + ":"
                + (pid = String.valueOf(re.eval("pid = Sys.getpid()").asInt())));

        if (true) {
            logInfo("Now the console is yours ... have fun");
            re.startMainLoop();
            log.printLog(LogType.LOG_DEBUG, "inicializando timeout user interation.tempo limite: "
                    + timeout_iteration + " {RJava.java/51}");
            int i = 0;
            while (i++ < timeout_iteration) {
                try {
                    Thread.sleep(1000);
                    logWarning("waiting user iteration. (" + i + ")");
                } catch (InterruptedException ex) {
                    logError("thread para timeout: " + ex);
                    System.exit(Errors.THREAD_TIMEOUT.ordinal());
                }
            }
            if (!inUse) {
                log.printLog(LogType.LOG_DEBUG, "encerrando rengine, não esta em uso. {RJava.java/64}");
                re.end();
                logError("timeout para interação do usuário.");
                System.exit(Errors.TIMEOUT_USER_ITERATION.ordinal());
            }
        } else {
            logError("erro desconhecido.");
            log.printLog(LogType.LOG_DEBUG, "encerrando rengine. {RJava.java/71}");
            re.end();
            System.exit(Errors.FAIL_GENERAL.ordinal());
        }
    }

    public void logInfo(String texto) {
        log(LogType.LOG_INFO, texto);
    }

    public void logWarning(String texto) {
        log(LogType.LOG_WARNING, texto);
    }

    public void logError(String texto) {
        log(LogType.LOG_ERROR, texto);
    }

    public void log(LogType tipo, String texto) {
        log.printLog(LogType.LOG_DEBUG, "imprimindo log. {RJava.java/90}");
        log.printLog(tipo, "[ RJAVA ] " + texto);
    }

    public Rengine getRengine() {
        return re;
    }

    public String getPid() {
        return pid;
    }

}
