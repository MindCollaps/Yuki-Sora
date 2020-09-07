package botApplication.discApplication.librarys.item.collectables.metal;

import botApplication.discApplication.librarys.item.Item;
import botApplication.discApplication.librarys.item.crafting.Cookable;

public class AluminiumOre extends Metal implements Cookable {

    public AluminiumOre() {
        setDescription("A peace of stone with some shining metal lines in it");
        setImgUrl(Item.imageItemPath + "aluminium-ore.PNG");
        setItemName("Aluminium Ore");
        setItemRarity(Item.Rarity.Normal);
    }

    @Override
    public int cookTime() {
        return 5;
    }

    @Override
    public Item result() {
        return new Aluminium();
    }
}