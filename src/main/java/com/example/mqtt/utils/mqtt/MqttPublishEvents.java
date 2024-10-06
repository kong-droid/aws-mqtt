package com.example.mqtt.utils.mqtt;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import software.amazon.awssdk.crt.mqtt5.Mqtt5Client;
import software.amazon.awssdk.crt.mqtt5.Mqtt5ClientOptions;
import software.amazon.awssdk.crt.mqtt5.PublishReturn;

import java.util.concurrent.CompletableFuture;

@Getter
@Slf4j
public class MqttPublishEvents implements Mqtt5ClientOptions.PublishEvents {

    private final CompletableFuture<String> payloadFeature = new CompletableFuture<>();

    @Override
    public void onMessageReceived(Mqtt5Client mqtt5Client, PublishReturn publishReturn) {
        val publishPacket = publishReturn.getPublishPacket();
        val contentType = publishPacket.getContentType();
        val qos = publishPacket.getQOS();
        val packetPayload = new String(publishPacket.getPayload());
        log.info("publish received on topic: {}", publishPacket.getTopic());
        log.info("publish info.\tcontent-type: {}, qos: {}, payload: {}", contentType, qos, packetPayload);
        payloadFeature.complete(packetPayload);
    }

}
