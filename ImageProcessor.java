import java.util.*;
import java.awt.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImageProcessor {
    private static BufferedImage uploadedImage;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Image Filtering");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200, 200, 500, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(5, 5));

        JLabel title = new JLabel("Image Processor", SwingConstants.CENTER);
        frame.add(title, BorderLayout.NORTH);
        setFontSize(title, 22);

        JPanel Options = new JPanel();
        Options.setLayout(new GridLayout(0, 1, 5, 5));
        frame.add(Options, BorderLayout.WEST);

        JButton upload = new JButton("Upload Image");
        setFontSize(upload, 22);
        upload.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    uploadedImage = ImageIO.read(selectedFile);
                    JLabel imageLabel = new JLabel(new ImageIcon(uploadedImage));
                    frame.add(imageLabel, BorderLayout.CENTER);
                    frame.pack();
                } catch (Exception ex) {
                    JLabel ErrorMessage = new JLabel("Error");
                    frame.add(ErrorMessage, BorderLayout.CENTER);
                }
            }
        });

        JButton GrayScale = new JButton("GrayScale");
        setFontSize(GrayScale, 22);
        GrayScale.addActionListener(e -> {
            if (uploadedImage != null) {
                try {
                    JLabel imageLabel = new JLabel(new ImageIcon(grayScaleConvert(uploadedImage)));
                    JFrame newframe = new JFrame();
                    newframe.setLocationRelativeTo(null);
                    newframe.add(imageLabel);
                    newframe.pack();
                    newframe.setVisible(true);
                } catch (Exception ex) {
                    JLabel ErrorMessage = new JLabel("Error");
                    frame.add(ErrorMessage, BorderLayout.CENTER);
                }
            }
        });

        JButton Pixelate = new JButton("Pixelate");
        setFontSize(Pixelate, 22);
        Pixelate.addActionListener(e -> {
            if (uploadedImage != null) {
                try {
                    JLabel imageLabel = new JLabel(new ImageIcon(pixelate(uploadedImage, 5)));
                    JFrame newframe = new JFrame();
                    newframe.setLocationRelativeTo(null);
                    newframe.add(imageLabel);
                    newframe.pack();
                    newframe.setVisible(true);
                } catch (Exception ex) {
                    JLabel ErrorMessage = new JLabel("Error");
                    frame.add(ErrorMessage, BorderLayout.CENTER);
                }
            }
        });

        JButton EdgeDetect = new JButton("Detect-edges");
        setFontSize(EdgeDetect, 22);
        EdgeDetect.addActionListener(e -> {
            if (uploadedImage != null) {
                try {
                    JLabel imageLabel = new JLabel(new ImageIcon(detectEdges(uploadedImage)));
                    JFrame newframe = new JFrame();
                    newframe.setLocationRelativeTo(null);
                    newframe.add(imageLabel);
                    newframe.pack();
                    newframe.setVisible(true);
                } catch (Exception ex) {
                    JLabel ErrorMessage = new JLabel("Error");
                    frame.add(ErrorMessage, BorderLayout.CENTER);
                }
            }
        });

        JButton NegatImage = new JButton("Negative Image");
        setFontSize(NegatImage, 22);
        NegatImage.addActionListener(e -> {
            if (uploadedImage != null) {
                try {
                    JLabel imageLabel = new JLabel(new ImageIcon(negativeImage(uploadedImage)));
                    JFrame newframe = new JFrame();
                    newframe.setLocationRelativeTo(null);
                    newframe.add(imageLabel);
                    newframe.pack();
                    newframe.setVisible(true);
                } catch (Exception ex) {
                    JLabel ErrorMessage = new JLabel("Error");
                    frame.add(ErrorMessage, BorderLayout.CENTER);
                }
            }
        });

        JButton FlipHori = new JButton("Flip Horizontally");
        setFontSize(FlipHori, 22);
        FlipHori.addActionListener(e -> {
            if (uploadedImage != null) {
                try {
                    JLabel imageLabel = new JLabel(new ImageIcon(flipHorizontally(uploadedImage)));
                    JFrame newframe = new JFrame();
                    newframe.setLocationRelativeTo(null);
                    newframe.add(imageLabel);
                    newframe.pack();
                    newframe.setVisible(true);
                } catch (Exception ex) {
                    JLabel ErrorMessage = new JLabel("Error");
                    frame.add(ErrorMessage, BorderLayout.CENTER);
                }
            }
        });

        JButton FlipVert = new JButton("Flip Vertically");
        setFontSize(FlipVert, 22);
        FlipVert.addActionListener(e -> {
            if (uploadedImage != null) {
                try {
                    JLabel imageLabel = new JLabel(new ImageIcon(flipVertically(uploadedImage)));
                    JFrame newframe = new JFrame();
                    newframe.setLocationRelativeTo(null);
                    newframe.add(imageLabel);
                    newframe.pack();
                    newframe.setVisible(true);
                } catch (Exception ex) {
                    JLabel ErrorMessage = new JLabel("Error");
                    frame.add(ErrorMessage, BorderLayout.CENTER);
                }
            }
        });

        Options.add(GrayScale);
        Options.add(Pixelate);
        Options.add(EdgeDetect);
        Options.add(NegatImage);
        Options.add(FlipHori);
        Options.add(FlipVert);
        frame.add(upload, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static BufferedImage detectEdges(BufferedImage img) {
        int h = img.getHeight(), w = img.getWidth(), threshold = 30, p = 0;
        BufferedImage edgeImg = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        int[][] vertical = new int[w][h];
        int[][] horizontal = new int[w][h];
        int[][] edgeWeight = new int[w][h];
        for (int y = 1; y < h - 1; y++) {
            for (int x = 1; x < w - 1; x++) {
                vertical[x][y] = (int) (img.getRGB(x + 1, y - 1)
                        & 0xFF + 2 * (img.getRGB(x + 1, y) & 0xFF) + img.getRGB(x + 1, y + 1) & 0xFF
                                - img.getRGB(x - 1, y - 1)
                        & 0xFF - 2 * (img.getRGB(x - 1, y) & 0xFF) - img.getRGB(x - 1, y + 1) & 0xFF);
                horizontal[x][y] = (int) (img.getRGB(x - 1, y + 1)
                        & 0xFF + 2 * (img.getRGB(x, y + 1) & 0xFF) + img.getRGB(x + 1, y + 1) & 0xFF
                                - img.getRGB(x - 1, y - 1)
                        & 0xFF - 2 * (img.getRGB(x, y - 1) & 0xFF) - img.getRGB(x + 1, y - 1) & 0xFF);
                edgeWeight[x][y] = (int) (Math
                        .sqrt(vertical[x][y] * vertical[x][y] + horizontal[x][y] * horizontal[x][y]));
                if (edgeWeight[x][y] > threshold)
                    p = (255 << 24) | (255 << 16) | (255 << 8) | 255;
                else
                    p = (255 << 24) | (0 << 16) | (0 << 8) | 0;
                edgeImg.setRGB(x, y, p);
            }
        }
        return edgeImg;
    }

    public static BufferedImage grayScaleConvert(BufferedImage img) {
        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = newImg.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return newImg;
    }

    public static BufferedImage pixelate(BufferedImage img, int n) {
        BufferedImage pixImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        int pix = 0, p = 0;
        for (int y = 0; y < img.getHeight() - n; y += n) {
            for (int x = 0; x < img.getWidth() - n; x += n) {
                for (int a = 0; a < n; a++) {
                    for (int b = 0; b < n; b++) {
                        pix += (img.getRGB(x + a, y + b) & 0xFF);
                    }
                }
                pix = (int) (pix / n / n);
                for (int a = 0; a < n; a++) {
                    for (int b = 0; b < n; b++) {
                        p = (255 << 24) | (pix << 16) | (pix << 8) | pix;
                        pixImg.setRGB(x + a, y + b, p);
                    }
                }
                pix = 0;
            }
        }
        return pixImg;
    }

    public static BufferedImage negativeImage(BufferedImage img) {
        BufferedImage negImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int pixel = img.getRGB(x, y);
                int red = 255 - (pixel >> 16) & 0xFF;
                int green = 255 - (pixel >> 8) & 0xFF;
                int blue = 255 - (pixel) & 0xFF;
                negImg.setRGB(x, y, (red << 16) | (green << 8) | blue);
            }
        }
        return negImg;
    }

    public static BufferedImage flipHorizontally(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = img.getRGB(x, y);
                flippedImage.setRGB(width - 1 - x, y, pixel);
            }
        }

        return flippedImage;
    }

    public static BufferedImage flipVertically(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = img.getRGB(x, y);
                flippedImage.setRGB(x, height - 1 - y, pixel);
            }
        }

        return flippedImage;
    }

    private static void setFontSize(JComponent component, int size) {
        Font font = component.getFont();
        component.setFont(new Font(font.getName(), font.getStyle(), size));
    }
}
