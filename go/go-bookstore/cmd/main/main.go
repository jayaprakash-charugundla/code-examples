package main

import (
	"fmt"
	"github.com/gorilla/mux"
	"github.com/jc/go-bookstore/pkg/routes"
	"log"
	"net/http"
)

func main() {
	r := mux.NewRouter()
	routes.RegisterBookStoreRoutes(r)
	http.Handle("/", r)
	fmt.Printf("Starting server at port 9010\n")
	log.Fatal(http.ListenAndServe("localhost:9010", r))
}
