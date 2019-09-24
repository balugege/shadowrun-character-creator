package de.jonas.spring.tabcontent;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.binder.Binder;
import de.jonas.spring.model.AwokenType;
import de.jonas.spring.model.MagicalSkillGroup;
import de.jonas.spring.model.PlayerCharacter;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class MagicOrResonanceContent extends FormLayout {
    public MagicOrResonanceContent(Binder<PlayerCharacter> binder) {
        binder.addValueChangeListener(event -> {
            build(binder.getBean().getAwokenType(), binder);
        });
        build(null, binder);
    }

    private void build(@Nullable AwokenType awokenType, Binder<PlayerCharacter> binder) {
        removeAll();
        if (awokenType == null) {
            add(new Label("Dein Charakter kann keine Magie oder Resonanz."));
            return;
        }

        switch (awokenType) {
            case ASPECT_WIZARD:
                ComboBox<MagicalSkillGroup> magicalSkillGroupComboBox = new ComboBox<>();
                magicalSkillGroupComboBox.setLabel("Fertigkeitsgruppe");
                magicalSkillGroupComboBox.setItems(EnumSet.allOf(MagicalSkillGroup.class));
                binder.bind(magicalSkillGroupComboBox, PlayerCharacter::getMagicalSkillGroup, PlayerCharacter::setMagicalSkillGroup);
                add(magicalSkillGroupComboBox);
                break;
        }
    }
}
