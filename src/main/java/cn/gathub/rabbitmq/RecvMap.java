package cn.gathub.rabbitmq;

import cn.gathub.model.User;
import cn.gathub.utils.JsonUtils;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.List;
import java.util.Map;

public class RecvMap {

    private final static String QUEUE_NAME = "gathub-map";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            Map<String, Object> map = JsonUtils.jsonToMap(message);
            List<User> userList = JSONObject.parseArray(map.get("userList").toString(), User.class);
            String code = (String) map.get("code");

            System.out.println(" [x] Received '" + code + "---" + userList.get(0).getUserName() + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
    }
}
