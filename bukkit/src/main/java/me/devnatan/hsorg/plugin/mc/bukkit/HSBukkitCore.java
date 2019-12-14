package me.devnatan.hsorg.plugin.mc.bukkit;

import me.devnatan.hsorg.plugin.mc.HSCore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class HSBukkitCore extends JavaPlugin {

    private HSCore backend;

    @Override
    public void onEnable() {
        Map<String, String> credentials = new HashMap<>();
        for (Map.Entry<String, Object> kv : getConfig().getConfigurationSection("api.credentials").getValues(false).entrySet()) {
            credentials.put(kv.getKey(), kv.getValue().toString());
        }

        backend = new HSCore(getLogger(), getName(), getConfig().getString("api.endpoint"), credentials);
        getLogger().info("Starting...");

        try {
            backend.start().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    @Override
    public void onDisable() {
        backend.stop();
    }

}
