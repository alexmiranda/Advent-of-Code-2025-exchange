import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicTextUI.BasicCaret;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

class Y23GUIOutput01 {

	@SuppressWarnings("serial")
	public static class MyCaret extends BasicCaret {
		@Override public void install(JTextComponent c) { return; }
	}
	
	private List<String> textListe;
	private int currentText;

    boolean buffered;

    JFrame f;
    JTextPane jt;
    JScrollPane sp;
    JLabel lbTextID;
    JSlider slider;
    
    Timer timer;
    
    public Y23GUIOutput01(String title, boolean buffered) {
    	this.textListe = new ArrayList<>();
    	this.currentText = -1;
    	this.buffered = buffered;
		create(title);
	}
    
    public void create(String title) {
    	initColorStlye();
        f = new JFrame(title);
        Container p = f.getContentPane();
        
        BoxLayout bl = new BoxLayout(p, BoxLayout.Y_AXIS);
        p.setLayout(bl);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        
        JButton btPrevious = new JButton("<");
        buttonPanel.add(btPrevious);
        btPrevious.addActionListener(ev -> {
        	previous();
        });

        buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
        
        lbTextID = new JLabel("0");
        buttonPanel.add(lbTextID);
        
        buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
        
        JButton btNext = new JButton(">");
        buttonPanel.add(btNext);
        btNext.addActionListener(ev -> {
        	next();
        });
        
        buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));

        JButton btSmaller = new JButton("v");
        buttonPanel.add(btSmaller);
        btSmaller.addActionListener(ev -> {
        	smaller();
        });
        
        JButton btBigger = new JButton("^");
        buttonPanel.add(btBigger);
        btBigger.addActionListener(ev -> {
        	bigger();
        });
        
        buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));

        JButton btBuffered = new JButton("Buffered");
        buttonPanel.add(btBuffered);
        btBuffered.addActionListener(ev -> {
        	buffered = ! buffered;
        });
        
        buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
        
        JButton btAnimation = new JButton("Animation");
        buttonPanel.add(btAnimation);
        btAnimation.addActionListener(ev -> {
        	animation();
        });
        
        p.add(buttonPanel);

        slider = new JSlider(JSlider.HORIZONTAL, 0, 10000, 0);
        slider.addChangeListener(ev -> {
        	double percent = slider.getValue() * 0.0001;
        	int page = (int) (percent * (textListe.size()-1));
        	switchPage(page);
        });
        p.add(slider);
        
        jt = new JTextPane();
//        jt.setCaret(new MyCaret());
        jt.setEditable(false);
//        jt.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        jt.setFont(new Font("Consolas", Font.PLAIN, 16));
        
        sp = new JScrollPane(jt); 
        p.add(sp);
        
        // set initial size of frame
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    
    private int nextPage = -1;

    private synchronized void switchPage(int page) {
    	if (nextPage == -1) {
        	nextPage = page;
        	SwingUtilities.invokeLater(() -> asyncSwitchPage());
        	return;
    	}
    	nextPage = page;
    }

    private Map<String, AttributeSet> styles = new HashMap<>();
    
    private void addColorStyle(String name, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        styles.put(name, aset);
    }
    
    private void initColorStlye() {
    	addColorStyle("cwhite", Color.WHITE);
    	addColorStyle("clgray", Color.LIGHT_GRAY);
    	addColorStyle("cgray", Color.GRAY);
    	addColorStyle("cdgray", Color.DARK_GRAY);
    	addColorStyle("cblack", Color.BLACK);
    	addColorStyle("cred", Color.RED);
    	addColorStyle("cpink", Color.PINK);
    	addColorStyle("corange", Color.ORANGE);
    	addColorStyle("cyellow", Color.YELLOW);
    	addColorStyle("cgreen", Color.GREEN);
    	addColorStyle("cmagenta", Color.MAGENTA);
    	addColorStyle("ccyan", Color.CYAN);
    	addColorStyle("cblue", Color.BLUE);
    }
    
    private void getStyle(String styleName) {
    	
    }
    
    private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        StyledDocument sDoc = tp.getStyledDocument();
        try {
			sDoc.insertString(sDoc.getLength(), msg, aset);
		} catch (BadLocationException e) {
			throw new RuntimeException(e.toString(), e);
		}
    }
    
    private synchronized void asyncSwitchPage() {
    	int targetPage = Math.min(textListe.size()-1, Math.max(0, nextPage));
    	if (currentText != targetPage) {
    		currentText = targetPage;
    		String text = textListe.get(currentText);
    		sp.getHorizontalScrollBar().getValue();
    		sp.getVerticalScrollBar().getValue();
        	addColoredText(text);
        	lbTextID.setText(Integer.toString(currentText));   	
    	}
    	nextPage = -1;
	}
    
    
    private void addColoredText(String text) {
    	jt.setText("");
    	if (!text.startsWith("°")) {
    		text = "°cblack;"+text;
    	}
    	String[] coloredBlocks = text.split("°");
    	for (String coloredBlock:coloredBlocks) {
    		
        	appendToPane(jt, text, );
    	}
	}

	private void previous() {
		switchPage(currentText-1);
	}
    
    private void next() {
		switchPage(currentText+1);
	}

	public void addStep(String text) {
		if (buffered) {
			textListe.add(text);
			if (currentText != -1) {
				return;
			}
		}
		currentText = textListe.size();
		textListe.add(text);
    	addColoredText(text);
    	lbTextID.setText(Integer.toString(currentText));
    }

    private void smaller() {
    	Font font = jt.getFont();
    	Font smallFont = font.deriveFont(font.getSize2D()/1.1f);
    	jt.setFont(smallFont);
	}

    private void bigger() {
    	Font font = jt.getFont();
    	Font smallFont = font.deriveFont(font.getSize2D()*1.1f);
    	jt.setFont(smallFont);
	}

    private synchronized void animation() {
    	if (timer != null) {
    		timer.stop();
    		timer = null;
    		return;
    	}
    	timer = new Timer(250, ev -> {
    		asyncAnimation();
    	});
    	timer.setInitialDelay(250);
    	timer.start();
	}

	private synchronized void asyncAnimation() {
		if (timer == null) {
			return;
		}
		int page = currentText + 1;
		if (page >= textListe.size()) {
			timer.stop();
			timer = null;
			return;
		}
		switchPage(page);
	}
    
	public static void main(String[] args) {
		Y23GUIOutput01 output = new Y23GUIOutput01("title", true);
		output.addStep("Text1\nText2");
	}
	
}