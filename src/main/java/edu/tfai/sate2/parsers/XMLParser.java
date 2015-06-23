package edu.tfai.sate2.parsers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;

public class XMLParser extends XStream {
    @Override
    protected MapperWrapper wrapMapper(MapperWrapper next) {
        return new MapperWrapper(next) {
            @Override
            public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                if (definedIn == Object.class) {
                    return false;
                }
                return super.shouldSerializeMember(definedIn, fieldName);
            }

        };

    }

}
