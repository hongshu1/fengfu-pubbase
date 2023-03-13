package cn.rjtech.kit;

import com.jfinal.json.Json;
import com.jfinal.kit.Func;
import com.jfinal.kit.StrKit;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kephon
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class RestResult extends HashMap {

    /**
     * 状态
     */
    static String STATE = "code";
    static Integer STATE_OK = 200;
    static Integer STATE_FAIL = 201;
    static Func.F30<RestResult, String, Object> stateWatcher = null;
    static Func.F21<Boolean, Object, Boolean> okFailHandler = null;

    /**
     * 数据
     */
    static String DATA = "data";
    static boolean dataWithOkState = false;			// data(Object) 方法伴随 ok 状态

    /**
     * 消息
     */
    static String MSG = "mesage";

    public RestResult() {
    }

    public static RestResult of(Object key, Object value) {
        return new RestResult().set(key, value);
    }

    public static RestResult by(Object key, Object value) {
        return new RestResult().set(key, value);
    }

    public static RestResult create() {
        return new RestResult();
    }

    public static RestResult ok() {
        return new RestResult().setOk();
    }

    public static RestResult ok(String msg) {
        return new RestResult().setOk()._setMsg(msg);
    }

    public static RestResult ok(Object key, Object value) {
        return new RestResult().setOk().set(key, value);
    }

    public static RestResult fail() {
        return new RestResult().setFail();
    }

    public static RestResult fail(String msg) {
        return new RestResult().setFail()._setMsg(msg);
    }

    @Deprecated
    public static RestResult fail(Object key, Object value) {
        return new RestResult().setFail().set(key, value);
    }

    public static RestResult state(Object value) {
        return new RestResult()._setState(value);
    }

    public static RestResult data(Object data) {
        return new RestResult()._setData(data);
    }

    public static RestResult msg(String msg) {
        return new RestResult()._setMsg(msg);
    }

    /**
     * 避免产生 setter/getter 方法，以免影响第三方 json 工具的行为
     *
     * 如果未来开放为 public，当 stateWatcher 不为 null 且 dataWithOkState 为 true
     * 与 _setData 可以形成死循环调用
     */
    protected RestResult _setState(Object value) {
        super.put(STATE, value);
        if (stateWatcher != null) {
            stateWatcher.call(this, STATE, value);
        }
        return this;
    }

    /**
     * 避免产生 setter/getter 方法，以免影响第三方 json 工具的行为
     *
     * 如果未来开放为 public，当 stateWatcher 不为 null 且 dataWithOkState 为 true
     * 与 _setState 可以形成死循环调用
     */
    protected RestResult _setData(Object data) {
        super.put(DATA, data);
        if (dataWithOkState) {
            _setState(STATE_OK);
        }
        return this;
    }

    // 避免产生 setter/getter 方法，以免影响第三方 json 工具的行为
    protected RestResult _setMsg(String msg) {
        super.put(MSG, msg);
        return this;
    }

    public RestResult setOk() {
        return _setState(STATE_OK);
    }

    public RestResult setFail() {
        return _setState(STATE_FAIL);
    }

    public boolean isOk() {
        Object state = get(STATE);
        if (STATE_OK.equals(state)) {
            return true;
        }
        if (STATE_FAIL.equals(state)) {
            return false;
        }
        if (okFailHandler != null) {
            return okFailHandler.call(Boolean.TRUE, state);
        }

        throw new IllegalStateException("调用 isOk() 之前，必须先调用 ok()、fail() 或者 setOk()、setFail() 方法");
    }

    public boolean isFail() {
        Object state = get(STATE);
        if (STATE_FAIL.equals(state)) {
            return true;
        }
        if (STATE_OK.equals(state)) {
            return false;
        }
        if (okFailHandler != null) {
            return okFailHandler.call(Boolean.FALSE, state);
        }

        throw new IllegalStateException("调用 isFail() 之前，必须先调用 ok()、fail() 或者 setOk()、setFail() 方法");
    }

    public RestResult set(Object key, Object value) {
        super.put(key, value);
        return this;
    }

    public RestResult setIfNotBlank(Object key, String value) {
        if (StrKit.notBlank(value)) {
            set(key, value);
        }
        return this;
    }

    public RestResult setIfNotNull(Object key, Object value) {
        if (value != null) {
            set(key, value);
        }
        return this;
    }

    public RestResult set(Map map) {
        super.putAll(map);
        return this;
    }

    public RestResult set(RestResult ret) {
        super.putAll(ret);
        return this;
    }

    public RestResult delete(Object key) {
        super.remove(key);
        return this;
    }

    public <T> T getAs(Object key) {
        return (T)get(key);
    }

    public <T> T getAs(Object key, T defaultValue) {
        Object ret = get(key);
        return ret != null ? (T) ret : defaultValue;
    }

    public String getStr(Object key) {
        Object s = get(key);
        return s != null ? s.toString() : null;
    }

    public Integer getInt(Object key) {
        Number n = (Number)get(key);
        return n != null ? n.intValue() : null;
    }

    public Long getLong(Object key) {
        Number n = (Number)get(key);
        return n != null ? n.longValue() : null;
    }

    public Double getDouble(Object key) {
        Number n = (Number)get(key);
        return n != null ? n.doubleValue() : null;
    }

    public Float getFloat(Object key) {
        Number n = (Number)get(key);
        return n != null ? n.floatValue() : null;
    }

    public Number getNumber(Object key) {
        return (Number)get(key);
    }

    public Boolean getBoolean(Object key) {
        return (Boolean)get(key);
    }

    /**
     * key 存在，并且 value 不为 null
     */
    public boolean notNull(Object key) {
        return get(key) != null;
    }

    /**
     * key 不存在，或者 key 存在但 value 为null
     */
    public boolean isNull(Object key) {
        return get(key) == null;
    }

    /**
     * key 存在，并且 value 为 true，则返回 true
     */
    public boolean isTrue(Object key) {
        Object value = get(key);
        return (value instanceof Boolean && ((Boolean)value == true));
    }

    /**
     * key 存在，并且 value 为 false，则返回 true
     */
    public boolean isFalse(Object key) {
        Object value = get(key);
        return (value instanceof Boolean && ((Boolean)value == false));
    }

    public String toJson() {
        return Json.getJson().toJson(this);
    }

    public boolean equals(Object ret) {
        return ret instanceof RestResult && super.equals(ret);
    }

}
