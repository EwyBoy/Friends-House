package com.ewyboy;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class FriendsHousePluginTest {
    public static void main(String[] args) throws Exception {
        ExternalPluginManager.loadBuiltin(FriendsHousePlugin.class);
        RuneLite.main(args);
    }

    @Test
    public void testPluginInitialization() {
        FriendsHousePlugin plugin = new FriendsHousePlugin();
        assertNotNull("Plugin should not be null", plugin);
    }

    @Test
    public void testPluginName() {
        FriendsHousePlugin plugin = new FriendsHousePlugin();
        assertNotNull("Plugin name should not be null", plugin.getName());
    }

}