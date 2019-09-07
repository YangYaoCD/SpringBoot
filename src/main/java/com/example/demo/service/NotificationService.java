package com.example.demo.service;

import com.example.demo.dto.NotificationDTO;
import com.example.demo.dto.PaginationDTO;
import com.example.demo.enums.NotificationStatusEnum;
import com.example.demo.enums.NotificationTypeEnum;
import com.example.demo.exception.CustomerException;
import com.example.demo.exception.CustomizeErrorCode;
import com.example.demo.mapper.NotificationMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Notification;
import com.example.demo.model.NotificationExample;
import com.example.demo.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            notifyDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
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

    public Integer UnreadCount(Long id) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return (Integer) (int) notificationMapper.countByExample(notificationExample);
    }


    public void setReplyCount(HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            Integer replyCount =this.getTotalCount(user.getId());
            model.addAttribute("replyCount",replyCount);
        }
    }

    public NotificationDTO read(long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification==null){
            throw new CustomerException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(),user.getId())){
            throw new CustomerException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
