package com.store.system.client;

import com.store.system.model.FinanceLog;
import org.apache.commons.beanutils.BeanUtils;

public class ClientFinanceLog extends FinanceLog {

    private String ownName;

    public ClientFinanceLog() {
    }

    public ClientFinanceLog(FinanceLog financeLog) {
        try {
            BeanUtils.copyProperties(this, financeLog);
        } catch (Exception e) {
            throw new IllegalStateException("ClientFinanceLog construction is error!");
        }
    }

    public String getOwnName() {
        return ownName;
    }

    public void setOwnName(String ownName) {
        this.ownName = ownName;
    }
}
