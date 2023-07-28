package dev.spaxter.curseguard;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.spaxter.curseguard.command.AddWordTest;
import dev.spaxter.curseguard.core.Language;
import dev.spaxter.curseguard.storage.Database;
import org.bukkit.permissions.PermissionAttachment;
import org.junit.jupiter.api.*;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class CurseGuardTests {
    public static PermissionAttachment perms;
    public static ServerMock server;
    public static CurseGuard plugin;

    @BeforeEach
    public void setUp() {
        // Start mock server
        server = MockBukkit.mock();
        plugin = MockBukkit.load(CurseGuard.class);
        plugin.onEnable();
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void testCommands() {
        PlayerMock player = server.addPlayer();
        player.addAttachment(plugin);
        PlayerMock adminPlayer = server.addPlayer();
        perms = adminPlayer.addAttachment(plugin);

        AddWordTest addWordTest = new AddWordTest(player, adminPlayer);
        addWordTest.permissionChecks();
        addWordTest.parameterChecks();
    }

    public static String prependPrefix(final String string) {
        return Language.Prefix.NOTIFICATION_PREFIX + string;
    }
}
