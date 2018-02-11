package com.lxx.mydemo.nettydemo.service.common.util.http.support;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.net.URI;

public class HttpConstants {

    public static final String CONTEXT_SCOPE = "context_scope";

    public static boolean isIntranet(URI uri) {
        String hostname = uri.getHost();

        if (Strings.isNullOrEmpty(hostname)) {
            return false;
        }

        // local
        if (hostname.equals("localhost") || hostname.equals("127.0.0.1")) {
            return true;
        }

        // dev
        if (Strings.isNullOrEmpty(hostname) || hostname.contains(".dev.") || hostname.contains(".tst.")) {
            return true;
        }
        // beta
        if (hostname.contains(".beta.")) {
            return true;
        }
        // prod
        if (hostname.contains(".prd.") || hostname.endsWith("benmu-health.com") || hostname.endsWith("benmu-health.org")) {
            return true;
        }
        // ip
        if (hostname.startsWith("192.168") || hostname.startsWith("10.86")) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Preconditions.checkArgument(isIntranet(URI.create("http://config.benmu-health.com")));
        Preconditions.checkArgument(isIntranet(URI.create("http://config.benmu-health.org")));
        Preconditions.checkArgument(isIntranet(URI.create("http://beta.qschedule.corp.qunar.com/")));
        Preconditions.checkArgument(isIntranet(URI.create("http://dev.qschedule.corp.qunar.com/")));
        Preconditions.checkArgument(!isIntranet(URI.create("http://www.qunar.com/")));
        Preconditions.checkArgument(!isIntranet(URI.create("http://www.qq.com/")));
        Preconditions.checkArgument(!isIntranet(URI.create("http://www.sina.com/")));
        Preconditions.checkArgument(!isIntranet(URI.create("http://199.2.33.1/")));
        Preconditions.checkArgument(isIntranet(URI.create("http://192.168.1.1/")));
        Preconditions.checkArgument(isIntranet(URI.create("http://192.167.1.1/")));
        Preconditions.checkArgument(isIntranet(URI.create("http://10.86.167.1.1/")));
    }
}
