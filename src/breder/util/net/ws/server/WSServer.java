package breder.util.net.ws.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Map;

import breder.util.net.ws.shared.WSInputStream;
import breder.util.net.ws.shared.WSRequest;

/**
 * Servlet de WebService
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class WSServer {

  /** Contextos */
  private static final ThreadLocal<WSServerContext> contexts =
    new ThreadLocal<WSServerContext>();
  /** Mapa de contextos */
  private static final Map<String, WSServerContext> contextMap =
    new Hashtable<String, WSServerContext>();

  /**
   * Aplica o serviço
   * 
   * @param config
   * @param session
   * @param input
   * @param output
   * @throws IOException
   */
  public static void service(WSServerConfig config, String session,
    InputStream input, OutputStream output) throws IOException {
    ObjectOutputStream ooutput = new ObjectOutputStream(output);
    contexts.set(getContext(config, session));
    try {
      Object result = execute(config, input);
      ooutput.writeObject(result);
    }
    catch (Throwable e) {
      ooutput.writeObject(e);
    }
    finally {
      contexts.remove();
    }
    ooutput.close();
  }

  /**
   * @param config
   * @param session
   * @return contexto
   */
  public static WSServerContext getContext(WSServerConfig config, String session) {
    WSServerContext context = contextMap.get(session);
    if (context == null || context.getUpdateTime() > config.getTimeoutSession()) {
      contextMap.put(session, context = new WSServerContext());
    }
    else {
      context.refreshUpdate();
    }
    return context;
  }

  /**
   * Executa o serviço
   * 
   * @param config
   * 
   * @param input
   * @return retorno do serviço
   * @throws Exception
   */
  private static Object execute(WSServerConfig config, InputStream input)
    throws Exception {
    ObjectInputStream oinput = new ObjectInputStream(input);
    WSRequest request = (WSRequest) oinput.readObject();
    for (int n = 0; n < request.getObjectParameters().length; n++) {
      Object arg = request.getObjectParameters()[n];
      if (arg.getClass() == WSInputStream.class) {
        request.getObjectParameters()[n] =
          new ByteArrayInputStream(((WSInputStream) arg).getBytes());
      }
    }
    Object service = config.getService(request.getClassname());
    return service.getClass().getMethod(request.getMethodname(),
      request.getClassParameters()).invoke(service,
      request.getObjectParameters());
  }

  /**
   * Retorna o context do servidor
   * 
   * @return contexto
   */
  public static WSServerContext getContext() {
    return contexts.get();
  }

  /**
   * Retorna o context do servidor
   * 
   * @param session
   * @return contexto
   */
  public static WSServerContext getContext(String session) {
    WSServerContext context = contextMap.get(session);
    if (context == null) {
      context = getContext(new WSServerConfig(), session);
    }
    contexts.set(context);
    return context;
  }

  /**
   * Retorna o context do servidor
   */
  public static void removeContext() {
    contexts.remove();
  }

  /**
   * Retorna o context do servidor
   * 
   * @param session
   */
  public static void removeContext(String session) {
    contextMap.remove(session);
  }

  /**
   * Retorna o context do servidor
   * 
   * @param session
   */
  public static void setContext(String session) {
    contexts.set(contextMap.get(session));
  }

}
