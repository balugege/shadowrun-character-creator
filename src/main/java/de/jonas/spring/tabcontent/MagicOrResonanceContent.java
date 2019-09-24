package de.jonas.spring.tabcontent;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.binder.Binder;
import de.jonas.spring.model.AwokenType;
import de.jonas.spring.model.PlayerCharacter;

import javax.annotation.Nullable;

public class MagicOrResonanceContent extends FormLayout {
    public MagicOrResonanceContent(Binder<PlayerCharacter> binder) {
        binder.addValueChangeListener(event -> {
            build(binder.getBean().getAwokenType());
        });
        build(null);
    }

    private void build(@Nullable AwokenType awokenType) {
        removeAll();
        if (awokenType == null) {
            add(new Label("Dein Charakter kann keine keine Magie oder Resonanz"));
            return;
        }
        add(new Label(awokenType.toString()));
    }
}
