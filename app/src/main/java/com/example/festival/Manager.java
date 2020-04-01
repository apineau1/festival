package com.example.festival;

public class Manager {

    private static Manager instance = null;

    private int id;
    private String nom;
    private String prenom;

    private Manager(int id, String nom, String prenom){
        this.id=id;
        this.nom=nom;
        this.prenom=prenom;
    }

    public synchronized static void setInstance(int id, String nom, String prenom){
        if(instance==null) instance=new Manager(id, nom, prenom);
    }

    public synchronized static Manager getInstance() throws Exception {
        if(instance==null) throw new Exception();
        return instance;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }
}
