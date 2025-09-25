package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;

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

        // final one element arrays because inner-class access funniness
        final String[] selectedCountryCode = {null};
        final String[] selectedLanguageCode = {null};

        SwingUtilities.invokeLater(() -> {

            // initialize language Panel (which contains the Combo Box)
            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));

            // create Combo Box, add country codes into it, and add it to our panel
            JComboBox<String> languageComboBox = new JComboBox<>();
            for (String countryCode : translator.getLanguageCodes()) {
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
            for (String countryCode : translator.getCountryCodes()) {
                countryList[i++] = countryConverter.fromCountryCode(countryCode);
            }

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
                        selectedLanguageCode[0] = getSelectedLanguageCode(languageConverter, languageComboBox);
                        updateTranslation(translator, resultLabel, selectedCountryCode[0], selectedLanguageCode[0]);
                    }
                }
            });

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
                    selectedCountryCode[0] = getSelectedCountryCode(countryConverter, list);
                    updateTranslation(translator, resultLabel, selectedCountryCode[0], selectedLanguageCode[0]);
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

            selectedCountryCode[0] = getSelectedCountryCode(countryConverter, list);
            selectedLanguageCode[0] = getSelectedLanguageCode(languageConverter, languageComboBox);
        });
    }

    private static void updateTranslation(Translator translator,
                                          JLabel resultLabel,
                                          String countryCode,
                                          String languageCode) {
        String result = translator.translate(countryCode, languageCode);
        if (result == null) {
            result = "no translation found!";
        }
        resultLabel.setText(result);
    }


    private static String getSelectedCountryCode(CountryCodeConverter countryConverter, JList<String> countryList) {
        String selectedCountry = countryList.getSelectedValue();
        return countryConverter.fromCountry(selectedCountry);
    }

    private static String getSelectedLanguageCode(LanguageCodeConverter languageConverter, JComboBox<String> languageComboBox) {
        String selectedLanguage = languageComboBox.getSelectedItem().toString();
        return languageConverter.fromLanguage(selectedLanguage);
    }
}
