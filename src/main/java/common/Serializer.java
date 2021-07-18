package common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class Serializer implements ISerialize {

    private final Gson gson = new GsonBuilder().create();

    @Override
    public String serialize(Object object) {
        return gson.toJson(object);
    }

    @Override
    public <T> T deserialize(String object, Type t) {
        return gson.fromJson(object, t);
    }
}
