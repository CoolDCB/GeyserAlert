package me.dave.geyseralert;

import org.geysermc.api.Geyser;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.command.Command;
import org.geysermc.geyser.api.command.CommandSource;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCommandsEvent;
import org.geysermc.geyser.api.extension.Extension;

import java.util.List;

public class GeyserAlert implements Extension {

    @Subscribe
    public void onDefineCommands(GeyserDefineCommandsEvent event) {
        Command command = Command.builder(this)
            .name("alert")
            .source(CommandSource.class)
            .aliases(List.of("alert"))
            .description("Send an alert to all online Bedrock players")
            .executableOnConsole(true)
            .suggestedOpOnly(true)
            .permission("geyseralert.alert")
            .executor((source, cmd, args) -> {
                if (!source.hasPermission("geyseralert.alert")) {
                    source.sendMessage("You don't have permission for this command");
                    return;
                }

                if (args.length < 1) {
                    source.sendMessage("Make sure to include the message you want to send");
                    return;
                }

                String alert = String.join(" ", args).replace("&", "§");

                Geyser.api().onlineConnections().forEach(connection -> {
                    if (connection instanceof GeyserConnection geyserConnection) {
                        geyserConnection.sendMessage(alert);
                    }
                });

                this.logger().info("Sent alert: '" + alert + "'");
            })
            .build();

        event.register(command);
    }

}
