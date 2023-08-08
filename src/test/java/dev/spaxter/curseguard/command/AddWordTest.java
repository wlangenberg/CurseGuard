package dev.spaxter.curseguard.command;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.spaxter.curseguard.CurseGuard;
import dev.spaxter.curseguard.CurseGuardTests;
import dev.spaxter.curseguard.core.Language;
import dev.spaxter.curseguard.logging.Logger;
import dev.spaxter.curseguard.models.Action;
import dev.spaxter.curseguard.storage.GlobalMemory;
import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionAttachment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class AddWordTest {

    private ServerMock server;
    private CurseGuard plugin;
    private PlayerMock player;

    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(CurseGuard.class);

        server.setPlayers(0);

        player = server.addPlayer();
        player.setOp(false);
        PermissionAttachment permissions = player.addAttachment(plugin);
        permissions.getPermissions().forEach((permission, value) -> {
            permissions.setPermission(permission, false);
        });
    }

    @AfterEach
    public void tearDown() {
        Bukkit.getScheduler().cancelTasks(plugin);
        MockBukkit.unmock();
    }

    @Test
    public void addWordCommand_MissingPermissions() {
        player.performCommand("cg addword test_noperms censor");
        String expected = Language.Notification.PERMISSION_DENIED;
        String actual = player.nextMessage();

        Assertions.assertEquals(expected, actual);
        Assertions.assertNull(GlobalMemory.wordActions.get("test_noperms"));
    }

    @Test
    public void addWordCommand_WithPermissions() {
        PermissionAttachment permissions = player.addAttachment(plugin);
        permissions.setPermission("curseguard.words.add", true);

        player.performCommand("cg addword test censor");
        String expected = CurseGuardTests.prependPrefix(Language.Notification.ADD_WORD_SUCCESS.replace("%word%", "test"));
        String actual = player.nextMessage();

        Assertions.assertEquals(expected, actual);
        Assertions.assertNotNull(GlobalMemory.wordActions.get("test"));
    }

    @Test
    public void addWordCommand_Censor_ValidParameters() {
        PermissionAttachment permissions = player.addAttachment(plugin);
        permissions.setPermission("curseguard.words.add", true);

        player.performCommand("cg addword banana censor");
        String expected = CurseGuardTests.prependPrefix(Language.Notification.ADD_WORD_SUCCESS.replace("%word%", "banana"));
        String actual = player.nextMessage();

        Assertions.assertEquals(expected, actual);
        Assertions.assertNotNull(GlobalMemory.wordActions.get("banana"));
        Assertions.assertEquals(Action.CENSOR, GlobalMemory.wordActions.get("banana"));
    }

    @Test
    public void addWordCommand_Block_ValidParameters() {
        PermissionAttachment permissions = player.addAttachment(plugin);
        permissions.setPermission("curseguard.words.add", true);

        player.performCommand("cg addword apple block");
        String expected = CurseGuardTests.prependPrefix(Language.Notification.ADD_WORD_SUCCESS.replace("%word%", "apple"));
        String actual = player.nextMessage();

        Assertions.assertEquals(expected, actual);
        Assertions.assertNotNull(GlobalMemory.wordActions.get("apple"));
        Assertions.assertEquals(Action.BLOCK, GlobalMemory.wordActions.get("apple"));
    }

    @Test
    public void addWordCommand_InvalidParameter_Action() {
        PermissionAttachment permissions = player.addAttachment(plugin);
        permissions.setPermission("curseguard.words.add", true);

        player.performCommand("cg addword pineapple invalid_action");
        String expected = CurseGuardTests.prependPrefix(Language.Notification.ADD_WORD_INVALID_ACTION);
        String actual = player.nextMessage();

        Assertions.assertEquals(expected, actual);
        Assertions.assertNull(GlobalMemory.wordActions.get("pineapple"));
    }

    @Test
    public void addWordCommand_MissingParameters() {
        PermissionAttachment permissions = player.addAttachment(plugin);
        permissions.setPermission("curseguard.words.add", true);

        player.setOp(true);
        boolean result = player.performCommand("cg addword");

        Assertions.assertFalse(result);
    }

    @Test
    public void addWordCommand_ExistingWord() {
        PermissionAttachment permissions = player.addAttachment(plugin);
        permissions.setPermission("curseguard.words.add", true);

        player.performCommand("cg addword cucumber censor");
        player.nextMessage();
        player.performCommand("cg addword cucumber censor");

        String actual = player.nextMessage();
        String expected = CurseGuardTests.prependPrefix(Language.Notification.ADD_WORD_WORD_EXISTS.replace("%word%", "cucumber"));
        Assertions.assertEquals(expected, actual);
    }
}
