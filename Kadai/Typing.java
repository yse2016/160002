import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class Typing extends JFrame implements KeyListener, ActionListener {

public static void main(String[] args) {
JFrame frame = new Typing("タイピング");
frame.setBounds(0, 0, 640, 480);
frame.setLocationRelativeTo(null);
frame.setVisible(true);
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}

private JLabel label;
private Random random;
private String[] words = { "have", "do", "be", "make", "can", "will",
"think", "see", "want", "get", "break", "sing", "swim", "love",
"what", "who", "when", "where", "why", "how", "go", "come", "know",
"word" };

// 得点表示用ラベル
private JLabel scoreLabel;

// 制限時間の初期値（ミリ秒）
private static final int INIT_DELAY = 2000;

// 入力成功ごとにスピードアップする速度（ミリ秒）
private static final int SPEED = 10;

// ランキング数
private static final int NUM_RANKING = 5;

private boolean isPlaying; // プレイ中ならtrue
private int delay; // 現在の制限時間 
private int score; // 現在の得点
private ArrayList<Integer> ranking; // ランキング
private long prevTime; // 直前に単語を表示した時刻
private javax.swing.Timer timer; // 時間制限に使うタイマー

public Typing(String title) {
super(title);

random = new Random(System.currentTimeMillis());
label = new JLabel(" ");
label.setFont(new Font(null, Font.PLAIN, 30));
label.setHorizontalAlignment(JLabel.CENTER);
label.setVerticalAlignment(JLabel.CENTER);

getContentPane().add(label);
addKeyListener(this);

// スコア表示ラベル
scoreLabel = new JLabel();
scoreLabel.setHorizontalAlignment(JLabel.LEFT);
getContentPane().add(scoreLabel, BorderLayout.SOUTH);

// ランキング配列の初期化
ranking = new ArrayList<Integer>(NUM_RANKING);
for (int i = 0; i < NUM_RANKING; i++) {
ranking.add(0);
}

isPlaying = false;
timer = new javax.swing.Timer(0, this);
timer.setRepeats(false);
showTitle();
}

public void keyPressed(KeyEvent e) {
}

public void keyReleased(KeyEvent e) {
}

public void keyTyped(KeyEvent e) {
if (!isPlaying) {
newGame();
} else {
String text = label.getText();
if (text.charAt(0) == e.getKeyChar()) {
if (text.length() > 1) {
label.setText(text.substring(1));
} else {
success();
}
}
}
}

// このメソッドはタイマーが呼び出す
@Override
public void actionPerformed(ActionEvent e) {
if (!isPlaying) {
return;
}
if (label.getText().length() > 0) {
gameOver();
}
}

// スコアの再描画
private void refreshScore() {
scoreLabel.setText("得点：" + score + " 速度：" + delay);
}

// 新規ゲーム開始
private void newGame() {
isPlaying = true;
score = 0;
delay = INIT_DELAY;
refreshScore();
newWord();
}

// ゲームオーバーの時の処理
private void gameOver() {
isPlaying = false;
timer.stop();
ranking.add(score);
Collections.sort(ranking);
Collections.reverse(ranking);
ranking.remove(NUM_RANKING);
StringBuilder sb = new StringBuilder();
sb.append("ゲームオーバー\n\n");
sb.append("あなたのスコアーは ");
sb.append(score);
sb.append("点\n\n");
String lineSeparator = "";
for (int i = 0, m = ranking.size(); i < m; i++) {
sb.append(lineSeparator);
sb.append(i + 1);
sb.append("位 ");
sb.append(ranking.get(i));
sb.append(" 点");
lineSeparator = "\n";
}
JOptionPane.showMessageDialog(this, sb);
showTitle();
}

// 入力成功時の処理
private void success() {
// 早く入力したほうが得点が高い
long time = System.currentTimeMillis();
score += INIT_DELAY - (time - prevTime);
delay -= SPEED; // だんだん早くなる
delay = Math.max(delay, 0); // タイマーは０以下にできない
newWord();
refreshScore();
}

// 新しい単語を表示
private void newWord() {
label.setText(words[random.nextInt(words.length)]);
timer.setInitialDelay(delay);
timer.restart();
prevTime = System.currentTimeMillis();
}

// タイトル？表示
private void showTitle() {
label.setText("何かボタンを押すとスタートします。");
}
}