package de.jonas.spring.tabcontent;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import de.jonas.spring.model.PlayerCharacter;
import de.jonas.spring.model.RunnerLevel;


public class ConceptContent extends FormLayout {
    public ConceptContent(PlayerCharacter playerCharacter) {
        addNameField(playerCharacter);
        addExperienceComboBox(playerCharacter);
    }

    private void addNameField(PlayerCharacter playerCharacter) {
        TextField nameField = new TextField("Name");
        Binder<PlayerCharacter> binder = new Binder<>();
        binder.bind(nameField, PlayerCharacter::getName, PlayerCharacter::setName);
        binder.setBean(playerCharacter);

        add(nameField);
    }

    private void addExperienceComboBox(PlayerCharacter playerCharacter) {
        RadioButtonGroup<RunnerLevel> radioButton = new RadioButtonGroup<>();
        radioButton.setItems(RunnerLevel.values());
        radioButton.setLabel("Erfahrung");
        Label experienceDescription = new Label();
        radioButton.addValueChangeListener(event -> {
            playerCharacter.setRunnerLevel(event.getValue());
        });
        radioButton.setValue(playerCharacter.getRunnerLevel());

        add(radioButton);
        add(experienceDescription);
    }
}
