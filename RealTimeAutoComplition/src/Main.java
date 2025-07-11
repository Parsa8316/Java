import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Main {
    private static List<String> dictionary = Arrays.asList(
            "سلام", "سلامت", "سلامتی", "سلطان", "سلسله", "سوال", "سوغات", "سوسک", "سوراخ", "سورنا", "سوره", "سرما", "سرور", "سرزمین",
            "سفر", "ساعت", "سایه", "ساکت", "ساحل", "ساده", "سازمان", "ساز", "سیستم", "سیب", "سیاه", "سینا", "سینما", "شادی", "شاد",
            "شجاع", "شایسته", "شامل", "شام", "شاگرد", "شخص", "شعر", "شناس", "شنا", "شانس", "شلوغ", "شیرین", "شیراز", "شیشه", "شکار",
            "شب", "شبنم", "شبکه", "شخصیت", "شایعه", "شکلات", "شکست", "شروع", "شریف", "شغل", "شور", "شورای", "شیمی", "صدا", "صفحه",
            "صندوق", "صداوسیما", "صبر", "صبح", "صورت", "صحنه", "صنعت", "صادق", "صالح", "ضمیر", "ضروری", "ضعیف", "ضربه", "ضایع",
            "طبیعت", "طلب", "طراحی", "طول", "طاقت", "طوفان", "ظرف", "ظریف", "عالی", "عشق", "علم", "عادل", "عدالت", "عذر", "عادت",
            "عجیب", "عینک", "عروس", "غم", "غذا", "غروب", "غیر", "فرهنگ", "فرصت", "فردا", "فناوری", "فکر", "فعالیت", "فیلم", "فیلسوف",
            "قانون", "قرمز", "قشنگ", "قدرت", "قلعه", "قصه", "قهرمان", "کتاب", "کلمه", "کار", "کارگر", "کافی", "کودک", "کوه", "کوچه",
            "کامل", "کلاس", "کامپیوتر", "کرامت", "کره", "کلید", "کمی", "کمال", "کمک", "کنترل", "گلاب", "گل", "گوشی", "گوش", "گفتگو",
            "گام", "گذر", "گرافیک", "گمشده", "گروه", "یاد", "یادگیری", "یار", "یاقوت", "یاس", "یوسف", "یخ", "یکتا", "یعنی", "یادداشت"
    );
    public static List<String> getSuggestions(String prefix) {
        List<String> suggestions = new ArrayList<>();
        List<String> suggestions1 = new ArrayList<>();
        List<String> suggestions2 = new ArrayList<>();
        List<String> suggestions3 = new ArrayList<>();

        for (String word : dictionary) {
            if (word.startsWith(prefix)) {
                suggestions1.add(word);
            }
            if (word.endsWith(prefix) && !word.startsWith(prefix)){
                suggestions2.add(word);
            }
            if (word.contains(prefix) && !word.startsWith(prefix) && !word.endsWith(prefix)) {
                suggestions3.add(word);
            }
        }
        suggestions1.sort(Comparator.comparingInt(String::length));
        suggestions2.sort(Comparator.comparingInt(String::length));
        suggestions3.sort(Comparator.comparingInt(String::length));
        for (String word : suggestions1) suggestions.add(word);
        for (String word : suggestions2) suggestions.add(word);
        for (String word : suggestions3) suggestions.add(word);

        return suggestions;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Real Time Auto Completion");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 500);
            frame.setLocation(360, 170);
            frame.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            frame.setLayout(new BorderLayout());
            frame.setVisible(true);

            JTextField textField = new JTextField();
            textField.setBackground(Color.lightGray);

            DefaultListModel<String> listModel = new DefaultListModel<>();
            JList<String> suggestionList = new JList<>(listModel);

            frame.add(textField, BorderLayout.NORTH);
            frame.add(new JScrollPane(suggestionList), BorderLayout.CENTER);

            textField.setHorizontalAlignment(JTextField.RIGHT);  //راست چین کردن
            suggestionList.setCellRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    label.setHorizontalAlignment(SwingConstants.RIGHT);
                    label.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                    return label;
                }
            });

            textField.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) {
                    updateSuggestions();
                }

                public void removeUpdate(DocumentEvent e) {
                    updateSuggestions();
                }

                public void changedUpdate(DocumentEvent e) {
                    updateSuggestions();
                }

                private void updateSuggestions() {
                    String input = textField.getText();
                    listModel.clear();
                    if (!input.isEmpty()) {
                        for (String suggestion : getSuggestions(input)) {
                            listModel.addElement(suggestion);
                        }
                    }
                }
            });
        });
    }
}
