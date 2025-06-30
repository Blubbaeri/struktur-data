package objects;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

public class Snake {
    public Queue<Point> body = new LinkedList<>();
    public int direction = 0; // 0=RIGHT, 1=DOWN, 2=LEFT, 3=UP

    private int width, height;

    public Snake(int x0, int y0, int x1, int y1, int width, int height) {
        body.add(new Point(x0, y0));
        body.add(new Point(x1, y1));
        this.width = width;
        this.height = height;
    }

    public void move(boolean grow) {
        Point head = ((LinkedList<Point>) body).getLast();
        int x = head.x;
        int y = head.y;

        // gerak berdasarkan arah
        switch (direction) {
            case 0: x++; break; // kanan
            case 1: y++; break; // bawah
            case 2: x--; break; // kiri
            case 3: y--; break; // atas
        }

        // teleportasi ke sisi lain
        if (x >= width) x = 0;
        if (x < 0) x = width - 1;
        if (y >= height) y = 0;
        if (y < 0) y = height - 1;

        Point newHead = new Point(x, y);

        // tabrakan dengan tubuh sendiri
        if (body.contains(newHead)) {
            body.clear();
            body.add(new Point(10, 10)); // reset posisi
            body.add(new Point(9, 10));
            direction = 0;
            return;
        }

        body.add(newHead);         // tambahkan kepala baru
        if (!grow) body.poll();    // buang ekor jika tidak makan
    }

    public Point getHead() {
        return ((LinkedList<Point>) body).getLast();
    }

    public int getLength() {
        return body.size();
    }

    public Point getNextHead() {
        Point head = getHead();
        int x = head.x;
        int y = head.y;
        switch (direction) {
            case 0: x++; break; // kanan
            case 1: y++; break; // bawah
            case 2: x--; break; // kiri
            case 3: y--; break; // atas
        }
        return new Point(x, y);
    }


}
