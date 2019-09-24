package de.jonas.spring.tabcontent;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import de.jonas.spring.model.PlayerCharacter;
import de.jonas.spring.model.RunnerLevel;


public class ConceptContent extends FormLayout {
    public ConceptContent(Binder<PlayerCharacter> binder) {
        addNameField(binder);
        addExperienceComboBox(binder);
    }

    private void addNameField(Binder<PlayerCharacter> binder) {
        TextField nameField = new TextField("Name");
        binder.bind(nameField, PlayerCharacter::getName, PlayerCharacter::setName);
        add(nameField);
    }

    private void addExperienceComboBox(Binder<PlayerCharacter> binder) {
        RadioButtonGroup<RunnerLevel> radioButton = new RadioButtonGroup<>();
        radioButton.setItems(RunnerLevel.values());
        radioButton.setLabel("Erfahrung");
        Label experienceDescription = new Label();
        radioButton.addValueChangeListener(event -> binder.getBean().setRunnerLevel(event.getValue()));
        radioButton.setValue(binder.getBean().getRunnerLevel());

        add(radioButton);
        add(experienceDescription);
    }
}
