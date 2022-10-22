package main

import (
	"net/http"
	"wsp/utility/handlers"
	"github.com/bmizerany/pat"
)

//setup the routes of the local server
func routes() http.Handler {
	mux := pat.New()

	mux.Get("/", http.HandlerFunc(handlers.Home))
	mux.Get("/wsp", http.HandlerFunc(handlers.WsEndpoint))

	fileserver := http.FileServer(http.Dir("./static/"))
	mux.Get("/static/", http.StripPrefix("/static", fileserver))
	return mux
}
