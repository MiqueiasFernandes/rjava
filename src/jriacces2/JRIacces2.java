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

        //test proposits
        if (args == null) {
            args = new String[]{"LIVE", "", "INFO", "--vanilla"};
        }

        if (args.length < 1 || args[0] == null || args[0].isEmpty()) {
            System.out.println("USAGE: [LIVE/BATCH]");
            System.exit(Errors.ARGS_INVALIDO.ordinal());
        }

        String path = System.getenv("R_HOME");

        if (path == null || path.isEmpty() || path.length() < 2) {

            String s = "R_HOME=/usr/lib64/R\n"
                    + "export R_HOME\n"
                    + "R_SHARE_DIR=/usr/share/R\n"
                    + "export R_SHARE_DIR\n"
                    + "R_INCLUDE_DIR=/usr/include/R\n"
                    + "export R_INCLUDE_DIR\n"
                    + "R_DOC_DIR=/usr/share/doc/R\n"
                    + "export R_DOC_DIR\n"
                    + "LD_LIBRARY_PATH=/home/mfernandes/R/x86_64-redhat-linux-gnu-library/3.3/rJava/jri\n"
                    + "export LD_LIBRARY_PATH";
            System.err.println("Não é possivel iniciar devido às variaveis de ambiente do JRI.\n,"
                    + "Para solucionar, altere e adicione em ~/.bashrc:\n" + s);
            System.exit(Errors.SYSTEM_ENVIRONMENT.ordinal());
        }

        ILog log = null;

        try {
            log = new TXTLog(args[1], LogType.getLogType(args[2]));
            log.printLog(LogType.LOG_DEBUG, "Arquivo Log inicializado com sucesso. {JRIaccess2.java/62}");
            log.printLog(LogType.LOG_INFO, "[ ARGS ]" + Arrays.toString(args));
        } catch (IOException ex) {
            System.err.println("erro ao inicializar arquivo de log: " + ex);
            System.exit(Errors.LOG_INICIALIZE.ordinal());
        }

        String modo = args[0];

        if (null == modo) {
            System.out.println("USAGE: [LIVE/BATCH]");
            System.exit(Errors.ARGS_INVALIDO.ordinal());
        } else {
            switch (modo) {
                case "LIVE":
                    log.printLog(LogType.LOG_DEBUG, "Inicializando protocolo. {JRIaccess2.java/77}");
                    Protocolo protocolo = new Protocolo(Arrays.copyOfRange(args, 3, args.length), log);
                    log.printLog(LogType.LOG_DEBUG, "Protocolo inicializado com sucesso. {JRIaccess2.java/79}");
                    break;
                case "BATCH":
                    log.printLog(LogType.LOG_DEBUG, "inicializando em modo BATCH. {JRIaccess2.java/60}");
                    break;
                default:
                    log.printLog(LogType.LOG_DEBUG, "Arquivo Log inicializado com sucesso. {JRIaccess2.java/85}");
                    System.out.println("USAGE: [LIVE/BATCH]");
                    System.exit(Errors.ARGS_INVALIDO.ordinal());
            }
        }
    }

}
