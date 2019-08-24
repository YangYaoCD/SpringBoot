window.onload=function (ev) {
    var closable = window.localStorage.getItem("closable");
    if (closable=="true"){
        window.close();
        window.localStorage.removeItem("closable");
    }
}