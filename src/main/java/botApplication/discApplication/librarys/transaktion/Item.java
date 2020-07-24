package botApplication.discApplication.librarys.transaktion;

import javax.management.MBeanRegistration;
import java.io.Serializable;

public class Item implements Serializable {

    private static final long serialVersionUID = 42L;

    private String itemName;
    private Rarity itemRarity;
    private String imgUrl;

    public enum Rarity {
        Normal, Epic, Legendary, Mystic
    }

    public static Rarity stringToRarity(String s){
        switch (s.toLowerCase()){
            case "normal":
                return Rarity.Normal;
            case "epic":
                return Rarity.Epic;
            case "legendary":
                return Rarity.Legendary;
            case "mystic":
                return Rarity.Mystic;
        }
        return null;
    }

    public static String rarityToString(Rarity r){
        switch (r){

            case Normal:
                return "normal";
            case Epic:
                return "epic";
            case Legendary:
                return "legendary";
            case Mystic:
                return "mystic";
        }
        return null;
    }

    public static int rarityToInt(Rarity r){
        switch (r){

            case Normal:
                return 0;
            case Epic:
                return 1;
            case Legendary:
                return 2;
            case Mystic:
                return 3;
        }
        return 0;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Rarity getItemRarity() {
        return itemRarity;
    }

    public void setItemRarity(Rarity itemRarity) {
        this.itemRarity = itemRarity;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
