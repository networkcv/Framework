package com.lwj.pulsar;

import org.apache.pulsar.client.api.*;

import java.util.concurrent.TimeUnit;

/**
 * Date: 2022/4/30
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class SubscriptionModeTest {
    public static void main(String[] args) throws PulsarClientException, InterruptedException {
        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar://localhost:6650")
                .build();

        Producer<String> producer = client.newProducer(Schema.STRING)
                .topic("my-topic")
                .enableBatching(false)
                .create();

        MessageListener myMessageListener = (consumer, msg) -> {
            try {
                System.out.println("Message consumer" + consumer.getConsumerName() + " received: " + new String(msg.getData()));
                consumer.acknowledge(msg);
            } catch (Exception e) {
                consumer.negativeAcknowledge(msg);
            }
        };

        new Thread(() -> {
            Consumer consumer = null;
            try {
                consumer = client.newConsumer()
                        .topic("my-topic")
                        .subscriptionName("my-subscription")
//                        .messageListener(myMessageListener)
                        //Exclusive,Shared,Failover,Key_Shared
                        .subscriptionType(SubscriptionType.Key_Shared)
                        .subscribe();
                System.out.println(consumer.getConsumerName());
            } catch (PulsarClientException e) {
                e.printStackTrace();
            }
            int count=0;
            while (true) {
                Message msg = null;
                try {
                    msg = consumer.receive();
//                    count++;
                    if (count==5){
                        consumer.close();
                        TimeUnit.DAYS.sleep(1);
                    }
                    System.out.println("Message consumer" + consumer.getConsumerName() + " received: " + new String(msg.getData()));
                    consumer.acknowledge(msg);
                } catch (PulsarClientException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        new Thread(() -> {
            Consumer consumer = null;
            try {
                consumer = client.newConsumer()
                        .topic("my-topic")
                        .subscriptionName("my-subscription")
//                        .messageListener(myMessageListener)
                        //Exclusive,Shared,Failover,Key_Shared
                        .subscriptionType(SubscriptionType.Key_Shared)
                        .subscribe();
                System.out.println(consumer.getConsumerName());
            } catch (PulsarClientException e) {
                e.printStackTrace();
            }
            while (true) {
                Message msg = null;
                try {
                    msg = consumer.receive();
                    System.out.println("Message consumer" + consumer.getConsumerName() + " received: " + new String(msg.getData()));
                    consumer.acknowledge(msg);
                } catch (PulsarClientException e) {
                    e.printStackTrace();
                }
            }

        }).start();




// 3 messages with "key-1", 3 messages with "key-2", 2 messages with "key-3" and 2 messages with "key-4"
        producer.newMessage().key("key-1").value("message-1-1").send();
        producer.newMessage().key("key-1").value("message-1-1").send();
        producer.newMessage().key("key-1").value("message-1-2").send();
        producer.newMessage().key("key-1").value("message-1-3").send();
        producer.newMessage().key("key-2").value("message-2-1").send();
        producer.newMessage().key("key-2").value("message-2-2").send();
        MessageId messageId = producer.newMessage().key("key-2").value("message-2-3").send();
        producer.newMessage().key("key-3").value("message-3-1").send();
        producer.newMessage().key("key-3").value("message-3-2").send();
        producer.newMessage().key("key-4").value("message-4-1").send();
        producer.newMessage().key("key-4").value("message-4-2").send();
        TimeUnit.SECONDS.sleep(3);



    }
}
