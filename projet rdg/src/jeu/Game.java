package jeu;

import java.util.*;
import java.util.stream.Collectors;

//import static jeu.Food.generateFood;

public class Game
{
    private static int nombre_h;
    private static int nombre_e;
    public static ArrayList<String>  liste;
    public static int niveau =0;
    public static int NIVEAU_MAX = 4;

    private static List<Hero> listeHeros= new ArrayList<>();
    private static List<Enemy> listeEnemies= new ArrayList<>();

    private Combattant combattant;

    public static void main(String[]args) {
        initGame();

        int compteurCombats = 0;
        boolean enemiesAllDead = false;
        boolean herosAllDead = false;

        while (compteurCombats <= 5 && !herosAllDead) {
            if (compteurCombats == 5) {
                compteurCombats++;
                // combat du boss
            } else {
                // Autres combats

                initEnnemies(compteurCombats);
                enemiesAllDead = false;

                // Début de boucle pour un combat
                while (!enemiesAllDead && !herosAllDead) {
                    Collections.shuffle(listeHeros);

                    //Tous les Héros attaquent en même temps
                    for (int i = 0; i < listeHeros.size(); i++) {

                        Enemy enemyCourant = listeEnemies.get(i);
                        boolean actionDone = false;
                        while (!actionDone) {
                            System.out.println("Que voulez vous faire : 1. Attaquer ou Soigner ( pour le Healer), 2. Se défendre, 3. Utiliser un consommable : ");
                            Scanner scanner = new Scanner(System.in);
                            int choix = scanner.nextInt();

                            Hero heroCourant = listeHeros.get(i);

                            if (choix == 1) {
                                if (heroCourant instanceof Healer) {
                                    for (int j = 0; j < listeHeros.size(); j++) {
                                        System.out.println(j + " - Lequel des héros voulez vous soigner ? ");
                                    }
                                    int heroChoisit = scanner.nextInt();
                                    ((Healer) heroCourant).soignerHero(listeHeros.get(heroChoisit));
                                }
                                else {
                                    int j;

                                    for (j = 0; j < listeEnemies.size(); j++) {

                                        if (enemyCourant.hp >0)
                                        {
                                            System.out.println(j+ listeEnemies.get(j).name + " Voulez vous attaquer ce monstre ? " + listeEnemies.get(j).hp);
                                        }
                                        else if(enemyCourant.hp <= 0)
                                        {
                                            listeEnemies.remove(enemyCourant);
                                            System.out.print(j+ " ");
                                            enemyCourant.isDead();
                                            System.out.println(j+ " Voulez vous attaquer ce monstre ? " + listeEnemies.get(j).hp);

                                        }

                                    }
                                    int enemyatt = scanner.nextInt();
                                    heroCourant.attaquer(listeEnemies.get(enemyatt));


                                    actionDone = true;
                                }
                            } else if (choix == 2) {
                                heroCourant.isDefense = true;

                                actionDone = true;
                            } else {
                                heroCourant.afficherItem();
                                int choixConsommable = scanner.nextInt();

                                actionDone = heroCourant.utiliserConsommable(choixConsommable);

                                if (!actionDone) {
                                    System.out.println("Vous n'avez rien consommé ");
                                }
                            }

                        }

                        for (int j = 0; j < listeHeros.size(); j++) {

                           enemyCourant.attaquer(listeHeros.get(j));
                            System.out.println(j + " Heros attaqués  " + listeHeros.get(j).hp);

                        }

                    }
                }


                if (!herosAllDead) {
                    // vérification que les héros ne sont pas morts
                    System.out.println("Vos héros ne sont pas morts");
                }
                listeHeros.get(0).isDead();

                List<Enemy> enemiesToRemove = new ArrayList<>();
                Enemy enemyCurrent;
                enemiesAllDead = true;
                //Vérification que tous les ennemies soient morts
                for (int i = 0; i < listeEnemies.size(); i++) {
                    enemyCurrent = listeEnemies.get(i);
                    enemiesAllDead &= enemyCurrent.isDead();
                }

                if (enemiesAllDead) {
                    // vérification que les ennemies soient morts
                    System.out.println("Les ennemies sont morts");
                }
            }
            if (enemiesAllDead) {
                // tous les héros ont gagné le combat
                // choisir ce que le héro veut augmenter avec scanner, parcourir la liste hero
                // et verifier pour chaque hero si ils sont vivants pour pouvoir augmenter pour chacun

                for (int i = 0; i < listeHeros.size(); i++) {
                    System.out.println("Vous avez gagné le combat, que voulez vous faire : 1. Augmenter les dégats que vous causez, 2. Augmenter votre résistance aux attaques " +
                            "3. Augmenter l'efficacité de la potion et de la nourriture , 4.Augmenter le nombre de potions ou de nourriture," +
                            " 5. Augmenter le nombre de flèches pour le Hunter, diminuer le coût en mana pour les sorceleurs ou l'efficacité de leurs sorts" +
                            " : ");
                    Scanner scanner = new Scanner(System.in);
                    int choix = scanner.nextInt();

                    if (choix == 4) {
                        choixfood(i, scanner);
                    }
                }

            }
            compteurCombats++;
        }


        if (herosAllDead) {
            // message tous les héros sont morts partie terminé , vérif que tous les héros sont morts
        }
    }


    private static void choixAugDegats(int i, Scanner scanner)
    {

    }
    private static void choixfood(int i, Scanner scanner)
    {
        System.out.println("Veuillez choisir la nourriture que votre héro va consommer : ");
        System.out.println(Arrays.stream(Food.FoodType.values()).map(Enum::name).collect(Collectors.joining(",")));
        String choixFood = scanner.next().toUpperCase();
        Food.FoodType foodType = Food.FoodType.valueOfOrNull(choixFood);

        while(foodType == null)
        {
            System.out.println("Veuillez choisir la nourriture que votre héro va consommer : ");
            System.out.println(Arrays.stream(Food.FoodType.values()).map(Enum::name).collect(Collectors.joining(",")));
            choixFood = scanner.next().toUpperCase();
            foodType = Food.FoodType.valueOfOrNull(choixFood);
        }
        //Création d'une nourriture
        Food food = Food.generateFood(foodType);

        // ajout de la nourriture créé au héro choisit
        listeHeros.get(i).addObjects(food);
    }

    private static void initGame()
    {
        System.out.println("Veuillez choisir le nombre de héros ");
        Scanner scanner = new Scanner(System.in);
        int nombre = scanner.nextInt();
        nombre_e = nombre;

        System.out.println("Le nombre d'heros est : " + nombre);

        System.out.println("Le nombre d'ennemis est : " + nombre);

        System.out.println("\n");

        int i;
        int classe = 0;

        int compteurHunter = 0;
        int compteurWarrior = 0;
        int compteurMage = 0;
        int compteurHealer = 0;

        for (i = 0; i < nombre; i++)
        {

            boolean valeurOk = false;

            while (!valeurOk)
            {
                System.out.println("Veuillez choisir la classe de votre héro : \n" + "1. Hunter \n" + "2. Warrior \n" + "3. Mage \n" + "4. Healer \n");

                if (scanner.hasNextInt())
                {
                    classe = scanner.nextInt();
                    valeurOk = classe >= 1 && classe <= 5;
                }

                if (valeurOk)
                {

                    Hero hero = null;

                    if (classe == 1) {
                        hero = new Hunter("Hunter" + compteurHunter, 100);
                        compteurHunter++;
                    } else if (classe == 2) {
                        hero = new Warrior("Warrior" + compteurWarrior, 100);
                        compteurWarrior++;
                    } else if (classe == 3) {
                        hero = new Mage("Mage" + compteurMage, 100);
                        compteurMage++;
                    } else {
                        hero = new Healer("Healer" + compteurHealer, 100);
                        compteurHealer++;
                    }
                    listeHeros.add(hero);
                }
            }
        }
    }

    private static void initEnnemies(int niveau)
    {
        listeEnemies.clear();

        if(niveau == NIVEAU_MAX)
        {
            listeEnemies.add(new Monster("Boss", 300, 50));
        }
        else {


        for (int i = 0; i < nombre_e; i++)
        {
            if(niveau == 0)
            {
                listeEnemies.add(new Monster("Archere", 100, 5));
            }
            else if (niveau == 1)
            {
                listeEnemies.add(new Monster("Dragon", 125, 10));
            }
            else if(niveau == 2)
            {
                listeEnemies.add(new Monster("Geant", 150, 15));
            }
            else if(niveau == 3)
            {
                listeEnemies.add(new Monster("Loki", 175, 20));
            }

        }
    }
}}
