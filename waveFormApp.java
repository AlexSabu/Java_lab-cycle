import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class waveFormApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Wave Form");
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            WavePanel wavePanel = new WavePanel();
            frame.add(wavePanel, BorderLayout.CENTER);

            JButton startButton = new JButton("Start");
            JButton stopButton = new JButton("Stop");

            startButton.addActionListener(e -> wavePanel.startWave());
            stopButton.addActionListener(e -> wavePanel.stopWave());

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(startButton);
            buttonPanel.add(stopButton);

            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
        });
    }
}
class WavePanel extends JPanel {
    private static final int WAVE_AMPLITUDE = 50;
    private static final int WAVE_FREQUENCY = 20;
    private static final int WAVE_SPEED = 5;

    private int xOffset = 0;
    private boolean isWaveRunning = false;

    public void startWave() {
        isWaveRunning = true;
        animateWave();
    }

    public void stopWave() {
        isWaveRunning = false;
    }

    private void animateWave() {
        Timer timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xOffset += WAVE_SPEED;
                repaint();

                if (!isWaveRunning) {
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int height = getHeight();
        int width = getWidth();

        g.setColor(Color.BLUE);

        int[] wavePoints = new int[width];

        for (int x = 0; x < width; x++) {
            int y = (int) (WAVE_AMPLITUDE * Math.sin(Math.toRadians(x + xOffset) * WAVE_FREQUENCY)) + height / 2;
            wavePoints[x] = y;
        }

        for (int x = 0; x < width - 1; x++) {
            g.drawLine(x, wavePoints[x], x + 1, wavePoints[x + 1]);
        }
    }
}
