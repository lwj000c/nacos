package com.alibaba.nacos.naming.extend;

import com.alibaba.nacos.core.utils.ApplicationUtils;
import com.jcraft.jsch.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class SFTPUtils {
    public ChannelSftp connect() {
        ChannelSftp sftp = null;
        String sftpIP = ApplicationUtils.getSystemEnv("sftp.IP");
        int sftpPort = Integer.valueOf(ApplicationUtils.getSystemEnv("sftp.Port"));
        String sftpUser = ApplicationUtils.getSystemEnv("sftp.User");
        String sftpPassword = ApplicationUtils.getSystemEnv("sftp.Password");

        try {
            JSch jsch = new JSch();
            jsch.getSession(sftpUser, sftpIP, sftpPort);
            Session sshSession = jsch.getSession(sftpUser, sftpIP, sftpPort);
            sshSession.setPassword(sftpPassword);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
        } catch (Exception e) {

        }
        return sftp;
    }

    public void upload(String directory, String uploadFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            sftp.put(new FileInputStream(file), file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 创建目录
     * @throws Exception
     */
    public void createDir(String createpath, ChannelSftp sftp) throws Exception {
        try {
            if (isDirExist(sftp, createpath)) {
                sftp.cd(createpath);
            }
            String pathArry[] = createpath.split("/");
            StringBuffer filePath = new StringBuffer("/");
            for (String path : pathArry) {
                if (path.equals("")) {
                    continue;
                }
                filePath.append(path + "/");
                if (isDirExist(sftp, filePath.toString())) {
                    sftp.cd(filePath.toString());
                } else {
                    // 建立目录
                    sftp.mkdir(filePath.toString());
                    // 进入并设置为当前目录
                    sftp.cd(filePath.toString());
                }
            }
            sftp.cd(createpath);
        } catch (SftpException e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 判断目录是否存在
     */
    public boolean isDirExist(ChannelSftp sftp,String directory) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }
}
