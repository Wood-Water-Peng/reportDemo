package com.example.lib.test;

import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TopStreamManager {
    private static final TopStreamManager INSTANCE = new TopStreamManager();
    protected final Map<Class<? extends TopStream>, Class<? extends TopStream>> mMapDefaultStreamClass = new ConcurrentHashMap<>();
    private TopStreamManager()
    {
    }

    public static TopStreamManager getInstance()
    {
        return INSTANCE;
    }
    /**
     * 注册默认的流接口实现类
     * <p>
     *
     * @param clazz
     */
    public synchronized void registerDefaultStream(Class<? extends TopStream> clazz)
    {
        checkProxyClass(clazz);
        checkFStreamClass(clazz);

        final Set<Class<? extends TopStream>> set = findAllStreamClass(clazz, false);
        if (set.isEmpty())
            throw new IllegalArgumentException("stream class was not found in " + clazz);

        for (Class<? extends TopStream> item : set)
        {
            mMapDefaultStreamClass.put(item, clazz);
        }
    }

    private Set<Class<? extends TopStream>> findAllStreamClass(Class<?> clazz, boolean getOne)
    {
        final Set<Class<? extends TopStream>> set = new HashSet<>();

        while (true)
        {
            if (clazz == null)
                break;
            if (!TopStream.class.isAssignableFrom(clazz))
                break;
            if (clazz.isInterface())
                throw new RuntimeException("clazz must not be an interface");

            for (Class<?> item : clazz.getInterfaces())
            {
                if (TopStream.class.isAssignableFrom(item) && TopStream.class != item)
                {
                    set.add((Class<? extends TopStream>) item);

                    if (getOne && set.size() > 0)
                        return set;
                }
            }

            clazz = clazz.getSuperclass();
        }

        return set;
    }

    //---------- default stream end ----------

    private static void checkProxyClass(Class<?> clazz)
    {
        if (Proxy.isProxyClass(clazz))
            throw new IllegalArgumentException("proxy class is not supported");
    }

    private static void checkFStreamClass(Class<?> clazz)
    {
        if (clazz == TopStream.class)
            throw new IllegalArgumentException("class must not be " + TopStream.class);
    }
}
