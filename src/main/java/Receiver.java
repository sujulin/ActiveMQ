import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Created by SuJuLin on 2017/12/30.
 */
public class Receiver {
    private static  final   String  MESSAGE_QUEUE="zhangphil";
    private final static    int PORT=61616;

    public static void main(String[] args) {

        // ConnectionFactory ：连接工厂，JMS 用它创建连接
        ConnectionFactory connectionFactory;

        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection = null;

        // Session： 一个发送或接收消息的线程
        Session session;

        // Destination ：消息的目的地;消息发送给谁.
        Destination destination;

        // 消息接收者
        MessageConsumer consumer;

        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,ActiveMQConnection.DEFAULT_PASSWORD, "tcp://127.0.0.1:"+PORT);

        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();

            // 启动
            connection.start();

            // 获取操作连接
            session = connection.createSession(Boolean.FALSE.booleanValue(), Session.AUTO_ACKNOWLEDGE);

            // 获取session注意参数值 zhangphil 是一个服务器的queue，只接受相同message queue字段的消息。
            destination = session.createQueue(MESSAGE_QUEUE);

            consumer = session.createConsumer(destination);

            while (true) {
                TextMessage message = (TextMessage) consumer.receive(1000);

                if (null != message) {
                    System.out.println("收到消息: " + message.getText());
                } else {
                    //如果不break，receiver将轮询接收消息。
                    //break，Receiver.java退出。
                    //break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {
            }
        }
    }
}
