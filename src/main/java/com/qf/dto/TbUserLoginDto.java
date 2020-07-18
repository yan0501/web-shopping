package com.qf.dto;

import lombok.Data;

/**
 * 对映射数据进行的封装
 */
@Data
public class TbUserLoginDto {

    private String phone; //用户手机号
    private String psw; //用户密码
}
