package com.store.system.client;

import com.quakoo.space.annotation.domain.PrimaryKey;
import com.store.system.model.ProductSPU;
import com.store.system.model.StatisticsCustomerJob;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: store-system
 * @Author: LiHaoJie
 * @Description:
 * @Date: 2019/6/15 16:27
 * @Version: 1.0
 */
@Data
public class ClientStatisticsCustomer extends StatisticsCustomerJob implements Serializable{

    private int ten;//0-10

    private int twenty;//11-20

    private int forty;//21-40

    private int sixty;//41-60

    private int more;//60+

    private int manProportion;//男比例

    private int womanProportion;//女比例

    private int total;//总人数

    private List<Integer> customerJobs = new ArrayList<>();//多店统计 每个门店每天的数据

    public ClientStatisticsCustomer(StatisticsCustomerJob statisticsCustomerJob) {
        try {
            BeanUtils.copyProperties(this, statisticsCustomerJob);
        } catch (Exception e) {
            throw new IllegalStateException("ClientStatisticsCustomer construction error!");
        }
    }
}
