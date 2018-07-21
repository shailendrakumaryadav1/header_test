import io.netty.handler.codec.http.HttpHeaders;
import org.asynchttpclient.Realm;
import org.asynchttpclient.filter.FilterContext;
import org.asynchttpclient.filter.FilterException;
import org.asynchttpclient.filter.RequestFilter;
import org.asynchttpclient.proxy.ProxyServer;

/**
 * Filter the request, so the logic of which IP should be set for ProxyMesh, is implemented here.
 */
public class ProxyMeshClient implements RequestFilter {

    public static final String X_PROXY_MESH_PREFER_IP = "X-ProxyMesh-Prefer-IP";
    public static final String X_PROXY_MESH_IP = "X-ProxyMesh-IP";
    public String proxyIp = "";

    public static final ProxyServer.Builder getProxyServer() {
        Realm.Builder realm = new Realm.Builder("username",
                "password");
        realm.setScheme(Realm.AuthScheme.BASIC);
        ProxyServer.Builder proxyServer = new ProxyServer.Builder(
                "localhost", 1090);
        proxyServer.setRealm(realm);
        return proxyServer;
    }

    @Override
    public <T> FilterContext<T> filter(FilterContext<T> ctx) throws FilterException {
        ctx.getRequest().getHeaders().add(X_PROXY_MESH_PREFER_IP, proxyIp);
        HttpHeaders responseHeaders = ctx.getResponseHeaders();
        if (responseHeaders != null) {
            proxyIp = responseHeaders.get(X_PROXY_MESH_IP);
            System.out.printf("Reading %s : %s", X_PROXY_MESH_IP, proxyIp);
        }
        return ctx;
    }
}
