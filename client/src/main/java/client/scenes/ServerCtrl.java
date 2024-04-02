package client.scenes;

import client.MyFXML;
import client.MyModule;
import client.utils.AdminWindows;
import client.utils.Config;
import client.utils.EventPropGrouper;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import static com.google.inject.Guice.createInjector;

import javax.inject.Inject;


public class ServerCtrl {
    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    private Config config;

    boolean startup = false;
    @FXML
    public Label startupNotification;
    @FXML
    public Label notConnectedError;
    @FXML
    public ImageView imageView;
    @FXML
    public TextField serverField;
    @FXML
    public Button connectButton;
    @FXML
    public Button backButton;

    @Inject
    public ServerCtrl(MainCtrl mainCtrl, Config config) {
        this.mainCtrl = mainCtrl;
        this.config = config;
    }


    public void setField(boolean startup) {
        this.startup = startup;
        if (config.getConnection() != null) {
            serverField.setText(config.getConnection());
        }
        if (startup) {
            startupNotification.setVisible(true);
            imageView.setImage(new Image("no-connection.png"));
            backButton.setText("Settings");
        } else {
            startupNotification.setVisible(false);
            imageView.setImage(new Image("connection2.png"));
            backButton.setText("Back");
        }
    }

    public void connect() {
        notConnectedError.setVisible(false);
        try {
            ServerUtils.serverDomain = serverField.getText();
            ServerUtils.resetServer();
            serverUtils = new ServerUtils();
            config.setConnection(serverField.getText());
            config.write();
            relaunch();
            mainCtrl.closeStage();
            startup = false;
        } catch (RuntimeException e) {
            notConnectedError.setVisible(true);
        }
    }

    private void relaunch() throws RuntimeException {
        Injector INJECTOR = createInjector(new MyModule());
        MyFXML FXML = new MyFXML(INJECTOR);
        var server = FXML.load(ServerCtrl.class, "client", "scenes", "Server.fxml");
        var settings = FXML.load(SettingsCtrl.class, "client", "scenes", "Settings.fxml");
        var invitation = FXML.load(InvitationCtrl.class, "client", "scenes", "Invitation.fxml");
        var splittyOverview = FXML.load(SplittyOverviewCtrl.class, "client", "scenes", "SplittyOverview.fxml");
        var startScreen = FXML.load(StartScreenCtrl.class, "client", "scenes", "StartScreen.fxml");
        var contactDetails = FXML.load(ContactDetailsCtrl.class, "client", "scenes", "ContactDetails.fxml");
        var userEventList = FXML.load(UserEventListCtrl.class, "client", "scenes", "UserEventList.fxml");
        var createEvent = FXML.load(CreateEventCtrl.class, "client", "scenes", "createEvent.fxml");
        var addExpense = FXML.load(AddExpenseCtrl.class, "client", "scenes", "AddExpense.fxml");
        var manageParticipants = FXML.load(ManageParticipantsCtrl.class, "client", "scenes", "ManageParticipants.fxml");
        var statistics = FXML.load(StatisticsCtrl.class, "client", "scenes", "Statistics.fxml");
        var debts = FXML.load(DebtCtrl.class, "client", "scenes", "Debts.fxml");
        var editEvent = FXML.load(EditEventCrtl.class, "client", "scenes", "EditEvent.fxml");
        var editExpense = FXML.load(EditExpenseCtrl.class, "client", "scenes", "EditExpense.fxml");
        // group these in the EventPropGrouper
        var eventPropGrouper = new EventPropGrouper(addExpense, manageParticipants,
                statistics, debts, editEvent, editExpense);

        var adminLogin = FXML.load(AdminLoginCtrl.class, "client", "scenes", "AdminLogin.fxml");
        var adminOverview = FXML.load(AdminOverviewCtrl.class, "client", "scenes", "AdminOverview.fxml");
        var adminWindows = new AdminWindows(adminLogin, adminOverview);
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(new Stage(), invitation,splittyOverview,
                startScreen, contactDetails, eventPropGrouper, userEventList, createEvent, adminWindows, settings, server);

    }

    public void back() {
        mainCtrl.showSettings(startup);
    }
    public void resetError(KeyEvent keyEvent) {
        notConnectedError.setVisible(false);
    }
}
