package common;

import java.lang.reflect.Type;

public interface ISerialize {

    String serialize(Object object);

    <T> T deserialize(String object, Type t);

}
