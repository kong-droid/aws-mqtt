package com.example.mqtt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

public class MqttDto {

    @Getter
    @Setter
    public static class MqttPublishDto {
        @Schema(description = "MQTT 토픽")
        private String topic;
        @Schema(description = "전송할 메세지 내용")
        private String message;
    }

    @Getter
    @Setter
    public static class MqttSubscribeDto {
        @Schema(description = "MQTT 토픽")
        private String topic;
    }

}
