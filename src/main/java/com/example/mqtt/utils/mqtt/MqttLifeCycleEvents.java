package com.example.mqtt.utils.mqtt;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import software.amazon.awssdk.crt.CRT;
import software.amazon.awssdk.crt.mqtt5.*;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class MqttLifeCycleEvents implements Mqtt5ClientOptions.LifecycleEvents {
    public static CompletableFuture<Object> connectedFeature = new CompletableFuture<>();
    public static CompletableFuture<Object> stoppedFeature = new CompletableFuture<>();

    @Override
    public void onAttemptingConnect(Mqtt5Client mqtt5Client, OnAttemptingConnectReturn onAttemptingConnectReturn) {
        log.info("mqtt5 client: attempting connection.");
    }

    @Override
    public void onConnectionSuccess(Mqtt5Client mqtt5Client, OnConnectionSuccessReturn onConnectionSuccessReturn) {
        log.info("mqtt5 client: connection success.\tqos: {}, clientId: {}, use retain: {}, keep alive: {}\n",
                onConnectionSuccessReturn.getNegotiatedSettings().getMaximumQOS().name(),
                onConnectionSuccessReturn.getNegotiatedSettings().getAssignedClientID(),
                onConnectionSuccessReturn.getNegotiatedSettings().getRetainAvailable(),
                onConnectionSuccessReturn.getNegotiatedSettings().getServerKeepAliveSeconds());
        if (!connectedFeature.isDone()) {
            connectedFeature.complete(null);
        }
    }

    @Override
    public void onConnectionFailure(Mqtt5Client mqtt5Client, OnConnectionFailureReturn onConnectionFailureReturn) {
        val errorMessage = CRT.awsErrorString(onConnectionFailureReturn.getErrorCode());
        log.info("mqtt5 client: connection failed with error. {}", errorMessage);
        if (!connectedFeature.isDone()) {
            connectedFeature.completeExceptionally(new Exception(errorMessage));
        }
    }

    @Override
    public void onDisconnection(Mqtt5Client mqtt5Client, OnDisconnectionReturn onDisconnectionReturn) {
        log.info("mqtt5 client: disconnected.");
        val disconnectedPacket = onDisconnectionReturn.getDisconnectPacket();
        if(disconnectedPacket != null) {
            log.info("disconnection packet code: {}", disconnectedPacket.getReasonCode());
            log.info("disconnection packet reason: {}", disconnectedPacket.getReasonString());
        }
    }

    @Override
    public void onStopped(Mqtt5Client mqtt5Client, OnStoppedReturn onStoppedReturn) {
        log.info("mqtt5 client: stopped.");
        if (!stoppedFeature.isDone()) {
            stoppedFeature.complete(null);
        }
    }
}
