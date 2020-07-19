package com.qf.controller;

import com.qf.constant.SystemConstant;
import com.qf.dto.TbUserDto;
import com.qf.dto.TbUserLoginDto;
import com.qf.service.UserService;
import com.qf.pojo.TbUser;
import com.qf.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "用户模块")//乔春燕
@RestController
@RequestMapping("api/user/")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("getAll")
    // @CheckToken//这里的注释是需要通过校验的，校验前端是否携带令牌
    public List<TbUser> getAll(){
        return userService.getAll();
    }

    /**
     * 乔春燕
     * 校验手机号是否存在
     */
    @ApiOperation(value = "校验手机号是否存在",notes = "手机号存在校验")
    @GetMapping("checkphone/{phone}")
    public R checkPhone(@PathVariable String phone){
        return   userService.checkPhone(phone);
    }

    /**
     * 乔春燕
     * 登录查询
     */
    @PostMapping("login")
    public R login(@RequestBody TbUserLoginDto loginDto){
        return userService.login(loginDto);
    }

    @ApiOperation("账户注册")
    @PostMapping("register")
    public R register(@RequestBody TbUserDto dto){
        return   userService.register(dto);
    }

    /**
     * 乔春燕
     * 忘记密码
     */
    @ApiOperation("忘记密码")
    @GetMapping("forgetPass")
    public R forgetPass(TbUserLoginDto loginDto){
       return userService.forgetPass(loginDto);
    }

    /**
     * 乔春燕
     * 重置密码
     */
    //先获取到用户的token
    @ApiOperation("重置密码")
    @PostMapping("resetPass")
    public R resetPass(HttpServletRequest request,  String pass){
        String token = request.getHeader(SystemConstant.TOKEN_HEADER);
        return userService.changePass(token, pass);
    }

    /**
     * 乔春燕
     * 退出登录
     * @param request
     * @return
     */
    @ApiOperation("退出")
    @GetMapping("logout")
    public R logout(HttpServletRequest request){
        String token = request.getHeader(SystemConstant.TOKEN_HEADER);
        return userService.logout(token);
    }

    /**
     * 乔春燕
     * 上传头像
     * @param file
     * @return
     */
    @ApiOperation("上传头像")
    @PostMapping("upload")
    public R upload(MultipartFile file){
       return userService.uploadAvatar(file);
    }


    /**
     * 乔春燕
     * 更换手机号
     * @param request
     * @param phone
     * @return
     */
    @ApiOperation("更换手机号")
    @PostMapping("resetPhone")
    public R resetPhone(HttpServletRequest request,  String phone){
        String token = request.getHeader(SystemConstant.TOKEN_HEADER);
        return userService.updatePhone(token, phone);
    }

    /**
     * 乔春燕
     * 修改昵称
     * @param request
     * @param nickName
     * @return
     */
    @ApiOperation("修改昵称")
    @PostMapping("resetNickname")
    public R resetNickname(HttpServletRequest request, String nickName){
        String token = request.getHeader(SystemConstant.TOKEN_HEADER);
        return userService.updateNickname(token, nickName);
    }
}
