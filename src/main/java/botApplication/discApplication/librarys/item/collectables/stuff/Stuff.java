package botApplication.discApplication.librarys.item.collectables.stuff;

import botApplication.discApplication.librarys.item.Item;

public class Stuff extends Item {

    @Override
    public Item clone() {
        Stuff t = new Stuff();
        t.setImgUrl(getImgUrl());
        t.setItemName(getItemName());
        t.setItemRarity(getItemRarity());
        t.setItemState(getItemState());
        return t;
    }
}
