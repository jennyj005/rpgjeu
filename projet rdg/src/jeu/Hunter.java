package jeu;

public class Hunter extends Hero
{

    public Hunter (String name, int hp)
    {
        super(name,hp, 30);
        addObjects(new Bow("arc", 15, 35));
        addObjects(new Armor("capuchon", 25));
    }

}
