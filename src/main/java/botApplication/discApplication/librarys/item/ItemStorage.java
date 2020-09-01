package botApplication.discApplication.librarys.item;

import botApplication.discApplication.librarys.item.collectables.gems.*;
import botApplication.discApplication.librarys.item.collectables.metal.*;
import botApplication.discApplication.librarys.item.collectables.stuff.*;
import botApplication.discApplication.librarys.item.consumable.food.Food;
import botApplication.discApplication.librarys.item.consumable.food.Fruit;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ItemStorage {

    public static Stuff getRandomStuff() {
        ArrayList<Stuff> stuffs = new ArrayList<>();

        stuffs.add(new Cable());
        stuffs.add(new Cobweb());
        stuffs.add(new Feather());
        stuffs.add(new Glass());
        stuffs.add(new MetalScrew());
        stuffs.add(new Stick());

        return stuffs.get(ThreadLocalRandom.current().nextInt(0, stuffs.size() - 1));
    }

    public static Food getRandomFood() {
        ArrayList<Food> foods = new ArrayList<>();

        foods.add(new Fruit());
        foods.add(new Fruit());

        return foods.get(ThreadLocalRandom.current().nextInt(0, foods.size() - 1));
    }

    public static Metal getRandomMetal() {
        ArrayList<Metal> metals = new ArrayList<>();

        metals.add(new Aluminium());
        metals.add(new Copper());
        metals.add(new Gold());
        metals.add(new Iron());
        metals.add(new Platinum());
        metals.add(new Silver());

        return metals.get(ThreadLocalRandom.current().nextInt(0, metals.size() - 1));
    }

    public static Gem getRandomGem() {
        ArrayList<Gem> gems = new ArrayList<>();

        gems.add(new Diamond());
        gems.add(new Emerald());
        gems.add(new Ruby());
        gems.add(new Sapphire());

        return gems.get(ThreadLocalRandom.current().nextInt(0, gems.size() - 1));
    }
}