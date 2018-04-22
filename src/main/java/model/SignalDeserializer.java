package model;

import com.google.gson.*;
import model.signal.*;

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
                case Utils.UNIFORM_NOISE: {
                    return context.deserialize(jsonObject, Uniform.class);
                }
                case Utils.GAUSSIAN_NOISE: {
                    return context.deserialize(jsonObject, Gaussian.class);
                }
                case Utils.SINE_SIGNAL: {
                    return context.deserialize(jsonObject, Sinusoidal.class);
                }
                case Utils.ONE_HALF_SINE_SIGNAL: {
                    return context.deserialize(jsonObject, SinusoidalOneHalf.class);
                }
                case Utils.TWO_HALF_SINE_SIGNAL: {
                    return context.deserialize(jsonObject, SinusoidalTwoHalf.class);
                }
                case Utils.RECT_SIGNAL: {
                    return context.deserialize(jsonObject, Rectangular.class);
                }
                case Utils.SYMMETRIC_RECT_SIGNAL: {
                    return context.deserialize(jsonObject, RectangularSymmetric.class);
                }
                case Utils.TRIANGLE_SIGNAL: {
                    return context.deserialize(jsonObject, Triangular.class);
                }
                case Utils.UNIT_JUMP_SIGNAL: {
                    return context.deserialize(jsonObject, UnitJump.class);
                }
                case Utils.PULSE_NOISE: {
                    return context.deserialize(jsonObject, Pulse.class);
                }
            }
        }
        return null;
    }
}