package dev.spaxter.curseguard.command;

import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.spaxter.curseguard.CurseGuardTests;
import dev.spaxter.curseguard.core.Language;
import dev.spaxter.curseguard.logging.Logger;
import org.bukkit.permissions.PermissionAttachment;
import org.junit.jupiter.api.Assertions;

import java.util.Objects;

public class AddWordTest {

    private final PlayerMock player;
    private final PlayerMock adminPlayer;
    public AddWordTest(PlayerMock player, PlayerMock adminPlayer) {
        this.player = player;
        this.adminPlayer = adminPlayer;
        CurseGuardTests.perms.setPermission("curseguard.words.add", true);
    }

    public void permissionChecks() {
        Logger.log("Running addword test with insufficient permissions");
        player.performCommand("cg addword test");
        Assertions.assertEquals(Language.Notification.PERMISSION_DENIED, player.nextMessage());

        Logger.log("Running addword test with sufficient permissions");
        adminPlayer.performCommand("cg addword test BLOCK");
        Assertions.assertEquals(CurseGuardTests.prependPrefix(Language.Notification.ADD_WORD_SUCCESS.replace("%word%", "test")), adminPlayer.nextMessage());
    }

    public void parameterChecks() {
        boolean result;

        Logger.log("Running addword test with correct parameter: censor");
        result = adminPlayer.performCommand("cg addword banana censor");
        Assertions.assertEquals(CurseGuardTests.prependPrefix(Language.Notification.ADD_WORD_SUCCESS.replace("%word%", "banana")), adminPlayer.nextMessage());
        Assertions.assertTrue(result);

        Logger.log("Running addword test with correct parameter: block");
        result = adminPlayer.performCommand("cg addword apple block");
        Assertions.assertEquals(CurseGuardTests.prependPrefix(Language.Notification.ADD_WORD_SUCCESS.replace("%word%", "apple")), adminPlayer.nextMessage());
        Assertions.assertTrue(result);

        Logger.log("Running addword test without any parameters");
        result = adminPlayer.performCommand("cg addword");
        Assertions.assertFalse(result);

        Logger.log("Running addword test with missing action parameter");
        result = adminPlayer.performCommand("cg addword pineapple");
        Assertions.assertFalse(result);
    }

}
