package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;

public class Main {

    public static JFrame window;

    public static void main(String[] args) {
        setupLogging();
        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
            System.err.println("=== UNCAUGHT EXCEPTION in thread '" + thread.getName() + "' ===");
            ex.printStackTrace(System.err);
        });
        try {
            window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);
            window.setUndecorated(true);
            window.setTitle(Constants.GAME_TITLE);

            GamePanel gamePanel = new GamePanel(
                Constants.SCREEN_WIDTH,
                Constants.SCREEN_HEIGHT
            );

            gamePanel.playMusic(Constants.SOUND_TITLE_SCREEN);
            gamePanel.config.titleLoader();

            window.add(gamePanel);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            gamePanel.requestFocus();
            gamePanel.start();
            gamePanel.setFocusTraversalKeysEnabled(false);
            gamePanel.setIcon();

            // Game loop to be added to EDT
            javax.swing.SwingUtilities.invokeLater(gamePanel::start);

            // Debug visual pipelines bro
            System.out.println("Java2D pipeline: " + System.getProperty("sun.java2d.opengl"));
            System.out.println("Java2D D3D: " + System.getProperty("sun.java2d.d3d"));
        } catch (Throwable t) {
            System.err.println("Fatal error during game startup:");
            t.printStackTrace();
        }
    }

    private static void setupLogging() {
        try {
            File projectRoot = new File(System.getProperty("user.dir"));
            File logFile = new File(projectRoot, Constants.LOG_FILE);

            PrintStream originalOut = System.out;
            PrintStream originalErr = System.err;

            FileOutputStream fileStream = new FileOutputStream(logFile, false); // overwrite each run

            System.setOut(new PrefixingPrintStream(new TeeOutputStream(originalOut, fileStream), "OUT"));
            System.setErr(new PrefixingPrintStream(new TeeOutputStream(originalErr, fileStream), "ERR"));

            String pid = ManagementFactory.getRuntimeMXBean().getName();
            System.out.println("=== Wookster Log Started === pid=" + pid + " path=" + logFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Writes every byte to two underlying streams. */
    private static final class TeeOutputStream extends OutputStream {
        private final OutputStream a;
        private final OutputStream b;
        TeeOutputStream(OutputStream a, OutputStream b) { this.a = a; this.b = b; }

        @Override public void write(int x) throws IOException { a.write(x); b.write(x); }
        @Override public void write(byte[] buf, int off, int len) throws IOException {
            a.write(buf, off, len); b.write(buf, off, len);
        }
        @Override public void flush() throws IOException { a.flush(); b.flush(); }
        @Override public void close() throws IOException { try { a.close(); } finally { b.close(); } }
    }

    /**
     * PrintStream that prepends "[HH:mm:ss.SSS][thread][channel] " to every printed line.
     * Buffers per-thread until newline so prefixes don't split mid-line.
     */
    private static final class PrefixingPrintStream extends PrintStream {
        private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        private final ThreadLocal<Boolean> atLineStart = ThreadLocal.withInitial(() -> Boolean.TRUE);
        private final String channel;

        PrefixingPrintStream(OutputStream out, String channel) {
            super(out, true);
            this.channel = channel;
        }

        private void writePrefixIfNeeded() {
            if (atLineStart.get()) {
                byte[] prefix = ("[" + LocalDateTime.now().format(FMT) + "][" + Thread.currentThread().getName() + "][" + channel + "] ").getBytes();
                try {
                    out.write(prefix);
                } catch (IOException ignored) {}
                atLineStart.set(false);
            }
        }

        @Override public synchronized void write(int b) {
            writePrefixIfNeeded();
            try {
                out.write(b);
            } catch (IOException ignored) {}
            if (b == '\n') atLineStart.set(true);
        }

        @Override public synchronized void write(byte[] buf, int off, int len) {
            for (int i = 0; i < len; i++) {
                write(buf[off + i]);
            }
        }
    }
}
