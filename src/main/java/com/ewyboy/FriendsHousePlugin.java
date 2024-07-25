package com.ewyboy;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.VarClientInt;
import net.runelite.api.events.VarClientIntChanged;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@PluginDescriptor(
        name = "Friend's House",
        description = "Quickly enter your friend's house with a single click",
        tags = {"friend", "friends", "house", "poh", "portal", "name"}
)
public class FriendsHousePlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private ClientThread clientThread;

    @Inject
    private FriendsHouseConfig config;

    @Override
    protected void startUp() throws Exception {
    }

    @Override
    protected void shutDown() throws Exception {
    }

    @Subscribe
    public void onVarClientIntChanged(VarClientIntChanged event) {
        if (config.name() == null || config.name().isEmpty()) {
            return;
        }

        if (event.getIndex() != VarClientInt.INPUT_TYPE) {
            return;
        }

        clientThread.invokeLater(() -> {
            var chatboxContainer = client.getWidget(ComponentID.CHATBOX_CONTAINER);
            if (chatboxContainer == null) {
                return;
            }

            var chatboxTitleWidget = client.getWidget(ComponentID.CHATBOX_TITLE);
            if (chatboxTitleWidget == null) {
                return;
            }

            String title = chatboxTitleWidget.getText();
            if (title == null || !title.toLowerCase().contains("enter name:")) {
                return;
            }

            var children = chatboxContainer.getChildren();

            boolean lastNameFound = false;

            if (children != null) {
                lastNameFound = Arrays.stream(children)
                        .filter(Objects::nonNull)
                        .map(Widget::getText)
                        .filter(Objects::nonNull)
                        .map(String::toLowerCase)
                        .anyMatch(text -> text.contains("last name:"));
            }

            var friendsHouse = new FriendsHouse(chatboxContainer, client, lastNameFound);
            friendsHouse.setInputText(config.name());
        });
    }

    @Provides
    FriendsHouseConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(FriendsHouseConfig.class);
    }
}
