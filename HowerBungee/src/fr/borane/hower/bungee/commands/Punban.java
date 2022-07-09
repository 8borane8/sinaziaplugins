package fr.borane.hower.bungee.commands;

import fr.borane.hower.bungee.Main;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class Punban extends Command {
    public Punban() {
        super("punban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("punban.use") && !sender.hasPermission("*")){
            sender.sendMessage(new TextComponent("§4Vous n'avez pas la permission d'exécuter cette commande !"));
            return;
        }

        if(args.length != 1){
            sender.sendMessage(new TextComponent("§4Utilise /punban [player]"));
            return;
        }

        if(BungeeCord.getInstance().getPlayer(args[0]) == null){
            sender.sendMessage(new TextComponent("§4Le joueur n'existe pas"));
            return;
        }

        Configuration config = Main.getInstance().getConfig("config");
        config.set("pban." + BungeeCord.getInstance().getPlayer(args[0]).getUniqueId(), null);
        config.set("tban." + BungeeCord.getInstance().getPlayer(args[0]).getUniqueId(), null);
        Main.getInstance().saveConfig(config, "config");
        sender.sendMessage(new TextComponent("§aSuccess"));
    }
}
