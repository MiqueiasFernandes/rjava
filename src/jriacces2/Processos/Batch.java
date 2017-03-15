/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jriacces2.Processos;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Scanner;
import jriacces2.JRIacces2;
import jriacces2.log.ILog;
import jriacces2.log.LogType;

/**
 *
 * @author mfernandes
 */
public class Batch {

    private static final String R_BATCH_CMD1 = "R";
    private static final String R_BATCH_CMD2 = "CMD";
    private static final String R_BATCH_CMD3 = "BATCH";
    private final String diretorio;
    private final String fileName;
    private final ILog log;
    private Process process;
    private ProcessBuilder processBuilder;
    private File output, error;
    private String pid = null;

    public Batch(ILog log, String fileName, String diretorio) {
        this.log = log;
        this.fileName = fileName;
        this.diretorio = diretorio;
        logInfo("nome do arquivo de script: " + fileName);
        logInfo("diretorio para o script: " + diretorio);
        try {
            output = File.createTempFile("status", null);
            error = File.createTempFile("status", null);
        } catch (IOException ex) {
            logError("impossivel criar arquivos temporarios de output e error: " + ex);
        }
    }

    public boolean startProcess() {
        try {
            processBuilder = new ProcessBuilder(R_BATCH_CMD1, R_BATCH_CMD2, R_BATCH_CMD3, fileName)
                    .directory(new File(diretorio))
                    .redirectOutput(output)
                    .redirectError(error);
            process = processBuilder.start();
            logInfo("processo iniciado com o comando: "
                    + Arrays.toString(new String[]{R_BATCH_CMD1, R_BATCH_CMD2, R_BATCH_CMD3, fileName}));

            new Thread(() -> {
                while (pid == null && "{\"error\":\"pid nao encontrado.\"}".equals(getStatus())) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        logError("impossivel aguardar por thread para obter PID. ex: " + ex);
                    }
                }
                System.out.println("pid:" + JRIacces2.getPID() + ":" + pid);
            }).start();

        } catch (IOException ex) {
            logError("imporssivel iniciar o processo: " + ex);
        }
        return true;
    }

    public boolean stopProcess() {
        if (process.isAlive()) {
            process.destroy();
            logInfo("processo terminado sob comando do usuario com sucesso.");
            return true;
        }
        if (pid != null) {
            try {
                Runtime.getRuntime().exec("kill " + pid);
                logWarning("processo forcado a terminar.");
                return true;
            } catch (IOException ex) {
                logWarning("impossivel terminar processo pid " + pid + " processo ja terminado? " + process.isAlive());
            }
        }
        logWarning("impossivel abortar processo, terminado");
        return false;
    }

    public String getStatus() {
        if (process.isAlive()) {
            try {
                if (pid == null) {
                    pid = new Scanner(new File(diretorio + "process.pid")).nextLine();
                }
                if (pid == null || pid.isEmpty() || pid.length() < 3) {
                    return "{\"error\":\"pid nao encontrado.\"}";
                }
                File temp = File.createTempFile("status", null);
                logDebug("arquivo temporario: " + temp.getAbsolutePath());
                ProcessBuilder builder = new ProcessBuilder("ps", "-p", pid, "-o", "%cpu,%mem,stat,time", "--no-headers");
                builder.redirectOutput(temp);
                logInfo("obtendo status: ps -p " + pid + " -o %cpu,%mem,stat,time --no-headers");
                Process ps = builder.start();
                logDebug("aguardando por termino de processo de status.");
                ps.waitFor();
                logDebug("obtendo resultado da consulta do status.");
                FileWriter fw = new FileWriter(temp, true);
                fw.append(System.lineSeparator());
                fw.close();
                String[] resultado = new Scanner(temp)
                        .nextLine()
                        .trim()
                        .replaceAll("\\s+", ",")
                        .split(",");
                logInfo("status obtido:\n" + Arrays.toString(resultado) + "\nfim do status obtido");
                return "{"
                        + "\"isAlive\":true,"
                        + "\"pid\":" + pid + ","
                        + "\"cpu\":\"" + resultado[0] + "\","
                        + "\"memoria\":\"" + resultado[1] + "\","
                        + "\"estado\":\"" + resultado[2] + "\","
                        + "\"tempo\":\"" + resultado[3] + "\""
                        + "}";
            } catch (IOException | InterruptedException ex) {
                logError("imporssivel obter status do processo: " + ex);
            }
        } else {
            logInfo("obtido status: processo morto.");
            return "{\"isAlive\":false,\"exitValue\":" + process.exitValue() + "}";
        }
        return "{\"error\":\"error while get status undefined.\"}";
    }

    public String getResult() {
        if (!process.isAlive()) {
            File result = new File(diretorio + fileName + "out");
            logInfo("lendo arquivo de saida em: " + diretorio + fileName + "out");
            return getLines(result, 0);
        }
        return "EM PROCESSAMENTO";
    }

    public String getOutput(int opcao) {
        return (getLines(output, opcao));
    }

    public String getErrorOutput(int opcao) {
        return (getLines(error, opcao));
    }

    //// case option < 0 = count lines botton to up starting on byte option
    //// case option > 0 = count lines up to botton
    //// case option = 0 = all lines
    String getLines(File file, int option) {
        try {
            StringBuilder lines = new StringBuilder();
            lines.append("[FILE START]").append(System.lineSeparator());
            int linescount = 0;
            if (option >= 0) {
                Scanner scanner = new Scanner(file);
                int count = 0;
                while ((option == 0 || (option > count++)) && scanner.hasNextLine()) {
                    lines.append("[").append(linescount++).append("]").append(scanner.nextLine());
                }
            } else {
                RandomAccessFile randFile = new RandomAccessFile(file, "r");
                randFile.seek(randFile.length() + option);
                while (randFile.getFilePointer() < randFile.length()) {
                    lines.append("[").append(linescount++).append("]").append(randFile.readLine());
                }
                randFile.close();
            }
            lines.append("[FILE END]").append(System.lineSeparator());
            return lines.toString();
        } catch (Exception ex) {
            logError("impossivel ler arquivo: " + file.getAbsolutePath() + " causa: " + ex);
        }
        return "GENERAL SYSTEM ERROR UNDEFINED. CONTATE ADMIN.";
    }

    public final void logInfo(String texto) {
        log(LogType.LOG_INFO, texto);
    }

    public final void logWarning(String texto) {
        log(LogType.LOG_WARNING, texto);
    }

    public final void logError(String texto) {
        log(LogType.LOG_ERROR, texto);
    }

    public final void logDebug(String texto) {
        log(LogType.LOG_DEBUG, texto);
    }

    public final void log(LogType tipo, String texto) {
        log.printLog(tipo, "[ BATCH ] " + texto);
    }

}
