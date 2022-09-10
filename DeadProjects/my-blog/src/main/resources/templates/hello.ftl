<#import "main.ftl" as main>

<@main.main>
<h1>Hello world</h1>
<#list posts as post>
    <h1>${post.tittle}</h1>

</#list>
</@main.main>
