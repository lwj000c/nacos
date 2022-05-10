/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.nacos.naming.pojo;

import com.alibaba.nacos.core.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Cluster info.
 *
 * @author caogu.wyp
 * @version $Id: ClusterInfo.java, v 0.1 2018-09-17 上午11:36 caogu.wyp Exp $$
 */

public class ServiceInfo implements Serializable {

    public String namespaceId;

    public String agent;

    public String clusters;

    public String clientIP;
    public int udpPort;
    public String env;
    public boolean isCheck;
    public String app;
    public String tenant;
    public boolean healthyOnly;
    public List<String> serviceNameList ;
    public String groupName;

    public String getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(String namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getClusters() {
        return clusters;
    }

    public void setClusters(String clusters) {
        this.clusters = clusters;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public boolean isHealthyOnly() {
        return healthyOnly;
    }

    public void setHealthyOnly(boolean healthyOnly) {
        this.healthyOnly = healthyOnly;
    }

    public List<String> getServiceNameList() {
        return serviceNameList;
    }

    public void setServiceNameList(List<String> serviceNameList) {
        this.serviceNameList = serviceNameList;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "ServiceInfo{" +
            "namespaceId='" + namespaceId + '\'' +
            ", agent='" + agent + '\'' +
            ", clusters='" + clusters + '\'' +
            ", clientIP='" + clientIP + '\'' +
            ", udpPort=" + udpPort +
            ", env='" + env + '\'' +
            ", isCheck=" + isCheck +
            ", app='" + app + '\'' +
            ", tenant='" + tenant + '\'' +
            ", healthyOnly=" + healthyOnly +
            ", serviceNameList=" + serviceNameList +
            ", groupName='" + groupName + '\'' +
            '}';
    }
}
