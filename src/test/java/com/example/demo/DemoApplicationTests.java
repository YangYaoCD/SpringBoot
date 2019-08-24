package com.example.demo;

import com.example.demo.mapper.QuestionMapper;
import com.example.demo.model.Question;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    @Autowired
    QuestionMapper questionMapper;
    @Test
    public void contextLoads() {
        String s="Spring Mvc|spring|java";
        List<Question> questions = questionMapper.selectRelated(s,3);
        for (Question question:
             questions) {
            System.out.println(question.toString());
        }
    }

}
