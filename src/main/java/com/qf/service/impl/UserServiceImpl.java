package com.qf.service.impl;

import com.alibaba.fastjson.JSON;
import com.qf.config.RedisKeyConfig;
import com.qf.constant.SystemConstant;
import com.qf.dto.TbUserDto;
import com.qf.dto.TbUserLoginDto;
import com.qf.mapper.TbUserMapper;
import com.qf.pojo.TbUser;
import com.qf.pojo.TbUserExample;
import com.qf.service.UserService;
import com.qf.util.*;
import com.qf.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
class UserServiceImpl implements UserService {
    //设置OSS存储空间名称
    public static final String OSS_BUCKET="uploadFile"; //乔春燕
    @Autowired
    TbUserMapper userMapper;
    @Autowired
    private JedisCore jedisCore;
    @Value("${shopping.aes.passkey}") //乔春燕
    public String key;

    /**
     * 测试程序连接的方法 getAll()
     * @return
     */
    @Override
    public List<TbUser> getAll() {
        return userMapper.selectByExample(null);
    }


    /**
     * 乔春燕
     * 测试手机号是否可用
     * @param phone
     * @return
     */
    @Override
    public R checkPhone(String phone) {
        TbUser user = userMapper.selectByPhone(phone);
        if(user != null){
            return R.error("手机号已经存在");
        }else{
            return R.ok();
        }
    }

    /**
     * 乔春燕
     * 登录
     * @param loginDto
     * @return
     */
    @Override
    public R login(TbUserLoginDto loginDto) {
        //如果被冻结的账号中可以查询到客户的手机号信息，返回客户的手机号已经被冻结
        if(jedisCore.checkKey(RedisKeyConfig.PHONE_FOR + loginDto.getPhone())){
            return R.error("非常抱歉，您的手机号已经被冻结，请在"+jedisCore.ttl(RedisKeyConfig.PHONE_FOR + loginDto.getPhone())+"秒后重新登录!");
        }else if(jedisCore.checkKey(RedisKeyConfig.PHONE_TOKEN + loginDto.getPhone())){
            //如果校验key已存在，提示客户已经登录
            return R.ok("亲，您已经登录过了");
        }else{
            //若客户账号未被冻结，且未登录，根据客户传入的手机号，校验客户手机号是否为空
            TbUser tbUser = userMapper.selectByPhone(loginDto.getPhone());
            //是否出现错误提示，默认为true
            boolean iserror = true;
            if(tbUser != null){
                //判断用户提供的密码是否正确
                if(tbUser.getPassword().equals(EncryptUtil.aesenc(key, loginDto.getPsw()))){
                    //若密码正确，生成令牌
                    String token = TokenUtil.createToken(tbUser.getUid());
                    //将生成的令牌存储在redis,并设置有效期
                    jedisCore.set(RedisKeyConfig.PHONE_TOKEN + loginDto.getPhone(), token , RedisKeyConfig.TOKEN_TIME);
                    jedisCore.set(RedisKeyConfig.TOKEN_USER + token , JSON.toJSONString(tbUser) , RedisKeyConfig.TOKEN_TIME);
                    //登录成功，不提示错误
                    iserror = false;
                    return R.ok(token);
                }
            }
            //若出现了错误提示，表明登录成功
            if(iserror){
                //检验校验次数  10 分钟以内，只允许错误3次
                if(jedisCore.keys(RedisKeyConfig.PHONE_ERROR + loginDto.getPhone()) == 3){
                    //若用户在10分钟内输错3次，账号冻结30分钟
                    jedisCore.set(RedisKeyConfig.PHONE_FOR + loginDto.getPhone(),System.currentTimeMillis() + "" , RedisKeyConfig.TOKENFOR_TIME);
                }
                //记录本次错误的有效期
                jedisCore.set(RedisKeyConfig.PHONE_ERROR + loginDto.getPhone() + ":" + System.currentTimeMillis(),"" , RedisKeyConfig.PHONERROR_TIME);
            }
            return R.error("账号或密码错误");
        }
    }

    /**
     * 乔春燕
     * 账户注册
     * @param dto
     * @return
     */
    @Override
    public R register(TbUserDto dto) {
        //校验手机号是否可用 调用checkphone（）方法，获取前端响应的状态码
        if(checkPhone(dto.getPhone()).getCode() == 200){
            //若状态为200，证明可用，将获取到的用户信息存储在数据库
            TbUser user = new TbUser();
            user.setPhone(dto.getPhone());
            user.setPassword(EncryptUtil.aesenc(key, dto.getPsw()));
            user.setNickname(dto.getNickname());
            userMapper.insert(user);

            return R.ok();
        }else{
            return R.error("亲，您的手机号已经被注册过了");
        }

    }

    /**
     * 乔春燕
     * 忘记密码
     * @param loginDto
     * @return
     */
    @Override
    public R forgetPass(TbUserLoginDto loginDto) {
        //获取用户手机号码，查询用户的信息
        TbUser tbUser = userMapper.selectByPhone(loginDto.getPhone());
        //判断用户输入的手机号是否存在
        if(tbUser != null){
            //若存在，判断用户的手机号是否有效 0有效  1被注销
            Integer flag = tbUser.getFlag();
            if(flag == 1){
                return R.error("亲，您的账号已经被注销");
            }else{
                //若用户的账号状态正常，将客户重新设置的密码，存储在数据库
                //获取用户的新密码
                tbUser.setPassword(EncryptUtil.aesenc(key, loginDto.getPsw()));//对密码加密
                return userMapper.updateByPrimaryKey(tbUser)==1 ? R.ok() :R.error("亲，密码修改失败，请重新填写") ;
            }
        }else{
            //若用户的手机号不存在，返回错误提示
            return R.error("亲，账号不存在，请您重新输入");
        }
    }

    /**
     * 乔春燕
     * 修改密码
     * @param token
     * @param pass
     * @return
     */
    @Override
    public R changePass(String token, String pass) {
        //判断用户是否登录
        if(jedisCore.checkKey(RedisKeyConfig.TOKEN_USER + token)){
            //若登录，获取用户传入的token信息,进而获取tbUser对象
            TbUser tbUser = JSON.parseObject(jedisCore.get(RedisKeyConfig.TOKEN_USER + token), TbUser.class);
            //获取前端传入的密码并加密存入user对象
            tbUser.setPassword(EncryptUtil.aesenc(key, pass));
            //调用修改密码方法，将新密码存储在数据库
            return userMapper.updateByPrimaryKey(tbUser) == 1 ? R.ok() : R.error("密码修改失败，请您重新输入");
        }
        return R.error("亲，请先登录");
    }


    /**
     * 乔春燕
     * 退出登录
     * @param token
     * @return
     */
    @Override
    public R logout(String token) {
        //判断token是否存在
            //若存在，删除用户信息
            jedisCore.del(RedisKeyConfig.TOKEN_USER + token);
            //从redis中获取用户信息，并转化为对象传递给后台
            TbUser tbUser = JSON.parseObject(jedisCore.get(RedisKeyConfig.TOKEN_USER + token),TbUser.class);
            //删除手机号
            jedisCore.del(RedisKeyConfig.PHONE_TOKEN + tbUser.getPhone());

            return R.ok();

    }

    /**
     * 乔春燕
     * 图片上传
     */
    @Override
    public R uploadAvatar(MultipartFile file) {
        if(!file.isEmpty()){
            try {

                //对源文件进行重命名
                String fileName = FileUtil.rename(file.getOriginalFilename());
                //初始化
                OssSingleCore singleCore = OssSingleCore.getInstance();
                //调用上传方法，其中设置存储空间名称，文件名，获取上传的文件大小
                String str = singleCore.upload(OSS_BUCKET, fileName, file.getBytes());
                //校验字符串是否为空
                if(!StringUtil.isEmpty(str)){
                    return R.ok(str);
                }
            }catch (Exception e){
                return R.error(e.getMessage());
            }
        }
        return R.error("文件上传失败");
    }

    /**
     * 修改手机号
     * @param token
     * @param phone
     * @return
     */
    @Override
    public R updatePhone(String token,String phone) {
        //根据token判断用户是否登录
//       if(jedisCore.checkKey(RedisKeyConfig.TOKEN_USER + token)) {
           //获取用户信息，并将用户信息转换为对象
           TbUser tbUser = JSON.parseObject(jedisCore.get(RedisKeyConfig.TOKEN_USER + token), TbUser.class);
           //获取前端传入的手机号
           tbUser.setPhone(phone);
           //调用更新方法，将用户的新手机号存储在数据库
           return userMapper.updateByPrimaryKey(tbUser) == 1 ? R.ok() : R.error("手机号修改失败");
//       }
//        return R.error("亲，请您先登录");
    }


    /**
     * 乔春燕
     * 修改昵称
     * @param token
     * @param nickName
     * @return
     */
    @Override
    public R updateNickname(String token, String nickName) {
        //根据token判断用户是否登录

            //获取用户信息，并将用户信息转换为对象
            TbUser tbUser = (TbUser)JSON.parseObject(jedisCore.get(RedisKeyConfig.TOKEN_USER + token), TbUser.class);
            //获取前端传入的昵称
            tbUser.setNickname(nickName);
            //调用更新方法，将用户的新昵称存储在数据库
            return userMapper.updateByPrimaryKey(tbUser) == 1 ? R.ok() : R.error("昵称修改失败");
    }
}
