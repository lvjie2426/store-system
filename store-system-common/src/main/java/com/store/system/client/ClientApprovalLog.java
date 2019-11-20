package com.store.system.client;

import com.store.system.model.attendance.ApprovalLog;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;


/**
 * @program: store-system
 * @description:
 * @author: zhangmeng
 * @create: 2019-09-16 16:59
 **/
@Data
public class ClientApprovalLog extends ApprovalLog {

    private long data;//申请日期
    private String checkName;//审批人
    private int status;//审批状态

    private int replaceStatus;//被调班人状态

    public ClientApprovalLog(ApprovalLog approvalLog){
        try {
            BeanUtils.copyProperties(this, approvalLog);
        } catch (Exception e) {
            throw new IllegalStateException("ClientApprovalLog construction error!");
        }
    }
}
