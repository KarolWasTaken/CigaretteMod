package net.karoll.cigarettemod;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class DebugCommands extends CommandBase {

    @Override
    public String getCommandName() {
        return "setvar";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/setvar <true/false>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            try {
                // Parse the argument to an integer
                if (!sender.getEntityWorld().isRemote) {
                    EntityPlayer player = sender.getEntityWorld()
                        .getClosestPlayer(
                            sender.getPlayerCoordinates().posX,
                            sender.getPlayerCoordinates().posY,
                            sender.getPlayerCoordinates().posZ,
                            1);
                    player.openGui(
                        CigaretteMod.instance,
                        420,
                        sender.getEntityWorld(),
                        (int) player.posX,
                        (int) player.posY,
                        (int) player.posZ);
                }

                // Send a confirmation message to the player
                // sender.addChatMessage(new ChatComponentText("offsets set to: " + y1 + " and " + y2));
            } catch (NumberFormatException e) {
                // Handle the case where the argument is not a valid integer
                sender.addChatMessage(new ChatComponentText("Invalid number format. Please enter a valid integer."));
            }
        } else {
            // Send usage message if the number of arguments is incorrect
            sender.addChatMessage(new ChatComponentText("Usage: " + getCommandUsage(sender)));
        }
    }
}
