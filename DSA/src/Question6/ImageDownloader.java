import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ImageDownloader extends JFrame {
    private JTextField urlField = new JTextField("");
    private JButton addButton = new JButton("Download");
    private DefaultListModel<DownloadInfo> listModel = new DefaultListModel<>();
    private JList<DownloadInfo> downloadList = new JList<>(listModel);
    private ExecutorService downloadExecutor = Executors.newFixedThreadPool(10); // 10 concurrent downloads

    public ImageDownloader() {
        super("Asynchronous Image Downloader");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 300);
        layoutComponents();
        setVisible(true);
        setLocationRelativeTo(null); // Center the JFrame on the screen
    }

    private void layoutComponents() {
        // Set dark theme
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set background color to gray
        Font roboto14 = new Font("Arial", Font.PLAIN, 20);
        Color whiteColor = Color.WHITE;
        Color blackColor = Color.BLACK;
        Color blueColor = new Color(18, 23, 92);
        Color lightBlueColor = new Color(235, 236, 247);
        Color greyColor = Color.lightGray;

        getContentPane().setBackground(blueColor);
        urlField.setFont(roboto14);
        urlField.setForeground(blueColor);
        urlField.setBackground(lightBlueColor);
        addButton.setFont(roboto14);
        addButton.setBackground(blueColor);
        addButton.setForeground(whiteColor);

        JLabel titleLabel = new JLabel("");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(blackColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel addPanel = new JPanel();
        addPanel.setBackground(blackColor);
        addPanel.setLayout(new BorderLayout());
        addPanel.add(urlField, BorderLayout.CENTER);
        addPanel.add(addButton, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(downloadList);
        downloadList.setCellRenderer(new DownloadListRenderer());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(blueColor);

        JButton pauseResumeButton = new JButton("Pause/Resume");
        pauseResumeButton.setFont(roboto14);
        pauseResumeButton.setBackground(blueColor);
        pauseResumeButton.setForeground(blackColor);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(roboto14);
        cancelButton.setBackground(blueColor);
        cancelButton.setForeground(blackColor);

        buttonPanel.add(pauseResumeButton);
        buttonPanel.add(cancelButton);

        addButton.addActionListener(e -> addDownload(urlField.getText().trim()));

        pauseResumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DownloadInfo selectedInfo = downloadList.getSelectedValue();
                if (selectedInfo != null) {
                    selectedInfo.togglePauseResume();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DownloadInfo selectedInfo = downloadList.getSelectedValue();
                if (selectedInfo != null) {
                    selectedInfo.cancel();
                    listModel.removeElement(selectedInfo);
                }
            }
        });

        setLayout(new BorderLayout());
        

        add(addPanel, BorderLayout.SOUTH);
        add(titleLabel, BorderLayout.EAST);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH);
        
    }

    private void addDownload(String url) {
        try {
            new URL(url);
            DownloadInfo info = new DownloadInfo(url);
            listModel.addElement(info);
            DownloadTask task = new DownloadTask(info, () -> SwingUtilities.invokeLater(this::repaint));
            info.setFuture(downloadExecutor.submit(task));
        } catch (MalformedURLException ex) {
            JOptionPane.showMessageDialog(this, "Invalid URL: " + url, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ImageDownloader::new);
    }
}

class DownloadInfo {
    private final String url;
    private volatile String status = "Waiting..."; // Corrected the typo here
    private volatile long totalBytes = 0L;
    private volatile long downloadedBytes = 0L;
    private Future<?> future;
    private final AtomicBoolean paused = new AtomicBoolean(false);

    public DownloadInfo(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public synchronized boolean isPaused() {
        return paused.get();
    }

    public synchronized void togglePauseResume() {
        paused.set(!paused.get());
        notifyAll();
    }

    public String getStatus() {
        return status;
    }

    public synchronized void setStatus(String status) {
        this.status = status;
    }

    public void setFuture(Future<?> future) {
        this.future = future;
    }

    public void cancel() {
        if (future != null)
            future.cancel(true);
    }

    public synchronized void setTotalBytes(long totalBytes) {
        this.totalBytes = totalBytes;
    }

    public synchronized void addDownloadedBytes(long bytes) {
        this.downloadedBytes += bytes;
    }

    public long getDownloadedBytes() {
        return downloadedBytes;
    }

    public long getTotalBytes() {
        return totalBytes;
    }
}

class DownloadTask implements Callable<Void> {
    private final DownloadInfo info;
    private final Runnable updateUI;

    public DownloadTask(DownloadInfo info, Runnable updateUI) {
        this.info = info;
        this.updateUI = updateUI;
    }

    @Override
    public Void call() throws Exception {
        info.setStatus("Downloading");
        @SuppressWarnings("deprecation")
        URL url = new URL(info.getUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        long fileSize = connection.getContentLengthLong();
        info.setTotalBytes(fileSize);

        try (InputStream in = new BufferedInputStream(connection.getInputStream())) {
            Path targetPath = Paths.get("downloads", new File(url.getPath()).getName());
            Files.createDirectories(targetPath.getParent());
            try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(targetPath))) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    synchronized (info) {
                        while (info.isPaused())
                            info.wait();
                    }
                    out.write(buffer, 0, bytesRead);
                    info.addDownloadedBytes(bytesRead);
                    updateUI.run();
                    Thread.sleep(200);
                }
                info.setStatus("Completed");
            }
        } catch (IOException | InterruptedException e) {
            info.setStatus("Error: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        } finally {
            updateUI.run();
        }
        return null;
    }
}

class DownloadListRenderer extends JPanel implements ListCellRenderer<DownloadInfo> {
    @Override
    public Component getListCellRendererComponent(JList<? extends DownloadInfo> list, DownloadInfo value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        this.removeAll();
        setLayout(new BorderLayout());
        JLabel urlLabel = new JLabel(value.getUrl());
        JProgressBar progressBar = new JProgressBar(0, 100);
        if (value.getTotalBytes() > 0) {
            int progress = (int) ((value.getDownloadedBytes() * 100) / value.getTotalBytes());
            progressBar.setValue(progress);
        }
        JLabel statusLabel = new JLabel(value.getStatus());
        add(urlLabel, BorderLayout.NORTH);
        add(progressBar, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }
}
