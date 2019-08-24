function selectTAG(tag){
    var previous = $("#tag").val();
    if (previous==""||previous.length==0) {
        $("#tag").val(tag);
    }else {
        var strings = previous.split(",");
        var exist=strings.indexOf(tag);
        if (exist==-1){
            $("#tag").val(previous+","+tag);
        }
    }
}
function showSelectTags() {
    console.log("111");
    $("#select-tags").css("display","block");
}
function hideSelectTags() {
    $("#select-tags").css("display","none");
}