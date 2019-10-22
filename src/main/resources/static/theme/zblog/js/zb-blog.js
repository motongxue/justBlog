console.log("%c zb-blog：轻量级博客系统" ,'color: #0bb8ca;');
(function ($) {
    $.fn.textSlider = function (settings) {
        settings = jQuery.extend({
            speed: "normal",
            line: 2,
            timer: 3000
        }, settings);
        return this.each(function () {
            $.fn.textSlider.scllor($(this), settings);
        });
    };
    $.fn.textSlider.scllor = function ($this, settings) {
        var ul = $("ul:eq(0)", $this);
        var timerID;
        var li = ul.children();
        var liHight = $(li[0]).height();
        var upHeight = 0 - settings.line * liHight;//滚动的高度；
        var scrollUp = function () {
            ul.animate({marginTop: upHeight}, settings.speed, function () {
                for (i = 0; i < settings.line; i++) {
                    ul.find("li:first", $this).appendTo(ul);
                }
                ul.css({marginTop: 0});
            });
        };
        var autoPlay = function () {
            timerID = window.setInterval(scrollUp, settings.timer);
        };
        var autoStop = function () {
            window.clearInterval(timerID);
        };
        //事件绑定
        ul.hover(autoStop, autoPlay).mouseout();
    };
})(jQuery);
$(window).scroll(function() {
    var scroTop = $(window).scrollTop();
    if (scroTop > 100) {
        $('.return_top').css("opacity","0.4");
    } else {
        $('.return_top').css("opacity","0");
    }
});

$(function () {
    loadSlider();

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

    function loadSlider(){
        if($(".swiper-container").length){
            var swiper = new Swiper('.swiper-container', {
                speed:500,
                spaceBetween: 30,
                centeredSlides: true,
                loop : true,
                effect : 'fade',
                autoplay: {
                    delay: 4000,
                    disableOnInteraction: false,
                },
                pagination: {
                    el: '.swiper-pagination',
                    clickable: true,
                },
                navigation: {
                    nextEl: '.swiper-button-next',
                    prevEl: '.swiper-button-prev',
                },
            });
            swiper.el.onmouseover = function(){
                swiper.autoplay.stop();
            }
            swiper.el.onmouseout = function(){
                swiper.autoplay.start();
            }
        }
    }



});
