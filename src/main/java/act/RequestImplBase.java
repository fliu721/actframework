package act;

import act.conf.AppConfig;
import org.osgl.$;
import org.osgl.http.H;
import org.osgl.util.E;
import org.osgl.util.FastStr;
import org.osgl.util.S;

public abstract class RequestImplBase<T extends H.Request> extends H.Request<T> {
    private AppConfig cfg;
    private H.Method method;
    private Boolean secure;

    protected RequestImplBase(AppConfig config) {
        E.NPE(config);
        cfg = config;
    }

    protected abstract H.Method _method();

    @Override
    public String contextPath() {
        return "";
    }

    @Override
    public T method(H.Method method) {
        this.method = $.NPE(method);
        return me();
    }

    @Override
    public H.Method method() {
        if (null == method) {
            method = _method();
            if (method == H.Method.POST) {
                // check the method overload
                String s = header(H.Header.Names.X_HTTP_METHOD_OVERRIDE);
                if (S.blank(s)) {
                    s = paramVal("_method"); // Spring use this
                }
                if (S.notBlank(s)) {
                    method = H.Method.valueOfIgnoreCase(s);
                }
            }
        }
        return method;
    }

    @Override
    public boolean secure() {
        if (null == secure) {
            if ("https".equals(cfg.xForwardedProtocol())) {
                secure = true;
            } else {
                secure = parseSecureXHeaders();
            }
        }
        return secure;
    }

    private boolean parseSecureXHeaders() {
        String s = header(H.Header.Names.X_FORWARDED_PROTO);
        if ("https".equals(s)) {
            return true;
        }
        s = header(H.Header.Names.X_FORWARDED_SSL);
        if ("on".equals(s)) {
            return true;
        }
        s = header(H.Header.Names.FRONT_END_HTTPS);
        if ("on".equals(s)) {
            return true;
        }
        s = header(H.Header.Names.X_URL_SCHEME);
        if ("https".equals(s)) {
            return true;
        }
        return false;
    }
}
