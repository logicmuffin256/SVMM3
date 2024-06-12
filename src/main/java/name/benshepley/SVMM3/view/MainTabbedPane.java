package name.benshepley.SVMM3.view;


import name.benshepley.SVMM3.model.application.event.SyncWithFileSystemEvent;
import name.benshepley.SVMM3.model.filesystem.ProfileFileSystemModel;
import name.benshepley.SVMM3.view.service.UiComponentSpringPrototypeFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class MainTabbedPane extends JTabbedPane {

    // Spring Beans:
    private final UiComponentSpringPrototypeFactory uiComponentSpringPrototypeFactory;

    // Listeners:
    @EventListener
    public void onApplicationEvent(SyncWithFileSystemEvent syncWithFileSystemEvent) {
        for (ProfileFileSystemModel profileFileSystemModel : syncWithFileSystemEvent.getApplicationSyncStateModel().getProfileFileSystem()) {
            var profileTabPanel = this.uiComponentSpringPrototypeFactory.produceProfileTabPanel(profileFileSystemModel);
            super.insertTab(profileFileSystemModel.getName(), null, profileTabPanel, null, 0);
            super.setSelectedIndex(0);
        }
    }


    public MainTabbedPane(UiComponentSpringPrototypeFactory uiComponentSpringPrototypeFactory) {
        super.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        this.uiComponentSpringPrototypeFactory = uiComponentSpringPrototypeFactory;

        JPanel addPanel = new JPanel();
        super.insertTab("➕", null, addPanel, null, 0);
        super.addChangeListener(e -> {
            if (super.getSelectedIndex() == super.getTabCount() - 1) {
                // TODO:
                System.out.println("IT is time, captain.");
            }
        });
    }


}
