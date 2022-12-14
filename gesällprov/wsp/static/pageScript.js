let socket = null;
    let o = document.getElementById("output");
    let userField = document.getElementById("username");
    let messageField = document.getElementById("message");

    //before leaving the page change state of message
    window.onbeforeunload = function () {
        console.log("Leaving");
        let jsonData = {};
        jsonData["action"] = "left";
        socket.send(JSON.stringify(jsonData))
    }

    //when the browser loads the tempkate. Open the browser to connect to the server, handled by the go handler
    document.addEventListener("DOMContentLoaded", function () {
        socket = new ReconnectingWebSocket("ws://127.0.0.1:8080/wsp", null, {debug: true, reconectInterval: 3000});

        const offline = `<span class="badge bg-danger">Not connected</span>`
        const online = `<span class="badge bg-success">Connected</span>`
        let statusDiv = document.getElementById("status");

        //Debugging events
        socket.onopen = () => {
            console.log("Successfully connected");
            statusDiv.innerHTML = online;
        }

        socket.onclose = () => {
            console.log("connection closed");
            statusDiv.innerHTML = offline;
        }

        socket.onerror = error => {
            console.log("there was an error");
            statusDiv.innerHTML = offline;
        }

        //handle the input packet acccordingly
        socket.onmessage = msg => {
            let data = JSON.parse(msg.data);
            console.log("Action is", data.action);

            switch (data.action) {
                //update list of users
                case "list_users":
                    let ul = document.getElementById("online_users");
                    while (ul.firstChild) ul.removeChild(ul.firstChild);

                    if (data.connected_users.length > 0) {
                        data.connected_users.forEach(function (item) {
                            let li = document.createElement("li");
                            li.appendChild(document.createTextNode(item));
                            ul.appendChild(li);
                        })
                    }
                    break;

                //Chat message
                case "broadcast":
                    o.innerHTML = o.innerHTML + data.message + "<br>";
                    break;
            }
        }

        //For any change in the userField 
        userField.addEventListener("change", function () {
            let jsonData = {};
            jsonData["action"] = "username";
            jsonData["username"] = this.value;
            socket.send(JSON.stringify(jsonData));
        })

        //Send the message of the input fields
        messageField.addEventListener("keydown", function (event) {
            if (event.code === "Enter") {
                if (!socket) {
                    console.log("no connection");
                    return false
                }

                if ((userField.value === "") || (messageField.value === "")) {
                    errorMessage("Fill out username and message!");
                    return false;
                } else {
                    sendMessage()
                }
                event.preventDefault();
                event.stopPropagation();
            }
        })

        //Alternatively Send the message of the iputfields after clicking on the send messsage button
        document.getElementById("sendBtn").addEventListener("click", function () {
            if ((userField.value === "") || (messageField.value === "")) {
                errorMessage("Fill out username and message!");
                return false;
            } else {
                sendMessage()
            }
        })
    })

    //sends the json over to the server
    function sendMessage() {
        let jsonData = {};
        jsonData["action"] = "broadcast";
        jsonData["username"] = userField.value;
        jsonData["message"] = messageField.value;
        socket.send(JSON.stringify(jsonData))
        messageField.value = "";
    }