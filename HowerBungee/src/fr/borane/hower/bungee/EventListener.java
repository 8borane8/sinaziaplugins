package fr.borane.hower.bungee;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class EventListener implements Listener {

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        if(Main.getInstance().getConfig("config").get("pban." + event.getPlayer().getUniqueId()) != null){
            String str = Main.getInstance().getConfig("config").getString("messages.pban");
            str = str.replace("%p", Main.getInstance().getConfig("config").getString("pban." + event.getPlayer().getUniqueId() + ".moderator_name"));
            str = str.replace("%r", Main.getInstance().getConfig("config").getString("pban." + event.getPlayer().getUniqueId() + ".raison"));
            event.getPlayer().disconnect(new TextComponent(str));
        }else if(Main.getInstance().getConfig("config").get("tban." + event.getPlayer().getUniqueId()) != null){
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Integer time = Main.getInstance().getConfig("config").getInt("tban." + event.getPlayer().getUniqueId() + ".duration");
            switch (Main.getInstance().getConfig("config").getString("tban." + event.getPlayer().getUniqueId() + ".unity")){
                case "d":
                    if(time * 60 * 60 * 24 + Main.getInstance().getConfig("config").getLong("tban." + event.getPlayer().getUniqueId() + ".time") < timestamp.getTime()){
                        return;
                    }
                    break;
                case "h":
                    if(time * 60 * 60 + Main.getInstance().getConfig("config").getLong("tban." + event.getPlayer().getUniqueId() + ".time") < timestamp.getTime()){
                        return;
                    }
                    break;
                case "m":
                    if(time * 60 + Main.getInstance().getConfig("config").getLong("tban." + event.getPlayer().getUniqueId() + ".time") < timestamp.getTime()){
                        return;
                    }
                    break;
                case "s":
                    if(time + Main.getInstance().getConfig("config").getLong("tban." + event.getPlayer().getUniqueId() + ".time") < timestamp.getTime()){
                        return;
                    }
                    break;
            }
            String str = Main.getInstance().getConfig("config").getString("messages.tban");
            str = str.replace("%p", Main.getInstance().getConfig("config").getString("tban." + event.getPlayer().getUniqueId() + ".moderator_name"));
            str = str.replace("%r", Main.getInstance().getConfig("config").getString("tban." + event.getPlayer().getUniqueId() + ".raison"));
            str = str.replace("%d", Main.getInstance().getConfig("config").getString("tban." + event.getPlayer().getUniqueId() + ".duration") + Main.getInstance().getConfig("config").getString("tban." + event.getPlayer().getUniqueId() + ".unity"));
            str = str.replace("%t", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Main.getInstance().getConfig("config").getLong("tban." + event.getPlayer().getUniqueId() + ".time")));
            event.getPlayer().disconnect(new TextComponent(str));
        }
    }

    @EventHandler
    public void onMessageSend(ChatEvent event){
        if(event.getSender() instanceof ProxiedPlayer && !event.getMessage().startsWith("/")){
            if(Main.getInstance().getConfig("config").get("pmute." + ((ProxiedPlayer)event.getSender()).getUniqueId()) != null){
                String str = Main.getInstance().getConfig("config").getString("messages.pmute");
                str = str.replace("%p", Main.getInstance().getConfig("config").getString("pmute." + ((ProxiedPlayer)event.getSender()).getUniqueId() + ".moderator_name"));
                str = str.replace("%r", Main.getInstance().getConfig("config").getString("pmute." + ((ProxiedPlayer)event.getSender()).getUniqueId() + ".raison"));
                ((ProxiedPlayer)event.getSender()).sendMessage(new TextComponent(str));
                event.setCancelled(true);
            }else if(Main.getInstance().getConfig("config").get("tmute." + ((ProxiedPlayer)event.getSender()).getUniqueId()) != null){
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                Integer time = Main.getInstance().getConfig("config").getInt("tmute." + ((ProxiedPlayer)event.getSender()).getUniqueId() + ".duration");
                switch (Main.getInstance().getConfig("config").getString("tmute." + ((ProxiedPlayer)event.getSender()).getUniqueId() + ".unity")){
                    case "d":
                        if(time * 1000 * 60 * 60 * 24 + Main.getInstance().getConfig("config").getLong("tmute." + ((ProxiedPlayer)event.getSender()).getUniqueId() + ".time") < timestamp.getTime()){
                            return;
                        }
                        break;
                    case "h":
                        if(time * 1000 * 60 * 60 + Main.getInstance().getConfig("config").getLong("tmute." + ((ProxiedPlayer)event.getSender()).getUniqueId() + ".time") < timestamp.getTime()){
                            return;
                        }
                        break;
                    case "m":
                        if(time * 1000 * 60 + Main.getInstance().getConfig("config").getLong("tmute." + ((ProxiedPlayer)event.getSender()).getUniqueId() + ".time") < timestamp.getTime()){
                            return;
                        }
                        break;
                    case "s":
                        if(time * 1000 + Main.getInstance().getConfig("config").getLong("tmute." + ((ProxiedPlayer)event.getSender()).getUniqueId() + ".time") < timestamp.getTime()){
                            System.out.println("return");
                            return;
                        }
                        break;
                }
                String str = Main.getInstance().getConfig("config").getString("messages.tmute");
                str = str.replace("%p", Main.getInstance().getConfig("config").getString("tmute." + ((ProxiedPlayer)event.getSender()).getUniqueId() + ".moderator_name"));
                str = str.replace("%r", Main.getInstance().getConfig("config").getString("tmute." + ((ProxiedPlayer)event.getSender()).getUniqueId() + ".raison"));
                str = str.replace("%d", Main.getInstance().getConfig("config").getString("tmute." + ((ProxiedPlayer)event.getSender()).getUniqueId() + ".duration") + Main.getInstance().getConfig("config").getString("tmute." + ((ProxiedPlayer)event.getSender()).getUniqueId() + ".unity"));
                str = str.replace("%t", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Main.getInstance().getConfig("config").getLong("tmute." + ((ProxiedPlayer)event.getSender()).getUniqueId() + ".time")));
                ((ProxiedPlayer)event.getSender()).sendMessage(new TextComponent(str));
                event.setCancelled(true);
            }
        }
    }

}
