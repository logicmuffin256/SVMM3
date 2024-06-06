package name.benshepley.SVMM3.view.component.panel;

import name.benshepley.SVMM3.controller.MainController;
import name.benshepley.SVMM3.model.application.settings.ModSettingsModel;
import name.benshepley.SVMM3.model.application.settings.ProfileSettingsModel;
import net.miginfocom.swing.MigLayout;
import org.springframework.context.ApplicationEventPublisher;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import java.util.Comparator;
import java.util.stream.Stream;

public class ProfileTabPanel extends JPanel {
    // Spring Beans:
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ProfileSettingsModel profileSettingsModel;

    // Components:
    private final JTable modsTable;

    private final JButton configureModButton;
    private final JButton viewModManifestButton;
    private final JButton browseToModFolderButton;
    private final JButton browseToModRepositoryButton;
    private final JButton copyModButton;
    private final JButton pasteModButton;
    private final JButton deleteModButton;


    public ProfileTabPanel(ApplicationEventPublisher applicationEventPublisher, ProfileSettingsModel profileSettingsModel) {
        super(new MigLayout("wrap 4", "[grow, fill]", "[grow, fill]"));
        super.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        this.applicationEventPublisher = applicationEventPublisher;
        this.profileSettingsModel = profileSettingsModel;

        this.configureModButton = new JButton("Edit Mod Configuration");
        this.configureModButton.setEnabled(false);
        this.viewModManifestButton = new JButton("View Mod Manifest");
        this.viewModManifestButton.setEnabled(false);
        this.browseToModFolderButton = new JButton("Browse to Mod Folder");
        this.browseToModFolderButton.setEnabled(false);
        this.browseToModRepositoryButton = new JButton("Browse to Mod Repository");
        this.browseToModRepositoryButton.setEnabled(false);
        this.copyModButton = new JButton("Copy Mod(s)");
        this.copyModButton.setEnabled(false);
        this.pasteModButton = new JButton("Paste Mod(s)");
        this.pasteModButton.setEnabled(false);
        this.deleteModButton = new JButton("Delete Mod(s)");
        this.deleteModButton.setEnabled(false);

        Object[][] data = Stream.concat(profileSettingsModel.getEnabledMods().stream(), profileSettingsModel.getDisabledMods().stream())
                .sorted(Comparator.comparing(ModSettingsModel::getName))
                .map(d -> new Object[]{d.getEnabled(), d.getName(), d.getInstalledVersion(), d.getNotes()})
                .toArray(Object[][]::new);

        DefaultTableModel model = new DefaultTableModel(data, new Object[]{ "Enabled", "Name", "Version", "Notes" }) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                } else {
                    return String.class;
                }
            }
        };

        this.modsTable = new JTable(model);
        JScrollPane modsTableScrollPane = new JScrollPane(this.modsTable);

        this.modsTable.getColumnModel().getColumn(0).setPreferredWidth(25);
        this.modsTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        this.modsTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        this.modsTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                this.configureModButton.setEnabled(true);
                this.viewModManifestButton.setEnabled(true);
                this.browseToModFolderButton.setEnabled(true);
                this.browseToModRepositoryButton.setEnabled(true);
                this.copyModButton.setEnabled(true);
                this.pasteModButton.setEnabled(true);
                this.deleteModButton.setEnabled(true);
            }
        });

        JButton playStardewWithSMAPIButton = new JButton("Play Stardew (With SMAPI)");
        playStardewWithSMAPIButton.addActionListener(a -> this.applicationEventPublisher.publishEvent(new MainController.ExecuteProcessEvent(this, "")));

        JButton playStardewWithoutSMAPIButton = new JButton("Play Stardew (Without SMAPI)");

        super.add(modsTableScrollPane, "span 3 7");

        super.add(configureModButton, "wrap");
        super.add(viewModManifestButton, "wrap");
        super.add(browseToModFolderButton, "wrap");
        super.add(browseToModRepositoryButton, "wrap");
        super.add(copyModButton, "wrap");
        super.add(pasteModButton, "wrap");
        super.add(deleteModButton, "wrap");

        super.add(playStardewWithSMAPIButton, "span 2");
        super.add(playStardewWithoutSMAPIButton, "span 2, wrap");


    }

}