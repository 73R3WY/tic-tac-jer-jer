
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

/**
 * @author Jeremy Patrick Tic-Tac-Toe Java implementation with AI
 */
public class TicTacJerJer extends JFrame implements ActionListener {

    public static final String TAG_P1 = "Player 1";
    public static final String TAG_P2 = "Player 2";
    public static final String TAG_DRAW = "Draw";
    public static final ImageIcon[] ICON_SYMBOLS = {
        new ImageIcon("res" + File.separator + "x.png"), new ImageIcon("res" + File.separator + "o.png")
    };

    public static final int[][] LINES = {
        {0, 1, 2},
        {3, 4, 5},
        {6, 7, 8},
        {0, 3, 6},
        {1, 4, 7},
        {2, 5, 8},
        {0, 4, 8},
        {2, 4, 6}
    };

    private JPanel GAMEBOARD;
    private JButton[] BUTTONS;
    private JLabel SETTINGS;
    private ArrayList<JButton> TILES;
    private boolean PLAYER_ONE_TURN;
    private ClickedTile[] CLICKED;

    // Settings
    private boolean versusAI;
    private boolean playerOneFirst;
    private boolean AIFirstMove;
    private int playerOneSymbol;
    private String settingsGame;

    public TicTacJerJer() {
        versusAI = true;
        playerOneFirst = true;
        playerOneSymbol = 0;
        setJFrameProps();
        initialize();
    }

    private void setJFrameProps() {
        // JFrame properties
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(ICON_SYMBOLS[1].getImage());
        setTitle("The Undefeatable TicTacJerJer");
        setLocation(150, 150);
        setPreferredSize(new Dimension(400, 450));
        setResizable(false);
        setLayout(null);
    }

    private void initialize() {
        getSettings();
        PLAYER_ONE_TURN = playerOneFirst;
        CLICKED = new ClickedTile[9];
        TILES = new ArrayList<>();
        BUTTONS = new JButton[9];
        GAMEBOARD = new JPanel();
        SETTINGS = new JLabel(settingsGame);
        SETTINGS.setBounds(50, 350, 300, 25);
        GAMEBOARD.setBounds(50, 25, 300, 300);
        GAMEBOARD.setLayout(new GridLayout(3, 3));

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                BUTTONS[(x * 3) + y] = new JButton();
                BUTTONS[(x * 3) + y].addActionListener(this);
                CLICKED[(x * 3) + y] = new ClickedTile(false, "");
                TILES.add(BUTTONS[(x * 3) + y]);
                GAMEBOARD.add(BUTTONS[(x * 3) + y]);
            }
        }

        add(GAMEBOARD);
        add(SETTINGS);
        pack();
        firstMove();
    }

    private void firstMove() {
        if (AIFirstMove) {
            TILES.get(4).doClick();
        }
    }

    private void getSettings() {
        final JPanel settingsPanel = new JPanel(new GridLayout(6, 1));
        final JLabel vs = new JLabel("VERSUS");
        final JCheckBox vsPlayer = new JCheckBox("Human");
        final JCheckBox vsComputer = new JCheckBox("Computer");
        final JCheckBox startWithPlayerOne = new JCheckBox("Start with Player 1");
        final JCheckBox playerOneIsX = new JCheckBox("Player 1 symbol is \"X\"");
        final JLabel displayOptions = new JLabel("");
        vsComputer.setSelected(versusAI);
        vsPlayer.setSelected(!versusAI);
        startWithPlayerOne.setSelected(playerOneFirst);
        playerOneIsX.setSelected(playerOneSymbol == 0);

        vsPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vsComputer.setEnabled(vsPlayer.isSelected());
                vsComputer.setSelected(!vsPlayer.isSelected());
                displayOptions.setText(getSettingsString(vsPlayer.isSelected(), startWithPlayerOne.isSelected(), playerOneIsX.isSelected()));
            }
        });

        vsComputer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vsPlayer.setEnabled(vsComputer.isSelected());
                vsPlayer.setSelected(!vsComputer.isSelected());
                displayOptions.setText(getSettingsString(vsPlayer.isSelected(), startWithPlayerOne.isSelected(), playerOneIsX.isSelected()));
            }
        });

        startWithPlayerOne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayOptions.setText(getSettingsString(vsPlayer.isSelected(), startWithPlayerOne.isSelected(), playerOneIsX.isSelected()));
            }
        });

        playerOneIsX.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayOptions.setText(getSettingsString(vsPlayer.isSelected(), startWithPlayerOne.isSelected(), playerOneIsX.isSelected()));
            }
        });

        settingsPanel.setPreferredSize(new Dimension(800, 100));
        settingsPanel.add(vs);
        settingsPanel.add(vsPlayer);
        settingsPanel.add(vsComputer);
        settingsPanel.add(startWithPlayerOne);
        settingsPanel.add(playerOneIsX);
        settingsPanel.add(displayOptions);
        displayOptions.setText(getSettingsString(vsPlayer.isSelected(), startWithPlayerOne.isSelected(), playerOneIsX.isSelected()));

        if (JOptionPane.showConfirmDialog(null, settingsPanel, "New game settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            versusAI = vsComputer.isSelected();
            playerOneFirst = startWithPlayerOne.isSelected();
            AIFirstMove = versusAI && !playerOneFirst;
            playerOneSymbol = playerOneIsX.isSelected() ? 0 : 1;
        } else {
            versusAI = false;
            AIFirstMove = false;
            playerOneFirst = true;
            playerOneSymbol = 0;
            settingsGame = "Default: vs Player 2, Player 1 first move, Player 1 is \"x\"";
        }
    }

    private String getSettingsString(boolean vsPlayer, boolean startP1, boolean XP1) {
        settingsGame = new String();
        String settings = "You will be playing against ";
        if (vsPlayer) {
            settingsGame += "[vs Player] ";
            settings += "ANOTHER PLAYER wherein ";
        } else {
            settingsGame += "[vs AI] ";
            settings += "the COMPUTER (AI) wherein ";
        }

        if (startP1) {
            settingsGame += "[P1 first move] ";
            settings += "YOU will MOVE FIRST and ";
        } else {
            settingsGame += "[AI/P2 first move] ";
            settings += "the OTHER PLAYER will MOVE FIRST and ";
        }

        if (XP1) {
            settingsGame += "[P1 = x] [AI/P2 = o]";
            settings += "YOUR symbol is x and the other player is o.";
        } else {
            settingsGame += "[P1 = o] [AI/P2 = x]";
            settings += "YOUR symbol is o and the other player is x.";
        }

        return settings;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton CLICKED_BTN = TILES.get(TILES.indexOf(e.getSource()));
        ClickedTile CLICKED_TILE = CLICKED[TILES.indexOf(e.getSource())];

        if (!CLICKED_TILE.isClicked()) {
            CLICKED_TILE.setClicked(true);
            CLICKED_TILE.setPlayerTile(PLAYER_ONE_TURN ? TAG_P1 : TAG_P2);
            CLICKED_BTN.setIcon(PLAYER_ONE_TURN ? ICON_SYMBOLS[playerOneSymbol] : (playerOneSymbol == 0 ? ICON_SYMBOLS[1] : ICON_SYMBOLS[0]));

            PLAYER_ONE_TURN = !PLAYER_ONE_TURN;

            BoardChecker BORED = new BoardChecker(CLICKED);
            if (BORED.isWon()) {
                int[] ENABLED = BORED.getWinningLine();
                for (JButton tile : TILES) {
                    tile.setEnabled(false);
                    for (int x : ENABLED) {
                        if (x == TILES.indexOf(tile)) {
                            tile.setEnabled(true);
                            break;
                        }
                    }
                }

                if (BORED.getWinner().equals(TAG_DRAW)) {
                    JOptionPane.showMessageDialog(getParent(), "The game is draw, there is no winner!", "Game Over! Draw!", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(getParent(), "Congratulations! The winner is " + BORED.getWinner() + ".", "Game Over!", JOptionPane.INFORMATION_MESSAGE);
                }

                remove(GAMEBOARD);
                remove(SETTINGS);
                initialize();
            }

            if (versusAI && !PLAYER_ONE_TURN) {
                UnbeatableTicTacJerJerAI AI = new UnbeatableTicTacJerJerAI(CLICKED);
                TILES.get(AI.getNextMove()).doClick();
            }
        }
    }

    class UnbeatableTicTacJerJerAI {

        private final ClickedTile[] T;
        private int nextBestMove;

        public UnbeatableTicTacJerJerAI(ClickedTile[] t) {
            T = t;
            selectNextBestMove();
        }

        private void selectNextBestMove() {
            ClickedTile[] T_Copy;
            int SCORE;
            int BEST_SCORE = -1;

            for (int location : new BoardChecker(T).getEmptyTiles()) {
                T_Copy = T.clone();
                T_Copy[location] = new ClickedTile(true, TAG_P2);
                SCORE = minMax(T_Copy, oppositePlayer(TAG_P2));
                if (SCORE >= BEST_SCORE) {
                    BEST_SCORE = SCORE;
                    nextBestMove = location;
                }
            }
        }

        private int minMax(ClickedTile[] board, String PLAYER) {
            ClickedTile[] T_Copy;
            int SCORE;
            int MAX = -1;
            int MULTIPLIER = 1;
            if (!PLAYER.equals(TAG_P2)) {
                MULTIPLIER = -1;
            }

            BoardChecker BORED = new BoardChecker(board);
            if (BORED.isWon()) {
                return evaluateWinner(BORED.getWinner());
            }

            for (int location : BORED.getEmptyTiles()) {
                T_Copy = board.clone();
                T_Copy[location] = new ClickedTile(true, PLAYER);
                SCORE = MULTIPLIER * minMax(T_Copy, oppositePlayer(PLAYER));
                if (SCORE >= MAX) {
                    MAX = SCORE;
                }
            }

            return MULTIPLIER * MAX;
        }

        private String oppositePlayer(String PLAYER) {
            return PLAYER.equals(TAG_P1) ? TAG_P2 : TAG_P1;
        }

        private int evaluateWinner(String PLAYER) {
            switch (PLAYER) {
                case TAG_DRAW:
                    return 0;
                case TAG_P2:
                    return 1;
                default:
                    return -1;
            }
        }

        public int getNextMove() {
            return nextBestMove;
        }
    }

    class ClickedTile {

        private boolean clicked;
        private String player;

        public ClickedTile(boolean clicked, String player) {
            this.clicked = clicked;
            this.player = player;
        }

        public void setClicked(boolean clicked) {
            this.clicked = clicked;
        }

        public void setPlayerTile(String player) {
            this.player = player;
        }

        public boolean isClicked() {
            return clicked;
        }

        public String getPlayer() {
            return player;
        }
    }

    class BoardChecker {

        private ClickedTile[] T;
        private String winner;
        private boolean won;
        private int[] LINE;
        private int[] EMPTY_TILES;

        public BoardChecker(ClickedTile[] t) {
            T = t;
            won = false;
        }

        public int[] getEmptyTiles() {
            ArrayList<Integer> empty = new ArrayList<>();
            for (int x = 0; x < T.length; x++) {
                if (!T[x].isClicked()) {
                    empty.add(x);
                }
            }

            EMPTY_TILES = new int[empty.size()];
            for (int x = 0; x < EMPTY_TILES.length; x++) {
                EMPTY_TILES[x] = empty.get(x);
            }

            return EMPTY_TILES;
        }

        public String getWinner() {
            return winner;
        }

        public int[] getWinningLine() {
            return LINE;
        }

        public boolean isWon() {
            won = checkBoard(TAG_P1);
            if (!won) {
                won = checkBoard(TAG_P2);
            }

            if (!won) {
                for (int x = 0; x < 9; x++) {
                    if (!T[x].isClicked()) {
                        won = true;
                        break;
                    }
                }

                if (won) {
                    won = false;
                } else {
                    won = true;
                    LINE = new int[]{69, 69, 69};
                    winner = TAG_DRAW;
                }
            }

            return won;
        }

        private boolean checkBoard(String player) {
            for (int[] currLine : LINES) {
                if (T[currLine[0]].getPlayer().equals(player) && T[currLine[1]].getPlayer().equals(player) && T[currLine[2]].getPlayer().equals(player)) {
                    LINE = currLine;
                    winner = player;
                    return true;
                }
            }

            return false;
        }
    }

    public static void main(String[] argssssss) {
        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TicTacJerJer().setVisible(true);
            }
        });
    }
}
