package com.store.system.client;

import com.store.system.model.InBill;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

public class ClientInBill extends InBill {

    private String inUserName; //入库人姓名

    private int itemCount;

    private List<ClientInBillItem> items;

    private int totalNum;

    public ClientInBill(InBill inBill) {
        try {
            BeanUtils.copyProperties(this, inBill);
        } catch (Exception e) {
            throw new IllegalStateException("ClientInBill construction error!");
        }
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public List<ClientInBillItem> getItems() {
        return items;
    }

    public void setItems(List<ClientInBillItem> items) {
        this.items = items;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getInUserName() {
        return inUserName;
    }

    public void setInUserName(String inUserName) {
        this.inUserName = inUserName;
    }
}
