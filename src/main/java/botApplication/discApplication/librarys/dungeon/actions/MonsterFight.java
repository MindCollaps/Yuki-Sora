package botApplication.discApplication.librarys.dungeon.actions;

import botApplication.discApplication.librarys.FightHandler;
import botApplication.discApplication.librarys.dungeon.Dungeon;
import botApplication.response.Response;
import core.Engine;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.io.Serializable;

public class MonsterFight implements DungeonAction, Serializable {

    public Engine engine;
    private final String msg = "Seems like here is a monster!";
    private FightHandler fightHandler;
    private Dungeon d;

    public MonsterFight(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void action(Dungeon dungeon) {
        d = dungeon;
        fightHandler = new FightHandler(dungeon.getUser().getUserId(), null, dungeon.getM(), null, engine);
        dungeon.getTextChannel().sendMessage(fightHandler.round("help")).queue();
        createResponse(engine, dungeon.getUser().getUserId(), dungeon.getTextChannel().getId(), dungeon.getTextChannel().getGuild().getId());
    }

    @Override
    public void generate() {

    }

    private void createResponse(Engine engine, String userId, String chanId, String guildId) {
        Response r = new Response(Response.ResponseTyp.Discord) {
            @Override
            public void onGuildMessage(GuildMessageReceivedEvent respondingEvent) {
                String re;
                re = fightHandler.round(respondingEvent.getMessage().getContentRaw());
                EmbedBuilder b = new EmbedBuilder().setColor(Color.GREEN).setDescription(re).setThumbnail(fightHandler.getRoundImage()).setTitle(fightHandler.getAgainst());
                d.getTextChannel().sendMessage(b.build()).queue();
                if (!fightHandler.fightDone) {
                    re = fightHandler.round(respondingEvent.getMessage().getContentRaw());
                    b = new EmbedBuilder().setColor(Color.YELLOW).setDescription(re).setThumbnail(fightHandler.getRoundImage()).setTitle(fightHandler.getAgainst());
                    d.getTextChannel().sendMessage(b.build()).queue();
                    if (!fightHandler.fightDone) {
                        engine.getDiscEngine().getTextUtils().sendSucces("Your turn!", d.getTextChannel());
                        createResponse(engine, userId, chanId, guildId);
                    } else {
                        if (fightHandler.getWinner() != null) {
                            engine.getDiscEngine().getApiManager().xpOnMonster(d.getM(), 20);
                            engine.getDiscEngine().getTextUtils().sendSucces("You won!", d.getTextChannel());
                        }
                        d.caveActionFinished(fightHandler.getWinner() == null);
                    }
                } else {
                    if (fightHandler.getWinner() != null) {
                        engine.getDiscEngine().getApiManager().xpOnMonster(d.getM(), 20);
                        engine.getDiscEngine().getTextUtils().sendSucces("You won!", d.getTextChannel());
                    }
                    d.caveActionFinished(fightHandler.getWinner() == null);
                }
            }

            @Override
            public void onError(Exception e) {
                d.dungeonCorruptedDeadEnd();
            }
        };
        r.discUserId = userId;
        r.discChannelId = chanId;
        r.discGuildId = guildId;
        engine.getResponseHandler().makeResponse(r);
    }
}
