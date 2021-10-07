if (!window.WebSocket) {
    alert("the browser does not support WebSocket");
}
const socket = new WebSocket("wss://localhost:8443/ws/transaction/status");
socket.onmessage = function (event) {
    const chat = document.getElementById("status");
    chat.innerHTML = chat.innerHTML + event.data + "<br />";
};

socket.onclose = function (event) {
    alert(event.code + ": " + event.reason);
}

function send(message) {
    if (socket.readyState === WebSocket.OPEN) {
        const bean = {id: message};
        socket.send(JSON.stringify(bean));
    } else {
        alert("socket is not open.");
    }
    return false;
}

document.getElementById("id").onkeypress = function () {
    if (event.keyCode === 13) {
        const input = document.getElementById("id");
        send(input.value);
        input.value = ''
    }
};

document.getElementById("form").onsubmit = function () {
    return false;
};
