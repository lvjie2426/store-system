package com.store.system.backend.controller;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.webframework.BaseController;
import com.store.system.client.ClientSubordinate;
import com.store.system.client.PagerResult;
import com.store.system.client.ResultClient;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.ProductPropertyName;
import com.store.system.model.ProductPropertyNamePool;
import com.store.system.service.ProductPropertyNameService;
import com.store.system.service.SubordinateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/productpropertyname")
public class ProductPropertyNameController extends BaseController {

    @Resource
    private ProductPropertyNameService productPropertyNameService;

    @Resource
    private SubordinateService subordinateService;

    @RequestMapping("/add")
    public ModelAndView add(ProductPropertyName productPropertyName, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            productPropertyName = productPropertyNameService.add(productPropertyName);
            return this.viewNegotiating(request,response, new ResultClient(productPropertyName));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/update")
    public ModelAndView update(ProductPropertyName productPropertyName, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = productPropertyNameService.update(productPropertyName);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/updateStatus")
    public ModelAndView updateStatus(ProductPropertyName productPropertyName, HttpServletRequest request, HttpServletResponse response, Model model,
                                     long id, int status) throws Exception {
        try {
            boolean res = productPropertyNameService.updateStatus(id, status);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/del")
    public ModelAndView del(@RequestParam(value = "id") long id,
                            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            boolean res = productPropertyNameService.del(id);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getAllList")
    public ModelAndView getAllList(@RequestParam(value = "cid") long cid, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        List<ProductPropertyName> res = productPropertyNameService.getAllList(cid);
        return this.viewNegotiating(request,response, new ResultClient(true, res));
    }

    @RequestMapping("/search")
    public ModelAndView search(HttpServletRequest request, HttpServletResponse response,Pager pager,
                               @RequestParam(value = "cid",defaultValue = "0") long cid,
                               @RequestParam(name = "type",defaultValue = "0") int type,
                               @RequestParam(name = "content",defaultValue = "") String content,
                               @RequestParam(name = "input",defaultValue = "-1") int input,
                               @RequestParam(name = "defaul",defaultValue = "-1") int defaul,
                               @RequestParam(name = "multiple",defaultValue = "-1") int multiple,
                               @RequestParam(name = "status",defaultValue = "0") int status,
                               Model model) throws Exception {
        return this.viewNegotiating(request,response, new PagerResult<>(productPropertyNameService.search( pager,cid,type,content,input,defaul,multiple,status)));
    }

    @RequestMapping("/addPool")
    public ModelAndView addPool(@RequestParam(value = "subid") long subid,
                                @RequestParam(value = "pnid") long pnid,
                                HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            ClientSubordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() != 0) throw new StoreSystemException("非公司不能添加");
            ProductPropertyName propertyName = productPropertyNameService.load(pnid);
            ProductPropertyNamePool productPropertyNamePool = new ProductPropertyNamePool();
            productPropertyNamePool.setSubid(subid);
            productPropertyNamePool.setCid(propertyName.getCid());
            productPropertyNamePool.setPnid(pnid);
            boolean sign = productPropertyNameService.addPool(productPropertyNamePool);
            return this.viewNegotiating(request,response, new ResultClient(true, sign));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/delPool")
    public ModelAndView delPool(@RequestParam(value = "subid") long subid,
                                @RequestParam(value = "pnid") long pnid,
                                HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            ClientSubordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() != 0) throw new StoreSystemException("非公司不能删除");
            ProductPropertyName propertyName = productPropertyNameService.load(pnid);
            ProductPropertyNamePool productPropertyNamePool = new ProductPropertyNamePool();
            productPropertyNamePool.setSubid(subid);
            productPropertyNamePool.setCid(propertyName.getCid());
            productPropertyNamePool.setPnid(pnid);
            boolean res = productPropertyNameService.delPool(productPropertyNamePool);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }

    @RequestMapping("/getSubAllList")
    public ModelAndView getSubAllList(@RequestParam(value = "subid") long subid,
                                      @RequestParam(value = "cid") long cid,
                                      HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        try {
            ClientSubordinate subordinate = subordinateService.load(subid);
            if(subordinate.getPid() > 0) subid = subordinate.getPid();
            List<ProductPropertyName> res = productPropertyNameService.getSubAllList(subid, cid);
            return this.viewNegotiating(request,response, new ResultClient(true, res));
        } catch (StoreSystemException e) {
            return this.viewNegotiating(request,response, new ResultClient(false, e.getMessage()));
        }
    }


}
