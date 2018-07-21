import org.asynchttpclient.*;
import org.asynchttpclient.proxy.ProxyServer;

import java.util.*;
import java.util.concurrent.Future;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class Test
{

    private AsyncHttpClient asyncHttpClient = asyncHttpClient();

    public static void main(String args[]) throws Exception
    {
        Test test = new Test();
        test.testing();
    }

    public void testing() throws Exception
    {

        initConfig();
        for (String term : getTerms())
        {
            makeGetRequest(term);
            Thread.sleep(new Random().nextInt(1000));
        }


    }

    public void initConfig()
    {
        Realm.Builder realm = new Realm.Builder("username",
                "password");
        realm.setScheme(Realm.AuthScheme.BASIC);
        ProxyServer.Builder proxyServer = new ProxyServer.Builder(
                "host", 999999);
        proxyServer.setRealm(realm);

        // TODO : make provision to set custom headers for https requests before sending
        AsyncHttpClientConfig clientConfig = new DefaultAsyncHttpClientConfig.Builder()
                .setProxyServer(proxyServer).build();
        asyncHttpClient = asyncHttpClient(clientConfig);
    }

    public Set<String> getTerms()
    {
        Set<String> set = new HashSet<String>();
        set.add("hello");
        set.add("hi");
        set.add("dog");
        return set;
    }

    public void makeGetRequest(String term)
    {
        List<Param> paramList = new ArrayList<>(1);
        paramList.add(new Param("q", term));

        String url = "https://www.google.com/search";

        Future<Integer> whenStatusCode = asyncHttpClient.prepareGet(url)
                .setQueryParams(paramList).execute(new GetRequestAsyncCompletionHandler(term));

    }
}
// INFO: Regarding what headers we would be inserting and reading, here is the link : https://docs.proxymesh.com/article/42-java-proxy-configuration-examples