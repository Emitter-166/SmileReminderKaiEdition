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
    public void onMessageReceived(MessageReceivedEvent e){
        User author = e.getAuthor();
        if(author.isBot()) return;
        String[] args = e.getMessage().getContentRaw().split(" ");

        if(args[0].equalsIgnoreCase(".funds") && args.length == 1){
            //sending info about funds to the user
            EmbedBuilder funds = new EmbedBuilder();
            funds.setTitle(String.format("Smile funds for %s:", author.getName()));
            funds.setColor(Color.cyan);
            try {
                funds.setDescription(String.format("`total smile funds for` %s `: %s`", author.getAsMention(), Database.get(author.getId()).get("funds")));
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            e.getChannel().sendMessageEmbeds(funds.build()).queue();
            try {
                if((Integer)Database.get(author.getId()).get("funds") == 0){
                    EmbedBuilder addFunds = new EmbedBuilder();
                    addFunds.setTitle("How to add smile funds?");
                    addFunds.setColor(Color.white);
                    addFunds.setDescription("you can ");
                }
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

        }else if((args[0].equalsIgnoreCase(".funds") && args.length == 2)){
            //sending info about funds to the user about another user

            EmbedBuilder funds = new EmbedBuilder();
            Member member =  e.getGuild().retrieveMemberById(args[1]).complete();
            funds.setTitle(String.format("Funds for %s:", member.getEffectiveName()));
            funds.setColor(Color.cyan);
            try {
                funds.setDescription(String.format("`total fund for` %s `: %s`", member.getAsMention(), Database.get(args[1]).get("funds")));
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            e.getChannel().sendMessageEmbeds(funds.build()).queue();
        }


        if(!(author.getId().equalsIgnoreCase("671016674668838952"))) return;
        if(args[0].equalsIgnoreCase(".addFund") && args.length == 3){
            //adding funds to a user
            Database.PositiveupdateDB(args[1], "funds", Integer.parseInt(args[2]));
            EmbedBuilder fundsAdded = new EmbedBuilder();
            fundsAdded.setTitle("Funds added!");
            fundsAdded.setColor(Color.WHITE);
            try {
                fundsAdded.setDescription(String.format("**Funds added for <@%s>!** \n" +
                        "`total fund for` <@%s> `: %s`", args[1], args[1], Database.get(args[1]).get("funds") ));
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            e.getChannel().sendMessageEmbeds(fundsAdded.build()).queue();
            Database.updateUsers(e.getGuild().getId(), "users", args[1]);

        }

        Calendar calendar = new GregorianCalendar();
        //charging members certain amounts of smiles every week
        if(calendar.get(Calendar.DAY_OF_WEEK) == 4){

            try {
                if(!weeklyFundtaken.get()){
                    String[] arr = (Database.confiGet(e.getGuild().getId()).get("users").toString().split(" "));
                    Arrays.stream(arr).forEach(user -> {
                        Database.Negative(user, "funds", 200);
                        EmbedBuilder fundTaken = new EmbedBuilder();
                        fundTaken.setTitle("Weekly fund notice!");
                        fundTaken.setColor(Color.yellow);
                        fundTaken.setDescription("**Weekly fund taken. Amount:** `200` \n" +
                                "**To check your current balance, please do:** `.funds` ");
                        fundTaken.setFooter(String.format("%s/%s/%s", calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR)));
                        e.getGuild().retrieveMemberById(user).complete().getUser().openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessageEmbeds(fundTaken.build())).queue();
                        weeklyFundtaken.set(true);
                    });

                }
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }else if(calendar.get(Calendar.DAY_OF_WEEK) == 1){
            weeklyFundtaken.set(false);
        }
    }
}
