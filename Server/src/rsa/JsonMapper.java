package rsa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created by Jk6 on 2017/7/18.
 */
public class JsonMapper extends ObjectMapper
{
    public JsonMapper()
    {
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
    }
}
