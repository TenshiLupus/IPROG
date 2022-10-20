package main

import (
	"net/http"
	"wsp/utility/handlers"

	"github.com/bmizerany/pat"
)

func routes() http.Handler {
	mux := pat.New()

	mux.Get("/", http.HandlerFunc(handlers.Home))
	mux.Get("/wsp", http.HandlerFunc(handlers.WsEndpoint))

	return mux
}
