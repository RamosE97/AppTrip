package com.example.ernestoramos.apptrip.Sesion;

public  class Sesion {
    private static Sesion INSTANCE = null;

    //Atributos
        private String Id;
        private String Nombre;
        private String correo;
        private Sesion() {};

        public static Sesion getInstance() {
            if (INSTANCE == null) {
                INSTANCE = new Sesion();
            }
            return(INSTANCE);
        }

   public void CerrarSesion(){
           this.Id="";
           this.Nombre="";
           this.correo="";
   }
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
