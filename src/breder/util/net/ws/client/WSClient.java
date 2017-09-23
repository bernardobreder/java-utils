package breder.util.net.ws.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import breder.util.log.storage.ErrorLog;
import breder.util.log.storage.LogModel;
import breder.util.net.ws.shared.WSInputStream;
import breder.util.net.ws.shared.WSRequest;

/**
 * Serviço de WebService
 * 
 * 
 * @author Bernardo Breder
 */
public class WSClient {

  /** Configuração */
  private static final WSClientConfig config = new WSClientConfig();

  /**
   * Busca por um serviço
   * 
   * @param <E>
   * @param host
   * @param c
   * @return serviço
   */
  @SuppressWarnings("unchecked")
  public static <E> E lookup(final String host, final Class<E> c) {
    if (!c.isInterface()) {
      throw LogModel.getInstance().addAndThrow(
        new IllegalArgumentException("only interface service"));
    }
    return (E) Proxy.newProxyInstance(WSClient.class.getClassLoader(),
      new Class<?>[] { c }, new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
          throws Throwable {
          if (args == null) {
            args = new Object[0];
          }
          for (int n = 0; n < args.length; n++) {
            if (args[n] instanceof InputStream) {
              args[n] = new WSInputStream((InputStream) args[n]);
            }
          }
          WSRequest request = buildRequest(c, method, args);
          Object result = executeRequest(host, request);
          WSStorage.getInstance().put(request, result);
          if (result instanceof InvocationTargetException) {
            throw LogModel.getInstance().addAndThrow(
              ((InvocationTargetException) result).getTargetException());
          }
          else if (result instanceof Throwable) {
            throw LogModel.getInstance().addAndThrow((Throwable) result);
          }
          else {
            return result;
          }
        }

        /**
         * @param c
         * @param method
         * @param args
         * @return requisição
         */
        private WSRequest buildRequest(final Class<?> c, Method method,
          Object[] args) {
          Class<?>[] classParams = new Class<?>[args.length];
          for (int n = 0; n < args.length; n++) {
            classParams[n] = method.getParameterTypes()[n];
          }
          WSRequest request =
            new WSRequest(c.getSimpleName(), method.getName(), classParams,
              args);
          return request;
        }

        private Object executeRequest(final String host, WSRequest request)
          throws InterruptedException {
          Object result = null;
          try {
            result = invokeNetwork(host, request);
          }
          catch (Throwable t) {
            if (getConfig().isStorage()) {
              WSStorage storage = WSStorage.getInstance();
              boolean found = false;
              synchronized (storage) {
                if (storage.contain(request)) {
                  result = storage.get(request);
                  found = true;
                }
              }
              if (!found) {
                result = retryInvokeNetwork(host, request);
              }
            }
            else {
              result = retryInvokeNetwork(host, request);
            }
          }
          return result;
        }

        private Object retryInvokeNetwork(final String host, WSRequest request)
          throws InterruptedException {
          Object result;
          for (;;) {
            try {
              result = invokeNetwork(host, request);
              break;
            }
            catch (Throwable tt) {
              LogModel.getInstance().add(new ErrorLog(tt));
              Thread.sleep(30 * 1000);
            }
          }
          return result;
        }

        /**
         * @param host
         * @param request
         * @return resultado do serviço
         * @throws IOException
         * @throws ClientProtocolException
         * @throws ClassNotFoundException
         */
        protected Object invokeNetwork(final String host, WSRequest request)
          throws IOException, ClientProtocolException, ClassNotFoundException {
          String hostpath = host;
          if (!hostpath.startsWith("http://")) {
            hostpath = "http://" + hostpath;
          }
          HttpPost httppost = new HttpPost(hostpath);
          HttpEntity entity = new SerializableEntity(request, true);
          httppost.setEntity(entity);
          HttpClient httpclient = new DefaultHttpClient();
          HttpResponse httpresp = httpclient.execute(httppost);
          InputStream content = httpresp.getEntity().getContent();
          ObjectInputStream oinput = new ObjectInputStream(content);
          Object result = oinput.readObject();
          return result;
        }
      });
  }

  /**
   * Retorna
   * 
   * @return instance
   */
  public static WSClientConfig getConfig() {
    return config;
  }

}
