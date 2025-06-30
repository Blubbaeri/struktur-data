package snakegame;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.*;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import java.awt.*;

public class IntroPlayer {

    private final EmbeddedMediaPlayer mediaPlayer;

    public IntroPlayer(String videoPath, Runnable onFinish) {
        JFrame frame = new JFrame();
        Canvas canvas = new Canvas();
        canvas.setBackground(Color.black);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(canvas, BorderLayout.CENTER);
        frame.setContentPane(panel);
        frame.setUndecorated(true);
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        MediaPlayerFactory factory = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        mediaPlayer.videoSurface().set(factory.videoSurfaces().newVideoSurface(canvas));

        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
                frame.dispose();
                onFinish.run();
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                System.err.println("Gagal mainkan video");
                frame.dispose();
                onFinish.run();
            }
        });

        mediaPlayer.media().play(videoPath);
    }
}
