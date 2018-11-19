package patient;


import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 *
 * @author plaza
 */
public class Patient implements Serializable{
private static final long serialVersionUID = -6291904286218553733L;

    private final String name;
    private final String surname;
    private final int age;
    private final float weight;
    private final float height;
    private final LocalDateTime monitoring;
    //simpledata format
    //
    private final String[] signs;
    private final String[] symptoms;
   // private final File folder;
    public String[] databitalino = new String[2];
    //private final File[] signs_symptoms;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }

    public float getWeight() {
        return weight;
    }

    public float getHeight() {
        return height;
    }

    public LocalDateTime getMonitoring() {
        return monitoring;
    }

    public String[] getSigns() {
        return signs;
    }

    public String[] getSymptoms() {
        return symptoms;
    }
    
    public void setDataBitalino(String[] datab){
        this.databitalino = datab;
    }
    
    public Patient (String namePatient, String surnameP, int agePatient, float weightPatient, float heightPatient, LocalDateTime monitoringData, String[] sign, String[] symptom)  {
        this.name = namePatient;
        this.surname = surnameP;
        this.age = agePatient;
        this.weight = weightPatient;
        this.height = heightPatient;
        this.monitoring = monitoringData;
        this.signs = sign;
        this.symptoms = symptom;
    }
    
    @Override
    public String toString() {
        return ("Name: " + name + "\nAge: " + age+ "\nWeight: " + weight+ "\nHeight: " +height+ "\nMonitoring: " + monitoring+ "\nSings: "+Arrays.toString(signs)+ "\nSymptoms: "+Arrays.toString(symptoms));
    } 
}
