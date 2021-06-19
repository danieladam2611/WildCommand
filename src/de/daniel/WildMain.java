package de.daniel;

import cn.nukkit.plugin.PluginBase;
import de.daniel.Command.WildCommand;

public class WildMain extends PluginBase {

    private static WildMain instance;
    public static WildMain getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveResource("config.yml");

        getServer().getCommandMap().register("wild", new WildCommand());

        getLogger().info("§b" + getName() + " §fwas successfully §aEnabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("§b" + getName() + " §fwas successfully §cDisabled");
    }
}
