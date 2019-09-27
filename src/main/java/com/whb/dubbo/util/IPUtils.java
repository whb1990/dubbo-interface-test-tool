package com.whb.dubbo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.management.*;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

/**
 * IP工具类
 */
@Slf4j
public class IPUtils {
    /**
     * 获取IP地址
     * <p>
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("IPUtils ERROR ", e);
        }

//        //使用代理，则获取第一个IP地址
//        if(StringUtils.isEmpty(ip) && ip.length() > 15) {
//			if(ip.indexOf(",") > 0) {
//				ip = ip.substring(0, ip.indexOf(","));
//			}
//		}

        return ip;
    }

    /**
     * 判断是否windows系统
     *
     * @return
     */
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    /**
     * 获取本机Ip地址（如果本机vmnet1跟vmnet8都是启用状态，则优先获取vmnet1或者vmnet8的地址）
     *
     * @return
     * @throws UnknownHostException
     * @throws SocketException
     */
    public static String getLocalIP()
            throws UnknownHostException, SocketException {
        if (isWindowsOS()) {
            InetAddress address = InetAddress.getLocalHost();
            String serviseIp = address.getHostAddress();
            return serviseIp;
        }
        return getLinuxLocalIP();
    }

    /**
     * 获取本地主机名
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getLocalHostName()
            throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        return address.getHostName();
    }

    /**
     * 获取本地Linux系统Ip地址
     *
     * @return
     * @throws SocketException
     */
    private static String getLinuxLocalIP()
            throws SocketException {
        String ip = "";
        for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = (NetworkInterface) en.nextElement();
            String name = intf.getName();
            if ((!(name.contains("docker"))) && (!(name.contains("lo")))) {
                for (Enumeration enumeration = intf.getInetAddresses(); enumeration.hasMoreElements(); ) {
                    InetAddress address = (InetAddress) enumeration.nextElement();
                    if (!(address.isLoopbackAddress())) {
                        String ipAddress = address.getHostAddress().toString();
                        if ((!(ipAddress.contains("::"))) && (!(ipAddress.contains("0:0:"))) && (!(ipAddress.contains("fe80")))) {
                            ip = ipAddress;
                        }
                    }
                }
            }
        }
        return ip;
    }

    /**
     * 获取端口号
     *
     * @return
     * @throws AttributeNotFoundException
     * @throws InstanceNotFoundException
     * @throws MBeanException
     * @throws ReflectionException
     */
    public static String getServerPort()
            throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException {
        MBeanServer mBeanServer = null;
        if (MBeanServerFactory.findMBeanServer(null).size() > 0) {
            mBeanServer = (MBeanServer) MBeanServerFactory.findMBeanServer(null).get(0);
        }

        if (mBeanServer == null) {
            System.out.println("调用findMBeanServer查询到的结果为null");
            return "";
        }

        Set names = null;
        try {
            names = mBeanServer.queryNames(new ObjectName("Catalina:type=Connector,*"), null);
        } catch (Exception e) {
            return "";
        }
        Iterator it = names.iterator();
        ObjectName oname = null;
        while (it.hasNext()) {
            oname = (ObjectName) it.next();
            String protocol = (String) mBeanServer.getAttribute(oname, "protocol");
            String scheme = (String) mBeanServer.getAttribute(oname, "scheme");
            Boolean secureValue = (Boolean) mBeanServer.getAttribute(oname, "secure");
            Boolean SSLEnabled = (Boolean) mBeanServer.getAttribute(oname, "SSLEnabled");

            if ((SSLEnabled != null) && (SSLEnabled.booleanValue())) {
                secureValue = Boolean.valueOf(true);
                scheme = "https";
            }
            if ((protocol != null) && ((("HTTP/1.1".equals(protocol)) || (protocol.contains("http"))))) {
                if (("https".equals(scheme)) && (secureValue.booleanValue())) {
                    return ((Integer) mBeanServer.getAttribute(oname, "port")).toString();
                }
                if ((!("https".equals(scheme))) && (!(secureValue.booleanValue()))) {
                    return ((Integer) mBeanServer.getAttribute(oname, "port")).toString();
                }
            }
        }
        return "";
    }

    public static void main(String[] args) throws Exception {
        try {
            String ip = getLocalIP();
            String linuxIp = getLinuxLocalIP();
            String hostName = getLocalHostName();
            String port = getServerPort();
            System.out.println("hostName:" + hostName + ",ip === " + ip + ",linuxIp:" + linuxIp + "port:" + port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
