package com.example.demo.controller;

import com.example.demo.dto.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("file")
public class FileController {
    @RequestMapping("upload")
    @ResponseBody
    public FileDTO upload(){
        FileDTO fileDTO=new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/static/img/325596.jpg");
        return fileDTO;
    }
}
