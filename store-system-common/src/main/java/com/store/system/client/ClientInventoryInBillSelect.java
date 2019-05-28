package com.store.system.client;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ClientInventoryInBillSelect implements Serializable {

    private ClientProductSPU productSPU;

    private List<ClientProductProperty> skuProperties;

    private List<ClientProductSKU> skuList;

}
