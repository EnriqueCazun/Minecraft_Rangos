package com.Rangos;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Rangos extends JavaPlugin implements Listener {

    private HashMap<UUID, Long> playTime = new HashMap<>();
    private RangoConfig rangoConfig;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        rangoConfig = new RangoConfig(this);
        
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("tiempo").setExecutor(new ComandoTiempo(this));
        
        loadPlayTimes();
        startPlayTimeTracker();
        getLogger().info("¡Plugin de Rangos activado!");
    }

    @Override
    public void onDisable() {
        savePlayTimes();
        getLogger().info("¡Plugin de Rangos desactivado!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        long tiempoAcumulado = playTime.getOrDefault(playerId, 0L);
        playTime.put(playerId, System.currentTimeMillis() - tiempoAcumulado);
        
        actualizarRango(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        if (playTime.containsKey(playerId)) {
            long tiempoJugado = System.currentTimeMillis() - playTime.get(playerId);
            playTime.put(playerId, tiempoJugado);
        }
        
        player.setDisplayName(player.getName());
    }

    private void startPlayTimeTracker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player jugador : Bukkit.getOnlinePlayers()) {
                    actualizarRango(jugador);
                }
                savePlayTimes();
            }
        }.runTaskTimer(this, 0L, 20L * 60 * 5);
    }

    public void actualizarRango(Player player) {
        UUID jugadorId = player.getUniqueId();
        if (!playTime.containsKey(jugadorId)) return;

        long tiempoJugado = getTiempoJugado(jugadorId);

        RangoConfig.Rango nuevoRango = rangoConfig.getRangoPorTiempo(tiempoJugado);

        player.setPlayerListName(nuevoRango.getColor() + "[" + nuevoRango.getNombre() + "] " 
                                + ChatColor.RESET + player.getName());
    }

    private void loadPlayTimes() {
        File file = new File(getDataFolder(), "playtimes.yml");
        if (file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            for (String key : config.getKeys(false)) {
                playTime.put(UUID.fromString(key), config.getLong(key));
            }
        }
    }

    private void savePlayTimes() {
        YamlConfiguration config = new YamlConfiguration();
        for (Map.Entry<UUID, Long> entry : playTime.entrySet()) {
            config.set(entry.getKey().toString(), entry.getValue());
        }
        try {
            config.save(new File(getDataFolder(), "playtimes.yml"));
        } catch (IOException e) {
            getLogger().severe("Error guardando tiempos: " + e.getMessage());
        }
    }

    public long getTiempoJugado(UUID jugadorId) {
        if (!playTime.containsKey(jugadorId)) return 0;
        return System.currentTimeMillis() - playTime.get(jugadorId);
    }

    public RangoConfig getRangoConfig() {
        return rangoConfig;
    }
}