<#macro main>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <title>MyBlog</title>

    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="/css/materialize.min.css"  media="screen,projection"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</head>
<body>
    <nav>
        <div class="nav-wrapper">
            <form>
                <div class="input-field">
                    <input name="query" id="search" type="search" required>
                    <label class="label-icon" for="search"><i class="material-icons">search</i></label>
                    <i class="material-icons">close</i>
                </div>
            </form>
        </div>
    </nav>
    <#nested>

    <script type="text/javascript" src="/js/materialize.min.js"></script>
</body>
</html>

</#macro>