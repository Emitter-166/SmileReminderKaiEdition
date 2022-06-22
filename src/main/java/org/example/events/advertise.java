package org.example.events;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.Database;
import java.awt.*;

public class advertise extends ListenerAdapter {

    int counter = 0;
    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        String channelId = null;
        int amountBeforeAd = 0;
        try {
            channelId = Database.confiGet(e.getGuild().getId()).get("reminderChannel").toString();
            amountBeforeAd = Integer.parseInt((String) Database.confiGet(e.getGuild().getId()).get("messageAmountBeforeAd"));
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        if(!(e.getChannel().equals(e.getGuild().getGuildChannelById(channelId)))) return;
        counter++;
        if(amountBeforeAd == counter){
            try {
                EmbedBuilder ad = new EmbedBuilder()
                        .setTitle("smile reminder!")
                        .addField("Did you know", "that you can use this bot for reminding you to use `.wish` and `.work`? \n" +
                                String.format("**do** `%s` **help for more information!**", Database.confiGet(e.getGuild().getId()).get("prefix")), false)
                        .setColor(Color.WHITE);
                e.getChannel().sendMessageEmbeds(ad.build()).queue();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            counter = 0;
        }

    }
}
