package cn.mall.mapper;

import cn.mall.domain.Address;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface AddressMapper extends BaseMapper<Address> {
    void deleteByUserId(Integer id);

    List<Address> getAddressByUserId(Integer id);

    void updateStatus(Integer userId);

    Address selectOtherAddress(Integer id, Integer userId);

}
