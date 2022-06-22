package org.example;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.example.events.setup;
import org.example.events.wish;

import javax.security.auth.login.LoginException;



public class Main  {
    public static JDA builder;
    public static void main(String[] args) throws LoginException{

        builder = JDABuilder.createLight("OTYxNjM3NDA1NTcwNDk4NjUy.Gw4_wg._ko9-uS7mHa_KbqPwCh-bNlGrl16lbkfM8nHE8")
                .addEventListeners(new wish())
                .addEventListeners(new Database())
                .addEventListeners(new setup())
                .setActivity(Activity.listening(".help"))
                .build();
    }
}