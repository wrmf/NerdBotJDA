package com.NerdBot.JDA;

import com.NerdBot.JDA.commands.dinfo;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;

public class NerdBot {

    private ShardManager shardManager;

    public NerdBot() {
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(TokenFile.token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("Intellij because I'm migrating to JDA"));
        try {
            shardManager = builder.build();
        } catch(LoginException e) {
            System.out.println("ERROR: Token is invalid\n");
        }

        shardManager.addEventListener(new dinfo());

    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public static void main(String[] args) { NerdBot bot = new NerdBot(); }
}
