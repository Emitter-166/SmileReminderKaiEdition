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

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if(e.getChannel().getType().equals(ChannelType.PRIVATE)) return; 
        try {
            if((Integer)Database.get(e.getAuthor().getId()).get("funds") > 0){
                //wish work command here
                String message = e.getMessage().getContentRaw();

                switch (message) {
                    case ".wish":
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
                                        .mentionRepliedUser(false)
                                        .queue();
                                Main.builder.openPrivateChannelById(e.getAuthor().getId()).flatMap(privateChannel -> privateChannel.sendMessageEmbeds(wishReminder.build())).queue();
                            }
                        }, 3600000);
                        break;

                    case ".work":
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
                                        .mentionRepliedUser(false)
                                        .queue();
                                Main.builder.openPrivateChannelById(e.getAuthor().getId()).flatMap(privateChannel -> privateChannel.sendMessageEmbeds(workReminder.build())).queue();
                            }
                        }, 3600000);
                        break;
                }

            } else if (e.getMessage().getContentRaw().split("")[0].equals(".") && e.getMessage().getContentRaw().split("")[1].equalsIgnoreCase("w") ) {
                EmbedBuilder subscribe = new EmbedBuilder();
                subscribe.setTitle("You can't do that!");
                subscribe.setDescription("**in order to do that, you must have smile funds on smile reminders. \n" +
                        "do** `.funds` **for more info** ");
                e.getMessage().replyEmbeds(subscribe.build()).queue();
                
            }

            if(e.getMessage().getContentRaw().equalsIgnoreCase(".help")){
                EmbedBuilder helpBuilder = new EmbedBuilder();
                helpBuilder.setTitle("Help");
                helpBuilder.addField("Commands: ", "" +
                        "`.wish` **set reminder for using** `.wish` \n" +
                        "`.work` **set reminder for using** `.work` \n" +
                        "`.funds` **see your current smile funds using** `.funds` \n" +
                        "`funds userId` **and other members smile funds using** `.funds userId` \n" +
                        "**Example:** `.funds 671016674668838952`" +
                        "ㅤㅤㅤㅤㅤㅤㅤㅤ", false);

                helpBuilder.addField("How to use?", " ㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤ\n" +
                        "**when you use** `.wish/.work` **command on <#969147973210607626> channel \n" +
                        "do it here too, it will remind you on the exact time when \n" +
                        "the cool down for those commands ends (one hour)** \n" +
                        "ㅤㅤㅤㅤㅤㅤㅤㅤ\n", false);

                helpBuilder.addField("Smile funds",
                        "ㅤㅤㅤㅤㅤㅤㅤㅤ\n" +
                                "**What is smile funds?** \n" +
                                "```In order you use this bot, you have to pay 200 smiles per week (every Saturday)." +
                                "you have to add funds to your account using smiles to pay that fee``` \n" +
                                "ㅤㅤㅤㅤㅤㅤㅤㅤ\n" +
                                "**How to add funds?** \n" +
                                "ㅤㅤㅤㅤㅤㅤㅤㅤ\n" +
                                "**step one: give <@671016674668838952> the amount of smiles you wanna add to your fund** \n" +
                                "`note: you will be charges` **200 smiles** `per week and it will be cut from your available fund, " +
                                "if you run out of/don't have funds, you cannot use this bot.` \n" +
                                "ㅤㅤㅤㅤㅤㅤㅤㅤ\n" +
                                "**step two: Direct message <@671016674668838952> that you have given smiles and you would like to add \n" +
                                "it to your fund** \n" +
                                "`note: it will take maximum 24 hours to add smiles to your fund` \n" +
                                "ㅤㅤㅤㅤㅤㅤㅤㅤ\n", false);
                helpBuilder.addField("About smile reminder", "" +
                        "ㅤㅤㅤㅤㅤㅤㅤㅤ\n" +
                        "```Smile reminder bot(v2) is a open source yet premium bot made to remind users on .wish/.work command cooldown \n" +
                        "It's not backed or supervised by official kai's haven. half of the funding goes to bot hosting and half to the developer``` ", false);

                helpBuilder.setDescription("**Thank you for choosing Smile reminder bot, it's appreciated :heart:**");

                e.getMessage().replyEmbeds(helpBuilder.build())
                        .mentionRepliedUser(false)
                        .queue();
            }

        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
