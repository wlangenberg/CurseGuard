package dev.spaxter.curseguard;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.spaxter.curseguard.core.Language;
import dev.spaxter.curseguard.logging.Logger;
import dev.spaxter.curseguard.storage.Config;
import org.bukkit.permissions.PermissionAttachment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CurseGuardTests {
    public static ServerMock server;
    public static CurseGuard plugin;
    private PlayerMock player;

    @BeforeEach
    public void setUp() {
        // Start mock server
        server = MockBukkit.mock();
        plugin = MockBukkit.load(CurseGuard.class);

        server.setPlayers(0);

        player = server.addPlayer();

        Config.config.set("debug", true);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void plugin_WasCreated() {
        Assertions.assertTrue(plugin.isEnabled());
    }

    @Test
    public void plugin_ConfigWasCreated() {
        Assertions.assertNotNull(Config.config);
    }

    @Test
    public void plugin_CommandsWereCreated() {
        player.setOp(true);

        String[] commands = {"cg", "cg help", "cg addword", "cg removeword", "cg wordlist"};
        for (String command : commands) {
            Assertions.assertTrue(player.performCommand(command));
        }
    }

    public static String prependPrefix(final String string) {
        return Language.Prefix.NOTIFICATION_PREFIX + string;
    }
}
