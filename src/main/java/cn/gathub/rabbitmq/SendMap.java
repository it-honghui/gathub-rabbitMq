package cn.gathub.rabbitmq;

import cn.gathub.model.User;
import cn.gathub.utils.JsonUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendMap {

    private final static String QUEUE_NAME = "gathub-map";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            User user = new User("弘辉", 24);
            User user2 = new User("肉球", 1);
            List<User> userList = Arrays.asList(user, user2);
            Map<String, Object> map = new HashMap<>();
            map.put("code", "200");
            map.put("userList", userList);

            channel.basicPublish("", QUEUE_NAME, null, JsonUtils.objectToJson(map).getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + map + "'");
        }
    }
}
