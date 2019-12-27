$(function () {
    var zblogNickName=Core.getCookie("zblog-username");
    var zblogQQ=Core.getCookie("zblog-qq");
    var zblogEmail=Core.getCookie("zblog-email");
    var zblogAvatar=Core.getCookie("zblog-avatar");
    if(zblogNickName!=""){
        $("#user-name-content").show();
        $("#user-name").text(zblogNickName);
        $("#nickname").val(zblogNickName);
        $("#qq").val(zblogQQ);
        $("#email").val(zblogEmail);
        $("#avatar").val(zblogAvatar);
    }else{
        $("#user-info").show();
    }

    $("#qq").blur(function () {
        var avatar = 'https://q1.qlogo.cn/g?b=qq&nk='+$(this).val()+'&s=100';
        $("#avatar").val(avatar);
    });

    function init(pageNum) {
        Core.postAjax("/api/comments",{"sid":sid,"pageNum": (pageNum==null? 1 : pageNum)},function (data) {
            var data = data.data;
            var commentOne="";
            if(data.list.length==0){
                commentOne+='<div class="no-comment">暂无评论，快来占领宝座</div>';
                $("#comment-ul").append(commentOne);
            }else{
                $.each(data.list,function (index,value) {
                    commentOne +=
                        '<li>'+
                        '	<div class="comment-body" id="comment-'+value.id+'">'+
                        '		<div class="comment-user-img">'+
                        '			<img src="'+(value.avatar||"/img/user-default.png")+'" onerror="this.src=\'/img/user-default.png\'" />'+
                        '		</div>'+
                        '		<div class="comment-info">'+
                        '			<div class="comment-top">'+
                        '				<span class="comment-nickname">'+
                        '					<a href="javascript:void(0)">'+value.nickname+'</a>'+
                        '				</span>'+
                        '				<span class="comment-time">'+Core.getDateDiff(new Date(value.createTime))+
                        '				</span>'+
                        '				<span class="comment-floor">#'+value.floor+'楼'+
                        '				</span>'+
                        '			</div>'+
                        '           <div class="comment-content">'+
                        '               <div class="comment-content-text">'+value.content+'</div>'+
                        '			</div>'+
                        '			<div class="comment-footer">'+
                        '				<span class="reply mr-5" reply-id="'+value.id+'" reply-mid="'+value.id+'" reply-nickname="'+value.nickname+'">回复</span>'+
                        '				<span class="cancel-reply mr-5" style="display: none;">取消回复</span>'+
                        '				<span class="comment-support pointer fa fa-thumbs-o-up" biz-id="'+value.id+'">'+value.supportNum+'</span>'+
                        '			</div>'+
                        '		</div>'+
                        '	</div> ';
                    if(value.nodes!=null){
                        $.each(value.nodes, function (nIndex,nValue) {
                            commentOne +=
                                '<div class="comment-body-sub" id="comment-'+nValue.id+'">'+
                                '		<div class="comment-user-img">'+
                                '			<img src="'+(nValue.avatar||"/img/user-default.png")+'" onerror="this.src=\'/img/user-default.png\'" />'+
                                '		</div>'+
                                '		<div class="comment-info">'+
                                '			<div class="comment-top">'+
                                '				<span class="comment-nickname">'+
                                '					<a href="javascript:void(0)">'+nValue.nickname+'</a><a class="comment-link" data-link="comment-'+nValue.parentId+'">@'+nValue.parentNickname+'</a>'+
                                '				</span>'+
                                '				<span class="comment-time">'+Core.getDateDiff(new Date(nValue.createTime))+
                                '				</span>'+
                                '			</div>'+
                                '           <div class="comment-content">'+
                                '               <div class="comment-content-text">'+nValue.content+'</div>'+
                                '			</div>'+
                                '			<div class="comment-footer">'+
                                '				<span class="reply mr-5" reply-id="'+nValue.id+'" reply-mid="'+nValue.mid+'" reply-nickname="'+nValue.nickname+'">回复</span>'+
                                '				<span class="cancel-reply mr-5" style="display: none;">取消回复</span>'+
                                '				<span class="comment-support pointer fa fa-thumbs-o-up" biz-id="'+nValue.id+'">'+nValue.supportNum+'</span>'+
                                '			</div>'+
                                '		</div>'+
                                '</div> ';
                        })
                    }
                    commentOne +='</li>';
                });
                $("#comment-more").remove();
                if(data.hasNextPage){
                    commentOne+='<div id="comment-more" data-page="'+data.nextPage+'" class="comment-more">加载更多</div>'
                };
                $("#comment-ul").append(commentOne);
                /*加载更多*/
                $("#comment-more").click(function () {
                    init($(this).attr("data-page"));
                });
                /*link至评论*/
                $("#comment-ul").on("click",".comment-link",function () {
                    var commentLinkId = $(this).attr("data-link");
                    $("html,body").animate({
                        scrollTop:$("#"+commentLinkId).offset().top-55},{duration: 300,easing: "swing"})
                });

                $(".reply").click(function () {
                    var replyId=$(this).attr("reply-id");
                    var replyMid=$(this).attr("reply-mid");
                    var replyNickname=$(this).attr("reply-nickname");
                    if($("#reply-comment-form").length>0){
                        $replyForm=$("#reply-comment-form");
                        $("#reply-comment-form").remove();
                        $replyForm.css("display","none");
                        $(this).parent().after($replyForm);
                        $("#reply-comment-form").slideDown(250);
                        $(".reply[style='display: none;']").next().hide();
                        $(".reply[style='display: none;']").show();
                        $("#replyId").val(replyId);
                    }else{
                        var replyForm =
                            '<form id="reply-comment-form" style="display: none;" class="form-horizontal mt-10">'+
                            '   <input name="sid" type="hidden" value="'+sid+'"  />'+
                            '   <input id="replyMid" name="mid" type="hidden" value="'+replyMid+'"  />'+
                            '   <input id="replyId" name="parentId" type="hidden" value="'+replyId+'"  />'+
                            '   <input id="replyNickname" name="parentNickname" type="hidden" value="'+replyNickname+'"  />'+
                            '   <div id="reply-user-form" class="form-group" style="display: '+(zblogNickName==""?"block":"none")+'">'+
                            '		<input id="reply-avatar" name="avatar" value="'+zblogAvatar+'" type="hidden">'+
                            '       <div class="col-sm-4">'+
                            '           <input id="reply-nickname" value="'+zblogNickName+'"  type="text" class="form-control" name="nickname" placeholder="昵称(必填)" />'+
                            '       </div>'+
                            '       <div class="col-sm-4">'+
                            '           <input id="reply-qq" value="'+zblogQQ+'" type="text" class="form-control" name="qq" placeholder="QQ（可获取头像和昵称）" />'+
                            '       </div>'+
                            '       <div class="col-sm-4">'+
                            '           <input id="reply-email" value="'+zblogEmail+'" type="text" class="form-control" name="email" placeholder="邮箱" />'+
                            '       </div>'+
                            '   </div>'+
                            '   <div class="form-group">'+
                            '       <div class="col-xs-12">'+
                            '           <input name="content" type="hidden" class="form-control" id="reply-comment-textarea-input">'+
                            '           <textarea class="form-control" id="reply-comment-textarea" rows="4" placeholder="说点什么吧~"></textarea>'+
                            '       </div>'+
                            '   </div>'+
                            '   <div>'+
                            '   <button id="submitReplyCommentBtn" type="button" class="btn btn-pri">发表评论</button>'+
                            '   </div>'+
                            '</form>';
                        $(this).parent().after(replyForm);
                        $("#reply-comment-form").slideDown(250);
                    }
                    $(this).hide();
                    $(this).next().show();

                    $("#reply-qq").blur(function () {
                        var avatar = 'https://q1.qlogo.cn/g?b=qq&nk='+$(this).val()+'&s=100';
                        $("#reply-avatar").val(avatar);
                    });

                    $("#submitReplyCommentBtn").on('click',function () {
                        if($("#reply-nickname").val()==""){
                            Core.msg("请输入昵称~");
                            return;
                        }else if($("#reply-comment-textarea").val()==""){
                            Core.msg("说点什么吧~");
                            return;
                        }
                        var replyContent = formatContent($("#reply-comment-textarea").val());
                        $("#reply-comment-textarea-input").val(replyContent);
                        Core.postAjax("/api/comment/save",$("#reply-comment-form").serialize(),function (data) {
                            Core.msg(data.msg);
                            if(data.status==200){
                                $("#reply-comment-textarea-input,#reply-comment-textarea").val("");
                                $("#reply-comment-form").hide();
                                $(".reply[style='display: none;']").next().hide();
                                $(".reply[style='display: none;']").show();
                                handleCommentData(data.data,1);
                                if(Core.getCookie("zblog-username")==""){
                                    Core.setCookie("zblog-username",$("#reply-nickname").val(),30);
                                    Core.setCookie("zblog-qq",$("#reply-qq").val(),30);
                                    Core.setCookie("zblog-email",$("#reply-email").val(),30);
                                    Core.setCookie("zblog-avatar",$("#reply-avatar").val(),30);
                                }
                            }
                        })
                    })
                });
                $(".cancel-reply").click(function () {
                    $("#reply-comment-form").slideUp(250);
                    $(this).hide();
                    $(this).prev().show();
                });

                $(".comment-support").click(function () {
                    $thisLove = $(this);
                    Core.postAjax("/api/comment/love",{"commentId":$(this).attr("biz-id")},function (data) {
                        if(data.status==200){
                            $thisLove.text(parseInt($thisLove.text())+1);
                        }else{
                            Core.msg(data.msg,2);
                        }
                    });
                })
            }
        })
    }
    init();

    /*提交评论*/
    $("#submitCommentBtn").click(function () {
        if($("#nickname").val()==""){
            Core.msg("请输入昵称~");
            return;
        }else if($("#comment-textarea").val()==""){
            Core.msg("说点什么吧~");
            return;
        }
        var content = formatContent($("#comment-textarea").val());
        $("#comment-textarea-input").val(content);
        Core.postAjax("/api/comment/save",$("#comment-form").serialize(),function (data) {
            Core.msg(data.msg);
            if(data.status===200){
                $("#comment-textarea-input,#comment-textarea").val("");
                handleCommentData(data.data);
                $(".no-comment").hide();
                var formNickname = $("#nickname").val();
                var formQQ = $("#qq").val();
                var formEmail = $("#email").val();
                var formAvatar = 'https://q1.qlogo.cn/g?b=qq&nk='+formQQ+'&s=100';
                if(zblogNickName!=formNickname || zblogQQ!=formQQ || zblogEmail!=formEmail){
                    console.log(zblogNickName);
                    if(zblogNickName==""){
                        $("#reply-user-form").hide();
                    }
                    $("#reply-nickname").val(formNickname);
                    $("#reply-qq").val(formQQ);
                    $("#reply-email").val(formEmail);
                    $("#reply-avatar").val(formAvatar);
                    zblogNickName=formNickname;
                    zblogQQ=formQQ;
                    zblogEmail=formEmail;
                    zblogAvatar=formEmail;
                    Core.setCookie("zblog-username",zblogNickName,30);
                    Core.setCookie("zblog-qq",zblogQQ,30);
                    Core.setCookie("zblog-email",zblogEmail,30);
                    Core.setCookie("zblog-avatar",zblogAvatar,30);
                }
            }
        })
    });
    /*点击用户名*/
    $("#user-name").click(function () {
        if($("#user-info").hasClass("user-show")){
            $(".save-user").hide();
            $("#user-info").slideUp(250);
            $("#user-info").removeClass("user-show");

        }else{
            $(".save-user").show();
            $("#user-info").slideDown(250);
            $("#user-info").addClass("user-show");
        }

    })
    /*保存用户*/
    $(".save-user").click(function () {
        var nickname=$("#nickname").val();
        var qq=$("#qq").val();
        var email=$("#email").val();
        var avatar = 'https://q1.qlogo.cn/g?b=qq&nk='+qq+'&s=100';
        if(zblogNickName!=nickname||zblogQQ!=qq||zblogEmail!=email){
            Core.setCookie("zblog-username",nickname,30);
            Core.setCookie("zblog-qq",qq,30);
            Core.setCookie("zblog-email",email,30);
            Core.setCookie("zblog-avatar",avatar,30);
            $("#user-name").text(nickname);
            zblogNickName=nickname;
            zblogQQ=qq;
            zblogEmail=email;
            zblogAvatar=avatar;
            $("#reply-nickname").val(nickname);
            $("#reply-qq").val(qq);
            $("#reply-email").val(email);
            $("#reply-avatar").val(avatar);
        }
        $(".save-user").hide();
        $("#user-info").slideUp(250);
        $("#user-info").removeClass("user-show");
    })
    /*格式化字符串*/
    function formatContent(a) {
        a = a.replace(/\r\n/g, '<br/>');
        a = a.replace(/\n/g, '<br/>');
        a = a.replace(/\s/g, ' ');
        return a;
    }

    function handleCommentData(data,isReply){
        var html='<li>'+
            '<div class="comment-body">'+
            ' <div class="comment-user-img">'+
            '<img src="'+data.avatar+'" onerror="this.src=\'/img/user-default.png\'" />'+
            '</div>'+
            '<div class="comment-info">'+
            '<div class="comment-top">'+
            '<span class="comment-nickname"> <a href="javascript:void(0)">'+data.nickname+'</a> </span>'+
            '<span class="comment-time">'+Core.getDateDiff(new Date(data.createTime))+'</span>'+
            ' </div>'+
            ' <div class="comment-content">';
        if(isReply){
            var pNickname= $("#reply-comment-form").parent().find(".comment-top .comment-nickname>a:first-child").text();
            var pText =  $("#reply-comment-form").parent().find(".comment-content>.comment-content-text").text();
            html+='<div class="comment-parent">'+
                '<div class="comment-parent-user">	'+
                '<a class="comment-link" data-link="comment-'+data.parentId+'">@'+pNickname+'</a>	'+
                '</div>	'+
                '<div class="comment-parent-content">'+pText+'</div>	'+
                '</div>';
        }
        html+=data.content+
            '</div>'+
            '<div class="comment-footer"><span class="audit">正在审核</span>'+
            '</div>'+
            '</div>'+
            '</div>'+
            '</li>';
        $("#comment-ul").prepend(html);
    }
})