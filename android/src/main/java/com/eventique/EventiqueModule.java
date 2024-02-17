package com.eventique;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import android.provider.Settings;

@ReactModule(name = EventiqueModule.NAME)
public class EventiqueModule extends ReactContextBaseJavaModule {
  public static final String NAME = "Eventique";
  private Properties eventiqueProperties;

  public EventiqueModule(ReactApplicationContext reactContext) {
    super(reactContext);
    eventiqueProperties = new Properties();
    try {
      InputStream is = reactContext.getAssets().open("eventique.properties");
      eventiqueProperties.load(is);
    } catch (IOException e) {
      Log.e("EventiqueModule", "Failed to load eventique.properties", e);
    }
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void connectToEventQueue(
    @Nullable String apiKey,
    final Promise promise
  ) {
    String host = eventiqueProperties.getProperty("host", "default_host");
    int port = Integer.parseInt(
      eventiqueProperties.getProperty("port", "8883")
    );
    String username = eventiqueProperties.getProperty(
      "username",
      "default_username"
    );
    String password = eventiqueProperties.getProperty(
      "password",
      "default_password"
    );
    String clientId = eventiqueProperties.getProperty(
      "clientId",
      "default_clientId"
    );
    String appName = getReactApplicationContext()
      .getApplicationInfo()
      .loadLabel(getReactApplicationContext().getPackageManager())
      .toString();

  String deviceId = Settings.Secure.getString(
    getReactApplicationContext().getContentResolver(),
    Settings.Secure.ANDROID_ID
  );

  String topicUuid = deviceId;

    if (apiKey != null && !apiKey.isEmpty()) {
      String credentialsUrl = "https://fetch-my-creds.com";
      try {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
          .url(credentialsUrl)
          .addHeader("Authorization", "Bearer " + apiKey)
          .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
          String responseBody = response.body().string();
          JSONObject credentials = new JSONObject(responseBody);
          host = credentials.optString("host", host);
          port = credentials.optInt("port", port);
          username = credentials.optString("username", username);
          password = credentials.optString("password", password);
          clientId = credentials.optString("clientId", clientId);
          topicUuid = credentials.optString("topicUuid", clientId);
        }
      } catch (Exception e) {
        Log.e(
          "EventiqueModule",
          "Error fetching credentials with API key, using default credentials",
          e
        );
      }
    }

    try {
      Mqtt5BlockingClient mqttClient = MqttClient
        .builder()
        .useMqttVersion5()
        .serverHost(host)
        .serverPort(port)
        .sslWithDefaultConfig()
        .identifier(clientId)
        .buildBlocking();

      mqttClient
        .connectWith()
        .simpleAuth()
        .username(username)
        .password(StandardCharsets.UTF_8.encode(password))
        .applySimpleAuth()
        .send();


      String subscriptionTopic = topicUuid + "/" + appName + "/sync/event";
      mqttClient
        .toAsync()
        .subscribeWith()
        .topicFilter(subscriptionTopic)
        .qos(MqttQos.AT_LEAST_ONCE)
        .callback(
          publish -> {
            String receivedTopic = publish.getTopic().toString();
            String message = new String(
              publish.getPayloadAsBytes(),
              StandardCharsets.UTF_8
            );
            WritableMap params = Arguments.createMap();
            params.putString("topic", receivedTopic);
            params.putString("message", message);
            getReactApplicationContext()
              .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
              .emit("sync_event", params);
          }
        )
        .send();

      promise.resolve(true);
    } catch (Exception e) {
      Log.e("EventiqueModule", "Error connecting to Queue", e);
      promise.reject(
        "EventiqueModule",
        "Error connecting to Queue: " + e.getMessage()
      );
    }
  }
}
