package main

import (
	"log"
	"net/http"
	"wsp/utility/handlers"
)

func main() {
	mux := routes()

	log.Println("Starting channel listener")
	go handlers.ListenToWsChannel()

	log.Println("starting web server on port 8080")

	_ = http.ListenAndServe(":8080", mux)
}
