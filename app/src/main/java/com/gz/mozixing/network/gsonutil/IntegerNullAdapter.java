package com.gz.mozixing.network.gsonutil;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author Alex
 * @since 19/1/30
 */
public class IntegerNullAdapter extends TypeAdapter<Integer> {
    @Override
    public Integer read(JsonReader reader) throws IOException {
        // TODO Auto-generated method stub
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return 0;
        }
        return reader.nextInt();
    }
    @Override
    public void write(JsonWriter writer, Integer value) throws IOException {
        // TODO Auto-generated method stub
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.value(value);
    }
}