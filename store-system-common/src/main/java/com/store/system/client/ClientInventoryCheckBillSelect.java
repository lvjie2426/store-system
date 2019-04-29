package com.store.system.client;

import com.store.system.model.InventoryDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
public class ClientInventoryCheckBillSelect implements Serializable {

    private ClientProductSPU productSPU;

    private int currentNum; //当前数量

    private List<InventoryDetail> details;

}
