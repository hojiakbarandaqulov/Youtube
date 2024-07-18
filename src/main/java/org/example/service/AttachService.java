package org.example.service;

import org.example.dto.AttachDTO;
import org.example.entity.AttachEntity;
import org.example.exp.AppBadException;
import org.example.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AttachService {

    @Value("${server.url}")
    private String serverUrl;
    private final AttachRepository attachRepository;

    @Value("${attach.upload.url}")
    public String attachUrl;

    public AttachService(AttachRepository attachRepository) {
        this.attachRepository = attachRepository;
    }

    public byte[] loadImage(String fileName) {
        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(new File("uploads/" + fileName));
        } catch (Exception e) {
            return new byte[0];
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "png", baos);

            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);

        return year + "/" + month + "/" + day; // 2024/06/08
    }

    public String getExtension(String fileName) { // mp3/jpg/npg/mp4.....
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    public AttachDTO saveAttach(MultipartFile file) {
        try {
            String pathFolder = getYmDString();
            File folder = new File("uploads/" + pathFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String key = UUID.randomUUID().toString(); // dasdasd-dasdasda-asdasda-asdasd
            String extension = getExtension(Objects.requireNonNull(file.getOriginalFilename())); // dasda.asdas.dasd.jpg
            // save to system
            byte[] bytes = file.getBytes();
            Path path = Paths.get("uploads/" + pathFolder + "/" + key + "." + extension);
            Files.write(path, bytes);
            // save to db
            AttachEntity entity = new AttachEntity();
            entity.setId(key + "." + extension);
            entity.setPath(pathFolder); // 2024/06/08
            entity.setOriginalName(file.getOriginalFilename());
            entity.setSize(file.getSize());
            entity.setExtension(extension);
            entity.setCreatedDate(LocalDateTime.now());
            attachRepository.save(entity);

            return toDTO(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AttachDTO toDTO(AttachEntity entity) {
        AttachDTO dto = new AttachDTO();
        dto.setId(entity.getId());
        dto.setCreatedData(entity.getCreatedDate());
        dto.setExtension(entity.getExtension());
        dto.setPath(entity.getPath());
        dto.setSize(entity.getSize());
        dto.setOriginalName(entity.getOriginalName());
        dto.setUrl(serverUrl + "/attach/open/" + entity.getId());
        return dto;
    }

    public AttachEntity get(String id) {
        return attachRepository.findById(id).orElseThrow(() -> {
            throw new AppBadException("Attach not found");
        });
    }

    public ResponseEntity download(String attachId) {
        try {
            AttachEntity entity = get(attachId);
            String path = entity.getPath() + "/" + attachId;
            Path file = Paths.get("uploads/" + path);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entity.getOriginalName() + "\"").body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public PageImpl<AttachDTO> getAttachPagination(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AttachEntity> pageObj = attachRepository.findAll(pageable);
        List<AttachDTO> list = new LinkedList<>();
        for (AttachEntity entity : pageObj.getContent()) {
            AttachDTO dto = toDTO(entity);
            list.add(dto);
        }
        Long total = pageObj.getTotalElements();
        return new PageImpl<AttachDTO>(list, pageable, total);
    }

    public Boolean delete(String id) {
        AttachEntity entity = get(id);
        File file = new File(attachUrl + "uploads/" + entity.getPath() + "/" + entity.getOriginalName());
        if (file.exists()) {
            if (!file.delete()) {
                throw new AppBadException("Could not delete the file: " + file.getAbsolutePath());
            }
        }
        attachRepository.delete(entity);
        return true;
    }

    public AttachDTO getDTOWithURL(String attachId) {
        AttachEntity attach = attachRepository.findById(attachId)
                .orElseThrow(() -> new AppBadException("Attach not found"));
        AttachDTO dto = new AttachDTO();
        dto.setId(attachId);
        dto.setUrl(serverUrl + "/" + "uploads/" + attach.getPath() + "/" + attachId);
        return dto;
    }
}

