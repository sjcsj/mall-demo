package cn.mall.controller;

import cn.mall.common.R;
import cn.mall.domain.Address;
import cn.mall.service.IAddressService;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private IAddressService addressService;

    @PostMapping
    @ApiOperation("用户添加地址")
    @PreAuthorize("hasAuthority('user')")
    @Transactional
    public R add(@RequestBody Address address){
        addressService.add(address);
        return R.success("添加成功");
    }

    @GetMapping("{id}")
    @ApiOperation("根据用户id查询该用户所有地址，并把默认地址放在第一位")
    @Transactional
    public R getAddressByUserId(@PathVariable("id") Integer id){
        return R.success(addressService.getAddressByUserId(id));
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("根据地址id查询该地址的具体信息")
    public R getAddressById(@PathVariable("id") Integer id){
        return R.success(addressService.getById(id));
    }

    @PutMapping
    @ApiOperation("修改地址信息")
    @PreAuthorize("hasAuthority('user')")
    @Transactional
    public R updateAddress(@RequestBody Address address){
        addressService.updateAddress(address);
        return R.success();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除地址")
    @PreAuthorize("hasAuthority('user')")
    public R deleteAddress(@PathVariable("id") Integer id){
        addressService.removeById(id);
        return R.success();
    }

    @GetMapping("/exist/{userId}")
    @ApiOperation("判断某个用户是否有填写地址")
    @Transactional
    public Integer addressexist(@PathVariable("userId") Integer userId){
        return addressService.exist(userId);
    }

}
