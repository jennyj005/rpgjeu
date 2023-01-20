package jeu;

public class Bow extends Weapon
{
    private int nbFleches;
    public Bow(String name, int nbFleches, int degatWeapon)
    {
        super(name, degatWeapon);
        this.nbFleches = nbFleches;
    }
}
