package org.example.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.Database;
import org.example.Main;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class wish extends ListenerAdapter {
    //here is reminder and user help cmd

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        String prefix;
        try {
            prefix = Database.confiGet(e.getGuild().getId()).get("prefix").toString();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        if(e.getChannel().getType().equals(ChannelType.PRIVATE)) return;
        String channelId = null;
        try {
            channelId = Database.confiGet(e.getGuild().getId()).get("reminderChannel").toString();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        if(!(e.getChannel().equals(e.getGuild().getGuildChannelById(channelId)))) return;
        try {

            String message = e.getMessage().getContentRaw();
            String wish = String.format("%s wish", prefix);
            String work = String.format("%s work", prefix);

            if ((Boolean) Database.get(e.getAuthor().getId()).get("isSubscribed")) {
                //wish work command here
                if (message.equals(wish)) {
                    EmbedBuilder wishBuilder = new EmbedBuilder();
                    wishBuilder.setTitle("Reminder set!");
                    wishBuilder.setDescription("**wish reminder set!** \n" +
                            "**Don't forget to do** `.wish` **in** <#969147973210607626> \n" +
                            "**we will remind you soon!**");
                    e.getMessage().replyEmbeds(wishBuilder.build())
                            .mentionRepliedUser(false)
                            .queue();

                    Timer wishTimer = new Timer();
                    wishTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            EmbedBuilder wishReminder = new EmbedBuilder();
                            wishReminder.setTitle("Time for .wish!");
                            wishReminder.setColor(Color.WHITE);
                            wishReminder.setDescription("**goto** <#969147973210607626> **and do** `.wish` \n" +
                                    "**Don't forget to set timer back!**");
                            e.getMessage().replyEmbeds(wishReminder.build())
                                    .mentionRepliedUser(true)
                                    .queue();
                        }
                    }, 3600000);

                } else if (message.equals(work)) {
                    EmbedBuilder workBuilder = new EmbedBuilder();
                    workBuilder.setTitle("Reminder set!");
                    workBuilder.setDescription("**work reminder set!** \n" +
                            "**Don't forget to do** `.work` **in** <#969147973210607626> \n" +
                            "**we will remind you soon!**");
                    e.getMessage().replyEmbeds(workBuilder.build())
                            .mentionRepliedUser(false)
                            .queue();

                    Timer workTimer = new Timer();
                    workTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            EmbedBuilder workReminder = new EmbedBuilder();
                            workReminder.setTitle("Time for .work!");
                            workReminder.setColor(Color.WHITE);
                            workReminder.setDescription("**goto** <#969147973210607626> **and do** `.work` \n" +
                                    "**Don't forget to set timer back!**");
                            e.getMessage().replyEmbeds(workReminder.build())
                                    .mentionRepliedUser(true)
                                    .queue();
                        }
                    }, 3600000);
                }
            } else if (message.equals(wish) || message.equals(work)) {
                EmbedBuilder subscribe = new EmbedBuilder();
                subscribe.setTitle("You can't do that!");
                subscribe.setDescription("**in order to do that, you must subscribe to this service using** `.subscribe`. \n " +
                        String.format("**with a weekly charge of %s smiles, you can unsubscribe anytime by** `.unsubscribe` \n", Database.confiGet(e.getGuild().getId()).get("toCut")) +
                        "**do** `.r help` **for more info** ");
                e.getMessage().replyEmbeds(subscribe.build()).queue();

            }

            if (message.equalsIgnoreCase(String.format("%s help", prefix))) {
                EmbedBuilder helpBuilder = new EmbedBuilder();
                helpBuilder.setTitle("Help");
                helpBuilder.addField("Commands: ", "" +
                        String.format("`%s` **set reminder for using** `.wish` \n", wish) +
                        String.format("`%s` **set reminder for using** `.work` \n", work) +
                        String.format("`.subscribe` **subscribe to use this bot with a charge of %s smiles per week** `.funds` \n", Database.confiGet(e.getGuild().getId()).get("toCut")) +
                        "`.unsubscribe` **unsubscribe from this service**\n" +
                        "ㅤㅤㅤㅤㅤㅤㅤㅤ", false);
                e.getMessage().replyEmbeds(helpBuilder.build())
                        .mentionRepliedUser(false)
                        .queue();
            }

        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
