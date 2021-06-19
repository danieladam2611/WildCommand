package de.daniel.Command;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;
import de.daniel.WildMain;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class WildCommand extends Command {

    Config cfg = new Config(new File(WildMain.getInstance().getDataFolder(), "config.yml"));
    private Vector3 temporalVector = new Vector3();

    public WildCommand() {
        super("wild", "teleport yourself to a random location", "/wild");
    }

    public Position WildPosition(Position pos) {

        int wide = cfg.getInt("wide");

        int i = 0;

        while (i <= 15) {

            int x = wide - (new Random().nextInt(wide) * 2);
            int z = wide - (new Random().nextInt(wide) * 2);

            x = (int) (x + pos.getX());
            z = (int) (z + pos.getZ());

            if (!pos.level.getGenerator().getName().equals("nether")) {
                for (int y = 127; y >= 1; y--) {
                    if (pos.level.getBlock(this.temporalVector.setComponents(x, y, z)).isSolid() && pos.level.getBlock(this.temporalVector.setComponents(x, y, z)).isNormalBlock()) {
                        return new Position(x + 0.5D, pos.level.getBlock(this.temporalVector.setComponents(x, y, z)).getBoundingBox().getMaxY(), z + 0.5D, pos.level);
                    }
                }
            } else {
                for (int y = 1; y <= 126; y++) {
                    if (pos.level.getBlock(this.temporalVector.setComponents(x, y, z)).isSolid()) {

                        Position posNether = new Position(x + 0.5D, y + 1, z + 0.5D, pos.level);
                        Block block = pos.level.getBlock(posNether);

                        if (block.getId() != Block.get(BlockID.STILL_LAVA).getId() &&
                                block.getId() != Block.get(BlockID.LAVA).getId() &&
                                !block.isSolid()) {

                            return posNether;
                        }
                    }
                }
            }
            i++;
        }
        return null;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        String prefix = cfg.getString("prefix");

        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + "Only Ingame!");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("command.wild")) {
            player.sendMessage(prefix + cfg.getString("messages.nopermission"));
            return false;
        }

        if (args.length != 0) {
            player.sendMessage(prefix + "§cUsage§8: §b" + usageMessage);
            return false;
        }

        ArrayList<String> list = new ArrayList<>();
        String[] arg = cfg.getString("worlds").replace("[", "").replace("]", "").split(",");

        for (int i = 0; i < arg.length; i++) {

            String worlds = arg[i];

            if (worlds.startsWith(" ")) {
                worlds = worlds.substring(1);
            }

            list.add(worlds);
        }

        if (!list.contains(player.level.getName())) {
            player.sendMessage(prefix + cfg.getString("messages.worlddisabled"));
            return false;
        }

        Position pos = WildPosition(player);

        if (pos == null) {
            player.sendMessage(prefix + cfg.getString("messages.failedteleport"));
            return false;
        }

        player.teleport(pos);
        player.sendMessage(prefix + cfg.getString("messages.teleport"));

        return false;
    }
}