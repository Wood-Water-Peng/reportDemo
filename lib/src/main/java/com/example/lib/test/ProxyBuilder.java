package com.example.lib.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @Author jacky.peng
 * @Date 2021/4/8 7:03 PM
 * @Version 1.0
 */
public class ProxyBuilder {
    public <T extends TopStream> T build(Class<? extends TopStream> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new MyInvocationHandler(clazz));
    }

    class MyInvocationHandler implements InvocationHandler {
        private final Class<? extends TopStream> mClass;

        MyInvocationHandler(Class<? extends TopStream> mClass) {
            this.mClass = mClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Map<Class<? extends TopStream>, Class<? extends TopStream>> httpCommParamMap = TopStreamManager.getInstance().mMapDefaultStreamClass;
            TopStream commParam = null;
            Class<? extends TopStream> clazzImpl = httpCommParamMap.get(mClass);
            if (clazzImpl != null) {
                commParam = clazzImpl.newInstance();
            }
            if (commParam != null) {
                return method.invoke(commParam, args);
            }
            return null;
        }
    }
}
