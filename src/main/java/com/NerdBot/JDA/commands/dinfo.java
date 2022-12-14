package com.NerdBot.JDA.commands;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
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
import java.util.Random;

public class dinfo extends ListenerAdapter {

    // PEOPLE
    private final String ownerID = "555207100603826177";
    private final String echoVial = "609355069212852244";
    private final String lockdown = "703964837578932234";

    //SERVERS
    private final String testServerID = "714224578376892427";
    private final String NAYLE_staff_2021 = "860183556599709737";
    private final String ldl_server = "707226419993772112";
    private final String support_server = "1054081712243621948";

    //CHANNELS
   //private final String[] beeChannels = beeChannels =  ["710542883375022160", "829711652382441503", "901267588041044059"];
    private final String dm_logs = "1057761462434668574";
    private final String command_logs = "1057849647885996083";

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        /* *******
        *Pings*
        *********/

        Message message = event.getMessage();

        Mentions m = message.getMentions();
        UserSnowflake u = User.fromId(ownerID);

        if(m.isMentioned(u) && !String.valueOf(message.getGuild().getIdLong()).equalsIgnoreCase(NAYLE_staff_2021)) {
                event.getMessage().addReaction("pingsock:1056019618147340350").queue();
        } else if(m.isMentioned(User.fromId(lockdown))) {
            // Check LDL staff & ping
            // for(UserSnowflake user : User.fromId(echoVial)) {
        }


        /* ****************
        *Mention responses*
         *****************/

        else if(message.getContentRaw().equalsIgnoreCase("nayle") && String.valueOf(event.getGuild().getIdLong()).equalsIgnoreCase(NAYLE_staff_2021)) {
            event.getChannel().sendMessage("NAYLE? I LOVE NAYLE!").queue();
        } else if(message.getContentRaw().equalsIgnoreCase("outpost") && String.valueOf(event.getGuild().getIdLong()).equalsIgnoreCase(NAYLE_staff_2021)) {
            event.getChannel().sendMessage("ARE WE THERE YET?").queue();
        } else if(message.getContentRaw().equalsIgnoreCase("gps") && String.valueOf(event.getGuild().getIdLong()).equalsIgnoreCase(NAYLE_staff_2021)) {
            event.getChannel().sendMessage("WHERE ARE WE?").queue();
        } else if(message.getContentRaw().equalsIgnoreCase("good bot")) {
            event.getChannel().sendMessage("beep boop").queue();
        } else if(message.getContentRaw().equalsIgnoreCase("bee")) { //inBeeChannels
            event.getChannel().sendMessage("https://cdn.discordapp.com/attachments/710544037198823454/932073503224627300/IMG_3040.webp!").queue();
        } else if(message.getContentRaw().equalsIgnoreCase("uwu") && String.valueOf(event.getGuild().getIdLong()).equalsIgnoreCase(ldl_server)) {
            event.getChannel().sendMessage("Stop it, get some help").queue();
        } else if(String.valueOf(event.getAuthor().getId()).equalsIgnoreCase(echoVial)) {
            Random random = new Random();
            if(random.nextInt(20) == 0) {
                event.getChannel().sendMessage(event.getMessage().getContentRaw()).queue();
            }

        }
    }

    private EmbedBuilder setEmbedPresets(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();

        try {
            if(Objects.requireNonNull(event.getMember()).getColor() == null) {
                embed.setColor(Color.decode("0x4284ff"));
            } else {
                embed.setColor(event.getMember().getColor());
            }
        } catch(NullPointerException e) {
            embed.setColor(Color.decode("0x4284ff"));
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

        EmbedBuilder logEmbed = setEmbedPresets(event);
        logEmbed.setTitle("Command used");
        logEmbed.setDescription("Command **"+command+"** used in **"+ Objects.requireNonNull(event.getGuild()).getName()+"** by "+ Objects.requireNonNull(event.getMember()).getAsMention());
        Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getJDA().getGuildById(support_server)).getTextChannelById(command_logs)).sendMessageEmbeds(logEmbed.build()).queue();

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

        /* ****
         *Nuke*
         *****/

        else if(command.equalsIgnoreCase("nuke")) {
            event.deferReply().setEphemeral(true).queue();
            EmbedBuilder embed = setEmbedPresets(event);
            OptionMapping nukeNum = event.getOption("num");
            assert nukeNum != null;
                int numNuke = Math.abs(nukeNum.getAsInt());

            if(!Objects.requireNonNull(event.getGuild()).getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                embed.setColor(Color.red);
                embed.setTitle("ERROR");
                embed.setDescription("I do not have permission to delete messages");
            } else if(Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_MANAGE) || String.valueOf(event.getMember().getIdLong()).equalsIgnoreCase(ownerID)) {
                System.out.println(Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_MANAGE));
                System.out.println(String.valueOf(event.getMember().getIdLong()).equalsIgnoreCase(ownerID));
                event.getChannel().getIterableHistory()
                        .takeAsync(numNuke)
                        .thenAccept(event.getChannel()::purgeMessages);
                embed.setColor(Color.green);
                embed.setTitle("Success");
                embed.setDescription(numNuke+" messages have been nuked");
            } else {
                embed.setColor(Color.red);
                embed.setTitle("ERROR");
                embed.setDescription("You do not have permission to delete messages");
            }

            event.getHook().sendMessageEmbeds(embed.build()).queue();
        }

        /* ****
         *Text*
         *****/

        else if(command.equalsIgnoreCase("echo")) {
            event.deferReply().setEphemeral(true).queue();
            OptionMapping messageText = event.getOption("string");
            assert messageText != null;
                String text = messageText.getAsString();
            if((Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_MANAGE) && Objects.requireNonNull(event.getGuild()).getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) || (String.valueOf(event.getMember().getId()).equalsIgnoreCase(ownerID) && Objects.requireNonNull(event.getGuild()).getSelfMember().hasPermission(Permission.MESSAGE_MANAGE))) {
                EmbedBuilder embed = setEmbedPresets(event);
                embed.setTitle("Success");
                embed.setDescription("Echo was successful!");
                event.getHook().sendMessageEmbeds(embed.build()).queue();
                event.getChannel().sendMessage(text).queue();
            } else {
                EmbedBuilder embed = setEmbedPresets(event);
                embed.setColor(Color.red);
                embed.setTitle("ERROR");
                embed.setDescription("Either you or the bot does not have permissions to use echo");
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            }

        } else if(command.equalsIgnoreCase("dm")) {
            event.deferReply().setEphemeral(true).queue();
            OptionMapping messageText = event.getOption("string");
            OptionMapping messageUser = event.getOption("user");
            assert messageUser != null;
                User user = messageUser.getAsUser();
            assert messageText != null;
                String text = messageText.getAsString();

            if((Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_MANAGE) && Objects.requireNonNull(event.getGuild()).getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) || (String.valueOf(event.getMember().getId()).equalsIgnoreCase(ownerID) && Objects.requireNonNull(event.getGuild()).getSelfMember().hasPermission(Permission.MESSAGE_MANAGE))) {
                EmbedBuilder embed = setEmbedPresets(event);
                embed.setTitle("Success");
                embed.setDescription("DM was successful!");
                event.getHook().sendMessageEmbeds(embed.build()).queue();
                user.openPrivateChannel().flatMap(channel -> channel.sendMessage(text)).queue();
                EmbedBuilder embed2 = setEmbedPresets(event);
                embed2.setTitle("DM");
                embed2.setDescription(""+event.getUser().getAsMention()+" sent a DM to "+user.getAsMention()+":\n\n **"+text+"**");
                Objects.requireNonNull(Objects.requireNonNull(event.getGuild().getJDA().getGuildById(support_server)).getTextChannelById(dm_logs)).sendMessageEmbeds(embed2.build()).queue();
            } else {
                EmbedBuilder embed = setEmbedPresets(event);
                embed.setColor(Color.red);
                embed.setTitle("ERROR");
                embed.setDescription("Either you or the bot does not have permissions to use DM");
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            }

        }

        /* *****
        *Stream*
        *******/

        else if(command.equalsIgnoreCase("startstream")) {
            event.deferReply().setEphemeral(true).queue();
            if(event.getUser().getId().equalsIgnoreCase(ownerID)) {
                OptionMapping gameName = event.getOption("name");
                assert gameName != null;
                    String name = gameName.getAsString();
                event.getJDA().getPresence().setActivity(Activity.streaming(name, "https://twitch.tv/EmerqldEnderman"));
                EmbedBuilder embed = setEmbedPresets(event);
                embed.setTitle("Success");
                embed.setDescription("Status was updated");
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            } else {
                EmbedBuilder embed = setEmbedPresets(event);
                embed.setTitle("ERROR");
                embed.setColor(Color.red);
                embed.setDescription("You do not have permission to use this command");
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            }
        } else if(command.equalsIgnoreCase("stopstream")) {
            event.deferReply().setEphemeral(true).queue();
            if(event.getUser().getId().equalsIgnoreCase(ownerID)) {
                OptionMapping messageText = event.getOption("string");
                assert messageText != null;
                String string = messageText.getAsString();
                event.getJDA().getPresence().setActivity(Activity.playing(string));
                EmbedBuilder embed = setEmbedPresets(event);
                embed.setTitle("Success");
                embed.setDescription("Status was updated");
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            } else {
                EmbedBuilder embed = setEmbedPresets(event);
                embed.setTitle("ERROR");
                embed.setColor(Color.red);
                embed.setDescription("You do not have permission to use this command");
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            }
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

        //NUKE
        OptionData nukeNum = new OptionData(OptionType.INTEGER, "num", "number of messages to nuke", true);
        commands.add(Commands.slash("nuke", "Nuke num number of messages").addOptions(nukeNum));


        //TEXT
        OptionData messageText = new OptionData(OptionType.STRING, "string", "message to echo", true);
        commands.add(Commands.slash("echo", "Echo a message").addOptions(messageText));
        OptionData messageUser = new OptionData(OptionType.USER, "user", "user to message", true);
        commands.add(Commands.slash("dm", "DM a user a message").addOptions(messageUser, messageText));

        //STREAM

        OptionData newStatus = new OptionData(OptionType.STRING, "string", "new status", true);
        OptionData streamGame = new OptionData(OptionType.STRING, "name", "name of game", true);
        commands.add(Commands.slash("startstream", "Change status to streaming").addOptions(streamGame));
        commands.add(Commands.slash("stopstream", "Change status from streaming").addOptions(newStatus));

        event.getJDA().updateCommands().addCommands(commands).queue();
    }
}
