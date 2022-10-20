let socket = null;
document.addEventListener('DOMContentLoaded', () => {

    //When the project URL is requested, the socket will open and run an assigned lambda function
    socket = new WebSocket("ws://127.0.0.1:8080/wsp")

    socket.onopen = () => {
        console.log("Sucessfully connected")
    }

    
});

