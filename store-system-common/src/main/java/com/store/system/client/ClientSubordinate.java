package com.store.system.client;



import com.store.system.model.Subordinate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.beanutils.BeanUtils;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientSubordinate extends Subordinate {

    public ClientSubordinate(Subordinate subordinate) {
        try {
            if(subordinate != null)
            BeanUtils.copyProperties(this, subordinate);
        } catch (Exception e) {
            throw new IllegalStateException("ClientSubordinate construction error!");
        }
    }

}
