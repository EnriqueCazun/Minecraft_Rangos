package com.Rangos;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ComandoTiempo implements CommandExecutor {

    private final Rangos plugin;

    public ComandoTiempo(Rangos plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo es para jugadores.");
            return true;
        }

        Player jugador = (Player) sender;
        long tiempo = plugin.getTiempoJugado(jugador.getUniqueId());
        RangoConfig.Rango rangoActual = plugin.getRangoConfig().getRangoPorTiempo(tiempo);
        
        String tiempoFormateado = formatTime(tiempo);
        String mensaje = ChatColor.GOLD + "Tiempo jugado: " + ChatColor.WHITE + tiempoFormateado + "\n" +
                        ChatColor.GOLD + "Rango actual: " + rangoActual.getColor() + rangoActual.getNombre();
        
        // Usar getRangos() en lugar de acceder directamente al campo
        RangoConfig.Rango proximoRango = getProximoRango(rangoActual);
        if (proximoRango != null) {
            long tiempoFaltante = proximoRango.getTiempoRequerido() - tiempo;
            mensaje += "\n" + ChatColor.GOLD + "Siguiente rango: " + proximoRango.getColor() + proximoRango.getNombre() +
                      ChatColor.GOLD + " en " + formatTime(tiempoFaltante);
        }
        
        jugador.sendMessage(mensaje);
        return true;
    }

    private RangoConfig.Rango getProximoRango(RangoConfig.Rango actual) {

        for (RangoConfig.Rango rango : plugin.getRangoConfig().getRangos()) {
            if (rango.getTiempoRequerido() > actual.getTiempoRequerido()) {
                return rango;
            }
        }
        return null;
    }

    private String formatTime(long millis) {
        long hours = millis / (60 * 60 * 1000);
        long minutes = (millis % (60 * 60 * 1000)) / (60 * 1000);
        return String.format("%d horas y %d minutos", hours, minutes);
    }
}