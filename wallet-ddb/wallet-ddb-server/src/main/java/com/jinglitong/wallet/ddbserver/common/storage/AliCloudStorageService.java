package com.jinglitong.wallet.ddbserver.common.storage;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Component
public class AliCloudStorageService extends CloudStorageService{
    private OSSClient client;


    @Value("${aliyun.accessKey}")
    private  String accessKey;
    @Value("${aliyun.secretKey}")
    private  String secretKey;
    @Value("${aliyun.endPoint}")
    private  String aliyunEndPoint;
    @Value("${aliyun.bucket}")
    private String aliyunBucket;
    @Value("${aliyun.domain}")
    private String aliyunDomain;


    @PostConstruct
    private void init(){
        client = new OSSClient(aliyunEndPoint,accessKey, secretKey);
    }


    @Override
    public String uploadFile(InputStream inputStream, String fileName) {
        return upload(inputStream, getPath("", getSuffix(fileName)));
    }

    @Override
    public String uploadFile(byte[] data, String fileName) {
        return upload(data, getPath("", getSuffix(fileName)));
    }

    @Override
    public String downloadPrivateFile(String bucketName, String fileKey, Long expireSeconds) {
        Date expiration = new Date(new Date().getTime() + expireSeconds * 1000);
        // 生成URL
        URL url = client.generatePresignedUrl(bucketName, fileKey, expiration);
        return url.toString();
    }


    public String getAliyunEndPoint() {
        return aliyunEndPoint;
    }

    public void setAliyunEndPoint(String aliyunEndPoint) {
        this.aliyunEndPoint = aliyunEndPoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAliyunBucket() {
        return aliyunBucket;
    }

    public void setAliyunBucket(String aliyunBucket) {
        this.aliyunBucket = aliyunBucket;
    }

    public String getAliyunDomain() {
        return aliyunDomain;
    }

    public void setAliyunDomain(String aliyunDomain) {
        this.aliyunDomain = aliyunDomain;
    }




    private String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }


    private String upload(InputStream inputStream, String path) {
        try {
            client.putObject(getAliyunBucket(), path, inputStream);
        } catch (Exception e){
            throw new RuntimeException("上传文件失败，请检查配置信息", e);
        }

        return getAliyunDomain() + "/" + path;
    }


    private String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath("", suffix));
    }


    private String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath("", suffix));
    }

    @Override
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

    public String uploadProtocolHtml(InputStream htmlIs, String dateTimeStr){
        String fileUrl="";
        try {
            fileUrl = upload(htmlIs, getPath("", getSuffix(dateTimeStr)));
        }catch (Exception e){
            e.printStackTrace();
            fileUrl = "";
        }
        return fileUrl;
    }
    
    public String uploadFaviconFile(MultipartFile file) {
        String fileUrl="";
        try{
            fileUrl = upload(file.getInputStream(), getPath("favicon", getSuffix(file.getOriginalFilename())));
        }catch (IOException e){
            e.printStackTrace();
            fileUrl = "";
        }
        return fileUrl;
    }

}
