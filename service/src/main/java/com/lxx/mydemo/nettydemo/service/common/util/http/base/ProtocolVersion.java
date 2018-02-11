package com.lxx.mydemo.nettydemo.service.common.util.http.base;

/**
 * @author zhenwei.liu
 * @since 2016-12-20
 */
public class ProtocolVersion {

    public static final ProtocolVersion HTTP_1_1 = new ProtocolVersion("http", 1, 1);
    public static final ProtocolVersion HTTPS_1_1 = new ProtocolVersion("https", 1, 1);
    public static final ProtocolVersion HTTP_1_0 = new ProtocolVersion("http", 1, 0);
    public static final ProtocolVersion HTTPS_1_0 = new ProtocolVersion("https", 1, 0);

    private final String protocol;
    private final int major;
    private final int minor;

    public ProtocolVersion(String protocol, int major, int minor) {
        this.protocol = protocol;
        this.major = major;
        this.minor = minor;
    }

    public String getProtocol() {
        return protocol;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }
}
