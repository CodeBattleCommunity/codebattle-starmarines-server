<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/welcome.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/classCommon.css" />
<script>

    var loginFormSelector = "#login-form";
    var errorSelector = ".errors";
    var loginFormPivot = "#login-pivot";
    var visible = false;


    jQuery(document).ready(function(){
        if(jQuery(loginFormSelector + " " + errorSelector).size() != 0){
            toggleLoginForm();
        }
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