package com.example.myapplication;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android.util.Log;

import com.google.gson.*;

import java.util.UUID;

public class MosquittoDriver
{


    public static MosquittoDriver instance = new MosquittoDriver();
    private MqttAsyncClient client;
    private String device;
    private final String clientName = UUID.randomUUID().toString() + " Android";
    private LampMQTTDelegate delegate;


    private MosquittoDriver()
    {
        try
        {
            Log.d("Mqtt", "Connecting to server");
            client = new MqttAsyncClient("tcp://3.89.174.155:50001", MqttAsyncClient.generateClientId(), new MemoryPersistence());
            MqttConnectOptions opts = new MqttConnectOptions();
            opts.setCleanSession(true);
            client.setCallback(new LampiCallback());
            client.connect(opts);
            Log.d("Mqtt", "Connected to server");
        }
        catch (MqttException me)
        {
            Log.d("mqtt", me.getReasonCode() + "");
            Log.d("mqtt", me.getMessage() + "");
        }
        catch (Exception e)
        {
            Log.d("mqtt", "Conncetion error");
            //"+me.getReasonCode());e.getReasonCode()
            Log.d("mqtt", e.getMessage() + " ");
            Log.d("mqtt", e.getLocalizedMessage());
            //Log.d("mqtt", e.getCause().toString());


        }
    }

    public void setCurrentDevice(String deviceName)
    {
        try
        {
            Log.d("Mqtt", "Subsribing device");
            client.unsubscribe("devices/" + device + "/lamp/changed");
            client.subscribe("devices/" + deviceName + "/lamp/changed", 1);
            Log.d("Mqtt", "Subsribed device");
            Log.d("Mqtt", "Unsubscribed device");
            this.device = deviceName;

        }
        catch (MqttException me)
        {
            Log.d("mqtt", me.getReasonCode() + "");
            Log.d("mqtt", me.getMessage() + "");
        }
        catch (Exception e)
        {
            Log.d("Mqtt", "Error Subsribing device");
            this.device = deviceName;
        }
    }

    public void setDelegate(LampMQTTDelegate delegate)
    {
        this.delegate = delegate;
    }

    public void publishState(boolean isOn, double h, double s, double brightness)
    {
        if(client.isConnected())
        {
            Log.d("Mqtt", "Publishing state");

            JsonObject newState = new JsonObject();
            newState.addProperty("client", clientName);
            newState.addProperty("brightness", brightness);
            newState.addProperty("on", isOn);

            JsonObject newColor = new JsonObject();
            newColor.addProperty("h", h);
            newColor.addProperty("s", s);

            newState.add("color", newColor);

            Log.d("Mqtt", newState.toString());

            MqttMessage stateMsg = new MqttMessage(newState.toString().getBytes());

            try {
                client.publish("devices/" + device + "/lamp/set_config", stateMsg);
            } catch (Exception e) {
                Log.d("Mosqutto", "could not send message");
            }
        }
        else
        {
            Log.d("Mosquitto", "Client disconnected");
        }
    }


    class LampiCallback implements MqttCallback
    {

        public void connectionLost(Throwable cause)
        {

        }

        public void deliveryComplete(IMqttDeliveryToken token)
        {

        }

        public void messageArrived(String topic, MqttMessage message)
        {

            try {
                Log.d("Mqtt", "Receiving state");
                String jsonString = message.toString();
                JsonElement parsed = new JsonParser().parse(jsonString);
                JsonObject newState = parsed.getAsJsonObject();

                Log.d("Mqtt", "Converted to JSON obj");
                if(!newState.getAsJsonPrimitive("client").getAsString().equals(clientName)) {

                    for(String key : newState.keySet())
                    {
                        Log.d("keys:", key);
                    }

                    JsonObject color = newState.getAsJsonObject("color");
                    Log.d("Mqtt", "Read color");
                    JsonPrimitive hue = color.getAsJsonPrimitive("h");
                    Log.d("Mqtt", "read h");
                    JsonPrimitive sat = color.getAsJsonPrimitive("s");
                    Log.d("Mqtt", "Read s");
                    double h = hue.getAsDouble();
                    double s = sat.getAsDouble();
                    Log.d("Mqtt", "Converted h/s to doubles " + h + " " + s);

                    JsonPrimitive brightness = newState.getAsJsonPrimitive("brightness");
                    double b = brightness.getAsDouble();
                    Log.d("Mqtt", "Got b " + b);

                    JsonPrimitive on = newState.getAsJsonPrimitive("on");
                    boolean isOn = on.getAsBoolean();

                    Log.d("Mqtt", isOn + "");
                    Log.d("Mqtt", "Got power");

                    if (delegate != null) {
                        Log.d("Mqtt", "Parsed full json obj");
                        delegate.receiveState(isOn, h, s, b);
                    }
                }
            }
            catch (Exception e)
            {
                Log.d("Json", e.getMessage());
                Log.d("Json", "parsing error");
            }

            //{"color": {"h": 0.25, "s": 0.74}, "on": true, "client": "lamp_ui", "brightness": 1.0}
        }
    }

}
