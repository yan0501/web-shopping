package com.qf.dto;

import lombok.Data;

/**
 * User层面的dto
 */
@Data
public class TbUserDto {

    private String phone;//手机号
    private String psw;//密码
    private String nickname;//昵称

}
