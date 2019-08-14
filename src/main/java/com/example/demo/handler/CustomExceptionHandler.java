package com.example.demo.handler;

import com.alibaba.fastjson.JSON;
import com.example.demo.dto.ResultDTO;
import com.example.demo.exception.CustomerException;
import com.example.demo.exception.CustomizeErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handler(HttpServletRequest request,
                         HttpServletResponse response,
                         Throwable e, Model model){
        String contentType = request.getContentType();
        if (contentType.equals("application/json")){
            ResultDTO resultDTO=null;
            if (e instanceof CustomerException){
                resultDTO= ResultDTO.errorOf((CustomerException) e);
            }else {
                resultDTO=ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.flush();
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;

        }else {
            if (e instanceof CustomerException){
                model.addAttribute("message",e.getMessage());
                model.addAttribute("code",((CustomerException) e).getCode());
            }else {
                model.addAttribute("message",CustomizeErrorCode.SYS_ERROR.getMessage());
                model.addAttribute("code",CustomizeErrorCode.SYS_ERROR.getCode());
            }
            return new ModelAndView("error");
        }
    }

}
