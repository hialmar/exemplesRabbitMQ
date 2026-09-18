package withspring;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class SubTopicAll {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        //factory.setUri("amqps://zsmjfdwa:YAMrH7fE2L-VhfXIwR5vSFFWjwRLfWZu@seal.lmq.cloudamqp.com/zsmjfdwa");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
        String queueName = channel.queueDeclare().getQueue();


        channel.queueBind(queueName, EXCHANGE_NAME, "#");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            Gson gson = new Gson();
            CustomMessage monObjet = gson.fromJson(message, CustomMessage.class);
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + monObjet.toString() + "'");
            System.out.println("Header bar = "+delivery.getProperties().getHeaders().get("bar"));
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
