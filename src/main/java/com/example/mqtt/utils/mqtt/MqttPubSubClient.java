package com.example.mqtt.utils.mqtt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.crt.mqtt5.Mqtt5Client;
import software.amazon.awssdk.crt.mqtt5.QOS;
import software.amazon.awssdk.crt.mqtt5.packets.ConnectPacket;
import software.amazon.awssdk.crt.mqtt5.packets.DisconnectPacket;
import software.amazon.awssdk.crt.mqtt5.packets.PublishPacket;
import software.amazon.awssdk.crt.mqtt5.packets.SubscribePacket;
import software.amazon.awssdk.iot.AwsIotMqtt5ClientBuilder;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MqttPubSubClient {

    @Value("${cloud.aws.mqtt.end-point}")
    private String endPoint;

    @Value("${cloud.aws.mqtt.certificate-path}")
    private String certificateFile;

    @Value("${cloud.aws.mqtt.private-path}")
    private String privateFile;

    private AwsIotMqtt5ClientBuilder awsIotMqtt5Client() {
        val clientId = UUID.randomUUID().toString();
        val builder = AwsIotMqtt5ClientBuilder.newDirectMqttBuilderWithMtlsFromPath(endPoint, certificateFile, privateFile);
        val connectProperties = new ConnectPacket.ConnectPacketBuilder();
        val lifeCycleEvents = new MqttLifeCycleEvents();
        connectProperties.withClientId(clientId);
        return builder
                .withConnectProperties(connectProperties)
                .withLifeCycleEvents(lifeCycleEvents);
    }


    public void publish(String topic, byte[] payload) {
        val mqttClient = awsIotMqtt5Client().build();
        start(mqttClient);
        clientCheck();
        try {
            val builder = new PublishPacket.PublishPacketBuilder();
            builder.withTopic(topic)
                    .withQOS(QOS.AT_LEAST_ONCE)
                    .withPayload(payload);
            mqttClient.publish(builder.build()).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("publish error. {}", e.toString());
        } finally {
            disconnect(mqttClient);
            close(mqttClient);
        }
    }

    public String subscribe(String topic) {

        val mqttClient = awsIotMqtt5Client();

        val publishEvent = new MqttPublishEvents();
        mqttClient.withPublishEvents(publishEvent);
        val mqttBuild = mqttClient.build();

        start(mqttBuild);
        clientCheck();

        try {
            val builder = new SubscribePacket.SubscribePacketBuilder();
            builder.withSubscription(topic, QOS.AT_LEAST_ONCE, false,
                    false, SubscribePacket.RetainHandlingType.DONT_SEND);

            mqttBuild.subscribe(builder.build()).get(1, TimeUnit.MINUTES);
            return publishEvent.getPayloadFeature().get(1, TimeUnit.MINUTES);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 현재 스레드의 인터럽트 상태를 복원
            log.error("subscribe interrupted. {}", e.toString());
            close(mqttBuild);
            return CompletableFuture.completedFuture(null).toString();
        } catch (Exception e) {
            log.error("subscribe error. {}", e.toString());
            close(mqttBuild);
            return CompletableFuture.completedFuture(null).toString();
        }
    }

    private void disconnect(Mqtt5Client mqttClient) {
        val disConnectBuilder = new DisconnectPacket.DisconnectPacketBuilder();
        disConnectBuilder.withReasonCode(DisconnectPacket.DisconnectReasonCode.NORMAL_DISCONNECTION);
        mqttClient.stop(disConnectBuilder.build());
        try {
            MqttLifeCycleEvents.stoppedFeature.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("mqtt client disconnect error. {}", e.getCause());
        }
    }

    private void close(Mqtt5Client mqttClient) {
        mqttClient.close();
    }

    private void start(Mqtt5Client mqttClient) {
        mqttClient.start();
    }

    private void clientCheck() {
        try {
            MqttLifeCycleEvents.connectedFeature.get(1, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("mqtt client error. {}", e.getCause());
        }
    }
}
