package com.amazonaws.fps.spring;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.amazonaws.utils.PropertyBundle;
import com.amazonaws.utils.PropertyKeys;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Bean to intercept the poor configuration handling of the Amazon FPS API
 * 
 */
public class AmazonFpsInterceptorBean {

	public AmazonFpsInterceptorBean(Environment environment) {
		try {
			Properties proxyProperties = (Properties) Enhancer.create(Properties.class, new PropertiesPropertyBundleProxy(environment));
			Field field = PropertyBundle.class.getDeclaredField("properties");
			field.setAccessible(true);
			field.set(null, proxyProperties);
		} catch (Exception e) {
			throw new BeanInitializationException("Failed to create interceptor bean", e);
		}
	}

	public static class PropertiesPropertyBundleProxy implements MethodInterceptor {
		
		private static final Map<String, String> ENVIRONMENT_MAP = new HashMap<String, String>() {{
			put(PropertyKeys.AWS_ACCESS_KEY.value(), "aws.accessKey");
			put(PropertyKeys.AWS_SECRET_KEY.value(), "aws.secretKey");
			put(PropertyKeys.CBUI_SERVICE_END_POINT.value(), "aws.cbui.endPoint");
			put(PropertyKeys.AWS_SERVICE_END_POINT.value(), "aws.fps.endPoint");
		}};
		
		private static final Map<String, String> DEFAULTS_MAP = new HashMap<String, String>() {{
			put(PropertyKeys.PROXY_HOST.value(), "");
			put(PropertyKeys.PROXY_PORT.value(), "-1");
			put(PropertyKeys.PROXY_USER_NAME.value(), "");
			put(PropertyKeys.PROXY_PASSWORD.value(), "");
			put(PropertyKeys.MAX_ERROR_RETRY.value(), "3");
			put(PropertyKeys.MAX_CONNECTIONS.value(), "300");
		}};

		private Environment environment;

		public PropertiesPropertyBundleProxy(Environment environment) {
			this.environment = environment;
		}

		@Override
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
			if (method.getName().equals("getProperty")) {
				return this.getSpringProperty((String)args[0]);
			}
			
			return proxy.invokeSuper(obj, args);
		}
		
		private String getSpringProperty(String what) {
			if (ENVIRONMENT_MAP.containsKey(what)) {
				return this.environment.getProperty(ENVIRONMENT_MAP.get(what));
			}
			
			return DEFAULTS_MAP.get(what);
		}

	}

}
