package com.store.system.client;

import java.io.Serializable;
import java.util.List;

public class ClientFinanceLogDetail implements Serializable {

    private double totalOut;

    private double totalIn;

    private List<ClientFinanceLog> logs;

    public double getTotalOut() {
        return totalOut;
    }

    public void setTotalOut(double totalOut) {
        this.totalOut = totalOut;
    }

    public double getTotalIn() {
        return totalIn;
    }

    public void setTotalIn(double totalIn) {
        this.totalIn = totalIn;
    }

    public List<ClientFinanceLog> getLogs() {
        return logs;
    }

    public void setLogs(List<ClientFinanceLog> logs) {
        this.logs = logs;
    }
}
