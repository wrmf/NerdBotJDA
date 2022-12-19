package com.NerdBot.JDA.commands;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
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
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.internal.utils.PermissionUtil;
import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class dinfo extends ListenerAdapter {

    private final String ownerID = "555207100603826177";
    private final String testServerID = "714224578376892427";

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

    /* ****
    *Dinfo*
     *****/

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
            if(String.valueOf(Objects.requireNonNull(event.getMember()).getIdLong()).equals(ownerID) && String.valueOf(Objects.requireNonNull(event.getGuild()).getIdLong()).equals("714224578376892427")) {
                EmbedBuilder embed = setEmbedPresets(event);

                List<String> list = new ArrayList<>();
                for (Guild guild : event.getJDA().getGuilds())
                    embed.addField("", guild.getName()+": "+guild.getOwner(), false);
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

        /* ********
         Moderation*
         **********/

        else if(command.equalsIgnoreCase("nick")) {
            event.deferReply().queue();
            if(Objects.requireNonNull(event.getMember()).hasPermission(Permission.BAN_MEMBERS) || event.getMember().isOwner() || Objects.equals(String.valueOf(event.getMember().getIdLong()), ownerID)) {
                EmbedBuilder embed = setEmbedPresets(event);

                try {
                    OptionMapping messageUser = event.getOption("user");
                    OptionMapping messageNick = event.getOption("nick");

                    Member m;
                    String s;

                    if(messageUser == null) {
                        m = event.getMember();
                    } else {
                        m = messageUser.getAsMember();
                    } if(messageNick == null) {
                        System.out.println(messageNick.getAsString());
                        embed.setColor(Color.red);
                        embed.setTitle("ERROR");
                        embed.setDescription("An unknown error occurred");
                    } else {
                        assert messageUser != null;
                        String newNick = messageNick.getAsString();

                        if(newNick.length() >= 32) {
                            embed.setColor(Color.red);
                            embed.setTitle("ERROR");
                            embed.setDescription("Nicknames must be < 32 characters");
                        } else {
                            if(PermissionUtil.canInteract(Objects.requireNonNull(event.getGuild()).getSelfMember(), m) && PermissionUtil.canInteract(event.getMember(), m)) {
                                m.modifyNickname(newNick).reason("Requested by "+event.getMember().getEffectiveName()).queue();
                                embed.setTitle("Success");
                                embed.setDescription("Changed "+m.getAsMention()+"'s nickname");
                                embed.setColor(Color.green);
                            } else {
                                embed.setColor(Color.red);
                                embed.setTitle("ERROR");
                                embed.setTitle("I do not have permission to modify that user");
                            }
                        }

                    }
                } catch(NullPointerException e) {
                    embed.setColor(Color.red);
                    embed.setTitle("ERROR");
                    embed.setDescription("An unknown error occurred");
                }

                event.getHook().sendMessageEmbeds(embed.build()).queue();
            } else {
                EmbedBuilder embed = setEmbedPresets(event);
                embed.setColor(Color.red);
                embed.setTitle("ERROR");
                embed.setDescription("You do not have permission to use this command");
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            }
        } else if(command.equalsIgnoreCase("ban")) {
            event.deferReply().queue();

            if(!Objects.requireNonNull(event.getGuild()).getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
                EmbedBuilder embed = setEmbedPresets(event);
                embed.setColor(Color.red);
                embed.setTitle("ERROR");
                embed.setDescription("**I** are not authorized to ban members :(");
                event.getHook().sendMessageEmbeds(embed.build()).queue();
                return;
            }

            if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.BAN_MEMBERS) || event.getMember().isOwner() || Objects.equals(String.valueOf(event.getMember().getIdLong()), ownerID)) {
                EmbedBuilder embed = setEmbedPresets(event);
                OptionMapping banUser = event.getOption("user");
                OptionMapping reasonString = event.getOption("reason");
                String reason;

                if(reasonString == null) {
                    reason = "Banned via ban command by "+event.getUser().getName()+"#"+event.getUser().getDiscriminator();
                } else {
                     reason = reasonString.getAsString();
                }

                if(banUser == null) {
                    embed.setColor(Color.red);
                    embed.setTitle("ERROR");
                    embed.setDescription("No user to ban was provided");
                } else if(banUser != null) {
                    User u = banUser.getAsUser();
                    Member m = banUser.getAsMember();
                    if(m != null) {
                        if (PermissionUtil.canInteract(Objects.requireNonNull(event.getGuild()).getSelfMember(), m) && PermissionUtil.canInteract(event.getMember(), m) && !String.valueOf(m.getIdLong()).equalsIgnoreCase(ownerID)) {
                            Objects.requireNonNull(event.getGuild()).ban(u, 0, reason).queue();
                            embed.setDescription(u.getAsMention() + " has been banned from " + event.getGuild().getName());
                        } else {
                            embed.setColor(Color.red);
                            embed.setTitle("ERROR");
                            embed.setDescription("I do not have permission to ban that user");
                        }
                    } else {
                        Objects.requireNonNull(event.getGuild()).ban(u, 0, reason).queue();
                        embed.setDescription(u.getAsMention() + " has been banned from " + event.getGuild().getName());
                    }
                } else {
                    embed.setColor(Color.red);
                    embed.setTitle("ERROR");
                    embed.setDescription("An unknown error occurred");
                }
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            } else {
                EmbedBuilder embed = setEmbedPresets(event);
                embed.setColor(Color.red);
                embed.setTitle("ERROR");
                embed.setDescription("You do not have permission to use this command");
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            }
        } else if(command.equalsIgnoreCase("unban")) {
            event.deferReply().queue();

            if(!Objects.requireNonNull(event.getGuild()).getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
                EmbedBuilder embed = setEmbedPresets(event);
                embed.setColor(Color.red);
                embed.setTitle("ERROR");
                embed.setDescription("**I** are not authorized to unban members :(");
                event.getHook().sendMessageEmbeds(embed.build()).queue();
                return;
            }

            if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.BAN_MEMBERS) || event.getMember().isOwner() || Objects.equals(String.valueOf(event.getMember().getIdLong()), ownerID)) {
                EmbedBuilder embed = setEmbedPresets(event);
                OptionMapping unbanUser = event.getOption("user");

                if(unbanUser != null) {
                    User u = unbanUser.getAsUser();
                    event.getGuild().unban(u).queue();
                    embed.setDescription(u.getAsMention()+"was unbanned from "+event.getGuild().getName());

                } else {
                    embed.setColor(Color.red);
                    embed.setTitle("ERROR");
                    embed.setDescription("An unknown error occurred");
                }

                event.getHook().sendMessageEmbeds(embed.build()).queue();
            } else {
                EmbedBuilder embed = setEmbedPresets(event);
                embed.setColor(Color.red);
                embed.setTitle("ERROR");
                embed.setDescription("You are not authorized to unban members");
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            }
        }

        /* *****
        *Invite*
         ******/

        else if(command.equalsIgnoreCase("invite")) {
            event.deferReply().setEphemeral(true).queue();
            EmbedBuilder embed = setEmbedPresets(event);
            embed.setDescription("My invite link is: https://tinyurl.com/yyoja52j");
            event.getHook().sendMessageEmbeds(embed.build()).queue();
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commands = new ArrayList<>();
        if(String.valueOf(event.getGuild().getIdLong()).equals(testServerID)) {
            commands.add(Commands.slash("getguilds", "Get the list of guilds this bot is in"));
        }
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

        //DINFO
        commands.add(Commands.slash("ping", "Test command"));
        commands.add(Commands.slash("roles", "Get roles in current server"));
        commands.add(Commands.slash("owner", "Get the owner of the current server"));
        OptionData user = new OptionData(OptionType.USER, "user", "The user you want to get the profile picture of; if none is provided, defaults to message author", false);
        commands.add(Commands.slash("pfp", "Get the profile picture of a user").addOptions(user));
        commands.add(Commands.slash("user", "Get server-specific information about a user").addOptions(user));
        OptionData role = new OptionData(OptionType.ROLE, "role", "the role you want to get information about", true);
        commands.add(Commands.slash("role", "Get information about a role").addOptions(role));
        commands.add(Commands.slash("server", "Get information about current server"));
        commands.add(Commands.slash("emojis", "Get emojis in current server"));

        //MODERATION
        OptionData user2 = new OptionData(OptionType.USER, "user", "The user you want to set the nickname of; changes author's nick if blank", false);
        OptionData nick = new OptionData(OptionType.STRING, "nick", "the new nick", true);
        commands.add(Commands.slash("nick", "Change the nickname of a user").addOptions(nick, user2));
        OptionData banUser = new OptionData(OptionType.USER, "user", "User to ban", true);
        OptionData banReason = new OptionData(OptionType.STRING, "reason", "reason user was banned", false);
        commands.add(Commands.slash("ban", "Ban a user").addOptions(banUser, banReason));
        OptionData unbanUser = new OptionData(OptionType.USER, "user", "User to unban", true);
        commands.add(Commands.slash("unban", "Unban a user").addOptions(unbanUser));

        //INVITE
        commands.add(Commands.slash("invite", "Get the bot's invite link"));
        event.getJDA().updateCommands().addCommands(commands).queue();
    }
}
