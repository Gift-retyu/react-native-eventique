package com.eventique;

import com.facebook.react.bridge.Promise;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import java.nio.charset.StandardCharsets;
import android.util.Log;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;

public class MqttClientWrapper {
    private Mqtt5BlockingClient mqttClient;

    public MqttClientWrapper(Mqtt5BlockingClient client) {
        this.mqttClient = client;
    }

    public boolean isConnected() {
        return mqttClient != null && mqttClient.getState().isConnected();
    }


    public void disconnect() {
        try {
            if (isConnected()) {
                mqttClient.disconnect();
                Log.d("MqttClientWrapper", "Disconnected successfully.");
            } else {
                Log.d("MqttClientWrapper", "MQTT client was not connected or already disconnected.");
            }
        } catch (Exception e) {
            Log.e("MqttClientWrapper", "Error disconnecting from MQTT", e);
        }
    }

    public interface MqttMessageHandler {
        void onMessage(String topic, String message);
    }

    public void subscribeToTopic(String topicFilter, MqttMessageHandler handler) {
        try {
            mqttClient.toAsync().subscribeWith()
                    .topicFilter(topicFilter)
                    .qos(MqttQos.AT_LEAST_ONCE)
                    .callback(publish -> {
                        String topic = publish.getTopic().toString();
                        String message = new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8);
                        handler.onMessage(topic, message);
                    })
                    .send();
            Log.d("MqttClientWrapper", "Subscribed to topic: " + topicFilter);
        } catch (Exception e) {
            Log.e("MqttClientWrapper", "Error subscribing to topic: " + topicFilter, e);
        }
    }

}
