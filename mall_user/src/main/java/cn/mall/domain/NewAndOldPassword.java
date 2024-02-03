package cn.mall.domain;

import lombok.Data;

@Data
public class NewAndOldPassword {

    private Integer id;

    private String oldpwd;

    private String newpwd;
}
