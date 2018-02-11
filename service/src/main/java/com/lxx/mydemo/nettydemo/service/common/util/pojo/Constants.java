package com.lxx.mydemo.nettydemo.service.common.util.pojo;

import com.google.common.base.Splitter;
import com.lxx.mydemo.nettydemo.service.common.util.VersionUtil;

public interface Constants {

    String VERSION = VersionUtil.getVersion(Constants.class, "");

    String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
    String CONTENT_TYPE_TEXT = "text/plain;charset=UTF-8";

    String HEADER_X_REAL_IP = "X-Real-IP";
    String HEADER_VERSION = "Q-Version";
    String HEADER_SERVER_TOKEN = "Q-Server-Token";
    String HEADER_APP_CODE = "Q-App-Code";

    String URL_PBSERVICE = "appcenter.inf.prd.sp";
    String URL_PBSERVICE_BETA = "10.32.64.12:9001";
    String URL_PBSERVICE_DEV = "172.16.100.155:9001";

    // protected
    String KEY_PBSERVICE = "pbservice.host";
    String KEY_MONITOR = "monitor.host";
    String MONITOR_DEFAULT = "g.benmu-health.org:2003";
    String KEY_GRAFANA = "grafana.host";
    String KEY_GRAFANA_SECRET = "grafana.secret";
    String GRAFANA_DEFAULT = "http://g.benmu-health.org:3000";
    String GRAFANA_SECRET_DEFAULT = "eyJrIjoiRVVHaldmQzZicjVXaXcxSXRBTGRUTzR6R3lkdHM0SDciLCJuIjoiUm9ib3RBZG1pbiIsImlkIjoxfQ==";
    String CENTER_LOCAL = "#";

    char STX = (char) 2; // start text
    char ETX = (char) 3; // end text
    char FF = '\r';
    char CR = '\n';

    Splitter SPLIT_CR = Splitter.on(CR).trimResults();
    Splitter SPLIT_COMMA = Splitter.on(',').trimResults();
    Splitter SPLIT_TAB = Splitter.on('\t').trimResults();
}
