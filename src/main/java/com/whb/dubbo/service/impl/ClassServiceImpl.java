package com.whb.dubbo.service.impl;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.whb.dubbo.cache.MethodCaches;
import com.whb.dubbo.cache.UrlCaches;
import com.whb.dubbo.context.Constant;
import com.whb.dubbo.context.DoeClassLoader;
import com.whb.dubbo.dto.ConnectDTO;
import com.whb.dubbo.dto.MethodModelDTO;
import com.whb.dubbo.dto.ResultDTO;
import com.whb.dubbo.exception.RRException;
import com.whb.dubbo.model.MethodModel;
import com.whb.dubbo.model.UrlModel;
import com.whb.dubbo.service.ClassService;
import com.whb.dubbo.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author Joey
 * @date 2018/6/28 11:32
 */
@Service("classService")
@Slf4j
public class ClassServiceImpl implements ClassService {


    @Override
    @Cacheable(value = Constant.DOE_CACHE_PREFIX, key = "#dto.serviceName")
    public List<MethodModelDTO> listMethods(ConnectDTO dto) {

        log.info("begin to invoke listMethods({})", JSON.toJSONString(dto));

        String interfaceName = dto.getServiceName();

        if (StringUtils.isEmpty(interfaceName)) {

            // get provider
            UrlModel provider = UrlCaches.get(dto.getProviderKey());
            if (null == provider) {
                throw new RRException(StringUtil.format("can't found the provider key {}.", dto.getProviderKey()));
            }
            interfaceName = provider.getUrl().getParameter(Constants.INTERFACE_KEY);
        }

        if (StringUtils.isEmpty(interfaceName)) {
            throw new RRException("interface name and provider cache key can't both be blank.");
        }

        try {

            // show only public method
            // Class<?> clazz = Class.forName(interfaceName);
            // load classes without affect system class since v1.2.0
            Class<?> clazz = DoeClassLoader.getClass(interfaceName);
            Method[] methods = clazz.getMethods();
            // convert and cache method object associate witch the unique key
            return MethodCaches.cache(interfaceName, methods);

        } catch (ClassNotFoundException e) {
            throw new RRException("can't found the interface from classpath, please add the dependency first.");
        }

    }

    /**
     * generate the simple json string of the method parameters.
     *
     * @param dto
     * @return
     */
    @Override
    public ResultDTO<String> generateMethodParamsJsonString(@NotNull MethodModelDTO dto) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        Method method = null;

        // get method from cache
        if (StringUtils.isNotEmpty(dto.getMethodKey())) {
            MethodModel model = MethodCaches.get(dto.getMethodKey());
            method = (null == model) ? null : model.getMethod();
        }
        // search from classpath
        if (null == method && StringUtils.isNotEmpty(dto.getInterfaceName()) && StringUtils.isNotEmpty(dto.getMethodName())) {
            method = getMethodByName(dto.getInterfaceName(), dto.getMethodName());
        }
        if (null == method) {
            return ResultDTO.createErrorResult(
                    StringUtil.format("can't find the method[{}] in the interface[{}]",
                            dto.getInterfaceName(), dto.getMethodName()), String.class);
        }

        List<Object> objects = new ArrayList<>();
        for (Parameter param : method.getParameters()) {

            objects.add(initObject(param.getType(), param.getParameterizedType()));

        }

        String json = JSON.toJSONString(objects, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
        return ResultDTO.createSuccessResult("SUCCESS", json, String.class);
    }

    /**
     * we must to make sure we have one no parameter constructor in our class.
     *
     * @param clazz
     * @param type
     * @return
     */
    private Object initObject(Class<?> clazz, Type type) throws IllegalAccessException, InstantiationException {

        log.debug("begin to init {}", clazz.getTypeName());

        if (clazz == Integer.class || clazz == int.class) {

            return RandomUtils.nextInt(0, 1000);

        } else if (clazz == String.class) {

            String base = UUID.randomUUID().toString();
            return base.substring(RandomUtils.nextInt(1, base.length()));

        } else if (clazz == Long.class || clazz == long.class) {

            return RandomUtils.nextLong(0, 1000);

        } else if (clazz == Short.class || clazz == short.class) {

            return RandomUtils.nextInt(0, 100);

        } else if (clazz == Date.class) {

            return new Date();

        } else if (clazz == List.class) {

            if (null != type) {
                return initArrayList(type);
            }

        } else if (clazz == Map.class) {

            return new HashMap<>();

        } else if (clazz == Set.class) {

            return new HashSet<>();

        }

        Object ret;
        try {
            ret = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.debug("you should define one no parameter constructor.", e);
            return new Object();
        }

        List<Field> fields = new ArrayList<>();
        ReflectionUtils.doWithFields(clazz, fields::add);
        for (Field field : fields) {

            field.setAccessible(true);
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            if(isStatic) {
                continue;
            }
            try {
                field.set(ret, initObject(field.getType(), field.getGenericType()));
            } catch (Exception e) {
                log.debug("can't set value for the field, you should complete the method initObject(Class<?> clazz, Type type) later.", e);
            }
        }

        return ret;
    }

    private List<Object> initArrayList(Type genericType) throws InstantiationException, IllegalAccessException {

        List<Object> list = new ArrayList<>();

        if (genericType == null) {
            return list;
        }
        // 如果是泛型参数的类型
        if (genericType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) genericType;
            // 得到泛型里的class类型对象
            Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
            list.add(initObject(genericClazz, null)); // too deep...
        }
        return list;
    }

    /**
     * we will get wrong result if there are overload method in the interface.
     *
     * @param interfaceName
     * @param methodName
     * @return
     * @throws ClassNotFoundException
     */
    private Method getMethodByName(String interfaceName, String methodName) throws ClassNotFoundException {

        Class<?> clazz = Class.forName(interfaceName);

        for (Method method : clazz.getDeclaredMethods()) {
            if (methodName.equals(method.getName())) {
                return method;
            }
        }
        return null;
    }
}
