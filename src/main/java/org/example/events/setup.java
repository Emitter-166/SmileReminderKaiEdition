package org.example.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.Database;

import java.awt.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicBoolean;

public class setup extends ListenerAdapter {
    AtomicBoolean weeklyFundtaken = new AtomicBoolean(false);

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        User author = e.getAuthor();
        if (author.isBot()) return;
        String[] args = e.getMessage().getContentRaw().split(" ");


            if (args[0].equalsIgnoreCase(".subscribe")) {
                //subscribing
                EmbedBuilder subscribed = new EmbedBuilder()
                        .setTitle("Subscribed!")
                        .setDescription("**you will be charged from Saturday + one time subscription fee**")
                        .setColor(Color.WHITE);
                e.getChannel().sendMessageEmbeds(subscribed.build()).queue();
                Database.updateUsers(e.getGuild().getId(), "users", e.getAuthor().getId(), true, false);
                try {
                    String message = String.format(String.format(Database.confiGet(e.getGuild().getId()).get("actionMessage").toString().replace("user", "%s"), e.getMember().getAsMention())
                            .replace("charge", "%s"), Database.confiGet(e.getGuild().getId()).get("toCut"));
                    try{
                        e.getGuild().getTextChannelById((String) Database.confiGet(e.getGuild().getId()).get("actionChannel")).sendMessage(message).queue();
                    }catch (Exception exception){}
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

            } else if (args[0].equalsIgnoreCase(".unsubscribe")) {
                //unsubscribing
                EmbedBuilder subscribed = new EmbedBuilder()
                        .setTitle("Unsubscribed!")
                        .setColor(Color.BLACK);
                e.getChannel().sendMessageEmbeds(subscribed.build()).queue();
                Database.updateUsers(e.getGuild().getId(), "users", e.getAuthor().getId(), false, true);

            }

        Calendar calendar = new GregorianCalendar();
            //charging members certain amounts of smiles every week
            if (calendar.get(Calendar.DAY_OF_WEEK) == 7) {

                try {
                    if (!weeklyFundtaken.get()) {

                           String subscribers[] = Database.confiGet(e.getGuild().getId()).get("users").toString().split(" ");
                        Arrays.stream(subscribers).forEach(subscriberId ->{
                            String message = null;
                            try {
                                message = String.format(String.format(Database.confiGet(e.getGuild().getId()).get("actionMessage").toString().replace("user", "%s"), e.getMember().getAsMention())
                                        .replace("charge", "%s"), Database.confiGet(e.getGuild().getId()).get("toCut"));
                                        e.getGuild().getTextChannelById((String) Database.confiGet(e.getGuild().getId()).get("actionChannel")).sendMessage(message).queue();
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
                        weeklyFundtaken.set(true);
                    }

                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            } else if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
                weeklyFundtaken.set(false);
            }

            //admin setup part
            if(e.getMember().hasPermission(Permission.ADMINISTRATOR)){
                switch (args[0]){
                    case ".help":
                        EmbedBuilder setupEmbed = new EmbedBuilder()
                                .setTitle("Setup commands:")
                                .addField("", "ㅤ\n" +
                                        "`.prefix prefix` **prefix for the bot, default is** `.r` \n" +
                                        "ㅤ\n" +
                                        "`.subAmount amount` **weekly amount (smiles) to charge subscribers** \n" +
                                        "ㅤ\n" +
                                        "`.reminderChannel` **the channel this cmd is used will be reminder channel** \n" +
                                        "ㅤ\n" +
                                        "`.actionMessage message` **command to use when someone subscribes, please see description for usage guide** \n" +
                                        "ㅤ\n" +
                                        "`.actionChannel` **the channel this command is used will be set as channel to send the command** \n" +
                                        "ㅤ\n" +
                                        "ㅤ\n", false)
                                .addField("Command description: ", "ㅤ\n" +
                                        "`.actionMessage` this message will be the bot command you want to use in order to remove certain amounts of smiles (amount will be defined by \n" +
                                        "`.subAmount` command, check setup commands) from the user. there are some special keywords: \n" +
                                        "`user` which will mention the user in the command message \n" +
                                        "`charge` which will put charge amount there \n" +
                                        "ㅤ\n" +
                                        "**Example:** \n" +
                                        "ㅤ\n" +
                                        "if actionMessage is `.remove charge user`, and Emitter subscribes, this message will be sent to actionChannel: `.remove` <@671016674668838952> `200` \n" +
                                        "this is just an example, you must set everything up before expecting it to work like this", false)
                                .setColor(Color.GRAY);
                        e.getMessage().replyEmbeds(setupEmbed.build())
                                .mentionRepliedUser(false)
                                .queue();
                                break;


                    case ".prefix":
                        e.getMessage().reply("`prefix set!`")
                                .mentionRepliedUser(false)
                                .queue();
                        e.getMessage().delete().queue();
                        Database.updateConfig(e.getGuild().getId(), "prefix", args[1]);
                        break;

                    case ".subAmount":
                        e.getMessage().reply("`subscription charge set!`")
                                .mentionRepliedUser(false)
                                .queue();
                        e.getMessage().delete().queue();
                        Database.updateConfig(e.getGuild().getId(), "toCut", args[1]);
                        break;

                    case ".reminderChannel":
                        e.getMessage().reply("`reminder channel set!`")
                                .mentionRepliedUser(false)
                                .queue();
                        e.getMessage().delete().queue();
                        Database.updateConfig(e.getGuild().getId(), "reminderChannel", e.getChannel().getId());
                        break;

                    case ".actionMessage":
                        e.getMessage().reply("`action message/command set!`")
                                .mentionRepliedUser(false)
                                .queue();
                        e.getMessage().delete().queue();
                        StringBuilder actionMessage = new StringBuilder();
                        for(int i = 1; i < args.length; i++){
                            actionMessage.append(args[i] + " ");
                        }
                        Database.updateConfig(e.getGuild().getId(), "actionMessage", actionMessage.toString());
                        break;

                    case ".actionChannel":
                        e.getMessage().reply("`Action channel set!`")
                                .mentionRepliedUser(false)
                                .queue();
                        e.getMessage().delete().queue();
                        Database.updateConfig(e.getGuild().getId(), "actionChannel", e.getChannel().getId());
                        break;
                }
            }
        }
    }

