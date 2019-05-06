package com.jinglitong.wallet.server.common.storage;

import com.jinglitong.wallet.server.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

public abstract class CloudStorageService {

    /**
     * 文件路径
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 返回上传路径
     */
    public String getPath(String prefix, String suffix) {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //文件路径
        String path = DateUtils.format(new Date(), "yyyyMMdd") + "/" + uuid;

        if(StringUtils.isNotBlank(prefix)){
            path = prefix + "/" + path;
        }

        return path + suffix;
    }


    public String getSuffix(String fileName){
        String suffix = ".doc";
        if(fileName.indexOf(".")>-1){
            suffix = fileName.substring(fileName.indexOf("."),fileName.length());
        }
        return suffix;
    }

    public abstract String uploadFile(MultipartFile file);


    public abstract String uploadFile(InputStream inputStream, String fileName);

    public abstract String uploadFile(byte[] data, String fileName);


    public abstract String downloadPrivateFile(String bucketName,String fileKey,Long expireSeconds);


}
