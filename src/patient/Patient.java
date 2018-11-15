package patient;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 *
 * @author plaza
 */
public class Patient implements Serializable{
private static final long serialVersionUID = -6291904286218553733L;
//claudia 
//saiz 
    private final String name;
    private final int age;
    private final float weight;
    private final float height;
    private final LocalDateTime monitoring;
    private final String[] signs;
    private final String[] symptoms;
    
    public Patient (String namePatient, int agePatient, float weightPatient, float heightPatient, LocalDateTime monitoringData, String[] sign, String[] symptom)  {
        this.name = namePatient;
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
