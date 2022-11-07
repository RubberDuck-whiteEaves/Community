/**
 * 提交回复
 */
function post() {
    // 获取id为question_id标签的value值
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId, 1, content);
}

function comment2target(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容");
        return;
    }

    // 通过post方法发送json对象
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        // 需要通过JSON.stringify方法将以下结构转化为json字符串，否则传输的是javascript对象
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        // response为ajax请求后，返回的服务器响应
        success: function (response) {
            if (response.code == 200) {
                // 界面进行实时刷新
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        // $('#myModal').modal({});
                        window.open("https://github.com/login/oauth/authorize?client_id=ad9fe79a0a0e7dc9c4c6&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        // 使用localStorage，将数据保存在前端，与 cookie 不同，Web 存储对象不会随每个请求被发送到服务器。
                        // 因此，我们可以保存更多数据。大多数现代浏览器都允许保存至少 5MB 的数据（或更多），并且具有用于配置数据的设置。
                        // 还有一点和 cookie 不同，服务器无法通过 HTTP header 操纵存储对象。一切都是在 JavaScript 中完成的。
                        // 存储绑定到源（域/协议/端口三者）。也就是说，不同协议或子域对应不同的存储对象，它们之间无法访问彼此数据。
                        // 通过js控制window的跳转和关闭，这里即window跳转到登录请求界面，登录成功会自动跳转回redirect_uri，
                        // 在首页的域中使用localStorage存储一个值来判断首页是否直接需要关闭，这部分的判断则可以直接在index.html通过js完成
                        window.localStorage.setItem("closable", "true");
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    comment2target(commentId, 2, content);
}

/**
 * 展开二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);

    // 获取一下二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        // 折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                // 由于获取到的data，即comments是按创建时间顺序排列的，但是每个标签都被prepend在最前面，则会产生一种倒序的效果
                // 为了还是按创建时间顺序排列，所以需要将data reverse
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<span/>", {
                        "class": "menu pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })).append($("<div/>", {
                        "html": comment.content
                    }));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);


                    // subCommentContainer.prepend(c);
                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}

function showSelectTag() {
    $("#select-tag").show();
}

// 通过js控制，在前端选择某个tag后，可以添加到input标签的value中
function selectTag(e) {
    var value = e.getAttribute("data-tag");
    // 获取id=tag标签（即publish.html中input标签）的值
    var previous = $("#tag").val();

    if (previous) {
        // 若previous!=null，即标签中存在初值，则在初值后面append value
        var index = 0;
        var appear = false; // 记录value是否已经作为一个独立的标签出现过
        while (true) {
            index = previous.indexOf(value, index); // value字符串在previous中出现的位置
            if (index == -1) break;
            // 判断previous中出现的value是否是另一个标签的一部分
            // 即value的前一个和后一个字符都是逗号","或者没有字符时，才说明value是一个独立的标签
            if ((index == 0 || previous.charAt(index - 1) == ",")
                && (index + value.length == previous.length || previous.charAt(index + value.length) == ",")
            ) {
                appear = true;
                break;
            }
            index++; //用于搜索下一个出现位置
        }
        if (!appear) {
            // 若value没有作为一个独立的标签出现过
            $("#tag").val(previous + ',' + value);
        }
    }
    else {
        // 否则直接在id=tag标签后面append value
        $("#tag").val(value);
    }
}