package com.alibaba.nacos.naming.controllers;

import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.naming.CommonParams;
import com.alibaba.nacos.common.utils.JacksonUtils;
import com.alibaba.nacos.core.auth.ActionTypes;
import com.alibaba.nacos.core.auth.Secured;
import com.alibaba.nacos.core.utils.WebUtils;
import com.alibaba.nacos.naming.core.ServiceManager;
import com.alibaba.nacos.naming.extend.UploadServiceProcessor;
import com.alibaba.nacos.naming.misc.UtilsAndCommons;
import com.alibaba.nacos.naming.web.NamingResourceParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


@RestController
@RequestMapping(UtilsAndCommons.NACOS_NAMING_CONTEXT + "/instances/batch")
public class InstanceBatchController {
//    private final ServiceManager serviceManager;

    private final InstanceController instanceController;

    @Autowired
    private UploadServiceProcessor uploadServiceProcessor;

    @Autowired
    public InstanceBatchController(InstanceController instanceController) {
//        this.serviceManager = serviceManager;
        this.instanceController = instanceController;
    }

    @PostMapping("/list")
    @Secured(parser = NamingResourceParser.class, action = ActionTypes.READ)
    public List<ObjectNode> list(HttpServletRequest request) throws Exception {

        uploadServiceProcessor.transferServiceListToFile();

        String namespaceId = WebUtils.optional(request, CommonParams.NAMESPACE_ID, Constants.DEFAULT_NAMESPACE_ID);
        String agent = WebUtils.getUserAgent(request);
        String clusters = WebUtils.optional(request, "clusters", StringUtils.EMPTY);
        String clientIP = WebUtils.optional(request, "clientIP", StringUtils.EMPTY);
        int udpPort = Integer.parseInt(WebUtils.optional(request, "udpPort", "0"));
        String env = WebUtils.optional(request, "env", StringUtils.EMPTY);
        boolean isCheck = Boolean.parseBoolean(WebUtils.optional(request, "isCheck", "false"));
        String app = WebUtils.optional(request, "app", StringUtils.EMPTY);
        String tenant = WebUtils.optional(request, "tid", StringUtils.EMPTY);
        boolean healthyOnly = Boolean.parseBoolean(WebUtils.optional(request, "healthyOnly", "false"));
        List<String> serviceNameList = parseServiceNameList(request);
        String groupName = WebUtils.optional(request, "groupName", Constants.DEFAULT_GROUP);

        return getServiceInfo(groupName, namespaceId, serviceNameList, agent, clusters, clientIP, udpPort, env, isCheck, app, tenant,
            healthyOnly);
    }

    // TODO: 2022/3/4 此处需要处理 json 转换异常
    // 解析 request 中 ServiceNameList，并转成List<String>返回。
    private List<String> parseServiceNameList(HttpServletRequest request) {
        String serviceNameJson =  WebUtils.optional(request, "serviceNameList", StringUtils.EMPTY);

        if (StringUtils.isEmpty(serviceNameJson)) {
            throw new IllegalArgumentException("Service Name is null.");
        }
        return JacksonUtils.toObj(serviceNameJson, new TypeReference<List<String>>() {
        });

    }

    // 遍历 service name，获取每个service的实例信息。
    public List<ObjectNode> getServiceInfo(String groupName, String namespaceId, List<String> serviceNameList, String agent, String clusters, String clientIP,
                             int udpPort, String env, boolean isCheck, String app, String tenant, boolean healthyOnly) throws Exception {
        List<ObjectNode> serviceInfoList = new ArrayList<>();
        for ( String serviceName : serviceNameList) {
            if (!serviceName.matches("[0-9a-zA-Z@\\.:_-]+@@[0-9a-zA-Z@\\.:_-]+")) {
                serviceName = groupName + Constants.SERVICE_INFO_SPLITER + serviceName;
            }
            ObjectNode res = instanceController.doSrvIpxt(namespaceId, serviceName, agent, clusters, clientIP, udpPort, env, isCheck, app, tenant,
                healthyOnly);
            serviceInfoList.add(res);
        }
        return serviceInfoList;
    }
}
