package de.jonas.spring.tabcontent;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import de.jonas.spring.model.*;
import de.jonas.spring.model.skills.MagicalOrResonanceSkill;
import de.jonas.spring.model.skills.MagicalSkillGroup;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class MagicOrResonanceContent extends VerticalLayout {
    private Grid<MagicalOrResonanceSkill> magicalSkillGrid;
    private Label noMagicOrResonanceHint = new Label("Dein Charakter kann keine Magie oder Resonanz.");
    private ComboBox<MagicalSkillGroup> magicalSkillGroupComboBox;
    private ComboBox<MagicalOrResonanceSkill> skillComboBox;

    public MagicOrResonanceContent(Binder<PlayerCharacter> binder) {
        build(binder);
        updateVisibility(binder);
        binder.addValueChangeListener(event -> updateVisibility(binder));
    }

    private void updateVisibility(Binder<PlayerCharacter> binder) {
        AwokenType awokenType = binder.getBean().getAwokenType();

        noMagicOrResonanceHint.setVisible(awokenType == null);

        magicalSkillGroupComboBox.setVisible(awokenType == AwokenType.ASPECT_WIZARD);

        magicalSkillGrid.setVisible(awokenType != null && awokenType != AwokenType.ASPECT_WIZARD);
        magicalSkillGrid.setItems(binder.getBean().getMagicalOrResonanceSkills());

        skillComboBox.setVisible(awokenType != null && awokenType != AwokenType.ASPECT_WIZARD);
        skillComboBox.setEnabled(canAddMoreSkills(binder));
        skillComboBox.setItems(EnumSet.allOf(MagicalOrResonanceSkill.class).stream().filter(skill -> (awokenType == AwokenType.TECHNOMANCER) == skill.isResonanceSkill()));
        skillComboBox.setLabel(awokenType == AwokenType.TECHNOMANCER ? "Resonanzfähigkeiten" : "Magische Fähigkeiten");
    }

    private void build(Binder<PlayerCharacter> binder) {
        add(noMagicOrResonanceHint);

        magicalSkillGroupComboBox = new ComboBox<>();
        magicalSkillGroupComboBox.setLabel("Fertigkeitsgruppe");
        magicalSkillGroupComboBox.setItems(EnumSet.allOf(MagicalSkillGroup.class));
        binder.bind(magicalSkillGroupComboBox, PlayerCharacter::getMagicalSkillGroup, PlayerCharacter::setMagicalSkillGroup);
        add(magicalSkillGroupComboBox);

        addMagicalSkillSelector(binder);
    }

    private <E extends Enum<E>> void addMagicalSkillSelector(Binder<PlayerCharacter> binder) {
        magicalSkillGrid = new Grid<>();
        skillComboBox = new ComboBox<>();

        magicalSkillGrid.setSelectionMode(Grid.SelectionMode.NONE);
        magicalSkillGrid.addColumn(MagicalOrResonanceSkill::toString).setHeader("Fähigkeit");
        magicalSkillGrid.addComponentColumn(magicalSkill -> {
            ComboBox<Integer> comboBox = new ComboBox<>();
            MagicOrResonance magicOrResonance = binder.getBean().getPriority(Prioritizable.MAGIC_OR_RESONANZ).getMagicOrResonance(binder.getBean().getAwokenType());
            int baseValue = binder.getBean().getAwokenType() == AwokenType.TECHNOMANCER ? magicOrResonance.getResonanceAbilityLevels() : magicOrResonance.getMagicalAbilityLevels();
            comboBox.setItems(Collections.singletonList(baseValue));
            comboBox.setValue(baseValue);
            return comboBox;
        }).setHeader("Stufe");
        magicalSkillGrid.addComponentColumn(magicalSkill -> {
            Button removeButton = new Button("Entfernen");
            removeButton.addClickListener(event -> {
                binder.getBean().getAllMagicalOrResonanceSkills().remove(magicalSkill);
                List<MagicalOrResonanceSkill> magicalSkills = binder.getBean().getMagicalOrResonanceSkills();
                magicalSkillGrid.setItems(magicalSkills);
                skillComboBox.setItems(EnumSet.allOf(MagicalOrResonanceSkill.class).stream().filter(magicalSkills::contains).collect(Collectors.toList()));
                skillComboBox.setEnabled(canAddMoreSkills(binder));
            });
            return removeButton;
        });

        skillComboBox.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                binder.getBean().getAllMagicalOrResonanceSkills().add(event.getValue());
                List<MagicalOrResonanceSkill> skills = binder.getBean().getMagicalOrResonanceSkills();
                magicalSkillGrid.setItems(skills);
                skillComboBox.setItems(EnumSet.allOf(MagicalOrResonanceSkill.class).stream().filter(skill -> (binder.getBean().getAwokenType() == AwokenType.TECHNOMANCER) == skill.isResonanceSkill()).filter(element -> !skills.contains(element)));
                skillComboBox.setEnabled(canAddMoreSkills(binder));
            }
        });

        add(skillComboBox);
        add(magicalSkillGrid);
    }

    private boolean canAddMoreSkills(Binder<PlayerCharacter> binder) {
        Priority priority = binder.getBean().getPriority(Prioritizable.MAGIC_OR_RESONANZ);
        if (priority == null) {
            return false;
        }
        int selectedSkills = binder.getBean().getMagicalOrResonanceSkills().size();
        MagicOrResonance magicOrResonance = priority.getMagicOrResonance(binder.getBean().getAwokenType());
        if (binder.getBean().getAwokenType() == AwokenType.TECHNOMANCER) {
            return selectedSkills < magicOrResonance.getResonanceAbilities();
        } else {
            return selectedSkills < magicOrResonance.getMagicalAbilities();
        }
    }
}
