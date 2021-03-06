package botApplication.discApplication.librarys;

import core.Engine;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class FightHandler {

    private JSONObject m1Json;
    private JSONObject m2Json;

    private JSONObject m1JsonRoot;
    private JSONObject m2JsonRoot;

    private String user1;
    private String user2;

    private String m1;
    private String m2;

    private boolean m1Ai;
    private boolean m2Ai;

    //false = 2 true = 1
    private boolean round = false;

    private final Engine engine;

    private String sAttack;

    private String slot;

    private long lastDmg;

    public boolean fightDone;


    private boolean sameUser = false;


    public FightHandler(String user1, String user2, String m1, String m2, Engine engine) {
        if (user1 != null) {
            this.user1 = user1;
            this.m1 = m1;
        } else {
            this.m1Ai = true;
        }

        if (user2 != null) {
            this.user2 = user2;
            this.m2 = m2;
        } else {
            this.m2Ai = true;
        }

        this.engine = engine;

        if (m1Ai) {
            JSONObject res = engine.getDiscEngine().getApiManager().createAiFight(user2);
            m1Json = (JSONObject) res.get("data");
        }
        if (m2Ai) {
            JSONObject res = engine.getDiscEngine().getApiManager().createAiFight(user1);
            m2Json = (JSONObject) res.get("data");
        }

        if (!m1Ai) {
            JSONObject res = engine.getDiscEngine().getApiManager().getUserMonstersById(user1);
            JSONArray mnsters = (JSONArray) res.get("data");
            for (Object o : mnsters) {
                JSONObject mnster = (JSONObject) o;
                if (mnster.get("_id").equals(m1)) {
                    m1Json = mnster;
                    break;
                }
            }
        }

        if (!m2Ai) {
            JSONObject res = engine.getDiscEngine().getApiManager().getUserMonstersById(user2);
            JSONArray mnsters = (JSONArray) res.get("data");
            for (Object o : mnsters) {
                JSONObject mnster = (JSONObject) o;
                if (mnster.get("_id").equals(m2)) {
                    m2Json = mnster;
                    break;
                }
            }
        }

        JSONObject res = engine.getDiscEngine().getApiManager().getMonsters();
        JSONArray mnsters = (JSONArray) res.get("data");
        for (Object o : mnsters) {
            JSONObject mnster = (JSONObject) o;
            if (mnster.get("_id").equals(m1Json.get("rootMonster"))) {
                m1JsonRoot = mnster;
            }

            if (mnster.get("_id").equals(m2Json.get("rootMonster"))) {
                m2JsonRoot = mnster;
            }
        }
    }

    public String currentPlayer() {
        if (round)
            return user1;
        else
            return user2;
    }

    public String nextPlayer() {
        if (sameUser)
            return currentPlayer();

        if (!round)
            return user1;
        else
            return user2;
    }

    public String round(String w) {
        if (!sameUser)
            round = !round;

        if (sameUser)
            sameUser = false;

        if (round) {
            if (testAi(m1Ai, user2, m2)) return fightInfo();
        } else {
            if (testAi(m2Ai, user1, m1)) return fightInfo();
        }

        try {
            switch (w.toLowerCase()) {
                case "a1":
                case "a4":
                case "a3":
                case "a2":
                    slot = w;
                    break;

                case "info": {
                    String m1 = m1JsonRoot.get("name") + " with " + getNumber(m1Json, "hp") + " hp";
                    String m2 = m2JsonRoot.get("name") + " with " + getNumber(m2Json, "hp") + " hp";
                    sameUser = true;
                    return m1 + " against " + m2;
                }

                case "help":
                    String m1 = m1JsonRoot.get("name") + " with " + getNumber(m1Json, "hp") + " hp";
                    String m2 = m2JsonRoot.get("name") + " with " + getNumber(m2Json, "hp") + " hp";
                    sameUser = true;
                    return "You are in a fight!\n\n" + m1 + " against " + m2 + "\n\nType info to see the info again and a1, a2, a3, a4 to use one of your attackslots";

                default:
                    sameUser = true;
                    return "invalid";
            }

            JSONObject res;
            if (round) {
                res = engine.getDiscEngine().getApiManager().fight(user1, false, m2Ai, m1, m2, null, slot);
            } else {
                res = engine.getDiscEngine().getApiManager().fight(user2, false, m1Ai, m2, m1, null, slot);
            }
            if (((Long) res.get("status")) == 200) {
                m1Json = (JSONObject) res.get("monster1");
                m2Json = (JSONObject) res.get("monster2");
                lastDmg = getNumber(res, "dmg");
                return fightInfo();
            } else {
                sameUser = true;
                return (String) res.get("message");
            }

        } catch (Exception ignored) {

        }
        return fightInfo();
    }

    private boolean testAi(boolean m1Ai, String user2, String m22) {
        if (m1Ai) {
            JSONObject res = engine.getDiscEngine().getApiManager().fight(user2, true, false, null, m22, null, null);
            m2Json = (JSONObject) res.get("monster1");
            m1Json = (JSONObject) res.get("monster2");
            lastDmg = getNumber(res, "dmg");
            return true;
        }
        return false;
    }

    private String fightInfo() {
        fightDone = calcFightDone();

        if (round) {
            return m1JsonRoot.get("name") + " with " + getNumber(m1Json, "hp") + " hp attacked " + m2JsonRoot.get("name") + " and made " + lastDmg + " damage. " + m2JsonRoot.get("name") + " has " + getNumber(m2Json, "hp") + " hp left";
        } else {
            return m2JsonRoot.get("name") + " with " + getNumber(m2Json, "hp") + " hp attacked " + m1JsonRoot.get("name") + " and made " + lastDmg + " damage. " + m1JsonRoot.get("name") + " has " + getNumber(m1Json, "hp") + " hp left";
        }
    }

    private boolean calcFightDone() {
        return getNumber(m1Json, "hp") <= 0 || getNumber(m2Json, "hp") <= 0;
    }

    private int getNumber(JSONObject o, String r) {
        try {
            return Math.toIntExact((Long) o.get(r));
        } catch (Exception e) {
            return Math.toIntExact(Math.round((Double) o.get(r)));
        }
    }

    public String getWinner() {
        if (fightDone) {
            if (getNumber(m1Json, "hp") <= 0)
                return user2;
            if (getNumber(m2Json, "hp") <= 0)
                return user1;
        }
        return null;
    }

    public String getAgainst(){
        return m1JsonRoot.get("name") + " against " + m2JsonRoot.get("name");
    }

    public String getRoundImage(){
        if (round) {
            return (String) m1JsonRoot.get("imageUrl");
        } else {
            return (String) m2JsonRoot.get("imageUrl");
        }
    }
}