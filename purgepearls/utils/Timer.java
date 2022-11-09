package net.bghddev.pearls.purgepearls.utils;

import net.bghddev.pearls.purgepearls.PurgePearls;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class Timer extends Handler implements Listener
{
	public HashMap<UUID, Long> cooldown;

	public Timer(final PurgePearls plugin) {
		super(plugin);
		this.cooldown = new HashMap<>();
	}

	public void enable() {
		PurgePearls.getInstance().getServer().getPluginManager().registerEvents((Listener)this, (Plugin)PurgePearls.getInstance());
	}

	public void disable() {
		this.cooldown.clear();
	}

	public boolean isActive(final Player player) {
		return this.cooldown.containsKey(player.getUniqueId()) && System.currentTimeMillis() < this.cooldown.get(player.getUniqueId());
	}

	public void quit(final Player player) {
		if (this.cooldown.containsKey(player.getUniqueId()) && this.isActive(player)) {
			this.cooldown.remove(player.getUniqueId());
		}
	}

	public long getMillisecondsLeft(final Player player) {
		if (this.cooldown.containsKey(player.getUniqueId())) {
			return Math.max(this.cooldown.get(player.getUniqueId()) - System.currentTimeMillis(), 0L);
		}
		return 0L;
	}

	public HashMap<UUID, Long> getActiveCooldowns() {
		return this.cooldown;
	}

	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		final Action action = event.getAction();
		if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
			return;
		}
		if (event.hasItem() && (action.equals((Object)Action.RIGHT_CLICK_AIR) || action.equals((Object)Action.RIGHT_CLICK_BLOCK)) && event.getItem().getType().equals((Object)Material.ENDER_PEARL)) {
			if (this.isActive(player)) {
				event.setUseItemInHand(Event.Result.DENY);
				if (PurgePearls.getInstance().getConfig().getBoolean("Timer.Send-Message")) {
					player.sendMessage(CC.translate(PurgePearls.getInstance().getConfig().getString("Timer.Message").replace("%time%", Utils.getRemaining(this.getMillisecondsLeft(player), true))));
				}
			}
			else if (event.getItem().getType() == Material.ENDER_PEARL && event.getItem().getItemMeta().getDisplayName() != null && event.getItem().getItemMeta().getDisplayName().equals(CC.translate(PurgePearls.getInstance().getConfig().getString("fast-pearl-name")))) {
				new BukkitRunnable() {
					public void run() {
						PurgePearls.getInstance().getTimer().cooldown.put(player.getUniqueId(), System.currentTimeMillis() + PurgePearls.getInstance().getConfig().getInt("fast-pearl-cooldown") * 1000);
					}
				}.runTaskLater((Plugin)PurgePearls.getInstance(), 1L);
			}
			else if (event.getItem().getType() == Material.ENDER_PEARL) {
				new BukkitRunnable() {
					public void run() {
						PurgePearls.getInstance().getTimer().cooldown.put(player.getUniqueId(), System.currentTimeMillis() + PurgePearls.getInstance().getConfig().getInt("Timer.Time") * 1000);
					}
				}.runTaskLater((Plugin)PurgePearls.getInstance(), 1L);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void SensiblePearls(PlayerInteractEvent event) {
		if (!event.hasBlock()) {
			return;
		}
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		Action action = event.getAction();

		if(player.getGameMode().equals(GameMode.CREATIVE)) return;

		if(PurgePearls.getInstance().getConfig().getBoolean("Settings.Spigot-1-7")
				&& !player.getGameMode().equals(GameMode.CREATIVE)
				&& !PurgePearls.getInstance().getTimer().isActive(player)
				&& action == Action.RIGHT_CLICK_BLOCK
				&& block.getType().isSolid()
				&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL) {
			//ItemStack old = new ItemStack(event.getPlayer().getItemInHand().getTypeId(), event.getPlayer().getItemInHand().getAmount() - 1);
			//event.getPlayer().setItemInHand(old);
			if (player.getItemInHand().getAmount() > 1) {
				player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
			}
			else {
				player.setItemInHand(new ItemStack(Material.AIR, 1));
			}
			player.launchProjectile(EnderPearl.class);
			player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 0.5f, 0.4f);
			event.setCancelled(true);
		}
		if(!player.getGameMode().equals(GameMode.CREATIVE)
				&& !PurgePearls.getInstance().getTimer().isActive(player)
				&& action == Action.RIGHT_CLICK_BLOCK
				&& block.getType() == Material.DISPENSER
				&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.STONE_BUTTON
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.WOOD_BUTTON
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.LEVER
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.FIRE
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.TRAP_DOOR
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.WOOD_DOOR
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.WOODEN_DOOR
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.BREWING_STAND
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.HOPPER
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.DROPPER
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.IRON_DOOR_BLOCK
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.FURNACE
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.BURNING_FURNACE
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.ANVIL
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.BEACON
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.ENCHANTMENT_TABLE
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.BED
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.BED_BLOCK
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.CHEST
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.ENDER_CHEST
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.TRAPPED_CHEST
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.WORKBENCH
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL
				||
				!player.getGameMode().equals(GameMode.CREATIVE)
						&& !PurgePearls.getInstance().getTimer().isActive(player)
						&& action == Action.RIGHT_CLICK_BLOCK
						&& block.getType() == Material.FENCE_GATE
						&& player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL) {
			//ItemStack old = new ItemStack(event.getPlayer().getItemInHand().getTypeId(), event.getPlayer().getItemInHand().getAmount() - 1);
			//event.getPlayer().setItemInHand(old);
			if (player.getItemInHand().getAmount() > 1) {
				player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
			}
			else {
				player.getItemInHand().setType(Material.AIR);
			}
			player.launchProjectile(EnderPearl.class);
			player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 0.5f, 0.4f);
			//event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDeath(final PlayerDeathEvent event) {
		final Player player = event.getEntity();
		if (this.cooldown.containsKey(player.getUniqueId())) {
			this.cooldown.remove(player.getUniqueId());
		}
	}

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		if (this.cooldown.containsKey(player.getUniqueId()) && !this.isActive(player)) {
			this.cooldown.remove(player.getUniqueId());
		}
	}
}