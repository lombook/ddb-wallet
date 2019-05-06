package com.jinglitong.wallet.server.common.storage;

import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

//@Component
public class QiniuCloudStorageService extends CloudStorageService{

    @Value("${qiniu.accessKey}")
    private  String accessKey;
    @Value("${qiniu.secretKey}")
    private  String secretKey;
    @Value("${qiniu.bucket}")
    private  String bucketName;
    @Value("${qiniu.cdnPrefix}")
    private String cdnPrefix;

    @Value("${qiniu.domain}")
    private String qiNiudomain;

    private  UploadManager uploadManager;
    private Auth auth  ;
    private  String token;

    @PostConstruct
    public void init(){
        uploadManager = new UploadManager(new Configuration(Zone.autoZone()));
        auth = Auth.create(accessKey, secretKey);
        token = auth.uploadToken(bucketName);
    }


    /**
     * 上传文件
     * @param file
     * @return
     */
    public String uploadFile(MultipartFile file) {
        String fileUrl="";
        try{
            fileUrl = upload(file.getInputStream(), getPath("", getSuffix(file.getOriginalFilename())));
        }catch (IOException e){
            e.printStackTrace();
            fileUrl = "";
        }
        return fileUrl;
    }


    /**
     * 上传文件 指定后缀
     * @param data
     * @param fileName
     * @return
     */
    public String uploadFile(byte[] data, String fileName) {
        return upload(data, getPath("", getSuffix(fileName)));
    }


    /**
     * 上传文件 指定后缀
     * @param inputStream
     * @param fileName
     * @return
     */
    public String uploadFile(InputStream inputStream, String fileName) {
        return upload(inputStream, getPath("", getSuffix(fileName)));
    }

    /**
     * 下载\私有空间文件
     * @param fileKey
     * @return
     */
    public String downloadPrivateFile(String bucketName,String fileKey,Long expireSeconds){
        String finalUrl = auth.privateDownloadUrl(qiNiudomain+bucketName+"/"+"/"+fileKey, expireSeconds);
        return finalUrl;
    }


    /**
     * 上传文件
     * @param data
     * @param path
     * @return
     */
    private String upload(byte[] data, String path) {
        try {
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                throw new RuntimeException("上传七牛出错：" + res.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException("上传文件失败，请核对七牛配置信息", e);
        }

        return qiNiudomain + "/" + path;
    }




    /**
     * 上传文件
     * @param inputStream
     * @param path
     * @return
     */
    private String upload(InputStream inputStream, String path) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path);
        } catch (IOException e) {
            throw new RuntimeException("上传文件失败", e);
        }
    }



}
