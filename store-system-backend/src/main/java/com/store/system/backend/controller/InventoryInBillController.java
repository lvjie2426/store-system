package com.store.system.backend.controller;

import com.google.common.collect.Lists;
import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.*;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.InventoryInBill;
import com.store.system.model.ProductPropertyName;
import com.store.system.model.ProductPropertyValue;
import com.store.system.model.User;
import com.store.system.service.*;
import com.store.system.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/inventoryinbill")
public class InventoryInBillController extends BaseController {

    @Resource
    private ProductService productService;

    @Resource
    private SubordinateService subordinateService;

//    @Resource
//    private ProductPropertyService productPropertyService;

    @Resource
    private ProductPropertyNameService productPropertyNameService;

    @Resource
    private ProductPropertyValueService productPropertyValueService;

    @Resource
    private InventoryInBillService inventoryInBillService;

    @RequestMapping("/select")
    public ModelAndView select(@RequestParam(value = "type") int type,
                               @RequestParam(value = "subid") long subid,
                               @RequestParam(value = "pid") long pid,
                               @RequestParam(value = "cid") long cid,
                               @RequestParam(value = "bid") long bid,
                               @RequestParam(value = "sid") long sid,
                               HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            ClientSubordinate subordinate = subordinateService.load(subid);
            long pSubid = subordinate.getPid();
            if(pSubid == 0) throw new StoreSystemException("店铺为空");
            ClientInventoryInBillSelect res = null;
            ClientProductSPU productSPU = productService.selectSPU(type, pSubid, pid, cid, bid, sid);
            if(null != productSPU) {
                List<ProductPropertyName> propertyNames = productPropertyNameService.getSubAllList(pSubid, cid);
                for(Iterator<ProductPropertyName> it = propertyNames.iterator(); it.hasNext();) {
                    ProductPropertyName propertyName = it.next();
                    if(propertyName.getType() != ProductPropertyName.type_sku) it.remove();
                }
                if(propertyNames.size() > 0) {
                    res = new ClientInventoryInBillSelect();
                    res.setProductSPU(productSPU);
                    List<ClientProductProperty> properties = Lists.newArrayList();
                    for(ProductPropertyName propertyName : propertyNames) {
                        ClientProductProperty property = new ClientProductProperty(propertyName);
                        if(propertyName.getInput() == ProductPropertyName.input_no) {
                            List<ProductPropertyValue> propertyValues = productPropertyValueService.getSubAllList(pSubid, propertyName.getId());
                            List<ClientProductPropertyValue> values = Lists.newArrayList();
                            for(ProductPropertyValue propertyValue : propertyValues) {
                                ClientProductPropertyValue value = new ClientProductPropertyValue(propertyValue);
                                values.add(value);
                            }
                            property.setValues(values);
                        }
                        properties.add(property);
                    }
                    res.setSkuProperties(properties);
                }
            }
            return this.viewNegotiating(request,response, new ResultClient(res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/add")
    public ModelAndView add(InventoryInBill inventoryInBill, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            inventoryInBill = inventoryInBillService.add(inventoryInBill);
            return this.viewNegotiating(request,response, new ResultClient(inventoryInBill));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(InventoryInBill inventoryInBill, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = inventoryInBillService.update(inventoryInBill);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = inventoryInBillService.del(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/submit")
    public ModelAndView submit(@RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = inventoryInBillService.submit(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getCreatePager")
    public ModelAndView getCreatePager(Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        User user = UserUtils.getUser(request);
        long createUid = user.getId();
        pager = inventoryInBillService.getCreatePager(pager, createUid);
        return this.viewNegotiating(request,response, new PagerResult<>(pager));
    }

    @RequestMapping("/getCheckPager")
    public ModelAndView getCheckPager(@RequestParam(value = "subid") long subid,
                                      Pager pager, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        pager = inventoryInBillService.getCheckPager(pager, subid);
        return this.viewNegotiating(request,response, new PagerResult<>(pager));
    }

    @RequestMapping("/pass")
    public ModelAndView pass(@RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            long checkUid = user.getId();
            inventoryInBillService.pass(id, checkUid);
            return this.viewNegotiating(request,response, new ResultClient(true));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/noPass")
    public ModelAndView noPass(@RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            User user = UserUtils.getUser(request);
            long checkUid = user.getId();
            inventoryInBillService.noPass(id, checkUid);
            return this.viewNegotiating(request,response, new ResultClient(true));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

}
