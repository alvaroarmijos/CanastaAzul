package com.armijos.democanasta;

class Carrito {

    private String nombre;
    private  String urlPhoto;
    private String descripcion;
    private String precio;
    private String cantidad;

    public Carrito(String nombre, String urlPhoto, String descripcion, String precio,String cantidad) {
        this.nombre = nombre;
        this.urlPhoto = urlPhoto;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad=cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}