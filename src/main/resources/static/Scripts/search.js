var NotAuthenticatedMsg = "Not Authenticated";
var InvalidInputMsg = "Invalid Input";

$(function() {
    isAuthenticatedHandler();
    setInterval(function () {
        isAuthenticatedHandler();
    }, 30000);

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

function sendSearch(byUsername) {
    var $error = $("#sendSearch").find(".error");
    var inputVaue = $("#sendSearch").find("input[name='text']").val()
    var url = "";
    var data = {}
    if (byUsername) {
        url = "api/searchByUserName";
        data = { "userName": inputVaue }
    } else {
        url = "api/searchByMessage";
        data = { "message": inputVaue }
    }
    $error.removeClass("active");
    $.get( url, data ).done(function(json) {
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
            searchHandler(json);
        }
    }).fail(function() {
        $error.text("API error").addClass("active");
    });
}

function searchHandler(json) {
    var template = `
           <ul>
               {{ self ->
                    <li><div class="li-inner"><div class="username">{{ _v.userName }}</div><div class="message">{{ _v.message }}</div></div></li> }}
            </ul>`;
    data = templateVariableReplace(template, json.object)
    $("#chat").html(data);
}