package net.bghddev.pearls.purgepearls;

import net.bghddev.pearls.purgepearls.commands.PurgePearlsCommand;
import net.bghddev.pearls.purgepearls.listeners.JoinListener;
import net.bghddev.pearls.purgepearls.utils.CC;
import net.bghddev.pearls.purgepearls.utils.ConfigFile;
import net.bghddev.pearls.purgepearls.utils.Timer;
import net.bghddev.pearls.purgepearls.utils.license.License;
import org.bukkit.plugin.java.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.entity.*;
import org.bukkit.metadata.*;
import org.bukkit.scheduler.*;

import org.bukkit.inventory.*;
import org.bukkit.material.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.*;
import org.bukkit.event.player.*;

public class PurgePearls extends JavaPlugin implements Listener
{
    private ArrayList<Player> playersToLastTeleport;
    private static PurgePearls instance;
    private ConfigFile configfile;
    private Timer timer;
    public ArrayList<UUID> onebyone;

    public void onEnable() {
        PurgePearls.instance = this;
        this.playersToLastTeleport = new ArrayList<>();
        this.saveDefaultConfig();
        this.timer = new Timer(this);
        this.timer.enable();
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        this.configfile = new ConfigFile();
        this.getCommand("purgepearls").setExecutor(new PurgePearlsCommand());
        getLogger().info("The config file has been loaded!");
        getLogger().info("Pearling methods have been loaded!");
        getLogger().info("Commands have been loaded!");


        //License
        new License(this, getConfig().getString("license", "XXXX"));
        if (!this.isEnabled()) return;


        Bukkit.getConsoleSender().sendMessage(CC.translate("&d[PurgePearls]&d has been &aenabled!"));
    }

    public void onDisable() {
        PurgePearls.instance = null;
        this.getLogger().info(this.getName() + " has been disabled!");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDamage(final EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof EnderPearl) {
            EnderPearl pearl = (EnderPearl) e.getDamager();
            Player player = (Player) pearl.getShooter();
            Player damaged = (Player) e.getEntity();
            if (player.getType() != null && e.getDamager().getType().equals(EntityType.ENDER_PEARL) && player != null && damaged != null) {
                Location loc = new Location(damaged.getWorld(), damaged.getLocation().getX(), damaged.getLocation().getY() + 0.75, damaged.getLocation().getZ());
                loc.setPitch(player.getLocation().getPitch());
                loc.setYaw(player.getLocation().getYaw());
            }
        } else {
            return;
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPearlLaunch(final ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof EnderPearl) || !(event.getEntity().getShooter() instanceof Player)) {
            return;
        }
        final EnderPearl enderPearl = (EnderPearl)event.getEntity();
        final Player player = (Player)enderPearl.getShooter();
        final Block block = this.getBlockTali(player);
        final Block block2 = this.getBlockTali2(player);
        final Block block3 = this.getBlockTali3(player);
        if (event.getEntity() instanceof EnderPearl) {
            if (block.getRelative(BlockFace.UP).getType() != Material.FENCE_GATE
                    && block.getRelative(BlockFace.UP).getType().isSolid()
                    && !block3.getLocation().getBlock().getRelative(BlockFace.SELF).getType().isSolid()

                    && !block.getRelative(BlockFace.SELF).getType().isSolid()

                    && block.getRelative(BlockFace.DOWN).getType() != Material.FENCE_GATE
                    && block.getRelative(BlockFace.DOWN).getType().isSolid()

                    && !block.getLocation().getBlock().getRelative(BlockFace.valueOf(this.getDirectionCompleteName(player))).getType().isSolid()) {
                enderPearl.setMetadata("selfair", new FixedMetadataValue(this, true));
            }
            if (block != null
                    && getInstance().getConfig().getBoolean("PearlMethods.cPearl")
                    && this.isCobbleWall(block)
                    && block.getRelative(BlockFace.UP).getType().isSolid()
                    && !block3.getLocation().getBlock().getRelative(BlockFace.SELF).getType().isSolid()) {
                enderPearl.setMetadata("cpearl", new FixedMetadataValue(this, true));
            }
            if (block.getRelative(BlockFace.SELF).getType() == Material.FENCE_GATE
                    && block.getRelative(BlockFace.UP).getType() != Material.FENCE_GATE
                    && block.getRelative(BlockFace.UP).getType().isSolid()
                    && !block3.getLocation().getBlock().getRelative(BlockFace.SELF).getType().isSolid()
                    && block.getRelative(BlockFace.DOWN).getType().isSolid()
                    && ((((Gate)block.getRelative(BlockFace.SELF).getState().getData()).getFacing() == BlockFace.NORTH
                    && BlockFace.valueOf(this.getDirectionCompleteName(player)) == BlockFace.NORTH) || (((Gate)block.getRelative(BlockFace.SELF).getState().getData()).getFacing() == BlockFace.SOUTH
                    && BlockFace.valueOf(this.getDirectionCompleteName(player)) == BlockFace.NORTH) || (((Gate)block.getRelative(BlockFace.SELF).getState().getData()).getFacing() == BlockFace.NORTH
                    && BlockFace.valueOf(this.getDirectionCompleteName(player)) == BlockFace.SOUTH) || (((Gate)block.getRelative(BlockFace.SELF).getState().getData()).getFacing() == BlockFace.SOUTH
                    && BlockFace.valueOf(this.getDirectionCompleteName(player)) == BlockFace.SOUTH) || (((Gate)block.getRelative(BlockFace.SELF).getState().getData()).getFacing() == BlockFace.WEST
                    && BlockFace.valueOf(this.getDirectionCompleteName(player)) == BlockFace.WEST) || (((Gate)block.getRelative(BlockFace.SELF).getState().getData()).getFacing() == BlockFace.EAST
                    && BlockFace.valueOf(this.getDirectionCompleteName(player)) == BlockFace.WEST) || (((Gate)block.getRelative(BlockFace.SELF).getState().getData()).getFacing() == BlockFace.WEST
                    && BlockFace.valueOf(this.getDirectionCompleteName(player)) == BlockFace.EAST) || (((Gate)block.getRelative(BlockFace.SELF).getState().getData()).getFacing() == BlockFace.EAST
                    && BlockFace.valueOf(this.getDirectionCompleteName(player)) == BlockFace.EAST))) {
                enderPearl.setMetadata("cpearl", new FixedMetadataValue(this, true));
            }
            if (block.getRelative(BlockFace.SELF).getType() == Material.FENCE_GATE
                    && block.getRelative(BlockFace.UP).getType() != Material.FENCE_GATE
                    && block.getRelative(BlockFace.UP).getType().isSolid()
                    && block.getRelative(BlockFace.DOWN).getType().isSolid()
                    && (((Gate)block.getRelative(BlockFace.SELF).getState().getData()).getFacing() != BlockFace.NORTH || BlockFace.valueOf(this.getDirectionCompleteName(player)) != BlockFace.NORTH)
                    && (((Gate)block.getRelative(BlockFace.SELF).getState().getData()).getFacing() != BlockFace.SOUTH || BlockFace.valueOf(this.getDirectionCompleteName(player)) != BlockFace.NORTH)
                    && (((Gate)block.getRelative(BlockFace.SELF).getState().getData()).getFacing() != BlockFace.NORTH || BlockFace.valueOf(this.getDirectionCompleteName(player)) != BlockFace.SOUTH)
                    && (((Gate)block.getRelative(BlockFace.SELF).getState().getData()).getFacing() != BlockFace.SOUTH || BlockFace.valueOf(this.getDirectionCompleteName(player)) != BlockFace.SOUTH)
                    && (((Gate)block.getRelative(BlockFace.SELF).getState().getData()).getFacing() != BlockFace.WEST || BlockFace.valueOf(this.getDirectionCompleteName(player)) != BlockFace.WEST)
                    && (((Gate)block.getRelative(BlockFace.SELF).getState().getData()).getFacing() != BlockFace.EAST || BlockFace.valueOf(this.getDirectionCompleteName(player)) != BlockFace.WEST)
                    && (((Gate)block.getRelative(BlockFace.SELF).getState().getData()).getFacing() != BlockFace.WEST || BlockFace.valueOf(this.getDirectionCompleteName(player)) != BlockFace.EAST)
                    && (((Gate)block.getRelative(BlockFace.SELF).getState().getData()).getFacing() != BlockFace.EAST || BlockFace.valueOf(this.getDirectionCompleteName(player)) != BlockFace.EAST)) {
                // event.setCancelled(true);
            }
            if (block != null
                    && getInstance().getConfig().getBoolean("PearlMethods.Chest-Pearl")) {
                if (this.isChest(block)
                        && block.getRelative(BlockFace.UP).getType().isSolid()
                        && !block3.getLocation().getBlock().getRelative(BlockFace.SELF).getType().isSolid()
                        && block.getRelative(BlockFace.UP).getType() != Material.FENCE_GATE
                        && !block.getLocation().getBlock().getRelative(BlockFace.valueOf(this.getDirectionCompleteName(player))).getType().isSolid()) {
                    enderPearl.setMetadata("cpearl", new FixedMetadataValue(this, true));
                }
            }
            if (block != null
                    && this.isEnchantmentTable(block)
                    && block.getRelative(BlockFace.UP).getType().isSolid()
                    && !block3.getLocation().getBlock().getRelative(BlockFace.SELF).getType().isSolid()) {
                enderPearl.setMetadata("cpearl", new FixedMetadataValue(this, true));
            }
            if (block != null
                    && this.isSign(block)
                    && block.getRelative(BlockFace.UP).getType().isSolid()
                    && !block3.getLocation().getBlock().getRelative(BlockFace.SELF).getType().isSolid()) {
                enderPearl.setMetadata("cpearl", new FixedMetadataValue(this, true));
            }
            if (block != null
                    && this.isAnvil(block)
                    && block.getRelative(BlockFace.UP).getType().isSolid()
                    && !block3.getLocation().getBlock().getRelative(BlockFace.SELF).getType().isSolid()) {
                enderPearl.setMetadata("cpearl", new FixedMetadataValue(this, true));
            }
            if (block != null
                    && getInstance().getConfig().getBoolean("PearlMethods.Chest-Pearl")
                    && (block.getRelative(BlockFace.DOWN).getType() == Material.CHEST || block.getRelative(BlockFace.DOWN).getType() == Material.TRAPPED_CHEST)
                    && block.getRelative(BlockFace.UP).getType() != Material.CHEST
                    && block.getRelative(BlockFace.UP).getType() != Material.TRAPPED_CHEST
                    && block.getRelative(BlockFace.UP).getType().isSolid()
                    && !block3.getLocation().getBlock().getRelative(BlockFace.SELF).getType().isSolid()
                    && block.getRelative(BlockFace.UP).getType() != Material.FENCE_GATE) {
                enderPearl.setMetadata("cpearl", new FixedMetadataValue(this, true));
            }
            if (block != null
                    && block.getRelative(BlockFace.SELF).getType() == Material.TRAP_DOOR
                    && block.getRelative(BlockFace.UP).getType().isSolid()
                    && !block3.getLocation().getBlock().getRelative(BlockFace.SELF).getType().isSolid()) {
                enderPearl.setMetadata("cpearl", new FixedMetadataValue(this, true));
            }
            if (block != null && getInstance().getConfig().getBoolean("PearlMethods.EndPortal-Pearl")
                    && this.isEndPortal(block)
                    && block.getRelative(BlockFace.UP).getType().isSolid()
                    && !block3.getLocation().getBlock().getRelative(BlockFace.SELF).getType().isSolid()) {
                enderPearl.setMetadata("cpearl", new FixedMetadataValue(this, true));
            }
            if (block != null && getInstance().getConfig().getBoolean("PearlMethods.Piston-Pearl")
                    && this.isPiston(block)
                    && block.getRelative(BlockFace.UP).getType().isSolid()
                    && !block3.getLocation().getBlock().getRelative(BlockFace.SELF).getType().isSolid()) {
                enderPearl.setMetadata("cpearl", new FixedMetadataValue(this, true));
            }
            if (block != null && getInstance().getConfig().getBoolean("PearlMethods.Bed-Pearl")
                    && this.isBed(block)
                    && block.getRelative(BlockFace.UP).getType().isSolid()
                    && !block3.getLocation().getBlock().getRelative(BlockFace.SELF).getType().isSolid()) {
                enderPearl.setMetadata("cpearl", new FixedMetadataValue(this, true));
            }
            if (block != null
                    && !block3.getLocation().getBlock().getRelative(BlockFace.SELF).getType().isSolid()
                    && block.getRelative(BlockFace.UP).getType().isSolid()
                    && ((getInstance().getConfig().getBoolean("PearlMethods.TalibanPearl.Slabs")
                    && this.isSlab(block))
                    ||
                    (getInstance().getConfig().getBoolean("PearlMethods.TalibanPearl.Stairs")
                            && this.isStair(block)))) {
                enderPearl.setMetadata("tali", new FixedMetadataValue(this, true));
            }
            if (block.getRelative(BlockFace.UP).getType() == Material.FENCE_GATE
                    && !((Gate)block.getRelative(BlockFace.UP).getState().getData()).isOpen()
                    && block.getRelative(BlockFace.SELF).getType() == Material.FENCE_GATE
                    && !enderPearl.hasMetadata("fenceopen")
                    && ((Gate)block.getRelative(BlockFace.SELF).getState().getData()).isOpen()
                    && block.getRelative(BlockFace.DOWN).getType().isSolid()
                    && !block.getLocation().getBlock().getRelative(BlockFace.valueOf(this.getDirectionCompleteName(player))).getType().isSolid()
                    && !block2.getLocation().getBlock().getRelative(BlockFace.valueOf(this.getDirectionCompleteName(player))).getType().isSolid()) {
                enderPearl.setMetadata("fenceopen", new FixedMetadataValue(this, true));
            }
            if (block.getRelative(BlockFace.UP).getType() != Material.FENCE_GATE
                    && block.getRelative(BlockFace.UP).getType().isSolid()
                    && !block.getRelative(BlockFace.SELF).getType().isSolid()
                    && block.getRelative(BlockFace.DOWN).getType().isSolid()) {
                enderPearl.setMetadata("fenceopen", new FixedMetadataValue(this, true));
            }
            if (block != null && block.getRelative(BlockFace.UP).getType() == Material.FENCE_GATE
                    && block.getRelative(BlockFace.UP).getType() == Material.FENCE_GATE
                    && !((Gate)block.getRelative(BlockFace.UP).getState().getData()).isOpen()
                    && block.getRelative(BlockFace.SELF).getType() == Material.FENCE_GATE
                    && !((Gate)block.getRelative(BlockFace.SELF).getState().getData()).isOpen() ) {
                event.setCancelled(true);
            }

        }
            //SELFAIR
            if (!enderPearl.hasMetadata("selfair")
                    //        		&& !onebyone.contains(player.getUniqueId())
                    && block.getRelative(BlockFace.UP).getType() != Material.FENCE_GATE
                    && block.getRelative(BlockFace.UP).getType().isSolid()

                    && !block.getRelative(BlockFace.SELF).getType().isSolid()

                    && block.getRelative(BlockFace.DOWN).getType().isSolid()
                    && block.getRelative(BlockFace.DOWN).getType() != Material.FENCE_GATE

                    && (block2.getLocation().getBlock().getRelative(BlockFace.UP).getType().isSolid()
                    || block.getLocation().getBlock().getRelative(BlockFace.valueOf(this.getDirectionCompleteName(player))).getType().isSolid())) {
                //event.setCancelled(true);
            }
            if (block != null) {
                final Block b = player.getTargetBlock((HashSet<Byte>) null, 4);
                if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR
                        && getInstance().getConfig().getBoolean("Settings.Refund-If-CritBlock")
                        && block.getType().isSolid()
                        && block2.getType().isSolid()
                        && block.getType() != Material.FENCE_GATE
                        && !this.isStair(block)
                        && !this.isSlab(block) && !this.isCobbleWall(block)
                        && !this.isEndPortal(block) && !this.isBed(block)
                        && !this.isEnchantmentTable(block)
                        && !this.isPiston(block)
                        && !this.isChest(block)
                        && !enderPearl.hasMetadata("cpearl")
                        && !enderPearl.hasMetadata("fenceopen")
                        && !enderPearl.hasMetadata("tali")
                        && b.getType() != null
                        && b.getType() != Material.AIR
                        && b.getType() != Material.TRAP_DOOR
                        && b.getType() != Material.WOOD_STEP
                        && b.getType() != Material.STEP
                        && b.getType() != Material.DAYLIGHT_DETECTOR
                        && b.getType() != Material.ACACIA_STAIRS
                        && b.getType() != Material.BIRCH_WOOD_STAIRS
                        && b.getType() != Material.BRICK_STAIRS
                        && b.getType() != Material.COBBLESTONE_STAIRS
                        && b.getType() != Material.DARK_OAK_STAIRS
                        && b.getType() != Material.JUNGLE_WOOD_STAIRS
                        && b.getType() != Material.NETHER_BRICK_STAIRS
                        && b.getType() != Material.QUARTZ_STAIRS
                        && b.getType() != Material.SANDSTONE_STAIRS
                        && b.getType() != Material.SPRUCE_WOOD_STAIRS
                        && b.getType() != Material.SMOOTH_STAIRS
                        && b.getType() != Material.WOOD_STAIRS) {
                    event.setCancelled(true);
                }
            }
            if (event.isCancelled()) {
                if (this.getConfig().getBoolean("PearlRefund.PearlRefund-Command")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), this.getConfig().getString("PearlRefund.Command").replace("%player%", player.getName()));
                }
                if (this.getConfig().getBoolean("PearlRefund.PearlRefund-Message")) {
                    player.sendMessage(CC.translate(this.getConfig().getString("PearlRefund.Message")));
                }
                new BukkitRunnable() {
                    public void run() {
                        if (PurgePearls.getInstance().getConfig().getBoolean("PearlRefund.PearlRefund-Remove-Timer")) {
                            PurgePearls.getInstance().getTimer().cooldown.remove(player.getUniqueId());
                        }
                    }
                }.runTaskLater(this, 2L);
                if (this.getConfig().getBoolean("Settings.PearlRefund")) {
                    player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
                }
            }
        }



    private boolean isPiston(final Block block) {
        final Material material = block.getType();
        return material != null && material != Material.AIR && (material == Material.PISTON_EXTENSION || material == Material.PISTON_BASE || material == Material.PISTON_MOVING_PIECE || material == Material.PISTON_STICKY_BASE);
    }

    private boolean isEndPortal(final Block block) {
        final Material material = block.getType();
        return material != null && material != Material.AIR && material == Material.ENDER_PORTAL_FRAME;
    }

    private boolean isBed(final Block block) {
        final Material material = block.getType();
        return material != null && material != Material.AIR && (material == Material.BED_BLOCK || material == Material.BED);
    }

    private boolean isSign(final Block block) {
        final Material material = block.getType();
        return material != null && material != Material.AIR && (material == Material.SIGN || material == Material.WALL_SIGN || material == Material.SIGN_POST);
    }

    private boolean isAnvil(final Block block) {
        final Material material = block.getType();
        return material != null && material != Material.AIR && material == Material.ANVIL;
    }

    private boolean isChest(final Block block) {
        final Material material = block.getType();
        return material != null && material != Material.AIR && (material == Material.ENDER_CHEST || material == Material.CHEST || material == Material.TRAPPED_CHEST);
    }

    private boolean isEnchantmentTable(final Block block) {
        final Material material = block.getType();
        return material != null && material != Material.AIR && material == Material.ENCHANTMENT_TABLE;
    }

    private boolean isCobbleWall(final Block block) {
        final Material material = block.getType();
        return material != null && material != Material.AIR && material == Material.COBBLE_WALL;
    }

    private boolean isSlab(final Block block) {
        final Material material = block.getType();
        return material != null && material != Material.AIR && (material == Material.WOOD_STEP || material == Material.STEP || material == Material.DAYLIGHT_DETECTOR);
    }

    private boolean isStair(final Block block) {
        final Material material = block.getType();
        return material != null && material != Material.AIR && (material == Material.ACACIA_STAIRS || material == Material.BIRCH_WOOD_STAIRS || material == Material.BRICK_STAIRS || material == Material.COBBLESTONE_STAIRS || material == Material.DARK_OAK_STAIRS || material == Material.JUNGLE_WOOD_STAIRS || material == Material.NETHER_BRICK_STAIRS || material == Material.QUARTZ_STAIRS || material == Material.SANDSTONE_STAIRS || material == Material.SPRUCE_WOOD_STAIRS || material == Material.SMOOTH_STAIRS || material == Material.WOOD_STAIRS);
    }

    @EventHandler
    public void onPearlLand(final ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof EnderPearl) || !(event.getEntity().getShooter() instanceof Player)) {
            return;
        }
        final EnderPearl enderPearl = (EnderPearl)event.getEntity();
        final Location enderLocation = enderPearl.getLocation().clone();
        final Player player = (Player)enderPearl.getShooter();
        if (enderPearl.hasMetadata("onebyone") && this.getLocTali(enderLocation, player) != null) {
            if (this.getLocTali(enderLocation, player) != null) {
                player.teleport(this.getOneByOne(enderLocation, player), PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
            }
            return;
        }
        if (enderPearl.hasMetadata("cpearl") && this.getLocTali(enderLocation, player) != null) {
            if (this.getLocTali(enderLocation, player) != null) {
                player.teleport(this.getLocTali(enderLocation, player), PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
            }
            return;
        }
        if (enderPearl.hasMetadata("selfair") && this.getLocTali(enderLocation, player) != null) {
            if (this.getLocTali(enderLocation, player) != null) {
                player.teleport(this.getLocTali(enderLocation, player), PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
            }
            return;
        }
        if (enderPearl.hasMetadata("tali") && this.getLocTali(enderLocation, player) != null) {
            if (this.getLocTali(enderLocation, player) != null) {
                player.teleport(this.getLocTali(enderLocation, player), PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
            }
            return;
        }
        if (enderPearl.hasMetadata("fenceopen") && this.getLocTali(enderLocation, player) != null) {
            if (this.getLocOpenFence(enderLocation, player) != null) {
                player.teleport(this.getLocOpenFence(enderLocation, player), PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
            }
            return;
        }

        final Block block = this.getBlockTali(enderLocation, player);
        final Block block2 = this.getBlockTali2(player);
        if (block == null) {
            return;
        }
    }

    @EventHandler
    public void onClip(final PlayerTeleportEvent e) {
        final Player p = e.getPlayer();
        final Location to = e.getTo().clone();
        final Block block2 = this.getBlockTali2(e.getPlayer());
        if (e.getCause() != null && e.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            return;
        }
        if (e.getCause() != null && e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && p.getItemInHand().getType() == Material.ENDER_PEARL) {
            if (to.getBlock().getRelative(BlockFace.UP).getType().isSolid()
                    && to.getBlock().getRelative(BlockFace.DOWN).getType().isSolid()
                    && block2.getRelative(BlockFace.DOWN).getType().isSolid()
                    && !this.isStair(to.getBlock())
                    && !this.isSlab(to.getBlock())
                    && !this.isCobbleWall(to.getBlock())
                    && !this.isEndPortal(to.getBlock())
                    && !this.isPiston(to.getBlock())
                    && !this.isBed(to.getBlock())
                    && !this.isChest(to.getBlock())
                    && !this.isSign(to.getBlock())
                    && !this.isAnvil(to.getBlock())
                    && !this.isEnchantmentTable(to.getBlock())
                    && to.getBlock().getType() != Material.FENCE_GATE
                    && to.getBlock().getType() != Material.TRAP_DOOR) {
                e.setCancelled(true);
            }
            if (to.getBlock() != null && (to.getBlock().getRelative(BlockFace.SELF).getType() == Material.ACACIA_STAIRS || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.BIRCH_WOOD_STAIRS || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.BRICK_STAIRS || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.COBBLESTONE_STAIRS || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.DARK_OAK_STAIRS || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.JUNGLE_WOOD_STAIRS || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.NETHER_BRICK_STAIRS || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.QUARTZ_STAIRS || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.SANDSTONE_STAIRS || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.SMOOTH_STAIRS || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.SPRUCE_WOOD_STAIRS || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.WOOD_STAIRS) && to.getBlock().getRelative(BlockFace.UP).getType().isSolid()) {
                to.setY(to.getY() - 0.5);
                e.setTo(to);
            }
            if (to.getBlock() != null && (to.getBlock().getType() == Material.STEP || to.getBlock().getType() == Material.WOOD_STEP) && to.getBlock().getRelative(BlockFace.UP).getType().isSolid()) {
                to.setY(to.getY() - 0.5);
                e.setTo(to);
            }
            if (to.getBlock().getRelative(BlockFace.UP).getType() != Material.AIR
                    && !to.getBlock().getLocation().getBlock().getRelative(BlockFace.valueOf(this.getDirectionCompleteName(p))).getType().isSolid()
                    && !this.isStair(to.getBlock())
                    && !this.isSlab(to.getBlock())
                    && !this.isCobbleWall(to.getBlock())
                    && !this.isEndPortal(to.getBlock())
                    && !this.isPiston(to.getBlock())
                    && !this.isBed(to.getBlock())
                    && !this.isChest(to.getBlock())
                    && !this.isSign(to.getBlock())
                    && !this.isAnvil(to.getBlock())
                    && !this.isEnchantmentTable(to.getBlock())
                    && to.getBlock().getType() != Material.FENCE_GATE
                    && to.getBlock().getType() != Material.TRAP_DOOR) {
                to.setY(to.getY() - 1.5);
                e.setTo(to);
            }
            if (to.getBlock().getRelative(BlockFace.UP).getType() != Material.AIR
                    && to.getBlock().getLocation().getBlock().getRelative(BlockFace.valueOf(this.getDirectionCompleteName(p))).getType().isSolid()
                    && !this.isStair(to.getBlock())
                    && !this.isSlab(to.getBlock())
                    && !this.isCobbleWall(to.getBlock())
                    && !this.isEndPortal(to.getBlock())
                    && !this.isPiston(to.getBlock())
                    && !this.isBed(to.getBlock())
                    && !this.isChest(to.getBlock())
                    && !this.isSign(to.getBlock())
                    && !this.isAnvil(to.getBlock())
                    && !this.isEnchantmentTable(to.getBlock())
                    && to.getBlock().getType() != Material.FENCE_GATE
                    && to.getBlock().getType() != Material.TRAP_DOOR) {
                to.setY(to.getY() - 0.5);
                e.setTo(to);
            }
            if (to.getBlock().getRelative(BlockFace.UP).getType() == Material.FENCE_GATE
                    && to.getBlock().getRelative(BlockFace.SELF).getType() == Material.FENCE_GATE
                    && !((Gate)to.getBlock().getRelative(BlockFace.SELF).getState().getData()).isOpen()
                    && to.getBlock().getRelative(BlockFace.DOWN).getType().isSolid()) {
                // e.setCancelled(true);
            }
            if (to.getBlock().getRelative(BlockFace.UP).getType() != Material.FENCE_GATE
                    && to.getBlock().getRelative(BlockFace.SELF).getType() == Material.FENCE_GATE
                    && !((Gate)to.getBlock().getRelative(BlockFace.SELF).getState().getData()).isOpen()
                    && to.getBlock().getRelative(BlockFace.DOWN).getType() == Material.FENCE_GATE
                    && !((Gate)to.getBlock().getRelative(BlockFace.DOWN).getState().getData()).isOpen()) {
                //e.setCancelled(true);
            }
            if (!getInstance().getConfig().getBoolean("PearlMethods.Chest-Pearl") && (to.getBlock().getRelative(BlockFace.SELF).getType() == Material.CHEST || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.TRAPPED_CHEST) && to.getBlock().getRelative(BlockFace.UP).getType().isSolid() && to.getBlock().getRelative(BlockFace.UP).getType() != Material.FENCE_GATE) {
                e.setCancelled(true);
            }
            if (!getInstance().getConfig().getBoolean("PearlMethods.Piston-Pearl") && this.isPiston(to.getBlock().getRelative(BlockFace.SELF)) && to.getBlock().getRelative(BlockFace.UP).getType().isSolid()) {
                e.setCancelled(true);
            }
            if (!getInstance().getConfig().getBoolean("PearlMethods.EndPortal-Pearl") && to.getBlock().getRelative(BlockFace.SELF).getType() == Material.ENDER_PORTAL_FRAME && to.getBlock().getRelative(BlockFace.UP).getType().isSolid()) {
                e.setCancelled(true);
            }
            if (!getInstance().getConfig().getBoolean("PearlMethods.Bed-Pearl") && (to.getBlock().getRelative(BlockFace.SELF).getType() == Material.BED_BLOCK || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.BED) && to.getBlock().getRelative(BlockFace.UP).getType().isSolid()) {
                e.setCancelled(true);
            }
            if (!getInstance().getConfig().getBoolean("PearlMethods.cPearl") && to.getBlock().getRelative(BlockFace.SELF).getType() == Material.COBBLE_WALL) {
                e.setCancelled(true);
            }
            if (to.getBlock().getRelative(BlockFace.SELF).getType() == Material.IRON_FENCE || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.NETHER_FENCE || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.ANVIL || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.THIN_GLASS || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.IRON_DOOR || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.WOOD_DOOR || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.WOODEN_DOOR || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.FENCE || to.getBlock().getRelative(BlockFace.SELF).getType() == Material.STAINED_GLASS_PANE) {
                if (this.getConfig().getBoolean("PearlRefund.PearlRefund-Command")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), this.getConfig().getString("PearlRefund.Command").replace("%player%", p.getName()));
                }
                if (this.getConfig().getBoolean("PearlRefund.PearlRefund-Message")) {
                    p.sendMessage(CC.translate(this.getConfig().getString("PearlRefund.Message")));
                }
                new BukkitRunnable() {
                    public void run() {
                        if (PurgePearls.getInstance().getConfig().getBoolean("PearlRefund.PearlRefund-Remove-Timer")) {
                            PurgePearls.getInstance().getTimer().cooldown.remove(p.getUniqueId());
                        }
                    }
                }.runTaskLater(this, 2L);
                if (this.getConfig().getBoolean("Settings.PearlRefund")) {
                    p.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
                }
                e.setCancelled(true);
            }

            if (to.getBlock().getType() != Material.BEDROCK || !onebyone.contains(p.getUniqueId())) {
                to.setX(to.getBlockX() + 0.5);
                to.setZ(to.getBlockZ() + 0.5);
                e.setTo(to);

            } else if (to.getBlock().getType() != Material.BEDROCK && onebyone.contains(p.getUniqueId())) {
                to.setX(to.getBlockX() + 0.5);
                to.setZ(to.getBlockZ() + 0.5);
                e.setTo(to);
                onebyone.remove(p.getUniqueId());
            } else if (to.getBlock().getType() == Material.BEDROCK && !onebyone.contains(p.getUniqueId())) {
                to.setX(to.getBlockX() + 0.5);
                to.setY(to.getBlockY() + 0.5);
                to.setZ(to.getBlockZ() + 0.5);
                e.setTo(to);
            } else if (to.getBlock().getType() == Material.BEDROCK && onebyone.contains(p.getUniqueId())) {
                to.setX(to.getBlockX() + 0.5);
                to.setY(to.getBlockY() + 0.5);
                to.setZ(to.getBlockZ() + 0.5);
                e.setTo(to);
                onebyone.remove(p.getUniqueId());
            }

            if (e.isCancelled()) {
                if (this.getConfig().getBoolean("PearlRefund.PearlRefund-Command")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), this.getConfig().getString("PearlRefund.Command").replace("%player%", p.getName()));
                }
                if (this.getConfig().getBoolean("PearlRefund.PearlRefund-Message")) {
                    p.sendMessage(CC.translate(this.getConfig().getString("PearlRefund.Message")));
                }
                new BukkitRunnable() {
                    public void run() {
                        if (PurgePearls.getInstance().getConfig().getBoolean("PearlRefund.PearlRefund-Remove-Timer")) {
                            PurgePearls.getInstance().getTimer().cooldown.remove(p.getUniqueId());
                        }
                    }
                }.runTaskLater(this, 2L);
                if (this.getConfig().getBoolean("Settings.PearlRefund")) {
                    p.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
                }
            }
        }
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        this.playersToLastTeleport.remove(e.getPlayer());
    }

    private Location getOneByOne(final Location location, final Player player) {
        final String direction = this.getDirectionName(player);
        Label_0353: {
            Label_0302: {
                Label_0251: {
                    final String s;
                    switch ((s = direction).hashCode()) {
                        case 69: {
                            if (!s.equals("E")) {
                                return null;
                            }
                            break Label_0353;
                        }
                        case 78: {
                            if (!s.equals("N")) {
                                return null;
                            }
                            break;
                        }
                        case 83: {
                            if (!s.equals("S")) {
                                return null;
                            }
                            break Label_0302;
                        }
                        case 87: {
                            if (!s.equals("W")) {
                                return null;
                            }
                            break Label_0251;
                        }
                        case 2487: {
                            if (!s.equals("NE")) {
                                return null;
                            }
                            break Label_0353;
                        }
                        case 2505: {
                            if (!s.equals("NW")) {
                                return null;
                            }
                            break;
                        }
                        case 2642: {
                            if (!s.equals("SE")) {
                                return null;
                            }
                            break Label_0302;
                        }
                        case 2660: {
                            if (!s.equals("SW")) {
                                return null;
                            }
                            break Label_0251;
                        }
                    }
                    final Location newLoc = new Location(player.getWorld(), location.getX(), location.getY(), location.getZ() - 1.5, player.getLocation().getYaw(), player.getLocation().getPitch());
                    return newLoc;
                }
                final Location newLoc = new Location(player.getWorld(), location.getX() - 1.5, location.getY(), location.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                return newLoc;
            }
            final Location newLoc = new Location(player.getWorld(), location.getX(), location.getY(), location.getZ() + 1.5, player.getLocation().getYaw(), player.getLocation().getPitch());
            return newLoc;
        }
        final Location newLoc = new Location(player.getWorld(), location.getX() + 1.5, location.getY(), location.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
        return newLoc;
    }

    private Location getLocTali(final Location location, final Player player) {
        final String direction = this.getDirectionName(player);
        Label_0353: {
            Label_0302: {
                Label_0251: {
                    final String s;
                    switch ((s = direction).hashCode()) {
                        case 69: {
                            if (!s.equals("E")) {
                                return null;
                            }
                            break Label_0353;
                        }
                        case 78: {
                            if (!s.equals("N")) {
                                return null;
                            }
                            break;
                        }
                        case 83: {
                            if (!s.equals("S")) {
                                return null;
                            }
                            break Label_0302;
                        }
                        case 87: {
                            if (!s.equals("W")) {
                                return null;
                            }
                            break Label_0251;
                        }
                        case 2487: {
                            if (!s.equals("NE")) {
                                return null;
                            }
                            break Label_0353;
                        }
                        case 2505: {
                            if (!s.equals("NW")) {
                                return null;
                            }
                            break;
                        }
                        case 2642: {
                            if (!s.equals("SE")) {
                                return null;
                            }
                            break Label_0302;
                        }
                        case 2660: {
                            if (!s.equals("SW")) {
                                return null;
                            }
                            break Label_0251;
                        }
                    }
                    final Location newLoc = new Location(player.getWorld(), location.getX(), location.getY(), location.getZ() - 1.5, player.getLocation().getYaw(), player.getLocation().getPitch());
                    return newLoc;
                }
                final Location newLoc = new Location(player.getWorld(), location.getX() - 1.5, location.getY(), location.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                return newLoc;
            }
            final Location newLoc = new Location(player.getWorld(), location.getX(), location.getY(), location.getZ() + 1.5, player.getLocation().getYaw(), player.getLocation().getPitch());
            return newLoc;
        }
        final Location newLoc = new Location(player.getWorld(), location.getX() + 1.5, location.getY(), location.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
        return newLoc;
    }

    private Location getLocOpenFence(final Location location, final Player player) {
        final String direction = this.getDirectionName(player);
        Label_0353: {
            Label_0302: {
                Label_0251: {
                    final String s;
                    switch ((s = direction).hashCode()) {
                        case 69: {
                            if (!s.equals("E")) {
                                return null;
                            }
                            break Label_0353;
                        }
                        case 78: {
                            if (!s.equals("N")) {
                                return null;
                            }
                            break;
                        }
                        case 83: {
                            if (!s.equals("S")) {
                                return null;
                            }
                            break Label_0302;
                        }
                        case 87: {
                            if (!s.equals("W")) {
                                return null;
                            }
                            break Label_0251;
                        }
                        case 2487: {
                            if (!s.equals("NE")) {
                                return null;
                            }
                            break Label_0353;
                        }
                        case 2505: {
                            if (!s.equals("NW")) {
                                return null;
                            }
                            break;
                        }
                        case 2642: {
                            if (!s.equals("SE")) {
                                return null;
                            }
                            break Label_0302;
                        }
                        case 2660: {
                            if (!s.equals("SW")) {
                                return null;
                            }
                            break Label_0251;
                        }
                    }
                    final Location newLoc = new Location(player.getWorld(), location.getX(), location.getY(), location.getZ() - 0.5, player.getLocation().getYaw(), player.getLocation().getPitch());
                    return newLoc;
                }
                final Location newLoc = new Location(player.getWorld(), location.getX() - 0.5, location.getY(), location.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                return newLoc;
            }
            final Location newLoc = new Location(player.getWorld(), location.getX(), location.getY(), location.getZ() + 0.5, player.getLocation().getYaw(), player.getLocation().getPitch());
            return newLoc;
        }
        final Location newLoc = new Location(player.getWorld(), location.getX() + 0.5, location.getY(), location.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
        return newLoc;
    }

    private Block getBlockTali(final Location location, final Player player) {
        final String direction = this.getDirectionName(location);
        Label_0353: {
            Label_0302: {
                Label_0251: {
                    final String s;
                    switch ((s = direction).hashCode()) {
                        case 69: {
                            if (!s.equals("E")) {
                                return null;
                            }
                            break Label_0353;
                        }
                        case 78: {
                            if (!s.equals("N")) {
                                return null;
                            }
                            break;
                        }
                        case 83: {
                            if (!s.equals("S")) {
                                return null;
                            }
                            break Label_0302;
                        }
                        case 87: {
                            if (!s.equals("W")) {
                                return null;
                            }
                            break Label_0251;
                        }
                        case 2487: {
                            if (!s.equals("NE")) {
                                return null;
                            }
                            break Label_0353;
                        }
                        case 2505: {
                            if (!s.equals("NW")) {
                                return null;
                            }
                            break;
                        }
                        case 2642: {
                            if (!s.equals("SE")) {
                                return null;
                            }
                            break Label_0302;
                        }
                        case 2660: {
                            if (!s.equals("SW")) {
                                return null;
                            }
                            break Label_0251;
                        }
                    }
                    final Location newLoc = new Location(player.getWorld(), location.getX(), location.getY(), location.getZ() - 0.7, player.getLocation().getYaw(), player.getLocation().getPitch());
                    return (newLoc == null) ? null : newLoc.getBlock();
                }
                final Location newLoc = new Location(player.getWorld(), location.getX() - 0.7, location.getY(), location.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                return (newLoc == null) ? null : newLoc.getBlock();
            }
            final Location newLoc = new Location(player.getWorld(), location.getX(), location.getY(), location.getZ() + 0.7, player.getLocation().getYaw(), player.getLocation().getPitch());
            return (newLoc == null) ? null : newLoc.getBlock();
        }
        final Location newLoc = new Location(player.getWorld(), location.getX() + 0.7, location.getY(), location.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
        return (newLoc == null) ? null : newLoc.getBlock();
    }

    private Block getBlockTali2(final Player player) {
        final String direction = this.getDirectionName(player);
        Label_0404: {
            Label_0336: {
                Label_0268: {
                    final String s;
                    switch ((s = direction).hashCode()) {
                        case 69: {
                            if (!s.equals("E")) {
                                return null;
                            }
                            break Label_0404;
                        }
                        case 78: {
                            if (!s.equals("N")) {
                                return null;
                            }
                            break;
                        }
                        case 83: {
                            if (!s.equals("S")) {
                                return null;
                            }
                            break Label_0336;
                        }
                        case 87: {
                            if (!s.equals("W")) {
                                return null;
                            }
                            break Label_0268;
                        }
                        case 2487: {
                            if (!s.equals("NE")) {
                                return null;
                            }
                            break Label_0404;
                        }
                        case 2505: {
                            if (!s.equals("NW")) {
                                return null;
                            }
                            break;
                        }
                        case 2642: {
                            if (!s.equals("SE")) {
                                return null;
                            }
                            break Label_0336;
                        }
                        case 2660: {
                            if (!s.equals("SW")) {
                                return null;
                            }
                            break Label_0268;
                        }
                    }
                    final Location newLoc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 1.0, player.getLocation().getZ() - 0.7, player.getLocation().getYaw(), player.getLocation().getPitch());
                    return (newLoc == null) ? null : newLoc.getBlock();
                }
                final Location newLoc = new Location(player.getWorld(), player.getLocation().getX() - 0.7, player.getLocation().getY() + 1.0, player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                return (newLoc == null) ? null : newLoc.getBlock();
            }
            final Location newLoc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 1.0, player.getLocation().getZ() + 0.7, player.getLocation().getYaw(), player.getLocation().getPitch());
            return (newLoc == null) ? null : newLoc.getBlock();
        }
        final Location newLoc = new Location(player.getWorld(), player.getLocation().getX() + 0.7, player.getLocation().getY() + 1.0, player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
        return (newLoc == null) ? null : newLoc.getBlock();
    }

    private Block getBlockTali3(final Player player) {
        final String direction = this.getDirectionName(player);
        Label_0404: {
            Label_0336: {
                Label_0268: {
                    final String s;
                    switch ((s = direction).hashCode()) {
                        case 69: {
                            if (!s.equals("E")) {
                                return null;
                            }
                            break Label_0404;
                        }
                        case 78: {
                            if (!s.equals("N")) {
                                return null;
                            }
                            break;
                        }
                        case 83: {
                            if (!s.equals("S")) {
                                return null;
                            }
                            break Label_0336;
                        }
                        case 87: {
                            if (!s.equals("W")) {
                                return null;
                            }
                            break Label_0268;
                        }
                        case 2487: {
                            if (!s.equals("NE")) {
                                return null;
                            }
                            break Label_0404;
                        }
                        case 2505: {
                            if (!s.equals("NW")) {
                                return null;
                            }
                            break;
                        }
                        case 2642: {
                            if (!s.equals("SE")) {
                                return null;
                            }
                            break Label_0336;
                        }
                        case 2660: {
                            if (!s.equals("SW")) {
                                return null;
                            }
                            break Label_0268;
                        }
                    }
                    final Location newLoc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ() - 1.5, player.getLocation().getYaw(), player.getLocation().getPitch());
                    return (newLoc == null) ? null : newLoc.getBlock();
                }
                final Location newLoc = new Location(player.getWorld(), player.getLocation().getX() - 1.5, player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                return (newLoc == null) ? null : newLoc.getBlock();
            }
            final Location newLoc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ() + 1.5, player.getLocation().getYaw(), player.getLocation().getPitch());
            return (newLoc == null) ? null : newLoc.getBlock();
        }
        final Location newLoc = new Location(player.getWorld(), player.getLocation().getX() + 1.5, player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
        return (newLoc == null) ? null : newLoc.getBlock();
    }

    private Block getBlockTali(final Player player) {
        final String direction = this.getDirectionName(player);
        Label_0398: {
            Label_0332: {
                Label_0266: {
                    final String s;
                    switch ((s = direction).hashCode()) {
                        case 69: {
                            if (!s.equals("E")) {
                                return null;
                            }
                            break Label_0398;
                        }
                        case 78: {
                            if (!s.equals("N")) {
                                return null;
                            }
                            break;
                        }
                        case 83: {
                            if (!s.equals("S")) {
                                return null;
                            }
                            break Label_0332;
                        }
                        case 87: {
                            if (!s.equals("W")) {
                                return null;
                            }
                            break Label_0266;
                        }
                        case 2487: {
                            if (!s.equals("NE")) {
                                return null;
                            }
                            break Label_0398;
                        }
                        case 2505: {
                            if (!s.equals("NW")) {
                                return null;
                            }
                            break;
                        }
                        case 2642: {
                            if (!s.equals("SE")) {
                                return null;
                            }
                            break Label_0332;
                        }
                        case 2660: {
                            if (!s.equals("SW")) {
                                return null;
                            }
                            break Label_0266;
                        }
                    }
                    final Location newLoc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ() - 0.7, player.getLocation().getYaw(), player.getLocation().getPitch());
                    return (newLoc == null) ? null : newLoc.getBlock();
                }
                final Location newLoc = new Location(player.getWorld(), player.getLocation().getX() - 0.7, player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                return (newLoc == null) ? null : newLoc.getBlock();
            }
            final Location newLoc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ() + 0.7, player.getLocation().getYaw(), player.getLocation().getPitch());
            return (newLoc == null) ? null : newLoc.getBlock();
        }
        final Location newLoc = new Location(player.getWorld(), player.getLocation().getX() + 0.7, player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
        return (newLoc == null) ? null : newLoc.getBlock();
    }

    private String getDirectionName(final Location location) {
        double rotation = (location.getYaw() - 90.0f) % 360.0f;
        if (rotation < 0.0) {
            rotation += 360.0;
        }
        if (0.0 <= rotation && rotation < 22.5) {
            return "W";
        }
        if (22.5 <= rotation && rotation < 67.5) {
            return "NW";
        }
        if (67.5 <= rotation && rotation < 112.5) {
            return "N";
        }
        if (112.5 <= rotation && rotation < 157.5) {
            return "NE";
        }
        if (157.5 <= rotation && rotation < 202.5) {
            return "E";
        }
        if (202.5 <= rotation && rotation < 247.5) {
            return "SE";
        }
        if (247.5 <= rotation && rotation < 292.5) {
            return "S";
        }
        if (292.5 <= rotation && rotation < 337.5) {
            return "SW";
        }
        if (337.5 <= rotation && rotation < 360.0) {
            return "W";
        }
        return null;
    }

    private String getDirectionName(final Player player) {
        double rotation = (player.getLocation().getYaw() - 90.0f) % 360.0f;
        if (rotation < 0.0) {
            rotation += 360.0;
        }
        if (0.0 <= rotation && rotation < 22.5) {
            return "W";
        }
        if (22.5 <= rotation && rotation < 67.5) {
            return "NW";
        }
        if (67.5 <= rotation && rotation < 112.5) {
            return "N";
        }
        if (112.5 <= rotation && rotation < 157.5) {
            return "NE";
        }
        if (157.5 <= rotation && rotation < 202.5) {
            return "E";
        }
        if (202.5 <= rotation && rotation < 247.5) {
            return "SE";
        }
        if (247.5 <= rotation && rotation < 292.5) {
            return "S";
        }
        if (292.5 <= rotation && rotation < 337.5) {
            return "SW";
        }
        if (337.5 <= rotation && rotation < 360.0) {
            return "W";
        }
        return null;
    }

    @SuppressWarnings("unused")
    private String getDirectionCompleteName(final Block block) {
        double rotation = (block.getLocation().getYaw() - 90.0f) % 360.0f;
        if (rotation < 0.0) {
            rotation += 360.0;
        }
        if (0.0 <= rotation && rotation < 22.5) {
            return "WEST";
        }
        if (22.5 <= rotation && rotation < 67.5) {
            return "NORTH_WEST";
        }
        if (67.5 <= rotation && rotation < 112.5) {
            return "NORTH";
        }
        if (112.5 <= rotation && rotation < 157.5) {
            return "NORTH_EAST";
        }
        if (157.5 <= rotation && rotation < 202.5) {
            return "EAST";
        }
        if (202.5 <= rotation && rotation < 247.5) {
            return "SOUTH_EAST";
        }
        if (247.5 <= rotation && rotation < 292.5) {
            return "SOUTH";
        }
        if (292.5 <= rotation && rotation < 337.5) {
            return "SOUTH_WEST";
        }
        if (337.5 <= rotation && rotation < 360.0) {
            return "WEST";
        }
        return null;
    }

    @SuppressWarnings("unused")
    private String getBackDirectionCompleteName(final Player player) {
        double rotation = (player.getLocation().getYaw() - 90.0f) % 360.0f;
        if (rotation < 0.0) {
            rotation += 360.0;
        }
        if (0.0 <= rotation && rotation < 22.5) {
            return "EAST";
        }
        if (22.5 <= rotation && rotation < 67.5) {
            return "SOUTH_WEST";
        }
        if (67.5 <= rotation && rotation < 112.5) {
            return "SOUTH";
        }
        if (112.5 <= rotation && rotation < 157.5) {
            return "SOUTH_EAST";
        }
        if (157.5 <= rotation && rotation < 202.5) {
            return "WEST";
        }
        if (202.5 <= rotation && rotation < 247.5) {
            return "NORTH_EAST";
        }
        if (247.5 <= rotation && rotation < 292.5) {
            return "NORTH";
        }
        if (292.5 <= rotation && rotation < 337.5) {
            return "NORTH_WEST";
        }
        if (337.5 <= rotation && rotation < 360.0) {
            return "EAST";
        }
        return null;
    }

    private String getDirectionCompleteName(final Player player) {
        double rotation = (player.getLocation().getYaw() - 90.0f) % 360.0f;
        if (rotation < 0.0) {
            rotation += 360.0;
        }
        if (0.0 <= rotation && rotation < 22.5) {
            return "WEST";
        }
        if (22.5 <= rotation && rotation < 67.5) {
            return "NORTH_WEST";
        }
        if (67.5 <= rotation && rotation < 112.5) {
            return "NORTH";
        }
        if (112.5 <= rotation && rotation < 157.5) {
            return "NORTH_EAST";
        }
        if (157.5 <= rotation && rotation < 202.5) {
            return "EAST";
        }
        if (202.5 <= rotation && rotation < 247.5) {
            return "SOUTH_EAST";
        }
        if (247.5 <= rotation && rotation < 292.5) {
            return "SOUTH";
        }
        if (292.5 <= rotation && rotation < 337.5) {
            return "SOUTH_WEST";
        }
        if (337.5 <= rotation && rotation < 360.0) {
            return "WEST";
        }
        return null;
    }

    public static PurgePearls getInstance() {
        return PurgePearls.instance;
    }

    public ConfigFile getconfigfile() {
        return this.configfile;
    }

    public Timer getTimer() {
        return this.timer;
    }
}

