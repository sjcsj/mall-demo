package cn.mall.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestParamWoIdAndAmount {

    Integer page;

    Integer size;

    String keyword;

    Integer minnumber;

    Integer maxnumber;
}
