package com.example.demo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> lists;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private List<Integer> page;
    private Integer showListSize=5;
    private Integer totalPage;
    private Integer currentPage;

    public void setCurrentPage(Integer currentPage,Integer totalPage) {
        if(currentPage>totalPage){
            currentPage=totalPage;
        }
        if(currentPage<1){
            currentPage=1;
        }
        this.currentPage = currentPage;
    }


    public boolean isShowPrevious() {
        return showPrevious;
    }

    public void setShowPrevious(Integer currentPage) {
        if (currentPage>1){
            showPrevious=true;
        }else {
            showPrevious=false;
        }
    }

    public boolean isShowFirstPage() {
        return showFirstPage;
    }

    public void setShowFirstPage(Integer currentPage) {
        if (currentPage==1){
            showFirstPage=false;
        }else {
            showFirstPage=true;
        }
    }

    public boolean isShowNext() {
        return showNext;
    }

    public void setShowNext(Integer currentPage,Integer totalPage) {
        if (currentPage<totalPage){
            showNext=true;
        }else {
            showNext=false;
        }
    }

    public boolean isShowEndPage() {
        return showEndPage;
    }

    public void setShowEndPage(Integer currentPage,Integer totalPage) {
        if (currentPage==totalPage||totalPage==1){
            showEndPage=false;
        }else {
            showEndPage=true;
        }
    }

    public List<Integer> getPage() {
        return page;
    }

    public void setPage(Integer showListSize,Integer currentPage,Integer totalPage) {
        List<Integer> list=new ArrayList<>();
        Integer minpage=1;
        Integer maxpage=1;
        Integer s=showListSize/2;
        for (int i = s; i >=0; i--) {
            if((currentPage-i)>=1){
                minpage=currentPage-i;
                break;
            }
        }
        for (int i = s; i >=0 ; i--) {
            if ((currentPage+i)<=totalPage){
                maxpage=currentPage+i;
                break;
            }
        }

        for (Integer i = minpage; i <=maxpage; i++) {
            list.add(i);
        }
        page=list;
    }

    public Integer getShowListSize() {
        return showListSize;
    }

    public void setShowListSize(Integer showListSize) {
        this.showListSize = showListSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalCount,Integer size) {
        this.totalPage=totalCount/size;
        if(totalCount%size!=0){
            this.totalPage+=1;
        }
    }
}
