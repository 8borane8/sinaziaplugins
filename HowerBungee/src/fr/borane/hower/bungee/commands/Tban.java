package fr.borane.hower.bungee.commands;

import fr.borane.hower.bungee.Main;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import java.sql.Timestamp;

public class Tban extends Command {
    public Tban() {
        super("tban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("tban.use") && !sender.hasPermission("*")){
            sender.sendMessage(new TextComponent("§4Vous n'avez pas la permission d'exécuter cette commande !"));
            return;
        }

        if(args.length < 3){
            sender.sendMessage(new TextComponent("§4Utilise /tban [player] [duration] [unity] <raison>"));
            return;
        }

        if(BungeeCord.getInstance().getPlayer(args[0]) == null){
            sender.sendMessage(new TextComponent("§4Le joueur n'existe pas"));
            return;
        }

        if(!args[2].equals(new String[]{"d", "h", "m", "s"})){
            sender.sendMessage(new TextComponent("§4Les unités disponibles sont d/h/m/s"));
            return;
        }

        try{
            Integer.parseInt(args[1]);
        }catch(NumberFormatException e){
            sender.sendMessage(new TextComponent("§4La duration est invalide"));
            return;
        }

        Configuration config = Main.getInstance().getConfig("config");
        config.set("tban." + BungeeCord.getInstance().getPlayer(args[0]).getUniqueId() + ".moderator_name", sender.getName());
        config.set("tban." + BungeeCord.getInstance().getPlayer(args[0]).getUniqueId() + ".time", new Timestamp(System.currentTimeMillis()).getTime());
        config.set("tban." + BungeeCord.getInstance().getPlayer(args[0]).getUniqueId() + ".duration", Integer.parseInt(args[1]));
        config.set("tban." + BungeeCord.getInstance().getPlayer(args[0]).getUniqueId() + ".unity", args[2]);

        if(args.length >= 4){
            StringBuilder sb = new StringBuilder();
            for(int i = 3; i < args.length; i++){
                sb.append(args[i]);
            }
            config.set("tban." + BungeeCord.getInstance().getPlayer(args[0]).getUniqueId() + ".raison", sb.toString());
        }else{
            config.set("tban." + BungeeCord.getInstance().getPlayer(args[0]).getUniqueId() + ".raison", config.getString("messages.default_raison"));
        }
        Main.getInstance().saveConfig(config, "config");

        sender.sendMessage(new TextComponent("§aSuccess"));
    }
}
