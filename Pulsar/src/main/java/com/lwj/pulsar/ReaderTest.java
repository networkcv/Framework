package com.lwj.pulsar;

import org.apache.pulsar.client.api.*;

/**
 * Date: 2022/5/1
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class ReaderTest {
    public static void main(String[] args) throws PulsarClientException {
        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar://localhost:6650")
                .build();



        Producer<String> producer = client.newProducer(Schema.STRING)
                .topic("my-topic1")
                .enableBatching(false)
                .create();

        producer.newMessage().key("key-1").value("message-1-1").send();
        producer.newMessage().key("key-1").value("message-1-2").send();
        producer.newMessage().key("key-1").value("message-1-3").send();
        producer.newMessage().key("key-1").value("message-1-4").send();


        MessageId messageId = MessageId.earliest;
        System.out.println(messageId.toByteArray());
        Reader reader = client.newReader()
                .topic("my-topic1")
                .startMessageId(messageId)
                .create();

        Consumer consumer = client.newConsumer()
                .topic("my-topic1")
                .subscriptionName("my-subscription")
                .subscriptionType(SubscriptionType.Shared)
                .subscribe();

        while (true) {
            Message msg = reader.readNext();
            // Process message
            System.out.println("Message:" + " received: " + new String(msg.getData()));
        }


    }
}
