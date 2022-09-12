package routes

import (
	"github.com/gorilla/mux"
	"github.com/jc/go-bookstore/pkg/controllers"
)

var RegisterBookStoreRoutes = func(router *mux.Router) {
	router.HandleFunc("/books", controllers.CreateBook).Methods("POST")
	router.HandleFunc("/books", controllers.GetBook).Methods("GET")
	router.HandleFunc("/books/{id}", controllers.GetBookById).Methods("GET")
	router.HandleFunc("/books/{id}", controllers.DeleteBook).Methods("DELETE")
	router.HandleFunc("/books/{id}", controllers.UpdateBook).Methods("PUT")
}
