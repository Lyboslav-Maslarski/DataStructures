package vaccopsjava;

import java.util.*;

public class VaccOps implements IVaccOps {

    public VaccOps() {
    }

    public void addDoctor(Doctor d) {
    }

    public void addPatient(Doctor d, Patient p) {
    }

    public Collection<Doctor> getDoctors() {
        return null;
    }

    public Collection<Patient> getPatients() {
        return null;
    }

    public boolean exist(Doctor d) {
        return false;
    }

    public boolean exist(Patient p) {
        return false;
    }


    public Doctor removeDoctor(String name) {
        return null;
    }

    public void changeDoctor(Doctor from, Doctor to, Patient p) {
    }

    public Collection<Doctor> getDoctorsByPopularity(int populariry) {
        return null;
    }

    public Collection<Patient> getPatientsByTown(String town) {
        return null;
    }

    public Collection<Patient> getPatientsInAgeRange(int lo, int hi) {
        return null;
    }

    public Collection<Doctor> getDoctorsSortedByPatientsCountDescAndNameAsc() {
        return null;
    }

    public Collection<Patient> getPatientsSortedByDoctorsPopularityAscThenByHeightDescThenByAge() {
        return null;
    }

}
