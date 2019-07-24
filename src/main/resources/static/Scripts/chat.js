var NotAuthenticatedMsg = "Not Authenticated";
var InvalidInputMsg = "Invalid Input";

$(function() {
    isAuthenticatedHandler();
    getChatHandler();
    updateOnlineUsers();
    setInterval(function () {
        isAuthenticatedHandler();
        getChatHandler();
    }, 30000);

    setInterval(function () {
        updateOnlineUsers();
    }, 10000);

    //Handle message submission
    $("#sendMessage").submit(function (event) {
        $this = $(this);
        $error = $this.find(".error");
        $error.removeClass("active");
        event.preventDefault();
        var message = $this.find("input[name='message']").val();
        $.post( "api/sendMessage",
            { userName: "None", message: message } ).done(function(json) {
            if (json.isError) {
                switch (json.errorMessage) {
                    case NotAuthenticatedMsg:
                        debugger;
                        window.location.href = "index.html";
                        break;
                    case InvalidInputMsg:
                        $error.text("Invalid input").addClass("active");
                        break;
                    default:
                        $error.text("Generic error").addClass("active");
                        break;
                }
            } else {
                chatHandler(json);
            }
        }).fail(function() {
            $error.text("API error").addClass("active");
        });
        $this.find("input[name='message']").val("");
    });

    //Handle logout submission
    $("#logout").submit(function (event) {
        event.preventDefault();
        $.get("api/logout").done(function(json) {
            window.location.href = "index.html";
        });
    });

});

//Handle the api response of is authenticated
function isAuthenticatedHandler() {
    $.get( "api/isAuthenticated").done(function(json) {
        if (!json.isAuthenticated) {
            window.location.href = "index.html";
        }
        var template = "Welcome {{ userName }}";
        $("#welcome").html(templateVariableReplace(template, json));
    });
}

//Handle the api response of th chat
function getChatHandler() {
    $.get( "api/getMessages").done(function(json) {
        if (json.isError) {
            window.location.href = "index.html";
        }
        chatHandler(json);
    });
}

//Update the chat data
function chatHandler(json) {
    var template = `
           <ul>
               {{ self ->
                    <li><div class="li-inner"><div class="username">{{ _v.userName }}</div><div class="message">{{ _v.message }}</div></div></li> }}
            </ul>`;
    data = templateVariableReplace(template, json.object)
    $("#chat").html(data);

    setTimeout(function() {
        $("#chat").scrollTop($("#chat").prop("scrollHeight"));
    }, 300);
}

function updateOnlineUsers() {
    $.get( "api/getOnlineUsers").done(function(json) {
        if (json.isError) {
            window.location.href = "index.html";
        }
        var template = `
           <ul>
               {{ self ->
                    <li><div class="user">{{ _v.userName }}</li> }}
            </ul>`;
        $("#users").html(templateVariableReplace(template, json.object));
    });
}