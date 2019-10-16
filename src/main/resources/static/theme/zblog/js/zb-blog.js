console.log("%c zb-blog：轻量级博客系统" ,'color: #0bb8ca;')

$(window).scroll(function() {
    var scroTop = $(window).scrollTop();
    if (scroTop > 100) {
        $('.return_top').css("opacity","0.4");
    } else {
        $('.return_top').css("opacity","0");
    }
});

$(function () {
    /*登出*/
    $("#logout").click(function () {
       Core.postAjax("/logout",{},function (data) {
           if (data.status === 200) {
                Core.removeCookie("access_token");
                window.location.href="/";
           }
       })
    });

    /*回到顶部*/
    $('.return_top').click(function(){
        $("html,body").animate({scrollTop:0},"fast");
    });

    /*初始化用户*/
    if($(".nav-user-info")){
        var curUser = Core.getCurrentUser();
        if(curUser){
            $(".nav-user-info .nav-user-info img").attr("src",curUser.avatar!=null?curUser.avatar:'/img/user-default.png');
        }
    }

});
