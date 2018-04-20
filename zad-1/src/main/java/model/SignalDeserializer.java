package model;

import com.google.gson.*;

import java.lang.reflect.Type;

public class SignalDeserializer implements JsonDeserializer<Signal> {
    @Override
    public Signal deserialize(JsonElement json, Type typeOfT,
                              JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement signalType = jsonObject.get("signalType");
        if (signalType != null) {
            switch (signalType.getAsString()) {
                case "Szum o rozkładzie jednostajnym":
                    return context.deserialize(jsonObject,
                            UniformSignal.class);
            }
        }
        return null;
    }
}