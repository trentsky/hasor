/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.rsf.adapter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import net.hasor.rsf.RsfBindInfo;
import net.hasor.rsf.RsfClient;
import net.hasor.rsf.RsfContext;
import net.hasor.rsf.RsfFuture;
import net.hasor.rsf.RsfRequest;
import net.hasor.rsf.RsfResponse;
import net.hasor.rsf.utils.RuntimeUtils;
import org.more.classcode.delegate.faces.MethodClassConfig;
import org.more.classcode.delegate.faces.MethodDelegate;
import org.more.future.FutureCallback;
/**
 * 负责维持与远程RSF服务器连接的客户端类，并同时负责维护request/response。
 * @version : 2014年9月12日
 * @author 赵永春(zyc@hasor.net)
 */
public abstract class AbstractfRsfClient implements RsfClient {
    /**获取{@link RsfContext}*/
    public abstract AbstractRsfContext getRsfContext();
    /**获取正在进行中的调用请求。*/
    public abstract RsfFuture getRequest(long requestID);
    /**发送连接请求。*/
    public abstract RsfFuture sendRequest(RsfRequest rsfRequest, FutureCallback<RsfResponse> listener);
    /**尝试再次发送Request请求（如果request已经超时则无效）。*/
    public abstract void tryAgain(long requestID);
    /**响应挂起的Request请求。*/
    public abstract void putResponse(long requestID, RsfResponse response);
    /**响应挂起的Request请求。*/
    public abstract void putResponse(long requestID, Throwable rsfException);
    //
    //
    //
    //
    //
    //
    private Map<String, Class<?>> wrapperMap = new ConcurrentHashMap<String, Class<?>>();
    /**获取远程服务对象*/
    public <T> T getRemote(String serviceID) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
        RsfBindInfo<T> bindInfo = this.getRsfContext().getBindCenter().getService(serviceID);
        if (bindInfo == null)
            return null;
        return this.wrapper(bindInfo, bindInfo.getBindType());
    }
    /**获取远程服务对象*/
    public <T> T getRemote(String group, String name, String version) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
        RsfBindInfo<T> bindInfo = this.getRsfContext().getBindCenter().getService(group, name, version);
        if (bindInfo == null)
            return null;
        return this.wrapper(bindInfo, bindInfo.getBindType());
    }
    /**将服务包装为另外一个接口。*/
    public <T> T wrapper(String serviceID, Class<T> interFace) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
        RsfBindInfo<T> bindInfo = this.getRsfContext().getBindCenter().getService(serviceID);
        if (bindInfo == null)
            return null;
        return this.wrapper(bindInfo, interFace);
    }
    /**将服务包装为另外一个接口。*/
    public <T> T wrapper(String group, String name, String version, Class<T> interFace) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
        RsfBindInfo<T> bindInfo = this.getRsfContext().getBindCenter().getService(group, name, version);
        if (bindInfo == null)
            return null;
        return this.wrapper(bindInfo, interFace);
    }
    /**将服务包装为另外一个接口。*/
    public <T> T wrapper(RsfBindInfo<?> bindInfo, Class<T> interFace) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
        if (interFace.isInterface() == false)
            throw new UnsupportedOperationException("interFace parameter must be an interFace.");
        Class<?> wrapperType = this.wrapperMap.get(bindInfo.getBindID());
        if (wrapperType == null) {
            MethodClassConfig mcc = new MethodClassConfig();
            mcc.addDelegate(interFace, new RemoteWrapper(bindInfo, this));
            wrapperType = mcc.toClass();
            this.wrapperMap.put(bindInfo.getBindID(), wrapperType);
        }
        return (T) wrapperType.newInstance();
    }
    //
    /**同步方式调用远程服务。*/
    public Object syncInvoke(RsfBindInfo<?> bindInfo, String methodName, Class<?>[] parameterTypes, Object[] parameterObjects) throws Throwable {
        //1.准备Request
        int timeout = validateTimeout(bindInfo.getClientTimeout());
        RsfRequest request = RuntimeUtils.buildRequest(bindInfo, this, methodName, parameterTypes, parameterObjects);
        //2.发起Request
        RsfFuture rsfFuture = this.sendRequest(request, null);
        //3.返回数据
        RsfResponse response = rsfFuture.get(timeout, TimeUnit.MILLISECONDS);
        return response.getResponseData();
    }
    /**异步方式调用远程服务。*/
    public RsfFuture asyncInvoke(RsfBindInfo<?> bindInfo, String methodName, Class<?>[] parameterTypes, Object[] parameterObjects) {
        //1.准备Request
        RsfRequest request = RuntimeUtils.buildRequest(bindInfo, this, methodName, parameterTypes, parameterObjects);
        //2.发起Request
        return this.sendRequest(request, null);
    }
    /**以回调方式调用远程服务。*/
    public void doCallBackInvoke(RsfBindInfo<?> bindInfo, String methodName, Class<?>[] parameterTypes, Object[] parameterObjects, final FutureCallback<Object> listener) {
        this.doCallBackRequest(bindInfo, methodName, parameterTypes, parameterObjects, new FutureCallback<RsfResponse>() {
            public void completed(RsfResponse result) {
                listener.completed(result.getResponseData());
            }
            public void failed(Throwable ex) {
                listener.failed(ex);
            }
            public void cancelled() {
                listener.cancelled();
            }
        });
    }
    /**以回调方式调用远程服务。*/
    public void doCallBackRequest(RsfBindInfo<?> bindInfo, String methodName, Class<?>[] parameterTypes, Object[] parameterObjects, final FutureCallback<RsfResponse> listener) {
        //1.准备Request
        RsfRequest request = RuntimeUtils.buildRequest(bindInfo, this, methodName, parameterTypes, parameterObjects);
        //2.发起Request
        this.sendRequest(request, listener);
    }
    //
    private int validateTimeout(int timeout) {
        if (timeout <= 0)
            timeout = this.getRsfContext().getSettings().getDefaultTimeout();
        return timeout;
    }
    private static class RemoteWrapper implements MethodDelegate {
        private RsfBindInfo<?> bindInfo = null;
        private RsfClient      client   = null;
        //
        public RemoteWrapper(RsfBindInfo<?> bindInfo, RsfClient client) {
            this.bindInfo = bindInfo;
            this.client = client;
        }
        public Object invoke(Method callMethod, Object target, Object[] params) throws Throwable {
            return this.client.syncInvoke(this.bindInfo, callMethod.getName(), callMethod.getParameterTypes(), params);
        }
    }
}