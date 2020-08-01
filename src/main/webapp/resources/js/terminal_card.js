'use strict';

var CommandInput = document.querySelector('#CommandInput');
var CommandOutput = document.querySelector('#CommandOutput');
var loading = document.getElementById("loading");

function showLoading(){
        loading.innerHTML = "<img id=\"loading-image\" src=\"/resources/images/loading.gif\" alt=\"Loading...\" />";
        loading.style.display = "block";
}
function hideLoading(){
        loading.innerHTML = "";
        loading.style.display = "none";
}

function processing(){
    showLoading();
    var command = CommandInput.value;
    console.log(command);
    var xhrPost = new XMLHttpRequest();
    xhrPost.open("POST", "/api/execute", true);
    xhrPost.setRequestHeader("Content-Type", "application/json");
    var data = JSON.stringify({"command": command });
    console.log(data);
    xhrPost.onload = function() {
        console.log(xhrPost.responseText);
        var response = xhrPost.responseText;
        if(xhrPost.status == 200 ) {
            hideLoading();
            CommandOutput.innerHTML = "<p>"+xhrPost.responseText+" </p>";
            CommandOutput.style.display = "block";
        }
    }
    xhrPost.send(data);
}
