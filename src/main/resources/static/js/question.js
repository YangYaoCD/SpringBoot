function post(){
    var questionId=$("#question_id").val();
    var content=$("#comment_content").val();
    $.ajax({
        type: "POST",
        url: "/comment/post",
        contentType:"application/json",
        dataType:'json',
        data: JSON.stringify({
            "parentId":questionId,
            "content":content,
            "type":1

        }),
        success: function (response) {
            console.log(response);
            console.log("code"+response.code)
            console.log("message"+response.message);
            var code=response.code;
            if (code==200){
                $("#comment_section").hide();
            } else {
                if (response.code==2003){
                    var isAccepted = confirm(response.message);
                    if (isAccepted){
                        window.open("/");
                    }
                }else {
                    alert(response.message);
                }
            }
        }
    });
}
// <script>
// var shuju={
//     "parentId":1,
//     "content":"这是一个回复内容",
//     "type":1
// }
// function fun1() {
//     $.ajax({
//         type:'post',
//         url:'/comment/post',
//         data:shuju,
//         dataType:'json',
//         success:function(data){
//             console.log(data)
//         }
//     });
// }
// </script>