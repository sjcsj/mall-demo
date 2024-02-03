package cn.mall.service;

import cn.mall.domain.Address;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IAddressService extends IService<Address> {
    List<Address> getAddressByUserId(Integer id);

    void add(Address address);

    void updateAddress(Address address);

    Integer exist(Integer userId);


}
