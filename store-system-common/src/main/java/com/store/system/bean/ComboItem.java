package com.store.system.bean;

import lombok.Data;

import java.io.Serializable;

/**套餐详情
 * @ClassName ComboItem
 * @Description TODO
 * @Author LaoMa
 * @Date 2019/11/25 19:29
 * @Version 1.0
 **/
@Data
public class ComboItem implements Serializable {

    /***
    * 加价(分)
    */
    private int price;
    /***
     * 商品ID
     */
    private long skuId;
}
