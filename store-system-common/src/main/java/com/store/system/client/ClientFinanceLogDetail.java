package com.store.system.client;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ClientFinanceLogDetail implements Serializable {

    private double totalOut;

    private double totalIn;

    private double aliOut;

    private double aliIn;

    private double wxOut;

    private double wxIn;

    private double cashOut;

    private double cashIn;

    private double storedOut;

    private double storedIn;

    private List<ClientFinanceLog> logs;

}
