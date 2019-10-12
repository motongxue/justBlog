console.log("%c zb-blog：轻量级博客系统" ,'color: #0bb8ca;')
//回到顶部
$(window).scroll(function() {
    //创建一个变量存储当前窗口下移的高度
    var scroTop = $(window).scrollTop();
    //判断当前窗口滚动高度
    //如果大于100，则显示顶部元素，否则隐藏顶部元素
    if (scroTop > 100) {
        $('.return_top').css("opacity","0.4");
    } else {
        $('.return_top').css("opacity","0");
    }
});

$(document).ready(function() {
    tagColor();

    /*回到顶部点击函数*/
    $('.return_top').click(function(){
        $("html,body").animate({scrollTop:0},"fast");
    });

});


function tagColor() {
    /*标签*/
    if($(".tag-li a").length>0){
        $(".tag-li a").each(function(i) {
            var let = new Array('rgb(198, 5, 210)', 'rgb(11, 214, 104)','rgb(25, 83, 255)','rgb(255, 84, 115)', 'rgb(49, 172, 118)', 'rgb(144, 113, 160)','rgb(0, 162, 179)', 'rgb(0, 126, 249)', 'rgb(0, 176, 234)','rgb(210, 50, 138)', 'rgb(170, 173, 0)', 'rgb(255, 99, 71)');
            var k = i%let.length;
            $(this).css("color",let[k])
        });
    }

}
