package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/upload")
@CrossOrigin
public class FileUploadController {

    @Value("${upload.path:./uploads/}")
    private String uploadPath;

    @Value("${spring.servlet.multipart.max-file-size:10MB}")
    private String maxFileSize;

    @PostMapping("/image")
    public Result<?> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件为空");
        }

        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.error("只能上传图片文件");
        }

        // 验证文件大小 (10MB)
        long maxSize = 10 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            return Result.error("文件大小不能超过 10MB");
        }

        try {
            // 创建上传目录
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            // 验证文件扩展名
            List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp");
            if (extension.isEmpty() || !allowedExtensions.contains(extension.toLowerCase())) {
                return Result.error("不支持的图片格式，仅支持 jpg、jpeg、png、gif、webp");
            }

            String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            String randomSuffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            String filename = timestamp + "_" + randomSuffix + extension;

            // 保存文件
            Path filePath = uploadDir.resolve(filename);
            file.transferTo(filePath.toFile());

            // 返回访问 URL
            String imageUrl = "/api/uploads/" + filename;
            
            Map<String, Object> data = new HashMap<>();
            data.put("url", imageUrl);
            data.put("filename", filename);
            data.put("size", file.getSize());
            data.put("contentType", contentType);

            return Result.success("上传成功", data);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败：" + e.getMessage());
        }
    }

    @PostMapping("/images")
    public Result<?> uploadImages(@RequestParam("files") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return Result.error("文件为空");
        }

        if (files.length > 9) {
            return Result.error("最多只能上传 9 张图片");
        }

        List<Map<String, Object>> uploadedFiles = new ArrayList<>();
        
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                continue;
            }

            // 验证文件大小
            long maxSize = 10 * 1024 * 1024;
            if (file.getSize() > maxSize) {
                continue;
            }

            try {
                // 创建上传目录
                Path uploadDir = Paths.get(uploadPath);
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                // 生成文件名
                String originalFilename = file.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                
                // 验证文件扩展名
                List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp");
                if (extension.isEmpty() || !allowedExtensions.contains(extension.toLowerCase())) {
                    continue;
                }

                String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                String randomSuffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                String filename = timestamp + "_" + randomSuffix + extension;

                // 保存文件
                Path filePath = uploadDir.resolve(filename);
                file.transferTo(filePath.toFile());

                // 返回访问 URL
                String imageUrl = "/api/uploads/" + filename;
                
                Map<String, Object> fileInfo = new HashMap<>();
                fileInfo.put("url", imageUrl);
                fileInfo.put("filename", filename);
                fileInfo.put("size", file.getSize());
                fileInfo.put("contentType", contentType);
                
                uploadedFiles.add(fileInfo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Result.success("上传成功", uploadedFiles);
    }

    @DeleteMapping("/image/{filename}")
    public Result<?> deleteImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadPath).resolve(filename);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return Result.success("删除成功");
            } else {
                return Result.error("文件不存在");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("删除失败：" + e.getMessage());
        }
    }
}
