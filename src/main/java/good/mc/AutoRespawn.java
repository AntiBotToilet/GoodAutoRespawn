package good.mc;

import org.bukkit.plugin.java.JavaPlugin;

public class AutoRespawn extends JavaPlugin {

    private static AutoRespawn instance;
    private MessagesManager messagesManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        messagesManager = new MessagesManager(this);

        getServer().getPluginManager().registerEvents(new DeathListener(this), this);

        ReloadCommand cmd = new ReloadCommand(this);
        if (getCommand("goodautorespawn") != null) {
            getCommand("goodautorespawn").setExecutor(cmd);
            getCommand("goodautorespawn").setTabCompleter(cmd);
        }

        getLogger().info("GoodAutoRespawn включен!");
    }

    @Override
    public void onDisable() {
        getLogger().info("GoodAutoRespawn выключен!");
    }

    public static AutoRespawn getInstance() {
        return instance;
    }

    public MessagesManager getMessagesManager() {
        return messagesManager;
    }

    public void reloadPlugin() {
        reloadConfig();
        messagesManager.reload();
    }
}