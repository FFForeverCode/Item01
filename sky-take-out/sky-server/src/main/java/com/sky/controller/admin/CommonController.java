package com.sky.controller.admin;

import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
    //阿里云工具类
    @Autowired
    private  AliOssUtil aliOssUtil;



    /**
     * 上传文件
     * @param file 名字必须与前端一致
     * @return
     */
    //TODO:MultipartFile、UUID
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String>upload(MultipartFile file) throws IOException {
        log.info("文件上传...");
        try {
            String originalFilename = file.getOriginalFilename();//获取原始文件名
            assert originalFilename != null;
            //截取后缀.png或别的
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = UUID.randomUUID().toString() + extension;//UUID避免重名
            //上传到阿里云OSS服务端中
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);//传入文件二进制数据、文件名（随机生成避免重名）
            return Result.success(filePath);
        }catch(IOException exception){
            log.info("文件上传失败...");
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }

    }
}
