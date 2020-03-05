import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameField extends JPanel implements ActionListener {
    private final int _size = 320;
    private final int _dot_size = 16;
    private final int _total_dots = 400;
    private Image _dot;
    private Image _apple;
    private int _appleX ;
    private int _appleY;
    private int[] _x_positions = new int[_total_dots];
    private int [] _y_positions = new int[_total_dots];
    private int _dots;
    private Timer _timer;
    private boolean _left = false;
    private boolean _right = true;
    private boolean _up = false;
    private boolean _down = false;
    private boolean _inGame = true;
    private int _score = 0;

    public GameField() {
        setBackground(Color.GREEN);
        __loadImages();
        __initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    public void __initGame() {
        _dots = 3;
        for (int i = 0; i < _dots; i++) {
            _x_positions[i] = 48 - i*_dot_size;
            _y_positions[i] = 48;
        }
        _timer = new Timer(250, this);
        _timer.start();
        __createApple();
    }

    public void __createApple() {
        int _rand = __rand(2, 15)*_dot_size;
        if (_rand == _appleX) {
            __createApple();
            return;
        }
        _appleX = _rand;
        _appleY = _rand;
//        System.out.println(_rand);
    }

    public void __loadImages() {
        ImageIcon _iia = new ImageIcon("apple.png");
        _apple = _iia.getImage();
        ImageIcon _iid = new ImageIcon("dot.png");
        _dot = _iid.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (_inGame) {
            this.updateScore(g);
            g.drawImage(_apple, _appleX, _appleY, this);
            for (int i = 0; i < _dots; i++) {
                g.drawImage(_dot, _x_positions[i], _y_positions[i], this);
            }
        } else {
            setBackground(Color.black);
            String _str = "Game Over!";
            String _score_str = "Your score: " + _score;
//            Font _font = new Font("Arial", 14, Font.BOLD);
            g.setColor(Color.white);
//            g.setFont(_font);
            g.drawString(_str, 125, _size/2);//160
            g.drawString(_score_str, 125, 170);
        }
    }

    public void updateScore(Graphics g) {
        String _str = "Score: " + _score;
        g.setColor(Color.white);
        g.drawString(_str, 0, 10);
    }

    public void __move() {
        for (int i = _dots; i > 0; i--) {
            _x_positions[i] = _x_positions[i-1];
            _y_positions[i] = _y_positions[i-1];
        }
        if (_left) {
            _x_positions[0] -= _dot_size;
        } if (_right) {
            _x_positions[0] += _dot_size;
        } if (_up) {
            _y_positions[0] -= _dot_size;
        } if (_down) {
            _y_positions[0] += _dot_size;
        }
    }

    public void __checkApple() {
        if (_x_positions[0] == _appleX && _y_positions[0] == _appleY) {
            _dots++;
            _score++;
            __createApple();
        }
    }

    public void __checkCollisions() {
        for (int i = _dots; i > 0; i--) {
            if (i > 4 && _x_positions[0] == _x_positions[i] && _y_positions[0] == _y_positions[i]) {
                _inGame = false;
            }
        }
        if (_x_positions[0] > _size) {
            _x_positions[0] = 0;
        } if (_y_positions[0] > _size) {
            _y_positions[0] = 0;
        } if (_x_positions[0] < 0) {
            _x_positions[0] = _size;
        } if (_y_positions[0] < 0) {
            _y_positions[0] = _size;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (_inGame) {
            __checkApple();
            __checkCollisions();
            __move();
        }
        repaint();
    }

    public int __rand(int min, int max) {
        return (int) (Math.random()*(max-min))+min;
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int _key = e.getKeyCode();
            if (_key == KeyEvent.VK_LEFT && ! _right) {
                _left = true;
                _up = false;
                _down = false;
            }
            if (_key == KeyEvent.VK_RIGHT && ! _left) {
                _right = true;
                _up = false;
                _down = false;
            }
            if (_key == KeyEvent.VK_DOWN && ! _up) {
                _down = true;
                _right = false;
                _left = false;
            }
            if (_key == KeyEvent.VK_UP && ! _down) {
                _up = true;
                _right = false;
                _left = false;
            }
        }
    }
}
