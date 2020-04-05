package com.example.festival.utilitaires;

public class Manager {
    private static int id;
    private static String nom;
    private static String prenom;
    private static boolean connecte=false;

    public Manager(int id, String nom, String prenom){
        this.id=id;
        this.nom=nom;
        this.prenom=prenom;
        this.connecte=true;
    }

    public  Manager(){
        this.connecte=false;
    }


    public static void deconnexion(){
        connecte=false;
    }

    public static int getId() {
        return id;
    }

    public static String getNom() {
        return nom;
    }

    public static String getPrenom() {
        return prenom;
    }

    public static boolean isConnecte() { return connecte; }
}
