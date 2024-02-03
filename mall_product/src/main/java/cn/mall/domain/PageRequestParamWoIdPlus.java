package cn.mall.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestParamWoIdPlus {

    Integer page;

    Integer size;

    String nameKeyword;

    String brandKeyword;

    Integer productstatus;
}
