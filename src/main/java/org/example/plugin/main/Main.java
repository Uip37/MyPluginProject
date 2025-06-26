package org.example.plugin.main;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import jdk.dynalink.StandardOperation;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

  private int count = 0;

  @Override
  public void onEnable() {
    Bukkit.getPluginManager().registerEvents(this, this);
  }

  /**
   * プレイヤーがスニークを開始/終了する際に起動されるイベントハンドラ。
   *
   * @param e イベント
   */
  @EventHandler
  public void onPlayerToggleSneak(PlayerToggleSneakEvent e) throws IOException {
    if (!e.isSneaking()) {
      return; // スニーク開始時だけ処理する
    }

    // イベント発生時のプレイヤーやワールドなどの情報を変数に持つ。
    Player player = e.getPlayer();
    World world = player.getWorld();

    count++;

    // BigInteger型の val を定義
    BigInteger val = new BigInteger(String.valueOf(count));
    // BigInteger側の val に対してnextProbablePrimeメソッドを使用
    System.out.println("count: " + count + ", isPrime: " + val.isProbablePrime(10));

    // val が素数であるかの判定 isProbablePrimeメソッドを使用
    if (val.isProbablePrime(10)) {
      System.out.println(val + "is a prime number. ");

      List<Color> colorList = List.of(Color.RED, Color.BLUE, Color.WHITE, Color.BLACK);

      for (Color color : colorList) {
        // 花火オブジェクトをプレイヤーのロケーション地点に対して出現させる。
        Firework firework = world.spawn(player.getLocation(), Firework.class);
        // 花火オブジェクトが持つメタ情報を取得。
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        // メタ情報に対して設定を追加したり、値の上書きを行う。

        fireworkMeta.addEffect(
            FireworkEffect.builder()
                .withColor(color)
                .with(Type.BALL_LARGE)
                .withFlicker()
                .build());
        fireworkMeta.setPower((2 * 3) - 4);
        // 追加した情報で再設定する。
        firework.setFireworkMeta(fireworkMeta);
      }

      Path path = Path.of("firework.txt");
      Files.writeString(path, "たーまやー");
      player.sendMessage(Files.readString(path));

    } else {
      System.out.println(val + " is not a prime number.");

    }
  }

  @EventHandler
  public void onPlayerBedEnter(PlayerBedEnterEvent e) {
    Player player = e.getPlayer();
    ItemStack[] itemStacks = player.getInventory().getContents();
    for (ItemStack item : itemStacks) {
      if (item != null && item.getMaxStackSize() == 64 && item.getAmount() < 64) {
        item.setAmount(30);
      }
    }
    player.getInventory().setContents(itemStacks);
  }
}

