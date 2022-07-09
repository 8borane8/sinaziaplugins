package fr.borane.hower.bungee.commands;

import fr.borane.hower.bungee.Main;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class Pban extends Command {
    public Pban() {
        super("pban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("pban.use") && !sender.hasPermission("*")){
            sender.sendMessage(new TextComponent("§4Vous n'avez pas la permission d'exécuter cette commande !"));
            return;
        }

        if(args.length < 1){
            sender.sendMessage(new TextComponent("§4Utilise /pban [player] <raison>"));
            return;
        }

        if(BungeeCord.getInstance().getPlayer(args[0]) == null){
            sender.sendMessage(new TextComponent("§4Le joueur n'existe pas"));
            return;
        }

        Configuration config = Main.getInstance().getConfig("config");
        config.set("pban." + BungeeCord.getInstance().getPlayer(args[0]).getUniqueId() + ".moderator_name", sender.getName());

        if(args.length >= 2){
            StringBuilder sb = new StringBuilder();
            for(int i = 1; i < args.length; i++){
                sb.append(args[i]);
            }
            config.set("pban." + BungeeCord.getInstance().getPlayer(args[0]).getUniqueId() + ".raison", sb.toString());
        }else{
            config.set("pban." + BungeeCord.getInstance().getPlayer(args[0]).getUniqueId() + ".raison", config.getString("messages.default_raison"));
        }
        Main.getInstance().saveConfig(config, "config");

        String str = Main.getInstance().getConfig("config").getString("messages.pban");
        str = str.replace("%p", Main.getInstance().getConfig("config").getString("pban." + BungeeCord.getInstance().getPlayer(args[0]).getUniqueId() + ".moderator_name"));
        str = str.replace("%r", Main.getInstance().getConfig("config").getString("pban." + BungeeCord.getInstance().getPlayer(args[0]).getUniqueId() + ".raison"));
        BungeeCord.getInstance().getPlayer(args[0]).disconnect(new TextComponent(str));

        sender.sendMessage(new TextComponent("§aSuccess"));
    }
}
