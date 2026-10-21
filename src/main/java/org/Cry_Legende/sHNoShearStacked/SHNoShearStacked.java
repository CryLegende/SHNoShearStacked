package org.Cry_Legende.sHNoShearStacked;

import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import com.bgsoftware.wildstacker.api.WildStackerAPI;
import org.bukkit.entity.Sheep;

public class SHNoShearStacked extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    // Gestion de l'interaction directe du joueur
    @EventHandler
    public void onPlayerInteractEntity(org.bukkit.event.player.PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Sheep &&
            event.getPlayer().getInventory().getItemInMainHand().getType() == Material.SHEARS) {
            Sheep sheep = (Sheep) event.getRightClicked();
            if (WildStackerAPI.getEntityAmount(sheep) > 1) {
                event.setCancelled(true);
            }
        }
    }

    // Gestion de la distribution par distributeur
    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {
        if (event.getItem().getType() == Material.SHEARS) {
            // Récupérer le bloc distributeur et déterminer la direction de diffusion
            Block dispenserBlock = event.getBlock();
            if (!(dispenserBlock.getBlockData() instanceof Directional)) {
                return;
            }
            Directional directional = (Directional) dispenserBlock.getBlockData();
            Block targetBlock = dispenserBlock.getRelative(directional.getFacing());

            // Vérifier si un mouton se trouve près du bloc cible
            for (Entity entity : targetBlock.getWorld().getNearbyEntities(targetBlock.getLocation(), 1, 1, 1)) {
                if (entity instanceof Sheep) {
                    Sheep sheep = (Sheep) entity;
                    if (WildStackerAPI.getEntityAmount(sheep) > 1) {
                        event.setCancelled(true);
                        break;
                    }
                }
            }
        }
    }
}

