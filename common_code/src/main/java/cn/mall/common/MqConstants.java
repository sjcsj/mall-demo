package cn.mall.common;

public class MqConstants {
    // 交换机
    public final static String MALL_EXCHANGE = "mall.topic";

    // 监听新增的队列
    public final static String MALL_INSERT_QUEUE = "mall.insert.queue";

    // 监听修改的队列
    public final static String MALL_UPDATE_QUEUE = "mall.update.queue";

    // 监听删除的队列
    public final static String MALL_DELETE_QUEUE = "mall.delete.queue";

    // 新增的RoutingKey
    public final static String MALL_INSERT_KEY = "mall.insert";

    // 修改的RoutingKey
    public final static String MALL_UPDATE_KEY = "mall.update";

    // 删除的RoutingKey
    public final static String MALL_DELETE_KEY = "mall.delete";

}
