package act.controller.meta;

import org.osgl.$;
import org.osgl.util.C;

import java.util.Map;

public class ParamAnnoInfo extends ParamAnnoInfoTraitBase {
    private String bindName = "";
    private Map<Class, Object> defValMap = C.newMap();

    public ParamAnnoInfo(int index) {
        super(index);
    }

    @Override
    public void attachTo(HandlerParamMetaInfo param) {
        param.paramAnno(this);
    }

    public ParamAnnoInfo bindName(String name) {
        this.bindName = name;
        return this;
    }
    public String bindName() {
        return bindName;
    }
    public ParamAnnoInfo defVal(Class<?> type, Object val) {
        defValMap.put(type, val);
        return this;
    }
    public <T> T defVal(Class<?> type) {
        if (primitiveTypes.containsKey(type)) {
            type = primitiveTypes.get(type);
        }
        Object v = defValMap.get(type);
        if (null == v) return null;
        return $.cast(v);
    }

    private static Map<Class, Class> primitiveTypes = C.map(
            boolean.class, Boolean.class,
            byte.class, Byte.class,
            short.class, Short.class,
            char.class, Character.class,
            int.class, Integer.class,
            float.class, Float.class,
            long.class, Long.class,
            double.class, Double.class
    );

}
