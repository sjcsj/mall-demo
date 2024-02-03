package cn.mall.controller;

import cn.mall.common.R;
import cn.mall.domain.IdAndAnswer;
import cn.mall.domain.NewAndOldPassword;
import cn.mall.domain.PageRequestParamWoId;
import cn.mall.domain.User;
import cn.mall.service.IUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    @ApiOperation("用户登录接口")
    public R login(@RequestBody User user) {
        return R.success("登录成功", userService.login(user));
    }

    @PostMapping("/logout")
    @ApiOperation("用户退出接口")
    public R logout() {
        userService.logout();
        return R.success("注销成功");
    }

    @PostMapping
    @ApiOperation("添加用户,注册,前端设置必填项:username,password,nickname,telephone")
    public R add(@RequestBody User user) {
        Integer add = userService.add(user);
        if (add == 0) {
            return R.error("该用户名已被使用，请更换用户名");
        } else if (add == 1) {
            return R.error("该手机号已注册");
        }
        return R.success("注册成功");
    }

    @PostMapping("/admin")
    @ApiOperation("添加管理员")
    @PreAuthorize("hasAuthority('superadmin')")
    public R addAdmin(@RequestBody User user) {
        Integer add = userService.addAdmin(user);
        if (add == 0) {
            return R.error("该用户名已被使用，请更换用户名");
        } else if (add == 1) {
            return R.error("该手机号已注册");
        }
        return R.success("添加成功");
    }

    @GetMapping("/userlist")
    @ApiOperation("获取用户列表，根据用户名分页查询模糊查询")
    @PreAuthorize("hasAnyAuthority('superadmin','admin')")
    public R getUserList(@RequestBody PageRequestParamWoId paramWoId) {
        IPage<User> userIPage = userService.getUserList(paramWoId);
        return R.success(userIPage);
    }

    @GetMapping("/adminlist")
    @ApiOperation("获取管理员列表，根据用户名分页查询模糊查询")
    @PreAuthorize("hasAuthority('superadmin')")
    public R getAdminList(@RequestBody PageRequestParamWoId paramWoId) {
        IPage<User> userIPage = userService.getAdminList(paramWoId);
        return R.success(userIPage);
    }

    @GetMapping("{id}")
    @ApiOperation("根据用户（管理员）id获取当前用户（管理员）的个人信息")
    public R getUserById(@PathVariable("id") Integer id) {
        User user = userService.getById(id);
        return R.success(user);
    }

    @PutMapping
    @ApiOperation("修改用户（管理员）个人信息")
    public R update(@RequestBody User user) {
        userService.updateById(user);
        return R.success();
    }

    @PostMapping("{id}")
    @ApiOperation("修改密码:呈现修改密码问的问题")
    public R getQuestionById(@PathVariable("id") Integer id) {
        String question = userService.getQuestionById(id);
        if (Objects.isNull(question)) {
            return R.error("该用户没有设置问题");
        }
        return R.success(question);
    }

    @GetMapping("/checkanswer")
    @ApiOperation("修改密码:校验用户所设问题答案（若用户没有设置问题则不用调用该接口）")
    public R checkAnswer(@RequestBody IdAndAnswer idAndAnswer) {
        return R.success(userService.check(idAndAnswer.getId(), idAndAnswer.getAnswer()));
    }

    @PutMapping("/updatepwd")
    @ApiOperation("修改密码")
    public R updatePassword(@RequestBody NewAndOldPassword newAndOldPassword) {
        return R.success(userService.updatePassword(newAndOldPassword));
    }

    @DeleteMapping
    @ApiOperation("用户永久注销接口（删除用户）（可批量删除）")
    @GlobalTransactional
    public R deleteUser(@RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            userService.deleteUser(id);
        }
        return R.success("删除成功");
    }

    @DeleteMapping("/admin")
    @ApiOperation("删除管理员（可批量删除）")
    @PreAuthorize("hasAuthority('superadmin')")
    public R deleteAdmin(@RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            userService.removeById(id);
        }
        return R.success("删除成功");
    }

    @DeleteMapping("/kick/{id}")
    @ApiOperation("将用户踢下线")
    @PreAuthorize("hasAnyAuthority('superadmin','admin')")
    public R kickUser(@PathVariable("id") Integer id){
        userService.kickUser(id);
        return R.success();
    }

}
