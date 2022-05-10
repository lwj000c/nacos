package com.alibaba.nacos.naming.controllers;

import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.common.utils.JacksonUtils;
import com.alibaba.nacos.naming.BaseTest;
import com.alibaba.nacos.naming.NamingApp;
import com.alibaba.nacos.naming.consistency.persistent.raft.RaftPeerSet;
import com.alibaba.nacos.naming.core.Cluster;
import com.alibaba.nacos.naming.core.Instance;
import com.alibaba.nacos.naming.core.Service;
import com.alibaba.nacos.naming.misc.UtilsAndCommons;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = MockServletContext.class)
//@WebAppConfiguration
//@SpringBootTest(classes = NamingApp.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RunWith(MockitoJUnitRunner.class)
//@ContextConfiguration(classes = MockServletContext.class)
//@WebAppConfiguration
//@SpringBootTest
public class InstanceBatchControllerTest extends BaseTest {

    @InjectMocks
    private InstanceBatchController instanceBatchController;

    @Mock
    private InstanceController instanceController;

//    private MockMvc mockmvc;

//    @Before
//    public void before() {
////        super.before();
//        mockmvc = MockMvcBuilders.standaloneSetup(instanceBatchController).build();
//    }

    @Test
    public void getListInstance() throws Exception {
        Service service1 = new Service();
        service1.setName(TEST_SERVICE_NAME + "1");

        Service service2 = new Service();
        service2.setName(TEST_SERVICE_NAME + "2");

        Cluster cluster1 = new Cluster(UtilsAndCommons.DEFAULT_CLUSTER_NAME, service1);
        service1.addCluster(cluster1);

        Cluster cluster2 = new Cluster(UtilsAndCommons.DEFAULT_CLUSTER_NAME, service2);
        service2.addCluster(cluster2);

        Instance instance1 = new Instance();
        instance1.setIp("10.10.10.10");
        instance1.setPort(8888);
        instance1.setWeight(2.0);
        instance1.setServiceName(service1.getName());
        instance1.setEphemeral(true);
        instance1.setClusterName("DEFAULT");
        MockHttpServletRequest registMock1 = new MockHttpServletRequest();
        registMock1.addParameter("serviceName",service1.getName() );
        registMock1.addParameter("namespaceId", "public");
        registMock1.addParameter("ip", instance1.getIp());
        registMock1.addParameter("port", String.valueOf(instance1.getPort()));
        registMock1.addParameter("cluster", instance1.getClusterName());
        String result1 = null;
        try {
            result1 = instanceController.register(registMock1);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(result1);
//        serviceManager.registerInstance(service1.getNamespaceId(), service1.getName(), instance1);

        Instance instance2 = new Instance();
        instance2.setIp("20.20.20.20");
        instance2.setPort(8888);
        instance2.setWeight(2.0);
        instance2.setEphemeral(true);
        instance2.setServiceName(service2.getName());
//        List<Instance> ipList2 = new ArrayList<>();
//        ipList2.add(instance2);
        serviceManager.registerInstance(service2.getNamespaceId(), service2.getName(), instance2);
//        service1.updateIPs(ipList2, false);

        Mockito.when(serviceManager.getService(Constants.DEFAULT_NAMESPACE_ID, service1.getName())).thenReturn(service1);
        Mockito.when(serviceManager.getService(Constants.DEFAULT_NAMESPACE_ID, service2.getName())).thenReturn(service2);

        String serviceNameList = "[\""+service1.getName()+"\",\""+service2.getName() +"\"]";

//        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
//            .post(UtilsAndCommons.NACOS_NAMING_CONTEXT + "/instances/batch/list").param("serviceNameList", serviceNameList);

//        MockHttpServletResponse response = mockmvc.perform(builder).andReturn().getResponse();
//        String actualValue = response.getContentAsString();
//        JsonNode result = JacksonUtils.toObj(actualValue);
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addParameter("serviceNameList", serviceNameList);
        mockHttpServletRequest.addParameter("namespaceId", "public");
        mockHttpServletRequest.addParameter("clientIP", "1.1.1.1");
        mockHttpServletRequest.addParameter("udpPort", String.valueOf(30760));
        mockHttpServletRequest.addParameter("groupName", "DEFAULT_GROUP");
        mockHttpServletRequest.addParameter("clusters", "DEFAULT");

        instanceBatchController.list(mockHttpServletRequest);
//        Mockito.when(instanceController.thenReturn(service1);

    }
}
