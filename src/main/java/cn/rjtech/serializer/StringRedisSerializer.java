package cn.rjtech.serializer;

import com.jfinal.plugin.redis.serializer.ISerializer;
import redis.clients.jedis.util.SafeEncoder;

/**
 * 字符串序列化方式
 *
 * @author Kephon
 */
public class StringRedisSerializer implements ISerializer {

    @Override
    public byte[] keyToBytes(String key) {
        return SafeEncoder.encode(key);
    }

    @Override
    public String keyFromBytes(byte[] bytes) {
        return SafeEncoder.encode(bytes);
    }

    @Override
    public byte[] fieldToBytes(Object field) {
        return valueToBytes(field);
    }

    @Override
    public Object fieldFromBytes(byte[] bytes) {
        return valueFromBytes(bytes);
    }

    @Override
    public byte[] valueToBytes(Object value) {
        return SafeEncoder.encode(value.toString());
    }

    @Override
    public Object valueFromBytes(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return SafeEncoder.encode(bytes);
    }

}
