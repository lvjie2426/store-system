package com.store.system.service.impl;

import com.quakoo.baseFramework.model.pagination.Pager;
import com.quakoo.ext.RowMapperHelp;
import com.store.system.client.ClientSubordinate;
import com.store.system.client.ResultClient;
import com.store.system.dao.CommissionDao;
import com.store.system.dao.CompanyDao;
import com.store.system.exception.StoreSystemException;
import com.store.system.model.Company;
import com.store.system.model.ProductSPU;
import com.store.system.model.Subordinate;
import com.store.system.service.CompanyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {


    private RowMapperHelp<Company> spuRowMapper = new RowMapperHelp<>(Company.class);

    @Resource
    private CompanyDao companyDao;
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public ResultClient insert(Company company) throws Exception {
        check(company);
        return new ResultClient(companyDao.insert(company));
    }

    @Override
    public ResultClient update(Company company) throws Exception {
        check(company);
        return new ResultClient(companyDao.update(company));
    }

    @Override
    public Pager getAll(Pager pager) throws Exception {
        pager.setData(companyDao.getAll());
        pager.setTotalCount(companyDao.getCount());
        return pager;
    }

    @Override
    public Pager getOverdue(Pager pager) throws Exception {

        String sql = "SELECT * FROM `company` where `status` = " + Company.status_online;
        String sqlCount = "SELECT COUNT(id) FROM `company` where `status` = " + Company.status_online;
        long time=System.currentTimeMillis();
        sql=sql+" and  comBusinessDateEnd >"+time;
        sqlCount=sqlCount+" and  comBusinessDateEnd >"+time;
        sql = sql + " order  by `ctime` desc";
        List<Company> companyList = this.jdbcTemplate.query(sql, spuRowMapper);
        int count = this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
        pager.setData(companyList);
        pager.setTotalCount(count);
        return pager;
    }

    @Override
    public List<Company> getMp(int mpId) throws Exception {
        String sql = "SELECT * FROM `company` where `status` = " + Company.status_online;
        sql=sql+" and  checkStatus=1";
        if(mpId>0){
                sql = sql + " and (`manufacturerProvider` like '[" + mpId + "]'  or `manufacturerProvider` like '%," + mpId + ",%'  or `manufacturerProvider` like '[" + mpId + ",%'   or `manufacturerProvider` like '%," + mpId + "]')";
        }
        List<Company> companyList = this.jdbcTemplate.query(sql, spuRowMapper);
        return companyList;
    }

    @Override
    public boolean checkStatus(List<Long> ids) throws Exception {
        List<Company> companyList = companyDao.load(ids);
        if(companyList.size()>0){
            for(Company company:companyList){
                company.setCheckStatus(Company.checkStatus_yes);
                companyDao.update(company);
            }
            return true;
        }else{
            return false;
        }
    }

    private void check(Company company) throws StoreSystemException {

        if (StringUtils.isBlank(company.getName())) throw new StoreSystemException("企业名称不能为空。");
        if (StringUtils.isBlank(company.getUserName())) throw new StoreSystemException("企业联系人不能为空。");
        if (StringUtils.isBlank(company.getPhone())) throw new StoreSystemException("企业联系人电话不能为空。");
        if (StringUtils.isBlank(company.getRange())) throw new StoreSystemException("企业经营范围不能为空。");

        if (company.getStoreImg().size() == 0) throw new StoreSystemException("企业营业执照照片不能为空。");
        if (StringUtils.isBlank(company.getComBusinessNum())) throw new StoreSystemException("企业营业执照编号不能为空。");
        if (company.getComBusinessDate() == 0) throw new StoreSystemException("企业营业执照发证日期不能为空。");
        if (company.getComBusinessDateEnd() == 0) throw new StoreSystemException("企业营业执照有效日期结束时间不能为空。");
        if (company.getComBusinessDateStart() == 0) throw new StoreSystemException("企业营业执照有效日期开始时间不能为空。");
        if (company.getManufacturerProvider().size() == 0) throw new StoreSystemException("生产商/供应商不能为空。");

        if (StringUtils.isBlank(company.getMfNum())) throw new StoreSystemException("医疗器械注册证编号不能为空。");
        if (company.getMfRegisterDate() == 0) throw new StoreSystemException("医疗器械注册证批准日期不能为空。");
        if (company.getMfRegisterDateEnd() == 0) throw new StoreSystemException("医疗器械注册证有效日期结束时间不能为空。");
        if (company.getMfRegisterDateStart() == 0) throw new StoreSystemException("医疗器械注册证有效日期开始时间不能为空。");
        if (company.getMfRegisterImg().size() == 0) throw new StoreSystemException("医疗器械注册证照片不能为空。");

        if (StringUtils.isBlank(company.getLicenceNum())) throw new StoreSystemException("生产许可证编号不能为空。");
        if ((company.getLicenceDate() == 0)) throw new StoreSystemException("生产许可证发证日期不能为空。");
        if ((company.getLicenceDateEnd() == 0)) throw new StoreSystemException("生产许可证有效日期结束时间不能为空。");
        if ((company.getLicenceDateStart() == 0)) throw new StoreSystemException("生产许可证有效日期开始时间不能为空。");
        if ((company.getLicenceImg().size() == 0)) throw new StoreSystemException("生产许可证照片不能为空。");
//		if((company.getLicenceDescImg().size()==0))throw new  StoreSystemException("生产许可证其他资料照片不能为空。");

        if (StringUtils.isBlank(company.getProNum())) throw new StoreSystemException("医疗器械经营许可证编号不能为空。");
        if (company.getProDate() == 0) throw new StoreSystemException("供应商经营许可证批准日期不能为空。");
        if (company.getProDateEnd() == 0) throw new StoreSystemException("供应商经营许可证有效日期结束时间不能为空。");
        if (company.getProDateStart() == 0) throw new StoreSystemException("供应商经营许可证有效日期开始时间不能为空。");
        if (company.getProImg().size() == 0) throw new StoreSystemException("经营许可证照片不能为空");

        if (company.getProvince() == 0) throw new StoreSystemException("企业省不能为空。");
        if (company.getCity() == 0) throw new StoreSystemException("企业城市不能为空。");
        if (company.getArea() == 0) throw new StoreSystemException("企业区不能为空。");
        if (StringUtils.isBlank(company.getAddress())) throw new StoreSystemException("企业详细地址不能为空。");

    }
}
