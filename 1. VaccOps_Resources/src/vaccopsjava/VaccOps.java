package vaccopsjava;

import java.util.*;
import java.util.stream.Collectors;

public class VaccOps implements IVaccOps {
    Map<String, Doctor> doctorMap;
    Map<String, Patient> patientMap;
    Map<Patient, Doctor> patientWithDoctorMap;
    Map<Doctor, List<Patient>> doctorWithPatientsListMap;


    public VaccOps() {
        doctorMap = new TreeMap<>();
        patientMap = new HashMap<>();
        patientWithDoctorMap = new HashMap<>();
        doctorWithPatientsListMap = new HashMap<>();
    }

    public void addDoctor(Doctor d) {
        String name = d.name;
        if (doctorMap.containsKey(name)) {
            throw new IllegalArgumentException();
        }
        doctorMap.put(name, d);
        doctorWithPatientsListMap.put(d, new ArrayList<>());
    }

    public void addPatient(Doctor d, Patient p) {
        String name = d.name;
        if (!doctorMap.containsKey(name)||patientMap.containsKey(p.name)) {
            throw new IllegalArgumentException();
        }
        patientMap.put(p.name, p);
        patientWithDoctorMap.put(p, d);
        doctorWithPatientsListMap.get(d).add(p);
    }

    public Collection<Doctor> getDoctors() {
        return new ArrayList<>(doctorMap.values());
    }

    public Collection<Patient> getPatients() {
        return new ArrayList<>(patientMap.values());
    }

    public boolean exist(Doctor d) {
        return doctorMap.containsKey(d.name);
    }

    public boolean exist(Patient p) {
        return patientMap.containsKey(p.name);
    }


    public Doctor removeDoctor(String name) {
        if (!doctorMap.containsKey(name)) {
            throw new IllegalArgumentException();
        }
        Doctor doctor = doctorMap.remove(name);
        for (Patient patient : doctorWithPatientsListMap.get(doctor)) {
            patientMap.remove(patient.name);
            patientWithDoctorMap.remove(patient);
        }
        doctorWithPatientsListMap.remove(doctor);
        return doctor;
    }

    public void changeDoctor(Doctor from, Doctor to, Patient p) {
        if (!doctorMap.containsKey(from.name) || !doctorMap.containsKey(to.name) || !patientMap.containsKey(p.name)) {
            throw new IllegalArgumentException();
        }
        doctorWithPatientsListMap.get(from).remove(p);
        doctorWithPatientsListMap.get(to).add(p);
    }

    public Collection<Doctor> getDoctorsByPopularity(int populariry) {
        return doctorMap.values()
                .stream()
                .filter(doctor -> doctor.popularity == populariry)
                .collect(Collectors.toList());
    }

    public Collection<Patient> getPatientsByTown(String town) {
        return patientMap.values()
                .stream()
                .filter(patient -> patient.town.equals(town))
                .collect(Collectors.toList());
    }

    public Collection<Patient> getPatientsInAgeRange(int lo, int hi) {
        return patientMap.values()
                .stream()
                .filter(patient -> patient.age >= lo && patient.age <= hi)
                .collect(Collectors.toList());
    }

    public Collection<Doctor> getDoctorsSortedByPatientsCountDescAndNameAsc() {
        return doctorMap.values()
                .stream()
                .sorted((f, s) -> Integer.compare(doctorWithPatientsListMap.get(s).size(), doctorWithPatientsListMap.get(f).size()))
                .collect(Collectors.toList());
    }

    public Collection<Patient> getPatientsSortedByDoctorsPopularityAscThenByHeightDescThenByAge() {
        return patientWithDoctorMap.entrySet()
                .stream()
                .sorted((f, s) -> {
                    int result = f.getValue().popularity-s.getValue().popularity;
                    if (result == 0){
                        result = s.getKey().height - f.getKey().height;
                        if (result == 0){
                            result = f.getKey().age - s.getKey().age;
                        }
                    }
                    return result;
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
