package fr.borane.hower.bungee.commands;

import fr.borane.hower.bungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Minage extends Command {

    public Minage() {
        super("secretminagetp");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("hower.bungee.minage") && !sender.hasPermission("*")){
            sender.sendMessage(new TextComponent("§4Vous n'avez pas la permission d'exécuter cette commande !"));
            return;
        }

        sender.sendMessage(new TextComponent("§aTéléportation au Minage !"));
        ((ProxiedPlayer)sender).connect(ProxyServer.getInstance().getServerInfo(Main.getInstance().getConfig("config").getString("minage")));
    }
}
