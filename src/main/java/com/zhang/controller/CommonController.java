package com.zhang.controller;

/**
 * @author shkstart
 * @create 2023-07-20 20:45
 */

import com.zhang.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 用来处理上传和下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {


    //获取目录位置
    @Value("${reggie.path}")
    private String parentPath;
    /**
     *   处理文件上传和下载操作
     * @param file 这里的参数名必须和上传页面后的file中的name属性保持一致
     * @return
     *
     */
    @PostMapping("/upload")
    public R<String> upImg(MultipartFile file){
        //MultipartFile类是Spring提供的一个用于上传图片的类
        //这里的file是一个零时文件，当请求结束后这个文件会自动删除
        log.info(file.toString());
        //获取原始文件的文件名
        String originalFilename = file.getOriginalFilename();

        //根据原始文件的文件名来获取文件的扩展名 abc.XXX
        //从 . 最后一次出现的索引位置开始截取
        //为了防止文件名重复，这里我们用UUID来创建文件名
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        //这里还要对目录位置是否存在进行判断
        File parentFile = new File(parentPath);
        if (!parentFile.exists()){
            //如果目录不存在，创建目录
            parentFile.mkdir();
        }

        //将临时文件保存到指定的位置
        try {
            file.transferTo(new File(parentPath+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return R.success(fileName);
    }


    /**
     * 下载文件并回传给页面
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //从磁盘中读取文件
            FileInputStream is = new FileInputStream(new File(parentPath+name));
            //使用输出流将文件回传给页面
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] buffer = new byte[1024];
            ServletOutputStream os = response.getOutputStream();
            while((len = is.read(buffer)) != -1){
                os.write(buffer,0,len);
                os.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
