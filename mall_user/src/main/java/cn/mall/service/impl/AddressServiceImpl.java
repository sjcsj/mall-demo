package cn.mall.service.impl;

import cn.mall.domain.Address;
import cn.mall.mapper.AddressMapper;
import cn.mall.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<Address> getAddressByUserId(Integer id) {
        List<Address> addressList = addressMapper.getAddressByUserId(id);
        return addressList;
    }

    @Override
    public void add(Address address) {
        if (address.getStatus() == 0) {
            addressMapper.insert(address);
        } else {
            // 若用户之前已将其他地址设置为默认地址，取消掉默认
            addressMapper.updateStatus(address.getUserId());
            addressMapper.insert(address);
        }
    }

    @Override
    public void updateAddress(Address address) {
        // 查询除了这个地址之外的其他地址，看是否有某个被设为默认
        Address address1 = addressMapper.selectOtherAddress(address.getId(), address.getUserId());
        if (Objects.isNull(address1)){ // 如果没有，直接更新
            addressMapper.updateById(address);
        }else {
            if (address.getStatus() == 1){ // 如果有，判断现在要更新的地址是否设为默认
                address1.setStatus(0);
                addressMapper.updateById(address1);
                addressMapper.updateById(address);
            }else {
                addressMapper.updateById(address);
            }
        }
    }

    @Override
    public Integer exist(Integer userId) {
        List<Address> addressByUserId = addressMapper.getAddressByUserId(userId);
        if (addressByUserId.size() == 0){
            return 0;
        }
        return 1;
    }

}
