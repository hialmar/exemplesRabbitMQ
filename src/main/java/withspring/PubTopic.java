package withspring;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;

public class PubTopic {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        //factory.setUri("amqps://zsmjfdwa:YAMrH7fE2L-VhfXIwR5vSFFWjwRLfWZu@seal.lmq.cloudamqp.com/zsmjfdwa");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);

            CustomMessage monObjet = new CustomMessage("Hello MIAGE!", 15, true);
            Gson gson = new Gson();
            String message = gson.toJson(monObjet);
            AMQP.BasicProperties props = new AMQP.BasicProperties();
            HashMap map = new HashMap<String,Object>();
            map.put("bar","foo");
            props = props.builder().headers(map).build();
            channel.basicPublish(EXCHANGE_NAME, "foo.bar.baz", props, message.getBytes("UTF-8"));

            System.out.println("Sent message "+ message);

            channel.exchangeDeclare("auto.headers", "headers", true, true, null);

            props = new AMQP.BasicProperties();
            map = new HashMap<String,Object>();
            map.put("thing1","somevalue");
            map.put("thing2","someothervalue");
            props = props.builder().headers(map).build();
            channel.basicPublish("auto.headers", "", props, message.getBytes("UTF-8"));

            System.out.println("Sent message "+ message);
        }
    }
}
