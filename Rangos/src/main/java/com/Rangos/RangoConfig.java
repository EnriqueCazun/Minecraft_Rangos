package com.Rangos;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

public class RangoConfig {
    
    private final JavaPlugin plugin;
    private final List<Rango> rangos = new ArrayList<>();

    public RangoConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        cargarConfig();
    }

    public static class Rango {
        private final String nombre;
        private final ChatColor color;
        private final long tiempoRequerido;

        public Rango(String nombre, ChatColor color, long tiempoRequerido) {
            this.nombre = nombre;
            this.color = color;
            this.tiempoRequerido = tiempoRequerido;
        }

        public String getNombre() { return nombre; }
        public ChatColor getColor() { return color; }
        public long getTiempoRequerido() { return tiempoRequerido; }
    }

    private void cargarConfig() {
        plugin.saveDefaultConfig();
        ConfigurationSection seccion = plugin.getConfig().getConfigurationSection("rangos");
        
        if (seccion != null) {
            for (String key : seccion.getKeys(false)) {
                String nombre = seccion.getString(key + ".nombre");
                String color = seccion.getString(key + ".color");
                long tiempo = seccion.getLong(key + ".tiempo") * 60 * 60 * 1000;
                
                rangos.add(new Rango(nombre, ChatColor.valueOf(color), tiempo));
            }
        }
    }

    public Rango getRangoPorTiempo(long tiempo) {
        Rango mejorRango = new Rango("Nuevo", ChatColor.RED, 0);
        
        for (Rango rango : rangos) {
            if (tiempo >= rango.getTiempoRequerido()) {
                mejorRango = rango;
            }
        }
        return mejorRango;
    }

    public List<Rango> getRangos() {
        return new ArrayList<>(rangos);
    }
}