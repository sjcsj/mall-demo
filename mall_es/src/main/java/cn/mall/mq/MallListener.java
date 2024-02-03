package cn.mall.mq;

import ch.qos.logback.core.pattern.util.IEscapeUtil;
import cn.mall.common.MqConstants;
import cn.mall.domain.Product;
import cn.mall.domain.Product1;
import cn.mall.service.IEsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MallListener {

    @Autowired
    private IEsService esService;

    // 监听产品新增的业务
    @RabbitListener(queues = MqConstants.MALL_INSERT_QUEUE)
    public void listenMallInsert(Product1 product1) {
        esService.insert(product1);
    }

    // 监听产品修改的业务
    @RabbitListener(queues = MqConstants.MALL_UPDATE_QUEUE)
    public void listenMallUpdate(Product1 product1) {
        esService.update(product1);
    }

    // 监听产品删除的业务
    @RabbitListener(queues = MqConstants.MALL_DELETE_QUEUE)
    public void listenMallDelete(Integer id) {
        esService.deleteById(id);
    }
}
