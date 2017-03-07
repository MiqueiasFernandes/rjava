/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jriacces2.state;

import jriacces2.Protocolo;
import jriacces2.log.ILog;

public class SelectingState extends AbstractState {

    SelectingState(Protocolo protocolo, ILog log) {
        super(protocolo, log);
        nameOfState = "selecionando arquivo";
        logInfoState();
    }

}
