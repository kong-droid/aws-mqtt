package com.example.mqtt.controller;


import com.example.mqtt.dto.MqttDto;
import com.example.mqtt.utils.mqtt.MqttPubSubClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.Callable;

@Slf4j
@Validated
@RestController
@RequestMapping("/aws/mqtt")
@RequiredArgsConstructor
@Tag(name = "MQTT Test", description = "MQTT 테스트")
public class MQTTController {

    private final MqttPubSubClient mqttPubSubClient;

    @PostMapping("/publish")
    @Operation(summary = "MQTT 메세지 송신")
    public void publish(@RequestBody @Valid MqttDto.MqttPublishDto request) {
        mqttPubSubClient.publish(request.getTopic(), request.getMessage().getBytes());
    }

    @PostMapping("/subscribe")
    @Operation(summary = "MQTT 메세지 수신")
    public Callable<String> subscribe(@RequestBody @Valid MqttDto.MqttSubscribeDto request) {
        return () -> mqttPubSubClient.subscribe(request.getTopic());
    }

}

