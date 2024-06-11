package name.benshepley.SVMM3.view.component.dialog;

import name.benshepley.SVMM3.controller.ApplicationSettingsController;
import name.benshepley.SVMM3.controller.ProfileController;
import name.benshepley.SVMM3.model.application.ui.PopupConfigurationModel;
import name.benshepley.SVMM3.model.filesystem.ProfileFileSystemModel;
import name.benshepley.SVMM3.view.MainFrame;
import name.benshepley.SVMM3.view.service.UiComponentSpringPrototypeFactory;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProfileSettingsDialog extends javax.swing.JDialog {
    /* Spring Beans: */
    private final UiComponentSpringPrototypeFactory uiComponentSpringPrototypeFactory;
    private final ProfileController profileController;

    /* Swing Components: */
    private final JTextField profileNameTextField;
    private final JButton saveButton;
    private final JButton deleteButton;
    private final JButton cancelButton;

    private ProfileFileSystemModel profileFileSystemModel;

    @Autowired
    public ProfileSettingsDialog(MainFrame parent, UiComponentSpringPrototypeFactory uiComponentSpringPrototypeFactory, ApplicationSettingsController applicationSettingsController, ProfileController profileController) {
        super(parent, "Profile Settings", true);
        super.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        this.uiComponentSpringPrototypeFactory = uiComponentSpringPrototypeFactory;
        this.profileController = profileController;

        /* Setup UI Components: */
        JLabel profileNameLabel = new JLabel("Profile Name");
        this.profileNameTextField = new JTextField(50);

        this.deleteButton = new JButton("Delete");
        this.saveButton = new JButton("Save");
        this.cancelButton = new JButton("Cancel");

        /* Layout: */
        super.setLayout(new MigLayout("wrap 3", "[grow,fill]", "[grow,fill]"));
        super.add(profileNameLabel);
        super.add(profileNameTextField, "span 2");

        super.add(deleteButton);
        super.add(saveButton);
        super.add(cancelButton);
    }

    public void loadSettings(ProfileFileSystemModel profileFileSystemModel) {
        this.profileFileSystemModel = profileFileSystemModel;

        if (this.profileFileSystemModel.getName().isBlank()) {
            /* Setup Buttons: */
            this.cancelButton.setEnabled(false);
            this.deleteButton.setEnabled(false);

            this.saveButton.addActionListener(a -> {
                this.profileFileSystemModel.setName(this.profileNameTextField.getText());
                if (this.validateForm()) {
                    super.dispose();
                    this.profileController.createProfile(this.profileFileSystemModel.getName());
                }
            });

            this.setupForFirstTime();
        } else {
            /* Setup Dialog: */
            this.profileNameTextField.setText(this.profileFileSystemModel.getName());
            /* Setup Buttons: */
            this.cancelButton.addActionListener(a -> super.dispose());
            this.deleteButton.addActionListener(a -> {
                super.dispose();
                this.profileController.deleteProfile(this.profileFileSystemModel.getName());
            });
            this.saveButton.addActionListener(a -> {
                if (this.validateForm()) {
                    super.dispose();
                    var profileDirectoryFromTextField = this.profileNameTextField.getText();
                    var profileDirectoryFromModel = this.profileFileSystemModel.getName();

                    if (!profileDirectoryFromTextField.equals(profileDirectoryFromModel)) {
                        this.profileController.moveProfile(profileDirectoryFromModel, profileDirectoryFromTextField);
                    }
                }
            });
        }
    }

    public void setupForFirstTime() {
        this.cancelButton.setEnabled(false);
        this.deleteButton.setEnabled(false);
        this.profileNameTextField.setText("Initial Profile");

        this.saveButton.addActionListener(e -> {

        });
    }

    private boolean validateForm() {
        if (this.profileNameTextField.getText() == null || this.profileNameTextField.getText().isBlank()) {
            this.uiComponentSpringPrototypeFactory.showPopupDialog(
                    PopupConfigurationModel.builder()
                            .title("Profile Name Required")
                            .message("Profile name is required.")
                            .build()
            );
            return false;
        }
        return true;
    }
}
