package com.alibaba.nacos.naming.extend;

import com.alibaba.nacos.naming.core.ServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.List;

@Component
public class UploadServiceProcessor {

    @Autowired
    protected ServiceManager serviceManager;

    /**
     * 通过环境变量中的namespace和group去遍历读取serviceMap里面的值,读完值并写到文件中.
     * @return 返回生成的文件名：环境标识-命名空间ID.txt
     */
    public String transferServiceListToFile(){
//        String namespaceId = ApplicationUtils.getSystemEnv("namespaceId.citicbank");
//        String group = ApplicationUtils.getSystemEnv("group.MESH");
//        String envSign = ApplicationUtils.getSystemEnv("envSign.FT");

        String namespaceId = "public";
        String group = "DEFAULT_GROUP";
        String envSign = "FT";

        String fileName = envSign + "-" + namespaceId + ".txt";

        List<String> serviceNameList = serviceManager.getAllServiceNameList(namespaceId);

        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(fileName));
            for (String key :  serviceNameList) {
                out.write(key);
                out.write("\n");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }



    //总方法：定时任务
    @Scheduled(cron = "0 0 18 02 * ?")
    public void uploadServiceListTask(){
        UploadServiceProcessor uploadServiceProcessor = new UploadServiceProcessor();
        uploadServiceProcessor.transferServiceListToFile();
    }

}
