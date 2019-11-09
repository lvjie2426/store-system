package com.store.system.client;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ClientInventoryCheckBillSelect implements Serializable {

    private ClientProductSPU productSPU;

    private int currentNum; //当前数量

    private List<ClientInventoryDetail> details;

}
