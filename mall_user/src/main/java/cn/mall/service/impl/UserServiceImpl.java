package cn.mall.service.impl;

import cn.mall.clients.ShoppingCartClient;
import cn.mall.domain.LoginUser;
import cn.mall.domain.NewAndOldPassword;
import cn.mall.domain.PageRequestParamWoId;
import cn.mall.domain.User;
import cn.mall.mapper.AddressMapper;
import cn.mall.mapper.CollectionMapper;
import cn.mall.mapper.UserMapper;
import cn.mall.service.IUserService;
import cn.mall.utils.JwtUtil;
import cn.mall.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private ShoppingCartClient shoppingCartClient;

    @Override
    public Map<String, String> login(User user) {
        //通过框架鉴权
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authentication)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //获取鉴权后的用户
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userid = loginUser.getUser().getId().toString();
        //生成jwt返回前端页面
        String jwt = JwtUtil.createJWT(userid);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        redisCache.setCacheObject("login:" + userid, loginUser);
        return map;
    }

    @Override
    public void logout() {
        //从redis中删除用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser principal = (LoginUser) authentication.getPrincipal();
        redisCache.deleteObject("login:" + principal.getUser().getId());
    }

    @Override
    public Integer add(User user) {
        // 检验用户名是否唯一
        LambdaQueryWrapper<User> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(User::getUsername, user.getUsername());
        User user1 = userMapper.selectOne(wrapper1);
        if (!Objects.isNull(user1)) {
            return 0;
        }
        // 检验手机号是否唯一
        LambdaQueryWrapper<User> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(User::getTelephone, user.getTelephone());
        User user2 = userMapper.selectOne(wrapper2);
        if (!Objects.isNull(user2)) {
            return 1;
        }
        user.setRole("user");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setCreatetime(new Date());
        userMapper.insert(user);
        return 2;
    }

    @Override
    public String getQuestionById(Integer id) {
        String s = userMapper.selectQuestionById(id);
        return s;
    }

    @Override
    public String check(Integer id, String answer) {
        User user = userMapper.selectById(id);
        if (user.getAnswer().equals(answer)) {
            return "答案正确";
        }
        return "答案错误";
    }

    @Override
    public String updatePassword(NewAndOldPassword newAndOldPassword) {
        User user = userMapper.selectById(newAndOldPassword.getId());
        if (!bCryptPasswordEncoder.matches(newAndOldPassword.getOldpwd(), user.getPassword())) {
            return "输入的旧密码错误";
        }
        if (newAndOldPassword.getOldpwd().equals(newAndOldPassword.getNewpwd())) {
            return "输入的新密码不能与旧密码相同";
        }
        String encode = bCryptPasswordEncoder.encode(newAndOldPassword.getNewpwd());
        user.setPassword(encode);
        userMapper.updateById(user);
        return "修改成功";
    }

    @Override
    public void deleteUser(Integer id) {
        // 删除用户信息
        userMapper.deleteById(id);
        // 删除该用户的收货地址
        addressMapper.deleteByUserId(id);
        // 删除该用户收藏的产品
        collectionMapper.deleteByUserId(id);
        // 删除该用户的购物车
        shoppingCartClient.deleteByUserId(id);
    }

    @Override
    public Integer addAdmin(User user) {
        // 检验用户名是否唯一
        LambdaQueryWrapper<User> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(User::getUsername, user.getUsername());
        User user1 = userMapper.selectOne(wrapper1);
        if (!Objects.isNull(user1)) {
            return 0;
        }
        // 检验手机号是否唯一
        LambdaQueryWrapper<User> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(User::getTelephone, user.getTelephone());
        User user2 = userMapper.selectOne(wrapper2);
        if (!Objects.isNull(user2)) {
            return 1;
        }
        user.setRole("admin");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setCreatetime(new Date());
        userMapper.insert(user);
        return 2;
    }

    @Override
    public IPage<User> getUserList(PageRequestParamWoId paramWoId) {
        if (Objects.isNull(paramWoId.getPage())) {
            paramWoId.setPage(1);
        }
        if (Objects.isNull(paramWoId.getSize())) {
            paramWoId.setSize(10);
        }
        IPage<User> userIPage = new Page<>(paramWoId.getPage(), paramWoId.getSize());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getRole, "user");
        queryWrapper.like(!Strings.isNullOrEmpty(paramWoId.getKeyword()), User::getUsername, paramWoId.getKeyword());
        userMapper.selectPage(userIPage, queryWrapper);
        return userIPage;
    }

    @Override
    public IPage<User> getAdminList(PageRequestParamWoId paramWoId) {
        if (Objects.isNull(paramWoId.getPage())) {
            paramWoId.setPage(1);
        }
        if (Objects.isNull(paramWoId.getSize())) {
            paramWoId.setSize(10);
        }
        IPage<User> userIPage = new Page<>(paramWoId.getPage(), paramWoId.getSize());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getRole, "admin");
        queryWrapper.like(!Strings.isNullOrEmpty(paramWoId.getKeyword()), User::getUsername, paramWoId.getKeyword());
        userMapper.selectPage(userIPage, queryWrapper);
        return userIPage;
    }

    @Override
    public void kickUser(Integer id) {
        redisCache.deleteObject("login:" + id);
    }

}
