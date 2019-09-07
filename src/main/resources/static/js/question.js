//提交回复
function post(){
    var questionId=$("#question_id").val();
    var content=$("#comment_content").val();
    comment2target(questionId,1,content,"/comment/post");
}

$(document).keydown(function(event){
    if(event.keyCode == 13){
        post();
    }

});


//展开二级评论
function collapseComments(e) {
    //console.log(e);
    var id = e.getAttribute("data");
    //console.log(attribute);
    var elementById = $("#comment-"+id);
    if (!elementById.hasClass("in")){
        var subCommentContainer=$("#comment-"+id);
        if (subCommentContainer.children().length!=1) {
            //展开评论
            elementById.addClass("in")
            e.classList.add("active");
        }else {
            $.getJSON( "/comment/"+id, function( data ) {

                //追加标签
                var commentBody = $("#comment-body-"+id);
                // commentBody.appendChild();
                $.each( data.data.reverse(), function( index,comment ) {
                    var mediaLeftElement=$("<div/>",{
                        "class":"media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement=$("<div/>",{
                        "class":"media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu",
                    }).append($("<span/>", {
                        "class": "glyphicon glyphicon-thumbs-up",
                    })).append($("<span/>", {
                        "class": "glyphicon glyphicon-thumbs-down",
                    })).append($("<span/>", {
                        "class": "pull-right",
                        "html":moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    // ctrl+shift+n 反抽变量
                    var mediaElement=$("<div/>",{
                       "class":"media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement=$("<div/>",{
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement);
                    subCommentContainer.prepend(commentElement);
                });

            });
            elementById.addClass("in")
            e.classList.add("active");
        }

    }else {
        //折叠评论
        elementById.removeClass("in");
        e.classList.remove("active");
    }
}

function comment2target(targetId,type,content,url) {
    $.ajax({
        type: "POST",
        url: url,
        contentType:"application/json",
        dataType:'json',
        data: JSON.stringify({
            "parentId":targetId,
            "content":content,
            "type":type

        }),
        success: function (response) {
            console.log(response);
            console.log("code"+response.code)
            console.log("message"+response.message);
            var code=response.code;
            if (code==200){
                window.location.reload();
                $("#comment_section").hidden;
            } else {
                if (response.code==2003){
                    var isAccepted = confirm(response.message);
                    if (isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=4146ce3195480451f292&redirect_uri=http://localhost:8585/callback&scope=user&state=1");
                        window.localStorage.setItem("closable",true);
                    }
                }else {
                    alert(response.message);
                }
            }
        }
    });
}
function comment(e){
    var id = e.getAttribute("id");
    commentId=id.split("-")[1];
    console.log(commentId);
    var e2 = $("#comment2-"+commentId);
    var content = e2.val();
    console.log(content);
    comment2target(commentId,2,content,"/comment/post");
}