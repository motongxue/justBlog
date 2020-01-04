var Core = (function () {
    var core = {};

    //ajax请求
    core.postAjax = function (url, dataToPost, d, type, contentType, async) {
        $.ajax({
            url: url,
            cache: false,
            async: async == undefined ? true : async,
            data: dataToPost,
            type: type == undefined ? "POST" : type,
            contentType: contentType == undefined ? 'application/x-www-form-urlencoded; charset=UTF-8' : contentType,
            success: function (data) {
                if (typeof d == "function") {
                    d(data);
                }
            },
            error:function (XMLHttpRequest, textStatus, errorThrown) {
                Core.msg("错误："+XMLHttpRequest.status,2)
            }
        });
    };

    //消息提示
    core.msg = function(msg,d,type) {
        if(typeof d === "number"){
            type=d;
        };
        var alertId = Core.generateMixed(6);
        var svgContent= (type===undefined||type===1) ? '<svg t="1571189863238" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3745" width="24" height="24"><path d="M837.461333 535.466667a19.072 19.072 0 0 0-22.314666 15.573333 326.314667 326.314667 0 0 1-321.792 269.226667c-180.266667 0-326.912-146.645333-326.912-326.912a326.826667 326.826667 0 0 1 597.930666-182.784 323.157333 323.157333 0 0 1 50.773334 125.056 19.242667 19.242667 0 0 0 37.888-6.698667 360.704 360.704 0 0 0-56.789334-139.946667A365.098667 365.098667 0 0 0 493.312 128 365.226667 365.226667 0 0 0 128 493.354667c0 201.472 163.882667 365.354667 365.354667 365.354666a364.672 364.672 0 0 0 359.68-300.885333 19.2 19.2 0 0 0-15.573334-22.314667" p-id="3746" fill="#2bc80f"></path><path d="M650.538667 389.504l-199.978667 200.021333-119.765333-119.765333a18.816 18.816 0 0 0-26.624 26.581333l132.224 132.181334c0.128 0.170667 0.341333 0.213333 0.469333 0.341333 0.170667 0.170667 0.213333 0.426667 0.426667 0.597333a18.773333 18.773333 0 0 0 26.581333 0l213.290667-213.333333a18.816 18.816 0 0 0-26.624-26.624" p-id="3747" fill="#2bc80f"></path></svg>' : '<svg t="1571190723226" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4874" width="24" height="24"><path d="M872.746667 573.866667a18.858667 18.858667 0 0 0-22.058667 15.445333 322.901333 322.901333 0 0 1-318.464 266.368c-178.389333 0-323.498667-145.066667-323.498667-323.498667a323.413333 323.413333 0 0 1 591.701334-180.906666 319.829333 319.829333 0 0 1 50.261333 123.818666 19.072 19.072 0 0 0 37.461333-6.656A356.949333 356.949333 0 0 0 832 329.984 361.301333 361.301333 0 0 0 532.224 170.666667 361.429333 361.429333 0 0 0 170.666667 532.181333c0 199.381333 162.218667 361.557333 361.557333 361.557334a360.874667 360.874667 0 0 0 355.925333-297.770667 19.029333 19.029333 0 0 0-15.36-22.058667" p-id="4875" fill="#a94442"></path><path d="M532.224 411.306667a20.138667 20.138667 0 0 0-20.138667 20.181333v322.133333a20.138667 20.138667 0 0 0 40.277334 0v-322.133333a20.138667 20.138667 0 0 0-20.138667-20.181333M552.362667 310.613333a20.138667 20.138667 0 0 0-40.277334 0v40.277334a20.138667 20.138667 0 0 0 40.277334 0v-40.32z" p-id="4876" fill="#a94442"></path></svg>';
        var html='<div id="'+alertId+'" class="alert '+((type===undefined||type===1)? 'alert-success':'alert-danger')+'">'+svgContent+'<span class="alert-text">'+msg+'</span></div>';
        $("body").append(html);
        $("#"+alertId).css("display","flex");
        setTimeout(function () {
            $("#"+alertId).remove();
            if (typeof d === "function") {
                d();
            }
        }, 2500);
    };

    core.generateMixed = function (n) {
        var res = "";
        var chars = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
        for(var i = 0; i < n ; i ++) {
            var id = Math.ceil(Math.random()*35);
            res += chars[id];
        }
        return res;
    };

    //禁用button
    core.mask = function (e) {
        $(e).attr('disabled', "true");//添加disabled属性
    };

    //启用button
    core.unmask = function (e) {
        $(e).removeAttr('disabled');//添加disabled属性
    };

    //设置cookie
    core.setCookie = function (cname,cvalue,exdays){
        var d = new Date();
        d.setTime(d.getTime()+(exdays*24*60*60*1000));
        var expires = "expires="+d.toGMTString();
        document.cookie = cname+"="+cvalue+"; "+expires+";path=/";
    };

    //获取cookie
    core.getCookie =  function (cname){
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for(var i=0; i<ca.length; i++) {
            var c = ca[i].trim();
            if (c.indexOf(name)==0) { return c.substring(name.length,c.length); }
        }
        return "";
    };

    //删除cookie
    core.removeCookie = function (name){
        var cval=this.getCookie(name);
        if(cval!=null){
            var exp = new Date();
            exp.setTime(exp.getTime() - 1);
            document.cookie= name + "="+cval+";expires="+exp.toGMTString()+";path=/";
        }
    };

    //格式化时间差
    core.getDateDiff = function(dateTimeStamp) {
        var minute = 1000 * 60;
        var hour = minute * 60;
        var day = hour * 24;
        var halfamonth = day * 15;
        var month = day * 30;
        var year = day * 365;
        var now = new Date().getTime();
        var diffValue = now - dateTimeStamp;
        if (diffValue < 0) {
            //若日期不符则弹出窗口告之
            return "结束日期不能小于开始日期！";
        }
        var yearC = diffValue / year;
        var monthC = diffValue / month;
        var weekC = diffValue / (7 * day);
        var dayC = diffValue / day;
        var hourC = diffValue / hour;
        var minC = diffValue / minute;
        if(yearC>=1){
            result = parseInt(yearC) + "年前";
        }else if (monthC >= 1) {
            result = parseInt(monthC) + "个月前";
        }else if (weekC >= 1) {
            result = parseInt(weekC) + "周前";
        } else if (dayC >= 1) {
            result = parseInt(dayC) + "天前";
        }else if (hourC >= 1) {
            result = parseInt(hourC) + "小时前";
        }else if (minC >= 1) {
            result = parseInt(minC) + "分钟前";
        }else{
            result = "刚刚";
        }
        return result;
    };

    //文字滚动
    core.textSlider = function (k,settings) {
        settings = jQuery.extend({
            speed: 300,
            line: 1,
            timer: 5000
        }, settings);
        return $(k).each(function () {
            Core.textSliderScroll($(k), settings);
        });
    };

    core.textSliderScroll = function ($this, settings) {
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

    core.slider = function(k, settings){
        settings = jQuery.extend({
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
        }, settings);
        if($(k).length){
            var swiper = new Swiper($(k), settings);
            swiper.el.onmouseover = function(){
                swiper.autoplay.stop();
            };
            swiper.el.onmouseout = function(){
                swiper.autoplay.start();
            };
        }
    };

    /*初始化评论*/
    core.initComment = function (options) {
        options = jQuery.extend({
            id:"",
            sid: "",
            emojiServer: "",
            emojis:["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"],
            suffix:"gif"
        }, options);
        if(!/\/$/.test(options.emojiServer)){
            options.emojiServer = options.emojiServer+"/";
        }
        var commentBox = '<form id="comment-form" class="form-horizontal mt-10">'+
            '<input name="sid" type="hidden" value="'+options.sid+'">'+
            '<div id="user-name-content" class="user-name-content">欢迎您：<b id="user-name"></b><span class="save-user btn btn-sm btn-pri">保存</span></div>'+
            '   <div class="form-group" id="user-info" style="display: none">'+
            '       <div class="col-sm-4">'+
            '           <input id="nickname" type="text" class="form-control"  name="nickname" placeholder="昵称（必填）">'+
            '       </div>'+
            '       <div class="col-sm-4">'+
            '           <input id="qq" type="text" class="form-control"  name="qq" placeholder="QQ（可获取头像和昵称）">'+
            '       </div>'+
            '       <div class="col-sm-4">'+
            '           <input id="email" type="text" class="form-control"  name="email" placeholder="邮箱">'+
            '       </div>'+
            '   </div>'+
            '   <div class="form-group">'+
            '       <div class="col-xs-12">'+
            '           <textarea  id="comment-textarea" placeholder="说点什么吧~" style="display: none" name="content"></textarea>'+
            '           <div id="emojiEditorBox">' +
            '               <div contenteditable="true" class="emoji-editor"></div>' +
            '               <div class="emoji-tool">' +
            '                   <div class="emoji-btn"><img src="'+options.emojiServer+'0.png"/></div>'+
            '                   <div class="emoji-content">'+
            '                       <ul>';
        var emojisLi = "";
        $(options.emojis).each(function(index,value){
            emojisLi+='<li><img src="'+options.emojiServer+value+'.'+options.suffix+'"></li>';
        });
        commentBox += emojisLi;
        commentBox += '             </ul>'+
            '                   </div>'+
            '               </div>'+
            '           </div>'+
            '       </div>'+
            '   </div>'+
            '   <div ><button id="submitCommentBtn" type="button" class="btn btn-pri">发表评论</button></div>'+
            '</form>'+
            '<hr class="hr0 mt-15"/>'+
            '<div class="col-xs-12">'+
            '   <div class="row">'+
            '       <ul id="comment-ul" class="comment">'+
            '       </ul>'+
            '   </div>'+
            '</div>';
        $(options.id).append(commentBox);

        /*用户信息区域*/
        var justNickName=Core.getCookie("just-nickname");
        var justQQ=Core.getCookie("just-qq");
        var justEmail=Core.getCookie("just-email");
        var justAvatar=Core.getCookie("just-avatar");
        if(justNickName!=""){
            $("#user-name-content").show();
            $("#user-name").text(justNickName);
            $("#nickname").val(justNickName);
            $("#qq").val(justQQ);
            $("#email").val(justEmail);
            $("#avatar").val(justAvatar);
        }else{
            $("#user-info").show();
        }
        /*qq失去焦点*/
        $("#qq").blur(function () {
            var qq = $(this).val();
            if(qq!=""){
                Core.postAjax("/api/qq/"+qq,{},function (data) {
                    if(data.status===200){
                        $("#nickname").val(data.data.name);
                    }
                });
                $("#avatar").val('https://q1.qlogo.cn/g?b=qq&nk='+qq+'&s=100');
            }
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
        });
        /*保存用户*/
        $(".save-user").click(function () {
            var nickname=$("#nickname").val();
            var qq=$("#qq").val();
            var email=$("#email").val();
            var avatar = 'https://q1.qlogo.cn/g?b=qq&nk='+qq+'&s=100';
            if(justNickName!=nickname||justQQ!=qq||justEmail!=email){
                Core.setCookie("just-nickname",nickname,30);
                Core.setCookie("just-qq",qq,30);
                Core.setCookie("just-email",email,30);
                Core.setCookie("just-avatar",avatar,30);
                $("#user-name").text(nickname);
                justNickName=nickname;
                justQQ=qq;
                justEmail=email;
                justAvatar=avatar;
                $("#reply-nickname").val(nickname);
                $("#reply-qq").val(qq);
                $("#reply-email").val(email);
                $("#reply-avatar").val(avatar);
            }
            $(".save-user").hide();
            $("#user-info").slideUp(250);
            $("#user-info").removeClass("user-show");
        });
        /*提交评论*/
        $("#submitCommentBtn").click(function () {
            var content = $("#emojiEditorBox .emoji-editor").html();
            $("#comment-textarea").val(content);
            if($("#nickname").val()==""){
                Core.msg("请输入昵称~");
                return;
            }else if(content==""){
                Core.msg("说点什么吧~");
                return;
            }
            Core.postAjax("/api/comment/save",$("#comment-form").serialize(),function (data) {
                Core.msg(data.msg);
                if(data.status===200){
                    $("#emojiEditorBox .emoji-editor").html("");
                    handleCommentData(data.data);
                    $(".no-comment").hide();
                    var formNickname = $("#nickname").val();
                    var formQQ = $("#qq").val();
                    var formEmail = $("#email").val();
                    var formAvatar = 'https://q1.qlogo.cn/g?b=qq&nk='+formQQ+'&s=100';
                    if(justNickName!=formNickname || justQQ!=formQQ || justEmail!=formEmail){
                        $("#user-name").text(formNickname);
                        if($("#user-info").hasClass("user-show")){
                            $(".save-user").hide();
                            $("#user-info").slideUp(250);
                            $("#user-info").removeClass("user-show");
                        }
                        if(justNickName==""){
                            $("#reply-user-form").hide();
                        }
                        $("#reply-nickname").val(formNickname);
                        $("#reply-qq").val(formQQ);
                        $("#reply-email").val(formEmail);
                        $("#reply-avatar").val(formAvatar);
                        justNickName=formNickname;
                        justQQ=formQQ;
                        justEmail=formEmail;
                        justAvatar=formEmail;
                        Core.setCookie("just-nickname",justNickName,30);
                        Core.setCookie("just-qq",justQQ,30);
                        Core.setCookie("just-email",justEmail,30);
                        Core.setCookie("just-avatar",justAvatar,30);
                    }
                }
            })
        });
        /*emoji可编辑div区域事件*/
        var sel,range;
        $("#emojiEditorBox .emoji-btn").click(function(e){
            stopPropagation(e);
            if($("#emojiEditorBox .emoji-content").is(':visible')){
                $("#emojiEditorBox .emoji-content").slideUp(250);
            }else{
                $("#emojiEditorBox .emoji-content").slideDown(250);
            }
        });
        $("#emojiEditorBox .emoji-content>ul>li").click(function(e){
            stopPropagation(e);
            if(range){
                range.collapse(false);
            }
            $("#emojiEditorBox .emoji-editor").focus();
            insertHtmlAtCaret('<img src="'+$(this).children().attr("src")+'">',sel,range);
            $("#emojiEditorBox .emoji-content").slideUp(250);
        });
        $(document).bind('click',function(){
            $("#emojiEditorBox .emoji-content").slideUp(250);
        });
        $("#emojiEditorBox .emoji-editor").mouseup(function(){
            if(window.getSelection){
                sel = window.getSelection();
                range = sel.getRangeAt(0);
            }
        }).keyup(function(){
            if(window.getSelection){
                sel = window.getSelection();
                range = sel.getRangeAt(0);
            }
        }).on('paste', function(e) {
            e.preventDefault();
            var text = null;
            if(window.clipboardData && clipboardData.setData) {
                // IE
                text = window.clipboardData.getData('text');
            } else {
                text = (e.originalEvent || e).clipboardData.getData('text/plain');
            }
            if (document.body.createTextRange) {
                if (document.selection) {
                    textRange = document.selection.createRange();
                } else if (window.getSelection) {
                    sel = window.getSelection();
                    range = sel.getRangeAt(0);
                    var tempEl = document.createElement("span");
                    tempEl.innerHTML = "&#FEFF;";
                    range.deleteContents();
                    range.insertNode(tempEl);
                    textRange = document.body.createTextRange();
                    textRange.moveToElementText(tempEl);
                    tempEl.parentNode.removeChild(tempEl);
                }
                textRange.text = text;
                textRange.collapse(false);
                textRange.select();
            } else {
                // Chrome之类浏览器
                document.execCommand("insertText", false, text);
            }
        });

        function insertHtmlAtCaret(html,sel,range){
            if(sel==null){
                sel= window.getSelection();
                range = sel.getRangeAt(0);
            }
            if (window.getSelection) {
                if (sel.getRangeAt && sel.rangeCount) {
                    var el = document.createElement("div");
                    el.innerHTML = html;
                    var frag = document.createDocumentFragment(), node, lastNode;
                    while ((node = el.firstChild)) {
                        lastNode = frag.appendChild(node);
                    }
                    range.insertNode(frag);
                    if (lastNode) {
                        range = range.cloneRange();
                        range.setStartAfter(lastNode);
                        range.collapse(false);
                        sel.removeAllRanges();
                        sel.addRange(range);
                    }
                }
            } else if (document.selection && document.selection.type != "Control") {
                document.selection.createRange().pasteHTML(html);
            }
        }
        function stopPropagation(e) {
            if (e.stopPropagation){
                e.stopPropagation();
            }else{
                e.cancelBubble = true;
            }
        }

        /*初始化回复表单域*/
        var replyForm =
            '<form id="reply-comment-form" style="display:none;" class="form-horizontal mt-10">'+
            '   <input name="sid" type="hidden" value="'+options.sid+'"/>'+
            '   <input id="replyMid" name="mid" type="hidden"  />'+
            '   <input id="replyId" name="parentId" type="hidden" />'+
            '   <input id="replyNickname" name="parentNickname" type="hidden" />'+
            '   <div id="reply-user-form" class="form-group" style="display: '+(justNickName==""?"block":"none")+'">'+
            '		<input id="reply-avatar" name="avatar" value="'+justAvatar+'" type="hidden">'+
            '       <div class="col-sm-4">'+
            '           <input id="reply-nickname" value="'+justNickName+'"  type="text" class="form-control" name="nickname" placeholder="昵称(必填)" />'+
            '       </div>'+
            '       <div class="col-sm-4">'+
            '           <input id="reply-qq" value="'+justQQ+'" type="text" class="form-control" name="qq" placeholder="QQ（可获取头像和昵称）" />'+
            '       </div>'+
            '       <div class="col-sm-4">'+
            '           <input id="reply-email" value="'+justEmail+'" type="text" class="form-control" name="email" placeholder="邮箱" />'+
            '       </div>'+
            '   </div>'+
            '   <div class="form-group">'+
            '       <div class="col-xs-12">'+
            '           <textarea id="reply-comment-textarea" name="content" style="display: none"></textarea>'+
            '           <div id="replyEmojiEditorBox">'+
            '               <div contenteditable="true" class="emoji-editor"></div>' +
            '               <div class="emoji-tool">' +
            '                   <div class="emoji-btn"><img src="'+options.emojiServer+'0.png'+'"/></div>'+
            '                   <div class="emoji-content">' +
            '                       <ul>';
        replyForm += emojisLi;
        replyForm +='               </ul>'+
            '                   </div>'+
            '               </div>'+
            '           </div>'+
            '       </div>'+
            '   </div>'+
            '   <div>'+
            '       <button id="submitReplyCommentBtn" type="button" class="btn btn-pri">发表评论</button>'+
            '   </div>'+
            '</form>';
        $("#comment-ul").append(replyForm);
        /*回复区域emoji事件*/
        var replySel,replyRange;
        $("#replyEmojiEditorBox .emoji-btn").click(function(e){
            stopPropagation(e);
            if($("#replyEmojiEditorBox .emoji-content").is(':visible')){
                $("#replyEmojiEditorBox .emoji-content").slideUp(250);
            }else{
                $("#replyEmojiEditorBox .emoji-content").slideDown(250);
            }
        });
        $("#replyEmojiEditorBox .emoji-content>ul>li").click(function(e){
            stopPropagation(e);
            $('#replyEmojiEditorBox .emoji-editor').focus();
            if(replyRange){
                replyRange.collapse(false);
            }
            $("#replyEmojiEditorBox .emoji-editor").focus();
            insertHtmlAtCaret('<img src="'+$(this).children().attr("src")+'">',replySel,replyRange);
            $("#replyEmojiEditorBox .emoji-content").slideUp(250);
        });
        $(document).bind('click',function(){
            $("#replyEmojiEditorBox .emoji-content").slideUp(250);
        });
       /* $("#replyEmojiEditorBox .emoji-editor").blur(function(){
            replySel = window.getSelection();
            replyRange = replySel.getRangeAt(0);
            replyRange.deleteContents();
        });*/

        $("#replyEmojiEditorBox .emoji-editor").mouseup(function(){
            if(window.getSelection){
                replySel = window.getSelection();
                replyRange = replySel.getRangeAt(0);
            }
        }).keyup(function(){
            if(window.getSelection){
                replySel = window.getSelection();
                replyRange = replySel.getRangeAt(0);
            }
        }).on('paste', function(e) {
            e.preventDefault();
            var text = null;
            if(window.clipboardData && clipboardData.setData) {
                // IE
                text = window.clipboardData.getData('text');
            } else {
                text = (e.originalEvent || e).clipboardData.getData('text/plain');
            }
            if (document.body.createTextRange) {
                if (document.selection) {
                    textRange = document.selection.createRange();
                } else if (window.getSelection) {
                    replySel = window.getSelection();
                    replyRange = sel.getRangeAt(0);
                    var tempEl = document.createElement("span");
                    tempEl.innerHTML = "&#FEFF;";
                    replyRange.deleteContents();
                    replyRange.insertNode(tempEl);
                    textRange = document.body.createTextRange();
                    textRange.moveToElementText(tempEl);
                    tempEl.parentNode.removeChild(tempEl);
                }
                textRange.text = text;
                textRange.collapse(false);
                textRange.select();
            } else {
                // Chrome之类浏览器
                document.execCommand("insertText", false, text);
            }
        });

        /*回复 qq失去焦点*/
        $("#reply-qq").blur(function () {
            var qq = $(this).val();
            if(qq!=""){
                Core.postAjax("/api/qq/"+qq,{},function (data) {
                    if(data.status===200){
                        $("#reply-nickname").val(data.data.name);
                    }
                });
                $("#reply-avatar").val('https://q1.qlogo.cn/g?b=qq&nk='+qq+'&s=100');
            }
        });
        /*提交回复内容*/
        $("#submitReplyCommentBtn").on('click',function () {
            var replyContent = $("#replyEmojiEditorBox .emoji-editor").html();
            $("#reply-comment-textarea").val(replyContent);
            if($("#reply-nickname").val()==""){
                Core.msg("请输入昵称~");
                return;
            }else if(replyContent==""){
                Core.msg("说点什么吧~");
                return;
            }
            Core.postAjax("/api/comment/save",$("#reply-comment-form").serialize(),function (data) {
                Core.msg(data.msg);
                if(data.status==200){
                    $("#replyEmojiEditorBox .emoji-editor").html("");
                    $("#reply-comment-form").hide();
                    $(".reply[style='display: none;']").next().hide();
                    $(".reply[style='display: none;']").show();
                    handleCommentData(data.data,1);
                    if(Core.getCookie("just-nickname")==""){
                        Core.setCookie("just-nickname",$("#reply-nickname").val(),30);
                        Core.setCookie("just-qq",$("#reply-qq").val(),30);
                        Core.setCookie("just-email",$("#reply-email").val(),30);
                        Core.setCookie("just-avatar",$("#reply-avatar").val(),30);
                    }
                }
            })
        });

        /*分页获取评论数据*/
        function init(pageNum) {
            Core.postAjax("/api/comments",{"sid":options.sid,"pageNum": (pageNum==null? 1 : pageNum)},function (data) {
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
                            '			<img src="'+(value.avatar||"/static/img/user-default.png")+'" onerror="this.src=\'/static/img/user-default.png\'" />'+
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
                                    '			<img src="'+(nValue.avatar||"/static/img/user-default.png")+'" onerror="this.src=\'/static/img/user-default.png\'" />'+
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
                    }
                    $("#comment-ul").append(commentOne);
                }
            })
        }
        init();

        /*加载更多*/
        $(options.id).on("click","#comment-more",function () {
            init($(this).attr("data-page"));
        });
        /*link至评论*/
        $(options.id).on("click",".comment-link",function () {
            var commentLinkId = $(this).attr("data-link");
            $("html,body").animate({
                scrollTop:$("#"+commentLinkId).offset().top-55},{duration: 300,easing: "swing"})
        });
        /*回复*/
        $(options.id).on("click",".reply",function () {
            replySel=null,replyRange=null;
            $("#replyEmojiEditorBox .emoji-editor").html("");
            var replyId=$(this).attr("reply-id");
            var replyMid=$(this).attr("reply-mid");
            var replyNickname=$(this).attr("reply-nickname");
            $("#reply-comment-form").css("display","none");
            $(this).parent().after($("#reply-comment-form"));
            $("#reply-comment-form").slideDown(250);
            $("#replyId").val(replyId);
            $("#replyMid").val(replyMid);
            $("#replyNickname").val(replyNickname);
            $(".cancel-reply:visible").hide();
            $(".reply").removeAttr("style");
            $(this).hide();
            $(this).next().show();
        });
        /*取消回复*/
        $(options.id).on("click",".cancel-reply",function () {
            $("#reply-comment-form").slideUp(250);
            $(this).hide();
            $(".reply").removeAttr("style");
        });
        /*评论点赞*/
        $(options.id).on("click",".comment-support",function () {
            var $thisLove = $(this);
            Core.postAjax("/api/comment/love",{"commentId":$(this).attr("biz-id")},function (data) {
                if(data.status===200){
                    $thisLove.text(parseInt($thisLove.text())+1);
                }else{
                    Core.msg(data.msg,2);
                }
            });
        });


        function handleCommentData(data,isReply){
            var html='<li>'+
                '<div class="comment-body">'+
                ' <div class="comment-user-img">'+
                '<img src="'+data.avatar+'" onerror="this.src=\'/static/img/user-default.png\'" />'+
                '</div>'+
                '<div class="comment-info">'+
                '<div class="comment-top">'+
                '<span class="comment-nickname"> <a href="javascript:void(0)">'+data.nickname+'</a> </span>'+
                '<span class="comment-time">'+Core.getDateDiff(new Date(data.createTime))+'</span>'+
                ' </div>'+
                ' <div class="comment-content">';
            if(isReply){
                var pNickname= $("#reply-comment-form").parent().find(".comment-top .comment-nickname>a:first-child").text();
                var pText =  $("#reply-comment-form").parent().find(".comment-content>.comment-content-text").html();
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
    };

    return core;
})(Core, window);

//回到顶部
$(window).scroll(function() {
    var scroTop = $(window).scrollTop();
    if (scroTop > 100) {
        $('.return_top').css("opacity","0.4");
    } else {
        $('.return_top').css("opacity","0");
    }
});

$(function () {
    $('.return_top').click(function(){
        $("html,body").animate({scrollTop:0},"fast");
    });
});

