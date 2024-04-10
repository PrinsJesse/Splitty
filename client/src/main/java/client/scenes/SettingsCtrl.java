package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.Currency;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;

import javax.inject.Inject;

public class SettingsCtrl {
    private ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private final Config config;

    private final Mail mail;
    private boolean noConnection = false;
    @FXML
    public Button cancelButton;
    @FXML
    public Button saveButton;
    @FXML
    private TextField emailField;
    @FXML
    public TextField currencyField;
    @FXML
    private TextField langTextfield;
    @FXML
    private Button changServerButton;
    @FXML
    private Label language;
    @FXML
    private Label languageText;
    @FXML
    private Label langInstructions;
    @FXML
    private Label addLangText;
    @FXML
    private Button addLanguage;
    @FXML
    private Label currency;
    @FXML
    private Label settingsText;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private TextField nameField;
    @FXML
    private TextField ibanField;
    @FXML
    private TextField bicField;
    @FXML
    private Label labelEmailToken;
    @FXML
    private TextField getToken;
    @FXML
    private Button sendEmail;
    @FXML
    private Label succes;



    @Inject
    public SettingsCtrl(MainCtrl mainCtrl, Config config, Mail mail){
        this.mainCtrl = mainCtrl;
        this.config = config;
        this.mail = mail;
    }
    public void initialize() {
        mainCtrl.setButtonRedProperty(cancelButton);
        mainCtrl.setButtonGreenProperty(saveButton);
    }

    public void setLabelEmailToken(String txt){
        labelEmailToken.setText(txt);
    }

    public void setSucces(String txt){
        succes.setText(txt);
    }

    public void setSendEmail(String txt){
        sendEmail.setText(txt);
    }

    /**
     * sets all the fields to the values obtained by the config file
     */
    public void initializeFields(boolean noConnection) {
        this.noConnection = noConnection;
        if (config.getEmail() != null) {
            emailField.setText(config.getEmail());
        } else {
            emailField.setText("");
        }
        if (config.getName() != null) {
            nameField.setText(config.getName());
        } else {
            nameField.setText("");
        }
        if (config.getIban() != null) {
            ibanField.setText(config.getIban());
        } else {
            ibanField.setText("");
        }
        if (config.getBic() != null) {
            bicField.setText(config.getBic());
        } else {
            bicField.setText("");
        }
        if (config.getEmailToken() != null){
            getToken.setText(config.getEmailToken());
        } else {
            getToken.setText("");
        }
        currencyField.setText(config.getCurrency().toString());
        languageText.setText(config.getLanguage());
    }

    /**
     * The method correlated to the save settings button. Every field is retrieved and if nothing is
     * incorrect everything will be saved by writing it to the config file.
     */
    public void saveSettings() {
        String email = emailField.getText();
        String currency = currencyField.getText();
        String name = nameField.getText();
        String iban  = ibanField.getText();
        String bic = bicField.getText();
        String emailToken = getToken.getText();
        boolean abort = false;
        if (email == null || email.isEmpty()) {
            email = null;
        }
        if (currency == null || (!currency.equals("EUR") &&
                !currency.equals("CHF") && !currency.equals("USD"))) {
            abort = true;
            // set error message
        }
        if (abort) {
            return;
        }
        config.setEmailToken(emailToken);
        config.setEmail(email);
        config.setCurrency(Currency.valueOf(currency));
        config.setName(name);
        config.setIban(iban);
        config.setBic(bic);
        config.write();
        if (noConnection) {
            mainCtrl.showServerStartup(true);
        } else {
            back();
            mainCtrl.setConfirmationSettings();
        }

    }

    public void back() {
        succes.setVisible(false);
        mainCtrl.showStartScreen();
    }

    public void initializeConfig() {
        config.read();
    }
    public String getEmail() {
        return config.getEmail();
    }
    public String getId() {
        return config.getId();
    }

    public String getConnection() {
        return config.getConnection();
    }

    public String getName() {
        return config.getName();
    }

    public String getIban() {
        return config.getIban();
    }

    public String getBic() {
        return config.getBic();
    }

    @FXML
    public void addLang(){
        progressBar.setVisible(true);
        String newLang = langTextfield.getText();
        if(newLang != null || !newLang.isBlank()){
            //setLanguage to new found language, we can no longer use an enum
            if(mainCtrl.languages.contains(newLang)){
                langTextfield.setPromptText("This language already exists");
                langTextfield.setText("");
                return;
            }
            try{
                mainCtrl.changeLanguage(newLang);
                mainCtrl.languages.add(newLang);
                mainCtrl.language = newLang;
                langTextfield.setText("");
            }catch (Exception e){
                progressBar.setVisible(false);
                langTextfield.setText("no valid languageCode");
                System.out.println(e);
            }
        }
        progressBar.setVisible(false);
    }

    public void sendDefaultEmail(){
        try{
            String fromEmail = config.getEmail();
            String toEmail = config.getEmail();
            String passwordToken = config.getEmailToken();
            String host = "smtp.gmail.com";
            String emailSubject = "configuration email splitty";
            String emailBody = toStringBody(fromEmail, passwordToken);
            int port = 587;
            Mailer mailer = mail.getSenderInfo(host, port, fromEmail, passwordToken);
            Email email = mail.makeEmail(fromEmail, toEmail, emailSubject, emailBody);
            mail.mailSending(email, mailer);
            System.out.println("email has been send correctly");
            succes.setVisible(true);
        } catch (RuntimeException e){
            getToken.setText("password does not match the email");
            System.out.println("could not make a Mailer");
            e.printStackTrace();
        } catch (Exception e){
            emailField.setText("something wrong with email");
        }

    }

    public String getLanguage() {
        return config.getLanguage().toString();
    }

    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            back();
        }
        KeyCodeCombination k = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        if (k.match(press)) {
            saveSettings();
        }
    }

    public void changeServer() {
        mainCtrl.showServerStartup(noConnection);
    }

    public void setCancelButton(String txt) {
        this.cancelButton.setText(txt);
    }

    public void setSaveButton(String txt) {
        this.saveButton.setText(txt);
    }

    public void setLanguage(String txt) {
        this.language.setText(txt);
    }

    public void setLanguageText(String txt) {
        this.languageText.setText(txt);
    }

    public void setLangInstructions(String txt) {
        this.langInstructions.setText(txt);
    }

    public void setAddLangText(String txt) {
        this.addLangText.setText(txt);
    }

    public void setAddLanguage(String txt) {
        this.addLanguage.setText(txt);
    }

    public void setCurrency(String txt) {
        this.currency.setText(txt);
    }

    public void setSettingsText(String txt) {
        this.settingsText.setText(txt);
    }
    public void setChangServerButton(String txt){
        this.changServerButton.setText(txt);
    }

    public String toStringBody(String fromEmail, String passwordToken){
        String s = "This email is from splitty. We would like to tell " +
                "you that your email and credentials are set up correctly." +
                "\n \n" +
                "Your credetials are:\n" +
                "email: " + fromEmail + "\n" +
                "password token: " + passwordToken +
                "\n \n" +
                "From now on you can this email to send invites or " +
                "send payment invitations." +
                "\n \n" +
                "sincerly, Team Splitty";

        return s;
    }

}
