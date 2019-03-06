<%@ page contentType="text/html; charset=UTF-8" %>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/welcome.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/classCommon.css" />
<script>

    var loginFormSelector = "#login-form";
    var errorSelector = ".errors";
    var loginFormPivot = "#login-pivot";
    var visible = false;

    jQuery(document).ready(function(){
      var params = new URLSearchParams(window.location.search);
      if (params.has('error')) {
        toggleLoginForm();
      }
    });

    $(document).on('submit','#login-form',function(event) {
      var array = $('input');
      var hasErrors = false;
      $("span[class|='errors']").remove();
      for (var i = 0; i < array.length; i++) {
        if(array[i].value.trim().length === 0) {
          $(array[i]).after("<span class=\"errors\">Поле должно быть заполнено!</span>");
          hasErrors = true;
        }
      }
      if (hasErrors) event.preventDefault();
    });

    function toggleLoginForm() {
        var form = $(loginFormSelector);
        formStyle = document.getElementById("login-form").style;
        formStyle.left = $(loginFormPivot).position().left - form.width()/2-2 + 'px';
        formStyle.top = $(loginFormPivot).position().top - form.height()*1.3 - 36 + 'px';
        form.find("input").first().focus();
        if(!visible) {
            form.show(100, function(){
                form.find("input").first().focus();
                });
        } else {
            form.hide(200);
        }
        visible = !visible;
    }

    function onWindowResize() {
        visible = !visible;
        toggleLoginForm();
    }

    window.addEventListener('resize', onWindowResize, false);
</script>