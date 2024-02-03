package cn.mall;

import cn.mall.domain.ProductAndCollectiontime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@SpringBootTest
class MallUserApplicationTests {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void contextLoads() {
        List<ProductAndCollectiontime> productAndCollectiontimes = new ArrayList<>();
        productAndCollectiontimes.add(null);
        productAndCollectiontimes.add(null);
        System.out.println(productAndCollectiontimes);

    }

}
