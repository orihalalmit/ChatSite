$(function() {
    //Handle login submission
    $("#login").submit(function (event) {
        $this = $(this);
        event.preventDefault();
        $error = $this.find(".error");
        $error.removeClass("active");
        var username = $this.find("input[name='username']").val();
        $.post( "api/login",
                { userName: username } ).done(function(json) {
            if(json.isAuthenticated) {
                window.location.href = "chat.html";
            } else {
                $error.text("Invalid input").addClass("active");
            }
        }).fail(function() {
            $error.text("API error").addClass("active");
        });
    });
});