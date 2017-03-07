/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jriacces2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import jriacces2.log.Errors;
import jriacces2.log.ILog;
import jriacces2.log.LogType;
import jriacces2.log.TXTLog;

/**
 *
 * @author mfernandes
 */
public class JRIacces2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        args = new String[]{"LIVE", "", "INFO", "--vanilla"};

        if (args.length < 1 || args[0] == null || args[0].isEmpty()) {
            System.out.println("USAGE: [LIVE/BATCH]");
            System.exit(Errors.ARGS_INVALIDO.ordinal());
        }

        ILog log = null;
        try {
            log = new TXTLog(args[1], getLogType(args[2]));
            log.printLog(LogType.LOG_DEBUG, "Arquivo Log inicializado com sucesso. {JRIaccess2.java/40}");
            log.printLog(LogType.LOG_INFO, "[ ARGS ]" + Arrays.toString(args));
        } catch (IOException ex) {
            System.err.println("erro ao inicializar arquivo de log: " + ex);
            System.exit(Errors.LOG_INICIALIZE.ordinal());
        }

        String modo = args[0];
        if (true || "LIVE".equals(modo)) {
            log.printLog(LogType.LOG_DEBUG, "Inicializando protocolo. {JRIaccess2.java/48}");
            Protocolo protocolo = new Protocolo(Arrays.copyOfRange(args, 3, args.length), log);
            log.printLog(LogType.LOG_DEBUG, "Protocolo inicializado com sucesso. {JRIaccess2.java/50}");
        } else if ("BATCH".equals(modo)) {
            log.printLog(LogType.LOG_DEBUG, ". {JRIaccess2.java/52}");
        } else {
            log.printLog(LogType.LOG_DEBUG, "Arquivo Log inicializado com sucesso. {JRIaccess2.java/54}");
            System.out.println("USAGE: [LIVE/BATCH]");
            System.exit(Errors.ARGS_INVALIDO.ordinal());
        }
    }

    public static LogType getLogType(String tipo) {
        if ("DEBUG".equals(tipo)) {
            return LogType.LOG_DEBUG;
        }
        if ("INFO".equals(tipo)) {
            return LogType.LOG_INFO;
        }
        if ("WARNING".equals(tipo)) {
            return LogType.LOG_WARNING;
        }
        if ("ERROR".equals(tipo)) {
            return LogType.LOG_ERROR;
        }
        return null;
    }

}
