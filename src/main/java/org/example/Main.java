package org.example;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.example.events.advertise;
import org.example.events.setup;
import org.example.events.wish;

import javax.security.auth.login.LoginException;



public class Main  {
    public static JDA builder;
    public static void main(String[] args) throws LoginException{

        builder = JDABuilder.createLight(System.getenv("token"))
                .addEventListeners(new wish())
                .addEventListeners(new Database())
                .addEventListeners(new setup())
                .addEventListeners(new advertise())
                .setActivity(Activity.listening(".help"))
                .build();
    }
}