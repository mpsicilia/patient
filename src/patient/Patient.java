package patient;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author plaza
 */
public class Patient implements Serializable {

    private static final long serialVersionUID = -6291904286218553733L;

    private String name;
    private String surname;
    private int age;
    private float weight;
    private float height;
    private LocalDateTime monitoring;
    private String signs_symptoms;
    public int[][] databitalino = new int[2][10000];
    private String username;
    private String passwordPat;
    private boolean sevheadache;
    private boolean fatigue;
    private boolean chestpain;
    private boolean difficultybreath;
    private boolean irregheartbeat;

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setMonitoring(LocalDateTime monitoring) {
        this.monitoring = monitoring;
    }

    public void setDatabitalino(int[][] databitalino) {
        this.databitalino = databitalino;
    }

    public void setSevheadache(boolean sevheadache) {
        this.sevheadache = sevheadache;
    }

    public void setFatigue(boolean fatigue) {
        this.fatigue = fatigue;
    }

    public void setChestpain(boolean chestpain) {
        this.chestpain = chestpain;
    }

    public void setBreath(boolean difficultybreath) {
        this.difficultybreath = difficultybreath;
    }

    public void setTachychardia(boolean irregheartbeat) {
        this.irregheartbeat = irregheartbeat;
    }

    public String getSigns_symptoms() {
        return signs_symptoms;
    }

    public boolean isSevheadache() {
        return sevheadache;
    }

    public boolean isFatigue() {
        return fatigue;
    }

    public boolean isChestpain() {
        return chestpain;
    }

    public boolean isBreath() {
        return difficultybreath;
    }

    public boolean isTachychardia() {
        return irregheartbeat;
    }
 

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

    public String getUsername() {
        return username;
    }

    public String getPasswordPat() {
        return passwordPat;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordPat(String passwordPat) {
        String chain="";
        for(int i=passwordPat.length()-1; i>=0; i--){
            chain = chain + passwordPat.charAt(i);
        }
       this.passwordPat = chain;
    }
    

    public LocalDateTime getMonitoring() {
        return monitoring;
    }

    public String getSigns() {
        return signs_symptoms;
    }



    public void setDataBitalino(int[][] databitalino) {
        this.databitalino = databitalino;
    }

    public Patient(){
    }
    
    public Patient(String username, String namePatient, String surnameP, int agePatient, float weightPatient, float heightPatient, LocalDateTime monitoringData,boolean sevheadache,  boolean fatigue, boolean chestpain, boolean difficultybreath, boolean irregheartbeat, String signs_symptoms) {
        this.username = username;
        this.name = namePatient;
        this.surname = surnameP;
        this.age = agePatient;
        this.weight = weightPatient;
        this.height = heightPatient;
        this.monitoring = monitoringData;
        this.sevheadache = sevheadache;
        this.fatigue = fatigue;
        this.chestpain=chestpain;
        this.difficultybreath=difficultybreath;
        this.irregheartbeat=irregheartbeat;
        this.signs_symptoms = signs_symptoms;
      
    }

    public Patient(String username, float weightPatient, LocalDateTime monitoringData,boolean sevheadache,  boolean fatigue, boolean chestpain, boolean difficultybreath, boolean irregheartbeat,String signs_symptoms) {
      
        this.username = username;
        this.weight = weightPatient;
        this.monitoring = monitoringData;
        this.sevheadache = sevheadache;
        this.fatigue = fatigue;
        this.chestpain=chestpain;
        this.difficultybreath=difficultybreath;
        this.irregheartbeat=irregheartbeat;
        this.signs_symptoms = signs_symptoms;
  
    }

    public int[][] getDatabitalino() {
        return databitalino;
    }

    @Override
    public String toString() {
        return (name + " " + surname + "\nAge: " + age + "\nWeight: " + weight + "\nHeight: " + height + "\nMonitoring: " + monitoring + "\nSevere headache: "+ sevheadache+ "\nchestpain: "+ chestpain+ "\nfatigue: "+ fatigue +"\nirregheartbeat: "+ irregheartbeat+"\ndifficultybreath: "+ difficultybreath+"\nAdditional information: " + signs_symptoms);
    }

    public static boolean RegularExp(String name) {
        String reggex = "^[a-zA-Z ]*$";
        Pattern pat = Pattern.compile(reggex);
        Matcher match = pat.matcher(name);
        Boolean itCoincides = match.find();
        return itCoincides;
    }
}
