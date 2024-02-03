package cn.mall.service;

import cn.mall.domain.NewAndOldPassword;
import cn.mall.domain.PageRequestParamWoId;
import cn.mall.domain.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface IUserService extends IService<User> {
    Map<String, String> login(User user);

    void logout();

    Integer add(User user);

    String getQuestionById(Integer id);

    String check(Integer id, String answer);

    String updatePassword(NewAndOldPassword newAndOldPassword);

    void deleteUser(Integer id);

    Integer addAdmin(User user);

    IPage<User> getUserList(PageRequestParamWoId paramWoId);


    IPage<User> getAdminList(PageRequestParamWoId paramWoId);

    void kickUser(Integer id);

}
