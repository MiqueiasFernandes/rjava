/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jriacces2.state;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import jriacces2.Protocolo;
import jriacces2.log.ILog;
import jriacces2.strategy.EchoWriter;
import jriacces2.strategy.StatusWriter;

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
        state.writeLine(new EchoWriter(echo + System.lineSeparator()));
        logDebug("echo enviado. {ReadingState.java/26}");
    }

    public String readLine() {
        String line = new Scanner(System.in).nextLine();
        logInfo("leu: " + line);
        if ("stop".equals(line)) {
            protocolo.fechar();
        }
        if ("status".equals(line)) {
            WritingState state = new WritingState(protocolo, log);
            protocolo.setState(state);
            state.writeLine(new StatusWriter(getStatus()));
            logDebug("status enviado. {ReadingState.java/246}");
            return "";
        }
        echo(new String(Base64.getDecoder().decode(line)));
        return line;
    }

    public String getStatus() {
        if (protocolo.getrJava().getRengine().isAlive()) {
            String pid = protocolo.getrJava().getPid();
            try {
                File temp = File.createTempFile("status", null);
                logDebug("arquivo temporario: " + temp.getAbsolutePath());
                ProcessBuilder builder = new ProcessBuilder("ps", "-p",
                        pid, "-o", "%cpu,%mem,stat,time", "--no-headers");
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
                        + "\"pidJAVA\":" + jriacces2.JRIacces2.getPID() + ","
                        + "\"cpu\":\"" + resultado[0] + "\","
                        + "\"memoria\":\"" + resultado[1] + "\","
                        + "\"estado\":\"" + resultado[2] + "\","
                        + "\"tempo\":\"" + resultado[3] + "\""
                        + "}";
            } catch (IOException | InterruptedException ex) {
                logError("imporssivel obter status do processo: " + ex);
            }
        }
        return "{\"error\":\"error while get status undefined.\"}";
    }

}
