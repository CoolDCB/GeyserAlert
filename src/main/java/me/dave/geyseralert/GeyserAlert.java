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
        this.logger().info("Event triggered successfully");

        Command command = Command.builder(this)
            .name("alert")
//            .bedrockOnly(true)
            .source(CommandSource.class)
            .aliases(List.of("alert"))
            .description("Send an alert to all online Bedrock players")
            .executableOnConsole(true)
            .suggestedOpOnly(true)
            .permission("geyseralert.alert")
            .executor((source, cmd, args) -> {
                if (args.length < 1) {
                    source.sendMessage("Make sure to include the message you want to send");
                    return;
                }

                Geyser.api().onlineConnections().forEach(connection -> {
                    if (connection instanceof GeyserConnection geyserConnection) {
                        geyserConnection.sendMessage(String.join(" ", args).replace("&", "§"));
                    }
                });
            })
            .build();

        event.register(command);
    }

}
