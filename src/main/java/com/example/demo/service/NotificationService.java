package com.example.demo.service;

import com.example.demo.dto.NotificationDTO;
import com.example.demo.dto.PaginationDTO;
import com.example.demo.enums.NotificationTypeEnum;
import com.example.demo.mapper.NotificationMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Notification;
import com.example.demo.model.NotificationExample;
import com.example.demo.model.User;
import com.mysql.cj.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Long id, Integer currentPage, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        //根据用户id获取评论的总数
        Integer totalCount = getTotalCount(id);
        //计算并设置总页数
        paginationDTO.setTotalPage(totalCount,size);
        //计算并设置当前页的页码
        paginationDTO.setCurrentPage(currentPage,paginationDTO.getTotalPage());
        Integer offset=size*(paginationDTO.getCurrentPage()-1);
        List<Notification> notificationList=notificationMapper.selectByReceiver(id,offset,size);
        if (notificationList.size()==0){
            return paginationDTO;
        }
        List<NotificationDTO> notificationDTOS=new ArrayList<>();
        for (Notification notification:notificationList) {
            User user=userMapper.selectByPrimaryKey(notification.getNotifier());
            NotificationDTO notifyDTO= new NotificationDTO();
            BeanUtils.copyProperties(notification,notifyDTO);
            notifyDTO.setType(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notifyDTO);
        }
        paginationDTO.setLists(notificationDTOS);
        paginationDTO.setShowPrevious(paginationDTO.getCurrentPage());
        paginationDTO.setShowFirstPage(paginationDTO.getCurrentPage());
        paginationDTO.setShowNext(paginationDTO.getCurrentPage(),paginationDTO.getTotalPage());
        paginationDTO.setShowEndPage(paginationDTO.getCurrentPage(),paginationDTO.getTotalPage());
        paginationDTO.setPage(paginationDTO.getShowListSize(),paginationDTO.getCurrentPage(),paginationDTO.getTotalPage());
        return paginationDTO;
    }

    @NotNull
    public Integer getTotalCount(Long id) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id);
        return (Integer) (int) notificationMapper.countByExample(notificationExample);
    }
}
