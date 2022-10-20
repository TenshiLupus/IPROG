package handlers

import (
	"fmt"
	"log"
	"net/http"

	"github.com/CloudyKit/jet/v6"
	"github.com/gorilla/websocket"
)

var wsChan = make(chan WsPayload)

var clients = make(map[WebSocketConnection]string)

// views on the jet viewset
var views = jet.NewSet(
	jet.NewOSFileSystemLoader("./html"),
	jet.InDevelopmentMode(),
)

var upgradeConnection = websocket.Upgrader{
	ReadBufferSize:  1024,
	WriteBufferSize: 1024,
	CheckOrigin:     func(r *http.Request) bool { return true },
}

func Home(w http.ResponseWriter, r *http.Request) {
	err := renderPage(w, "home.jet", nil)
	if err != nil {
		log.Println(err)
	}
}

// WsJsonResponse defines the response sent back from websocket

type WebSocketConnection struct {
	*websocket.Conn
}
type WsJsonResponse struct {
	Action      string `json:"action"`
	Message     string `json:"message"`
	MessageType string `json:"messge_type"`
}

type WsPayload struct {
	Action   string              `json:"action"`
	Username string              `json:"username"`
	Message  string              `json:"message"`
	Conn     WebSocketConnection `json:"-"`
}

// WsEndPoint upgrades connection to websocket
func WsEndpoint(w http.ResponseWriter, r *http.Request) {
	ws, err := upgradeConnection.Upgrade(w, r, nil)
	if err != nil {
		log.Println(err)
	}

	log.Println("Client connected to endpoint")

	//when someone new connects
	conn := WebSocketConnection{Conn: ws}
	clients[conn] = ""

	//instantiating an empty instance of WsJsonResponse
	var response WsJsonResponse
	response.Message = `<em><small>Connected to Server</small></em>`

	err = ws.WriteJSON(response)
	if err != nil {
		log.Println(err)

	}

	go ListenForWs(&conn)
}

//Listens for incoming packets are come through the established connection
func ListenForWs(conn *WebSocketConnection) {
	//execute this function, if the upper function stops runnning
	defer func() {
		if r := recover(); r != nil {
			log.Println("Error", fmt.Sprintf("%v", r))
		}
	}()

	var payload WsPayload

	for {
		err := conn.ReadJSON(&payload)
		if err != nil {

		} else {
			payload.Conn = *conn
			wsChan <- payload
		}

	}
}

func ListenToWsChannel() {
	var response WsJsonResponse

	//forever
	for {
		e := <-wsChan

		response.Action = "Got here"
		response.Message = fmt.Sprintf("Some message, and action was %s", e.Action)
		BroadCastToAll(response)
	}
}

func BroadCastToAll(response WsJsonResponse) {
	for client := range clients {
		err := client.WriteJSON(response)
		if err != nil {
			log.Println("websocket err")
			_ = client.Close()
			delete(clients, client)
		}
	}
}

func renderPage(w http.ResponseWriter, tmpl string, data jet.VarMap) error {
	view, err := views.GetTemplate(tmpl)
	if err != nil {
		log.Println(err)
		return err
	}

	err = view.Execute(w, data, nil)
	if err != nil {
		log.Println(err)
		return err
	}

	return nil
}
