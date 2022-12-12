package com.NerdBot.JDA.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

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
            embed.setTitle("Test!");
            //embed.setColor(event.getUser())
            //embed.setTitle("**Roles for "+ Objects.requireNonNull(event.getGuild()).getName()+"**");
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
