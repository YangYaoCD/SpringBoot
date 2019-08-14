package com.example.demo.exception;

public enum  CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(2001, "你找的问题不在，再试试？"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论进行回复！"),
    NO_LOGIN(2003,"未登录，请先登录！"),
    SYS_ERROR(2004,"服务器冒烟了！"),
    PUBLISH_NOT_FOUND(2005,"找不到，请重试！"),
    TYPE_PARAM_WRONG(2006,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2007,"回复的评论不在了，要不再试试");

    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code,String message) {
        this.message = message;
        this.code=code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
