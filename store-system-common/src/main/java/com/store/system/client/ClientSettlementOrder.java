package com.store.system.client;

import lombok.Data;

/**
 * @ClassName ClientSettlementOrder
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/8/8 15:10
 * @Version 1.0
 **/
@Data
public class ClientSettlementOrder {

    public int ali;

    public int wx;

    public int cash;

    public int stored;

    public int otherStored;

    public int amount;//未收

    public int total;


}
