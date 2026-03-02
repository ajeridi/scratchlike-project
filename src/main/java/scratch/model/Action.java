package scratch.model;

public abstract class Action {
    private String nom;
    private double value;

    public Action(double value, String nom) {
        this.value = value;
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    abstract public MyLine execute(Cursor cursor);
}
