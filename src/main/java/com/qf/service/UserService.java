package com.qf.service;

import com.qf.dto.TbUserDto;
import com.qf.dto.TbUserLoginDto;
import com.qf.pojo.TbUser;
import com.qf.vo.R;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<TbUser> getAll();

    /**
     * 乔春燕
     * userservice 抽象方法
     * @param phone
     * @return
     */
    R checkPhone(String phone); //用户手机号码的验证
    R login(TbUserLoginDto loginDto); //登录验证
    R register(TbUserDto dto);        //注册
    R forgetPass(TbUserLoginDto loginDto); //密码查询
    R changePass(String token,String pass); //修改密码
    R logout(String token); //退出

    R uploadAvatar(MultipartFile file);//头像上传

    R updatePhone(String token,String phone);  //修改手机号

    R updateNickname(String token,String phone);  //修改昵称


}
