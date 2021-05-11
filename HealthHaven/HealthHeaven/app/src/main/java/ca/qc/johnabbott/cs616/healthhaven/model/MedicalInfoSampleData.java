package ca.qc.johnabbott.cs616.healthhaven.model;

import java.util.ArrayList;
import java.util.List;

public class MedicalInfoSampleData {
    public static List<MedicalInfo> medicalInfo;

    public static List<MedicalInfo> getMedicalConditionData(){
        medicalInfo = new ArrayList<>();

        medicalInfo.add(new MedicalInfo(1, MedicalInfo.Type.MEDICAL_CONDITION, "Diabetes", "Type 2. Should limit sugar counts in the blood stream"));
        medicalInfo.add(new MedicalInfo(2, MedicalInfo.Type.MEDICAL_CONDITION, "High Blood Pressure", "Avoid abundance of salt"));
        medicalInfo.add(new MedicalInfo(3, MedicalInfo.Type.MEDICAL_CONDITION, "Epilepsy", "Must avoid flickering lights"));
        medicalInfo.add(new MedicalInfo(4, MedicalInfo.Type.MEDICAL_CONDITION, "Semi Blind", "Cannot see beyond 5ft"));
        medicalInfo.add(new MedicalInfo(5, MedicalInfo.Type.MEDICAL_CONDITION, "Cancer", "Stage 4"));

        return medicalInfo;
    }

    public static List<MedicalInfo> getAllergyData(){
        medicalInfo = new ArrayList<>();

        medicalInfo.add(new MedicalInfo(1, MedicalInfo.Type.ALLERGY, "Sulfate", "Rash and itchiness"));
        medicalInfo.add(new MedicalInfo(2, MedicalInfo.Type.ALLERGY, "Cat fur", "Rash and itchiness"));
        medicalInfo.add(new MedicalInfo(3, MedicalInfo.Type.ALLERGY, "Bee stings", "Severe, suffocation"));
        medicalInfo.add(new MedicalInfo(4, MedicalInfo.Type.ALLERGY, "Dust", "Rash and itchiness"));
        medicalInfo.add(new MedicalInfo(5, MedicalInfo.Type.ALLERGY, "Pollen", "Rash and itchiness"));

        return medicalInfo;
    }
}
