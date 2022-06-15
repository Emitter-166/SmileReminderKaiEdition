package org.example.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.Database;

public class wish extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        try {
            if((Integer)Database.get(e.getAuthor().getId()).get("funds") > 0){
                //wish work command here 






            } else if (e.getMessage().getContentRaw().split("")[0].equals(".") && e.getMessage().getContentRaw().split("")[1].equalsIgnoreCase("w") ) {
                EmbedBuilder subscribe = new EmbedBuilder();
                subscribe.setTitle("You can't do that!");
                subscribe.setDescription("**in order to do that, you must have smile funds on smile reminders. \n" +
                        "do** `.fund` **for more info** ");
                e.getMessage().replyEmbeds(subscribe.build()).queue();
                
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
