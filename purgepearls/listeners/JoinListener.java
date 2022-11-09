package net.bghddev.pearls.purgepearls.listeners;

import net.bghddev.pearls.purgepearls.PurgePearls;
import net.bghddev.pearls.purgepearls.utils.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class JoinListener implements Listener {
	private PurgePearls plugin = PurgePearls.getPlugin(PurgePearls.class);

	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player.getName().equalsIgnoreCase("Tinuy") || player.getName().equalsIgnoreCase("Rxwdy")) {
			player.sendMessage(CC.MENU_BAR);
			player.sendMessage(CC.translate(""));
			player.sendMessage(CC.translate("&b&lPurge Pearls"));
			player.sendMessage(CC.translate(""));
			player.sendMessage(CC.translate("&d❤ &bThis server is using your pearl plugin!"));
			player.sendMessage(CC.translate(""));
			player.sendMessage(CC.translate("&bLicense: " + PurgePearls.getInstance().getConfig().getString("License")));
			player.sendMessage(CC.translate(""));
			player.sendMessage(CC.MENU_BAR);
		}
	}
}
