/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package patient;

import java.io.Serializable;

/**
 *
 * @author plaza
 */
public class Patient implements Serializable{
private static final long serialVersionUID = -6291904286218553733L;

    private String name;
    private int age;
    private float weight;
    
    public Patient (String namePatient, int agePatient, float weightPatient) {
        this.name = namePatient;
        this.age = agePatient;
        this.weight = weightPatient;
    }

    @Override
    public String toString() {
        return ("Name: " + name + "\nAge: " + age
                + "\nWeight: " + weight);
    }
    
    
}
