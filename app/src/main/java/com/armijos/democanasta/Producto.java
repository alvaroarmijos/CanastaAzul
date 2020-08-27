package com.armijos.democanasta;

import android.os.Parcel;
import android.os.Parcelable;


class Producto implements Parcelable {

    private String nombre;
    private  String urlPhoto;
    private String descripcion;
    private String precio;
    private String cantidad;



    public Producto(String nombre, String urlPhoto, String descripcion, String precio, String cantidad) {
        this.nombre = nombre;
        this.urlPhoto = urlPhoto;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad=cantidad;
    }

    protected Producto(Parcel in) {
        nombre = in.readString();
        urlPhoto = in.readString();
        descripcion = in.readString();
        precio = in.readString();
        cantidad = in.readString();
    }

    public static final Creator<Producto> CREATOR = new Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel in) {
            return new Producto(in);
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size];
        }
    };

    public String getNombre() {
        return nombre;
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

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nombre);
        parcel.writeString(urlPhoto);
        parcel.writeString(descripcion);
        parcel.writeString(precio);
        parcel.writeString(cantidad);
    }
}
