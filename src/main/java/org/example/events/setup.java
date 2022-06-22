package org.example.events;

import net.dv8tion.jda.api.EmbedBuilder;
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
                    String message = String.format(Database.confiGet(e.getGuild().getId()).get("actionMessage").toString().replace("user", "%s"), e.getMember().getAsMention());
                    e.getGuild().getTextChannelById((String) Database.confiGet(e.getGuild().getId()).get("actionChannel")).sendMessage(message).queue();
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
                                message = String.format(Database.confiGet(e.getGuild().getId()).get("actionMessage").toString().replace("user", "%s"),
                                        e.getGuild().retrieveMemberById(subscriberId).complete().getAsMention());
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
        }
    }

