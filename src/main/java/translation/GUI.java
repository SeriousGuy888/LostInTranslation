package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.Arrays;


// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {

    public static void main(String[] args) {
        JSONTranslator translator = new JSONTranslator();
        CountryCodeConverter countryConverter = new CountryCodeConverter();
        LanguageCodeConverter languageConverter = new LanguageCodeConverter();

        final String[] selected = new String[2]; // stores selected country code and language code

        SwingUtilities.invokeLater(() -> {

            // initialize language Panel (which contains the Combo Box)
            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));

            // create Combo Box, add country codes into it, and add it to our panel
            JComboBox<String> languageComboBox = new JComboBox<>();
            for(String countryCode : translator.getLanguageCodes()) {
                String languageName = languageConverter.fromLanguageCode(countryCode);
                languageComboBox.addItem(languageName);
            }
            languagePanel.add(languageComboBox);

            // initialize translation Panel (which contains the translation label)
            JPanel translationPanel = new JPanel();
            JLabel resultLabelText = new JLabel("Translation:");
            translationPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            translationPanel.add(resultLabel);

            // initialize country name list
            String[] countryList = new String[translator.getCountryCodes().size()];
            int i = 0;
            for(String countryCode : translator.getCountryCodes()) {
                countryList[i++] = countryConverter.fromCountryCode(countryCode);
            }

            // create the JList with the array of country names
            JList<String> list = new JList<>(countryList);

            // initialize country panel (which contains the JList of country names)
            JPanel countryPanel = new JPanel();
            JScrollPane scrollPane = new JScrollPane(list);
            countryPanel.add(scrollPane);

            list.addListSelectionListener(new ListSelectionListener() {
                /**
                 * Called whenever the value of the selection changes.
                 *
                 * @param e the event that characterizes the change.
                 */
                @Override
                public void valueChanged(ListSelectionEvent e) {

                    String selectedCountry = list.getSelectedValue();
                    selected[0] = countryConverter.fromCountry(selectedCountry);
                }
            });

            // add listener for when an item is selected.
            languageComboBox.addItemListener(new ItemListener() {

                /**
                 * Invoked when an item has been selected or deselected by the user.
                 * The code written for this method performs the operations
                 * that need to occur when an item is selected (or deselected).
                 *
                 * @param e the event to be processed
                 */
                @Override
                public void itemStateChanged(ItemEvent e) {

                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String language = languageComboBox.getSelectedItem().toString();
                        selected[1] = languageConverter.fromLanguage(language);
//                        String result = translator.translate(country, languageCode);
//                        if (result == null) {
//                            result = "no translation found!";
//                        }
//                        resultLabel.setText(result);
                    }
                }
            });

            // add all panels to the main panel
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(translationPanel);
            mainPanel.add(countryPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
