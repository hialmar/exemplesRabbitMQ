package objects;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class Sender {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        // utilise l'URL du serveur AMQP
        // factory.setUri("amqps://ullismzb:c9SOdRKAdhUnqWjDW847u_1I7yn7L0VD@collie.lmq.cloudamqp.com/ullismzb");
        // pour fonctionnement en local
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            MaClasse monObjet = new MaClasse("Hello World!", 1.5f);
            Gson gson = new Gson();
            String message = gson.toJson(monObjet);

            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
