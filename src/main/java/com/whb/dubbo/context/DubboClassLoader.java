package com.whb.dubbo.context;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.whb.dubbo.exception.RRException;
import com.whb.dubbo.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 自定义类加载器，做沙箱隔离.
 */
@Slf4j
public class DubboClassLoader extends ClassLoader {

    private final String path;

    private static Map<String, byte[]> classMap = new ConcurrentHashMap<>();


    /**
     * 不使用双亲委派机制
     */
    public DubboClassLoader(String path) {
        super(null);
        this.path = path;
    }

    /**
     * 扫描Jar包
     *
     * @param file
     * @throws Exception
     */
    private void scanJarFile(File file) throws Exception {
        JarFile jar = new JarFile(file);

        Enumeration<JarEntry> en = jar.entries();
        while (en.hasMoreElements()) {
            JarEntry je = en.nextElement();
            je.getName();
            String name = je.getName();
            if (name.endsWith(".class")) {

                String className = makeClassName(name);

                try (InputStream input = jar.getInputStream(je); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];
                    int bytesNumRead;
                    while ((bytesNumRead = input.read(buffer)) != -1) {
                        baos.write(buffer, 0, bytesNumRead);
                    }
                    addClass(className, baos.toByteArray());
                }
            }
        }
        jar.close();
    }

    private String makeClassName(String name) {
        String ret = name.replace("\\", ".")
                .replace("/", ".")
                .replace(".class", "");
        return ret;
    }

    /**
     * 从指定路径加载Jar包
     */
    public void loadJars() throws Exception {

        if (StringUtils.isEmpty(path)) {
            throw new RRException(StringUtil.format("can't found the path {}", path));
        }

        File libPath = new File(path);
        if (!libPath.exists()) {
            throw new RRException(StringUtil.format("the path[{}] is not exists.", path));
        }

        File[] files = libPath.listFiles((dir, name) -> name.endsWith(".jar") || name.endsWith(".zip"));

        if (files != null) {
            for (File file : files) {
                scanJarFile(file);
            }
        }
    }

    /**
     * 动态添加一个类
     */
    public static boolean addClass(String className, byte[] byteCode) {
        if (!classMap.containsKey(className)) {
            classMap.put(className, byteCode);
            return true;
        }
        return false;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        name = makeClassName(name);

        byte[] stream = get(name);

        if (null != stream) {

            return defineClass(name, stream, 0, stream.length);
        }

        return super.loadClass(name, resolve);

    }

    /**
     * 从自定义类加载中加载类
     */
    public static Class<?> getClass(String name) throws ClassNotFoundException {
        return new DubboClassLoader("").loadClass(name, false);
    }

    private static byte[] get(String className) {
        return classMap.getOrDefault(className, null);
    }

    /**
     * 扫描Class文件
     *
     * @param file
     */
    private void scanClassFile(File file) {
        if (file.exists()) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                try {
                    byte[] byteCode = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
                    String className = file.getAbsolutePath().replace(this.path, "")
                            .replace(File.separator, ".");

                    className = makeClassName(className);

                    addClass(className, byteCode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (file.isDirectory()) {
                for (File f : Objects.requireNonNull(file.listFiles())) {
                    scanClassFile(f);
                }
            }
        }
    }

    /**
     * 从指定路径加载Class文件
     */
    public void loadClassFile() {
        File[] files = new File(path).listFiles();
        if (files != null) {
            for (File file : files) {
                scanClassFile(file);
            }
        }
    }

    /**
     * 清空缓存
     */
    public void clearCache() {
        classMap.clear();
    }
}
