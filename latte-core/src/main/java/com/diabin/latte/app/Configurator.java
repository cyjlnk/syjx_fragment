package com.diabin.latte.app;


import android.os.Handler;
import android.support.annotation.NonNull;

import com.diabin.latte.delegates.web.event.Event;
import com.diabin.latte.delegates.web.event.EventManager;
import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Interceptor;

/**
 * Created by fei on 2017/7/30.
 */

public class Configurator {

    private static final HashMap<Object,Object> LATTE_CONFIGS=new HashMap<>();
    private static final ArrayList<IconFontDescriptor> ICONS =new ArrayList<>();
    private static final ArrayList<Interceptor> INTERCEPTORS=new ArrayList<>();
    private static final Handler HANDLER = new Handler();

    private Configurator(){
        LATTE_CONFIGS.put(ConfigType.CONFIG_READY,false);
        LATTE_CONFIGS.put(ConfigType.HANDLER, HANDLER);
    }

    public static Configurator getInstance(){
        return Holder.INSTANCE;
    }

    final HashMap<Object,Object> getLatteConfigs(){
        return LATTE_CONFIGS;
    }

    private static class Holder{
        private static final Configurator INSTANCE=new Configurator();
    }

    public final void configure(){
        initIcons();
        LATTE_CONFIGS.put(ConfigType.CONFIG_READY,true);
    }

    public final Configurator withApiHost(String host){
        LATTE_CONFIGS.put(ConfigType.API_HOST,host);
        return this;
    }

    public Configurator withWebEvent(@NonNull String name, @NonNull Event event) {
        final EventManager manager = EventManager.getInstance();
        manager.addEvent(name, event);
        return this;
    }

    //浏览器加载的HOST
    public Configurator withWebHost(String host) {
        LATTE_CONFIGS.put(ConfigType.WEB_HOST, host);
        return this;
    }

    public Configurator withJavascriptInterface(@NonNull String name) {
        LATTE_CONFIGS.put(ConfigType.JAVASCRIPT_INTERFACE, name);
        return this;
    }

    public final Configurator withLoaderDelayed(long delayed) {
        LATTE_CONFIGS.put(ConfigType.LOADER_DELAYED, delayed);
        return this;
    }

    public final Configurator withInterceptor(Interceptor interceptor){
        INTERCEPTORS.add(interceptor);
        LATTE_CONFIGS.put(ConfigType.INTERCEPTOR,INTERCEPTORS);
        return this;
    }

    public final Configurator withInterceptors(ArrayList<Interceptor> interceptors){
        INTERCEPTORS.addAll(interceptors);
        LATTE_CONFIGS.put(ConfigType.INTERCEPTOR,INTERCEPTORS);
        return this;
    }

    private void initIcons(){
        if(ICONS.size()>0){
            final Iconify.IconifyInitializer initializer=Iconify.with(ICONS.get(0));
            for(int i=1;i<ICONS.size();i++){
                initializer.with(ICONS.get(i));
            }
        }
    }

    public final Configurator withIcon(IconFontDescriptor descriptor){
        ICONS.add(descriptor);
        return this;
    }

    private void checkConfiguration(){
        final boolean isReady= (boolean) LATTE_CONFIGS.get(ConfigType.CONFIG_READY);
        if(!isReady){
            throw new RuntimeException("Configuration is not ready,call configure");
        }
    }


    final <T> T getConfiguration(Object key){
        checkConfiguration();
        final Object value=LATTE_CONFIGS.get(key);
        if(value==null){
            throw new NullPointerException(key.toString()+"IS NULL");
        }
        return (T) LATTE_CONFIGS.get(key);
    }

}
