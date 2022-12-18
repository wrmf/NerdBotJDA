package com.NerdBot.JDA.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class dinfo extends ListenerAdapter {

    private final String ownerID = "555207100603826177";

    private EmbedBuilder setEmbedPresets(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();

        try {
            if(Objects.requireNonNull(event.getMember()).getColor() == null) {
                embed.setColor(Color.decode("4284ff"));
            } else {
                embed.setColor(event.getMember().getColor());
            }
        } catch(NullPointerException e) {
            embed.setColor(Color.decode("4284ff"));
        }

        User m = event.getUser();
        embed.setFooter("Message requested by " + m.getName() + "#" + m.getDiscriminator());

        return embed;

    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        if(command.equalsIgnoreCase("ping")) {
            event.deferReply().queue();
            EmbedBuilder embed = setEmbedPresets(event);
            embed.setDescription("PONG");
            event.getHook().sendMessageEmbeds(embed.build()).queue();
        } else if(command.equalsIgnoreCase("roles")) {
            event.deferReply().queue();
            EmbedBuilder embed = setEmbedPresets(event);

            embed.setTitle("**Roles for "+ Objects.requireNonNull(event.getGuild()).getName()+"**");

            if(event.getGuild().getRoles().size() <= 1) {
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
            EmbedBuilder embed = setEmbedPresets(event);
            User m = event.getUser();
            embed.setTitle("**Owner for " + Objects.requireNonNull(event.getGuild()).getName() + "**:");
            try {
                embed.setDescription(Objects.requireNonNull(event.getGuild().getOwner()).getAsMention());
            } catch(NullPointerException e) {
                embed.setDescription("none found");
            }
            event.getHook().sendMessageEmbeds(embed.build()).queue();
        } else if(command.equalsIgnoreCase("getguilds")) {
            event.deferReply().queue();
            if(String.valueOf(Objects.requireNonNull(event.getMember()).getIdLong()).equals(ownerID)) {
                EmbedBuilder embed = setEmbedPresets(event);

                List<String> list = new ArrayList<>();
                for (Guild guild : event.getJDA().getGuilds())
                    embed.addField("", guild.getName(), false);
                embed.setTitle("Guilds: "+event.getJDA().getGuilds().size());
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            } else {
                EmbedBuilder embed = setEmbedPresets(event);
                embed.setColor(Color.red);
                embed.setTitle("ERROR");
                embed.setDescription("You are not authorized to perform this command");
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            }
        } else if(command.equalsIgnoreCase("pfp")) {
            event.deferReply().queue();
            User m;
            try {
                OptionMapping messageOption = event.getOption("user");
                if(messageOption == null) {
                    m = event.getUser();
                } else {
                    m = messageOption.getAsUser();
                }
            } catch(NullPointerException e) {
                m = event.getUser();
            }
            EmbedBuilder embed = setEmbedPresets(event);
            embed.setThumbnail(m.getEffectiveAvatarUrl());
            embed.setTitle("Avatar for "+m.getName());
            event.getHook().sendMessageEmbeds(embed.build()).queue();
        } else if(command.equalsIgnoreCase("user")) {
            event.deferReply().queue();
            User u;
            Member m;
            try {
                OptionMapping messageOption = event.getOption("user");
                if(messageOption == null) {
                    u = event.getUser();
                    m = event.getMember();
                } else {
                    u = messageOption.getAsUser();
                    m = messageOption.getAsMember();
                }
            } catch(NullPointerException e) {
                u = event.getUser();
                m = event.getMember();
            }
            EmbedBuilder embed = setEmbedPresets(event);
            embed.setThumbnail(u.getEffectiveAvatarUrl());
            embed.addField("Name: ", u.getName(), true);
            assert m != null;
                embed.addField("Nickname: ", m.getEffectiveName(), true);
            embed.addField("Account created: ", String.valueOf(m.getTimeCreated().toLocalDate()), true);
            embed.addField("Joined "+ Objects.requireNonNull(event.getGuild()).getName()+":", String.valueOf(m.getTimeJoined().toLocalDate()), true);

            StringBuilder sb = new StringBuilder();
            if(m.getRoles().size() <= 1) {
                sb.append("none found");
            } else {
                int i = 0;
                for(Role r : m.getRoles()) {
                    if(r.getName().equals("@everyone")) {
                        break;
                    } else {
                        sb.append(r.getAsMention());
                    } if(i < (event.getGuild().getRoles().size()-2)) {
                        sb.append(", ");
                    } i++;
                }
            }

            embed.addField("Roles: ", sb.toString(), true);
            event.getHook().sendMessageEmbeds(embed.build()).queue();
        } else if(command.equalsIgnoreCase("role")) {
            event.deferReply().queue();
            Role r;
            try {
                OptionMapping messageOption = event.getOption("role");
                if(messageOption == null) {
                    EmbedBuilder embed = setEmbedPresets(event);
                    embed.setColor(Color.red);
                    embed.setTitle("ERROR");
                    embed.setDescription("NO ROLE **"+ Objects.requireNonNull(event.getOption("role")).getAsString()+"** FOUND");
                    event.getHook().sendMessageEmbeds(embed.build()).queue();
                } else {
                    r = messageOption.getAsRole();
                    EmbedBuilder embed = setEmbedPresets(event);
                    embed.setColor(r.getColor());
                    embed.addField("Name: ", r.getName(), false);
                    embed.addField("ID: ", r.getId(), false);
                    embed.addField("Colour: ", String.valueOf(r.getColorRaw()), false);
                    embed.addField("Permissions info: ", String.valueOf(r.getPermissionsRaw()), false);
                    embed.addField("Created at: ", String.valueOf(r.getTimeCreated().toLocalDate()), false);
                    event.getHook().sendMessageEmbeds(embed.build()).queue();

                }
            } catch(NullPointerException e) {
                EmbedBuilder embed = setEmbedPresets(event);
                embed.setColor(Color.red);
                embed.setTitle("ERROR");
                embed.setDescription("NO ROLE **"+ Objects.requireNonNull(event.getOption("role")).getAsString()+"** FOUND");
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            }
        } else if(command.equalsIgnoreCase("server")) {
            event.deferReply().queue();
            EmbedBuilder embed = setEmbedPresets(event);
            embed.setThumbnail(Objects.requireNonNull(event.getGuild()).getIconUrl());
            embed.addField("Server Name: ", event.getGuild().getName(), true);
            embed.addField("Server ID: ", String.valueOf(event.getGuild().getIdLong()), true);
            embed.addField("Members: ", String.valueOf(event.getGuild().getMemberCount()), true);

            Member owner;
            try {
                embed.setDescription(Objects.requireNonNull(event.getGuild().getOwner()).getAsMention());
                embed.addField("Owner: ", event.getGuild().getOwner().getAsMention(), true);
            } catch(NullPointerException e) {
                embed.addField("Owner: ", "none found", true);
            }

            embed.addField("Created on: ", String.valueOf(event.getGuild().getTimeCreated().toLocalDate()), true);
            event.getHook().sendMessageEmbeds(embed.build()).queue();

        } else if(command.equalsIgnoreCase("emojis")) {
            event.deferReply().queue();
            EmbedBuilder embed = setEmbedPresets(event);
            try {
                if(event.getGuild().getEmotes().size() <= 0) {
                    embed.setDescription("No emojis found in " + Objects.requireNonNull(event.getGuild()).getName());
                } else {
                    embed.setTitle("Emojis: "+event.getGuild().getEmotes().size());
                    if(event.getGuild().getEmotes().size() >= 25) {
                        embed.setDescription("There are more than 25 emotes in this guild. Unfortunately, sending more than 25 emotes is not supported at this time");
                    } else {
                        for(int i = 0; i < event.getGuild().getEmotes().size(); i++) {
                            embed.addField("", "<:"+event.getGuild().getEmotes().get(i).getName()+":"+event.getGuild().getEmotes().get(i).getId()+">", true);
                        }
                    }
                }
            } catch(NullPointerException e) {
                embed.setDescription("No emojis found in " + Objects.requireNonNull(event.getGuild()).getName());
            }
            event.getHook().sendMessageEmbeds(embed.build()).queue();
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commands = new ArrayList<>();
        event.getGuild().updateCommands().addCommands(commands).queue();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        List<CommandData> commands = new ArrayList<>();
        event.getGuild().updateCommands().addCommands(commands).queue();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<CommandData> commands = new ArrayList<>();
        commands.add(Commands.slash("ping", "Test command"));
        commands.add(Commands.slash("roles", "Get roles in current server"));
        commands.add(Commands.slash("owner", "Get the owner of the current server"));
        commands.add(Commands.slash("getguilds", "Get the list of guilds this bot is in"));

        OptionData user = new OptionData(OptionType.USER, "user", "The user you want to get the profile picture of; if none is provided, defaults to message author", false);
        commands.add(Commands.slash("pfp", "Get the profile picture of a user").addOptions(user));
        commands.add(Commands.slash("user", "Get server-specific information about a user").addOptions(user));

        OptionData role = new OptionData(OptionType.ROLE, "role", "the role you want to get information about", true);
        commands.add(Commands.slash("role", "Get information about a role").addOptions(role));
        commands.add(Commands.slash("server", "Get information about current server"));
        commands.add(Commands.slash("emojis", "Get emojis in current server"));
        event.getJDA().updateCommands().addCommands(commands).queue();
    }
}
