package cn.mall.mapper;

import cn.mall.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface UserMapper extends BaseMapper<User> {
    List<String> selectRoleByUserId(Integer id);

    String selectQuestionById(Integer id);

    List<User> getUserList();

    List<User> getAdminList();

}
