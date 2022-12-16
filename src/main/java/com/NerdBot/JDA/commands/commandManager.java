package com.NerdBot.JDA.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class commandManager extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        if(command.equalsIgnoreCase("ping")) {
            event.reply("PONG").queue();
        } else if(command.equalsIgnoreCase("roles")) {
            event.deferReply().queue();
            EmbedBuilder embed = new EmbedBuilder();

            try {
                if(event.getMember().getColor() == null) {
                    embed.setColor(Color.decode("4284ff"));
                } else {
                    embed.setColor(event.getMember().getColor());
                }
            } catch(NullPointerException e) {
                embed.setColor(Color.decode("4284ff"));
            }

            embed.setTitle("**Roles for "+ Objects.requireNonNull(event.getGuild()).getName()+"**");
            User m = event.getUser();
            embed.setFooter("Message requested by " + m.getName() + "#" + m.getDiscriminator());

            if(event.getGuild().getRoles().size() == 1) {
                embed.setDescription("No roles found in **"+ Objects.requireNonNull(event.getGuild()).getName()+"**");
            } else {
                StringBuilder sb = new StringBuilder();
                int i = 0;
                for(Role r : event.getGuild().getRoles()) {
                    if(r.getName().equals("@everyone")) {
                        break;
                    } else {
                        sb.append(r.getAsMention());
                    } if(i < (event.getGuild().getRoles().size()-2)) {
                        sb.append(", ");
                    } i++;
                }

                embed.setDescription(sb);
            }

            event.getHook().sendMessageEmbeds(embed.build()).queue();
        } else if(command.equalsIgnoreCase("owner")) {
            event.deferReply().queue();
            EmbedBuilder embed = new EmbedBuilder();
            User m = event.getUser();
            embed.setTitle("**Owner for " + Objects.requireNonNull(event.getGuild()).getName() + "**:");
            embed.setDescription(Objects.requireNonNull(event.getGuild().getOwner()).getAsMention());
            embed.setFooter("Message requested by "+ m.getName()+"#"+m.getDiscriminator());
            event.getHook().sendMessageEmbeds(embed.build()).queue();
        }

    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commands = new ArrayList<>();
        commands.add(Commands.slash("ping", "Test command"));
        event.getGuild().updateCommands().addCommands(commands).queue();
        commands.add(Commands.slash("roles", "Get roles in current server"));
        event.getGuild().updateCommands().addCommands(commands).queue();
        commands.add(Commands.slash("owner", "Get the owner of the current server"));
        event.getGuild().updateCommands().addCommands(commands).queue();
    }
/*
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        List<CommandData> commands = new ArrayList<>();
        commands.add(Commands.slash("ping", "Test command"));
        event.getGuild().updateCommands().addCommands(commands).queue();
    }*/

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<CommandData> commands = new ArrayList<>();
        commands.add(Commands.slash("ping", "Test command"));
        event.getJDA().updateCommands().addCommands(commands).queue();
    }
}
